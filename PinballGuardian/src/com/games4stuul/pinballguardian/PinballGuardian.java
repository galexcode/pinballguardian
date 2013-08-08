package com.games4stuul.pinballguardian;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.physics.box2d.World;
import com.games4stuul.pinballguardian.screen.LoadingScreen;
import com.games4stuul.pinballguardian.screen.SelectLevelScreen;

public class PinballGuardian extends Game {

	@Override
	public void create() {
		World.setVelocityThreshold(Constant.velocityThreshold);
		setScreen(new LoadingScreen(this, new SelectLevelScreen(this)));
	}

}
