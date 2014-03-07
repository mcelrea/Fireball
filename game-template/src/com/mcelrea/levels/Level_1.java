package com.mcelrea.levels;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.ChainShape;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.mcelrea.gameTemplate.MyContactFilter;
import com.mcelrea.gameTemplate.Player;
import com.mcelrea.input.GamePlayInput;

public class Level_1 implements Screen{

	//The world class manages all physics entities, dynamic simulation, and asynchronous queries.
	private World world;

	public static Player player1;

	//draws boxes, joints, velocities, contacts, lets us debug our game
	private Box2DDebugRenderer debugRenderer;

	public static OrthographicCamera camera; //2d camera

	private final float TIMESTEP = 1 / 60f; //1/60th of a secondj , 60 FPS
	private final int VELOCITYITERATIONS = 8; //pretty common, makes the world stable
	private final int POSITIONITERATIONS = 3; //pretty common, makes the world stable

	private Body box;
	private Body plat1, plat2, plat3, plat4, plat5, plat6, plat7, plat8, ground;
	private Body jumpPower;
	Label velocityLabel;
	SpriteBatch batch;
	BitmapFont velFont;

	@Override
	public void render(float delta) {
		//set the color to clear the screen to
		Gdx.gl.glClearColor(0, 0, 0, 1);
		//clear the screen to the glClearColor
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		update();

		/*
		 * TIMESTEP - the amount of time to simulate
		 * VELOCITYITERATIONS - for the velocity constraint solver
		 * POSITIONITERATIONS - for the position constriaint solver
		 * 
		 * world.step performs collision detection , integration, and constraint solution
		 */
		world.step(TIMESTEP, VELOCITYITERATIONS, POSITIONITERATIONS);
		//box.applyForceToCenter(GamePlayInput.movement, false);

		//the camera will follow our box
		//camera.position.set(box.getPosition().x, box.getPosition().y, 0);

		//Recalculates the projection and view matrix of this camera and the Frustum planes. Use this after you've manipulated any of the attributes of the camera.
		camera.update();

		/*
		 * Begin the batch, which means to begin drawing to the screen
		 * You cannot draw to the screen until batch.begin is called.
		 */
		batch.setProjectionMatrix(camera.combined);
		batch.begin();
		Sprite s = ((Sprite)player1.getBody().getUserData());
		s.setPosition(player1.getBody().getPosition().x - s.getWidth()/2, 
				      player1.getBody().getPosition().y - s.getHeight()/2);
		s.draw(batch);
		/*
		 * Put some information text onto the screen.  This will help us
		 * see what is happening in our game.  We will remove this when the game
		 * is complete.
		 */
		velFont.setColor(new Color(1,1,1,1));
		velFont.draw(batch, "x-velocity: " + player1.getBody().getLinearVelocity().x, 10, 580);
		velFont.draw(batch, "y-velocity: " + player1.getBody().getLinearVelocity().y, 10, 565);
		velFont.draw(batch, "jumping: " + player1.isJumping(), 170, 580);
		velFont.draw(batch, "x: " + player1.getBody().getPosition().x, 170, 565);
		velFont.draw(batch, "y: " + player1.getBody().getPosition().y, 170, 550);
		velFont.draw(batch, "body x: " + player1.getBody().getPosition().x, 400, 565);
		velFont.draw(batch, "body y: " + player1.getBody().getPosition().y, 400, 550);
		velFont.draw(batch, "alive: " + player1.isAlive(), 170, 535);

		//place platforms y-value
		Vector2 pos = new Vector2();
		((ChainShape)plat1.getFixtureList().get(0).getShape()).getVertex(0, pos);
		velFont.draw(batch, "" + pos.y, 70, 500);


		/*
		 * End the batch, which means to end drawing to the screen
		 * You must tell libgdx when you are done drawing things
		 */
		batch.end();

		//show the debug objects on the screen
		debugRenderer.render(world, camera.combined);
	}//end render

	@Override
	public void resize(int width, int height) {
		/*
		 * viewport has nothing to do with the window size
		 * viewport determines how many pixels are shown in the window at a time
		 * It can effect zoom levels because more pixels zooms camera out
		 */
		camera.viewportWidth = width/25;
		camera.viewportHeight = height/25;
	}//end resize

