package com.nickan.epiphany3D.model.items;

import com.nickan.epiphany3D.model.StatisticsHandler;
import com.nickan.epiphany3D.model.items.Consumable.ConsumableType;

public class Inventory {
	private static final int ROW = 4;
	private static final int COL = 8;
	Item[][] items = new Item[ROW][COL];
	StatisticsHandler statsHandler;
	
	Consumable consumableBeingUsed = null;

	public Inventory(StatisticsHandler statsHandler) {
		this.statsHandler = statsHandler;
		clear();
		
		// Testing
		items[0][0] = new Consumable(ConsumableType.MP_POTION, 15, 3);
	}

	public void update(float delta) {
		if (consumableBeingUsed != null) {
			if (!consumableBeingUsed.updateEffect(statsHandler, delta)) {
				consumableBeingUsed = null;
			}
		}
	}
	
	public void doubleClicked(int row, int col) {
		if (items[row][col] == null)
			return;
		
		switch (items[row][col].getItemClass()) {
		case WEARABLE:
			wear(row, col);
			break;
		case CONSUMABLE:
			consume(row, col);
			break;
		}
	}
	
	private void wear(int row, int col) {
		
	}
	
	private void consume(int row, int col) {
		Consumable consumable = (Consumable) items[row][col];
		consumable.use(statsHandler);
		
		if (consumableBeingUsed != null)
			consumableBeingUsed.addRegenPoints((int) consumable.getRegenPoints());
		consumableBeingUsed = consumable;
		
		if (consumable.isEmpty()) {
			items[row][col] = null;
		}
	}
	
	public void singleClicked(int row, int col) {
		Item item = items[row][col];
		if (item == null)
			return;
		
		switch (item.getItemClass()) {
		case WEARABLE:
			
			break;
		case CONSUMABLE:
			break;
		}
	}
	
	public final Item[][] getItems() {
		return items;
	}
	
	
	public boolean putItem(Item item) {
		for (int row = 0; row < items.length; ++row) {
			for (int col = 0; col < items[row].length; ++col) {
				if (items[row][col] == null) {
					items[row][col] = item;
					return true;
				}
			}
		}
		return false;
	}
	
	
	
	private void clear() {
		for (int row = 0; row < ROW; ++row) {
			for (int col = 0; col < COL; ++col) {
				items[row][col] = null;
			}
		}
	}


}
