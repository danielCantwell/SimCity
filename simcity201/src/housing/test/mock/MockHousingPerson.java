/**
 * 
 */
package housing.test.mock;

import restaurant.test.mock.EventLog;
import restaurant.test.mock.LoggedEvent;
import SimCity.Base.Person;
import SimCity.Base.Person.PersonState;
import SimCity.Globals.Money;
import SimCity.gui.Gui;
import housing.interfaces.HousingPerson;

/**
 * @author Daniel
 * 
 */
public class MockHousingPerson extends Person {

	public MockHousingPerson(Gui gui, String mainRole, Vehicle vehicle,
			Morality morality, Money money, Money moneyThresh, int hunger,
			int hungerThresh) {
		super(gui, mainRole, vehicle, morality, money, moneyThresh, hunger,
				hungerThresh);
	}
	
	@Override
	public int getHungerLevel() {
		// TODO Auto-generated method stub
		return super.getHungerLevel();
	}

}
