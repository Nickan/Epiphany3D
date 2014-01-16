package com.nickan.epiphany3D.view.gamescreenview;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.environment.PointLight;
import com.badlogic.gdx.graphics.g3d.model.Node;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.BoundingBox;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.utils.Align;
import com.badlogic.gdx.utils.Array;
import com.nickan.epiphany3D.Epiphany3D;
import com.nickan.epiphany3D.model.ArtificialIntelligence;
import com.nickan.epiphany3D.model.Dimension;
import com.nickan.epiphany3D.view.gamescreenview.subview.AttackDamageRenderer;

public class WorldRenderer {
	World world;
	PerspectiveCamera cam;
	ModelBatch batch;
	PointLight lights;
	Environment environment;
	AssetManager assets;

	Array<ModelInstance> instances = new Array<ModelInstance>();
	Array<ModelInstance> grasses = new Array<ModelInstance>();

	ModelInstance player;
	ModelInstance zombie;

	private boolean loading;

	// For debugging
	ShapeRenderer shapeRenderer;

	// Drawing text
	SpriteBatch spriteBatch;
	BitmapFont courier;
	BitmapFont arial;
	BitmapFont comic;
	
	ShaderProgram fontShader;

	Stage stage;
	Label label;

	ModelInstanceManager instanceManager = new ModelInstanceManager();
	
	Texture barTexture;
	Sprite barSprite;

	public WorldRenderer(World world) {
		this.world = world;

		Gdx.gl.glViewport(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

		batch = new ModelBatch();
		environment = new Environment();
		lights = new PointLight().set(1, 1, 1, 3, 3, 3, 10f);
		environment.add(lights);

		float fov = 67;
		cam = new PerspectiveCamera(fov, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		cam.position.set(0, 2.0f, 0.0f);
		cam.near = 0.1f;
		cam.far = 50.0f;
		cam.update();

		assets = new AssetManager();
		assets.load("graphics/scene.g3db", Model.class);
		assets.load("graphics/zombie.g3db", Model.class);
		assets.load("graphics/player.g3db", Model.class);

		loading = true;

		// For Debugging...
		shapeRenderer = new ShapeRenderer();
		
		load2DVariables();
	}

	public void render(float delta) {
		if (loading && assets.update())
			doneLoading();

		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);
		Gdx.gl.glClearColor(0f, 0f, 0f, 1.0f);

		Vector3 lightPos = world.player.getPosition();
		lights.position.set(lightPos.x, lightPos.y + 2, lightPos.z);

		instanceManager.update(instances, lightPos);

		batch.begin(cam);

		if (!loading) {
//			skydome.transform.setToTranslation(cam.position.x, cam.position.y - 2, cam.position.z);
//			batch.render(skydome);
//			batch.flush();

			for (ModelInstance instance: instances)
				batch.render(instance, environment);
		}
		batch.end();

		renderSpriteBatch(delta);
//		debug();
	}

	/**
	 * For me to draw 2D HUD
	 * @param delta
	 */
	private void renderSpriteBatch(float delta) {
		spriteBatch.begin();
		drawHUDClickedEnemyHealthBar();
		drawLetters(delta);
		spriteBatch.end();
	}
	
	private void drawLetters(float delta) {
		spriteBatch.setShader(fontShader);
		AttackDamageRenderer.getInstance().draw(spriteBatch, world.camController.cam, comic, delta);	
		spriteBatch.setShader(null);
	}



	/**
	 * It does exactly what the method name is, uses the matrix of the stage SpriteBatch camera to project the ShapeRenderer
	 */
	private void drawHUDClickedEnemyHealthBar() {		
		// Testing the newly found knowledge that should have been discovered in the first place >_<
		// Full Hp
		barSprite.setColor(Color.GRAY);
		barSprite.setBounds(0, 0, 200, 10);
		
		float barPosX = (Gdx.graphics.getWidth() / 2) - barSprite.getWidth() / 2;
		float barPosY = (Gdx.graphics.getHeight() - barSprite.getHeight() / 2) - Gdx.graphics.getHeight() / 30;
		barSprite.setPosition( barPosX, barPosY);
		barSprite.draw(spriteBatch);
		
		// Current Hp
		barSprite.setColor(Color.YELLOW);
		barSprite.setBounds(0, 0, 100, 10);
		barSprite.setPosition(barPosX, barPosY);
		barSprite.draw(spriteBatch);
	}

	private void doneLoading() {
		player = new ModelInstance(assets.get("graphics/player.g3db", Model.class));
		zombie = new ModelInstance(assets.get("graphics/zombie.g3db", Model.class));
		
		world.player.setModelInstance(player, 6, 40, 10);
		
		//...
//		System.out.println("Player's attack animation speed: " + world.player.aniHandler.attackPlaySpeed);
		
//		world.enemy.setModelInstance(zombie, 11, 20, 10);
		Model zombieModel = assets.get("graphics/zombie.g3db", Model.class);
		for (ArtificialIntelligence enemy: world.enemies) {
			ModelInstance tempInstance = new ModelInstance(zombieModel);
			enemy.setModelInstance(tempInstance, 11, 20, 10);
			instances.add(tempInstance);
		}
		
		instances.add(player);
//		instances.add(zombie);

		Model model = assets.get("graphics/scene.g3db", Model.class);
		for (int i = 0; i < model.nodes.size; ++i) {
			String id = model.nodes.get(i).id;
			ModelInstance instance = new ModelInstance(model, id);
			Node node = instance.getNode(id);

			instance.transform.set(node.globalTransform);
			node.translation.set(0, 0, 0);
			node.scale.set(1, 1, 1);
			node.rotation.idt();
			instance.calculateTransforms();

			if (id.startsWith("wall")) {
//				instances.add(instance);
			} else if (id.startsWith("ground")) {
				instances.add(instance);
			}
		}
		
		loading = false;
	}

	private void load2DVariables() {
		spriteBatch = new SpriteBatch();
		arial = new BitmapFont(Gdx.files.internal("graphics/fonts/arial.fnt"));
		arial.setUseIntegerPositions(false);
//		arial.setColor(Color.BLUE);
		arial.setScale(2);
		
		Texture texture = new Texture(Gdx.files.internal("graphics/fonts/comic.png"), true);
		texture.setFilter(TextureFilter.MipMapLinearNearest, TextureFilter.Linear);
		
		barTexture = new Texture(Gdx.files.internal("graphics/bar.png"), true);
		barTexture.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		
		barSprite = new Sprite(barTexture);
		barSprite.setBounds(0, 0, 200, 10);
		barSprite.setPosition( (Gdx.graphics.getWidth() / 2) - barSprite.getWidth() / 2, 
				(Gdx.graphics.getHeight() - barSprite.getHeight() / 2) - Gdx.graphics.getHeight() / 30);
		
		comic = new BitmapFont(Gdx.files.internal("graphics/fonts/comic.fnt"), new TextureRegion(texture), false);
		comic.setColor(Color.WHITE);
//		comic.setScale(2f);
//		arial.setUseIntegerPositions(false);
		
		fontShader = new ShaderProgram(Gdx.files.internal("graphics/fonts/font.vert"), Gdx.files.internal("graphics/fonts/font.frag"));
		
		if (!fontShader.isCompiled()) {
		    Gdx.app.error("fontShader", "compilation failed:\n" + fontShader.getLog());
		}
		
		if (stage == null) {
			stage = new Stage(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), true);
		}
		stage.clear();


		LabelStyle ls = new LabelStyle(arial, Color.YELLOW);
		label = new Label("", ls);
		label.setPosition(Gdx.graphics.getWidth() / 2, Gdx.graphics.getHeight() / 2);
		label.setWidth(0);
		label.setAlignment(Align.center);

		stage.addActor(label);
	}
	
