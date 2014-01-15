package com.nickan.epiphany3D.view.gamescreenview.subview;


import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;

public class AttackDamageRenderer {
	private static AttackDamageRenderer attackDamageRenderer = new AttackDamageRenderer();
	private static final float DURATION = 5.0f;
	private static final float Y_SPEED = 50f;
	private static final Vector3 pos = new Vector3();
	StringBuilder builder = new StringBuilder();

	/**
	 * Will store the x and y for the position of the damage for the damage value to be shown
	 * on the screen
	 */
	private Array<AttackDamageStorage> entities = new Array<AttackDamageStorage>();

	private AttackDamageRenderer() {
		entities.clear();
	}

	private class AttackDamageStorage {
		private Vector3 position;
		private Vector2 pos;
		private int damage;
		private float duration;

		private AttackDamageStorage(Vector3 position, int damage) {
			this.position = position;
			this.pos = new Vector2();
			this.damage = damage;
			this.duration = 0;
		}
	}

	public void draw(SpriteBatch batch, PerspectiveCamera cam, BitmapFont font, float delta) {
		for (AttackDamageStorage storage: entities) {
			pos.set(storage.position);
			float yPosDrawAdj = 2;
			pos.y += yPosDrawAdj;
			cam.project(pos);
			
			builder.delete(0, builder.length());
			
			if (storage.damage == 0) {
				builder.append("Miss");
			} else {
				builder.append(storage.damage);
			}
			
			font.draw(batch, builder.toString(), pos.x + storage.pos.x, pos.y + storage.pos.y);
			storage.duration += delta;
			storage.pos.y += Y_SPEED * delta;
		}

		for (AttackDamageStorage storage: entities) {
			if (storage.duration > DURATION) {
				entities.removeValue(storage, true);
				break;
			}
		}
	}

	/**
	 *
	 * @param x	- x coordinates
	 * @param y - y coordinates
	 * @param attackDamage - The value of the damage to be shown, set to zero to show miss
	 */
	public void addAttackDamageToScreen(Vector3 position, int damage) {
		entities.add(new AttackDamageStorage(position, damage));
	}
	


	public static AttackDamageRenderer getInstance() {
		return attackDamageRenderer;
	}

}
