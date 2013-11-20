/**
 * 
 */
package housing.roles;

import housing.interfaces.Owner;
import housing.interfaces.Tenant;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import SimCity.Base.Role;
import SimCity.Globals.Money;

/**
 * @author Daniel
 * 
 */
public class OwnerRole extends Role implements Owner{
	
	public OwnerRole() {
		appliances.add(new Appliance("Fridge", 10));
		appliances.add(new Appliance("Stove", 8));
		appliances.add(new Appliance("Table", 6));
		appliances.add(new Appliance("Bed", 4));
	}

	//-------------------------------------DATA-------------------------------------

	private final Money RENT = new Money(100, 0);

	public List<MyTenant> myTenants = Collections
			.synchronizedList(new ArrayList<MyTenant>());
	
	public List<Appliance> appliances = Collections
			.synchronizedList(new ArrayList<Appliance>());

	enum TenantState {
		None, OwesRent, Notified, InDebt
	};
	enum ApplianceState {
		Working, NeedsFixing
	};

	//-----------------------------------MESSAGES-----------------------------------
	
	// MSG from setup
	public void msgAddTenant(Tenant tenant) {
		myTenants.add(new MyTenant(tenant));
		tenant.msgHereAreAppliances(appliances);
	}

	// MSG from the God class at a certain time
	public void msgTimeToCollectRent() {
		if (myPerson.getHomeType() == "Apartment") {
			synchronized (myTenants) {
				for (MyTenant tenant : myTenants) {
					tenant.state = TenantState.OwesRent;
					tenant.rentOwed.add(RENT);
					stateChanged();
				}
			}
		}
	}

	// MSG from a tenant
	public void msgHereIsRent(Tenant t, Money m) {
		synchronized (myTenants) {
			for (MyTenant tenant : myTenants) {
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
		synchronized (myTenants) {
			for (MyTenant tenant : myTenants) {
				tenant.state = TenantState.InDebt;
				stateChanged();
			}
		}
	}

	// MSG from a tenant
	public void msgApplianceBroken(Tenant t, Appliance a) {
		synchronized (myTenants) {
			for (MyTenant tenant : myTenants) {
				if (tenant.tenant == t) {
					tenant.rentOwed.add(20, 0);
					stateChanged();
				}
			}
		}
	}

	//-----------------------------------SCHEDULER-----------------------------------

	protected boolean pickAndExecuteAnAction() {
		synchronized (myTenants) {
			for (MyTenant t : myTenants) {
				if (t.state == TenantState.OwesRent) {
					collectRent(t);
					return true;
				}
			}
		}
		
		synchronized (myTenants) {
			for (MyTenant t : myTenants) {
				if (t.strikes > 3) {
					evictTenant(t);
					return true;
				}
			}
		}
		
		synchronized (myTenants) {
			for (MyTenant t : myTenants) {
				if (t.state == TenantState.InDebt) {
					t.strikes++;
					t.state = TenantState.None;
					return true;
				}
			}
		}

		return false;
	}

	//------------------------------------ACTIONS------------------------------------

	private void collectRent(MyTenant t) {
		t.tenant.msgPayRent(RENT);
		t.state = TenantState.Notified;
	}
	
	private void evictTenant(MyTenant t) {
		t.tenant.msgEvictionNotice();
		myTenants.remove(t);
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
		public String type;
		public ApplianceState state;
		public int initialDurability;
		public int durability;
		
		public Appliance(String type, int durability) {
			this.type = type;
			this.initialDurability = durability;
			this.durability = durability;
		}
		
		public void useAppliance() {
			durability--;
		}
		
		public void fixAppliance() {
			durability = initialDurability;
			state = ApplianceState.Working;
		}
	}

	@Override
	public void workOver() {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void enterBuilding() {
		// TODO Auto-generated method stub
		
	}

}
