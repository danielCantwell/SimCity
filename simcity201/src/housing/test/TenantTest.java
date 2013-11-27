/**
 * 
 */
package housing.test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.sun.corba.se.impl.interceptors.PICurrent;

import exterior.gui.SimCityGui;
import SimCity.Base.Person.TimeState;
import housing.roles.OwnerRole;
import housing.roles.TenantRole;
import housing.roles.OwnerRole.Appliance;
import housing.roles.TenantRole.Time;
import housing.test.mock.MockHousingPerson;
import housing.test.mock.MockOwner;
import junit.framework.TestCase;

/**
 * @author Daniel
 * 
 */
public class TenantTest extends TestCase {

	TenantRole tenant;

	MockOwner mockOwner;

	List<Appliance> appliances = Collections
			.synchronizedList(new ArrayList<Appliance>());

	/**
	 * This method is run before each test. You can use it to instantiate the
	 * class variables for your agent and mocks, etc.
	 */
	public void setUp() throws Exception {
		super.setUp();
		tenant = new TenantRole();
		mockOwner = new MockOwner();

		tenant.myPerson = new MockHousingPerson("housing.roles.TenantRole");
		tenant.owner = mockOwner;

		appliances = new OwnerRole().appliances;
		
		SimCityGui scg = new SimCityGui();
	}

	public void testOne() {
		try {
			setUp();
		} catch (Exception e) {
			System.out.println("setUp failed");
			e.printStackTrace();
		}

		// Check Pre Conditions
		assertEquals(
				"Tenant should not have a list of appliances yet. He does.", 0,
				tenant.appliances.size());

		// Receive Message
		tenant.msgHereAreAppliances(appliances);
		for (Appliance a : tenant.appliances) {
			System.out.println("Tenant Appliance   ::   " + a.type);
		}
		// Check Post Conditions
		assertEquals(
				"Tenant should have a list of four appliances. He doesn't.", 4,
				tenant.appliances.size());

		// PickAndExecuteAnAction should return false
		assertFalse(tenant.pickAndExecuteAnAction());

		// Receive Message
		tenant.msgMorning();
		// Check Post Conditions
		assertEquals("Tenant state should be awake. It's not.", Time.awake,
				tenant.time);

		// PickAndExecuteAnAction should return false
		assertFalse(tenant.pickAndExecuteAnAction());

		// Receive Message
		tenant.msgSleeping();
		// Check Post Conditions
		assertEquals("Tenant state should be msgSleep. It's not.",
				Time.msgSleep, tenant.time);

		tenant.msgAtBed(); // release semaphore since gui is not being used
		// PickAndExecuteAnAction should return true
		assertTrue(tenant.pickAndExecuteAnAction());

		// Check Post Conditions
		assertEquals("Tenant state should be sleeping. It's not.",
				Time.sleeping, tenant.time);
		
		// PickAndExecuteAnAction should return false
		assertFalse(tenant.pickAndExecuteAnAction());
		
		// Receive Message
		tenant.msgMorning();
		// Check Post Conditions
		assertEquals("Tenant state should be awake. It's not.", Time.awake, tenant.time);
		
		tenant.myPerson.setHungerLevel(2);
		tenant.msgAtDoor(); // release semaphore since gui is not being used
		// PickAndExecuteAnAction should return true
		assertTrue(tenant.pickAndExecuteAnAction());
	}

}
