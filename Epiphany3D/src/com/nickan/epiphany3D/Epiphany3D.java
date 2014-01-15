package com.nickan.epiphany3D;

import com.badlogic.gdx.Game;
import com.nickan.epiphany3D.screen.GameScreen;

public class Epiphany3D extends Game {
	public static boolean debug = true;

	@Override
	public void create() {
		// TODO Auto-generated method stub
		setScreen(new GameScreen(this));
	}

}