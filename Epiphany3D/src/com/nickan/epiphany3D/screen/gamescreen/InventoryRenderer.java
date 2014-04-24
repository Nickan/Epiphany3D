package com.nickan.epiphany3D.screen.gamescreen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.nickan.epiphany3D.model.StatisticsHandler;
import com.nickan.epiphany3D.model.items.Consumable;
import com.nickan.epiphany3D.model.items.Inventory;
import com.nickan.epiphany3D.model.items.Item;
import com.nickan.epiphany3D.model.items.Wearable;

public class InventoryRenderer {
	InventoryHandler handler;
	Stage stage;
	TextureAtlas atlas;
	Skin skin;
	private BitmapFont arial;
	private BitmapFont comic;

	public InventoryRenderer(InventoryHandler handler, SpriteBatch spriteBatch) {
		this.handler = handler;
		initialize(handler, spriteBatch);
		initializeItemSlots(handler);
	}
	
	private void initialize(InventoryHandler handler, SpriteBatch spriteBatch) {
		atlas = new TextureAtlas("graphics/inventorytextures.pack");
		skin = new Skin(atlas);

		float width = Gdx.graphics.getWidth();
		float height = Gdx.graphics.getHeight();

		stage = new Stage(width, height, true, spriteBatch);
		stage.clear();
		Gdx.input.setInputProcessor(stage);

		handler.bodySlot = new Button(skin.getDrawable("equipmentslotnormal"), skin.getDrawable("equipmentslotpressed"));
		handler.footSlot = new Button(skin.getDrawable("equipmentslotnormal"), skin.getDrawable("equipmentslotpressed"));
		handler.glovesSlot = new Button(skin.getDrawable("equipmentslotnormal"), skin.getDrawable("equipmentslotpressed"));
		handler.headSlot = new Button(skin.getDrawable("equipmentslotnormal"), skin.getDrawable("equipmentslotpressed"));
		handler.leftHandSlot = new Button(skin.getDrawable("equipmentslotnormal"), skin.getDrawable("equipmentslotpressed"));
		handler.rightHandSlot = new Button(skin.getDrawable("equipmentslotnormal"), skin.getDrawable("equipmentslotpressed"));

		handler.addButtonStr = new Button(skin.getDrawable("positivebuttonnormal"), skin.getDrawable("positivebuttonpressed"));
		handler.addButtonDex = new Button(skin.getDrawable("positivebuttonnormal"), skin.getDrawable("positivebuttonpressed"));
		handler.addButtonVit = new Button(skin.getDrawable("positivebuttonnormal"), skin.getDrawable("positivebuttonpressed"));
		handler.addButtonAgi = new Button(skin.getDrawable("positivebuttonnormal"), skin.getDrawable("positivebuttonpressed"));
		handler.addButtonWis = new Button(skin.getDrawable("positivebuttonnormal"), skin.getDrawable("positivebuttonpressed"));
		
		handler.resumeButton = new Button(skin.getDrawable("resumebuttonnormal"), skin.getDrawable("resumebuttonpressed"));

		stage.addActor(handler.bodySlot);
		stage.addActor(handler.footSlot);
		stage.addActor(handler.glovesSlot);
		stage.addActor(handler.headSlot);
		stage.addActor(handler.leftHandSlot);
		stage.addActor(handler.rightHandSlot);
		stage.addActor(handler.resumeButton);

		stage.addActor(handler.addButtonStr);
		stage.addActor(handler.addButtonDex);
		stage.addActor(handler.addButtonVit);
		stage.addActor(handler.addButtonAgi);
		stage.addActor(handler.addButtonWis);
	}

	private void initializeItemSlots(InventoryHandler handler) {
		for (int row = 0; row < handler.itemSlots.length; ++row) {
			for (int col = 0; col < handler.itemSlots[row].length; ++col) {
				handler.itemSlots[row][col] = new Button(skin.getDrawable("itemslotnormal"), skin.getDrawable("itemslotpressed"));
				stage.addActor(handler.itemSlots[row][col]);
			}
		}
	}
	

	public void render(InventoryHandler handler, float delta) {
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT  | GL10.GL_DEPTH_BUFFER_BIT);
		Gdx.gl.glClearColor(0f, 0f, 0f, 1.0f);

		stage.act(delta);
		SpriteBatch batch = (SpriteBatch) stage.getSpriteBatch();

