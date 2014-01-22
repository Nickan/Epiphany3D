package com.nickan.epiphany3D.screen.subgamescreen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.Button;

public class InventoryController {
	InventoryScreen screen;

	public InventoryController(InventoryScreen screen) {
		this.screen = screen;
		initializeButtons();
	}

	private void initializeButtons() {
		initializePositiveButton();
		initializeResumeButton();
	}
	
	private void initializePositiveButton() {
		Button positiveButton = screen.positiveButton;

		positiveButton.addListener(new InputListener() {
			public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
				return true;
			}

			public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
			}
		});
	}
	
	private void initializeResumeButton() {
		Button resumeButton = screen.resumeButton;
		
		resumeButton.addListener(new InputListener() {
			public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
		 		return true;
		 	}
		 
		 	public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
		 		Gdx.app.log("Example", "touch done at (" + x + ", " + y + ")");
		 		screen.game.setScreen(screen.gameScreen);
		 		Gdx.input.setInputProcessor(screen.gameScreen.worldCtrl);
		 	}
		});
	}
}
