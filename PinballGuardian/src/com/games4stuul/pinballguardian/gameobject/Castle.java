package com.games4stuul.pinballguardian.gameobject;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.games4stuul.pinballguardian.Constant;
import com.games4stuul.pinballguardian.screen.GameScreen;

public class Castle {
	float y;
	float hpMax;
	float hp;
	float defense;
	GameScreen gameScreen;
	World world;
	Body body;
	
	public Castle(float y, float hpMax, float defense, GameScreen gameScreen) {
		this.y = y;
		this.hpMax = hpMax;
		hp = hpMax;
		this.defense = defense;
		this.gameScreen = gameScreen;
		world = gameScreen.getWorld();
	}
	
	public void addToWorld() {
		//make body
		BodyDef bodyDef = new BodyDef();
		bodyDef.type = BodyType.StaticBody;
		bodyDef.position.set(0, 0);
		body = world.createBody(bodyDef);
		body.setBullet(true);
		body.setUserData(this);
		
		//add fixtures
		FixtureDef fixtureDef = new FixtureDef();
		PolygonShape polygonShape = new PolygonShape();
		polygonShape.set(new float[] {
				0.0f, 0.0f, 
				Constant.worldWidth, 0.0f, 
				Constant.worldWidth, y, 
				0.0f, y
		});
		fixtureDef.shape = polygonShape;
		fixtureDef.filter.categoryBits = Constant.castleCategory;
		fixtureDef.filter.maskBits = Constant.enemyWeaponCategory;
		body.createFixture(fixtureDef);
	}
	
	public float getY() {
		return y;
	}
	
	public float getHp() {
		return hp;
	}
	
	public float getHpMax() {
		return hpMax;
	}
	
	public float getDefense() {
		return defense;
	}
	
	public void applyDamage(float attack) {
		float damage = attack - defense;
		if (damage < 0) damage = 0;
		hp -= damage;
		if (hp < 0.0f) {
			hp = 0.0f;
		}
	}
	
	public void update() {
		if (hp == 0.0f) {
			gameScreen.gameOver();
		}
	}

	public void render(ShapeRenderer shapeRenderer) {
		shapeRenderer.begin(ShapeType.Line);
			shapeRenderer.setColor(1, 1, 1, 1);
			shapeRenderer.rect(0, 0, Constant.worldWidth, y);
			shapeRenderer.setColor(0.8f, 0, 0, 1);
			shapeRenderer.rect(Constant.worldWidth / 2 - 0.4f, 0.1f, 0.8f, 0.03f);
		shapeRenderer.end();
		
		shapeRenderer.setColor(1, 0, 0, 1);
		shapeRenderer.begin(ShapeType.Filled);
			shapeRenderer.rect(Constant.worldWidth / 2 - 0.4f, 0.1f, hp / hpMax * 0.8f, 0.03f);
		shapeRenderer.end();
	}
}
