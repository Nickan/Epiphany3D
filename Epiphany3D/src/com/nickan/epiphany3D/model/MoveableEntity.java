package com.nickan.epiphany3D.model;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.math.Vector3;
import com.nickan.epiphany3D.screen.gamescreen.World;
import com.nickan.framework1_0.pathfinder1_0.Node;
import com.nickan.framework1_0.pathfinder1_0.Node.NodeType;

/**
 * Having a major change...
 *
 * @author Nickan
 *
 */
public abstract class MoveableEntity extends Entity {
	private float previousX = 0;
	private float previousY = 0;
	private float previousZ = 0;
	private float traveledX = 0;
	private float traveledY = 0;
	private float traveledZ = 0;
	private boolean movingThruX = false;
	private boolean movingThruY = false;
	private boolean movingThruZ = false;

	private int moveIndicatorX = 0;
	private int moveIndicatorY = 0;
	private int moveIndicatorZ = 0;
	private float speed;

	private Vector3 velocity;
	protected Node currentNode = new Node();
	private Node nextNode = new Node();
	protected Node targetNode = new Node();
	private List<Node> shortestPath = new ArrayList<Node>();

	/**
	 *
	 * @param position
	 * @param rotation
	 * @param velocity - I might delete this
	 * @param speed
	 */
	public MoveableEntity(Vector3 position, Vector3 rotation, Vector3 velocity, float speed) {
		super(position, rotation);
		this.velocity = velocity;
		this.speed = speed;
		currentNode.set((int) position.x, (int) position.z);	// To be changed later................
		identifyOccupiedNode();
	}

	/**
	 * Updates everything needed by the Entity under the sun :D
	 * @param delta
	 */
	protected void update(float delta) {
		if (!isMoving()) {
			currentNode.set((int) position.x, (int) position.z);	// To be changed later................
		}

		super.update();
	}


	// For state design pattern methods

	/**
	 * Always called in the update method, it handles the movement on all the axes, triggered by identifyMovent
	 * and automatically set it to false when the movement is done in precisely 1 unit
	 * @param delta
	 */
	public void move(float delta) {
		if (movingThruX) {

			// Moving right
			if (moveIndicatorX == 1) {
				position.x += speed * delta;

				traveledX = position.x - previousX;
				if (traveledX >= 1) {
					movingThruX = false;
					traveledX = 0;
					position.x = previousX + 1;
				}

				// Moving left
			} else if (moveIndicatorX == -1) {
				position.x -= speed * delta;

				traveledX = position.x - previousX;
				if (Math.abs(traveledX) >= 1) {
					movingThruX = false;
					traveledX = 0;
					position.x = previousX - 1;
				}
			}

		}

		if (movingThruY) {

			// Moving upward
			if (moveIndicatorY == 1) {

				position.y += speed * delta;
				traveledY = position.y - previousY;

				// Cancel the updating if it has traveled 1 node
				if (traveledY >= 1) {
					movingThruY = false;
					traveledY = 0;
					position.y = previousY + 1;
				}

				// Moving downward
			} else if (moveIndicatorY == -1) {
				position.y -= speed * delta;

				traveledY = position.y - previousY;
				if (Math.abs(traveledY) >= 1) {
					movingThruY = false;
					traveledY = 0;
					position.y = previousY - 1;
				}
			}

		}

		if (movingThruZ) {

			// Moving upward
			if (moveIndicatorZ == 1) {

				position.z += speed * delta;
				traveledZ = position.z - previousZ;

				// Cancel the updating if it has traveled 1 node
				if (traveledZ >= 1) {
					movingThruZ = false;
					traveledZ = 0;
					position.z = previousZ + 1;
				}

				// Moving downward
			} else if (moveIndicatorZ == -1) {
				position.z -= speed * delta;

				traveledZ = position.z - previousZ;
				if (Math.abs(traveledZ) >= 1) {
					movingThruZ = false;
					traveledZ = 0;
					position.z = previousZ - 1;
				}
			}

		}
	}

	/**
	 * Handles all the necessary variables to be used inside the move() and rotate().
	 */
	public void identifyNextMove() {
		// Update for the next node to go to
		moveIndicatorX = nextNode.x - currentNode.x;
		moveIndicatorZ = nextNode.y - currentNode.y;		// To change later...............

		//...
		velocity.set(moveIndicatorX, moveIndicatorY, moveIndicatorZ);
		velocity.nor();

		// Corrects the positioning of the character
		if (moveIndicatorX != 0) {
			movingThruX = true;
			traveledX = 0;
			previousX = currentNode.x + 0.5f;	/////////////////.......................................
		}

		if (moveIndicatorY != 0) {
			movingThruY = true;
			traveledY = 0;
			previousY = currentNode.y + 0.5f;
		}

		if (moveIndicatorZ != 0) {
			movingThruZ = true;
			traveledZ = 0;
			previousZ = currentNode.y + 0.5f;		// To change later.................
		}
	}

	/**
	 * I was trying to get the angle from direction angle, this is what I came up with. (Trying to reverse Euler angle)
	 * Arctan only produces 0 - 180 degrees even if the direction is facing over 180 degrees,
	 * But it produces the exact opposite of the direction, so I just add 180 degrees whenever the x is negative
	 * @param delta
	 */
	public void faceCoordinate(int x, int y) {
		float tempX = x - currentNode.x;
		float tempY = y - currentNode.y;
		rotation.y = (float) Math.toDegrees(Math.atan(-tempY / tempX));

		// If it is viewing the other side
		if (tempX < 0) {
			rotation.y += 180;
		}
	}

	public void faceCoordinate(float x, float y) {
		rotation.y = (float) Math.toDegrees(Math.atan(-y / x));

		// If it is viewing the other side
		if (x < 0) {
			rotation.y += 180;
		}
	}


	public boolean isMoving() {
		if (movingThruX || movingThruY || movingThruZ)
			return true;
		return false;
	}

	public void initializePath(Node targetNode) {
		this.targetNode.set(targetNode);
		World.pathFinder.setHeuristics(targetNode);

		initializeOccupiedNodes();
	}

	/**
	 * Set the occupied nodes as not walkable
	 */
	private void initializeOccupiedNodes() {
		for (Node node: World.occupiedNodes) {
			World.pathFinder.nodes[node.y][node.x].type = NodeType.UNWALKABLE;
		}
	}

	public void identifyOccupiedNode() {
		if (!shortestPath.isEmpty())
			nextNode.set(shortestPath.remove(shortestPath.size() - 1));
		else
			nextNode.set(currentNode);
	}


	public void setShortestPath(List<Node> shortestPath) {
		this.shortestPath.clear();
		this.shortestPath.addAll(shortestPath);
	}


	public Node getCurrentNode() {
		return currentNode;
	}

	public Node getNextNode() {
		return nextNode;
	}

	public Node getTargetNode() {
		return targetNode;
	}

	public void setTargetNode(Node targetNode) {
		this.targetNode = targetNode;
	}

	public boolean isPathEmpty() {
		return shortestPath.isEmpty();
	}

	public Vector3 getVelocity() {
		return velocity;
	}

}
