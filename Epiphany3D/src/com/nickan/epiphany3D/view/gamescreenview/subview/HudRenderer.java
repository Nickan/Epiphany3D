package com.nickan.epiphany3D.view.gamescreenview.subview;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.nickan.epiphany3D.model.Character;

/**
 * Work in progress. Separates the HUD Rendering to make the WorldRenderer Class more manageable
 * 
 * @author Nickan
 *
 */

public class HudRenderer {
	Texture hpBarTexture;
	Sprite hpBarSprite;
	Label enemyNameLabel;
	private BitmapFont comic;
	BitmapFont arial;
	public Character enemy = null;
	
	private float hpBarWidth;
	private float hpBarHeight;
	private float hpBarPosX;
	private float hpBarPosY;
	
	PerspectiveCamera cam;
	
	public HudRenderer(BitmapFont arial, BitmapFont comic, PerspectiveCamera camera) {
		this.arial = arial;
		this.comic = comic;
		this.cam = camera;
		
		hpBarWidth = Gdx.graphics.getWidth() / 3;
		hpBarHeight = Gdx.graphics.getHeight() / 30;
		
		LabelStyle ls = new LabelStyle(comic, Color.RED);
		enemyNameLabel = new Label("Enemy", ls);
		enemyNameLabel.setHeight(hpBarHeight);
		
		hpBarTexture = new Texture(Gdx.files.internal("graphics/hpBar.png"), true);
		hpBarTexture.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		
		hpBarSprite = new Sprite(hpBarTexture);
	}
	
	public void draw(SpriteBatch spriteBatch) {
		drawHealthBar(spriteBatch);
		drawLetters(spriteBatch);
		AttackDamageRenderer.getInstance().draw(spriteBatch, cam, comic);
	}
	
	private void drawHealthBar(SpriteBatch spriteBatch) {
		if (enemy == null)
			return;

		// Full Hp
		hpBarSprite.setColor(Color.DARK_GRAY);
		hpBarSprite.setBounds(hpBarPosX, hpBarPosY, hpBarWidth, hpBarHeight);
		hpBarSprite.draw(spriteBatch);

		// Current Hp
		float fracCurrentHp = enemy.statsHandler.currentHp / enemy.statsHandler.totalFullHp;
		hpBarSprite.setColor(Color.LIGHT_GRAY);
		hpBarSprite.setBounds(hpBarPosX, hpBarPosY, hpBarWidth * fracCurrentHp, hpBarHeight);
		hpBarSprite.draw(spriteBatch);
	}
	
	private void drawLetters(SpriteBatch spriteBatch) {
		if (enemy == null)
			return;
		
		float labelPosX = (Gdx.graphics.getWidth() / 2) - enemyNameLabel.getWidth() / 2;
		float labelPosY = hpBarPosY + hpBarHeight / 2;
		
		enemyNameLabel.setPosition(labelPosX, labelPosY);
		enemyNameLabel.draw(spriteBatch, 1);
	}
	
	public void resize(int width, int height) {
		hpBarWidth = Gdx.graphics.getWidth() / 3;
		hpBarHeight = Gdx.graphics.getHeight() / 30;
		
		hpBarPosX = (width / 2) - (hpBarWidth / 2);
		hpBarPosY = height - (hpBarHeight * 2);
	}
	
	public void dispose() {
		hpBarTexture.dispose();
	}
}
