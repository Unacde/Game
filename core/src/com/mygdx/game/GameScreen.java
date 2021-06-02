package com.mygdx.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.TimeUtils;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

import java.io.Console;
import java.util.Iterator;

public class GameScreen implements Screen {
	final Drop game;
	SpriteBatch batch;
	Texture img;
	OrthographicCamera camera;
	Vector3 touchPos;
	Rectangle bucket;
	Texture dimg;
	Array<Rectangle> raindrops;
	long lastDropTime;
    int dropsTaked;
    int dropSpeed;
	public GameScreen (final Drop gam) {



		this.game = gam;
		touchPos = new Vector3();

		camera = new OrthographicCamera();
		camera.setToOrtho(false,800,480);


		batch = new SpriteBatch();
		img = new Texture("Buck.png");
		dimg = new Texture("raindrop.png");

		bucket = new Rectangle();

		bucket.x = 800/2 - 64/2;
		bucket.y = 20;

		bucket.height = 64;
		bucket.width = 64;

		dropSpeed =200;
		raindrops = new Array<Rectangle>();
		spawnRainDrop();


	}

	private void spawnRainDrop(){
		Rectangle raindrop = new Rectangle();
		raindrop.x = MathUtils.random(0,800-64);
		raindrop.y = 480;
		raindrop.width = 64;
		raindrop.height = 64;
		raindrops.add(raindrop);
		lastDropTime = TimeUtils.nanoTime();
		dropSpeed+=10;
	}
	@Override
	public void render (float delta) {
		Gdx.gl.glClearColor(0,0, 0.2f, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		camera.update();

		game.batch.setProjectionMatrix(camera.combined);
		game.batch.begin();
		game.font.draw(game.batch,"Drops Taked:" + dropsTaked,0,480);
		game.batch.draw(img, bucket.x, bucket.y);
		for (Rectangle raindrop: raindrops){
			game.batch.draw(dimg,raindrop.x,raindrop.y);
		}
		game.batch.end();

		if(Gdx.input.isTouched()){
			touchPos.set(Gdx.input.getX(),Gdx.input.getY(),0);
			camera.unproject(touchPos);
			bucket.x = (int) (touchPos.x - 64/2);
		}

		if (bucket.x < 0 ) bucket.x = 0;
		if (bucket.x > 800 - 64) bucket.x = 800 - 64;

		if(TimeUtils.nanoTime() - lastDropTime > 1000000000) spawnRainDrop();
		Iterator<Rectangle> iter = raindrops.iterator();
		while (iter.hasNext()){
			Rectangle raindrop = iter.next();

			raindrop.y -= dropSpeed * Gdx.graphics.getDeltaTime();

			if(raindrop.y + 64 < 0) iter.remove();
			if(raindrop.overlaps(bucket)){
				dropsTaked++;
				iter.remove();
			}
		}
	}

	@Override
	public void resize(int width, int height) {

	}

	@Override
	public void pause() {

	}

	@Override
	public void resume() {

	}

	@Override
	public void hide() {

	}

	@Override
	public void dispose () {
		batch.dispose();
		img.dispose();
		dimg.dispose();
	}
	@Override
	public void show(){

	}
}
