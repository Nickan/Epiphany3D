package com.nickan.epiphany3D.model.characterstate;

import com.nickan.epiphany3D.model.Character;
import com.nickan.epiphany3D.model.messagingsystem.Telegram;
import com.nickan.epiphany3D.model.state.BaseState;
import com.nickan.epiphany3D.view.gamescreenview.World;

public class KilledState implements BaseState<Character> {
	private static final KilledState instance = new KilledState();

	private KilledState() { }
	
	@Override
	public void start(Character entity) {
		// TODO Auto-generated method stub
		entity.setKilled();
		World.occupiedNodes.removeValue(entity.getNextNode(), true);
	}

	@Override
	public void update(Character entity) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void exit(Character entity) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean onMessage(Telegram telegram) {
		// TODO Auto-generated method stub
		return false;
	}
	
	public static final BaseState<Character> getInstance() {
		return instance;
	}

}
