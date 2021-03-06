package com.nickan.epiphany3D.model;

/**
 * Takes care of the statistics of the character class
 * @author Nickan
 *
 */
public class StatisticsHandler {
	public int level = 1;
	private long experience = 0;
	public int remainingStatusPoints = 10;
	private int expGained = 0;

	public float currentHp;
	public float currentMp;

	// The bases fields will be used for balancing (if that will happen)
	float baseStr;
	float baseDex;
	float baseVit;
	float baseAgi;
	float baseWis;

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
	float addedAvd;
	float addedDef;
	float addedCrt;
	float addedHp;
	float addedMp;

	public StatisticsHandler() {
		baseStr = 10;
		baseDex = 10;
		baseVit = 10;
		baseAgi = 10;
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
	 * For testing, making the character a bit more buff
	 */
	public void whosYourDaddy() {
		baseStr = 100;
		baseDex = 100;
		baseVit = 100;
		baseAgi = 100;
		baseWis = 100;
		
		resetAddedAttributes();
		calcFinalAttributes();
		currentHp = getFullHp();
		currentMp = getFullMp();
	}
	
	/**
	 * Should be called prior to applying all of the added attributes
	 */
	public void resetAddedAttributes() {
		final int defaultAddedHp = 200;
		
		addedStr = 0;
		addedDex = 0;
		addedVit = 0;
		addedAgi = 0;
		addedWis = 0;
		
		addedAtkDmg = 0;
		addedAtkSpd = 0;
		addedHit = 0;
		addedAvd = 0;
		addedDef = 0;
		addedCrt = 0;
		addedHp = 0 + defaultAddedHp;
		addedMp = 0;
	}
	
	/**
	 * Should be called after applying the added attributes
	 */
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
		return (baseAgi + addedAgi) + addedAvd;
	}
	
	public float getAttackHit() {
		return (baseDex + addedDex) + addedHit;
	}
	
	public float getAttackCrit() {
		return (baseDex + addedDex) + addedCrt;
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
	 * Computes the attack delay of the character
	 */
	private void computeAttackDelay() {
		final float MAX_ATTACK_DELAY = 1.5f;
		final float MIN_ATTACK_DELAY = 0.1f;
		final float attackDelayThreshold = MAX_ATTACK_DELAY - MIN_ATTACK_DELAY;
		attackDelay = MAX_ATTACK_DELAY - (attackDelayThreshold * getAttackSpd() / 100);
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

	
	public void gainExperience(int enemyLevel) {
		// Add exp base on the level of the character
		// Should be just greater than 9 levels of the character being killed
		float levelGap = enemyLevel - level;
		if (levelGap >= -10) {
			expGained = Math.round(1 + (levelGap / 10));
			experience += expGained;

			if (experience >= 100) {
				experience -= 100;
				levelUp();
			}
		}
	}
	
	/**
	 * After getting the values, it automatically sets to zero
	 * @return
	 */
	public int getExpGained() {
		int tempExpGained = expGained;
		expGained = 0;
		return tempExpGained;
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
	
	
	// Methods for adding attributes from equipment or from sources
	public void addAddedStr(int str) {
		addedStr += str;
	}
	
	public void addAddedDex(int dex) {
		addedDex += dex;
	}
	
	public void addAddedVit(int vit) {
		addedVit += vit;
	}
	
	public void addAddedAgi(int agi) {
		addedAgi += agi;
	}
	
	public void addAddedWis(int wis) {
		addedWis += wis;
	}
	
	public void addAddedAtkDmg(int dmg) {
		addedAtkDmg += dmg;
	}
	
	public void addAddedAtkSpd(float spd) {
		addedAtkSpd += spd;
	}
	
	public void addAddedAvd(int avd) {
		addedAvd += avd;
	}
	
	public void addAddedHit(int hit) {
		addedHit += hit;
	}
	
	public void addAddedCrt(float crt) {
		addedCrt += crt;
	}
	
	public void addAddedDef(int def) {
		addedDef += def;
	}
	
	public void addAddedHp(int hp) {
		addedHp += hp;
	}
	
	public void addAddedMp(int mp) {
		addedMp += mp;
	}
	
}
