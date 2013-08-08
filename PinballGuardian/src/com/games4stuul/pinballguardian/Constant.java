package com.games4stuul.pinballguardian;

public class Constant {
	public static final int screenWidth = 480;
	public static final int screenHeight = 800;
	
	public static final float worldWidth = 4.8f;
	public static final float worldHeight = 7.0f;
	
	public static final float damping = 0.4f;
	
	public static final float minFPS = 0.1f;
	public static final int velocityIterations = 6;
	public static final int positionIterations  = 2;
	
	public static final float flipperAngularSpeed = 6.0f;
	public static final float maxFlipperTorque = 8.0f;
	public static final float flipperTranslationalSpeed = 1.0f;
	
	public static final short ballCategory = 0x0001;
	public static final short wallCategory = 0x0002;
	public static final short enemyCategory = 0x0004;
	public static final short weaponCategory = 0x0008;
	public static final short triggerCategory = 0x0016;
	public static final short castleCategory = 0x0032;
	public static final short enemyWeaponCategory = 0x0064;
	
	public static final int lowestLevel = 1;
	public static final int highestLevel = 5;
	public static final float restitution = 0.8f;
	public static final float velocityThreshold = 0.7f;
}
