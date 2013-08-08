package com.games4stuul.pinballguardian;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;

public class DesktopPinballGuardian {
	public static void main (String[] args) {
		new LwjglApplication(new PinballGuardian(), "Pinball Guardian", Constant.screenWidth, Constant.screenHeight, false);
	}
}
