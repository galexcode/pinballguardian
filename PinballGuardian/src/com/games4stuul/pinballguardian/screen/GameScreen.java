package com.games4stuul.pinballguardian.screen;

import java.io.BufferedReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.games4stuul.pinballguardian.Assets;
import com.games4stuul.pinballguardian.Constant;
import com.games4stuul.pinballguardian.PlayerStats;
import com.games4stuul.pinballguardian.comparator.EnemyReserveTimeComparator;
import com.games4stuul.pinballguardian.enums.TriggerType;
import com.games4stuul.pinballguardian.gameobject.Ball;
import com.games4stuul.pinballguardian.gameobject.EnemyProjectile;
import com.games4stuul.pinballguardian.gameobject.Projectile;
import com.games4stuul.pinballguardian.gameobject.Castle;
import com.games4stuul.pinballguardian.gameobject.LeftFlipper;
import com.games4stuul.pinballguardian.gameobject.RightFlipper;
import com.games4stuul.pinballguardian.gameobject.Trigger;
import com.games4stuul.pinballguardian.gameobject.Wall;
import com.games4stuul.pinballguardian.gameobject.enemy.Enemy;
import com.games4stuul.pinballguardian.gameobject.enemy.Pudding;
import com.games4stuul.pinballguardian.listener.ContactListener;
import com.games4stuul.pinballguardian.triggereffect.BallAttackEffect;
import com.games4stuul.pinballguardian.triggereffect.BallBulletEffect;
import com.games4stuul.pinballguardian.triggereffect.BallRimEffect;
import com.games4stuul.pinballguardian.triggereffect.EnemyFreezeEffect;
import com.games4stuul.pinballguardian.triggereffect.EnemySlowEffect;
import com.games4stuul.pinballguardian.triggereffect.EnemyStunEffect;
import com.games4stuul.pinballguardian.triggereffect.TriggerEffect;

//IDEAS
//-triggers: after ball touching every trigger, activate effect
//-effects can be random or preset or depends on order of triggers and how many triggers are chained
//-triggers can have colour and shape, different chains/combinations have different effects
//-effects:
//	-fixed line segment that deals damage to enemies
//	-moving line segment(s) that deal damage to enemies
//	-damages all enemies on screen
//	-stuns all enemies on screen
//	-add more balls

public class GameScreen implements Screen, InputProcessor {
	float cameraWidth;
	float cameraHeight;
	
	Game game;
	float gameTime;
	String levelFile;
	float exitTime;
	boolean isWin;
	
	Texture backgroundTexture;
	
	//camera
	OrthographicCamera camera;
	
	//world
	World world;
	float worldWidth;
	float worldHeight;
	
	//rendering
	Box2DDebugRenderer debugRenderer;
	ShapeRenderer shapeRenderer;
	SpriteBatch spriteBatch;
	
	//buttons
	Rectangle leftFlipperButtonBounds;
	int leftFlipperButtonPointer;
	Rectangle rightFlipperButtonBounds;
	int rightFlipperButtonPointer;
	Rectangle moveLeftButtonBounds;
	int moveLeftButtonPointer;
	Rectangle moveRightButtonBounds;
	int moveRightButtonPointer;
	
	//game objects
	List<Wall> walls;
	List<LeftFlipper> leftFlippers;
	List<RightFlipper> rightFlippers;
	List<Ball> balls;
	List<Enemy> enemies;
	List<Enemy> reserveEnemies;
	List<Trigger> triggers;
	List<Trigger> activeTriggers;
	List<TriggerEffect> triggerEffects;
	List<TriggerEffect> activeTriggerEffects;
	List<Projectile> reserveProjectiles;
	List<Projectile> activeProjectiles;
	List<EnemyProjectile> reserveEnemyProjectiles;
	List<EnemyProjectile> activeEnemyProjectiles;
	Castle castle;
	
	int lastReserveProjectile;
	int lastReserveEnemyProjectile;
	
	//game stats
	long goldFound;
	
	public GameScreen(Game game, String levelFile) {
		this.game = game;
		this.levelFile = levelFile;
		
		//load assets
		game.setScreen(new LoadingScreen(game, this));
	}

	@Override
	public boolean keyDown(int keycode) {
		return false;
	}

	@Override
	public boolean keyUp(int keycode) {
		return false;
	}

	@Override
	public boolean keyTyped(char character) {
		return false;
	}
	
