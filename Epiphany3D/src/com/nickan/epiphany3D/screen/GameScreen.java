package com.nickan.epiphany3D.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Screen;
import com.nickan.epiphany3D.Epiphany3D;
import com.nickan.epiphany3D.view.gamescreenview.InputHandler;
import com.nickan.epiphany3D.view.gamescreenview.World;
import com.nickan.epiphany3D.view.gamescreenview.WorldRenderer;

public class GameScreen implements Screen {
	public World world;
	public WorldRenderer renderer;
	InputHandler worldController;
	public InputMultiplexer worldCtrl;

	public GameScreen(Epiphany3D game) {
		world = new World();
		renderer = new WorldRenderer(world);
		world.setWorldRenderer(renderer);
		this.worldController = new InputHandler(world, game);
		
		worldCtrl = new InputMultiplexer(renderer.stage, worldController);
		Gdx.input.setInputProcessor(worldCtrl);
	}

	@Override
	public void render(float delta) {
		world.update(delta);
		renderer.render(delta);
	}

	@Override
	public void resize(int width, int height) {
		// TODO Auto-generated method stub
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
		renderer.dispose();
	}
	
}
