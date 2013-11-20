/**
 * 
 */
package housing.interfaces;

import java.util.ArrayList;
import java.util.List;

import housing.roles.OwnerRole.Appliance;
import SimCity.Globals.Money;

/**
 * @author Daniel
 *
 */
public interface Owner {
	
	public abstract void msgAddTenant(Tenant t);
	
	public abstract void msgTimeToCollectRent();
	
	public abstract void msgHereIsRent(Tenant t, Money m);
	
	public abstract void msgCannotPayRent(Tenant t);
	
	public abstract void msgApplianceBroken(Tenant t, Appliance a);

}
