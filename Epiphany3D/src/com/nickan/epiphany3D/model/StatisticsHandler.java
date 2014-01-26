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

	public float currentHp;
	public float currentMp;

	// The bases fields will be used for balancing (if that will happen)
	public float baseStr;
	public float baseDex;
	public float baseVit;
	public float baseAgi;
	public float baseWis;

	// Sub status
	public int sightRange = 5;
	public int attackRange = 1;

	float hitChance = 80;

	public float attackDelay = 1.5f;
	
	private int hpDamage;
	
	// New implementation;
	float addedStr;
	float addedDex;
	float addedVit;
	float addedAgi;
	float addedWis;
	
	float addedAtkDmg;
	float addedAtkSpd;
	float addedHit;
	float addedAvoid;
	float addedDef;
	float addedCrit;
	float addedHp;
	float addedMp;

	public StatisticsHandler() {
		baseStr = 10;
		baseDex = 10;
		baseVit = 10;
		baseAgi = 100;
		baseWis = 10;
		
		resetAddedAttributes();
		calcFinalAttributes();
		currentHp = getFullHp();
		currentMp = getFullMp();
	}

	public StatisticsHandler(float baseStr, float baseDex, float baseVit, float baseAgi, float baseWis) {
		this.baseStr = baseStr;
		this.baseDex = baseDex;
		this.baseVit = baseVit;
		this.baseAgi = baseAgi;
		this.baseWis = baseWis;
	}
	
	/**
	 * For testing, making the character a bit more buffed
	 */
	public void whosYourDaddy() {
		baseStr = 100;
		baseDex = 100;
		baseVit = 100;
		baseAgi = 100;
		baseWis = 100;
	}
	
	public void resetAddedAttributes() {
		addedStr = 0;
		addedDex = 0;
		addedVit = 0;
		addedAgi = 0;
		addedWis = 0;
		
		addedAtkDmg = 0;
		addedAtkSpd = 0;
		addedHit = 0;
		addedAvoid = 0;
		addedDef = 0;
		addedCrit = 0;
		addedHp = 0;
		addedMp = 0;
	}
	
	public void calcFinalAttributes() {
		computeAttackDelay();
	}
	
	public float getStr() {
		return baseStr + addedStr;
	}
	
	public float getAgi() {
		return baseAgi + addedAgi;
	}
	
	public float getDex() {
		return baseDex + addedDex;
	}
	
	public float getVit() {
		return baseVit + addedVit;
	}
	
	public float getWis() {
		return baseWis + addedWis;
	}
	
	public float getAttackDmg() {
		return ((baseStr + addedStr) * 2) + addedAtkDmg;
	}
	
	public float getAttackSpd() {
		return (baseAgi + addedAgi) + addedAtkSpd;
	}
	
	public float getAvoid() {
		return (baseAgi + addedAgi) + addedAvoid;
	}
	
	public float getAttackHit() {
		return (baseDex + addedDex) + addedHit;
	}
	
	public float getAttackCrit() {
		return (baseDex + addedDex) + addedCrit;
	}
	
	public float getDef() {
		return (baseVit + addedVit) + addedDef;
	}
	
	public float getFullHp() {
		return ((baseVit + addedVit) * 3) + addedHp;
	}
	
	public float getFullMp() {
		return ((baseWis + addedWis) * 3) + addedMp;
	}
	
	/**
	 * Computes the attack delay, Should be called with updateAnimationAtkHitTime to synchronize the attack and attack animation of the Character.
	 * The formula might be changed in the future, should really limit the attack speed computation to 150 totalAgi
	 */
	private void computeAttackDelay() {
		final float MAX_ATTACK_DELAY = 1.5f;
		final float MIN_ATTACK_DELAY = 0.3f;
		final float attackDelayThreshold = MAX_ATTACK_DELAY - MIN_ATTACK_DELAY;
		attackDelay = attackDelayThreshold * getAttackSpd();
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
		++baseStr;
		++baseDex;
		++baseVit;
		++baseAgi;
		++level;
		remainingStatusPoints = 10;
	}
	
	
	public void applyDamage(int damage) {
		hpDamage = (int) (damage - getDef());
		if (hpDamage < 1) {
			hpDamage = 1;
		}
		currentHp -= hpDamage;
	}
	
	public int getHpDamage() {
		return hpDamage;
	}
	
	public boolean isAlive() {
		return (currentHp < 1) ? false: true;
	}
	
	public boolean isCurrentHpFull() {
		return (currentHp < getFullHp()) ? false : true;
	}
	
	public boolean isCurrentMpFull() {
		return (currentMp < getFullMp()) ? false : true;
	}
	
}
