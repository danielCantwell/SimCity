/**
 * 
 */
package housing.interfaces;

import housing.roles.OwnerRole.Appliance;

import java.util.List;

import SimCity.Globals.Money;

/**
 * @author Daniel
 *
 */
public interface Tenant {
	
	public abstract void msgHereAreAppliances(List<Appliance> a);
	
	public abstract void msgPayRent(Money m);
	
	public abstract void msgEvictionNotice();
	
	public abstract void msgMorning();

}
