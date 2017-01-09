package com.mygdx.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapRenderer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;

import java.util.Random;

public class MyGdxGame extends ApplicationAdapter {
	SpriteBatch batch;
	Texture img, zombieImg;
	TiledMap tiledMap;
	TiledMapRenderer tiledMapRenderer;
	OrthographicCamera camera;
	float x, y, xv, yv, zx, zy, zxv, zyv;
	static final float MAX_VELOCITY = 100;
	TextureRegion tree;
	TextureRegion up, upStep, down, downStep, left, right, standRight, standLeft;
	TextureRegion zombieUp, zombieUpStep, zombieDown, zombieDownStep, zombieLeft, zombieRight, zombieStepRight, zombieStepLeft;
	Animation walkRight, walkLeft, walkUp, walkDown, zombieWalkRight, zombieWalkLeft, zombieWalkUp, zombieWalkDown;
	float time;
	int random;
	Random rand = new Random();

	static final int WIDTH = 16;
	static final int HEIGHT = 16;

	static final int DRAW_WIDTH = WIDTH*3;
	static final int DRAW_HEIGHT = HEIGHT*3;

	float acceleration = 2f;

	@Override
	public void create () {
		batch = new SpriteBatch();

		Texture tiles = new Texture("tiles.png");
		TextureRegion[][] grid = TextureRegion.split(tiles, 16, 16);
		camera = new OrthographicCamera();
		camera.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		camera.update();
		tiledMap = new TmxMapLoader().load("level1.tmx");
		tiledMapRenderer = new OrthogonalTiledMapRenderer(tiledMap, 1.7f , batch);

		tree = new TextureRegion(tiles,0, 8, 16, 16);
		down = grid[6][0];
		downStep = new TextureRegion(down);
		downStep.flip(true,false);
		up = grid[6][1];
		upStep = new TextureRegion(up);
		upStep.flip(true, false);
		right = grid[6][3];
		left = new TextureRegion(right);
		left.flip(true, false);
		standRight = grid [6][2];
		standLeft = new TextureRegion(standRight);
		standLeft.flip(true, false);
		walkRight = new Animation(0.2f, standRight, right);
		walkLeft = new Animation(.2f, standLeft, left);
		walkUp = new Animation(.2f, up,upStep);
		walkDown = new Animation(.2f, down, downStep);

		zombieDown = grid[6][4];
		zombieDownStep = new TextureRegion(zombieDown);
		zombieDownStep.flip(true,false);
		zombieUp = grid[6][5];
		zombieUpStep = new TextureRegion(zombieUp);
		zombieUpStep.flip(true, false);
		zombieRight = grid [6][6];
		zombieLeft = new TextureRegion(zombieRight);
		zombieLeft.flip(true,false);
		zombieStepRight = grid[6][7];
		zombieStepLeft = new TextureRegion(zombieStepRight);
		zombieStepLeft.flip(true,false);
		zombieWalkRight = new Animation(.2f, zombieRight, zombieStepRight);
		zombieWalkLeft = new Animation(.2f, zombieLeft, zombieStepLeft);
		zombieWalkUp = new Animation(.2f, zombieUp, zombieUpStep);
		zombieWalkDown = new Animation(.2f, zombieDown, zombieDownStep);
	}

