package com.games4stuul.pinballguardian.triggereffect;

import java.util.List;

import com.games4stuul.pinballguardian.PlayerStats;
import com.games4stuul.pinballguardian.enums.TriggerType;
import com.games4stuul.pinballguardian.gameobject.enemy.Enemy;
import com.games4stuul.pinballguardian.screen.GameScreen;

public class EnemyStunEffect extends TriggerEffect {
	float duration;
	float startEffectTime;

	public EnemyStunEffect(float duration, GameScreen gameScreen) {
		super(new TriggerType[]{
				TriggerType.WOOD, 
				TriggerType.WOOD, 
				TriggerType.WOOD
		}, gameScreen);
		this.duration = duration;
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
				enemies.get(n).applyStun();
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
			enemies.get(n).undoStun();
		}
	}

	@Override
	public void unlock() {
		if (!PlayerStats.unlockEnemyStunEffect) {
			PlayerStats.unlockEnemyStunEffect = true;
		}
	}
}
