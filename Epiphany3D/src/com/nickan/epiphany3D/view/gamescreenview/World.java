package com.nickan.epiphany3D.view.gamescreenview;


import com.badlogic.gdx.graphics.FPSLogger;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.utils.Array;
import com.nickan.epiphany3D.model.ArtificialIntelligence;
import com.nickan.epiphany3D.model.Player;
import com.nickan.epiphany3D.model.messagingsystem.MessageDispatcher;
import com.nickan.framework1_0.pathfinder1_0.Node;
import com.nickan.framework1_0.pathfinder1_0.PathFinder;

public class World {
	WorldRenderer renderer;

	public Player player;
	static final int tileWidth = 200;
	static final int tileHeight = 200;
	public static final PathFinder pathFinder = new PathFinder(tileWidth, tileHeight);

	/** Auto-magically handled by the Character class */
	public static final Array<Node> occupiedNodes = new Array<Node>();

	public static final Array<Node> unwalkableNodes = new Array<Node>();


	/** For the detection of the double click */
	float timePassed = 0;

	ArtificialIntelligence enemy;
	Array<ArtificialIntelligence> enemies = new Array<ArtificialIntelligence>();

	//...
	FPSLogger logger = new FPSLogger();
	CameraController camController;
	Vector3 tileCursor = new Vector3(21.5f, 0.001f, 21.5f);
	Vector2 cursor = new Vector2(100, 100);
	
	Vector2 clickedArea = new Vector2();
	private static final int BUTTONS_QUANTITY = 2;
	Button[] optionButtons = new Button[BUTTONS_QUANTITY];
	Button upCamButton;
	Button downCamButton;
	Button leftCamButton;
	Button rightCamButton;

	public World() {
		player = new Player(new Vector3(20.5f, 0, 20.5f), new Vector3(0, 0, 0), new Vector3(0, 0, 1f), 2f);
		player.statsHandler.whosYourDaddy();
		System.out.println("Player's attack speed " + player.statsHandler.getAttackSpd());
		System.out.println("Player attack delay " + player.statsHandler.attackDelay);
		System.out.println("Player' Agi " + player.statsHandler.getAgi());
		
		// Testing the potion
//		player.statsHandler.currentHp = 50;
//		player.statsHandler.currentMp = 50;
		System.out.println("Player current Hp: " + player.statsHandler.currentHp);
		System.out.println("Player current Hp: " + player.statsHandler.getFullHp());
		
		occupiedNodes.add(player.getNextNode());
		enemies.clear();
		
		// Testing for summoning lots of enemies
		enemies.add(new ArtificialIntelligence(new Vector3(14.5f, 0, 20.5f), new Vector3(0, 0, 0), new Vector3(0, 0, 1f), 0.5f));
		enemies.add(new ArtificialIntelligence(new Vector3(3.5f, 0, 2.5f), new Vector3(0, 0, 0), new Vector3(0, 0, 1f), 0.5f));
		enemies.add(new ArtificialIntelligence(new Vector3(4.5f, 0, 2.5f), new Vector3(0, 0, 0), new Vector3(0, 0, 1f), 0.5f));
		enemies.add(new ArtificialIntelligence(new Vector3(5.5f, 0, 2.5f), new Vector3(0, 0, 0), new Vector3(0, 0, 1f), 0.5f));
		enemies.add(new ArtificialIntelligence(new Vector3(6.5f, 0, 2.5f), new Vector3(0, 0, 0), new Vector3(0, 0, 1f), 0.5f));
		enemies.add(new ArtificialIntelligence(new Vector3(7.5f, 0, 2.5f), new Vector3(0, 0, 0), new Vector3(0, 0, 1f), 0.5f));
		
		enemies.add(new ArtificialIntelligence(new Vector3(3.5f, 0, 3.5f), new Vector3(0, 0, 0), new Vector3(0, 0, 1f), 0.5f));
		enemies.add(new ArtificialIntelligence(new Vector3(4.5f, 0, 4.5f), new Vector3(0, 0, 0), new Vector3(0, 0, 1f), 0.5f));
		enemies.add(new ArtificialIntelligence(new Vector3(5.5f, 0, 5.5f), new Vector3(0, 0, 0), new Vector3(0, 0, 1f), 0.5f));
		enemies.add(new ArtificialIntelligence(new Vector3(6.5f, 0, 6.5f), new Vector3(0, 0, 0), new Vector3(0, 0, 1f), 0.5f));
		enemies.add(new ArtificialIntelligence(new Vector3(7.5f, 0, 7.5f), new Vector3(0, 0, 0), new Vector3(0, 0, 1f), 0.5f));

		
		for (ArtificialIntelligence tempEnemy: enemies) {
			tempEnemy.setEnemyId(player.getId());
			occupiedNodes.add(tempEnemy.getNextNode());
		}
//		enemies.add(enemy);
		//... Testing
//		enemies.get(0).statsHandler.addAddedAgi(90);
//		enemies.get(0).statsHandler.calcFinalAttributes();
	}

	public void update(float delta) {
		player.update(delta);
		updateEnemies(delta);

		MessageDispatcher.getInstance().update(delta);

		timePassed += delta;
		camController.update(delta);

//		logger.log();
	}

	private void updateEnemies(float delta) {
		for (ArtificialIntelligence enemy : enemies) {
			enemy.update(delta);
		}
		
		if (renderer.hudRenderer.enemy != null) {
			if (!renderer.hudRenderer.enemy.isAlive()) {
				renderer.hudRenderer.enemy = null;
				renderer.instances.add(renderer.clickedCharacter);
				renderer.clickedCharacter = null;
			}
		}
	}
	
	public void resize(int width, int height) {
		float widthUnit = width / 16f;
		float heightUnit = height / 12f;
		
		for (int num = 0; num < optionButtons.length; ++num) {
			optionButtons[num].setBounds(widthUnit * 14f, heightUnit * (num * 1.4f), widthUnit * 2.2f, heightUnit * 2);
		}
		
		upCamButton.setBounds(widthUnit * 1.5f, heightUnit * 3, widthUnit * 1.5f, heightUnit * 1.5f);
		downCamButton.setBounds(widthUnit * 1.5f, 0, widthUnit * 1.5f, heightUnit * 1.5f);
		leftCamButton.setBounds(0, heightUnit * 1.5f, widthUnit * 1.5f, heightUnit * 1.5f);
		rightCamButton.setBounds(widthUnit * 3, heightUnit * 1.5f, widthUnit * 1.5f, heightUnit * 1.5f);
	}
	
	public void setWorldRenderer(WorldRenderer renderer) {
		this.renderer = renderer;
		camController = new CameraController(renderer.cam);
		camController.playerPos = player.getPosition();		
	}

}
