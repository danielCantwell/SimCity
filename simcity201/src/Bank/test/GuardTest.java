package Bank.test;

import SimCity.Globals.Money;
import Bank.bankGuardRole;
import Bank.tellerRole;
import junit.framework.TestCase;
import Bank.test.mock.*;

/**
 * 
 * @author Eric
 *
 */
public class GuardTest {
	public class TellerTest extends TestCase {
		bankGuardRole guard;
		MockTeller teller;
		MockCustomer cust1;
		MockBankPerson guardPerson;
		int accNum;

		public void setUp() throws Exception {
			super.setUp();
			accNum = 1;
			guard = new bankGuardRole();
			teller = new MockTeller();
			cust1 = new MockCustomer();
			guardPerson = new MockBankPerson("Bank.bankGuardRole");
			guard.myPerson = guardPerson;
		}
	}
}
