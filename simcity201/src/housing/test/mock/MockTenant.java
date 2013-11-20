/**
 * 
 */
package housing.test.mock;

import restaurant.test.mock.EventLog;
import restaurant.test.mock.LoggedEvent;
import SimCity.Globals.Money;
import housing.interfaces.Tenant;

/**
 * @author Daniel
 *
 */
public class MockTenant implements Tenant {
	
	public EventLog log = new EventLog();

	@Override
	public void msgPayRent(Money m) {
		log.add(new LoggedEvent("Received msgPayRent from owner. "
				+ "Tenant owes $" + m.dollars + "." + m.cents));
	}

	@Override
	public void msgEvictionNotice() {
		log.add(new LoggedEvent("Received msgEvictionNotice from owner."));
	}

}
