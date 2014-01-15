package com.nickan.epiphany3D.model.aibehavior;

import com.nickan.epiphany3D.model.Character;
import com.nickan.epiphany3D.model.ArtificialIntelligence;
import com.nickan.epiphany3D.model.Character.State;
import com.nickan.epiphany3D.model.characterstate.IdleState;
import com.nickan.epiphany3D.model.messagingsystem.EntityManager;
import com.nickan.epiphany3D.model.messagingsystem.MessageDispatcher;
import com.nickan.epiphany3D.model.messagingsystem.Telegram;
import com.nickan.epiphany3D.model.messagingsystem.Telegram.Message;
import com.nickan.epiphany3D.model.state.BaseState;

public class EnemyState implements BaseState<ArtificialIntelligence> {
	private static final EnemyState instance = new EnemyState();

	private EnemyState() { }
	
	@Override
	public void start(ArtificialIntelligence entity) {

	}

	@Override
	public void update(ArtificialIntelligence entity) {
		if (!entity.isMoving()) {
			
			if (entity.getEnemyId() != -1) {
				handleAttack(entity);
			}
		}
	}
	
	private void handleAttack(ArtificialIntelligence entity) {
		if (entity.isInSightRange(entity.getEnemyNode())) {
			
			// Just set it if is not in attacking state
			if (entity.getState() != State.ATTACKING) {
				MessageDispatcher.getInstance().dispatchMessage(entity.getId(), entity.getEnemyId(), 0, 
						Message.TARGETED_BY_SENDER, entity.getNextNode());
			}
		} else {

			// Set the state to idle if it is still not in idle
			if (entity.getState() != State.IDLE)
				entity.changeState(IdleState.getInstance());
		}
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

	public static final EnemyState getInstance() {
		return instance;
	}
	
	private Character getCharacter(int charId) {
		return (Character) EntityManager.getInstance().getEntity(charId);
	}

}
