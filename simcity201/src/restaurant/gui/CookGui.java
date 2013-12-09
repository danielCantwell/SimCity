/**
 * 
 */
package restaurant.gui;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import SimCity.gui.Gui;

/**
 * @author Daniel
 * 
 */
public class CookGui implements Gui {

	boolean pause;
	boolean isPresent = true;

	private List<Food> food = Collections.synchronizedList(new ArrayList<Food>());

	enum FoodState {
		Cooking, Plating
	};
	
	public CookGui() {
		isPresent = true;
	}

	@Override
	public void updatePosition() {

	}

	@Override
	public void draw(Graphics2D g) {
		g.setColor(Color.WHITE);
		synchronized (this.food) {
			for (Food food : this.food) {
				if (food.state == FoodState.Cooking) {
					if (food.table == 1) {
						g.drawString(food.foodIcon, 122, 405);
					} else if (food.table == 2) {
						g.drawString(food.foodIcon, 139, 405);
					} else if (food.table == 3) {
						g.drawString(food.foodIcon, 157, 405);
					} else if (food.table == 4) {
						g.drawString(food.foodIcon, 174, 405);
					}
				} else if (food.state == FoodState.Plating) {
					if (food.table == 1) {
						g.drawString(food.foodIcon, 232, 405);
					} else if (food.table == 2) {
						g.drawString(food.foodIcon, 248, 405);
					} else if (food.table == 3) {
						g.drawString(food.foodIcon, 267, 405);
					} else if (food.table == 4) {
						g.drawString(food.foodIcon, 284, 405);
					}
				}
			}
		}
	}

	@Override
	public boolean isPresent() {
		return isPresent;
	}
	
	public void setPresent(boolean p) {
		isPresent = p;
	}

	@Override
	public void pause() {
		pause = true;
	}

	@Override
	public void restart() {
		pause = false;
	}

	private class Food {
		String type;
		String foodIcon;
		FoodState state = FoodState.Cooking;
		int table; // used as a kind of ID

		Food(String type, int table) {
			this.type = type;
			this.table = table;

			if (type == "Steak")
				foodIcon = "St";
			else if (type == "Chicken")
				foodIcon = "Ch";
			else if (type == "Pizza")
				foodIcon = "Pz";
			else if (type == "Salad")
				foodIcon = "Sa";
		}
	}

	public void DoCookFood(String type, int table) {
		food.add(new Food(type, table));
	}

	public void DoPlateFood(String type, int table) {
		synchronized (this.food) {
			for (Food food : this.food) {
				if (food.type == type && food.table == table) {
					food.state = FoodState.Plating;
					break;
				}
			}
		}
	}

	public void DoRemoveFood(String type, int table) {
		synchronized (this.food) {
			for (Food food : this.food) {
				if (food.type == type && food.table == table) {
					this.food.remove(food);
					break;
				}
			}
		}
	}

}
