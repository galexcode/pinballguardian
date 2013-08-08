package com.games4stuul.pinballguardian.triggereffect;

import java.util.List;

import com.games4stuul.pinballguardian.PlayerStats;
import com.games4stuul.pinballguardian.enums.TriggerType;
import com.games4stuul.pinballguardian.gameobject.Ball;
import com.games4stuul.pinballguardian.screen.GameScreen;

public class BallRimEffect extends TriggerEffect {
	float duration;
	float rimIncrease;
	float startEffectTime;
	
	public BallRimEffect(float duration, float rimIncrease, GameScreen gameScreen) {
		super(new TriggerType[]{
				TriggerType.METAL, 
				TriggerType.METAL, 
				TriggerType.METAL
		}, gameScreen);
		this.duration = duration;
		this.rimIncrease = rimIncrease;
	}
	
	@Override
	public void startEffect() {
		startEffectTime = System.nanoTime() / 1000000000.0f;
	}

	@Override
	public void stopEffect() {
		List<Ball> balls = gameScreen.getBalls();
		int length = balls.size();
		for (int n = 0; n < length; n++) {
			balls.get(n).endRimEffect();
		}
	}

	@Override
	public void update() {
		float delta = System.nanoTime() / 1000000000.0f - startEffectTime;
		if (delta > duration) {
			stopEffect();
			gameScreen.removeActiveTriggerEffect(this);
		}
		else {
			List<Ball> balls = gameScreen.getBalls();
			int length = balls.size();
			for (int n = 0; n < length; n++) {
				balls.get(n).beginRimEffect(rimIncrease);
			}
		}
	}
	
	@Override
	public void unlock() {
		if (!PlayerStats.unlockBallRimEffect) {
			PlayerStats.unlockBallRimEffect = true;
		}
	}
}
