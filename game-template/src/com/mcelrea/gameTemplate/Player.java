package com.mcelrea.gameTemplate;

import java.util.ArrayList;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.ui.List;
import com.badlogic.gdx.utils.Array;

public class Player 
{
	Body body;
	Fixture myFixture;
	float force;
	boolean jumping;
	final float MAX_VELOCITY = 5;
	final float jumpForce = 450;
	boolean alive;
	boolean canJump;
	Sprite image;
	
	public Player(World world, float x, float y)
	{
		force = 20;
		alive = true;
		image = new Sprite(new Texture("img/fireball.png"));
		
		BodyDef bodyDef = new BodyDef();
		FixtureDef fixtureDef = new FixtureDef();
		
		bodyDef.type = BodyType.DynamicBody;
		bodyDef.position.set(new Vector2(x,y));
		CircleShape circleShape = new CircleShape();
		circleShape.setRadius(0.5f);
		fixtureDef.shape = circleShape;//shape of the box2D object
		fixtureDef.friction = 1f; //how much resistance when dragged across a surface 0-1.0
		fixtureDef.restitution = 0f;//reflective force, how much bounce, 0-1.0
		fixtureDef.density = 70; //mass in kg per square meter
		body = world.createBody(bodyDef);//add the body to the world
		myFixture = body.createFixture(fixtureDef);//add the fixture to the box
		body.getFixtureList().get(0).setUserData("player");
		body.setFixedRotation(true);
		circleShape.dispose(); //erase the boxShape, we are done with it, free up memory
	}
	
	public void moveRight()
	{
		if(body.getLinearVelocity().x < MAX_VELOCITY)
		{
			body.applyLinearImpulse(force, 
									0, 
									body.getLocalCenter().x, 
									body.getLocalCenter().y, 
									true);
		}
	}
	
	public void moveLeft()
	{
		if(body.getLinearVelocity().x > -MAX_VELOCITY)
		{
			body.applyLinearImpulse(-force, 
									0, 
									body.getLocalCenter().x, 
									body.getLocalCenter().y, 
									true);
		}
	}
	
	public void jump()
	{
		if(canJump && !jumping)
		{
			body.applyLinearImpulse(0, 
									jumpForce, 
									body.getLocalCenter().x, 
									body.getLocalCenter().y, 
									true);
			
			jumping = true;
		}
	}
	
	public boolean isJumping()
	{
		return jumping;
	}
	
	public void setJumping(boolean j)
	{
		jumping = j;
	}
	
	public Body getBody() {
		return body;
	}
	

	public boolean isAlive() {
		return alive;
	}

	public void setAlive(boolean alive) {
		this.alive = alive;
	}
	
	public boolean isCanJump() {
		return canJump;
	}

	public void setCanJump(boolean canJump) {
		this.canJump = canJump;
	}

	public Sprite getImage() {
		return image;
	}

	public void updateJump(World world)
	{
		Array<Contact> contactList = world.getContactList();
		
		//go through everything I can hit
		for(int i=0; i < contactList.size; i++)
		{
			Contact contact = contactList.get(i);
			
			//if a fixture is touching AND the fixture is me
			/*
			if(contact.isTouching())
			{
				jumping = false;
				
			}
			*/
		}
	}
}




