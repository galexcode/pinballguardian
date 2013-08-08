package com.games4stuul.pinballguardian.gameobject;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.games4stuul.pinballguardian.Constant;
import com.games4stuul.pinballguardian.screen.GameScreen;

public class EnemyProjectile {
	static final float size = 0.02f;
	
	GameScreen gameScreen;
	World world;
	Body body;
	
	float attack;
	
	public EnemyProjectile(GameScreen gameScreen) {
		this.gameScreen = gameScreen;
		world = gameScreen.getWorld();
	}
	
	public void addToWorld() {
		//make body
		BodyDef bodyDef = new BodyDef();
		bodyDef.type = BodyType.DynamicBody;
		bodyDef.position.set(0, 0);
		body = world.createBody(bodyDef);
		body.setBullet(true);
		body.setUserData(this);
		
		//add fixtures
		FixtureDef fixtureDef = new FixtureDef();
		CircleShape circleShape = new CircleShape();
		circleShape.setRadius(size);
		fixtureDef.shape = circleShape;
		fixtureDef.filter.categoryBits = Constant.enemyWeaponCategory;
		fixtureDef.filter.maskBits = Constant.castleCategory;
		body.setGravityScale(0.0f);
		body.createFixture(fixtureDef);
		body.setActive(false);
	}
	
	public void enable(float x, float y, float attack, float speed, float angle) {
		body.setActive(true);
		body.setTransform(x, y, 0);
		body.setLinearVelocity((float) Math.cos(angle) * speed, (float) Math.sin(angle) * speed);
		this.attack = attack;
	}
	
	public void disable() {
		body.setActive(false);
	}
	
	public float getAttack() {
		return attack;
	}

	public void render(ShapeRenderer shapeRenderer) {
		Vector2 position = body.getPosition();
		
		shapeRenderer.setColor(1, 1, 1, 1);
		shapeRenderer.begin(ShapeType.Filled);
			shapeRenderer.circle(position.x, position.y, size, 5);
		shapeRenderer.end();
	}
}