	public static boolean isPointInRectangle(Rectangle rectangle, float pointX, float pointY) {
		if (pointX >= rectangle.x && pointX <= rectangle.x + rectangle.width && pointY >= rectangle.y && pointY <= rectangle.y + rectangle.height) {
			return true;
		}
		return false;
	}

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		Vector3 point = new Vector3(screenX, screenY, 0);
		camera.unproject(point);
		
		if (isPointInRectangle(leftFlipperButtonBounds, point.x, point.y)) {
			if (leftFlipperButtonPointer < 0) {
				leftFlipperButtonPointer = pointer;
				int length = leftFlippers.size();
				for (int n = 0; n < length; n++) {
					leftFlippers.get(n).flipUp();
				}
			}
		}
		if (isPointInRectangle(rightFlipperButtonBounds, point.x, point.y)) {
			if (rightFlipperButtonPointer < 0) {
				rightFlipperButtonPointer = pointer;
				int length = rightFlippers.size();
				for (int n = 0; n < length; n++) {
					rightFlippers.get(n).flipUp();
				}
			}
		}
		if (isPointInRectangle(moveLeftButtonBounds, point.x, point.y)) {
			if (moveLeftButtonPointer < 0 && moveRightButtonPointer < 0) {
				moveLeftButtonPointer = pointer;
				int length = leftFlippers.size();
				for (int n = 0; n < length; n++) {
					leftFlippers.get(n).moveLeft();
				}
				length = rightFlippers.size();
				for (int n = 0; n < length; n++) {
					rightFlippers.get(n).moveLeft();
				}
			}
		}
		if (isPointInRectangle(moveRightButtonBounds, point.x, point.y)) {
			if (moveLeftButtonPointer < 0 && moveRightButtonPointer < 0) {
				moveRightButtonPointer = pointer;
				int length = leftFlippers.size();
				for (int n = 0; n < length; n++) {
					leftFlippers.get(n).moveRight();
				}
				length = rightFlippers.size();
				for (int n = 0; n < length; n++) {
					rightFlippers.get(n).moveRight();
				}
			}
		}
		return true;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		Vector3 point = new Vector3(screenX, screenY, 0);
		camera.unproject(point);
		
