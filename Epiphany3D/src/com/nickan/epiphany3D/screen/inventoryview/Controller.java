package com.nickan.epiphany3D.screen.inventoryview;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.nickan.epiphany3D.model.StatisticsHandler;
import com.nickan.epiphany3D.model.items.Inventory;

public class Controller {
	Inventory inventory;
	Handler handler;

	public Controller(Handler handler) {
		this.handler = handler;
		initializeButtons();
		
		inventory = handler.player.inventory;
	}

	private void initializeButtons() {
		initializeEquipmentSlot();
		initializeAddButtons();
		initializeResumeButton();
		initializeItemSlots();
	}
	
	private void initializeEquipmentSlot() {
		handler.bodySlot.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				Inventory inventory = handler.player.inventory;
				if (inventory.getArmor() == null)
					return;

				// Considered a single click
				if (getTapCount() % 2 == 1) {
					
				} else {
					inventory.removeEquippedItem(inventory.getArmor());
				}
			}
		});
		
		handler.footSlot.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				Inventory inventory = handler.player.inventory;
				if (inventory.getBoots() == null)
					return;

				// Considered a single click
				if (getTapCount() % 2 == 1) {
					
				} else {
					inventory.removeEquippedItem(inventory.getBoots());
				}
			}
		});
		
		handler.glovesSlot.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				Inventory inventory = handler.player.inventory;
				if (inventory.getGloves() == null)
					return;

				// Considered a single click
				if (getTapCount() % 2 == 1) {
					
				} else {
					inventory.removeEquippedItem(inventory.getGloves());
				}
			}
		});
		
		handler.headSlot.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				Inventory inventory = handler.player.inventory;
				if (inventory.getHelm() == null)
					return;

				// Considered a single click
				if (getTapCount() % 2 == 1) {
					
				} else {
					inventory.removeEquippedItem(inventory.getHelm());
				}
			}
		});
		
		handler.leftHandSlot.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				Inventory inventory = handler.player.inventory;
				if (inventory.getLeftHand() == null)
					return;

				// Considered a single click
				if (getTapCount() % 2 == 1) {
					
				} else {
					inventory.removeEquippedItem(inventory.getLeftHand());
				}
			}
		});
		
		handler.rightHandSlot.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				Inventory inventory = handler.player.inventory;
				if (inventory.getRightHand() == null)
					return;

				// Considered a single click
				if (getTapCount() % 2 == 1) {
					
				} else {
					inventory.removeEquippedItem(inventory.getRightHand());
					
				}
				
			}
		});

	}
	
	private void initializeItemSlots() {
		Button[][] itemSlots = handler.itemSlots;
		for (int row = 0; row < itemSlots.length; ++row) {
			for (int col = 0; col < itemSlots[row].length; ++col) {
				itemSlots[row][col].addListener(new ClickListener() {			
					@Override
					public void clicked(InputEvent event, float x, float y) {
						int indexY = (int) ((event.getStageX() - handler.startingX) / handler.width);
						int indexX = (int) ((event.getStageY() - handler.startingY) / handler.height);
						
						// Considered a single click
						if (getTapCount() % 2 == 1) {
							inventory.singleClicked(indexX, indexY);
						} else {
							inventory.doubleClicked(indexX, indexY);
						}
					}
				} );
			}
		}
		
	}
	
	private void initializeAddButtons() {
		handler.addButtonStr.addListener(new InputListener() {
			public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
				StatisticsHandler statsHandler = handler.player.statsHandler;
				if (statsHandler.remainingStatusPoints > 0) {
					statsHandler.addBaseStr();
				} else {
					handler.renderer.stage.getActors().removeAll(handler.addAttributeButtons, true);
				}
				return true;
			}

			public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
			}
		});
		
		handler.addButtonDex.addListener(new InputListener() {
			public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
				StatisticsHandler statsHandler = handler.player.statsHandler;
				if (statsHandler.remainingStatusPoints > 0) {
					statsHandler.addBaseDex();
				} else {
					handler.renderer.stage.getActors().removeAll(handler.addAttributeButtons, true);
				}
				return true;
			}

			public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
			}
		});
		
		handler.addButtonVit.addListener(new InputListener() {
			public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
				StatisticsHandler statsHandler = handler.player.statsHandler;
				if (statsHandler.remainingStatusPoints > 0) {
					statsHandler.addBaseVit();
				} else {
					handler.renderer.stage.getActors().removeAll(handler.addAttributeButtons, true);
				}
				return true;
			}

			public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
			}
		});
		
		handler.addButtonAgi.addListener(new InputListener() {
			public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
				StatisticsHandler statsHandler = handler.player.statsHandler;
				if (statsHandler.remainingStatusPoints > 0) {
					statsHandler.addBaseAgi();
				} else {
					handler.renderer.stage.getActors().removeAll(handler.addAttributeButtons, true);
				}
				return true;
			}

			public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
			}
		});
		
		handler.addButtonWis.addListener(new InputListener() {
			public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
				StatisticsHandler statsHandler = handler.player.statsHandler;
				if (statsHandler.remainingStatusPoints > 0) {
					statsHandler.addBaseWis();
				} else {
					handler.renderer.stage.getActors().removeAll(handler.addAttributeButtons, true);
				}
				return true;
			}

			public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
			}
		});
	}
	
	private void initializeResumeButton() {
		handler.resumeButton.addListener(new InputListener() {
			public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
		 		return true;
		 	}
		 
		 	public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
		 		Gdx.app.log("Example", "touch done at (" + x + ", " + y + ")");
		 		handler.screen.backToGameScreen();
		 	}
		});
	}
	
	
}
