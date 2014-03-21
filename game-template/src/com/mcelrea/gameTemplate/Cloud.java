package com.mcelrea.gameTemplate;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.mcelrea.levels.Level_1;

public class Cloud 
{
	Body myBody;
	float rainDropSpawnTime = 0.001f;//how often to drop rain
	float rainCounter = 0; //count the passage of time
	
	public Cloud(float x, float y, World world)
	{
		BodyDef bodyDef = new BodyDef();
		FixtureDef fixtureDef = new FixtureDef();
		
		bodyDef.type = BodyType.KinematicBody;
		bodyDef.position.x = x;
		bodyDef.position.y = y;
		PolygonShape box = new PolygonShape();
		box.setAsBox(2, 1);
		fixtureDef.shape = box;
		myBody = world.createBody(bodyDef);
		myBody.createFixture(fixtureDef);
		myBody.getFixtureList().get(0).setUserData("cloud");
	}
	
	public void act(World world)
	{
		rainCounter += Gdx.graphics.getDeltaTime();
		
		if(rainCounter >= rainDropSpawnTime)
		{
			rainCounter = 0;
			
			BodyDef bodyDef = new BodyDef();
			FixtureDef fixtureDef = new FixtureDef();
			bodyDef.type = BodyType.DynamicBody;
			bodyDef.position.x = myBody.getPosition().x  + ((float)(-2 + Math.random()*4));
			bodyDef.position.y = myBody.getPosition().y;
			CircleShape circle = new CircleShape();
			circle.setRadius(0.1f);
			fixtureDef.shape = circle;
			Body rain = world.createBody(bodyDef);
			rain.createFixture(fixtureDef);
			rain.setLinearVelocity(0, -0.2f);
			rain.getFixtureList().get(0).setUserData("rain");
			Level_1.rainDrops.add(rain);
		}
	}
}












