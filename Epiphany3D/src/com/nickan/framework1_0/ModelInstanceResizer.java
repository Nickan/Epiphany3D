package com.nickan.framework1_0;

import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.BoundingBox;

/**
 * Putting in a separate class, later to think where should be put.
 *
 * @author Nickan
 *
 */
public class ModelInstanceResizer {
	private static final BoundingBox box = new BoundingBox();

	private ModelInstanceResizer() { }

	/**
	 * Resize the instance to be passed by LibGdx's default unit size
	 * @param instance	- The ModelInstance to be resized
	 * @param x
	 * @param y
	 * @param z
	 * @return
	 */
	public static final void setSize(ModelInstance instance, float x, float y, float z) {
		instance.calculateBoundingBox(box);
		Vector3 dimension = box.getDimensions();
		dimension.set(x / dimension.x, y / dimension.y, z / dimension.z);
		instance.transform.scl(dimension);
	}
}
