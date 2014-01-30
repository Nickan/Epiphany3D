package com.nickan.epiphany3D.view.gamescreenview;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.FPSLogger;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
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

	/** Automagically handled by the Character class */
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
	Vector2 cursorVelocity = new Vector2();
	Circle cursorCtrl;
	Rectangle cursorCtl;
	
	Vector2 clickedArea = new Vector2();

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
		enemies.get(0).statsHandler.addAddedAgi(90);
		enemies.get(0).statsHandler.calcFinalAttributes();
	}

	public void update(float delta) {
		player.update(delta);
		updateEnemies(delta);

		MessageDispatcher.getInstance().update(delta);

		timePassed += delta;
		camController.update(delta);
		
		updateCursor(delta);
//		logger.log();
	}
	
	private void updateCursor(float delta) {
		if (cursor.x < 0) {
			cursorVelocity.x = 0;
			cursor.x = 1;
		}
		
		if (cursor.x > Gdx.graphics.getWidth() - 5) {
			cursorVelocity.x = 0;
			cursor.x = Gdx.graphics.getWidth() - 5;
		}
		
		if (cursor.y < 20) {
			cursorVelocity.y = 0;
			cursor.y = 20;
		}
		
		if (cursor.y > Gdx.graphics.getHeight()) {
			cursorVelocity.y = 0;
			cursor.y = Gdx.graphics.getHeight();
		}
			
		cursor.add(cursorVelocity.x * delta, cursorVelocity.y * delta);
	}

	private void updateEnemies(float delta) {
		for (ArtificialIntelligence enemy : enemies) {
			enemy.update(delta);
		}
	}
	
	public void resize(int width, int height) {
		float widthUnit = width / 16f;
		float heightUnit = height / 12f;
		
//		cursorCtrl = new Circle(0, 0, heightUnit * 3);
		cursorCtrl = new Circle(heightUnit * 1.5f, heightUnit * 1.5f, heightUnit * 1.5f);
//		cursorCtrl.setPosition(widthUnit * 2, heightUnit * 2);
		
		cursorCtl = new Rectangle(0, 0, widthUnit * 2.7f, heightUnit * 3);
	}
	
	public void setWorldRenderer(WorldRenderer renderer) {
		this.renderer = renderer;
		camController = new CameraController(renderer.cam);
		camController.playerPos = player.getPosition();		
	}

}
