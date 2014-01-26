package com.nickan.epiphany3D.model;

import com.badlogic.gdx.math.Vector3;
import com.nickan.epiphany3D.model.items.Inventory;

public class Player extends Character {
	public Inventory inventory;

	public Player(Vector3 position, Vector3 rotation, Vector3 velocity,
			float speed) {
		super(position, rotation, velocity, speed);
		inventory = new Inventory(statsHandler);
	}

	public void update(float delta) {
		inventory.update(delta);
		super.update(delta);
	}

}
