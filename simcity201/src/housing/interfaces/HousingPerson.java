/**
 * 
 */
package housing.interfaces;

import SimCity.Base.Person.PersonState;
import SimCity.Globals.Money;

/**
 * @author Daniel
 *
 */
public interface HousingPerson {

	public abstract void setHomeType(String type);
	public abstract int getHungerLevel();
	public abstract int getHungerThreshold();
	public abstract PersonState getPersonState();
	public abstract Money getMoney();
}