	// For debugging methods
	private void debug() {
		if (Epiphany3D.debug) {
			// Represents tile in the game
			shapeRenderer.setProjectionMatrix(cam.combined);
			shapeRenderer.begin(ShapeType.Line);
			for (int z = 0; z < 10; ++z) {
				for (int x = 0; x < 10; ++x) {
					shapeRenderer.identity();
					shapeRenderer.translate(x, 0, z);
					shapeRenderer.rotate(1, 0, 0, 90);
					shapeRenderer.rect(0, 0, 1f, 1f);
				}
			}

			// Player bounding box
			drawBounds(world.player.getPosition(), world.player.getDimension());
			drawBounds(world.enemy.getPosition(), world.enemy.getDimension());
			shapeRenderer.end();
		}
	}
	
	private void drawBounds(Vector3 position, Dimension dimension) {
		shapeRenderer.setColor(Color.WHITE);
		shapeRenderer.identity();
		shapeRenderer.translate(position.x, position.y, position.z);
		shapeRenderer.box(-dimension.width / 2, 0, -dimension.depth / 2, dimension.width, dimension.height, -dimension.depth);
	}

	private void drawBounds(BoundingBox box) {
		shapeRenderer.setColor(Color.WHITE);
		shapeRenderer.identity();
		shapeRenderer.translate(box.min.x, box.min.y, box.min.z);
		Vector3 dimension = new Vector3(box.max.x - box.min.x, box.max.y - box.min.y, box.max.z - box.min.z);
		shapeRenderer.box(0, 0, 0, dimension.x, dimension.y, -dimension.z);
	}

	public void dispose() {
		assets.dispose();
		batch.dispose();
		instances.clear();
		spriteBatch.dispose();
		courier.dispose();
		arial.dispose();
		comic.dispose();
		shapeRenderer.dispose();
		barTexture.dispose();
	}

}
