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
	Body body; //players body
	Fixture myFixture; //players only fixture on the body
	float force; //force applied when moving
	boolean jumping; //is the player jumping or not
	final float MAX_VELOCITY = 5; //maximum speed the player can move left and right
	final float jumpForce = 450; //the force applied when the jump button is pressed
	boolean alive; //is the player alive or not
	boolean canJump; //can the player currently jump, a powerup is needed in order to give the player the jump ability
	Sprite image; //the image of the player
	
	public Player(World world, float x, float y)
	{
		force = 20;//force applied when moving
		alive = true;//the player starts out alive
		image = new Sprite(new Texture("img/fireball.png"));//load the image of the player
		
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
		
		image.setSize(1.2f, 1.2f);
		body.setUserData(image);

	}//end Player constructor
	
	public void moveRight()
	{
		//if the player is not already at maximum velcity
		if(body.getLinearVelocity().x < MAX_VELOCITY)
		{
			//apply more force in the right direction
			body.applyLinearImpulse(force, 
									0, 
									body.getLocalCenter().x, 
									body.getLocalCenter().y, 
									true);
		}//end if
	}//end moveRight
	
	public void moveLeft()
	{
		//if the player is not already at maximum velocity
		if(body.getLinearVelocity().x > -MAX_VELOCITY)
		{
			//apply more force in the left direction
			body.applyLinearImpulse(-force, 
									0, 
									body.getLocalCenter().x, 
									body.getLocalCenter().y, 
									true);
		}//end if
	}//end moveLeft
	
	public void jump()
	{
		//if the player has the power up to allow jumping AND the player is not currently jumping
		if(canJump && !jumping)
		{
			//apply force in the upwards direction (jump)
			body.applyLinearImpulse(0, 
									jumpForce, 
									body.getLocalCenter().x, 
									body.getLocalCenter().y, 
									true);
			
			jumping = true;//set jumping to true since the player just jumped, this prevents infinite jumping ability
		}//end if
	}//end jump
	
	//a method that will tell others if the player is jumping or not
	public boolean isJumping()
	{
		return jumping;
	}//end isJumping
	
	//a method to allow others to stop the players jump
	public void setJumping(boolean j)
	{
		jumping = j;
	}//end setJumping
	
	//a method that allows others to get the players body, allowing them to change my (x,y) position
	public Body getBody() {
		return body;
	}//end getBody
	
	//a method to allow others to see if the player is alive or dead
	public boolean isAlive() {
		return alive;
	}//end isAlive

	//a method to others to kill the player or bring the player back to life
	public void setAlive(boolean alive) {
		this.alive = alive;
	}//end setAlive
	
	//a method that will tell others if the player is currently allowed to jump or not
	//jumping ability is obtained through a powerup
	public boolean isCanJump() {
		return canJump;
	}//end isCanJump

	//a method that will allow others to give the player the ability to jump
	public void setCanJump(boolean canJump) {
		this.canJump = canJump;
	}//end setCanJump

	//a method that will allow others to get the image associated with the player
	public Sprite getImage() {
		return image;
	}//end getImage

	//this method is no longer used!!!!!!!!!!!!!!
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
		}//end for loop
	}//end updateJump
}//end class Player




