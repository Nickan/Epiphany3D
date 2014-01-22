package com.nickan.epiphany3D;

import com.badlogic.gdx.Game;
import com.nickan.epiphany3D.screen.GameScreen;

public class Epiphany3D extends Game {
	public static boolean debug = true;
	public GameScreen gameScreen;

	@Override
	public void create() {
		// TODO Auto-generated method stub
		// Need a copy of the screen, as I need it for the sub screens of the Game Screen
		gameScreen = new GameScreen(this);
		setScreen(gameScreen);
	}

}