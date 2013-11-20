/**
 * 
 */
package housing.test;

import housing.roles.OwnerRole;
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
	
	/**
	 * This method is run before each test. You can use it to instantiate the
	 * class variables for your agent and mocks, etc.
	 */
	public void setUp() throws Exception {
		super.setUp();
		owner = new OwnerRole();
		mtOne = new MockTenant();
		mtTwo = new MockTenant();
	}
	
	public void testOneTenant() {
		try {
			setUp();
		} catch (Exception e) {
			System.out.println("setUp failed");
			e.printStackTrace();
		}
		
		// TODO tests
	}

}