		batch.begin();
		batch.draw(skin.getRegion("pausebackground"), 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

		StatisticsHandler statsHandler = handler.player.statsHandler;
		float widthUnit = handler.widthUnit;
		float heightUnit = handler.heightUnit;
		Vector2 statsPos = handler.statsPos;

		batch.draw(skin.getRegion("statsbox"), widthUnit * (statsPos.x - 1), heightUnit * statsPos.y, widthUnit, heightUnit);
		batch.draw(skin.getRegion("statsbox"), widthUnit * (statsPos.x - 1), heightUnit * (statsPos.y - 1.5f), widthUnit, heightUnit);
		batch.draw(skin.getRegion("statsbox"), widthUnit * (statsPos.x - 1), heightUnit * (statsPos.y - 3), widthUnit, heightUnit);
		batch.draw(skin.getRegion("statsbox"), widthUnit * (statsPos.x - 1), heightUnit * (statsPos.y - 4.5f), widthUnit, heightUnit);
		batch.draw(skin.getRegion("statsbox"), widthUnit * (statsPos.x - 1), heightUnit * (statsPos.y - 6), widthUnit, heightUnit);

//		batch.setShader(fontShader);
//		drawLettersWithDistanceField(batch);
		drawLettersWithRegularFont(batch);
		batch.setShader(null);

		drawItems(batch);

		batch.end();

		stage.draw();

		batch.begin();
		drawEquipment(batch);
		batch.end();
	}

	private void drawLettersWithDistanceField(InventoryHandler handler, SpriteBatch batch) {
		StatisticsHandler statsHandler = handler.player.statsHandler;
		float widthUnit = handler.widthUnit;
		float heightUnit = handler.heightUnit;
		Vector2 statsPos = handler.statsPos;
		/*
		comic.draw(batch, handler.player.name, widthUnit * (statsPos.x - 1f), heightUnit * (statsPos.y + 2.8f));

		comic.draw(batch, "" + (int) statsHandler.getStr() + "   STR", 
				widthUnit * (statsPos.x - .7f), heightUnit * (statsPos.y + 1f));
		comic.draw(batch, " atk: " + (int) statsHandler.getAttackDmg(),
				widthUnit * (statsPos.x - 1), heightUnit * (statsPos.y + .25f));

		comic.draw(batch, "" + (int) statsHandler.getDex() + "   DEX", 
				widthUnit * (statsPos.x - .7f), heightUnit * (statsPos.y - .5f));
		comic.draw(batch, " hit: " + (int) statsHandler.getAttackHit() + "   crt: " + (int) statsHandler.getAttackCrit(),
				widthUnit * (statsPos.x - 1), heightUnit * (statsPos.y - 1.25f));

		comic.draw(batch, "" + (int) statsHandler.getVit() + "   VIT", 
				widthUnit * (statsPos.x - .7f), heightUnit * (statsPos.y - 2f));
		comic.draw(batch, " def: " + (int) statsHandler.getDef(),
				widthUnit * (statsPos.x - 1), heightUnit * (statsPos.y - 2.75f));

		comic.draw(batch, "" + (int) statsHandler.getAgi() + "   AGI", 
				widthUnit * (statsPos.x - .7f), heightUnit * (statsPos.y - 3.5f));
		comic.draw(batch, " spd: " + (int) statsHandler.getAttackSpd() + "   avd: " + (int) statsHandler.getAvoid(),
				widthUnit * (statsPos.x - 1), heightUnit * (statsPos.y - 4.25f));

		comic.draw(batch, "" + (int) statsHandler.getWis() + "   WIS", 
				widthUnit * (statsPos.x - .7f), heightUnit * (statsPos.y - 5f));
		comic.draw(batch, " mag: " + 1 + "   magDef: " + 1,
				widthUnit * (statsPos.x - 1), heightUnit * (statsPos.y - 5.75f));
		*/
	}
	
	/** Temporary variables to place the temporary font to be used */
	Vector2 statsPos = new Vector2();
	
	/**
	 * Temporary methods to draw letters, as I am having a bug about the ShaderProgram class
	 * @param batch
	 */
	private void drawLettersWithRegularFont(SpriteBatch batch) {
		if (arial == null) {
			return;
		}
		
		StatisticsHandler statsHandler = handler.player.statsHandler;
		float widthUnit = handler.widthUnit;
		float heightUnit = handler.heightUnit;
		statsPos.set(handler.statsPos);
		statsPos.add(-0.1f, -0.3f);
		
		arial.draw(batch, handler.player.name, widthUnit * (statsPos.x - 1f), heightUnit * (statsPos.y + 2.8f));

		arial.draw(batch, "" + (int) statsHandler.getStr() + "   STR", 
				widthUnit * (statsPos.x - .7f), heightUnit * (statsPos.y + 1f));
		arial.draw(batch, " atk: " + (int) statsHandler.getAttackDmg(),
				widthUnit * (statsPos.x - 1), heightUnit * (statsPos.y + .25f));

		arial.draw(batch, "" + (int) statsHandler.getDex() + "   DEX", 
				widthUnit * (statsPos.x - .7f), heightUnit * (statsPos.y - .5f));
		arial.draw(batch, " hit: " + (int) statsHandler.getAttackHit() + "   crt: " + (int) statsHandler.getAttackCrit(),
				widthUnit * (statsPos.x - 1), heightUnit * (statsPos.y - 1.25f));

		arial.draw(batch, "" + (int) statsHandler.getVit() + "   VIT", 
				widthUnit * (statsPos.x - .7f), heightUnit * (statsPos.y - 2f));
		arial.draw(batch, " def: " + (int) statsHandler.getDef(),
				widthUnit * (statsPos.x - 1), heightUnit * (statsPos.y - 2.75f));

		arial.draw(batch, "" + (int) statsHandler.getAgi() + "   AGI", 
				widthUnit * (statsPos.x - .7f), heightUnit * (statsPos.y - 3.5f));
		arial.draw(batch, " spd: " + (int) statsHandler.getAttackSpd() + "   avd: " + (int) statsHandler.getAvoid(),
				widthUnit * (statsPos.x - 1), heightUnit * (statsPos.y - 4.25f));

		arial.draw(batch, "" + (int) statsHandler.getWis() + "   WIS", 
				widthUnit * (statsPos.x - .7f), heightUnit * (statsPos.y - 5f));
		arial.draw(batch, " mag: " + 1 + "   magDef: " + 1,
				widthUnit * (statsPos.x - 1), heightUnit * (statsPos.y - 5.75f));
		
	}

