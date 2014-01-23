package com.nickan.epiphany3D.model.characterstate;

import com.nickan.epiphany3D.model.Character;
import com.nickan.epiphany3D.model.messagingsystem.EntityManager;
import com.nickan.epiphany3D.model.messagingsystem.MessageDispatcher;
import com.nickan.epiphany3D.model.messagingsystem.Telegram;
import com.nickan.epiphany3D.model.messagingsystem.Telegram.Message;
import com.nickan.epiphany3D.model.state.BaseState;
import com.nickan.epiphany3D.view.gamescreenview.subview.AttackDamageRenderer;

public class GlobalState implements BaseState<Character> {
	private static BaseState<Character> instance = new GlobalState();

	@Override
	public void start(Character entity) {
		// TODO Auto-generated method stub

	}

	@Override
	public void update(Character entity) {
		// TODO Auto-generated method stub

	}

	@Override
	public void exit(Character entity) {
		// TODO Auto-generated method stub

	}

	public static final BaseState<Character> getInstance() {
		return instance;
	}

	@Override
	public boolean onMessage(Telegram telegram) {
		Character receiver = getCharacter(telegram.receiverId);
		
		switch (telegram.msg) {
		case ATTACKED_BY_SENDER:
			Character sender = getCharacter(telegram.senderId);
			float tempNum = (Float) telegram.extraInfo;
			receiver.applyDamage((int) tempNum);
			AttackDamageRenderer.getInstance().addAttackDamageToScreen(receiver.getPosition(), receiver.statsHandler.getHpDamage());
			
			if (!receiver.isAlive()) {
				receiver.changeState(KilledState.getInstance());
				sender.setEnemyId(-1);
				sender.changeState(IdleState.getInstance());
			}
			
			return true;
		case LOCATION_REQUEST:
			MessageDispatcher.getInstance().dispatchMessage(telegram.receiverId, telegram.senderId, 0, 
					Message.LOCATION_RESPONSE, receiver.getNextNode());
			return true;
		case TARGETED_BY_SENDER:
			MessageDispatcher.getInstance().dispatchMessage(telegram.receiverId, telegram.senderId, 0, 
					Message.TARGETED_RESPONSE, null);
			return true;
		case TARGETED_RESPONSE:
			receiver.setEnemyId(telegram.senderId);
			receiver.changeState(AttackState.getInstance());
			return true;
		case PATH_FIND_NODE:
			receiver.changeState(PathFindNodeState.getInstance());
			return true;
		default:
			return false;
		}
		
	}
	
	private Character getCharacter(int charId) {
		return (Character) EntityManager.getInstance().getEntity(charId);
	}
}
