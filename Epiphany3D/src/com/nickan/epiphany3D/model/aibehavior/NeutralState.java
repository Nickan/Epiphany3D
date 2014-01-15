package com.nickan.epiphany3D.model.aibehavior;

import com.nickan.epiphany3D.model.ArtificialIntelligence;
import com.nickan.epiphany3D.model.messagingsystem.Telegram;
import com.nickan.epiphany3D.model.state.BaseState;

public class NeutralState implements BaseState<ArtificialIntelligence> {
	private static final NeutralState instance = new NeutralState();

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

	public static final NeutralState getInstance() {
		return instance;
	}

}
