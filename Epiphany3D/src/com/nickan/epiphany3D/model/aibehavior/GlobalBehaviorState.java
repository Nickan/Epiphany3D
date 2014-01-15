package com.nickan.epiphany3D.model.aibehavior;

import com.nickan.epiphany3D.model.ArtificialIntelligence;
import com.nickan.epiphany3D.model.messagingsystem.Telegram;
import com.nickan.epiphany3D.model.state.BaseState;

public class GlobalBehaviorState implements BaseState<ArtificialIntelligence> {
	private static final GlobalBehaviorState instance = new GlobalBehaviorState();

	private GlobalBehaviorState() { }

	@Override
	public void start(ArtificialIntelligence owner) {
		// TODO Auto-generated method stub

	}

	@Override
	public void update(ArtificialIntelligence owner) {
		// TODO Auto-generated method stub

	}

	@Override
	public void exit(ArtificialIntelligence owner) {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean onMessage(Telegram telegram) {
		// TODO Auto-generated method stub
		return false;
	}

	public static final GlobalBehaviorState getInstance() {
		return instance;
	}
}
