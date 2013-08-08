package com.games4stuul.pinballguardian.gameobject;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.EdgeShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.joints.RevoluteJoint;
import com.badlogic.gdx.physics.box2d.joints.RevoluteJointDef;
import com.games4stuul.pinballguardian.Constant;

public class RightFlipper {
	World world;
	float x;
	float y;
	float size;
	float angle;
	float moveRange;
	float[] vertices;
	Body body;
	Body connectorBody;
	RevoluteJoint revoluteJoint;
	
	public RightFlipper(float x, float y, float size, float angle, float moveRange, World world) {
		this.x = x;
		this.y = y;
		this.size = size;
		this.angle = angle;
		this.world = world;
		this.moveRange = moveRange;
		
		vertices = new float[] {
				-this.size, -this.size * 0.1f, 
				0.0f, -this.size * 0.1f, 
				0.0f, 0.0f, 
				-this.size, 0.0f
		};
	}
	
	public Body getBody() {
		return body;
	}
	
	public void flipDown() {
		revoluteJoint.setMotorSpeed(Constant.flipperAngularSpeed);
	}
	
	public void flipUp() {
		revoluteJoint.setMotorSpeed(-Constant.flipperAngularSpeed);
	}
	
	public void moveLeft() {
		if (connectorBody.getPosition().x > -moveRange) {
			connectorBody.setLinearVelocity(-Constant.flipperTranslationalSpeed, 0);
		}
	}
	
	public void moveRight() {
		if (connectorBody.getPosition().x < moveRange) {
			connectorBody.setLinearVelocity(Constant.flipperTranslationalSpeed, 0);
		}
	}
	
	public void moveStop() {
		connectorBody.setLinearVelocity(0, 0);
	}
	
	public void fixMoveLimit() {
		float movePosition = connectorBody.getPosition().x;
		if (movePosition < -moveRange) {
			connectorBody.setTransform(-moveRange, 0, 0);
			moveStop();
		}
		else if (movePosition > moveRange) {
			connectorBody.setTransform(moveRange, 0, 0);
			moveStop();
		}
	}
	
	public void addToWorld() {
		BodyDef bodyDef = new BodyDef();
		bodyDef.type = BodyType.DynamicBody;
		bodyDef.gravityScale = 0.0f;
		bodyDef.position.set(new Vector2(x, y));
		bodyDef.angle = angle + 0.6f;
		FixtureDef fixtureDef = new FixtureDef();
		fixtureDef.density = 9999.0f;
		fixtureDef.friction = 0.0f;
		fixtureDef.restitution = Constant.restitution;
		fixtureDef.filter.categoryBits = Constant.wallCategory;
		fixtureDef.filter.maskBits = Constant.ballCategory;
		PolygonShape polygonShape = new PolygonShape();
		polygonShape.set(vertices);
		fixtureDef.shape = polygonShape;
		body = world.createBody(bodyDef);
		body.setUserData(this);
		body.createFixture(fixtureDef);
		
		bodyDef = new BodyDef();
		bodyDef.type = BodyType.KinematicBody;
		bodyDef.position.set(0, 0);
		fixtureDef = new FixtureDef();
		fixtureDef.density = 9999.0f;
		fixtureDef.friction = 0.0f;
		fixtureDef.restitution = 0.8f;
		fixtureDef.filter.categoryBits = Constant.wallCategory;
		fixtureDef.filter.maskBits = Constant.ballCategory;
		EdgeShape edgeShape = new EdgeShape();
		edgeShape.set(new Vector2(x, y), new Vector2(x + Constant.worldWidth, y + Constant.worldWidth));
		fixtureDef.shape = edgeShape;
		connectorBody = world.createBody(bodyDef);
		connectorBody.setUserData(this);
		connectorBody.createFixture(fixtureDef);
		
		RevoluteJointDef revoluteJointDef = new RevoluteJointDef();
		revoluteJointDef.bodyA = connectorBody;
		revoluteJointDef.bodyB = body;
		revoluteJointDef.localAnchorA.set(new Vector2(x, y));
		revoluteJointDef.localAnchorB.set(0, 0);
		revoluteJointDef.enableLimit = true;
		revoluteJointDef.upperAngle = angle + 0.6f;
		revoluteJointDef.lowerAngle = angle - 0.6f;
		revoluteJointDef.enableMotor = true;
		revoluteJointDef.maxMotorTorque = Constant.maxFlipperTorque * fixtureDef.density;
		revoluteJoint = (RevoluteJoint) world.createJoint(revoluteJointDef);
		
		flipDown();
	}

	public void render(ShapeRenderer shapeRenderer) {
		shapeRenderer.translate(x, y, 0);
		shapeRenderer.rotate(0, 0, 1, body.getAngle() * 57.3f);
		shapeRenderer.setColor(1, 1, 1, 1);
		shapeRenderer.begin(ShapeType.Line);
			shapeRenderer.polygon(vertices);
		shapeRenderer.end();
		shapeRenderer.identity();
	}
}
