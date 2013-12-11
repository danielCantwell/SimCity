package Bank.test;

import SimCity.Globals.Money;
import Bank.tellerRole;
import junit.framework.TestCase;
import Bank.test.mock.*;

/**
 * 
 * @author Eric
 *
 */

public class TellerTest extends TestCase {
	tellerRole teller;
	MockCustomer cust1;
	MockBankPerson tellerPerson;
	int accNum;
	Money money, editmoney;

	public void setUp() throws Exception {
		super.setUp();
		accNum = 1;
		money = new Money(35,0);
		editmoney = new Money(10,0);
		teller = new tellerRole();
		cust1 = new MockCustomer();
		tellerPerson = new MockBankPerson("Bank.tellerRole");
		teller.myPerson = tellerPerson;
	}

	public void testOne() { //Account creation and deposit
		try {
			setUp();
		} catch (Exception e) {
			System.out.println("setUp failed");
			e.printStackTrace();
		}
		//Check preConditions
		assertEquals("Teller should have an empty list of clients but it doesn't",0,teller.clients.size());
		assertTrue("Teller should have an empty map of accounts but it doesn't",teller.bankAccs.isEmpty());
		//Receive message
		teller.tellerAssigned(cust1, accNum);
		//check postConditions
		assertEquals("Teller should have an list of clients size 1 but it doesn't",1,teller.clients.size());
		// PickAndExecuteAnAction should return true
		assertTrue(teller.pickAndExecuteAnAction());
		assertTrue("cust1 should have logged \"Teller called for me but instead his log reads"+cust1.log.getLastLoggedEvent().toString(),
				cust1.log.containsString("Teller called for me"));
		//Receive message
		teller.foundTeller( money, cust1);
		// PickAndExecuteAnAction should return true
		assertTrue(teller.pickAndExecuteAnAction());
		//check postConditions, accounts created successfully
		assertEquals("Teller should have a bankAcc map with 1 entry but doesn't",1,teller.bankAccs.size());
		assertEquals("Teller should have account number 1 with balance of $0 but doesn't",500,teller.bankAccs.get(1).getDollar());
		//Receive message
		teller.requestDeposit(accNum, editmoney);
		// PickAndExecuteAnAction should return true
//		assertTrue(teller.pickAndExecuteAnAction());
//		assertEquals("Account 1 should have 10 dollars in it but it doesn't", 10, teller.bankAccs.get(1).getDollar());
	}
	public void testTwo() {	//Account creation and withdraw
		try {
			setUp();
		} catch (Exception e) {
			System.out.println("setUp failed");
			e.printStackTrace();
		}
		//Check preConditions
		assertEquals("Teller should have an empty list of clients but it doesn't",0,teller.clients.size());
		assertTrue("Teller should have an empty map of accounts but it doesn't",teller.bankAccs.isEmpty());
		//Receive message
		teller.tellerAssigned(cust1, accNum);
		//check postConditions
		assertEquals("Teller should have an list of clients size 1 but it doesn't",1,teller.clients.size());
		// PickAndExecuteAnAction should return true
		assertTrue(teller.pickAndExecuteAnAction());
		assertTrue("cust1 should have logged \"Teller called for me but instead his log reads"+cust1.log.getLastLoggedEvent().toString(),
				cust1.log.containsString("Teller called for me"));
		//Receive message
		teller.foundTeller( money, cust1);
		// PickAndExecuteAnAction should return true
		assertTrue(teller.pickAndExecuteAnAction());
		//check postConditions, accounts created successfully
		assertEquals("Teller should have a bankAcc map with 1 entry but doesn't",1,teller.bankAccs.size());
		assertEquals("Teller should have account number 1 with balance of $0 but doesn't",500,teller.bankAccs.get(1).getDollar());
		//Receive message
		teller.requestWithdraw(accNum, editmoney);
		// PickAndExecuteAnAction should return true
		//assertTrue(teller.pickAndExecuteAnAction());
	}
}
