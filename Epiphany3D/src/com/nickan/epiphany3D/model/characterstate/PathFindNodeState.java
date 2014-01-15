package com.nickan.epiphany3D.model.characterstate;

import java.util.List;

import com.badlogic.gdx.Gdx;
import com.nickan.epiphany3D.model.Character;
import com.nickan.epiphany3D.model.Character.Action;
import com.nickan.epiphany3D.model.Character.State;
import com.nickan.epiphany3D.model.messagingsystem.EntityManager;
import com.nickan.epiphany3D.model.messagingsystem.Telegram;
import com.nickan.epiphany3D.model.state.BaseState;
import com.nickan.epiphany3D.view.gamescreenview.World;
import com.nickan.framework1_0.pathfinder1_0.Node;

public class PathFindNodeState implements BaseState<Character> {
	private static BaseState<Character> instance = new PathFindNodeState();

	@Override
	public void start(Character entity) {
		// TODO Auto-generated method stub
		entity.setAction(Action.RUNNING);

		//...
		System.out.println("Entering path find");
		entity.setState(State.PATHFINDING);
	}

	@Override
	public void update(Character entity) {
		if (!entity.isMoving()) {
			
			// Don't process the rest if there is already pending telegram (to be changed later...)
			if (entity.pendingTelegram != null) {
				switch (entity.pendingTelegram.msg) {
				case TARGETED_RESPONSE:
					entity.setEnemyId(entity.pendingTelegram.senderId);
					entity.changeState(AttackState.getInstance());
					break;
				default:
					break;
				}
				return;
			}
			
			// To be changed later, what if the target node is not walkable, he will never rest!!! :(
			if (!entity.getTargetNode().isSame(entity.getCurrentNode()))
				pathFinding(entity);
			
			if (!entity.isPathEmpty()) {

				Node nextNode = entity.getNextNode();

				World.occupiedNodes.removeValue(nextNode, true);
				entity.identifyOccupiedNode();

				if (World.occupiedNodes.contains(nextNode, true)) {
					pathFinding(entity);
					entity.identifyOccupiedNode();

					//...
					System.out.println("Is in occupied nodes");
				}
				World.occupiedNodes.add(nextNode);

				entity.identifyNextMove();
				entity.faceCoordinate(nextNode.x, nextNode.y);
			} else {
				// Done path finding (Needs to be updated later...
				entity.changeState(IdleState.getInstance());
			}

		}

		entity.move(Gdx.graphics.getDeltaTime());
	}

	private void pathFinding(Character entity) {
		entity.initializePath(entity.getTargetNode());
		List<Node> shortestPath = World.pathFinder.getShortestPath(entity.getCurrentNode(), entity.getTargetNode());
		entity.setShortestPath(shortestPath);
	}

	@Override
	public void exit(Character entity) {
		// TODO Auto-generated method stub

		//...
		System.out.println("Exiting path find");
	}

	public static BaseState<Character> getInstance() {
		// TODO Auto-generated method stub
		return instance;
	}

	@Override
	public boolean onMessage(Telegram telegram) {
		Character receiver = getCharacter(telegram.receiverId);
		switch (telegram.msg) {
		case LOCATION_RESPONSE:
			receiver.pendingTelegram = telegram;
			return true;
		default:
			break;
		}
		return false;
	}

	private Character getCharacter(int charId) {
		return (Character) EntityManager.getInstance().getEntity(charId);
	}

}
