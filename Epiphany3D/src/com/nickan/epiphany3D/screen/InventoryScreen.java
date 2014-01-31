package com.nickan.epiphany3D.screen;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.nickan.epiphany3D.Epiphany3D;
import com.nickan.epiphany3D.screen.inventoryview.Controller;
import com.nickan.epiphany3D.screen.inventoryview.Handler;
import com.nickan.epiphany3D.screen.inventoryview.Renderer;

public class InventoryScreen implements Screen {
	GameScreen gameScreen;
	Epiphany3D game;
	Controller controller;
	Handler handler;
	Renderer renderer;

	public InventoryScreen(Epiphany3D game, GameScreen gameScreen) {
		this.game = game;
		this.gameScreen = gameScreen;
		handler = new Handler(this, gameScreen.world.player);
//		renderer = new Renderer(handler, gameScreen.renderer.spriteBatch, gameScreen.renderer.arial, 
//				gameScreen.renderer.comic, gameScreen.renderer.fontShader);
		controller = new Controller(handler);
	}
	
	public void backToGameScreen() {
		game.setScreen(gameScreen);
		Gdx.input.setInputProcessor(gameScreen.worldCtrl);
	}

	@Override
	public void render(float delta) {
		renderer.render(delta);
	}

	@Override
	public void resize(int width, int height) {
		handler.resize(width, height);
		renderer.resize(width, height);
	}

	@Override
	public void show() {
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
