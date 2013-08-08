package com.games4stuul.pinballguardian.triggereffect;

import java.util.List;

import com.games4stuul.pinballguardian.enums.TriggerType;
import com.games4stuul.pinballguardian.gameobject.Trigger;
import com.games4stuul.pinballguardian.screen.GameScreen;

public abstract class TriggerEffect {
	GameScreen gameScreen;
	TriggerType[] triggerShapeCombo;
	
	public TriggerEffect(TriggerType[] triggerShapeCombo, GameScreen gameScreen) {
		this.gameScreen = gameScreen;
		this.triggerShapeCombo = triggerShapeCombo;
	}
	
	public boolean isMatchShapeCombo(List<Trigger> triggers) {
		if (triggers.size() == triggerShapeCombo.length) {
			int length = triggerShapeCombo.length;
			for (int n = 0; n < length; n++) {
				if (!triggers.get(n).getTriggerType().equals(triggerShapeCombo[n])) {
					return false;
				}
			}
			return true;
		}
		else {
			return false;
		}
	}
	
	public abstract void update();
	
	public abstract void startEffect();
	
	public abstract void stopEffect();
	
	public abstract void unlock();
}
