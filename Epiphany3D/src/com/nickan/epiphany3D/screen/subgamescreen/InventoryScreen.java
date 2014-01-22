package com.nickan.epiphany3D.screen.subgamescreen;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.Align;
import com.nickan.epiphany3D.Epiphany3D;
import com.nickan.epiphany3D.screen.GameScreen;

public class InventoryScreen implements Screen {
	Stage stage;
	Button positiveButton;
	Button resumeButton;
	TextureAtlas atlas;
	Skin skin;
	GameScreen gameScreen;
	Epiphany3D game;
	InventoryController inventory;
	
	public InventoryScreen(Epiphany3D game, GameScreen gameScreen) {
		this.game = game;
		this.gameScreen = gameScreen;
	}

	@Override
	public void render(float delta) {
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT  | GL10.GL_DEPTH_BUFFER_BIT);
		Gdx.gl.glClearColor(0f, 0f, 0f, 1.0f);
		
		stage.act(delta);
		SpriteBatch batch = (SpriteBatch) stage.getSpriteBatch();
		batch.begin();
		batch.draw(skin.getRegion("pausebackground"), 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		batch.end();
		stage.draw();
	}

	@Override
	public void resize(int width, int height) {
		stage = new Stage(width, height, true, gameScreen.renderer.spriteBatch);
		stage.clear();
		Gdx.input.setInputProcessor(stage);
		
		float widthUnit = width / 16f;
		float heightUnit = height / 12f;
		
		positiveButton = new Button(skin.getDrawable("positivebuttonnormal"), skin.getDrawable("positivebuttonpressed"));
		positiveButton.align(Align.center);
		positiveButton.setSize(widthUnit, heightUnit);
		positiveButton.setPosition(widthUnit, heightUnit);
		
		resumeButton = new Button(skin.getDrawable("resumebuttonnormal"), skin.getDrawable("resumebuttonpressed"));
		resumeButton.setSize(widthUnit, heightUnit);
		resumeButton.setPosition(widthUnit * 15f, heightUnit * 11f);

		stage.addActor(positiveButton);
		stage.addActor(resumeButton);
		inventory = new InventoryController(this);
	}

	@Override
	public void show() {
		atlas = new TextureAtlas("graphics/inventorytexture.pack");
		skin = new Skin(atlas);
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
		// TODO Auto-generated method stub
		
	}
	
}
