package com.nickan.epiphany3D.view.gamescreenview;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Input.Buttons;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.Ray;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.nickan.epiphany3D.Epiphany3D;
import com.nickan.epiphany3D.model.ArtificialIntelligence;
import com.nickan.epiphany3D.model.Character;
import com.nickan.epiphany3D.model.Player;
import com.nickan.epiphany3D.model.messagingsystem.MessageDispatcher;
import com.nickan.epiphany3D.model.messagingsystem.Telegram.Message;
import com.nickan.epiphany3D.screen.InventoryScreen;
import com.nickan.epiphany3D.view.gamescreenview.subview.HudRenderer;
import com.nickan.framework1_0.math.LineAABB;

public class InputHandler implements InputProcessor {
	World world;
	public Epiphany3D game;

	boolean rightMouseDown = false;
	boolean leftMouseDown = false;

	// Manipulation of text on the screen
	float pos = 0.1f;

	public InputHandler(World world, Epiphany3D game) {
		this.world = world;
		this.game = game;
		initializeButtons();
	}
	
	// For the Stage's buttons
	private void initializeButtons() {
		initializePauseButton();
		initializeOptionButtons();
		initializeCamButtons();
	}
	
	private void initializePauseButton() {
		Button pauseButton = world.renderer.pauseButton;

		pauseButton.addListener(new InputListener() {
			public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
				return true;
			}

			public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
				game.setScreen(new InventoryScreen(game, game.gameScreen));
			}
		});
		
	}
	
	private void initializeOptionButtons() {
		Button[] buttons = world.optionButtons;
		buttons[1].addListener(new InputListener() {
			public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
				if (pointer > 0)
					return false;
				
				return true;
			}

			public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
				Player player = world.player;
				if (world.renderer.hudRenderer.enemy != null) {
					Character enemy = world.renderer.hudRenderer.enemy;
					MessageDispatcher.getInstance().dispatchMessage(player.getId(), enemy.getId(), 0, Message.TARGETED_BY_SENDER, player.getNextNode());
				} else {
					player.pathFindWalkableNode((int) world.tileCursor.x , (int) world.tileCursor.z);
				}
			}
		});
		
		// Cancel button
		buttons[0].addListener(new InputListener() {
			public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {	
				return true;
			}

			public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
				if (world.renderer.hudRenderer.enemy != null) {
					world.player.pathFindWalkableNode((int) world.tileCursor.x , (int) world.tileCursor.z);
				}
			}
		});
	}
	
	private void initializeCamButtons() {
		final float speed = 100;
		world.upCamButton.addListener(new InputListener() {
			public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
				world.camController.rotationSpeed.x = speed;
				return true;
			}

			public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
				world.camController.rotationSpeed.x = 0;
			}
		});
	
		world.downCamButton.addListener(new InputListener() {
			public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
				world.camController.rotationSpeed.x = -speed;
				return true;
			}

			public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
				world.camController.rotationSpeed.x = 0;
			}
		});
		
		world.leftCamButton.addListener(new InputListener() {
			public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
				world.camController.rotationSpeed.y = speed;
				return true;
			}

			public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
				world.camController.rotationSpeed.y = 0;
			}
		});
		
		world.rightCamButton.addListener(new InputListener() {
			public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
				world.camController.rotationSpeed.y = -speed;
				return true;
			}

			public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
				world.camController.rotationSpeed.y = 0;
			}
		});
	}
	
	// End of Stage's buttons

	@Override
	public boolean keyDown(int keycode) {
		return false;
	}

	@Override
	public boolean keyUp(int keycode) {
		return false;
	}

	@Override
	public boolean keyTyped(char character) {
		// TODO Auto-generated method stub
		return false;
	}
	
	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		this.button = button;
		return true;
	}

	private ArtificialIntelligence getClickedEnemy(Vector3 origin, Vector3 end) {
		for (ArtificialIntelligence enemy : world.enemies) {
			if (LineAABB.getLineAABBIntersection(origin, end, enemy.boundingBox) != null && enemy.isAlive()) {
				return enemy;
			}
		}
		return null;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		// Resets the rotation of the camera
		world.camController.rotationSpeed.set(0, 0, 0);
		
		CameraController camCtrl = world.camController;
		Ray ray = camCtrl.cam.getPickRay(screenX, screenY - 20);

		// Get how many times the direction.y to reach the ground (y = 0) from the origin.y
		float mul = Math.abs(ray.origin.y / ray.direction.y);
		
		// Then multiply all of the axis from the result to get the clicked surface
		Vector3 dest = new Vector3(ray.origin).add(ray.direction.scl(mul));
		
		// See if there is an enemy being clicked
		ArtificialIntelligence enemy = getClickedEnemy(ray.origin, dest);
		HudRenderer renderer = world.renderer.hudRenderer;
		
		if (enemy != null) {
			renderer.enemy = enemy;
			world.renderer.clickedCharacter = enemy.getModelInstance();
		} else {
			
		}
		
		// Always set the tile cursor to the clicked area (For now)
		dest.set((int) dest.x + 0.5f, 0.001f, (int) dest.z + 0.5f);
		// Preventing to path find those areas that are not included in path finding node list
		if (dest.x >= 0 || dest.z >= 0 || dest.x < 100 || dest.z < 100)
			world.tileCursor.set(dest);
		return false;
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {	
		world.clickedArea.set(screenX, Gdx.graphics.getHeight() - screenY + 20);
		
		if (button == Buttons.RIGHT)
			camControllerDragged(screenX, screenY);
		return true;
	}
	
	Vector2 pointerPos = new Vector2();
	int button;
	
	private void camControllerDragged(int screenX, int screenY) {
		CameraController camCtrl = world.camController;
		if (screenY > pointerPos.y) {
			camCtrl.lookDown();
		} else if (screenY < pointerPos.y) {
			camCtrl.lookUp();
		}

		if (screenX > pointerPos.x) {
			camCtrl.lookRight();
		} else if (screenX < pointerPos.x) {
			camCtrl.lookLeft();
		}

		pointerPos.set(screenX, screenY);
	}


	@Override
	public boolean mouseMoved(int screenX, int screenY) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean scrolled(int amount) {
		world.camController.zoomScale += Gdx.graphics.getDeltaTime() * 10 * amount;
		return true;
	}

}
