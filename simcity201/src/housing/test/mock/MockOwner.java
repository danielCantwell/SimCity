/**
 * 
 */
package housing.test.mock;

import restaurant.test.mock.EventLog;
import restaurant.test.mock.LoggedEvent;
import SimCity.Globals.Money;
import housing.interfaces.Owner;
import housing.interfaces.Tenant;
import housing.roles.OwnerRole.Appliance;

/**
 * @author Daniel
 *
 */
public class MockOwner implements Owner {
	
	public MockOwner() {
		
	}
	
	public EventLog log = new EventLog();

	@Override
	public void msgTimeToCollectRent() {
		log.add(new LoggedEvent("Received msgTimeToCollectRent from God class"));
	}

	@Override
	public void msgHereIsRent(Tenant t, Money m) {
		log.add(new LoggedEvent("Received msgHereIsRent from tenant."
				+ " Tenant Payed $" + m.dollars + "." + m.cents));
	}

	@Override
	public void msgCannotPayRent(Tenant t) {
		log.add(new LoggedEvent("Received msgCannotPayRent from tenant."));
	}

	@Override
	public void msgApplianceBroken(Tenant t, Appliance a) {
		log.add(new LoggedEvent("Received msgApplianceBroken from tenant."));
	}

	@Override
	public void msgAddTenant(Tenant t) {
		log.add(new LoggedEvent("Received msgAddTenant from UI"));
	}

}
