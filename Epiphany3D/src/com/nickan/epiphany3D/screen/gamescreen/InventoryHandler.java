package com.nickan.epiphany3D.screen.gamescreen;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.utils.Align;
import com.nickan.epiphany3D.model.Player;
import com.nickan.epiphany3D.screen.InventoryScreen;


public class InventoryHandler {
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
	
	float widthUnit;
	float heightUnit;
	float startingX;
	float startingY;
	float width;
	float height;
	
	Vector2 statsPos = new Vector2(1.5f, 8.5f);
	Vector2 worldUnit = new Vector2(16f, 12f);
	
	Player player;
	InventoryScreen screen;
	InventoryHandler handler;
	InventoryRenderer renderer;
	
	public InventoryHandler(InventoryScreen screen, Player player) {
		this.screen = screen;
		this.player = player;
	}
	
	public void resize(int width, int height) {
		widthUnit = width / 16f;
		heightUnit = height / 12f;

		bodySlot.setBounds(widthUnit * 8f, heightUnit * 8.5f, widthUnit, heightUnit);
		footSlot.setBounds(widthUnit * 8.75f, heightUnit * 7f, widthUnit, heightUnit);
		glovesSlot.setBounds(widthUnit * 7.25f, heightUnit * 7f, widthUnit, heightUnit);
		headSlot.setBounds(widthUnit * 8f, heightUnit * 10f, widthUnit, heightUnit);
		leftHandSlot.setBounds(widthUnit * 6.5f, heightUnit * 8.5f, widthUnit, heightUnit);
		rightHandSlot.setBounds(widthUnit * 9.5f, heightUnit * 8.5f, widthUnit, heightUnit);

		addButtonStr.setBounds(widthUnit * (statsPos.x + 2), heightUnit * (statsPos.y), widthUnit, heightUnit);
		addButtonDex.setBounds(widthUnit * (statsPos.x + 2), heightUnit * (statsPos.y - 1.5f), widthUnit, heightUnit);
		addButtonVit.setBounds(widthUnit * (statsPos.x + 2), heightUnit * (statsPos.y - 3f), widthUnit, heightUnit);
		addButtonAgi.setBounds(widthUnit * (statsPos.x + 2), heightUnit * (statsPos.y - 4.5f), widthUnit, heightUnit);
		addButtonWis.setBounds(widthUnit * (statsPos.x + 2), heightUnit * (statsPos.y - 6f), widthUnit, heightUnit);
		
		resumeButton.setBounds(widthUnit * 15f, heightUnit * 11f, widthUnit, heightUnit);
		setItemSlotsPosition(widthUnit, heightUnit);
	}
	
	private void setItemSlotsPosition(float widthUnit, float heightUnit) {
		float startingPosX = 6.05f;
		float startingPosY = .3f;
		width = widthUnit + (widthUnit / 5.1f);
		height = heightUnit + (heightUnit / 2.4f);

		startingX = startingPosX * widthUnit;
		startingY = startingPosY * heightUnit;

		for (int row = 0; row < itemSlots.length; ++row) {
			for (int col = 0; col < itemSlots[row].length; ++col) {
				itemSlots[row][col].setBounds((startingPosX * widthUnit) + (width * col), 
						(startingPosY * heightUnit) + (height * row), width, height);
				itemSlots[row][col].align(Align.center);
			}
		}
	}
}
