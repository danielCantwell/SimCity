/**
 * 
 */
package housing.test.mock;

import java.util.List;

import restaurant.test.mock.EventLog;
import restaurant.test.mock.LoggedEvent;
import SimCity.Globals.Money;
import housing.gui.TenantGui;
import housing.interfaces.Tenant;
import housing.roles.OwnerRole.Appliance;

/**
 * @author Daniel
 *
 */
public class MockTenant implements Tenant {
	
	public MockHousingPerson myPerson;
	
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

	@Override
	public void msgHereAreAppliances(List<Appliance> a) {
		log.add(new LoggedEvent("Received msgHereAreAppliances from owner."));
	}
	
	@Override
	public void msgMorning() {
		log.add(new LoggedEvent("Received msgMorning from god."));
	}

	@Override
	public void msgAtTable() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgAtBed() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgAtFridge() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgAtStove() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgAtMail() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgAtDoor() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public TenantGui getGui() {
		// TODO Auto-generated method stub
		return null;
	}

}
