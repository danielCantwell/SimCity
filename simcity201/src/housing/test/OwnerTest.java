/**
 * 
 */
package housing.test;

import SimCity.Globals.Money;
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
	MockTenant mtThree;

	MockHousingPerson ownerPerson;

	/**
	 * This method is run before each test. You can use it to instantiate the
	 * class variables for your agent and mocks, etc.
	 */
	public void setUp() throws Exception {
		super.setUp();
		owner = new OwnerRole();
		mtOne = new MockTenant();
		mtTwo = new MockTenant();
		mtThree = new MockTenant();
		ownerPerson = new MockHousingPerson("housing.roles.OwnerRole");
		
		owner.myPerson = ownerPerson;
		owner.myPerson.house = "Apartment";
	}

	/*
	 * Tests one tenant paying full rent, paying partial rent, paying no rent,
	 * and being evicted
	 */
	public void testOneTenant() {
		try {
			setUp();
		} catch (Exception e) {
			System.out.println("setUp failed");
			e.printStackTrace();
		}

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
		assertEquals(
				"Owner should have added rent to his money, from 100 to 200.",
				200, owner.myPerson.money.dollars);
		assertEquals("Tenant should not owe any rent. He does.", 0,
				owner.myTenants.get(0).rentOwed.dollars);

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
		owner.msgHereIsRent(mtOne, new Money(50, 0));
		// Check Post Conditions
		assertEquals(
				"Owner should have added rent to his money, from 200 to 250.",
				250, owner.myPerson.money.dollars);
		assertEquals("Tenant should owe $50 in rent. He doesn't.", 50,
				owner.myTenants.get(0).rentOwed.dollars);

		// PickAndExecuteAnAction should return false
		assertTrue(owner.pickAndExecuteAnAction());
		// Check Post Conditions
		assertEquals("Tenant should have one strike. He doesn't.", 1,
				owner.myTenants.get(0).strikes);
		assertEquals("Tenant state should be none.", TenantState.None,
				owner.myTenants.get(0).state);

		assertFalse(owner.pickAndExecuteAnAction());

		// Receive Message
		owner.msgTimeToCollectRent();
		// Check Post Conditions
		assertEquals("Tenant state should be OwesRent. It's not.",
				TenantState.OwesRent, owner.myTenants.get(0).state);
		assertEquals("Tenant's rentOwed should be $150. It's not.",
				owner.RENT.dollars + 50,
				owner.myTenants.get(0).rentOwed.dollars);

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
		owner.msgCannotPayRent(mtOne);
		// Check Post Conditions
		assertEquals("Owner should not have added rent to his money.", 250,
				owner.myPerson.money.dollars);
		assertEquals("Tenant should owe $150 in rent. He doesn't.", 150,
				owner.myTenants.get(0).rentOwed.dollars);
		assertEquals("Tenant state should be InDebt. It's not.",
				TenantState.InDebt, owner.myTenants.get(0).state);

		owner.myTenants.get(0).strikes = 4;

		// PickAndExecuteAnAction should return true
		assertTrue(owner.pickAndExecuteAnAction());
		// Check Post Conditions
		assertEquals(
				"Owner should have evicted the tenant, and have no tenants.",
				0, owner.myTenants.size());
	}

	/*
	 * Tests multiple tenants
	 */
	public void testThreeTenant() {
		try {
			setUp();
		} catch (Exception e) {
			System.out.println("setUp failed");
			e.printStackTrace();
		}

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
		owner.msgAddTenant(mtTwo);
		// Check Post Conditions
		assertEquals("Owner should have two tenants. It doesn't.", 2,
				owner.myTenants.size());

		// PickAndExecuteAnAction should return false
		assertFalse(owner.pickAndExecuteAnAction());

		// Receive Message
		owner.msgAddTenant(mtThree);
		// Check Post Conditions
		assertEquals("Owner should have two tenants. It doesn't.", 3,
				owner.myTenants.size());

		// PickAndExecuteAnAction should return false
		assertFalse(owner.pickAndExecuteAnAction());

		// Receive Message
		owner.msgTimeToCollectRent();
		// Check Post Conditions
		assertEquals("Tenant One state should be OwesRent. It's not.",
				TenantState.OwesRent, owner.myTenants.get(0).state);
		assertEquals("Tenant's rentOwed should be zero. It's not.",
				owner.RENT.dollars, owner.myTenants.get(0).rentOwed.dollars);
		assertEquals("Tenant Two state should be OwesRent. It's not.",
				TenantState.OwesRent, owner.myTenants.get(1).state);
		assertEquals("Tenant's rentOwed should be zero. It's not.",
				owner.RENT.dollars, owner.myTenants.get(1).rentOwed.dollars);
		assertEquals("Tenant Three state should be OwesRent. It's not.",
				TenantState.OwesRent, owner.myTenants.get(2).state);
		assertEquals("Tenant's rentOwed should be zero. It's not.",
				owner.RENT.dollars, owner.myTenants.get(2).rentOwed.dollars);

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

		// PickAndExecuteAnAction should return true
		assertTrue(owner.pickAndExecuteAnAction());
		// Check Post Conditions
		assertTrue(
				"Tenant Two should have logged \"Received msgPayRent from owner. "
						+ "Tenant owes $"
						+ owner.myTenants.get(1).rentOwed.dollars + "."
						+ owner.myTenants.get(1).rentOwed.cents
						+ "\" but didn't. His log reads instead: "
						+ mtTwo.log.getLastLoggedEvent().toString(),
				mtTwo.log.containsString("Received msgPayRent from owner. "
						+ "Tenant owes $"
						+ owner.myTenants.get(1).rentOwed.dollars + "."
						+ owner.myTenants.get(1).rentOwed.cents));

		// PickAndExecuteAnAction should return true
		assertTrue(owner.pickAndExecuteAnAction());
		// Check Post Conditions
		assertTrue(
				"Tenant Three should have logged \"Received msgPayRent from owner. "
						+ "Tenant owes $"
						+ owner.myTenants.get(2).rentOwed.dollars + "."
						+ owner.myTenants.get(2).rentOwed.cents
						+ "\" but didn't. His log reads instead: "
						+ mtThree.log.getLastLoggedEvent().toString(),
				mtThree.log.containsString("Received msgPayRent from owner. "
						+ "Tenant owes $"
						+ owner.myTenants.get(2).rentOwed.dollars + "."
						+ owner.myTenants.get(2).rentOwed.cents));

		// Receive Message
		owner.msgHereIsRent(mtOne, owner.myTenants.get(0).rentOwed);
		// Check Post Conditions
		assertEquals(
				"Owner should have added rent to his money, from 100 to 200.",
				200, owner.myPerson.money.dollars);
		assertEquals("Tenant should not owe any rent. He does.", 0,
				owner.myTenants.get(0).rentOwed.dollars);

		// PickAndExecuteAnAction should return false
		assertFalse(owner.pickAndExecuteAnAction());

		// Receive Message
		owner.msgHereIsRent(mtTwo, new Money(50, 0));
		// Check Post Conditions
		assertEquals(
				"Owner should have added rent to his money, from 200 to 250.",
				250, owner.myPerson.money.dollars);
		assertEquals("Tenant should owe $50 rent. He doesn't.", 50,
				owner.myTenants.get(1).rentOwed.dollars);
		assertEquals("Tenant two should have state InDebt. He doesn't.",
				TenantState.InDebt, owner.myTenants.get(1).state);

		// PickAndExecuteAnAction should return true
		assertTrue(owner.pickAndExecuteAnAction());
		// Check Post Conditions
		assertEquals("Tenant two should have state None. He doesn't.",
				TenantState.None, owner.myTenants.get(1).state);
		assertEquals("Tenant two should have one strike. He doesn't.",
				1, owner.myTenants.get(1).strikes);

		// Receive Message
		owner.msgCannotPayRent(mtThree);
		// Check Post Conditions
		assertEquals("Owner should not add rent to his money.", 250,
				owner.myPerson.money.dollars);
		assertEquals("Tenant should owe $100 rent. He doesn't.", 100,
				owner.myTenants.get(2).rentOwed.dollars);
		assertEquals("Tenant three should have state InDebt. He doesn't.",
				TenantState.InDebt, owner.myTenants.get(2).state);

		// PickAndExecuteAnAction should return true
		assertTrue(owner.pickAndExecuteAnAction());
		// Check Post Conditions
		assertEquals("Tenant three should have state None. He doesn't.",
				TenantState.None, owner.myTenants.get(2).state);
		assertEquals("Tenant two should have one strike. He doesn't.",
				1, owner.myTenants.get(2).strikes);
	}

}
