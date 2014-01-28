package com.nickan.epiphany3D.model;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.math.Vector3;
import com.nickan.epiphany3D.model.characterstate.GlobalState;
import com.nickan.epiphany3D.model.characterstate.IdleState;
import com.nickan.epiphany3D.model.messagingsystem.EntityManager;
import com.nickan.epiphany3D.model.messagingsystem.MessageDispatcher;
import com.nickan.epiphany3D.model.messagingsystem.Telegram;
import com.nickan.epiphany3D.model.messagingsystem.Telegram.Message;
import com.nickan.epiphany3D.model.state.BaseState;
import com.nickan.epiphany3D.model.state.StateMachine;
import com.nickan.epiphany3D.view.gamescreenview.subview.AttackDamageRenderer;
import com.nickan.framework1_0.pathfinder1_0.Node;

/**
 * Handles the universal behavior of character entities (Enemy, player, player's allies, etc)
 * @author Nickan
 */
public class Character extends MoveableEntity {
	public String name = "Unnamed";

	// Temporary values, might be changed later
	public AttackHandler attackHandler;
	public AnimationHandler aniHandler = null;
	public StatisticsHandler statsHandler;

	// Making the status of the movable entity to be more readable
	public enum Action { STANDING, RUNNING, ATTACKING }

	public enum State { IDLE, PATHFINDING, ATTACKING }
	private State state = State.IDLE;

	/** Needs in path finding */
	public boolean targetNodeIsSet = false;

	private StateMachine<Character> stateMachine;
	private Node enemyNode;
	private int enemyId;
	
	public Telegram pendingTelegram = null;
	private static final int allianceNumListSize = 10;
	private static final int enemiesNumListSize = 10;
	private int[] allianceNumList = new int[allianceNumListSize];
	private int[] enemiesNumList = new int[enemiesNumListSize];
	
	public enum Allegiance { ENEMY, NEUTRAL, ALLY }

	/**
	 * For the attacking lock of the character, when already attacking, it should first land the attack before trying to chase
	 * the target again.
	 */
	public boolean deliveringAttack = false;

	public Character(Vector3 position, Vector3 rotation, Vector3 velocity,
			float speed) {
		super(position, rotation, velocity, speed);
		// TODO Auto-generated constructor stub

		attackHandler = new AttackHandler();
		statsHandler = new StatisticsHandler();

		stateMachine = new StateMachine<Character>(this);
		stateMachine.changeState(IdleState.getInstance());
		stateMachine.setGlobalState(GlobalState.getInstance());
		
		for (int i = 0; i < allianceNumListSize; ++i) {
			allianceNumList[i] = -1;
		}
		
		for (int i = 0; i < enemiesNumListSize; ++i) {
			enemiesNumList[i] = -1;
		}
		
		enemyId = -1;
	}

	/**
	 * Sets the character to locate the target location being passed to it. Will never do anything when the target is just the same as
	 * the current node or position
	 *
	 * @param targetX
	 * @param targetY
	 */
	public void pathFindWalkableNode(int targetX, int targetY) {
		targetNode.set(targetX, targetY);
		MessageDispatcher.getInstance().dispatchMessage(-1, id, 0, Message.PATH_FIND_NODE, targetNode);
	}

	public void setEnemyId(int enemyId) {
		this.enemyId = enemyId;
		
		if (enemyId == -1) {
			enemyNode = null;
			return;
		}
		Character enemy = (Character) EntityManager.getInstance().getEntity(enemyId);
		this.enemyNode = enemy.getNextNode();
	}
	
	public int getEnemyId() {
		return enemyId;
	}


	protected void update(float delta) {
		stateMachine.update();
		super.update(delta);

		if (aniHandler != null) {
			aniHandler.updateAnimation(position, rotation, attackHandler.attackTimer, delta);
		}
	}

	/**
	 * Should only be called when the enemy is in attack range
	 */
	public boolean targetHit() {
		Character enemy = (Character) EntityManager.getInstance().getEntity(enemyId);
		float hitChance = (statsHandler.getAttackHit() / enemy.statsHandler.getAttackHit()) * 100;
		
		switch (attackHandler.getAttackStatus(hitChance, statsHandler.attackDelay, Gdx.graphics.getDeltaTime())) {
		case HIT:
			attackHit();
			return true;
		case MISS:
			attackMiss();
			return true;
		case NONE:
			break;
		}
		return false;
	}


	private void attackHit() {
		//...
//		System.out.println("Attack hit");
		MessageDispatcher.getInstance().dispatchMessage(this.id, enemyId, 0, Message.ATTACKED_BY_SENDER, statsHandler.getAttackDmg());
	}
	
	public void applyDamage(int damage) {
		statsHandler.applyDamage(damage);
		
		//...
		System.out.println("Current hp: " + statsHandler.currentHp);
	}
	
	public boolean isAlive() {
		return statsHandler.isAlive();
	}

	private void attackMiss() {
		Character enemy = (Character) EntityManager.getInstance().getEntity(enemyId);
		AttackDamageRenderer.getInstance().addAttackDamageToScreen(enemy.getPosition(), 0);
	}

	@Override
	public boolean handleMessage(Telegram telegram) {
		return stateMachine.handleMessage(telegram);
	}

	public void changeState(BaseState<Character> state) {
		stateMachine.changeState(state);
	}

	public void setAction(Action action) {
		if (aniHandler != null) {
			aniHandler.checkAnimationStatus(action);
		}
	}

	public boolean targetIsInAttackRange() {
		return isInRange(enemyNode, statsHandler.attackRange);
	}

	public boolean targetIsInSightRange() {
		return isInRange(enemyNode, statsHandler.sightRange);
	}

	public boolean isInSightRange(Node targetNode) {
		return isInRange(targetNode, statsHandler.sightRange);
	}

	public boolean isInRange(Node node, int range) {
		return (Math.abs(currentNode.x - node.x) <= range &&
				Math.abs(currentNode.y - node.y) <= range) ? true : false;
	}

	public void setModelInstance(ModelInstance instance, int standingFrameNum, int runningFrameNum, int attackingFrameNum) {
		aniHandler = new AnimationHandler(instance, standingFrameNum, runningFrameNum, attackingFrameNum);
		aniHandler.setAttackAnimationSpeed(statsHandler.attackDelay);
	}

	public void resetAttack() {
		attackHandler.reset();
	}


	public Node getEnemyNode() {
		return enemyNode;
	}

	public void setEnemyNode(Node enemyNode) {
		this.enemyNode = enemyNode;
	}


	public State getState() {
		return state;
	}

	public void setState(State state) {
		this.state = state;
	}
	
	// To be implemented later...
	public Allegiance getAllegiance(int teamNum) {
		if (isInArray(allianceNumList, teamNum)) {
			return Allegiance.ALLY;
		}
		
		if (isInArray(enemiesNumList, teamNum)) {
			return Allegiance.ENEMY;
		}
		return Allegiance.NEUTRAL;
	}
	
	public boolean isInArray(int[] array, int num) {
		for (int i = 0; i < array.length; ++i) {
			if (array[i] == num) {
				return true;
			}
		}
		return false;
	}
	
	public void setKilled() {
		aniHandler.playDead();
	}

}
