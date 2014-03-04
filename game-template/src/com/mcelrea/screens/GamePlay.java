package com.mcelrea.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.ChainShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.mcelrea.input.GamePlayInput;

public class GamePlay implements Screen{

	//The world class manages all physics entities, dynamic simulation, and asynchronous queries.
	private World world;

	//draws boxes, joints, velocities, contacts, lets us debug our game
	private Box2DDebugRenderer debugRenderer;

	public static OrthographicCamera camera; //2d camera

	private final float TIMESTEP = 1 / 60f; //1/60th of a secondj , 60 FPS
	private final int VELOCITYITERATIONS = 8; //pretty common, makes the world stable
	private final int POSITIONITERATIONS = 3; //pretty common, makes the world stable

	private Body box;

	@Override
	public void render(float delta) {
		//set the color to clear the screen to
		Gdx.gl.glClearColor(0, 0, 0, 1);
		//clear the screen to the glClearColor
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		/*
		 * TIMESTEP - the amount of time to simulate
		 * VELOCITYITERATIONS - for the velocity constraint solver
		 * POSITIONITERATIONS - for the position constriaint solver
		 * 
		 * world.step performs collision detection , integration, and constraint solution
		 */
		world.step(TIMESTEP, VELOCITYITERATIONS, POSITIONITERATIONS);
		box.applyForceToCenter(GamePlayInput.movement, false);

		//the camera will follow our box
		camera.position.set(box.getPosition().x, box.getPosition().y, 0);

		//Recalculates the projection and view matrix of this camera and the Frustum planes. Use this after you've manipulated any of the attributes of the camera.
		camera.update();

		//show the debug objects on the screen
		debugRenderer.render(world, camera.combined);
	}

	@Override
	public void resize(int width, int height) {
		/*
		 * viewport has nothing to do with the window size
		 * viewport determines how many pixels are shown in the window at a time
		 * It can effect zoom levels because more pixels zooms camera out
		 */
		camera.viewportWidth = width/25;
		camera.viewportHeight = height/25;
	}

	@Override
	public void show() {
		//World(gravity, sleep)
		world = new World(new Vector2(0, -9.81f), true);
		debugRenderer = new Box2DDebugRenderer();
		camera = new OrthographicCamera();

		Gdx.input.setInputProcessor(new GamePlayInput());

		//independent of actual use.  Re-used several times
		BodyDef bodyDef = new BodyDef();
		FixtureDef fixtureDef = new FixtureDef();

		/*
		 * Body Types
		 * --------------
		 * DynamicBody - objects which move around and are affected by forces and other dynamic, 
		 *               kinematic and static objects. Dynamic bodies are suitable for any object 
		 *               which needs to move and be affected by forces.
		 * StaticBody - objects which do not move and are not affected by forces. Dynamic bodies 
		 *              are affected by static bodies. Static bodies are perfect for ground, walls, 
		 *              and any object which does not need to move. Static bodies require less 
		 *              computing power.
		 * KinematicBody - Kinematic bodies are somewhat in between static and dynamic bodies. 
		 *                 Like static bodies, they do not react to forces, but like dynamic bodies, 
		 *                 they do have the ability to move. Kinematic bodies are great for things 
		 *                 where you, the programmer, want to be in full control of a body's motion, 
		 *                 such as a moving platform in a platform game.
		 */


		//ground
		bodyDef.type = BodyType.StaticBody;
		bodyDef.position.scl(0,0);
		ChainShape groundShape = new ChainShape();
		groundShape.createChain(new Vector2[]{new Vector2(-10,0), new Vector2(10,0)});
		fixtureDef.shape = groundShape;
		fixtureDef.friction = 0.5f;
		fixtureDef.restitution = 0;
		world.createBody(bodyDef).createFixture(fixtureDef);
		groundShape.dispose();

		//ground
		bodyDef.type = BodyType.StaticBody;
		bodyDef.position.scl(0,0);
		groundShape = new ChainShape();
		groundShape.createChain(new Vector2[]{new Vector2(-10,20), new Vector2(10,20)});
		fixtureDef.shape = groundShape;
		fixtureDef.friction = 0.5f;
		fixtureDef.restitution = 0;
		world.createBody(bodyDef).createFixture(fixtureDef);
		groundShape.dispose();

		//rectangle
		bodyDef.type = BodyType.DynamicBody;
		bodyDef.position.set(new Vector2(2.25f, 10));
		PolygonShape boxShape = new PolygonShape();//create a polygon -> many sided figure
		boxShape.setAsBox(.5f, 1);//create the box .5 meters wide, 1 meter tall
		fixtureDef.shape = boxShape;//shape of the box2D object
		fixtureDef.friction = .75f; //how much resistance when dragged across a surface 0-1.0
		fixtureDef.restitution = 0.5f;//reflective force, how much bounce, 0-1.0
		fixtureDef.density = 4; //mass in kg per square meter
		box = world.createBody(bodyDef);//add the body to the world
		box.createFixture(fixtureDef);//add the fixture to the box
		boxShape.dispose(); //erase the boxShape, we are done with it, free up memory

	}

	@Override
	public void hide() {

	}

	@Override
	public void pause() {

	}

	@Override
	public void resume() {

	}

	@Override
	public void dispose() {
		//dispose = get rid of resources, to free up memory
		//failure to do this will result in memory leaks and your
		//game will "eat" resources!
		world.dispose();
		debugRenderer.dispose();
	}

}


