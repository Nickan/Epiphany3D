package com.nickan.epiphany3D.view.gamescreenview;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;
import com.nickan.framework1_0.math.Euler;

public class CameraController {
	PerspectiveCamera cam;
	Vector3 rotation = new Vector3(45, 0, 0);
	Vector3 playerPos;
	Vector3 rotationSpeed = new Vector3();

	float moveSpeed = 5f;
	float lookSpeed = 50f;
	float zoomScale = 5f;

	boolean moveForward = false;
	boolean moveBackward = false;
	boolean moveLeft = false;
	boolean moveRight = false;
	
	/**Only the CHASE_MODE is working properly, FREE_MODE will be fixed later */
	public enum CameraMode { CHASE_MODE, FREE_MODE }
	
	/** Don't set it to FREE_MODE, as it is still not working correctly */
	public CameraMode cameraMode = CameraMode.CHASE_MODE;


	// For deciphering the low level math later
	Matrix4 translate = new Matrix4();
	Matrix4 rotate = new Matrix4();
	Matrix4 transform = new Matrix4();


	public CameraController(PerspectiveCamera cam) {
		this.cam = cam;
	}

	void update(float delta) {
		switch (cameraMode) {
		case CHASE_MODE:
			updateChaseMode(delta);
			break;
		case FREE_MODE:
			updateFreeMode(delta);
			break;
		}
		cam.update();
	}


	private void updateChaseMode(float delta) {
		rotation.add(rotationSpeed.x * delta, rotationSpeed.y * delta, rotationSpeed.z * delta);
		rotationCheck();
		// Get the reverse direction
		cam.direction.set(Euler.toAxes(rotation)).nor().scl(-1);

		// For the position
		Vector3 tmp = new Vector3();
		tmp.set(0, 0, 0);
		tmp.add(cam.direction).scl(-zoomScale);
		tmp.add(playerPos);

		// Make the camera interpret 0 as .5f to make it look at the character, to be changed later
//		tmp.x += 0.5f;
//		tmp.z += 0.5f;

		// The mid section of the character
		tmp.y += .5f;

		cam.position.set(tmp);

	}

	private void updateFreeMode(float delta) {
		// Get the direction
		cam.direction.set(Euler.toAxes(rotation)).nor();

		// For the position
		Vector3 right = new Vector3().set(cam.direction).crs(Vector3.Y);
		right.y = 0;
		Vector3 forward = new Vector3().set(cam.direction);
		forward.y = 0;
		Vector3 velocity = new Vector3();

		if (moveForward) {
			velocity.set(forward.scl(moveSpeed * delta));
		} else if (moveBackward) {
			velocity.set(forward.scl(-moveSpeed * delta));
		}

		if (moveLeft) {
			velocity.set(right.scl(-moveSpeed * delta));
		} else if (moveRight) {
			velocity.set(right.scl(moveSpeed * delta));
		}

		cam.position.add(velocity);
	}
	
	private void rotationCheck() {
		if (rotation.x < 15)
			rotation.x = 15;
		if (rotation.x > 80)
			rotation.x = 80;
		if (rotation.y < 0)
			rotation.y = 359;
		if (rotation.y > 359)
			rotation.y = 0;
	}


	void lookUp() {
		rotation.x -= lookSpeed * Gdx.graphics.getDeltaTime();
	//	if (rotation.x < 15)
	//		rotation.x = 15;
	}

	void lookDown() {
		rotation.x += lookSpeed * Gdx.graphics.getDeltaTime();
	//	if (rotation.x > 80)
	//		rotation.x = 80;
	}

	void lookLeft() {
		rotation.y -= lookSpeed * Gdx.graphics.getDeltaTime();
	//	if (rotation.y < 0)
	//		rotation.y = 359;
	}

	void lookRight() {
		rotation.y += lookSpeed * Gdx.graphics.getDeltaTime();
	//	if (rotation.y > 359)
	//		rotation.y = 0;
	}


	/** Later, I want to know the low level math of camera */
	void transform() {
		translate.idt();
//		translate.translate(position);

//		System.out.println(translate.getValues()[12]);


		rotate.idt();
		rotate.rotate(Vector3.X, rotation.x);
		rotate.rotate(Vector3.Y, rotation.y);
		rotate.rotate(Vector3.Z, rotation.z);

//		System.out.println(rotate.getValues()[12]);

		transform.idt();
		transform.set(translate.mul(rotate));
		cam.view.set(transform);
		cam.update();

//		System.out.println(transform.getValues()[12]);

//		cam.transform(transform);
//		cam.translate(position);
//		cam.update();

		/*
		cam.position.set(position);
		cam.rotate(Vector3.X, rotation.x);
		cam.rotate(Vector3.Y, rotation.y);
		cam.rotate(Vector3.Z, rotation.z);
		*/

//		cam.update();
	}

}
