package com.nickan.framework1_0;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;

public class CameraManager {
	public static OrthographicCamera newStretchedCamera(float width, float height) {
		OrthographicCamera camera = new OrthographicCamera(width, height);
		camera.position.set(width / 2, height / 2, 0);
		camera.update();
		return camera;
	}

	public static OrthographicCamera newAdjustedCamera(float virtualWidth, float virtualHeight) {
		float viewportWidth = virtualWidth;
		float viewportHeight = virtualHeight;
		float physicalWidth = Gdx.graphics.getWidth();
		float physicalHeight = Gdx.graphics.getHeight();
		float aspect = virtualWidth / virtualHeight;

		if (physicalWidth / physicalHeight > aspect)
			viewportWidth = virtualHeight * physicalWidth / physicalHeight;
		else
			viewportHeight = virtualWidth * physicalHeight / physicalWidth;

		OrthographicCamera camera = new OrthographicCamera(viewportWidth, viewportHeight);
		camera.position.set(viewportWidth / 2, viewportHeight / 2, 0);
		camera.update();
		return camera;
	}
}
