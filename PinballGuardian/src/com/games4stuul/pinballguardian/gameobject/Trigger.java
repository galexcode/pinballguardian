package com.games4stuul.pinballguardian.gameobject;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.World;
import com.games4stuul.pinballguardian.Constant;
import com.games4stuul.pinballguardian.enums.TriggerType;

public class Trigger {
	float x;
	float y;
	float size;
	TriggerType triggerType;
	World world;
	Body body;
	float[] vertices;
	
	public Trigger(float x, float y, float size, TriggerType triggerType, World world) {
		this.world = world;
		this.x = x;
		this.y = y;
		this.size = size;
		this.triggerType = triggerType;
	}
	
	public void addToWorld() {
		//make body
		BodyDef bodyDef = new BodyDef();
		bodyDef.type = BodyType.StaticBody;
		bodyDef.position.set(x, y);
		body = world.createBody(bodyDef);
		body.setUserData(this);
		
		//add fixtures
		FixtureDef fixtureDef = new FixtureDef();
		CircleShape circleShape = new CircleShape();
		circleShape.setRadius(size);
		fixtureDef.shape = circleShape;
		fixtureDef.filter.categoryBits = Constant.triggerCategory;
		fixtureDef.filter.maskBits = Constant.ballCategory;
		body.createFixture(fixtureDef);
	}
	
	public TriggerType getTriggerType() {
		return triggerType;
	}

	public void render(ShapeRenderer shapeRenderer) {
		shapeRenderer.begin(ShapeType.Filled);
			shapeRenderer.circle(x, y, size, 40);
		shapeRenderer.end();
	}
}