	@Override
	public void show() {
		
		world = new World(new Vector2(0, -9.81f), true);//create the world with (gravity, sleep)
		debugRenderer = new Box2DDebugRenderer(); //create a debugrenderer which will draw all the box2d elements onto the screen for us
		camera = new OrthographicCamera(); //create a 2D camera to "look" at the game world
		player1 = new Player(world, -15,9);//create a player in the world at (-15,9) x,y location
		batch = new SpriteBatch();//create a new SpriteBatch for drawing images, text, etc onto the screen
		
		velFont = new BitmapFont();//create a new font.  This will use the default font.  We could specify an actual font with some more code

		Gdx.input.setInputProcessor(new GamePlayInput());
		
		/*
		 * Tell the world to check the class MyContactFilter for specifications on
		 * what should happen when two Box2D objects collide.  In the class MyContactFilter
		 * we can specify different actions to take when different things collide.  For
		 * example, when a bullet hits an enemy kill the enemy, don't let my own bullets kill
		 * me, etc.
		 */
		world.setContactFilter(new MyContactFilter());

		//independent of actual use.  Re-used several times
		BodyDef bodyDef = new BodyDef();
		FixtureDef fixtureDef = new FixtureDef();
		Body body;

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


		//start platform (platform 1)
		bodyDef.type = BodyType.StaticBody;
		ChainShape groundShape = new ChainShape();//ChainShape is a line that must contain at least two (x,y) points though it may contain more
		groundShape.createChain(new Vector2[]{new Vector2(-17,8), new Vector2(-10,8)});//create the actual line by passing in (x,y) points
		fixtureDef.shape = groundShape;
		fixtureDef.friction = 0.5f;
		fixtureDef.restitution = 0;
		plat1 = world.createBody(bodyDef);
		plat1.createFixture(fixtureDef);
		/*
		 * it's important to name every fixture so we can identify it in the MyContactFilter class
		 * We will use these names to specify what happens when this fixture hits another fixture
		 */
		plat1.getFixtureList().get(0).setUserData("platform");
		groundShape.dispose();
		
		

		//ground
		bodyDef.type = BodyType.StaticBody;
		groundShape = new ChainShape();//ChainShape is a line that must contain at least two (x,y) points though it may contain more
		groundShape.createChain(new Vector2[]{new Vector2(-17,-10), new Vector2(17,-10)});//create the actual line by passing in (x,y) points
		fixtureDef.shape = groundShape;
		fixtureDef.friction = 0.5f;
		fixtureDef.restitution = 0;
		ground = world.createBody(bodyDef);
		ground.createFixture(fixtureDef);
		/*
		 * it's important to name every fixture so we can identify it in the MyContactFilter class
		 * We will use these names to specify what happens when this fixture hits another fixture
		 */
		ground.getFixtureList().get(0).setUserData("platform");
		groundShape.dispose();
		
		

		//platform 2
		bodyDef.type = BodyType.StaticBody;
		groundShape = new ChainShape();//ChainShape is a line that must contain at least two (x,y) points though it may contain more
		groundShape.createChain(new Vector2[]{new Vector2(-8,5), new Vector2(-1,5)});//create the actual line by passing in (x,y) points
		fixtureDef.shape = groundShape;
		fixtureDef.friction = 0.5f;
		fixtureDef.restitution = 0;
		plat2 = world.createBody(bodyDef);
		plat2.createFixture(fixtureDef);
		/*
		 * it's important to name every fixture so we can identify it in the MyContactFilter class
		 * We will use these names to specify what happens when this fixture hits another fixture
		 */
		plat2.getFixtureList().get(0).setUserData("platform");
		groundShape.dispose();
		
		

		//platform 3
		bodyDef.type = BodyType.StaticBody;
		groundShape = new ChainShape();//ChainShape is a line that must contain at least two (x,y) points though it may contain more
		groundShape.createChain(new Vector2[]{new Vector2(-13,2), new Vector2(-6,2)});//create the actual line by passing in (x,y) points
		fixtureDef.shape = groundShape;
		fixtureDef.friction = 0.5f;
		fixtureDef.restitution = 0;
		plat3 = world.createBody(bodyDef);
		plat3.createFixture(fixtureDef);
		/*
		 * it's important to name every fixture so we can identify it in the MyContactFilter class
		 * We will use these names to specify what happens when this fixture hits another fixture
		 */
		plat3.getFixtureList().get(0).setUserData("platform");
		groundShape.dispose();
		
		

		//platform 4
		bodyDef.type = BodyType.StaticBody;
		groundShape = new ChainShape();//ChainShape is a line that must contain at least two (x,y) points though it may contain more
		groundShape.createChain(new Vector2[]{new Vector2(-17,-1), new Vector2(-10,-1)});//create the actual line by passing in (x,y) points
		fixtureDef.shape = groundShape;
		fixtureDef.friction = 0.5f;
		fixtureDef.restitution = 0;
		plat4 = world.createBody(bodyDef);
		plat4.createFixture(fixtureDef);
		/*
		 * it's important to name every fixture so we can identify it in the MyContactFilter class
		 * We will use these names to specify what happens when this fixture hits another fixture
		 */
		plat4.getFixtureList().get(0).setUserData("platform");
		groundShape.dispose();
		
		

		//platform 5
		bodyDef.type = BodyType.StaticBody;
		groundShape = new ChainShape();//ChainShape is a line that must contain at least two (x,y) points though it may contain more
		groundShape.createChain(new Vector2[]{new Vector2(-11,-4), new Vector2(-4,-4)});//create the actual line by passing in (x,y) points
		fixtureDef.shape = groundShape;
		fixtureDef.friction = 0.5f;
		fixtureDef.restitution = 0;
		plat5 = world.createBody(bodyDef);
		plat5.createFixture(fixtureDef);
		/*
		 * it's important to name every fixture so we can identify it in the MyContactFilter class
		 * We will use these names to specify what happens when this fixture hits another fixture
		 */
		plat5.getFixtureList().get(0).setUserData("platform");
		groundShape.dispose();
		
		

		//platform 6
		bodyDef.type = BodyType.StaticBody;
		groundShape = new ChainShape();//ChainShape is a line that must contain at least two (x,y) points though it may contain more
		groundShape.createChain(new Vector2[]{new Vector2(-5,-7), new Vector2(2,-7)});//create the actual line by passing in (x,y) points
		fixtureDef.shape = groundShape;
		fixtureDef.friction = 0.5f;
		fixtureDef.restitution = 0;
		plat6 = world.createBody(bodyDef);
		plat6.createFixture(fixtureDef);
		/*
		 * it's important to name every fixture so we can identify it in the MyContactFilter class
		 * We will use these names to specify what happens when this fixture hits another fixture
		 */
		plat6.getFixtureList().get(0).setUserData("platform");
		groundShape.dispose();

		
		
		//platform 7
		bodyDef.type = BodyType.StaticBody;
		groundShape = new ChainShape();//ChainShape is a line that must contain at least two (x,y) points though it may contain more
		groundShape.createChain(new Vector2[]{new Vector2(-1, 7), new Vector2(6, 7)});//create the actual line by passing in (x,y) points
		fixtureDef.shape = groundShape;
		fixtureDef.friction = 0.5f;
		fixtureDef.restitution = 0;
		plat7 = world.createBody(bodyDef);
		plat7.createFixture(fixtureDef);
		/*
		 * it's important to name every fixture so we can identify it in the MyContactFilter class
		 * We will use these names to specify what happens when this fixture hits another fixture
		 */
		plat7.getFixtureList().get(0).setUserData("platform");
		groundShape.dispose();

		
		
		//platform 8 (end platform)
		bodyDef.type = BodyType.StaticBody;
		groundShape = new ChainShape();//ChainShape is a line that must contain at least two (x,y) points though it may contain more
		groundShape.createChain(new Vector2[]{new Vector2(9, 8), new Vector2(17, 8)});//create the actual line by passing in (x,y) points
		fixtureDef.shape = groundShape;
		fixtureDef.friction = 0.0f;
		fixtureDef.restitution = 0;
		plat8 = world.createBody(bodyDef);
		plat8.createFixture(fixtureDef);
		/*
		 * it's important to name every fixture so we can identify it in the MyContactFilter class
		 * We will use these names to specify what happens when this fixture hits another fixture
		 */
		plat8.getFixtureList().get(0).setUserData("platform");
		groundShape.dispose();

		
		
		//left barrier
		bodyDef.type = BodyType.StaticBody;
		groundShape = new ChainShape();//ChainShape is a line that must contain at least two (x,y) points though it may contain more
		groundShape.createChain(new Vector2[]{new Vector2(-16, 12), new Vector2(-16, -12)});//create the actual line by passing in (x,y) points
		fixtureDef.shape = groundShape;
		fixtureDef.friction = 0.0f;
		fixtureDef.restitution = 0;
		body = world.createBody(bodyDef);
		body.createFixture(fixtureDef);
		/*
		 * it's important to name every fixture so we can identify it in the MyContactFilter class
		 * We will use these names to specify what happens when this fixture hits another fixture
		 */
		body.getFixtureList().get(0).setUserData("wall");
		groundShape.dispose();

		
		
		//right barrier
		bodyDef.type = BodyType.StaticBody;
		groundShape = new ChainShape();//ChainShape is a line that must contain at least two (x,y) points though it may contain more
		groundShape.createChain(new Vector2[]{new Vector2(16, 12), new Vector2(16, -12)});//create the actual line by passing in (x,y) points
		fixtureDef.shape = groundShape;
		fixtureDef.friction = 0.5f;
		fixtureDef.restitution = 0;
		body = world.createBody(bodyDef);
		body.createFixture(fixtureDef);
		/*
		 * it's important to name every fixture so we can identify it in the MyContactFilter class
		 * We will use these names to specify what happens when this fixture hits another fixture
		 */
		body.getFixtureList().get(0).setUserData("wall");
		groundShape.dispose();

		
		
		//jump powerup
		bodyDef.type = BodyType.KinematicBody;
		CircleShape circle = new CircleShape();
		circle.setRadius(0.5f);
		circle.setPosition(new Vector2(8, -9));
		fixtureDef.shape = circle;
		fixtureDef.friction = 0.5f;
		fixtureDef.restitution = 0;
		body = world.createBody(bodyDef);
		body.createFixture(fixtureDef);
		/*
		 * it's important to name every fixture so we can identify it in the MyContactFilter class
		 * We will use these names to specify what happens when this fixture hits another fixture
		 */
		body.getFixtureList().get(0).setUserData("jump_power_up");
		circle.dispose();

		
		
		//door
		bodyDef.type = BodyType.StaticBody;
		bodyDef.position.set(new Vector2(13, 9));
		PolygonShape boxShape = new PolygonShape();//create a polygon -> many sided figure
		boxShape.setAsBox(.5f, 1);//create the box .5 meters wide, 1 meter tall
		fixtureDef.shape = boxShape;//shape of the box2D object
		fixtureDef.friction = .75f; //how much resistance when dragged across a surface 0-1.0
		fixtureDef.restitution = 0.5f;//reflective force, how much bounce, 0-1.0
		fixtureDef.density = 4; //mass in kg per square meter
		box = world.createBody(bodyDef);//add the body to the world
		box.createFixture(fixtureDef);//add the fixture to the box
		/*
		 * it's important to name every fixture so we can identify it in the MyContactFilter class
		 * We will use these names to specify what happens when this fixture hits another fixture
		 */
		box.getFixtureList().get(0).setUserData("door");
		boxShape.dispose(); //erase the boxShape, we are done with it, free up memory
		 
	}//end show


