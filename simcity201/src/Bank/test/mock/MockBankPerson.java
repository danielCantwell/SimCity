/**
 * 
 */
package Bank.test.mock;

import restaurant.test.mock.EventLog;
import restaurant.test.mock.LoggedEvent;
import SimCity.Base.Person;
import SimCity.Globals.Money;

/**
 * @author Eric
 * 
 */
public class MockBankPerson extends Person {
	
	public EventLog log = new EventLog();

	
	public MockBankPerson(String mainRole) {
		super(mainRole);
	}
}
