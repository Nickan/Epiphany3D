package com.nickan.epiphany3D;

import com.badlogic.gdx.Game;
import com.nickan.epiphany3D.screen.StartScreen;
import com.nickan.epiphany3D.screen.gamescreen.GameScreen;

public class Epiphany3D extends Game {
	public static boolean debug = false;
	public GameScreen gameScreen;
	
	public static final float WIDTH = 480;
	public static final float HEIGHT = 320;

	@Override
	public void create() {
		// TODO Auto-generated method stub
		// Need a copy of the screen, as I need it for the sub screens of the Game Screen
		setScreen(new StartScreen(this));
	}

}