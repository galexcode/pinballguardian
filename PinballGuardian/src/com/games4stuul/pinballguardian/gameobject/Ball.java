package com.games4stuul.pinballguardian.gameobject;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.games4stuul.pinballguardian.Constant;
import com.games4stuul.pinballguardian.screen.GameScreen;

public class Ball {
	float x;
	float y;
	float size;
	float rimSize;
	GameScreen gameScreen;
	World world;
	Body body;
	Fixture ballFixture;
	Fixture rimFixture;
	
	float damageInterval;
	float attack;
	float baseAttack;
	
	public Ball(float x, float y, float size, float rimSize, GameScreen gameScreen) {
		this.x = x;
		this.y = y;
		this.size = size;
		this.rimSize = rimSize;
		this.gameScreen = gameScreen;
		world = gameScreen.getWorld();
		damageInterval = 0.2f;
		baseAttack = 30.0f;
		attack = baseAttack;
	}
	
	public void addToWorld() {
		BodyDef bodyDef = new BodyDef();
		bodyDef.type = BodyType.DynamicBody;
		bodyDef.position.set(x, y);
		bodyDef.bullet = true;
		bodyDef.linearDamping = Constant.damping;
		body = world.createBody(bodyDef);
		body.setUserData(this);
		CircleShape circleShape = new CircleShape();
		circleShape.setRadius(size);
		FixtureDef fixtureDef = new FixtureDef();
		fixtureDef.shape = circleShape;
		fixtureDef.density = 1.0f;
		fixtureDef.friction = 0.0f;
		fixtureDef.restitution = Constant.restitution;
		fixtureDef.filter.categoryBits = Constant.ballCategory;
		fixtureDef.filter.maskBits = Constant.wallCategory | Constant.triggerCategory | Constant.enemyCategory;
		ballFixture = body.createFixture(fixtureDef);
		
		circleShape = new CircleShape();
		circleShape.setRadius(size + rimSize);
		fixtureDef = new FixtureDef();
		fixtureDef.shape = circleShape;
		fixtureDef.density = 0.0f;
		fixtureDef.filter.categoryBits = Constant.weaponCategory;
		fixtureDef.filter.maskBits = Constant.enemyCategory;
		rimFixture = body.createFixture(fixtureDef);
	}
	
	public float getDamageInterval() {
		return damageInterval;
	}
	
	public float getAttack() {
		return attack;
	}
	
	public Body getBody() {
		return body;
	}
	
	public void beginAttackEffect(float attackIncrease) {
		attack = baseAttack * (1.0f + attackIncrease);
	}
	
	public void endAttackEffect() {
		attack = baseAttack;
	}
	
	public void beginBulletEffect(float attack, float speed, float angle) {
		Vector2 position = body.getPosition();
		gameScreen.addProjectile(position.x, position.y, attack, speed, angle);
	}
	
	public void endBulletEffect() {
	}
	
	public void beginRimEffect(float rimIncrease) {
		rimFixture.getShape().setRadius(size + rimSize * (1.0f + rimIncrease));
	}
	
	public void endRimEffect() {
		rimFixture.getShape().setRadius(size + rimSize);
	}

	public void render(ShapeRenderer shapeRenderer) {
		Vector2 position = body.getPosition();
		
		Gdx.gl.glEnable(GL10.GL_BLEND);
		shapeRenderer.begin(ShapeType.Filled);
			shapeRenderer.setColor(0, 0, 1, 0.3f);
			shapeRenderer.circle(position.x, position.y, size, 15);
			shapeRenderer.circle(position.x, position.y, rimFixture.getShape().getRadius(), 20);
		shapeRenderer.end();
		Gdx.gl.glDisable(GL10.GL_BLEND);
	}
}
