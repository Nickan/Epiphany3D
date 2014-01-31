package com.nickan.epiphany3D.model;


import com.nickan.framework1_0.math.RandomNumber;


/**
 * Separate the concerns and handles the attack functionality of the Character class
 * @author Nickan
 *
 */
public class AttackHandler {
	float attackTimer = 0;
	/** For the indication if the attack has hit the target player */
	public enum AttackStatus { HIT, MISS, NONE };

	public AttackHandler() { }

	/**
	 * Should be called every frame if is in attack state, returns the status of the attack
	 *
	 * @param hitChance		- The percentage the attack will land at the target
	 * @param attackDelay	- Delay between the attack (Attack delta time)
	 * @param delta			- Delta time
	 * @return				- The status of the attack
	 */
	AttackStatus getAttackStatus(float hitChance, float attackDelay, float delta) {
		attackTimer += delta;

		if (attackTimer >= attackDelay) {
			attackTimer -= attackDelay;

			if (attackHasHit(hitChance)) {
				return AttackStatus.HIT;
			} else {
				return AttackStatus.MISS;
			}
		}

		return AttackStatus.NONE;
	}

	/**
	 *
	 * @param hitChance		- The percentage the attack will land at the target
	 * @return				- If the attack has hit the target or not
	 */
	private boolean attackHasHit(float hitChance) {
		int min = 0;
		int max = 100;
		return (RandomNumber.getRandomInt(min, max) <= hitChance);
	}

	void reset() {
		attackTimer = 0;
	}

}
