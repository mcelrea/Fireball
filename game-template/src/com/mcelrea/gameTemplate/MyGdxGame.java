package com.mcelrea.gameTemplate;

import com.badlogic.gdx.Game;
import com.mcelrea.levels.Level_1;

public class MyGdxGame extends Game {
	
	@Override
	public void create() {	 	
		setScreen(new Level_1());
	}

	@Override
	public void dispose() {
		super.dispose();
	}

	@Override
	public void render() {		
		super.render();
		
	}

	@Override
	public void resize(int width, int height) {
		super.resize(width, height);
	}

	@Override
	public void pause() {
		super.pause();
	}

	@Override
	public void resume() {
		super.resume();
	}
}
