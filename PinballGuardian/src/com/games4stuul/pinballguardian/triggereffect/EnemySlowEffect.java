package com.games4stuul.pinballguardian.triggereffect;

import java.util.List;

import com.games4stuul.pinballguardian.PlayerStats;
import com.games4stuul.pinballguardian.enums.TriggerType;
import com.games4stuul.pinballguardian.gameobject.enemy.Enemy;
import com.games4stuul.pinballguardian.screen.GameScreen;

public class EnemySlowEffect extends TriggerEffect {
	float duration;
	float effectStrength;
	float startEffectTime;

	public EnemySlowEffect(float duration, float effectStrength, GameScreen gameScreen) {
		super(new TriggerType[]{
				TriggerType.WATER, 
				TriggerType.WATER, 
				TriggerType.WATER
		}, gameScreen);
		this.duration = duration;
		this.effectStrength = effectStrength;
	}

	@Override
	public void update() {
		float delta = System.nanoTime() / 1000000000.0f - startEffectTime;
		if (delta > duration) {
			stopEffect();
			gameScreen.removeActiveTriggerEffect(this);
		}
		else {
			List<Enemy> enemies = gameScreen.getEnemies();
			int length = enemies.size();
			for (int n = 0; n < length; n++) {
				enemies.get(n).reduceSpeed(effectStrength);
			}
		}
	}

	@Override
	public void startEffect() {
		startEffectTime = System.nanoTime() / 1000000000.0f;
	}

	@Override
	public void stopEffect() {
		List<Enemy> enemies = gameScreen.getEnemies();
		int length = enemies.size();
		for (int n = 0; n < length; n++) {
			enemies.get(n).restoreSpeed();
		}
	}
	
	@Override
	public void unlock() {
		if (!PlayerStats.unlockEnemySlowEffect) {
			PlayerStats.unlockEnemySlowEffect = true;
		}
	}
}
