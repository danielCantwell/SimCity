/**
 * 
 */
package housing.test.mock;

import exterior.gui.PersonGui;
import restaurant.test.mock.EventLog;
import restaurant.test.mock.LoggedEvent;
import SimCity.Base.Person;
import SimCity.Buildings.B_House;
import SimCity.Globals.Money;
import SimCity.gui.Gui;

/**
 * @author Daniel
 * 
 */
public class MockHousingPerson extends Person {
	
	public EventLog log = new EventLog();

	public MockHousingPerson(PersonGui gui, String mainRole, Vehicle vehicle,
			Morality morality, Money money, Money moneyThresh, int hunger,
			int hungerThresh) {
		super("Danny", gui, mainRole, vehicle, morality, money, moneyThresh, hunger,
				hungerThresh, "apartment", new B_House(25, null));
	}
	
	public MockHousingPerson(String mainRole) {
		super(mainRole);
	}
	
	@Override
	public void setHomeType(String home) {
		log.add(new LoggedEvent("Received setHomeType from tenant. Type = " + home ));
		super.setHomeType(home);
	}
	
	@Override
	public int getHungerLevel() {
		log.add(new LoggedEvent("Received getHungerLevel from tenant."));
		return super.getHungerLevel();
	}
	
	@Override
	public int getHungerThreshold() {
		log.add(new LoggedEvent("Received getHungerThreshold from tenant."));
		return super.getHungerThreshold();
	}
	
	/*@Override
	public PersonState getPersonState() {
		log.add(new LoggedEvent("Received getPersonState from tenant."));
		return super.getPersonState();
	}*/
	
	@Override
	public Money getMoney() {
		log.add(new LoggedEvent("Received getMoney from tenant."));
		return super.getMoney();
	}
	
	@Override
	public Money getMoneyThreshold() {
		log.add(new LoggedEvent("Received getMoneyThreshold from tenant."));
		return super.getMoneyThreshold();
	}

}
