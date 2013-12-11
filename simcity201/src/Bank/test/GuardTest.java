package Bank.test;
import java.util.*;

import Bank.bankGuardRole;
import junit.framework.TestCase;
import Bank.test.mock.*;

/**
 * 
 * @author Eric
 *
 */
public class GuardTest extends TestCase{
		bankGuardRole guard;
		MockTeller teller;
		MockCustomer cust1;
		MockBankPerson guardPerson;
		int accNum;
		List<String>goodInv = new ArrayList<String>();
		List<String>badInv = new ArrayList<String>();
	
	public void setUp() throws Exception {
		//super.setUp();
		guard = new bankGuardRole();
		teller = new MockTeller();
		cust1 = new MockCustomer();
		guardPerson = new MockBankPerson("Bank.bankGuardRole");
		guard.myPerson = guardPerson;
	}
	public void testOne() {
		try {
			setUp();
		} catch (Exception e) {
			System.out.println("setUp failed");
			e.printStackTrace();
		}
		//Check preConditions
		assertEquals("The guard should have an empty list of entering customers but doesn't",0,guard.custEnter.size());
		assertEquals("Guard should have a list of 3 bad objects but doesn't", 3, guard.badObjs.size());
		//receive message
		guard.wantEnter(cust1);
		// PickAndExecuteAnAction should return true
		assertTrue(guard.pickAndExecuteAnAction());
		//check postConditions
		assertTrue("Customer log should read: Guard has requested search but instead it is"+cust1.log.getLastLoggedEvent().toString(),
				cust1.log.containsString("Guard has requested search"));
		//receive message
		guard.allowSearch(cust1, goodInv);
		assertEquals("Guard should receive customer Inv of size 0 but doesn't",0,goodInv.size());
	}
	
	public void testTwo() {
		try {
			setUp();
			goodInv.add("cheese");	
			
		} catch (Exception e) {
			System.out.println("setUp failed");
			e.printStackTrace();
		}
			//Check preConditions
			assertEquals("The guard should have an empty list of entering customers but doesn't",0,guard.custEnter.size());
			assertEquals("Guard should have a list of 3 bad objects but doesn't", 3, guard.badObjs.size());
			//receive message
			guard.wantEnter(cust1);
			// PickAndExecuteAnAction should return true
			assertTrue(guard.pickAndExecuteAnAction());
			//check postConditions
			assertTrue("Customer log should read: Guard has requested search but instead it is"+cust1.log.getLastLoggedEvent().toString(),
			cust1.log.containsString("Guard has requested search"));
			//receive message
			guard.allowSearch(cust1, goodInv);
			assertEquals("Guard should receive customer Inv of size 1 but doesn't",1,goodInv.size());
	}
	
	public void testThree() {
		try {
			setUp();
			badInv.add("gun");
			badInv.add("knife");
			badInv.add("ski mask");
		} catch (Exception e) {
			System.out.println("setUp failed");
			e.printStackTrace();
		}
			//Check preConditions
			assertEquals("The guard should have an empty list of entering customers but doesn't",0,guard.custEnter.size());
			assertEquals("Guard should have a list of 3 bad objects but doesn't", 3, guard.badObjs.size());
			//receive message
			guard.wantEnter(cust1);
			// PickAndExecuteAnAction should return true
			assertTrue(guard.pickAndExecuteAnAction());
			//check postConditions
			assertTrue("Customer log should read: Guard has requested search but instead it is"+cust1.log.getLastLoggedEvent().toString(),
			cust1.log.containsString("Guard has requested search"));
			//receive message
			guard.allowSearch(cust1, goodInv);
			assertEquals("Guard should receive customer Inv of size 2 but doesn't",3,badInv.size());
	}
}
