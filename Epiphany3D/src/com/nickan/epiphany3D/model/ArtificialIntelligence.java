package com.nickan.epiphany3D.model;

import com.badlogic.gdx.math.Vector3;
import com.nickan.epiphany3D.model.aibehavior.EnemyState;
import com.nickan.epiphany3D.model.aibehavior.FriendlyState;
import com.nickan.epiphany3D.model.aibehavior.GlobalBehaviorState;
import com.nickan.epiphany3D.model.aibehavior.NeutralState;
import com.nickan.epiphany3D.model.state.StateMachine;


public class ArtificialIntelligence extends Character {
	public boolean hasTarget = false;

	private StateMachine<ArtificialIntelligence> aiBehaviourStateMachine;

	public ArtificialIntelligence(Vector3 position, Vector3 rotation, Vector3 velocity,
			float speed) {
		super(position, rotation, velocity, speed);

		aiBehaviourStateMachine = new StateMachine<ArtificialIntelligence>(this);
		aiBehaviourStateMachine.setGlobalState(GlobalBehaviorState.getInstance());
		//...
		setEnemy();
	}

	public void update(float delta) {
		// Don't process any behavior of the AI if it is dead
		if (isAlive())
			aiBehaviourStateMachine.update();

		super.update(delta);
	}
	
	public void setEnemy() {
		aiBehaviourStateMachine.changeState(EnemyState.getInstance());
	}
	
	public void setNeutral() {
		aiBehaviourStateMachine.changeState(NeutralState.getInstance());
	}
	
	public void setFriendly() {
		aiBehaviourStateMachine.changeState(FriendlyState.getInstance());
	}

}
