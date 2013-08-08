package com.games4stuul.pinballguardian.listener;

import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.games4stuul.pinballguardian.gameobject.Ball;
import com.games4stuul.pinballguardian.gameobject.Castle;
import com.games4stuul.pinballguardian.gameobject.EnemyProjectile;
import com.games4stuul.pinballguardian.gameobject.Projectile;
import com.games4stuul.pinballguardian.gameobject.Trigger;
import com.games4stuul.pinballguardian.gameobject.Wall;
import com.games4stuul.pinballguardian.gameobject.enemy.Enemy;
import com.games4stuul.pinballguardian.screen.GameScreen;

public class ContactListener implements com.badlogic.gdx.physics.box2d.ContactListener {
	GameScreen gameScreen;
	
	public ContactListener(GameScreen gameScreen) {
		super();
		this.gameScreen = gameScreen;
	}

	@Override
	public void beginContact(Contact contact) {
		Body bodyA = contact.getFixtureA().getBody();
		Body bodyB = contact.getFixtureB().getBody();
		
		//ball collision
		if (bodyA.getUserData() instanceof Ball || bodyB.getUserData() instanceof Ball) {
			Object other;
			if (bodyA.getUserData() instanceof Ball) {
				other = bodyB.getUserData();
			}
			else {
				other = bodyA.getUserData();
			}
			
			if (other instanceof Trigger) {
				Trigger trigger = (Trigger) other;
				gameScreen.addActiveTrigger(trigger);
			}
		}
		
		//projectile collision
		else if (bodyA.getUserData() instanceof Projectile || bodyB.getUserData() instanceof Projectile) {
			Projectile projectile;
			Object other;
			if (bodyA.getUserData() instanceof Projectile) {
				projectile = (Projectile) bodyA.getUserData();
				other = bodyB.getUserData();
			}
			else {
				projectile = (Projectile) bodyB.getUserData();
				other = bodyA.getUserData();
			}
			
			if (other instanceof Enemy) {
				contact.setEnabled(false);
				Enemy enemy = (Enemy) other;
				enemy.applyDamage(projectile.getAttack());
				gameScreen.removeProjectile(projectile);
			}
			else if (other instanceof Wall) {
				contact.setEnabled(false);
				gameScreen.removeProjectile(projectile);
			}
		}
		
		//enemy projectile collision
		else if (bodyA.getUserData() instanceof EnemyProjectile || bodyB.getUserData() instanceof EnemyProjectile) {
			EnemyProjectile enemyProjectile;
			Object other;
			if (bodyA.getUserData() instanceof Projectile) {
				enemyProjectile = (EnemyProjectile) bodyA.getUserData();
				other = bodyB.getUserData();
			}
			else {
				enemyProjectile = (EnemyProjectile) bodyB.getUserData();
				other = bodyA.getUserData();
			}
			
			if (other instanceof Castle) {
				contact.setEnabled(false);
				Castle castle = (Castle) other;
				castle.applyDamage(enemyProjectile.getAttack());
				gameScreen.removeProjectile(enemyProjectile);
			}
		}
	}

	@Override
	public void endContact(Contact contact) {
	}

	@Override
	public void preSolve(Contact contact, Manifold oldManifold) {
		Body bodyA = contact.getFixtureA().getBody();
		Body bodyB = contact.getFixtureB().getBody();
		
		//ball collision
		if (bodyA.getUserData() instanceof Ball || bodyB.getUserData() instanceof Ball) {
			Ball ball;
			Object other;
			if (bodyA.getUserData() instanceof Ball) {
				ball = (Ball) bodyA.getUserData();
				other = bodyB.getUserData();
			}
			else {
				ball = (Ball) bodyB.getUserData();
				other = bodyA.getUserData();
			}
			
			if (other instanceof Enemy) {
				contact.setEnabled(false);
				Enemy enemy = (Enemy) other;
				float time = (float) System.nanoTime() / 1000000000.0f;
				if (time - enemy.lastDamageTime > ball.getDamageInterval()) {
					enemy.applyDamage(ball.getAttack());
					enemy.lastDamageTime = time;
				}
			}
			else if (other instanceof Trigger) {
				contact.setEnabled(false);
			}
		}
	}

	@Override
	public void postSolve(Contact contact, ContactImpulse impulse) {
		// TODO Auto-generated method stub
		
	}

}
