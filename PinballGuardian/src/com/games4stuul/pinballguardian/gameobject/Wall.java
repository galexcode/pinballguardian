package com.games4stuul.pinballguardian.gameobject;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.EdgeShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.games4stuul.pinballguardian.Constant;

public class Wall {
	World world;
	private Body body;
	float[] vertices;
	
	public Wall(float[] vertices, World world) {
		this.world = world;
		this.vertices = vertices;
	}
	
	public void addToWorld() {
		BodyDef bodyDef = new BodyDef();
		bodyDef.type = BodyType.StaticBody;
		bodyDef.position.set(0, 0);
		body = world.createBody(bodyDef);
		body.setUserData(this);
		FixtureDef fixtureDef = new FixtureDef();
		fixtureDef.density = 1.0f;
		fixtureDef.friction = 0.0f;
		fixtureDef.restitution = Constant.restitution;
		fixtureDef.filter.categoryBits = Constant.wallCategory;
		fixtureDef.filter.maskBits = Constant.ballCategory;
		
		EdgeShape edgeShape = new EdgeShape();
		fixtureDef.shape = edgeShape;
		int length = vertices.length;
		for (int n = 3; n < length; n+=2) {
			edgeShape.set(
					new Vector2(vertices[n-3], vertices[n-2]), 
					new Vector2(vertices[n-1], vertices[n])
			);
			body.createFixture(fixtureDef);
		}
	}
	
	public Body getBody() {
		return body;
	}

	public void render(ShapeRenderer shapeRenderer) {
		shapeRenderer.setColor(1, 1, 1, 1);
		shapeRenderer.begin(ShapeType.Line);
			int length = vertices.length;
			for (int n = 3; n < length; n += 2) {
				shapeRenderer.line(vertices[n-3], vertices[n-2], 
						vertices[n-1], vertices[n]);
			}
		shapeRenderer.end();
	}
}
