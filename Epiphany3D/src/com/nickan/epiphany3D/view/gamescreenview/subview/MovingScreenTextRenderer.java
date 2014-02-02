package com.nickan.epiphany3D.view.gamescreenview.subview;


import com.badlogic.gdx.Gdx;
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
	 * Will Store the position and the damage to be rendered on the screen
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

	public void draw(SpriteBatch batch, PerspectiveCamera cam, BitmapFont font) {
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
			storage.duration += Gdx.graphics.getDeltaTime();
			storage.pos.y += Y_SPEED * Gdx.graphics.getDeltaTime();
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
	 * @param posRef - The reference to the vector
	 * @param damage
	 */
	public void addAttackDamageToScreen(Vector3 posRef, int damage) {
		entities.add(new AttackDamageStorage(posRef, damage));
	}

	public static AttackDamageRenderer getInstance() {
		return attackDamageRenderer;
	}

}
