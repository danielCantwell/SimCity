/**
 * 
 */
package housing.interfaces;

import SimCity.Globals.Money;

/**
 * @author Daniel
 *
 */
public interface Tenant {
	
	public abstract void msgPayRent(Money m);
	
	public abstract void msgEvictionNotice();

}
