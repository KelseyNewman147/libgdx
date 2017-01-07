package com.mygdx.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class MyGdxGame extends ApplicationAdapter {
	SpriteBatch batch;
	Texture img;
	float x, y, xv, yv;
	static final float MAX_VELOCITY = 100;
	TextureRegion up, upStep, down, downStep, left, right, standRight, standLeft;
	Animation walkRight, walkLeft, walkUp, walkDown;
	float time;

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



	}

	@Override
	public void render () {
		time += Gdx.graphics.getDeltaTime();
		move();

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

		Gdx.gl.glClearColor(0.5f, 1, 0.5f, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		batch.begin();
		batch.draw(img, x, y, DRAW_HEIGHT, DRAW_WIDTH);
		batch.end();
	}

	float decelerate(float velocity) {
		float deceleration = 0.95f;
		velocity *= deceleration;
		if (Math.abs(velocity) < 1) {
			velocity = 0;
		}
		return velocity;
	}

	void move() {
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

		//if in far right x = width of screen
		// use .graphics to find width & height of screen
		//if far left x = 0
		//if up y = height of screen
		//if down y = 0
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

	@Override
	public void dispose () {
		batch.dispose();
		img.dispose();
	}
}
