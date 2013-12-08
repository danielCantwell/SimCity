package jesseRest;

import java.util.ArrayList;
import java.util.List;

import SimCity.Globals.Money;

public class Menu {
	public List<String> foodChoices = new ArrayList<String>();
	
	public Menu() {
		foodChoices.add("Steak");
		foodChoices.add("Chicken");
		foodChoices.add("Salad");
		foodChoices.add("Pizza");
	}
	
	public Money getPrice(String choice) {
		// Prices of food are on the Menu
		if (choice == "Steak") return new Money(15, 99);
		if (choice == "Chicken") return new Money(11, 99);
		if (choice == "Salad") return new Money(5, 99);
		if (choice == "Pizza") return new Money(8, 99);
		
		return null;
		
	}
}