	public void update()
	{
		updatePlayer1();
	}//end update

	public void updatePlayer1()
	{
		//if the player is alive, allow them to move, jump, etc.
		if(player1.isAlive())
		{
			//calls the updateJump method in class Player
			player1.updateJump(world);

			//D -> move right
			if(Gdx.input.isKeyPressed(Keys.D))
			{
				//run the moveRight code from class Player
				player1.moveRight();
			}//end if
			//A -> move left
			if(Gdx.input.isKeyPressed(Keys.A))
			{
				//run the moveLeft code from class Player
				player1.moveLeft();
			}//end if
			//W -> jump
			if(Gdx.input.isKeyPressed(Keys.W))
			{
				//run the jump code from class Player
				player1.jump();
			}//end if
		}//end if(player1.isAlive())
		else //player must be dead
		{
			//if they press the number 1 key, put them back alive and move them to the start position again
			if(Gdx.input.isKeyPressed(Keys.NUM_1))
			{
				player1.setAlive(true);
				player1.getBody().setTransform(-15, 9, 0);
			}//end if
		}//end else

	}//end updatePlayer1


	@Override
	public void hide() {

	}//end hide

	@Override
	public void pause() {

	}//end pause

	@Override
	public void resume() {

	}//end resume

	@Override
	public void dispose() {
		//dispose = get rid of resources, to free up memory
		//failure to do this will result in memory leaks and your
		//game will "eat" resources!
		world.dispose();
		debugRenderer.dispose();
	}//end dispose

}



