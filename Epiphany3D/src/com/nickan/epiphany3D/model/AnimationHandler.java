package com.nickan.epiphany3D.model;

import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.utils.AnimationController;
import com.badlogic.gdx.graphics.g3d.utils.AnimationController.AnimationDesc;
import com.badlogic.gdx.graphics.g3d.utils.AnimationController.AnimationListener;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;
import com.nickan.epiphany3D.model.Character.Action;

/**
 * Handles the standing(idle), running and attacking animations. The frame number should be overridden according to the individual
 * frame numbers of the animations as that will be set to be played in one second (My basis for synchronization, might be changed later as
 * these are all for learning purposes :D).
 * @author Nickan
 *
 */
public class AnimationHandler {
	/** Sets up the right animation to be shown */
	private enum AnimationStatus { NONE, STANDING, RUNNING, ATTACKING }
	private AnimationStatus aniStat = AnimationStatus.NONE;

	/** For calculation of the matrix, I might change this later */
	private final static Matrix4 aniTempMatrix = new Matrix4();

	private Action storedAction;

	private AnimationController aniController;

	/** The time to start playing the animation to synchronize it */
	private float attackPlayTime;

	/** Default play speed, maybe based on blender's play speed. No reason to change or modify it. I will work based on it **/
	private static final float DEFAULT_FPS = 24.0f;

	/** Defines the animation play time to be one second */
	private final float attackAnimationPlaySpeed;
	private final float standingPlaySpeed;
	private final float runningPlaySpeed;

	/** Make the scale global for now... To be changed later if needed */
	public float aniScale = 1f;

	private final static float DEFAULT_ATTACK_SPEED = 1.0f;
	public float attackPlaySpeed;
	private boolean firstAttack = true;
	private boolean attackUpdated = false;
	private ModelInstance instance;

	public AnimationHandler(ModelInstance instance, float standingFrameNum, float runningFrameNum, float attackingFrameNum) {
		this.instance = instance;
		aniController = new AnimationController(instance);

		standingPlaySpeed = standingFrameNum / DEFAULT_FPS;
		runningPlaySpeed = runningFrameNum / DEFAULT_FPS;
		attackAnimationPlaySpeed = attackingFrameNum / DEFAULT_FPS;

		aniController.setAnimation("Standing", -1, standingPlaySpeed, null);
	}

	/**
	 *  Handles the animation status
	 */
	void checkAnimationStatus(Action action) {
		if (actionHasChanged(action)) {
			switch (aniStat) {
			case STANDING:
				aniController.setAnimation("Standing", -1, standingPlaySpeed, null);
				break;
			case RUNNING:
				aniController.setAnimation("Running", -1, runningPlaySpeed, null);
				firstAttack = true;
				break;
			case ATTACKING:
				aniController.setAnimation("Attacking");
				aniController.update(1000);
				firstAttack = true;
				attackUpdated = false;
				break;
			default:
				break;
			}
		}
	}

	/**
	 *  Updates the animation
	 */
	public void updateAnimation(Vector3 position, Vector3 rotation, float attackTime, float delta) {
		// Not updating the animation controller once it is set to be killed
		if (killed)
			return;

		if (aniStat == AnimationStatus.ATTACKING)
			handleAttackUpdate(attackTime);

		updateAnimationMatrix(position, rotation, aniController.target.transform);
		aniController.update(delta);
	}


	/**
	 * Handles the start of playing attack animation.
	 * @param attackTime - The attack timer of the character
	 */
	private void handleAttackUpdate(float attackTime) {
		// Should know if that's the first time the animation will be played, to show believable attack animation
		if (firstAttack) {
			if (attackTime >= attackPlayTime) {
				firstAttack = false;
			}
		} else {
			updateContinousAttack(attackTime);
		}

	}

	/**
	 * Will continue updating the attack animation after the attack time goes back to zero, to show the sword or any weapon
	 * to be brought back from the attack stance or starting attack stance.
	 * @param attackTime - The attack timer of the character
	 */
	private void updateContinousAttack(float attackTime) {
		if (!attackUpdated) {
			if (attackTime >= attackPlayTime) {
				attackUpdated = true;
				playAttackAnimation();
			}
		} else {
			if (attackTime < attackPlayTime) {
				attackUpdated = false;
			}
		}
	}

	private void playAttackAnimation() {
		// Finish the currently updating attack to reset it
	//	aniController.update(1000);
		aniController.setAnimation("Standing");
		aniController.update(1000);
		aniController.setAnimation("Attacking", 1, attackPlaySpeed, null);
	//	aniController.animate("Attacking", 1, attackPlaySpeed, null, 0);
		
		//...
		System.out.println("Attack!!!");
	}

	/**
	 * Set the attack counter value and animation to make them synchronize to attack delay of the character.
	 * If the attack delay is greater than one, the difference of one to the attack delay will just make the animation wait to attack.
	 * otherwise the delay time to play the attack animation is just half the attack delay.
	 */
	void setAttackAnimationSpeed(float attackDelay) {
		float attackTimeToLandHit = attackDelay / 2f;

		/*
		 * Set the default attack delay of the animation to one second, to prevent boring very slow attack animation,
		 * but if the attack delay is lower than one second, set the attack delay of the animation according to it.
		 */
		if (attackDelay > DEFAULT_ATTACK_SPEED) {	
			attackPlayTime = attackDelay - DEFAULT_ATTACK_SPEED / 2;
			attackPlaySpeed = attackAnimationPlaySpeed;
		} else {
			attackPlayTime = attackTimeToLandHit;
			attackPlaySpeed = attackAnimationPlaySpeed / attackDelay;
		}
	}

	/**
	 * Updates and manipulates the ModelInstance's matrix to be passed here
	 *
	 * @param position - Position of the character
	 * @param rotation - Rotation of the character
	 * @param aniMatrix - The matrix from ModelInstance
	 */
	private void updateAnimationMatrix(Vector3 position, Vector3 rotation, Matrix4 aniMatrix) {
		// Set the position
		aniMatrix.idt();
		aniMatrix.translate(position.x, position.y, position.z);

		// Set the rotation
		aniTempMatrix.idt();

		aniTempMatrix.rotate(Vector3.Y, rotation.y + 90);

		aniMatrix.mul(aniTempMatrix);

		// Set the scale
		aniTempMatrix.idt();
		aniTempMatrix.scl(aniScale);

		aniMatrix.mul(aniTempMatrix);
	}

	/**
	 * Check if there is changed of current action of the entity and automatically set the action and the corresponding animation status
	 * @return
	 */
	private boolean actionHasChanged(Action action) {
		if (action != storedAction) {
			storedAction = action;
			switch (action) {
			case STANDING:
				aniStat = AnimationStatus.STANDING;
				break;
			case RUNNING:
				aniStat = AnimationStatus.RUNNING;
				break;
			case ATTACKING:
				aniStat = AnimationStatus.ATTACKING;
				break;
			default:
				break;
			}
			return true;
		}
		return false;
	}

	public void playDead() {	
		aniController.setAnimation("Stumbling", 1, 1, new AnimationListener() {

			@Override
			public void onEnd(AnimationDesc animation) {
				// TODO Auto-generated method stub
				killed = true;
				float timePerFrame = 1f / DEFAULT_FPS;
				aniController.update(1000);
				aniController.animate("Stumbling", 0);
				aniController.update(timePerFrame * 14);
			}

			@Override
			public void onLoop(AnimationDesc animation) {
				// TODO Auto-generated method stub
			}
			
		});
	}
	
	public ModelInstance getModelInstance() {
		return instance;
	}
	
	boolean killed = false;
}