	@Override
	public void render () {
		time += Gdx.graphics.getDeltaTime();
		move();
		zombieMove();

		TextureRegion img;
		if (yv < 0) {
			img = walkDown.getKeyFrame(time, true);
		} else if (yv > 0){
			img = walkUp.getKeyFrame(time, true);
		} else if (xv < 0) {
			img = walkLeft.getKeyFrame(time, true);
		} else if (xv > 0) {
			img = walkRight.getKeyFrame(time, true);
		} else {
			img = standRight;
		}

		TextureRegion zombieImg;
		if (zyv < 0) {
			zombieImg = zombieWalkDown.getKeyFrame(time,true);
		} else if (zyv > 0) {
			zombieImg = zombieWalkUp.getKeyFrame(time, true);
		} else if (zxv < 0) {
			zombieImg = zombieWalkLeft.getKeyFrame(time, true);
		} else if (zxv > 0) {
			zombieImg = zombieWalkRight.getKeyFrame(time, true);
		} else {
			zombieImg = zombieRight;
		}

		Gdx.gl.glClearColor(0.5f, 1, 0.5f, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		camera.update();
		tiledMapRenderer.setView(camera);
		tiledMapRenderer.render();
		batch.begin();
		batch.draw(img, x, y, DRAW_HEIGHT, DRAW_WIDTH);
		batch.draw(zombieImg, zx, zy, DRAW_HEIGHT, DRAW_WIDTH);
		batch.draw(tree, 0,150, DRAW_HEIGHT, DRAW_WIDTH);
		batch.draw(tree, 275, 290, DRAW_HEIGHT, DRAW_WIDTH);
		batch.draw(tree, 450, 140, DRAW_HEIGHT, DRAW_WIDTH);
		batch.draw(tree, 550, 425, DRAW_HEIGHT, DRAW_WIDTH);
		batch.end();
	}

	public float decelerate(float velocity) {
		float deceleration = 0.95f;
		velocity *= deceleration;
		if (Math.abs(velocity) < 1) {
			velocity = 0;
		}
		return velocity;
	}

	public void move() {
		if (Gdx.input.isKeyPressed(Input.Keys.UP)) {
			yv = MAX_VELOCITY;
		}
		if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
			yv = MAX_VELOCITY * -1;
		}
		if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
			xv = MAX_VELOCITY;
		}
		if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
			xv = MAX_VELOCITY * -1;
		}
		if (Gdx.input.isKeyPressed(Input.Keys.SPACE) && Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
			yv = MAX_VELOCITY * -1 * acceleration;
		}
		if (Gdx.input.isKeyPressed(Input.Keys.SPACE) && Gdx.input.isKeyPressed(Input.Keys.UP)) {
			yv = MAX_VELOCITY * acceleration;
		}
		if (Gdx.input.isKeyPressed(Input.Keys.SPACE) && Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
			xv = MAX_VELOCITY * acceleration;
		}
		if (Gdx.input.isKeyPressed(Input.Keys.SPACE) && Gdx.input.isKeyPressed(Input.Keys.LEFT))  {
			xv = MAX_VELOCITY * -1 * acceleration;
		}

		y += yv * Gdx.graphics.getDeltaTime();
		x += xv * Gdx.graphics.getDeltaTime();

		yv = decelerate(yv);
		xv = decelerate(xv);

		//if guy reaches left side, set x = width of screen and turn him around
		//if guy reaches right side of the screen, set x =0 and turn him around
		if (y < 0 ){
			y = Gdx.graphics.getHeight();
		}
		if (y > Gdx.graphics.getHeight()) {
			y = 0;
		}
		if (x < 0) {
			x = Gdx.graphics.getWidth();
		}
		if (x > Gdx.graphics.getWidth()) {
			x = 0;
		}
	}
	 public void zombieMove () {

		 random = rand.nextInt(4);
		 if (random == 1){
			 zy--;
			 zyv = MAX_VELOCITY * -1;
		 } else if (random == 2) {
			 zy++;
			 zyv = MAX_VELOCITY;
		 }else if (random == 3) {
			 zx--;
			 zxv = MAX_VELOCITY * -1;
		 }else if (random == 4) {
			 zx++;
			 zxv = MAX_VELOCITY;
		 }

		 zy += zyv * Gdx.graphics.getDeltaTime();
		 zx += zxv * Gdx.graphics.getDeltaTime();

		 if (zy <0) {
			 zy = Gdx.graphics.getHeight();
		 }
		 if (zy > Gdx.graphics.getHeight()) {
			 zy = 0;
		 }
		 if (zx < 0) {
			 zx = Gdx.graphics.getWidth();
		 }
		 if (zx > Gdx.graphics.getWidth()) {
			 zx = 0;
		 }
	 }

	@Override
	public void dispose () {
		batch.dispose();
		img.dispose();
		zombieImg.dispose();
	}
}
