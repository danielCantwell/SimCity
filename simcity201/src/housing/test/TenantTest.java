/**
 * 
 */
package housing.test;

import housing.roles.TenantRole;
import housing.test.mock.MockOwner;
import junit.framework.TestCase;

/**
 * @author Daniel
 *
 */
public class TenantTest extends TestCase {
	
	TenantRole tenant;
	
	MockOwner mockOwner;
	
	/**
	 * This method is run before each test. You can use it to instantiate the
	 * class variables for your agent and mocks, etc.
	 */
	public void setUp() throws Exception {
		super.setUp();
		tenant = new TenantRole();
		mockOwner = new MockOwner();
	}
	
	public void testOne() {
		try {
			setUp();
		} catch (Exception e) {
			System.out.println("setUp failed");
			e.printStackTrace();
		}
		
		// TODO tests
	}

}
