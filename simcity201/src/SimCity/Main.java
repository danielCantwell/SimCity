package SimCity;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;

import Bank.bankCustomerRole;
import Bank.bankGuardRole;
import Bank.bankManagerRole;
import Bank.tellerRole;
import SimCity.Base.God;
import SimCity.Base.Person;
import SimCity.Buildings.B_Bank;
import restaurant.gui.RestaurantGui;
/**
 * @author Brian
 *
 */
public class Main{

	public static void main(String[] args) {
		God.Get();
		//Create a bank
		God.Get().addBuilding(new B_Bank());
		
		//Create people for bank
		Person manager = new Person("Bank.bankManagerRole");
		Person teller = new Person("Bank.tellerRole");
		Person guard = new Person("Bank.bankGuardRole");
		Person bankCustomer = new Person("Bank.bankCustomerRole");
		
		manager.startThread();
		teller.startThread();
		guard.startThread();
		bankCustomer.startThread();
		
		//Make the roles for each
		bankManagerRole bmr = (bankManagerRole)(manager.roles.get(0));
		tellerRole tr = (tellerRole)(teller.roles.get(0));
		bankGuardRole bgr = (bankGuardRole)guard.roles.get(0);
		bankCustomerRole bcr = (bankCustomerRole)bankCustomer.roles.get(0);
		
		bgr.setBank((B_Bank)God.Get().getBuilding(0));
		
		bmr.setActive(true);
		tr.setActive(true);
		bgr.setActive(true);
		bcr.setActive(true);
		
		bcr.setGuard(bgr);
		
		//Setup people and bank
		
		bmr.newTeller(tr);
		bmr.setGuard(bgr);
		
		System.out.println ("##--------Setup complete--------## \n");
		
		God.Get().EnterBuilding(God.Get().getBuilding(0), bankCustomer, "Bank.bankCustomerRole");
		

	    /*RestaurantGui gui = new RestaurantGui();
	    gui.setTitle("csci201 Restaurant");
	    gui.setVisible(true);
	    gui.setResizable(false);
	    gui.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	    */
	}

}
