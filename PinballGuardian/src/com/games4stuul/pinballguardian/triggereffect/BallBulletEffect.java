package com.games4stuul.pinballguardian.triggereffect;

import java.util.List;

import com.games4stuul.pinballguardian.PlayerStats;
import com.games4stuul.pinballguardian.enums.TriggerType;
import com.games4stuul.pinballguardian.gameobject.Ball;
import com.games4stuul.pinballguardian.screen.GameScreen;

public class BallBulletEffect extends TriggerEffect {
	float duration;
	float attack;
	float speed;
	float frequency;
	float startEffectTime;
	float lastEffectTime;
	float angle;
	
	public BallBulletEffect(float duration, float attack, float speed, float frequency, GameScreen gameScreen) {
		super(new TriggerType[]{
				TriggerType.METAL, 
				TriggerType.METAL, 
				TriggerType.FIRE
		}, gameScreen);
		this.duration = duration;
		this.attack = attack;
		this.speed = speed;
		this.frequency = frequency;
		this.angle = 1.57f;
	}

	@Override
	public void update() {
		float delta = System.nanoTime() / 1000000000.0f - startEffectTime;
		if (delta > duration) {
			stopEffect();
			gameScreen.removeActiveTriggerEffect(this);
		}
		else {
			if (delta - lastEffectTime > 1 / frequency) {
				lastEffectTime = delta;
				List<Ball> balls = gameScreen.getBalls();
				int length = balls.size();
				for (int n = 0; n < length; n++) {
					balls.get(n).beginBulletEffect(attack, speed, angle);
				}
			}
		}
	}

	@Override
	public void startEffect() {
		startEffectTime = System.nanoTime() / 1000000000.0f;
		lastEffectTime = 0.0f;
	}

	@Override
	public void stopEffect() {
		
	}

	@Override
	public void unlock() {
		if (!PlayerStats.unlockBallBulletEffect) {
			PlayerStats.unlockBallBulletEffect = true;
		}
	}
}
