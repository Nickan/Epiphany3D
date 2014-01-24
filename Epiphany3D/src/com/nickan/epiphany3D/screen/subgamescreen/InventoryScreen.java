package com.nickan.epiphany3D.screen.subgamescreen;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.Align;
import com.nickan.epiphany3D.Epiphany3D;
import com.nickan.epiphany3D.model.items.Consumable;
import com.nickan.epiphany3D.model.items.Item;
import com.nickan.epiphany3D.model.items.Wearable;
import com.nickan.epiphany3D.screen.GameScreen;

public class InventoryScreen implements Screen {
	Button addButtonStr;
	Button addButtonDex;
	Button addButtonVit;
	Button addButtonAgi;
	Button addButtonWis;
	
	Button resumeButton;
	
	Button bodySlot;
	Button footSlot;
	Button glovesSlot;
	Button headSlot;
	Button leftHandSlot;
	Button rightHandSlot;
	
	private static final int BUTTON_ROWS = 4;
	private static final int BUTTON_COLS = 8;
	Button[][] itemSlots = new Button[BUTTON_ROWS][BUTTON_COLS];
	
	Stage stage;
	TextureAtlas atlas;
	Skin skin;
	GameScreen gameScreen;
	Epiphany3D game;
	InventoryController inventory;
	BitmapFont comic;
	BitmapFont arial;
	ShaderProgram fontShader;
	
	float widthUnit;
	float heightUnit;
	Vector2 statsPos = new Vector2(1.5f, 8.5f);
	Item[][] playerItems;
	
	Vector2 worldUnit = new Vector2(16f, 12f);
	
	public InventoryScreen(Epiphany3D game, GameScreen gameScreen) {
		this.game = game;
		this.gameScreen = gameScreen;
		comic = gameScreen.renderer.comic;
		arial = gameScreen.renderer.arial;
		fontShader = gameScreen.renderer.fontShader;
		playerItems = gameScreen.world.player.inventory.getItems();
	}

	@Override
	public void render(float delta) {
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT  | GL10.GL_DEPTH_BUFFER_BIT);
		Gdx.gl.glClearColor(0f, 0f, 0f, 1.0f);
		
		stage.act(delta);
		SpriteBatch batch = (SpriteBatch) stage.getSpriteBatch();
		
		float width = Gdx.graphics.getWidth();
		float height = Gdx.graphics.getHeight();
		batch.begin();
		batch.draw(skin.getRegion("pausebackground"), 0, 0, width, height);
		batch.draw(skin.getRegion("statsbox"), widthUnit * statsPos.x, heightUnit * statsPos.y, widthUnit, heightUnit);
		batch.draw(skin.getRegion("statsbox"), widthUnit * statsPos.x, heightUnit * (statsPos.y - 1), widthUnit, heightUnit);
		batch.draw(skin.getRegion("statsbox"), widthUnit * statsPos.x, heightUnit * (statsPos.y - 2), widthUnit, heightUnit);
		batch.draw(skin.getRegion("statsbox"), widthUnit * statsPos.x, heightUnit * (statsPos.y - 3), widthUnit, heightUnit);
		batch.draw(skin.getRegion("statsbox"), widthUnit * statsPos.x, heightUnit * (statsPos.y - 4), widthUnit, heightUnit);
		
		batch.setShader(fontShader);
		comic.draw(batch, "STR", widthUnit * (statsPos.x - 1), heightUnit * (statsPos.y + 1.05f));
		comic.draw(batch, "DEX", widthUnit * (statsPos.x - 1), heightUnit * (statsPos.y + .05f));
		comic.draw(batch, "VIT", widthUnit * (statsPos.x - 1), heightUnit * (statsPos.y - .95f));
		comic.draw(batch, "AGI", widthUnit * (statsPos.x - 1), heightUnit * (statsPos.y - 1.95f));
		comic.draw(batch, "WIS", widthUnit * (statsPos.x - 1), heightUnit * (statsPos.y - 2.95f));
		batch.setShader(null);
		
		drawItems(batch);
		batch.end();
		
