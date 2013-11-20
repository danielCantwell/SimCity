/**
 * 
 */
package housing.roles;

import housing.interfaces.Owner;
import housing.interfaces.Tenant;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import SimCity.Base.Person;
import SimCity.Globals.Money;
import restaurant.WaiterAgent.MyCustomer;

/**
 * @author Daniel
 * 
 */
public class OwnerRole implements Owner{
	
	public OwnerRole() {
		appliances.add(new Appliance("Fridge", 10));
		appliances.add(new Appliance("Stove", 8));
		appliances.add(new Appliance("Table", 6));
		appliances.add(new Appliance("Bed", 4));
	}

	/*
	 * Data
	 */
	
	Person person;

	private final Money RENT = new Money(100, 0);

	public List<MyTenant> tenants = Collections
			.synchronizedList(new ArrayList<MyTenant>());
	
	public List<Appliance> appliances = Collections
			.synchronizedList(new ArrayList<Appliance>());

	enum TenantState {
		None, OwesRent, Notified, InDebt
	};
	enum ApplianceState {
		Working, NeedsFixing
	};

	/*
	 * Messages
	 */

	// MSG from the God class at a certain time
	public void msgTimeToCollectRent() {
		if (person.getHomeType() == "Apartment") {
			synchronized (tenants) {
				for (MyTenant tenant : tenants) {
					tenant.state = TenantState.OwesRent;
					tenant.rentOwed.add(RENT);
					stateChanged();
				}
			}
		}
	}

	// MSG from a tenant
	public void msgHereIsRent(Tenant t, Money m) {
		synchronized (tenants) {
			for (MyTenant tenant : tenants) {
				tenant.rentOwed.subtract(m);
				if (tenant.rentOwed.isZero()) {
					tenant.state = TenantState.None;
				}
				else {
					tenant.state = TenantState.InDebt;
				}
				stateChanged();
			}
		}
	}

	// MSG from a tenant
	public void msgCannotPayRent(Tenant t) {
		synchronized (tenants) {
			for (MyTenant tenant : tenants) {
				tenant.state = TenantState.InDebt;
				stateChanged();
			}
		}
	}

	// MSG from a tenant
	public void msgApplianceBroken(Tenant t, Appliance a) {
		synchronized (tenants) {
			for (MyTenant tenant : tenants) {
				if (tenant.tenant == t) {
					tenant.rentOwed.add(20, 0);
					a.state = ApplianceState.NeedsFixing;
					stateChanged();
				}
			}
		}
	}

	/*
	 * Scheduler
	 */

	protected boolean pickAndExecuteAnAction() {
		synchronized (tenants) {
			for (MyTenant t : tenants) {
				if (t.state == TenantState.OwesRent) {
					collectRent(t);
					return true;
				}
			}
		}
		
		synchronized (tenants) {
			for (MyTenant t : tenants) {
				if (t.strikes > 3) {
					evictTenant(t);
					return true;
				}
			}
		}
		
		synchronized (tenants) {
			for (MyTenant t : tenants) {
				if (t.state == TenantState.InDebt) {
					t.strikes++;
					t.state = TenantState.None;
					return true;
				}
			}
		}
		
		synchronized(appliances) {
			for (Appliance a : appliances) {
				if (a.state == ApplianceState.NeedsFixing) {
					a.fixAppliance();
					return true;
				}
			}
		}

		return false;
	}

	/*
	 * Actions
	 */

	private void collectRent(MyTenant t) {
		t.tenant.msgPayRent(RENT);
		t.state = TenantState.Notified;
	}
	
	private void evictTenant(MyTenant t) {
		t.tenant.msgEvictionNotice();
		tenants.remove(t);
	}

	private class MyTenant {
		Tenant tenant;
		TenantState state = TenantState.None;
		Money rentOwed;

		// If in debt for > 3 pay periods, tenant is kicked out
		int strikes = 0;

		MyTenant(Tenant t) {
			tenant = t;
		}
	}
	
	public class Appliance {
		String type;
		ApplianceState state;
		int initialDurability;
		int durability;
		
		Appliance(String type, int durability) {
			this.type = type;
			this.initialDurability = durability;
			this.durability = durability;
		}
		
		int useAppliance() {
			durability--;
			return durability;
		}
		
		void fixAppliance() {
			durability = initialDurability;
		}
	}

}
