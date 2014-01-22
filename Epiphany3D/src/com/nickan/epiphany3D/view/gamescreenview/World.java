package com.nickan.epiphany3D.view.gamescreenview;


import com.badlogic.gdx.graphics.FPSLogger;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.nickan.epiphany3D.model.ArtificialIntelligence;
import com.nickan.epiphany3D.model.Player;
import com.nickan.epiphany3D.model.messagingsystem.MessageDispatcher;
import com.nickan.framework1_0.pathfinder1_0.Node;
import com.nickan.framework1_0.pathfinder1_0.PathFinder;

public class World {
	WorldRenderer renderer;

	Player player;
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

	public World() {
		player = new Player(new Vector3(20.5f, 0, 20.5f), new Vector3(0, 0, 0), new Vector3(0, 0, 1f), 2f);
//		player.statsHandler.whosYourDaddy();
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
	}
	
	public void setWorldRenderer(WorldRenderer renderer) {
		this.renderer = renderer;
		camController = new CameraController(renderer.cam);
		camController.playerPos = player.getPosition();		
	}

}
