package com.nickan.epiphany3D.screen.subgamescreen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.nickan.epiphany3D.model.items.Inventory;

public class InventoryController {
	InventoryScreen screen;
	Inventory inventory;

	public InventoryController(InventoryScreen screen) {
		this.screen = screen;
		initializeButtons();
		
		// The heck is this??? (Poor system design)
	}

	private void initializeButtons() {
		initializeEquipmentSlot();
		initializeAddButtons();
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
		Button[][] itemSlots = screen.itemSlots;
		for (int row = 0; row < itemSlots.length; ++row) {
			for (int col = 0; col < itemSlots[row].length; ++col) {
				itemSlots[row][col].addListener(new InputListener() {
					public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
						return true;
					}

					public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
						
					}
				} );
			}
		}
	}
	
	
	private void initializeAddButtons() {
		screen.addButtonStr.addListener(new InputListener() {
			public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
				return true;
			}

			public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
			}
		});
		
		screen.addButtonDex.addListener(new InputListener() {
			public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
				return true;
			}

			public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
			}
		});
		
		screen.addButtonVit.addListener(new InputListener() {
			public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
				return true;
			}

			public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
			}
		});
		
		screen.addButtonAgi.addListener(new InputListener() {
			public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
				return true;
			}

			public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
			}
		});
		
		screen.addButtonWis.addListener(new InputListener() {
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
