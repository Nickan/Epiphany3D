package com.nickan.epiphany3D.model.characterstate;

import com.nickan.epiphany3D.model.Character;
import com.nickan.epiphany3D.model.Character.Action;
import com.nickan.epiphany3D.model.Character.State;
import com.nickan.epiphany3D.model.messagingsystem.EntityManager;
import com.nickan.epiphany3D.model.messagingsystem.Telegram;
import com.nickan.epiphany3D.model.state.BaseState;

public class IdleState implements BaseState<Character> {
	private static BaseState<Character> instance = new IdleState();

	@Override
	public void start(Character entity) {
		entity.setAction(Action.STANDING);

		//...
		System.out.println("Enter idle state");
		entity.setState(State.IDLE);
	}

	@Override
	public void update(Character entity) {
		//...

	}

	@Override
	public void exit(Character entity) {
		// TODO Auto-generated method stub

	}

	public static BaseState<Character> getInstance() {
		// TODO Auto-generated method stub
		return instance;
	}

	@Override
	public boolean onMessage(Telegram telegram) {
		Character receiver = getCharacter(telegram.receiverId);
		return false;
	}

	private Character getCharacter(int charId) {
		return (Character) EntityManager.getInstance().getEntity(charId);
	}

}
