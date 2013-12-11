/**
 * 
 */
package housing.roles;

import housing.interfaces.Owner;
import housing.interfaces.Tenant;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import SimCity.Base.Person;
import SimCity.Base.Role;
import SimCity.Globals.Money;
import SimCity.trace.AlertTag;

/**
 * @author Daniel
 * 
 */
public class OwnerRole extends Role implements Owner {

	public OwnerRole() {
	}

	// -------------------------------------DATA-------------------------------------

	public final Money RENT = new Money(100, 0);

	public List<MyTenant> myTenants = Collections
			.synchronizedList(new ArrayList<MyTenant>());

	public List<Appliance> appliances = Collections
			.synchronizedList(new ArrayList<Appliance>() {
				{
					add(new Appliance("Fridge", 10));
					add(new Appliance("Stove", 8));
					add(new Appliance("Table", 6));
					add(new Appliance("Bed", 4));
				}
			});

	public enum TenantState {
		None, OwesRent, Notified, InDebt
	};

	public enum ApplianceState {
		Working, NeedsFixing
	};

	// -----------------------------------MESSAGES-----------------------------------

	// MSG from setup
	public void msgAddTenant(Tenant tenant) {
		boolean newTenant = true;
		synchronized (myTenants) {
			for (MyTenant mt : myTenants) {
				if (mt.tenant == tenant) {
					newTenant = false;
				}
			}
		}

		synchronized (myTenants) {
			if (newTenant) {
				Do(AlertTag.House,"Owner added a tenant");
				myTenants.add(new MyTenant(tenant));
				tenant.msgHouseInfo(appliances, myTenants.size());
				Do(AlertTag.House,"Owner has " + myTenants.size()
						+ " tenants.");
			}
		}
	}

	// MSG from the God class at a certain time
	public void msgTimeToCollectRent() {
		if (myPerson.getHomeType() == "Apartment") {
			Do(AlertTag.House,"Owner is collecting rent from tenants");
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
				if (tenant.tenant == t) {
					myPerson.money.add(m);
					tenant.rentOwed.subtract(m);
					if (tenant.rentOwed.isZero()) {
						tenant.state = TenantState.None;
						Do(AlertTag.House,"Owner received rent from tenant");
					} else {
						tenant.state = TenantState.InDebt;
						Do(AlertTag.House,"Owner did not receive full rent from tenant");
					}
					stateChanged();
					break;
				}
			}
		}
	}

	// MSG from a tenant
	public void msgCannotPayRent(Tenant t) {
		synchronized (myTenants) {
			for (MyTenant tenant : myTenants) {
				if (tenant.tenant == t) {
					Do(AlertTag.House,"Owner has a tenant who cannot pay rent");
					tenant.state = TenantState.InDebt;
					stateChanged();
					break;
				}
			}
		}
	}

	// MSG from a tenant
	public void msgApplianceBroken(Tenant t, Appliance a) {
		synchronized (myTenants) {
			for (MyTenant tenant : myTenants) {
				if (tenant.tenant == t) {
					Do(AlertTag.House,"Owner has a tenant who broke a "+ a.type);
					tenant.rentOwed.add(20, 0);
					stateChanged();
					break;
				}
			}
		}
	}

	// -----------------------------------SCHEDULER-----------------------------------

	public boolean pickAndExecuteAnAction() {

		/*
		 * synchronized (myTenants) { for (MyTenant t : myTenants) { if
		 * (t.strikes > 3) { evictTenant(t); return true; } } }
		 */
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
				if (t.state == TenantState.InDebt) {
					t.strikes++;
					t.state = TenantState.None;
					return true;
				}
			}
		}
		
		synchronized (appliances) {
			for (Appliance a : appliances) {
				if (a.state == ApplianceState.NeedsFixing) {
					a.fixAppliance();
					Do(AlertTag.House,"Owner fixed appliance " + a.type);
					return true;
				}
			}
		}

		return false;
	}

	// ------------------------------------ACTIONS------------------------------------

	public void collectRent(MyTenant t) {
		t.tenant.msgPayRent(t.rentOwed);
		t.state = TenantState.Notified;
	}

	public void evictTenant(MyTenant t) {
		t.tenant.msgEvictionNotice();
		myTenants.remove(t);
	}

	public class MyTenant {
		public Tenant tenant;
		public TenantState state = TenantState.None;
		public Money rentOwed = new Money(0, 0);

		// If in debt for > 3 pay periods, tenant is kicked out
		public int strikes = 0;

		public MyTenant(Tenant t) {
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
		// Do Nothing
	}

	@Override
	protected void enterBuilding() {
		// Do Nothing
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return "HOwn";
	}

}