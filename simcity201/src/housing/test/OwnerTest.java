/**
 * 
 */
package housing.test;

import restaurant.test.mock.LoggedEvent;
import SimCity.Globals.Money;
import SimCity.gui.Gui;
import housing.roles.OwnerRole;
import housing.roles.OwnerRole.TenantState;
import housing.test.mock.MockHousingPerson;
import housing.test.mock.MockTenant;
import junit.framework.TestCase;

/**
 * @author Daniel
 * 
 */
public class OwnerTest extends TestCase {

	OwnerRole owner;

	MockTenant mtOne;
	MockTenant mtTwo;

	MockHousingPerson ownerPerson;
	MockHousingPerson tenantPersonOne;
	MockHousingPerson tenantPersonTwo;

	/**
	 * This method is run before each test. You can use it to instantiate the
	 * class variables for your agent and mocks, etc.
	 */
	public void setUp() throws Exception {
		super.setUp();
		owner = new OwnerRole();
		mtOne = new MockTenant();
		mtTwo = new MockTenant();
		ownerPerson = new MockHousingPerson(null,
				"housing.roles.OwnerRole", null, null, new Money(100, 0),
				new Money(12, 0), 8, 5);
		tenantPersonOne = new MockHousingPerson(null,
				"housing.roles.TenantRole", null, null, new Money(230, 0),
				new Money(12, 0), 8, 5);
		tenantPersonTwo = new MockHousingPerson(null,
				"housing.roles.TenantRole", null, null, new Money(80, 0),
				new Money(12, 0), 8, 5);

		owner.myPerson = ownerPerson;
	}

	public void testOneTenantPaysRent() {
		try {
			setUp();
		} catch (Exception e) {
			System.out.println("setUp failed");
			e.printStackTrace();
		}

		owner.myPerson.house = "Apartment";

		// Check Pre-Conditions
		assertEquals(
				"Owner should have a list of appliances of size 4. It doesn't.",
				4, owner.appliances.size());
		assertEquals("Owner should have zero tenants. It doesn't.", 0,
				owner.myTenants.size());

		// Receive Message
		owner.msgAddTenant(mtOne);
		// Check Post Conditions
		assertEquals("Owner should have one tenant. It doesn't.", 1,
				owner.myTenants.size());

		// PickAndExecuteAnAction should return false
		assertFalse(owner.pickAndExecuteAnAction());

		// Receive Message
		owner.msgTimeToCollectRent();
		// Check Post Conditions
		assertEquals("Tenant state should be OwesRent. It's not.",
				TenantState.OwesRent, owner.myTenants.get(0).state);
		assertEquals("Tenant's rentOwed should be zero. It's not.",
				owner.RENT.dollars, owner.myTenants.get(0).rentOwed.dollars);

		// PickAndExecuteAnAction should return true
		assertTrue(owner.pickAndExecuteAnAction());
		// Check Post Conditions
		assertTrue(
				"Tenant One should have logged \"Received msgPayRent from owner. "
						+ "Tenant owes $"
						+ owner.myTenants.get(0).rentOwed.dollars + "."
						+ owner.myTenants.get(0).rentOwed.cents
						+ "\" but didn't. His log reads instead: "
						+ mtOne.log.getLastLoggedEvent().toString(),
				mtOne.log.containsString("Received msgPayRent from owner. "
						+ "Tenant owes $"
						+ owner.myTenants.get(0).rentOwed.dollars + "."
						+ owner.myTenants.get(0).rentOwed.cents));
		
		// Receive Message
		owner.msgHereIsRent(mtOne, owner.myTenants.get(0).rentOwed);
		// Check Post Conditions
		//assertEquals("Owner should have added rent to his money, from 100 to 200.", 200, owner.myPerson.money.dollars);
		//assertEquals("Tenant should have subtracted rent from his money, from 230 to 130.", 130, )
	}

}
