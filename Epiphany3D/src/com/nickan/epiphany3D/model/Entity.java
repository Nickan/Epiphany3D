package com.nickan.epiphany3D.model;

import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.BoundingBox;
import com.nickan.epiphany3D.model.messagingsystem.EntityManager;
import com.nickan.epiphany3D.model.messagingsystem.Telegram;

/**
 * At last, mouse click detection using Mouse ray detection on AABB! Whew!. But I don't know if this is the
 * right class to handle this (>_<)
 *
 * @author Nickan
 *
 */
public abstract class Entity {
	int id;
	protected Vector3 position;
	protected Vector3 rotation;
	protected Dimension dimension;

	/**  Bounding box for mouse collision detection, I will use the Line-Box Intersection algorithm */
	public BoundingBox boundingBox = new BoundingBox();

	public Entity(Vector3 position, Vector3 rotation) {
		this.position = position;
		this.rotation = rotation;
		dimension = new Dimension(1f, 1f, 1f);
		
		this.id = EntityManager.getInstance().getAvailableId(this);
	}

	/** Updates the bounding box of the entity */
	protected void update() {
		boundingBox.min.set(position.x - dimension.width / 2, position.y, position.z - dimension.depth / 2);
		boundingBox.max.set(position.x + dimension.width / 2, position.y + dimension.height, position.z + dimension.depth / 2);
	}


	public Vector3 getPosition() {
		return position;
	}

	public Vector3 rotation() {
		return rotation;
	}

	public Dimension getDimension() {
		return dimension;
	}
	
	public abstract boolean handleMessage(Telegram telegram);

	public int getId() {
		return id;
	}

}
