package agent;

import java.util.ArrayList;
import java.util.List;

public class Menu {
	public List<String> foodChoices = new ArrayList<String>();
	
	public Menu() {
		foodChoices.add("Steak");
		foodChoices.add("Chicken");
		foodChoices.add("Salad");
		foodChoices.add("Pizza");
	}
	
	public double getPrice(String choice) {
		// Prices of food are on the Menu
		if (choice == "Steak") return 15.99;
		if (choice == "Chicken") return 11.99;
		if (choice == "Salad") return 5.99;
		if (choice == "Pizza") return 8.99;
		
		return -1;
		
	}
}