		stage.draw();
	}
	
	private void drawItems(SpriteBatch batch) {
		for (int row = 0; row < playerItems.length; ++row) {
			for (int col = 0; col < playerItems[row].length; ++col) {
				Item item = playerItems[row][col];
				
				if (item == null)
					continue;
					
				switch (item.getItemClass()) {
				case CONSUMABLE:
					drawConsumable(batch, (Consumable) item, col, row);
					break;
				case WEARABLE:
					drawWearable(batch, (Wearable) item, col, row);
					break;
				default:
					break;
				}
			}
		}
	}
	
	private void drawConsumable(SpriteBatch batch, Consumable consumable, int x, int y) {
		switch (consumable.getConsumableType()) {
		case HP_POTION:
			drawTextureRegionItem(batch, skin.getRegion("hppotion"), x, y);
			break;
		case MP_POTION:
			drawTextureRegionItem(batch, skin.getRegion("mppotion"), x, y);
			break;
		default:
			break;
		}
	}
	
	private void drawWearable(SpriteBatch batch, Wearable wearable, int x, int y) {
		
	}
	
	private void drawTextureRegionItem(SpriteBatch batch, TextureRegion region, int slotX, int slotY) {
		Button slot = itemSlots[slotX][slotY];
		batch.draw(region, slot.getX() + slot.getWidth() / worldUnit.x, slot.getY() + slot.getHeight() / worldUnit.y, 
				widthUnit, heightUnit);
	}


	@Override
	public void resize(int width, int height) {
		stage.setViewport(width, height);
		
		comic.setScale(width / Epiphany3D.WIDTH, height / Epiphany3D.HEIGHT);
		
		widthUnit = width / 16f;
		heightUnit = height / 12f;
		
		bodySlot.setBounds(widthUnit * 8f, heightUnit * 8.5f, widthUnit, heightUnit);
		footSlot.setBounds(widthUnit * 8.75f, heightUnit * 7f, widthUnit, heightUnit);
		glovesSlot.setBounds(widthUnit * 7.25f, heightUnit * 7f, widthUnit, heightUnit);
		headSlot.setBounds(widthUnit * 8f, heightUnit * 10f, widthUnit, heightUnit);
		leftHandSlot.setBounds(widthUnit * 6.5f, heightUnit * 8.5f, widthUnit, heightUnit);
		rightHandSlot.setBounds(widthUnit * 9.5f, heightUnit * 8.5f, widthUnit, heightUnit);
		
		addButtonStr.setBounds(widthUnit * (statsPos.x + 1), heightUnit * (statsPos.y), widthUnit, heightUnit);
		addButtonDex.setBounds(widthUnit * (statsPos.x + 1), heightUnit * (statsPos.y - 1), widthUnit, heightUnit);
		addButtonVit.setBounds(widthUnit * (statsPos.x + 1), heightUnit * (statsPos.y - 2), widthUnit, heightUnit);
		addButtonAgi.setBounds(widthUnit * (statsPos.x + 1), heightUnit * (statsPos.y - 3), widthUnit, heightUnit);
		addButtonWis.setBounds(widthUnit * (statsPos.x + 1), heightUnit * (statsPos.y - 4), widthUnit, heightUnit);
		
		resumeButton.setBounds(widthUnit * 15f, heightUnit * 11f, widthUnit, heightUnit);
		setItemSlotsPosition(widthUnit, heightUnit);
	}

	@Override
	public void show() {
		atlas = new TextureAtlas("graphics/inventorytextures.pack");
		skin = new Skin(atlas);

		float width = Gdx.graphics.getWidth();
		float height = Gdx.graphics.getHeight();
		
		stage = new Stage(width, height, true, gameScreen.renderer.spriteBatch);
		stage.clear();
		Gdx.input.setInputProcessor(stage);
		
		bodySlot = new Button(skin.getDrawable("equipmentslotnormal"), skin.getDrawable("equipmentslotpressed"));
		footSlot = new Button(skin.getDrawable("equipmentslotnormal"), skin.getDrawable("equipmentslotpressed"));
		glovesSlot = new Button(skin.getDrawable("equipmentslotnormal"), skin.getDrawable("equipmentslotpressed"));
		headSlot = new Button(skin.getDrawable("equipmentslotnormal"), skin.getDrawable("equipmentslotpressed"));
		leftHandSlot = new Button(skin.getDrawable("equipmentslotnormal"), skin.getDrawable("equipmentslotpressed"));
		rightHandSlot = new Button(skin.getDrawable("equipmentslotnormal"), skin.getDrawable("equipmentslotpressed"));
		
		addButtonStr = new Button(skin.getDrawable("positivebuttonnormal"), skin.getDrawable("positivebuttonpressed"));
		addButtonDex = new Button(skin.getDrawable("positivebuttonnormal"), skin.getDrawable("positivebuttonpressed"));
		addButtonVit = new Button(skin.getDrawable("positivebuttonnormal"), skin.getDrawable("positivebuttonpressed"));
		addButtonAgi = new Button(skin.getDrawable("positivebuttonnormal"), skin.getDrawable("positivebuttonpressed"));
		addButtonWis = new Button(skin.getDrawable("positivebuttonnormal"), skin.getDrawable("positivebuttonpressed"));
		
		resumeButton = new Button(skin.getDrawable("resumebuttonnormal"), skin.getDrawable("resumebuttonpressed"));
		
		stage.addActor(bodySlot);
		stage.addActor(footSlot);
		stage.addActor(glovesSlot);
		stage.addActor(headSlot);
		stage.addActor(leftHandSlot);
		stage.addActor(rightHandSlot);
		stage.addActor(addButtonStr);
		stage.addActor(addButtonDex);
		stage.addActor(addButtonVit);
		stage.addActor(addButtonAgi);
		stage.addActor(addButtonWis);
		stage.addActor(resumeButton);
		
		initializeItemSlots();
		inventory = new InventoryController(this);
	}

	private void initializeItemSlots() {
		for (int row = 0; row < itemSlots.length; ++row) {
			for (int col = 0; col < itemSlots[row].length; ++col) {
				itemSlots[row][col] = new Button(skin.getDrawable("itemslotnormal"), skin.getDrawable("itemslotpressed"));
				stage.addActor(itemSlots[row][col]);
			}
		}
	}
	
	private void setItemSlotsPosition(float widthUnit, float heightUnit) {
		float startingPosX = 6.05f;
		float startingPosY = .3f;
		float width = widthUnit + (widthUnit / 5.1f);
		float height = heightUnit + (heightUnit / 2.4f);
		
		for (int row = 0; row < itemSlots.length; ++row) {
			for (int col = 0; col < itemSlots[row].length; ++col) {
				itemSlots[row][col].setBounds((startingPosX * widthUnit) + (width * col), 
						(startingPosY * heightUnit) + (height * row), width, height);
				itemSlots[row][col].align(Align.center);
			}
		}
	}
	
	
	@Override
	public void hide() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void pause() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void resume() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub
		
	}
	
}
