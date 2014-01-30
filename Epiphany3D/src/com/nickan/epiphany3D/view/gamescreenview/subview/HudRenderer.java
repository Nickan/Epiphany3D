package com.nickan.epiphany3D.view.gamescreenview.subview;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.nickan.epiphany3D.Epiphany3D;
import com.nickan.epiphany3D.model.Character;

/**
 * Work in progress. Separates the HUD Rendering to make the WorldRenderer Class more manageable
 * 
 * @author Nickan
 *
 */

public class HudRenderer {
	Label enemyNameLabel;
	private BitmapFont comic;
	BitmapFont arial;
	public Character enemy = null;
	public Character player = null;

	private float hpBarWidth;
	private float hpBarHeight;
	private float hpBarPosX;
	private float hpBarPosY;
	private float widthUnit;
	private float heightUnit;
	
	TextureAtlas atlas;
	Skin skin;
	Sprite hpBarSprite;
	Sprite clickedSprite;

	public HudRenderer(BitmapFont arial, BitmapFont comic) {
		this.arial = arial;
		this.comic = comic;

		hpBarWidth = Gdx.graphics.getWidth() / 3;
		hpBarHeight = Gdx.graphics.getHeight() / 30;

		LabelStyle ls = new LabelStyle(comic, Color.RED);
		enemyNameLabel = new Label("Enemy", ls);
		enemyNameLabel.setHeight(hpBarHeight);

		atlas = new TextureAtlas("graphics/hudrenderer.pack");
		skin = new Skin(atlas);
		
		hpBarSprite = skin.getSprite("hpbar");
		clickedSprite = skin.getSprite("cursorsmall");
		clickedSprite.setAlpha(.5f);
		
	}

	public void draw(SpriteBatch spriteBatch, PerspectiveCamera cam) {
		spriteBatch.draw(skin.getRegion("circle"), 0, 0, heightUnit * 3, heightUnit * 3);
		drawEnemyHealthBar(spriteBatch);
		drawPlayerHealthBar(spriteBatch);
//		drawLetters(spriteBatch);
		AttackDamageRenderer.getInstance().draw(spriteBatch, cam, comic);
	}

	private void drawEnemyHealthBar(SpriteBatch spriteBatch) {
		if (enemy == null)
			return;
		
		// Full Hp
		hpBarSprite.setColor(Color.DARK_GRAY);
		hpBarSprite.setBounds(hpBarPosX, hpBarPosY, hpBarWidth, hpBarHeight);
		hpBarSprite.draw(spriteBatch);

		// Current Hp
		float fracCurrentHp = enemy.statsHandler.currentHp / enemy.statsHandler.getFullHp();
		hpBarSprite.setColor(Color.LIGHT_GRAY);
		hpBarSprite.setBounds(hpBarPosX, hpBarPosY, hpBarWidth * fracCurrentHp, hpBarHeight);
		hpBarSprite.draw(spriteBatch);
	}

	private void drawPlayerHealthBar(SpriteBatch spriteBatch) {
		if (player == null)
			return;

		float hpWidth = 80;
		float hpHeight = 10;
		Sprite hpBarSprite = skin.getSprite("hpbar");
		// Full Hp
		hpBarSprite.setColor(Color.DARK_GRAY);
		hpBarSprite.setBounds(1, hpBarPosY, hpWidth, hpHeight);
		hpBarSprite.draw(spriteBatch);

		// Current Hp
		float fracCurrentHp = player.statsHandler.currentHp / player.statsHandler.getFullHp();
		hpBarSprite.setColor(Color.YELLOW);
		hpBarSprite.setBounds(1, hpBarPosY, hpWidth * fracCurrentHp, hpHeight);
		hpBarSprite.draw(spriteBatch);

		// Full Hp
		hpBarSprite.setColor(Color.DARK_GRAY);
		hpBarSprite.setBounds(1, hpBarPosY - hpHeight * 1.5f, hpWidth, hpHeight);
		hpBarSprite.draw(spriteBatch);

		// Current Hp
		float fracCurrentMp = player.statsHandler.currentMp / player.statsHandler.getFullMp();
		hpBarSprite.setColor(Color.BLUE);
		hpBarSprite.setBounds(1, hpBarPosY - hpHeight * 1.5f, hpWidth * fracCurrentMp, hpHeight);
		hpBarSprite.draw(spriteBatch);
	}

	public void drawCursor(SpriteBatch spriteBatch, Vector2 cursorPos) {
		spriteBatch.draw(skin.getRegion("mousecursor"), cursorPos.x - 8f, cursorPos.y - 35f);
	}
	
	public void drawClickedArea(SpriteBatch spriteBatch, Vector2 clickedAreaPos) {
//		spriteBatch.draw(clickedSprite, clickedAreaPos.x, clickedAreaPos.y, widthUnit / 2, heightUnit / 2);
		clickedSprite.setPosition(clickedAreaPos.x - (clickedSprite.getWidth() / 2), clickedAreaPos.y - (clickedSprite.getHeight() / 2));
		clickedSprite.draw(spriteBatch, 1f);
	}
	
	public void drawLetters(SpriteBatch spriteBatch) {
		if (enemy == null)
			return;
		enemyNameLabel.draw(spriteBatch, 1);
	}

	public void resize(int width, int height) {
		hpBarWidth = width / 3;
		hpBarHeight = height / 30;

		widthUnit = width / 16f;
		heightUnit = height / 12f;

		enemyNameLabel.setSize(0, 0);
		enemyNameLabel.setFontScale(width / Epiphany3D.WIDTH, height / Epiphany3D.HEIGHT);
		enemyNameLabel.setPosition(widthUnit * 7.5f, heightUnit * 11.68f);

		hpBarPosX = (widthUnit * 8f) - (hpBarWidth / 2);
		hpBarPosY = (heightUnit * 11.5f) - (hpBarHeight / 2);
		
		clickedSprite.setSize(widthUnit / 2, heightUnit / 2);
	}

	public void dispose() {
		skin.dispose();
		atlas.dispose();
	}
}
