package com.nickan.epiphany3D.model.characterstate;

import java.util.List;

import com.badlogic.gdx.Gdx;
import com.nickan.epiphany3D.model.Character;
import com.nickan.epiphany3D.model.Character.Action;
import com.nickan.epiphany3D.model.Character.State;
import com.nickan.epiphany3D.model.messagingsystem.Telegram;
import com.nickan.epiphany3D.model.state.BaseState;
import com.nickan.epiphany3D.view.gamescreenview.World;
import com.nickan.framework1_0.pathfinder1_0.Node;
import com.nickan.framework1_0.pathfinder1_0.Node.NodeType;

public class AttackState implements BaseState<Character> {
	private static final AttackState instance = new AttackState();

	private AttackState() { }

	@Override
	public void start(Character entity) {
		entity.deliveringAttack = false;
		entity.resetAttack();

		//...
//		System.out.println("Enter attack state");
		entity.setState(State.ATTACKING);
	}

	@Override
	public void update(Character entity) {
		if (!entity.isMoving()) {

			if (entity.getState() == State.PATHFINDING) {
				entity.changeState(PathFindNodeState.getInstance());
				return;
			}

			if (entity.deliveringAttack) {
				deliveringAttack(entity);
			} else {
				notAttacking(entity);
			}

		}
		
		entity.move(Gdx.graphics.getDeltaTime());
	}

	private void notAttacking(Character entity) {
		// Check to see if the enemy to be attacked is within attack range, then don't process the rest
		if (entity.targetIsInAttackRange()) {
			entity.setAction(Action.ATTACKING);
			entity.deliveringAttack = true;
			entity.resetAttack();

			// Don't process the rest if the target is now set to be attacked
			return;
		} else {
			pathFindEnemyNode(entity);
			
			if (!entity.isPathEmpty()) {
				Node nextNode = entity.getNextNode();


				World.occupiedNodes.removeValue(nextNode, true);
				entity.identifyOccupiedNode();

				if (World.occupiedNodes.contains(nextNode, true)) {
					pathFindEnemyNode(entity);;
					entity.identifyOccupiedNode();
				}
				World.occupiedNodes.add(nextNode);

				entity.identifyNextMove();
				entity.setAction(Action.RUNNING);
				entity.faceCoordinate(entity.getVelocity().x, entity.getVelocity().z);
			}
		}
	}

	private void deliveringAttack(Character entity) {
		entity.faceCoordinate(entity.getEnemyNode().x, entity.getEnemyNode().y);
		if (entity.targetHit())
			entity.deliveringAttack = false;
	}

	private void pathFindEnemyNode(Character entity) {
//		System.out.println("path find enemy node");

		Node enemyNode = entity.getEnemyNode();
		entity.initializePath(enemyNode);
		Node targetNode = World.pathFinder.nodes[enemyNode.y][enemyNode.x];

		// Set as walkable
		targetNode.type = NodeType.WALKABLE;
		List<Node> shortestPath = World.pathFinder.getShortestPath(entity.getCurrentNode(), targetNode);

		// Remove the target node from the shortest path list
		shortestPath.remove(targetNode);
		entity.setShortestPath(shortestPath);
	}


	@Override
	public void exit(Character entity) {
		entity.deliveringAttack = false;
		entity.resetAttack();

		//...
//		System.out.println("Exit attacking");
	}

	@Override
	public boolean onMessage(Telegram telegram) {
//		Character receiver = (Character) EntityManager.getInstance().getEntity(telegram.receiverId);
		return false;
	}

	public static final BaseState<Character> getInstance() {
		return instance;
	}

}
