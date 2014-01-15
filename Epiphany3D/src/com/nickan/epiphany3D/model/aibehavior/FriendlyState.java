package com.nickan.epiphany3D.model.aibehavior;

import com.nickan.epiphany3D.model.ArtificialIntelligence;
import com.nickan.epiphany3D.model.messagingsystem.Telegram;
import com.nickan.epiphany3D.model.state.BaseState;

public class FriendlyState implements BaseState<ArtificialIntelligence> {
	private static final FriendlyState instance = new FriendlyState();

	@Override
	public void start(ArtificialIntelligence entity) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void update(ArtificialIntelligence entity) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void exit(ArtificialIntelligence entity) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean onMessage(Telegram telegram) {
		// TODO Auto-generated method stub
		return false;
	}
	
	public static final BaseState<ArtificialIntelligence> getInstance() {
		return instance;
	}

}
