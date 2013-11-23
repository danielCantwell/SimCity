package SimCity;
import javax.swing.JFrame;

import SimCity.Base.God;
import SimCity.Base.Person;
import restaurant.gui.RestaurantGui;

public class Main{
	
	

	public static void main(String[] args) {
		God.Get();
		Person person = new Person("Bank.tellerRole");
		Person guard = new Person("Bank.bankGuardRole");
		Person bankCustomer = new Person("Bank.bankCustomerRole");
	    RestaurantGui gui = new RestaurantGui();
	    gui.setTitle("csci201 Restaurant");
	    gui.setVisible(true);
	    gui.setResizable(false);
	    gui.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

}
