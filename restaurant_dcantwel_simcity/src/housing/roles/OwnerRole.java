/**
 * 
 */
package housing.roles;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import SimCity.Base.Person;
import SimCity.Globals.Money;
import restaurant.WaiterAgent.MyCustomer;

/**
 * @author Daniel
 * 
 */
public class OwnerRole {

	/*
	 * Data
	 */
	
	Person person;

	private final Money RENT = new Money(100, 0);

	public List<Tenant> tenants = Collections
			.synchronizedList(new ArrayList<Tenant>());

	enum TenantState {
		None, OwesRent, Notified, InDebt
	};

	/*
	 * Messages
	 */

	// MSG from the God class at a certain time
	public void msgTimeToCollectRent() {
		if (person.getHomeType() == "Apartment") {
			synchronized (tenants) {
				for (Tenant tenant : tenants) {
					tenant.state = TenantState.OwesRent;
					tenant.rentOwed.add(RENT);
				}
			}
		}
	}

	// MSG from a tenant
	public void msgHereIsRent(TenantRole t, Money m) {
		synchronized (tenants) {
			for (Tenant tenant : tenants) {
				tenant.state = TenantState.None;
				tenant.rentOwed.subtract(m);
			}
		}
	}

	// MSG from a tenant
	public void msgCannotPayRent(TenantRole t) {
		synchronized (tenants) {
			for (Tenant tenant : tenants) {
				tenant.state = TenantState.InDebt;
				tenant.rentOwed.subtract(m);
			}
		}
	}

	// MSG from a tenant
	public void msgApplianceBroken(TenantRole t, Appliance a) {

	}

	/*
	 * Scheduler
	 */

	protected boolean pickAndExecuteAnAction() {
		synchronized (tenants) {
			for (Tenant t : tenants) {
				if (t.state == TenantState.OwesRent) {
					collectRent(t);
					return true;
				}
			}
		}
		
		synchronized (tenants) {
			for (Tenant t : tenants) {
				if (t.strikes > 3) {
					evictTenant(t);
					return true;
				}
			}
		}
		
		synchronized (tenants) {
			for (Tenant t : tenants) {
				if (t.state == TenantState.InDebt) {
					t.strikes++;
					t.state = TenantState.None;
					return true;
				}
			}
		}

		return false;
	}

	/*
	 * Actions
	 */

	private void collectRent(Tenant t) {
		t.tenant.msgPayRent(RENT);
		t.state = TenantState.Notified;
	}
	
	private void evictTenant(Tenant t) {
		t.tenant.msgEvictionNotice();
		tenants.remove(t);
	}

	private class Tenant {
		TenantRole tenant;
		TenantState state = TenantState.None;
		Money rentOwed;

		// If in debt for > 3 pay periods, tenant is kicked out
		int strikes = 0;

		Tenant(TenantRole t) {
			tenant = t;
		}
	}

}
