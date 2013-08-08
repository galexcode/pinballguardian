package com.games4stuul.pinballguardian.gameobject.enemy;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
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

public class Enemy {
	float x0;
	float y0;
	float size;
	float level;
	float hp;
	float attack;
	float defense;
	float hpMax;
	float speed;
	float attackSpeed;
	float attackRate;
	float gold;
	float reserveTime;
	float attackRange;
	float projectileSpeed;
	World world;
	Body body;
	GameScreen gameScreen;
	boolean isAttacking;
	float lastAttackingTime;
	public float lastDamageTime;
	TextureRegion[] walkTextures;
	TextureRegion[] attackTextures;
	TextureRegion[] deadTexture;
	Animation walkAnimation;
	Animation attackAnimation;
	TextureRegion currentFrame;
	float spawnTime;
	float alpha;
	float deathTime;
	
	//add attack speed
	public Enemy(float level, float x, float reserveTime, GameScreen gameScreen, float size, float attackRange, float projectileSpeed, 
			float baseHpMax, float baseAttack, float baseDefense, float baseSpeed, float baseAttackSpeed, float baseGold, 
			float hpMaxGrowth, float attackGrowth, float defenseGrowth, float speedGrowth, float attackSpeedGrowth, float goldGrowth, 
			TextureRegion[] walkTextures, TextureRegion[] attackTextures, TextureRegion[] deadTexture) {
		this.level = level;
		x0 = x;
		y0 = 7.0f + size;
		this.reserveTime = reserveTime;
		this.gameScreen = gameScreen;
		world = gameScreen.getWorld();
		this.walkTextures = walkTextures; 
		this.attackTextures = attackTextures;
		this.deadTexture = deadTexture;
		
		this.size = size;
		this.attackRange = attackRange;
		this.projectileSpeed = projectileSpeed;
		hpMax = baseHpMax + (level - 1) * hpMaxGrowth;
		attack = baseAttack + (level - 1) * attackGrowth;
		defense = baseDefense + (level - 1) * defenseGrowth;
		speed = baseSpeed + (level - 1) * speedGrowth;
		attackSpeed = baseAttackSpeed + (level - 1) * attackSpeedGrowth;
		gold = baseGold + (level - 1) * goldGrowth;
		
		lastDamageTime = 0;
		hp = hpMax;
		isAttacking = false;
	}
	
	public void addToWorld() {
		//make body
		BodyDef bodyDef = new BodyDef();
		bodyDef.type = BodyType.DynamicBody;
		bodyDef.position.set(x0, y0);
		body = world.createBody(bodyDef);
		body.setUserData(this);
		
		//add fixtures
		FixtureDef fixtureDef = new FixtureDef();
		CircleShape circleShape = new CircleShape();
		circleShape.setRadius(size);
		fixtureDef.shape = circleShape;
		fixtureDef.density = 1.0f;
		fixtureDef.friction = 0.0f;
		fixtureDef.restitution = 0.8f;
		fixtureDef.filter.categoryBits = Constant.enemyCategory;
		fixtureDef.filter.maskBits = Constant.weaponCategory;
		body.setGravityScale(0.0f);
		body.setLinearVelocity(0 ,-speed);
		body.createFixture(fixtureDef);
		
		walkAnimation = new Animation(0.05f / speed, walkTextures);
		spawnTime = (float) System.nanoTime() / 1000000000.0f;
		alpha = 1.0f;
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
		float time = (float) System.nanoTime() / 1000000000.0f;
		if (hp == 0.0f) {
			if (alpha == 1.0f) {
				body.setLinearVelocity(0.0f, 0.0f);
				speed = 0.0f;
				gameScreen.addGoldFound(gold);
				deathTime = time;
				alpha = 0.99f;
			}
			else if (alpha == 0.0f) {
				world.destroyBody(body);
				body.setUserData(null);
				body = null;
				gameScreen.getEnemies().remove(this);
			}
		}
		else if (isAttacking) {
			if (time - lastAttackingTime > 1 / attackSpeed) {
				if (projectileSpeed > 0) {
					Vector2 position = body.getPosition();
					gameScreen.addEnemyProjectile(position.x, position.y - size, attack, projectileSpeed, -1.57f);
				}
				else {
					gameScreen.getCastle().applyDamage(attack);
				}
				attackAnimation = new Animation(0.25f / attackSpeed, attackTextures);
				lastAttackingTime = time;
			}
		}
		else {
			//reach attack range
			if (body.getPosition().y - attackRange < gameScreen.getCastle().getY()) {
				body.setTransform(body.getPosition().x, gameScreen.getCastle().getY() + attackRange, 0.0f);
				body.setLinearVelocity(0.0f, 0.0f);
				isAttacking = true;
				update();
			}
		}
	}
	
	public Body getBody() {
		return body;
	}
	
	public float getHpMax() {
		return hpMax;
	}
	
	public float getSize() {
		return size;
	}
	
	public float getReserveTime() {
		return reserveTime;
	}
	
	public void reduceSpeed(float effectStrength) {
		if (!isAttacking) {
			body.setLinearVelocity(0, -speed * (1.0f - effectStrength));
		}
	}
	
	public void restoreSpeed() {
		if (!isAttacking) {
			body.setLinearVelocity(0, -speed);
		}
	}
	
	public void applyStun() {
		reduceSpeed(1.0f);
		isAttacking = false;
	}
	
	public void undoStun() {
		restoreSpeed();
	}
	
	public float getHp() {
		return hp;
	}
	
	public void render(SpriteBatch spriteBatch, ShapeRenderer shapeRenderer) {
		Vector2 position = body.getPosition();
		float time = (float) System.nanoTime() / 1000000000.0f;
		
		spriteBatch.begin();
			if (alpha == 1.0f) {
				if (!isAttacking) {
					currentFrame = walkAnimation.getKeyFrame(time - spawnTime, true);
				}
				else {
					currentFrame = attackAnimation.getKeyFrame(time - lastAttackingTime, false);
				}
				spriteBatch.draw(currentFrame, 
						position.x - size, position.y - size, 
						size * 2.0f, size * 2.0f);
			}
			else {
				alpha = 0.99f - (time - deathTime);
				if (alpha < 0.0f) {
					alpha = 0.0f;
				}
				currentFrame = deadTexture[0];
				spriteBatch.setColor(1.0f, 1.0f, 1.0f, alpha);
				spriteBatch.draw(currentFrame, 
						position.x - size, position.y - size, 
						size * 2.0f, size * 2.0f);
				spriteBatch.setColor(1.0f, 1.0f, 1.0f, 1.0f);
			}
		spriteBatch.end();
		
		shapeRenderer.begin(ShapeType.Filled);
			shapeRenderer.setColor(1, 0, 0, 1);
			shapeRenderer.rect(position.x - 0.15f, position.y - size - 0.05f, hp / hpMax * 0.3f, 0.03f);
		shapeRenderer.end();
		
		shapeRenderer.begin(ShapeType.Line);
			shapeRenderer.setColor(0, 0, 0, 1);
			shapeRenderer.rect(position.x - 0.15f, position.y - size - 0.05f, 0.3f, 0.03f);
		shapeRenderer.end();
	}
}
