package SimCity;
import housing.roles.OwnerRole;
import housing.roles.TenantRole;

import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;

import Bank.bankCustomerRole;
import Bank.bankGuardRole;
import Bank.bankManagerRole;
import Bank.tellerRole;
import SimCity.Base.God;
import SimCity.Base.Person;
import SimCity.Base.Person.Intent;
import SimCity.Buildings.B_Bank;
import SimCity.Buildings.B_House;
import restaurant.gui.RestaurantGui;
/**
 * @author Brian
 *
 */
public class Main{

	public static void main(String[] args) {
		God.Get();
		//Create a bank
		//B_Bank bank = new B_Bank(0);
		//God.Get().addBuilding(bank);
		/*
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
		
		bank.setBankManager(bmr);
		bgr.setBank((B_Bank)God.Get().getBuilding(0));
		
		bmr.setActive(true);
		tr.setActive(true);
		bgr.setActive(true);
		//bcr.setActive(true);
		
		bcr.setGuard(bgr);
		bcr.setPerson(bankCustomer);
		bcr.setMoney(bankCustomer.getMoney());
		bcr.setAccNum(bankCustomer.getAccNum());
		
		//Setup people and bank
		
		bmr.newTeller(tr);
		bmr.setGuard(bgr);
		
		System.out.println ("##--------Setup complete--------## \n");
		
		bankCustomer.msgGoToBuilding(bank, Intent.customer);
		*/
		
		B_House myHome = new B_House(25, null);
		God.Get().addBuilding(myHome);

		Person owner = new Person("housing.roles.OwnerRole");
		Person tenant = new Person("housing.roles.TenantRole");
		
		
		OwnerRole or= (OwnerRole)owner.mainRole;
		TenantRole tr= (TenantRole)tenant.mainRole;
		
		or.setPerson(owner);
		tr.setPerson(tenant);
		
		myHome.setOwner(or);
		owner.setHouse(myHome);
		tenant.setHouse(myHome);
		
//		/owner.msgGoToBuilding(myHome, Intent.work);
		tenant.msgGoToBuilding(myHome, Intent.customer);
		
		owner.startThread();
		tenant.startThread();
		
		
		
	    /*RestaurantGui gui = new RestaurantGui();
	    gui.setTitle("csci201 Restaurant");
	    gui.setVisible(true);
	    gui.setResizable(false);
	    gui.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	    */
	}

}
