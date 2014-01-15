package com.nickan.epiphany3D.view.gamescreenview;

import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;

/**
 * Manages the instances to be put in the renderer. If it is inside the draw distance given based on the base position to be given
 * to the update()
 * @author Nickan
 *
 */
public class ModelInstanceManager {
	private Array<ModelInstance> idleInstances;
	private float drawDistance;
	private Vector3 tempPos;

	public ModelInstanceManager() {
		idleInstances = new Array<ModelInstance>();
		tempPos = new Vector3();
		drawDistance = 15;
	}

	public void update(Array<ModelInstance> instances, Vector3 position) {
		for (ModelInstance instance: instances) {
			instance.transform.getTranslation(tempPos);

			// If the ModelInstance is far away from the set drawDistance
			if (position.dst2(tempPos) > drawDistance * drawDistance) {
				idleInstances.add(instance);
				instances.removeValue(instance, true);
				continue;
			}
		}

		for (ModelInstance instance: idleInstances) {
			instance.transform.getTranslation(tempPos);
			// If the ModelInstance is near the drawDistance
			if (position.dst2(tempPos) < drawDistance * drawDistance) {
				instances.add(instance);
				idleInstances.removeValue(instance, true);
				continue;
			}
		}
	}
	
	public void setDrawDistance(float drawDistance) {
		this.drawDistance = drawDistance;
	}
	
}
