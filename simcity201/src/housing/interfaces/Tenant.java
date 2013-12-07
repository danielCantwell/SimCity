/**
 * 
 */
package housing.interfaces;

import housing.gui.HousingAnimation;
import housing.gui.TenantGui;
import housing.roles.OwnerRole.Appliance;

import java.util.List;

import SimCity.Globals.Money;

/**
 * @author Daniel
 *
 */
public interface Tenant {
	
	public abstract void msgHouseInfo(Owner owner, List<Appliance> a, int tenantNumber);
	
	public abstract void msgPayRent(Money m);
	
	public abstract void msgEvictionNotice();
	
	public abstract void msgMorning();
	
	public abstract void msgAtTable();

	public abstract void msgAtBed();

	public abstract void msgAtFridge();

	public abstract  void msgAtStove();

	public abstract void msgAtMail();

	public abstract void msgAtDoor();
	
	public abstract TenantGui getGui();

}
