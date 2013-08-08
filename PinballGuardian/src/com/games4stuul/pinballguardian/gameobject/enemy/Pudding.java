package com.games4stuul.pinballguardian.gameobject.enemy;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.games4stuul.pinballguardian.screen.GameScreen;

public class Pudding extends Enemy {
	public static TextureRegion[] walkTextures = new TextureRegion[4];
	public static TextureRegion[] attackTextures = new TextureRegion[3];
	public static TextureRegion[] deadTexture = new TextureRegion[1];
	
	public static final float size = 0.2f;
	public static final float attackRange = 0.2f;
	public static final float projectileSpeed = 0;
	
	public static final float baseHpMax = 100.0f;
	public static final float baseAttack = 1.0f;
	public static final float baseDefense = 0.0f;
	public static final float baseSpeed = 0.1f;
	public static final float baseAttackSpeed = 1.0f;
	public static final float baseGold = 10.0f;
	
	public static final float hpMaxGrowth = 20.0f;
	public static final float attackGrowth = 4.0f;
	public static final float defenseGrowth = 2.0f;
	public static final float speedGrowth = 0.01f;
	public static final float attackSpeedGrowth = 0.1f;
	public static final float goldGrowth = 1.0f;

	public Pudding(float level, float x, float reserveTime, GameScreen gameScreen) {
		super(level, x, reserveTime, gameScreen, size, attackRange, projectileSpeed, 
				baseHpMax, baseAttack, baseDefense, baseSpeed, baseAttackSpeed, baseGold, 
				hpMaxGrowth, attackGrowth, defenseGrowth, speedGrowth, attackSpeedGrowth, goldGrowth, 
				walkTextures, attackTextures, deadTexture);
	}

}
