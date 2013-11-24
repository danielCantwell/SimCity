/**
 * 
 */
package SimCity.Base.tests;

import SimCity.Base.Person;
import SimCity.Base.Person.Morality;
import SimCity.Base.Person.Vehicle;
import SimCity.Globals.Money;
import housing.roles.OwnerRole;
import housing.test.mock.MockTenant;
import junit.framework.TestCase;

/**
 * @author Brian
 *
 */
public class PersonTest extends TestCase {
	
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
		person = new Person("Briiiannn", null, "Bank.bankCustomerRole", Vehicle.walk, Morality.good, new Money(60,3), new Money(10, 0), 10, 3, "house");
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
		assertTrue("Name should be Briiiannn", person.name.equals("Briiiannn"));
		//Test main role
		assertTrue("Main role should be Bank.bankCustomerRole class: ", person.mainRole instanceof Bank.bankCustomerRole);
		//Test vehicle
		assertTrue("Vehicle should be walk", person.vehicle == Vehicle.walk);
		//Test Morality
		assertTrue("Morality is good" , person.mor == Morality.good);
		//Test current money
		assertTrue("Dollar amount in person shoudld be 60.03", person.money.dollars == 60 && person.money.cents == 3);
		//Test money threshold.
		assertTrue("Money threshold should be $10", person.moneyThreshold.dollars == 10 && person.moneyThreshold.cents == 0);
		//Test hunger level
		assertTrue("Hunger level should be 10, which is not hungry", person.hungerLevel == 10);
		assertTrue("Hunger threshold should be 3, thats when hes hunry", person.hungerThreshold == 3);
		assertTrue("Person's house should be initialized to string house", person.house.equals("house"));
		assertTrue("Person's inventory should be initialized to have nothing in it. ", person.inventory.size() == 0);
	}
	
	
}


















