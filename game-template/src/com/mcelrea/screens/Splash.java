package com.mcelrea.screens;

import aurelienribon.tweenengine.BaseTween;
import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenCallback;
import aurelienribon.tweenengine.TweenManager;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.mcelrea.tweens.SpriteAccessor;

public class Splash implements Screen
{
	//a collection of Sprites
	SpriteBatch batch;
	//a 2D image
	Sprite image;
	
	TweenManager tweenManager;
	

	@Override
	public void render(float delta) {
		//set the color to clear the screen to
		Gdx.gl.glClearColor(0, 0, 0, 1);
		//clear the screen to the glClearColor
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		tweenManager.update(delta);

		batch.begin();
		image.draw(batch);
		batch.end();
	}

	@Override
	public void resize(int width, int height) {
		// TODO Auto-generated method stub

	}

	@Override
	public void show() {
		batch = new SpriteBatch();
		Texture t = new Texture("img/cool-blue-background-wallpapers-16.jpg");
		
		tweenManager = new TweenManager();
		Tween.registerAccessor(Sprite.class, new SpriteAccessor());
		
		image = new Sprite(t);
		image.setSize(800, 600);//make it full screen
		
		//set the start point of the tween
		Tween.set(image, SpriteAccessor.ALPHA).target(0).start(tweenManager);
		Tween.to(image, SpriteAccessor.ALPHA, 1).target(1).repeatYoyo(1, 0.2f).setCallback(new TweenCallback() {
			
			@Override
			public void onEvent(int type, BaseTween<?> source) {
				((Game)Gdx.app.getApplicationListener()).setScreen(new Menu());
			}
		}).start(tweenManager);
		

	}

	@Override
	public void hide() {
		// TODO Auto-generated method stub
		dispose();
	}

	@Override
	public void pause() {
		// TODO Auto-generated method stub

	}

	@Override
	public void resume() {
		// TODO Auto-generated method stub

	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub

	}

}
