package com.nickan.epiphany3D.model;

/**
 * Takes care of the statistics of the character class
 * @author Nickan
 *
 */
public class StatisticsHandler {
	public int level = 1;
	public float experience = 0;
	public int remainingStatusPoints = 10;

	public float hp;
	public float mp;

	// The bases fields will be used for balancing (if that will happen)
	public float baseStr = 1;
	public float baseDex = 1;
	public float baseVit = 1;
	public float baseAgi = 1;
	public float baseWis = 1;

	public float totalStr;
	public float totalDex;
	public float totalVit;
	public float totalAgi;
	public float totalWis;


	// Sub status
	public int sightRange = 5;
	public int attackRange = 1;

	public float baseAtkDmg = 0;
	public float baseAtkSpd = 0;
	public float baseHit = 0;
	public float baseAvoid = 0;
	public float baseDef = 0;
	public float baseCrit = 0;
	public float baseFullHp = 100;
	public float baseFullMp = 100;

	public float totalAtkDmg;
	public float totalAtkSpd;
	public float totalHit;
	public float totalAvoid;
	public float totalDef;
	public float totalCrit;
	public float totalFullHp;
	public float totalFullMp;

	float hitChance = 80;

	public float attackDelay = 1.5f;
	
	private int hpDamage;

	public StatisticsHandler() {
		baseStr = 10;
		baseDex = 10;
		baseVit = 10;
		baseAgi = 100;
		baseWis = 10;
		
		resetStats();
		initializeStatus();
		applyFinalStats();
	}

	public StatisticsHandler(float baseStr, float baseDex, float baseVit, float baseAgi, float baseWis) {
		this.baseStr = baseStr;
		this.baseDex = baseDex;
		this.baseVit = baseVit;
		this.baseAgi = baseAgi;
		this.baseWis = baseWis;
		
		resetStats();
		initializeStatus();
		applyFinalStats();
	}
	
	/**
	 * For testing, making the character a bit more buff
	 */
	public void whosYourDaddy() {
		baseStr = 100;
		baseDex = 100;
		baseVit = 100;
		baseAgi = 100;
		baseWis = 100;
		resetStats();
		initializeStatus();
		applyFinalStats();
	}

	/**
	 * The default base values of the base statistics
	 */
	protected void initializeStatus() {
		baseAtkDmg = 10;
		baseAtkSpd = 10;
		baseHit = 10;
		baseAvoid = 10;
		baseDef = 10;
		baseCrit = 10;
		
		hp = totalVit * 3;
		mp = totalWis * 3;
	}

	/**
	 * Should be called prior to equipping a weapon, to apply bonuses correctly
	 */
	void resetStats() {
		totalStr = baseStr;
		totalAgi = baseAgi;
		totalDex = baseDex;
		totalVit = baseVit;
		totalWis = baseWis;

		totalAtkDmg = baseAtkDmg;
		totalAtkSpd = baseAtkSpd;
		totalHit = baseHit;
		totalAvoid = baseAvoid;
		totalDef = baseDef;
		totalCrit = baseCrit;
		totalFullHp = hp = baseFullHp + (baseVit * 3);
		totalFullMp = mp = baseFullMp;
	}

	/**
	 * Finalizing the total statistics bonuses
	 */
	void applyFinalStats() {
		totalAtkDmg += totalStr;
		totalHit += totalDex;
		totalAvoid += totalAgi;
		totalDef += totalVit;
		totalCrit += totalDex;

		computeAttackDelay();
	}

	/**
	 * Computes the attack delay, Should be called with updateAnimationAtkHitTime to synchronize the attack and attack animation of the Character.
	 * The formula might be changed in the future (As if)
	 */
	private void computeAttackDelay() {
		final float MAX_ATTACK_DELAY = 1.5f;
		final float MIN_ATTACK_DELAY = 0.3f;
		final float CAP_TOTAL_AGI = 150;

		// For the manipulation of the attack delay
		float delayPercentage = (totalAgi / CAP_TOTAL_AGI) + (totalAtkSpd / 100);
		attackDelay = MAX_ATTACK_DELAY - ((MAX_ATTACK_DELAY - MIN_ATTACK_DELAY) * delayPercentage);

		// The totalAtkSpd is the percentage of the attack speed depending on the CAP_TOTAL_AGI
		totalAtkSpd = (delayPercentage * 100);
	}

	public void addBaseStr() {
		baseStr++;
		remainingStatusPoints--;
	}

	public void addBaseDex() {
		baseDex++;
		remainingStatusPoints--;
	}

	public void addBaseVit() {
		baseVit++;
		remainingStatusPoints--;
	}

	public void addBaseAgi() {
		baseAgi++;
		remainingStatusPoints--;
	}

	public void addBaseWis() {
		baseWis++;
		remainingStatusPoints--;
	}


	void gainExperience(int enemyLevel) {
		// Add exp base on the level of the character
		// Should be just greater than 9 levels of the character being killed
		float levelGap = enemyLevel - level;
		if (levelGap >= -10) {
			experience += 1 + (levelGap / 10);

			if (experience >= 100) {
				experience -= 100;
				levelUp();
			}
		}
	}

	void levelUp() {
		baseAtkDmg++;
		baseAtkSpd++;
		baseHit++;
		baseAvoid++;
		baseDef++;
		baseCrit++;
		baseFullHp += 10;
		baseFullMp += 10;
		level++;
		remainingStatusPoints = 5;
	}
	
	
	public void applyDamage(int damage) {
		hpDamage = (int) (damage - totalDef);
		if (hpDamage < 1) {
			hpDamage = 1;
		}
		hp -= hpDamage;
	}
	
	public int getHpDamage() {
		return hpDamage;
	}
	
	public boolean isAlive() {
		return (hp < 1) ? false: true;
	}
	
}