		if (pointer == leftFlipperButtonPointer) {
			leftFlipperButtonPointer = -1;
			int length = leftFlippers.size();
			for (int n = 0; n < length; n++) {
				leftFlippers.get(n).flipDown();
			}
		}
		if (pointer == rightFlipperButtonPointer) {
			rightFlipperButtonPointer = -1;
			int length = rightFlippers.size();
			for (int n = 0; n < length; n++) {
				rightFlippers.get(n).flipDown();
			}
		}
		if (pointer == moveLeftButtonPointer || pointer == moveRightButtonPointer) {
			moveLeftButtonPointer = -1;
			moveRightButtonPointer = -1;
			int length = leftFlippers.size();
			for (int n = 0; n < length; n++) {
				leftFlippers.get(n).moveStop();
			}
			length = rightFlippers.size();
			for (int n = 0; n < length; n++) {
				rightFlippers.get(n).moveStop();
			}
		}
		return true;
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		return false;
	}

	@Override
	public boolean mouseMoved(int screenX, int screenY) {
		return false;
	}

	@Override
	public boolean scrolled(int amount) {
		return false;
	}
	
	public void updateEnemies() {
		//update enemies
		for (int n = 0; n < enemies.size(); n++) {
			enemies.get(n).update();
		}
		
		//spawn new enemies
		for (int n = 0; n < reserveEnemies.size(); n++) {
			if (reserveEnemies.get(n).getReserveTime() > gameTime) {
				break;
			}
			reserveEnemies.get(n).addToWorld();
			enemies.add(reserveEnemies.get(n));
			reserveEnemies.remove(n);
			n--;
		}
	}
	
	public void updateTriggerEffects() {
		int length = activeTriggerEffects.size();
		for (int n = 0; n < length; n++) {
			activeTriggerEffects.get(n).update();
		}
	}
	
	public void fixBounds() {
		int length = leftFlippers.size();
		for (int n = 0; n < length; n++) {
			leftFlippers.get(n).fixMoveLimit();
		}
		
		length = rightFlippers.size();
		for (int n = 0; n < length; n++) {
			rightFlippers.get(n).fixMoveLimit();
		}
	}
	
	public void gameRender() {
		int length = enemies.size();
		for (int n = 0; n < length; n++) {
			enemies.get(n).render(spriteBatch, shapeRenderer);
		}

		length = activeProjectiles.size();
		for (int n = 0; n < length; n++) {
			activeProjectiles.get(n).render(shapeRenderer);
		}
		
		length = activeEnemyProjectiles.size();
		for (int n = 0; n < length; n++) {
			activeEnemyProjectiles.get(n).render(shapeRenderer);
		}
		
		castle.render(shapeRenderer);
		
		length = balls.size();
		for (int n = 0; n < length; n++) {
			balls.get(n).render(shapeRenderer);
		}
		
		length = walls.size();
		for (int n = 0; n < length; n++) {
			walls.get(n).render(shapeRenderer);
		}
		
		length = leftFlippers.size();
		for (int n = 0; n < length; n++) {
			leftFlippers.get(n).render(shapeRenderer);
		}
		
		length = rightFlippers.size();
		for (int n = 0; n < length; n++) {
			rightFlippers.get(n).render(shapeRenderer);
		}
		
		length = triggers.size();
		for (int n = 0; n < length; n++) {
			triggers.get(n).render(shapeRenderer);
		}
	}
	
	public void barRender() {
		shapeRenderer.begin(ShapeType.Filled);
			shapeRenderer.setColor(0, 0, 0, 1);
			shapeRenderer.rect(0, worldHeight, worldWidth, cameraHeight - worldHeight);
		shapeRenderer.end();
		
		shapeRenderer.begin(ShapeType.Line);
			shapeRenderer.setColor(1, 1, 1, 1);
			shapeRenderer.rect(0, worldHeight, worldWidth, cameraHeight - worldHeight);
		shapeRenderer.end();
	}
	
	public void backgroundRender() {
		spriteBatch.begin();
			spriteBatch.draw(backgroundTexture, 
					0, 0, 
					worldWidth, worldHeight, 
					0, 0, 
					512, 1024, 
					false, false);
		spriteBatch.end();
	}
	
	@Override
	public void render(float delta) {
		gameTime += delta;
		
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
		
		spriteBatch.setProjectionMatrix(camera.combined);
		shapeRenderer.setProjectionMatrix(camera.combined);
		
		backgroundRender();
		gameRender();
		barRender();
		
		update(delta);
		
		if (exitTime > 0) {
			if (gameTime > exitTime) {
				if (isWin) {
					PlayerStats.gold += goldFound;
					if (PlayerStats.currentLevel < Constant.highestLevel){ 
						PlayerStats.currentLevel++;
					}
					game.setScreen(new SelectLevelScreen(game));
				}
				else {
					PlayerStats.gold += goldFound;
					game.setScreen(new SelectLevelScreen(game));
				}
			}
		}
	}

	@Override
	public void resize(int width, int height) {
		
	}

	@Override
	public void show() {
		Gdx.input.setInputProcessor(this);
		
		//initialize world
		gameTime = 0;
		exitTime = 0;
		world = new World(new Vector2(0, -5.0f), true);
		worldWidth = Constant.worldWidth;
		worldHeight = Constant.worldHeight;
		
		//initialize camera
		cameraWidth = Constant.screenWidth / 100.0f;
		cameraHeight = Constant.screenHeight / 100.0f;
		camera = new OrthographicCamera(cameraWidth, cameraHeight);
		camera.position.set(cameraWidth / 2, cameraHeight / 2, 0);
		camera.update();
		
		//initialize buttons
		leftFlipperButtonPointer = -1;
		leftFlipperButtonBounds = new Rectangle(0, 0, 2.4f, 4.0f);
		rightFlipperButtonPointer = -1;
		rightFlipperButtonBounds = new Rectangle(2.4f, 0, 2.4f, 4.0f);
		moveLeftButtonPointer = -1;
		moveLeftButtonBounds = new Rectangle(0, 4.0f, 2.4f, 4.0f);
		moveRightButtonPointer = -1;
		moveRightButtonBounds = new Rectangle(2.4f, 4.0f, 2.4f, 4.0f);
		
		//load stage file
		loadLevelFile(levelFile);
		
		//initialize castle
		castle = new Castle(1.0f, 100.0f, 0.0f, this);
		castle.addToWorld();
		
		//initialize wall
		int length = walls.size();
		for (int n = 0; n < length; n++) {
			walls.get(n).addToWorld();
		}
		
		//initialize left flippers
		length = leftFlippers.size();
		for (int n = 0; n < length; n++) {
			leftFlippers.get(n).addToWorld();
		}
		
		//initialize right flippers
		length = rightFlippers.size();
		for (int n = 0; n < length; n++) {
			rightFlippers.get(n).addToWorld();
		}
		
		//initialize triggers
		length = triggers.size();
		for (int n = 0; n < length; n++) {
			triggers.get(n).addToWorld();
		}
				
		//initialize balls
balls.add(new Ball(2.0f, 6.0f, 0.1f, 0.2f, this));
		length = balls.size();
		for (int n = 0; n < length; n++) {
			balls.get(n).addToWorld();
		}
		
		//initialize trigger effects
		triggerEffects = new ArrayList<TriggerEffect>();
		triggerEffects.add(new BallRimEffect(10.0f, 1.0f, this));
		triggerEffects.add(new BallAttackEffect(10.0f, 1.0f, this));
		triggerEffects.add(new EnemySlowEffect(10.0f, 0.5f, this));
		triggerEffects.add(new EnemyFreezeEffect(10.0f, this));
		triggerEffects.add(new EnemyStunEffect(10.0f, this));
		triggerEffects.add(new BallBulletEffect(10.0f, 100.0f, 5.0f, 2.0f, this));

		activeTriggers = new ArrayList<Trigger>();
		activeTriggerEffects = new ArrayList<TriggerEffect>();
		
		enemies = new ArrayList<Enemy>();
		
		reserveProjectiles = new ArrayList<Projectile>();
		activeProjectiles = new ArrayList<Projectile>();
		reserveEnemyProjectiles = new ArrayList<EnemyProjectile>();
		activeEnemyProjectiles = new ArrayList<EnemyProjectile>();
		
		debugRenderer = new Box2DDebugRenderer();
		debugRenderer.setDrawJoints(false);
		debugRenderer.setDrawInactiveBodies(false);
		shapeRenderer = new ShapeRenderer();
		spriteBatch = new SpriteBatch();
		
		world.setContactListener(new ContactListener(this));
		
		lastReserveProjectile = 0;
		lastReserveEnemyProjectile = 0;
	}

	@Override
	public void hide() {
		
	}

	@Override
	public void pause() {
		
	}

	@Override
	public void resume() {
		
	}

	@Override
	public void dispose() {
		
	}

	public boolean loadLevelFile(String fileName) {
		walls = new ArrayList<Wall>();
		leftFlippers = new ArrayList<LeftFlipper>();
		rightFlippers = new ArrayList<RightFlipper>();
		balls = new ArrayList<Ball>();
		reserveEnemies = new ArrayList<Enemy>();
		triggers = new ArrayList<Trigger>();
		
		try {
			FileHandle file = Gdx.files.internal(fileName);
			BufferedReader br = file.reader(8192);
			String line = br.readLine();
			String[] strArray1, strArray2;
			int n, length;
			while (line != null) {
				strArray1 = line.split(":");
				strArray2 = strArray1[1].split(",");
				if (strArray1[0].equals("W")) {		//wall
					length = strArray2.length;
					float[] vertices = new float[length];
					for (n = 0; n < length; n++) {
						vertices[n] = Float.parseFloat(strArray2[n]);
					}
					walls.add(new Wall(vertices, world));
				}
				else if (strArray1[0].equals("FL")) {		//left flipper
					leftFlippers.add(new LeftFlipper(
							Float.parseFloat(strArray2[0]), 
							Float.parseFloat(strArray2[1]), 
							Float.parseFloat(strArray2[2]), 
							Float.parseFloat(strArray2[3]), 
							Float.parseFloat(strArray2[4]), 
							world
					));
				}
				else if (strArray1[0].equals("FR")) {		//right flipper
					rightFlippers.add(new RightFlipper(
							Float.parseFloat(strArray2[0]), 
							Float.parseFloat(strArray2[1]), 
							Float.parseFloat(strArray2[2]), 
							Float.parseFloat(strArray2[3]), 
							Float.parseFloat(strArray2[4]), 
							world
					));
				}
				else if (strArray1[0].equals("E")) {	//enemy
					float level = Float.parseFloat(strArray2[1]);
					float x = Float.parseFloat(strArray2[2]);
					float reserveTime = Float.parseFloat(strArray2[3]);
					if (strArray2[0].equals("1")) {		//pudding
						reserveEnemies.add(new Pudding(level, x, reserveTime, this));
					}
				}
				else if (strArray1[0].equals("BL")) {		//ball launcher
					//set ball launcher
				}
				else if (strArray1[0].equals("T")) {		//trigger
					float x = Float.parseFloat(strArray2[0]);
					float y = Float.parseFloat(strArray2[1]);
					float size = Float.parseFloat(strArray2[2]);
					TriggerType triggerType = TriggerType.valueOf(strArray2[3]);
					triggers.add(new Trigger(x, y, size, triggerType, world));
				}
				else if (strArray1[0].equals("BG")) {	//background
					if (strArray2[0].equals("grass")) {
						backgroundTexture = Assets.grassBackgroundTexture;
					}
					else if (strArray2[0].equals("desert")) {
						backgroundTexture = Assets.desertBackgroundTexture;
					}
				}
				line = br.readLine();
			}
			
			//sort reserveEnemies by reserveTime
			Collections.sort(reserveEnemies, new EnemyReserveTimeComparator());
			
			return true;
		} catch (Exception e) {
			return false;
		}
	}
	
	public void addActiveTrigger(Trigger trigger) {
		activeTriggers.add(trigger);
		int length = triggerEffects.size();
		for (int n = 0; n < length; n++) {
			if (triggerEffects.get(n).isMatchShapeCombo(activeTriggers)) {
				TriggerEffect triggerEffect = triggerEffects.get(n);
				triggerEffect.unlock();
				if (activeTriggerEffects.indexOf(triggerEffect) == -1) {
					triggerEffect.startEffect();
					activeTriggerEffects.add(triggerEffect);
				}
				else {
					triggerEffect.stopEffect();
					triggerEffect.startEffect();
				}
				activeTriggers.clear();
				break;
			}
		}
	}
	
	public void removeActiveTriggerEffect(TriggerEffect triggerEffect) {
		activeTriggerEffects.remove(triggerEffect);
	}
	
	public List<Ball> getBalls() {
		return balls;
	}
	
	public List<Enemy> getEnemies() {
		return enemies;
	}
	
	public World getWorld() {
		return world;
	}
	
	public Castle getCastle() {
		return castle;
	}
	
	public void addGoldFound(float gold) {
		goldFound += gold;
	}
	
	public void gameOver() {
		if (exitTime == 0) {
			exitTime = gameTime + 2.0f;
			isWin = false;
		}
	}
	
	public void win() {
		if (exitTime == 0) {
			exitTime = gameTime + 2.0f;
			isWin = true;
		}
	}
	
	public void update(float delta) {
		if (delta > Constant.minFPS) delta = Constant.minFPS;
		world.step(delta, Constant.velocityIterations, Constant.positionIterations);
		fixBounds();
		
		updateEnemies();
		castle.update();
		updateTriggerEffects();
		
		if (reserveEnemies.size() == 0 && enemies.size() == 0) {
			win();
		}
		
		for (int n = lastReserveProjectile; n < reserveProjectiles.size(); n++) {
			reserveProjectiles.get(n).disable();
		}
		lastReserveProjectile = reserveProjectiles.size();
		
		for (int n = lastReserveEnemyProjectile; n < reserveEnemyProjectiles.size(); n++) {
			reserveEnemyProjectiles.get(n).disable();
		}
		lastReserveEnemyProjectile = reserveEnemyProjectiles.size();
if (balls.get(0).getBody().getPosition().y < 0) {
	balls.get(0).getBody().setTransform(2.0f, 6.0f, 0.0f);
	balls.get(0).getBody().setLinearVelocity(0.0f, 0.0f);
}
	}
	
	public void addProjectile(float x, float y, float attack, float speed, float angle) {
		Projectile projectile;
		if (reserveProjectiles.size() > 0) {
			projectile = reserveProjectiles.get(reserveProjectiles.size() - 1);
			activeProjectiles.add(projectile);
			reserveProjectiles.remove(projectile);
		}
		else {
			projectile = new Projectile(this);
			projectile.addToWorld();
			activeProjectiles.add(projectile);
		}
		projectile.enable(x, y, attack, speed, angle);
	}
	
	public void addEnemyProjectile(float x, float y, float attack, float speed, float angle) {
		EnemyProjectile enemyProjectile;
		if (reserveEnemyProjectiles.size() > 0) {
			enemyProjectile = reserveEnemyProjectiles.get(reserveEnemyProjectiles.size() - 1);
			activeEnemyProjectiles.add(enemyProjectile);
			reserveEnemyProjectiles.remove(enemyProjectile);
		}
		else {
			enemyProjectile = new EnemyProjectile(this);
			enemyProjectile.addToWorld();
			activeEnemyProjectiles.add(enemyProjectile);
		}
		enemyProjectile.enable(x, y, attack, speed, angle);
	}
	
	public void removeProjectile(Projectile projectile) {
		reserveProjectiles.add(projectile);
		activeProjectiles.remove(projectile);
	}

	public void removeProjectile(EnemyProjectile enemyProjectile) {
		reserveEnemyProjectiles.add(enemyProjectile);
		activeEnemyProjectiles.remove(enemyProjectile);
	}
}
