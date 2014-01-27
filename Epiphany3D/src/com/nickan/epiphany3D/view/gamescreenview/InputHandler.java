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
import com.badlogic.gdx.utils.Array;
import com.nickan.epiphany3D.Epiphany3D;
import com.nickan.epiphany3D.model.ArtificialIntelligence;
import com.nickan.epiphany3D.model.Player;
import com.nickan.epiphany3D.model.messagingsystem.MessageDispatcher;
import com.nickan.epiphany3D.model.messagingsystem.Telegram.Message;
import com.nickan.epiphany3D.screen.InventoryScreen;
import com.nickan.framework1_0.math.LineAABB;
import com.nickan.framework1_0.pathfinder1_0.Node;

public class InputHandler implements InputProcessor {
	World world;
	public Epiphany3D game;
	Vector2 pointerPos = new Vector2();
	Vector2 previousTouch = new Vector2();
	Vector3 touch = new Vector3();

	// For the detection of the double click
	private boolean storedClick = false;
	private float clickDelay = 0.5f;

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
	}
	
	private void initializePauseButton() {
		Button pauseButton = world.renderer.pauseButton;

		pauseButton.addListener(new InputListener() {
			public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
				Gdx.app.log("Example", "touch started at (" + x + ", " + y + ")");
				return true;
			}

			public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
				Gdx.app.log("Example", "touch done at (" + x + ", " + y + ")");
				game.setScreen(new InventoryScreen(game, game.gameScreen));
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
		switch (button) {
		case Buttons.LEFT:
			leftMouseDown = true;
			break;
		case Buttons.RIGHT:
			rightMouseDown = true;
			break;
		default:
			break;
		}

		if (button != Buttons.LEFT)
			return false;

		CameraController camCtrl = world.camController;
		Ray ray = camCtrl.cam.getPickRay(screenX, screenY);

		// Get how many times the direction.y has to be multiplied to make the exact opposite value of origin.y
		// To determine the tiles being clicked, y-axis as the surface (Making the y-axis zero)
		float mul = -ray.origin.y / ray.direction.y;
		Vector3 dest = new Vector3();
		dest.set(ray.direction);
		dest.scl(mul);
		// The origin should be added to locate the clicked area
		dest.add(ray.origin);

		// Check if the character is clicked
		ArtificialIntelligence enemy = getClickedEnemy(ray.origin, dest);

		if (enemy != null) {
			processClickedEnemy(enemy, screenX, screenY);
		} else {

			// Preventing to path find those areas that are not included in path finding node list
			if (dest.x < 0 || dest.z < 0 || dest.x >= 100 || dest.z >= 100)
				return false;

			if (!isInList(World.occupiedNodes, (int) dest.x, (int) dest.z)) {
				world.player.pathFindWalkableNode((int) dest.x, (int) dest.z);
			}
		}

		return true;
	}

	private boolean isInList(Array<Node> nodeList, int nodeX, int nodeY) {
		for (Node tempNode: nodeList) {
			if (tempNode.isSame(nodeX, nodeY)) {
				return true;
			}
		}
		return false;
	}

	private void processClickedEnemy(ArtificialIntelligence enemy, int screenX, int screenY) {
		Player player = world.player;
		if (doubleClicked(screenX, screenY)) {
			if (enemy.isAlive()) {
				MessageDispatcher.getInstance().dispatchMessage(player.getId(), enemy.getId(), 0, Message.TARGETED_BY_SENDER, player.getNextNode());			
			}
		} else {
			// Show name and status
		}
	}

	private ArtificialIntelligence getClickedEnemy(Vector3 origin, Vector3 end) {
		for (ArtificialIntelligence enemy : world.enemies) {
			if (LineAABB.getLineAABBIntersection(origin, end, enemy.boundingBox) != null && enemy.isAlive()) {
				return enemy;
			}
		}
		return null;
	}

	/**
	 *
	 * @param screenX
	 * @param screenY
	 * @return If the user double clicks
	 */
	private boolean doubleClicked(int screenX, int screenY) {
		boolean doubleClicked = false;
		// Implementing the double clicking
		if (storedClick) {
			if (world.timePassed < clickDelay) {

				// Check to see if the user clicked on the same area
				// Make an allowance
				float allowanceClick = 5;
				if (Math.abs(previousTouch.x - screenX) < allowanceClick && Math.abs(previousTouch.y - screenY) < allowanceClick ) {
					//				if (previousTouch.x == screenX && previousTouch.y == screenY) {
					storedClick = false;
					doubleClicked = true;
				}
			}
		} else {
			storedClick = true;
		}

		previousTouch.set(screenX, screenY);
		world.timePassed = 0;
		return doubleClicked;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		
		switch (button) {
		case Buttons.LEFT:
			leftMouseDown = false;
			break;
		case Buttons.RIGHT:
			rightMouseDown = false;
			break;
		default:
			break;
		}

		return true;
	}

	boolean touch1 = false;
	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		/*
		// Detects two fingers being dragged on the screen
		if (pointer == 0) {
			touch1 = true;
		} else {

			if (pointer == 1 && touch1) {
				world.camController.zoomScale += Gdx.graphics.getDeltaTime() * 10;
			}
		}
		*/

		//		if (rightMouseDown)
		camControllerDragged(screenX, screenY);
		return true;
	}

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
