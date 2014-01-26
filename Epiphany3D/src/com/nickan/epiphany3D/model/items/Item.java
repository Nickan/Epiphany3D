package com.nickan.epiphany3D.model.items;

import com.nickan.epiphany3D.model.StatisticsHandler;

public abstract class Item {
	public enum ItemClass { WEARABLE, CONSUMABLE };
	private final ItemClass itemClass;
	
	public Item(ItemClass itemClass) {
		this.itemClass = itemClass;
	}
	
	public abstract void use(StatisticsHandler statsHandler);
	
	public final ItemClass getItemClass() {
		return itemClass;
	}
	
}
