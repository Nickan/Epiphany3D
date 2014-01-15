package com.nickan.epiphany3D.model.state;

import com.nickan.epiphany3D.model.messagingsystem.Telegram;


public class StateMachine<OwnerType> {
	OwnerType owner;

	BaseState<OwnerType> currentState;
	BaseState<OwnerType> globalState = null;
	BaseState<OwnerType> previousState = null;

	public StateMachine(OwnerType owner) {
		this.owner = owner;
		this.currentState = null;
		this.globalState = null;
	}

	public void update() {
		if (owner == null)
			return;

		if (globalState != null)
			globalState.update(owner);

		if (currentState != null)
			currentState.update(owner);
	}

	public boolean handleMessage(Telegram telegram) {
		if (currentState.onMessage(telegram))
			return true;

		if (globalState.onMessage(telegram))
			return true;

		return false;
	}

	public void changeState(BaseState<OwnerType> state) {
		previousState = currentState;

		if (currentState != null)
			currentState.exit(owner);

		currentState = state;
		currentState.start(owner);
	}

	public void setGlobalState(BaseState<OwnerType> state) {
		this.globalState = state;
	}

	public void revertToPreviousState() {
		changeState(previousState);
	}
}
