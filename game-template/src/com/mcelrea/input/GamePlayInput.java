package com.mcelrea.input;

import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.math.Vector2;
import com.mcelrea.screens.GamePlay;

public class GamePlayInput implements InputProcessor
{
	private float speed = 500;
	public static Vector2 movement = new Vector2();
	public static float zoom = 0;

	@Override
	public boolean keyDown(int keycode) {
		
		if(keycode == Keys.W)
		{
			movement.y = speed;
		}
		if(keycode == Keys.A)
		{
			movement.x = -speed;
		}
		if(keycode == Keys.D)
		{
			movement.x = speed;
		}
		if(keycode == Keys.S)
		{
			movement.y = -speed;
		}
		
		return true;
	}

	@Override
	public boolean keyUp(int keycode) {
		
		if(keycode == Keys.W)
		{
			movement.y = 0;
		}
		if(keycode == Keys.A)
		{
			movement.x = 0;
		}
		if(keycode == Keys.D)
		{
			movement.x = 0;
		}
		if(keycode == Keys.S)
		{
			movement.y = 0;
		}
		
		return true;
	}

	@Override
	public boolean keyTyped(char character) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean mouseMoved(int screenX, int screenY) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean scrolled(int amount) {
		GamePlay.camera.zoom += amount/25f;
		return true;
	}

}
