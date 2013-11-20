/**
 * 
 */
package restaurant;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Daniel
 *
 */
public class Menu {
	
	public static int size;

	List<String> foods = new ArrayList<String>();
	List<Double> prices = new ArrayList<Double>();
	
	Menu() {
		foods.add("Steak");
		prices.add(16.00);
		
		foods.add("Chicken");
		prices.add(11.00);
		
		foods.add("Pizza");
		prices.add(9.00);
		
		foods.add("Salad");
		prices.add(6.00);
		
		size = 4;
	}
	
	Menu(String choice) {
		foods.add("Steak");
		prices.add(16.00);
		
		foods.add("Chicken");
		prices.add(11.00);
		
		foods.add("Pizza");
		prices.add(9.00);
		
		foods.add("Salad");
		prices.add(6.00);
		
		prices.remove(foods.indexOf(choice));
		foods.remove(choice);
		
		size = 3;
	}
	
	public int size() {
		return size;
	}
	
	public String at(int i) {
		return foods.get(i);
	}
	
}
