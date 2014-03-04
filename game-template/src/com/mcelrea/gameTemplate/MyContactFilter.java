package com.mcelrea.gameTemplate;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.ChainShape;
import com.badlogic.gdx.physics.box2d.ContactFilter;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.mcelrea.levels.Level_1;
import com.mcelrea.screens.Menu;

public class MyContactFilter implements ContactFilter
{

	@Override
	public boolean shouldCollide(Fixture fixtureA, Fixture fixtureB) {
		// TODO Auto-generated method stub
		if(fixtureA.getUserData().equals("platform") && fixtureB.getUserData().equals("player"))
		{
			//get the bottom y value of both the platform and the player
			Vector2 pos = new Vector2();
			((ChainShape)fixtureA.getShape()).getVertex(0, pos);
			float platform_y = pos.y;
			float player_y = fixtureB.getBody().getPosition().y - 0.5f;
			
			//if the platform is beneath my player
			if(platform_y < player_y)
				Level_1.player1.setJumping(false);
			
			if(Level_1.player1.getBody().getLinearVelocity().y < -12)
			{
				Level_1.player1.setAlive(false);
				Level_1.player1.setCanJump(false);
			}
		}
		else if(fixtureA.getUserData().equals("player") && fixtureB.getUserData().equals("platform"))
		{
			//get the bottom y value of both the platform and the player
			Vector2 pos = new Vector2();
			((ChainShape)fixtureB.getShape()).getVertex(0, pos);
			float platform_y = pos.y;
			float player_y = fixtureA.getBody().getPosition().y - 0.5f;
			
			//if the platform is beneath my player
			if(platform_y < player_y)
				Level_1.player1.setJumping(false);
			
			if(Level_1.player1.getBody().getLinearVelocity().y < -12)
			{
				Level_1.player1.setAlive(false);
				Level_1.player1.setCanJump(false);
			}
		}
		
		
		if(fixtureA.getUserData().equals("jump_power_up") && fixtureB.getUserData().equals("player"))
		{
			Level_1.player1.setCanJump(true);
			return false;
		}
		if(fixtureA.getUserData().equals("player") && fixtureB.getUserData().equals("jump_power_up"))
		{
			Level_1.player1.setCanJump(true);
			return false;
		}
		



		
		
		
		
		
		
		
		
		
		if(fixtureA.getUserData().equals("door") && fixtureB.getUserData().equals("player"))
		{
			((Game)Gdx.app.getApplicationListener()).setScreen(new Menu());
		}
		if(fixtureA.getUserData().equals("player") && fixtureB.getUserData().equals("door"))
		{
			((Game)Gdx.app.getApplicationListener()).setScreen(new Menu());
		}
		
		return true;
	}

}







