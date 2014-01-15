package com.nickan.epiphany3D.model.messagingsystem;

import com.nickan.epiphany3D.model.Entity;

public class EntityManager {
	private static final int QUANTITY = 200;
	private Entity[] entityList = new Entity[QUANTITY];

	private static final EntityManager instance = new EntityManager();

	private EntityManager() {
		clear();
	}

	public int getAvailableId(Entity entity) {
		for (int i = 0; i < entityList.length; ++i) {
			if (entityList[i] == null) {
				entityList[i] = entity;
				return i;
			}
		}
		return -1;
	}

	public boolean deleteEntity(int id) {
		if (entityList[id] != null) {
			entityList[id] = null;
			return true;
		}
		return false;
	}

	public Entity getEntity(int id) {
		return entityList[id];
	}

	private void clear() {
		for (int i = 0; i < entityList.length; ++i)
			entityList[i] = null;
	}

	public static final EntityManager getInstance() {
		return instance;
	}

}
