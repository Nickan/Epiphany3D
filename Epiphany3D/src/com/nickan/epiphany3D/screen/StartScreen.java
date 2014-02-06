package com.nickan.epiphany3D.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.nickan.epiphany3D.Epiphany3D;

public class StartScreen implements Screen {
	Skin skin;
	TextureAtlas atlas;
	Stage stage;
	Button startButton;
	Button soundButton;
	Epiphany3D game;
	BitmapFont arial;
	Label startLabel;
	
	public StartScreen(Epiphany3D game) {
		this.game = game;
	}
	
	@Override
	public void render(float delta) {
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);
		Gdx.gl.glClearColor(0, 0, 0, 1);
		
		stage.act();
		stage.draw();
		
		SpriteBatch batch = (SpriteBatch) stage.getSpriteBatch();
		batch.begin();
		startLabel.draw(batch, 1);
		batch.end();
	}

	@Override
	public void resize(int width, int height) {
		if (stage == null) {
			stage = new Stage(width, height, false);
		} else {
			stage.setViewport(width, height);
		}
		Gdx.input.setInputProcessor(stage);
		
		arial = new BitmapFont(Gdx.files.internal("graphics/fonts/arial.fnt"));
		arial.setScale(width / Epiphany3D.WIDTH, height / Epiphany3D.HEIGHT);
		
		atlas = new TextureAtlas("graphics/startscreentextures.pack");
		skin = new Skin(atlas);
		
		startButton = new Button(skin.getDrawable("startnormal"), skin.getDrawable("startpressed"));
		startButton.setSize(width / 16f * 4, height / 12f);
		startButton.setPosition(width / 2f - (startButton.getWidth() / 2), height / 2f - (startButton.getHeight() / 2));
		startButton.addListener(new InputListener() {
			
			@Override
			public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
				return true;
			}
			
			@Override
			public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
				game.gameScreen = new GameScreen(game);
				game.setScreen(game.gameScreen);
			}
			
		});
		
		soundButton = new Button(skin.getDrawable("soundonnormal"), skin.getDrawable("soundonpressed"));
		soundButton.setBounds(width / 16f, height / 12f, width / 16f * 2, height / 12f * 2);
		soundButton.addListener(new InputListener() {
			@Override
			public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
				
				return true;
			}
			
			@Override
			public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
				if (soundButton.getStyle().up.equals(skin.getDrawable("soundonnormal"))) {
					soundButton.getStyle().up = skin.getDrawable("soundoffnormal");
					soundButton.getStyle().down = skin.getDrawable("soundoffpressed");
				} else {
					soundButton.getStyle().up = skin.getDrawable("soundonnormal");
					soundButton.getStyle().down = skin.getDrawable("soundonpressed");
				}
			}
			
		});
		
		LabelStyle lStyle = new LabelStyle(arial, Color.YELLOW);
		startLabel = new Label("START!", lStyle);
		startLabel.setSize(width / 16f * 4, height / 12f);
		startLabel.setPosition(width / 2f - (startLabel.getWidth() / 4), height / 2f - (startLabel.getHeight() / 2));
		
		stage.addActor(startButton);
		stage.addActor(soundButton);
	}

	@Override
	public void show() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void hide() {
		// TODO Auto-generated method stub
		
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
		skin.dispose();
		atlas.dispose();
		stage.dispose();
	}

}
