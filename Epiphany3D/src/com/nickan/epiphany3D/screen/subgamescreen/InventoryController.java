package com.nickan.epiphany3D.screen.subgamescreen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;

public class InventoryController {
	InventoryScreen screen;

	public InventoryController(InventoryScreen screen) {
		this.screen = screen;
		initializeButtons();
	}

	private void initializeButtons() {
		initializeEquipmentSlot();
		initializePositiveButton();
		initializeResumeButton();
		initializeItemSlots();
	}
	
	private void initializeEquipmentSlot() {
		screen.bodySlot.addListener(new InputListener() {
			public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
				return true;
			}

			public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
				
			}
		});
		
		screen.footSlot.addListener(new InputListener() {
			public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
				return true;
			}

			public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
				
			}
		});
		
		screen.glovesSlot.addListener(new InputListener() {
			public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
				return true;
			}

			public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
				
			}
		});
		
		screen.headSlot.addListener(new InputListener() {
			public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
				return true;
			}

			public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
				
			}
		});
		
		screen.leftHandSlot.addListener(new InputListener() {
			public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
				return true;
			}

			public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
				
			}
		});
		
		screen.rightHandSlot.addListener(new InputListener() {
			public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
				return true;
			}

			public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
				
			}
		});

	}
	
	private void initializeItemSlots() {
		
	}
	
	
	private void initializePositiveButton() {
		screen.positiveButton.addListener(new InputListener() {
			public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
				return true;
			}

			public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
			}
		});
	}
	
	private void initializeResumeButton() {
		screen.resumeButton.addListener(new InputListener() {
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