	private void drawEquipment(SpriteBatch batch) {
		
		Inventory inventory = handler.player.inventory;

		if (inventory.getHelm() != null) 
			drawWearable(batch, inventory.getHelm(), handler.headSlot);

		if (inventory.getArmor() != null) 
			drawWearable(batch, inventory.getArmor(), handler.bodySlot);

		if (inventory.getLeftHand() != null) 
			drawWearable(batch, inventory.getLeftHand(), handler.leftHandSlot);

		if (inventory.getRightHand() != null)
			drawWearable(batch, inventory.getRightHand(), handler.rightHandSlot);

		if (inventory.getGloves() != null)
			drawWearable(batch, inventory.getGloves(), handler.glovesSlot);

		if (inventory.getBoots() != null)
			drawWearable(batch, inventory.getBoots(), handler.footSlot);
	}

	private void drawItems(SpriteBatch batch) {
		
		Item[][] playerItems = handler.player.inventory.getItems();
		for (int row = 0; row < playerItems.length; ++row) {
			for (int col = 0; col < playerItems[row].length; ++col) {
				Item item = playerItems[row][col];
				Button slot = handler.itemSlots[row][col];

				if (item == null)
					continue;

				switch (item.getItemClass()) {
				case CONSUMABLE:
					drawConsumable(batch, (Consumable) item, slot);
					break;
				case WEARABLE:
					drawWearable(batch, (Wearable) item, slot);
					break;
				default:
					break;
				}
			}
		}
		
	}

	private void drawConsumable(SpriteBatch batch, Consumable consumable, Button slot) {
		switch (consumable.getConsumableType()) {
		case HP_POTION:
			drawItemSlotTexture(batch, skin.getRegion("hppotion"), slot);
			break;
		case MP_POTION:
			drawItemSlotTexture(batch, skin.getRegion("mppotion"), slot);
			break;
		default:
			break;
		}
	}

	private void drawWearable(SpriteBatch batch, Wearable wearable, Button slot) {
		switch (wearable.getWearableType()) {
		case HELM:
			drawItemSlotTexture(batch, skin.getRegion("helm"), slot);
			break;
		case ARMOR:
			drawItemSlotTexture(batch, skin.getRegion("armor"), slot);
			break;
		case LEFT_HAND:
			drawItemSlotTexture(batch, skin.getRegion("shield"), slot);
			break;
		case RIGHT_HAND:
			drawItemSlotTexture(batch, skin.getRegion("sword"), slot);
			break;
		case GLOVES:
			drawItemSlotTexture(batch, skin.getRegion("gloves"), slot);
			break;
		case BOOTS:
			drawItemSlotTexture(batch, skin.getRegion("boots"), slot);
			break;
		}
	}

	private void drawItemSlotTexture(SpriteBatch batch, TextureRegion region, Button slot) {
		batch.draw(region, slot.getX() + slot.getWidth() / handler.worldUnit.x, slot.getY() + slot.getHeight() / handler.worldUnit.y, 
				handler.widthUnit, handler.heightUnit);
	}



	public void resize(int width, int height) {
		stage.setViewport(width, height);

	//	comic.setScale(width / Epiphany3D.WIDTH, height / Epiphany3D.HEIGHT);
	//	arial.setScale(width / Epiphany3D.WIDTH, height / Epiphany3D.HEIGHT);
	}
	
	public void setBitmapFont(BitmapFont arial, BitmapFont comic) {
		this.arial = arial;
		this.comic = comic;
	}
	
	public void dispose() {
		skin.dispose();
		stage.dispose();
	}
}
