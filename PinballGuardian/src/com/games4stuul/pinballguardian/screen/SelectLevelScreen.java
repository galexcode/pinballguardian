package com.games4stuul.pinballguardian.screen;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.games4stuul.pinballguardian.Constant;
import com.games4stuul.pinballguardian.PlayerStats;

public class SelectLevelScreen implements Screen, InputProcessor {
	Game game;
	
	//camera
	OrthographicCamera camera;
	
	ShapeRenderer shapeRenderer;
	SpriteBatch spriteBatch;
	BitmapFont bitmapFont;
	
	float screenWidth, screenHeight;
	
	Rectangle previousLevelButtonBounds;
	Rectangle nextLevelButtonBounds;
	Rectangle beginLevelButtonBounds;
	
	int selectedLevel;
	
	public SelectLevelScreen(Game game) {
		this.game = game;
		
		//load assets
		game.setScreen(new LoadingScreen(game, this));
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
		Vector3 point = new Vector3(screenX, screenY, 0);
		camera.unproject(point);
		
		if (GameScreen.isPointInRectangle(previousLevelButtonBounds, point.x, point.y)) {
			if (selectedLevel > Constant.lowestLevel) {
				selectedLevel--;
			}
		}
		else if (GameScreen.isPointInRectangle(nextLevelButtonBounds, point.x, point.y)) {
			if (selectedLevel < PlayerStats.currentLevel) {
				selectedLevel++;
			}
		}
		else if (GameScreen.isPointInRectangle(beginLevelButtonBounds, point.x, point.y)) {
			game.setScreen(new GameScreen(game, "map/level" + Integer.toString(selectedLevel) + ".txt"));
		}
		
		return true;
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

	@Override
	public void render(float delta) {
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
		
		shapeRenderer.setProjectionMatrix(camera.combined);
		spriteBatch.setProjectionMatrix(camera.combined);
		
		spriteBatch.begin();
			bitmapFont.setColor(1, 0, 1, 1);
			bitmapFont.draw(spriteBatch, Integer.toString(selectedLevel), 200, 300);
		spriteBatch.end();
		
		spriteBatch.begin();
			bitmapFont.setColor(1, 1, 0, 1);
			bitmapFont.draw(spriteBatch, Long.toString(PlayerStats.gold), 200, 250);
		spriteBatch.end();
		
		shapeRenderer.setColor(1, 0, 0, 1);
		shapeRenderer.begin(ShapeType.Line);
			shapeRenderer.rect(previousLevelButtonBounds.x, previousLevelButtonBounds.y, 
					previousLevelButtonBounds.width, previousLevelButtonBounds.height);
		shapeRenderer.end();
		spriteBatch.begin();
			bitmapFont.setColor(1, 0, 0, 1);
			bitmapFont.draw(spriteBatch, "<", previousLevelButtonBounds.x, previousLevelButtonBounds.y + bitmapFont.getCapHeight());
		spriteBatch.end();
		
		shapeRenderer.setColor(0, 1, 0, 1);
		shapeRenderer.begin(ShapeType.Line);
			shapeRenderer.rect(nextLevelButtonBounds.x, nextLevelButtonBounds.y, 
					nextLevelButtonBounds.width, nextLevelButtonBounds.height);
		shapeRenderer.end();
		spriteBatch.begin();
			bitmapFont.setColor(0, 1, 0, 1);
			bitmapFont.draw(spriteBatch, ">", nextLevelButtonBounds.x, nextLevelButtonBounds.y + bitmapFont.getCapHeight());
		spriteBatch.end();
		
		shapeRenderer.setColor(0, 0, 1, 1);
		shapeRenderer.begin(ShapeType.Line);
			shapeRenderer.rect(beginLevelButtonBounds.x, beginLevelButtonBounds.y, 
					beginLevelButtonBounds.width, beginLevelButtonBounds.height);
		shapeRenderer.end();
		spriteBatch.begin();
			bitmapFont.setColor(0, 0, 1, 1);
			bitmapFont.draw(spriteBatch, "START", beginLevelButtonBounds.x, beginLevelButtonBounds.y + bitmapFont.getCapHeight());
		spriteBatch.end();
	}

	@Override
	public void resize(int width, int height) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void show() {
		Gdx.input.setInputProcessor(this);
		
		screenWidth = Constant.screenWidth;
		screenHeight = Constant.screenHeight;
		
		//initialize camera
		camera = new OrthographicCamera(screenWidth, screenHeight);
		camera.position.set(screenWidth / 2, screenHeight / 2, 0);
		camera.update();
		
		shapeRenderer = new ShapeRenderer();
		spriteBatch = new SpriteBatch();
		bitmapFont = new BitmapFont();
		
		//buttons
		previousLevelButtonBounds = new Rectangle(100, 100, 50, 20);
		nextLevelButtonBounds = new Rectangle(100, 140, 50, 20);
		beginLevelButtonBounds = new Rectangle(100, 180, 50, 20);
		
		//variables
		selectedLevel = PlayerStats.currentLevel;
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
