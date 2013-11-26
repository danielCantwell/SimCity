/**
 * 
 */
package SimCity.Base.tests;

import SimCity.Base.God;
import SimCity.Base.God.BuildingType;
import SimCity.Base.Person;
import SimCity.Base.Person.Action;
import SimCity.Base.Person.GoAction;
import SimCity.Base.Person.Intent;
import SimCity.Base.Person.Morality;
import SimCity.Base.Person.Vehicle;
import SimCity.Buildings.B_Bank;
import SimCity.Buildings.B_House;
import SimCity.Globals.Money;
import housing.roles.OwnerRole;
import housing.test.mock.MockTenant;
import junit.framework.TestCase;

/**
 * @author Brian
 *
 */
public class GodTest extends TestCase {
	
	OwnerRole owner;
	Person person;
	MockTenant mtOne;
	MockTenant mtTwo;
	
	/**
	 * This method is run before each test. You can use it to instantiate the
	 * class variables for your agent and mocks, etc.
	 */
	public void setUp() throws Exception {
		super.setUp();
		God.Get();
		person = new Person("Briiiannn", null, "Bank.bankCustomerRole", Vehicle.walk, Morality.good, new Money(60,3), new Money(10, 0), 10, 3, "house", new B_House(25, null));
	}
	
	public void set(){
		try {
			setUp();
		} catch (Exception e) {
			System.out.println("setUp failed");
			e.printStackTrace();
		}
	}
	
	public void test_CheckConstructor() {
		set();
		assertTrue("God should have zero hour", God.Get().hour == 0);
		assertTrue("God should have zero day", God.Get().day == 0);
		
		
	}
}


















