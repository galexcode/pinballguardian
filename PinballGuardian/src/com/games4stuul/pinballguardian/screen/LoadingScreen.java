package com.games4stuul.pinballguardian.screen;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.games4stuul.pinballguardian.Assets;
import com.games4stuul.pinballguardian.gameobject.enemy.Pudding;

public class LoadingScreen implements Screen, InputProcessor {
	Game game;
	Screen nextScreen;
	
	public LoadingScreen(Game game, Screen nextScreen) {
		this.game = game;
		this.nextScreen = nextScreen;
		
		//load backgrounds
		Assets.assetManager.load("texture/background/grass.png", Texture.class);
		Assets.assetManager.load("texture/background/desert.png", Texture.class);
		
		Assets.assetManager.load("texture/enemy/pudding.png", Texture.class);
	}

	@Override
	public boolean keyDown(int keycode) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean keyUp(int keycode) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean keyTyped(char character) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean mouseMoved(int screenX, int screenY) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean scrolled(int amount) {
		// TODO Auto-generated method stub
		return false;
	}
	
	private void getEnemyTextures(Texture texture, TextureRegion[] walkTextures, TextureRegion[] attackTextures,
			TextureRegion[] deadTexture) {
		TextureRegion[][] textureRegions = TextureRegion.split(texture, 128, 128);
		for (int n = 0; n < 4; n++) {
        	walkTextures[n] = textureRegions[0][n];
        }
		deadTexture[0] = textureRegions[0][4];
		for (int n = 0; n < 3; n++) {
        	attackTextures[n] = textureRegions[0][n + 5];
        }
	}

	@Override
	public void render(float delta) {
		if (Assets.assetManager.update()) {
			Assets.grassBackgroundTexture = Assets.assetManager.get("texture/background/grass.png", Texture.class);
			Assets.desertBackgroundTexture = Assets.assetManager.get("texture/background/desert.png", Texture.class);
			
			getEnemyTextures(Assets.assetManager.get("texture/enemy/pudding.png", Texture.class), 
					Pudding.walkTextures, 
					Pudding.attackTextures, 
					Pudding.deadTexture);
			
			game.setScreen(nextScreen);
		}
		
		//display assetManager.getProgress()
	}

	@Override
	public void resize(int width, int height) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void show() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void hide() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void pause() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void resume() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub
		
	}
}
