/**
 * 
 */
package restaurant.test;

import java.awt.print.Printable;

import junit.framework.TestCase;
import restaurant.CashierAgent;
import restaurant.CashierAgent.State;
import restaurant.interfaces.Cashier;
import restaurant.test.mock.MockCustomer;
import restaurant.test.mock.MockMarket;
import restaurant.test.mock.MockWaiter;

/**
 * @author Daniel
 * 
 */
public class CashierTest extends TestCase {
	// these are instantiated for each test separately via the setUp() method.
	CashierAgent cashier;

	MockCustomer customerOne;
	MockCustomer customerTwo;
	MockCustomer customerThree;
	MockCustomer customerFour;
	MockCustomer customerFive;

	MockWaiter waiterOne;
	MockWaiter waiterTwo;
	MockWaiter waiterThree;
	MockWaiter waiterFour;

	MockMarket marketOne;
	MockMarket marketTwo;

	/**
	 * This method is run before each test. You can use it to instantiate the
	 * class variables for your agent and mocks, etc.
	 */
	public void setUp() throws Exception {
		super.setUp();
		cashier = new CashierAgent("cashier");
		customerOne = new MockCustomer("mockCustomer_One");
		customerTwo = new MockCustomer("mockCustomer_Two");
		customerThree = new MockCustomer("mockCustomer_Three");
		customerFour = new MockCustomer("mockCustomer_Four");
		customerFive = new MockCustomer("mockCustomer_Five");
		waiterOne = new MockWaiter("mockWaiter_One");
		waiterTwo = new MockWaiter("mockWaiter_Two");
		waiterThree = new MockWaiter("mockWaiter_Three");
		waiterFour = new MockWaiter("mockWaiter_Four");
		marketOne = new MockMarket("mockMarket_One");
		marketTwo = new MockMarket("mockMarket_Two");
	}

	public void testOneMarket() {
		try {
			setUp();
		} catch (Exception e) {
			System.out.println("setUp failed");
			e.printStackTrace();
		}

		// Check Pre-Conditions
		assertEquals("Cashier should have 0 markets to pay. It doesn't.",
				cashier.marketsToPay.size(), 0);
		assertEquals(
				"CashierAgent should have an empty event log before the Cashier's PayMarket is called."
						+ " Instead, the Cashier's event log reads: "
						+ cashier.log.toString(), 0, cashier.log.size());
		assertEquals(
				"MockMarket should have an empty event log before the Market's sends PayMarket."
						+ " Instead, the Market's event log reads: "
						+ marketOne.log.toString(), 0, marketOne.log.size());

		// Receive Message
		cashier.msgPayMarket(marketOne, 12.0);
		// Check Post Conditions
		assertEquals("Cashier should have 1 market to pay. It doesn't.",
				cashier.marketsToPay.size(), 1);

		// Execute an Action
		cashier.pickAndExecuteAnAction();
		// Check Post Conditions
		assertEquals("Cashier should have 0 markets to pay. It doesn't.",
				cashier.marketsToPay.size(), 0);
		assertTrue(
				"Market One should have logged \"Received msgPayment from cashier."
						+ " Cashier payed $12.0\" but didn't. His log reads instead: "
						+ marketOne.log.getLastLoggedEvent().toString(),
				marketOne.log
						.containsString("Received msgPayment from cashier. Cashier payed $12.0"));
	}

	public void testTwoMarkets() {

		// Check Pre-Conditions
		assertEquals("Cashier should have 0 markets to pay. It doesn't.",
				cashier.marketsToPay.size(), 0);
		assertEquals(
				"CashierAgent should have an empty event log before the Cashier's PayMarket is called."
						+ " Instead, the Cashier's event log reads: "
						+ cashier.log.toString(), 0, cashier.log.size());
		assertEquals(
				"MockMarket One should have an empty event log before the Market sends PayMarket."
						+ " Instead, the Market's event log reads: "
						+ marketOne.log.toString(), 0, marketOne.log.size());
		assertEquals(
				"MockMarket Two should have an empty event log before the Market sends PayMarket."
						+ " Instead, the Market's event log reads: "
						+ marketTwo.log.toString(), 0, marketTwo.log.size());

		// Receive Message
		cashier.msgPayMarket(marketOne, 12.0);
		// Check Post-Condition
		assertEquals("Cashier should have 1 market to pay. It doesn't.",
				cashier.marketsToPay.size(), 1);
		// Receive Message
		cashier.msgPayMarket(marketTwo, 8.0);
		// Check Post-Condition
		assertEquals("Cashier should have 2 markets to pay. It doesn't.",
				cashier.marketsToPay.size(), 2);

		// Execute an Action
		cashier.pickAndExecuteAnAction();
		// Check Post-Conditions
		assertEquals("Cashier should have 1 markets to pay. It doesn't.",
				cashier.marketsToPay.size(), 1);
		assertTrue(
				"Market One should have logged \"Received msgPayment from cashier."
						+ " Cashier payed $12.0\" but didn't. His log reads instead: "
						+ marketOne.log.getLastLoggedEvent().toString(),
				marketOne.log
						.containsString("Received msgPayment from cashier. Cashier payed $12.0"));
		assertEquals( // this log should not have changed from before the action
				"MockMarket Two should have an empty event log before the Market sends PayMarket."
						+ " Instead, the Market's event log reads: "
						+ marketTwo.log.toString(), 0, marketTwo.log.size());

		// Execute An Action
		cashier.pickAndExecuteAnAction();
		// Check Post-Conditions
		assertEquals("Cashier should have 0 markets to pay. It doesn't.",
				cashier.marketsToPay.size(), 0);
		assertTrue(
				// this log should not have changed from before the action
				"Market One should have logged \"Received msgPayment from cashier."
						+ " Cashier payed $12.0\" but didn't. His log reads instead: "
						+ marketOne.log.getLastLoggedEvent().toString(),
				marketOne.log
						.containsString("Received msgPayment from cashier. Cashier payed $12.0"));
		assertTrue(
				"Market Two should have logged \"Received msgPayment from cashier."
						+ " Cashier payed $8.0\" but didn't. His log reads instead: "
						+ marketTwo.log.getLastLoggedEvent().toString(),
				marketTwo.log
						.containsString("Received msgPayment from cashier. Cashier payed $8.0"));
	}

	// One Customer, has enough money. One Waiter. One Market.
	public void testOne() {

		// Check Pre-Conditions
		assertEquals("Cashier should have 0 payingCustomers. It doesn't.",
				cashier.payingCustomers.size(), 0);
		assertEquals("Cashier should have 0 markets to pay. It doesn't.",
				cashier.marketsToPay.size(), 0);
		assertEquals(
				"CashierAgent should have an empty event log before the Cashier's PayMarket is called."
						+ " Instead, the Cashier's event log reads: "
						+ cashier.log.toString(), 0, cashier.log.size());

		// Receive Message
		cashier.msgGetBill(waiterOne, customerOne, "Chicken");
		// Check Post-Conditions
		assertEquals("Cashier should have 1 payingCustomer. It doesn't.",
				cashier.payingCustomers.size(), 1);
		assertEquals("Paying customer's state should be Bill. It's not ",
				cashier.payingCustomers.get(0).state, State.Bill);

		// Execute an Action
		cashier.pickAndExecuteAnAction();
		// Check Post-Conditions
		assertEquals("Paying customer's state should be none. It's not ",
				cashier.payingCustomers.get(0).state, State.none);
		assertTrue(
				"WaiterOne should have logged \"Received msgHereIsBill from cashier. "
						+ "Customer owes 11.0\" but didn't. His log reads instead: "
						+ waiterOne.log.getLastLoggedEvent().toString(),
				waiterOne.log
						.containsString("Received msgHereIsBill from cashier. Customer owes 11.0"));

		// Receive Message
		cashier.msgPayMarket(marketOne, 12.0);
		// Check Post-Conditions
		assertEquals("Cashier should have 1 market to pay. It doesn't.",
				cashier.marketsToPay.size(), 1);

		// Execute an Action
		cashier.pickAndExecuteAnAction();
		// Check Post-Conditions
		assertEquals("Cashier should have 0 markets to pay. It doesn't.",
				cashier.marketsToPay.size(), 0);
		assertTrue(
				"Market One should have logged \"Received msgPayment from cashier."
						+ " Cashier payed $12.0\" but didn't. His log reads instead: "
						+ marketOne.log.getLastLoggedEvent().toString(),
				marketOne.log
						.containsString("Received msgPayment from cashier. Cashier payed $12.0"));

		// Receive Message
		cashier.msgPayment(customerOne, 15.0);
		// Check Post-Conditions
		assertEquals("Paying customer's state should be Payment. It's not ",
				cashier.payingCustomers.get(0).state, State.Payment);

		// Execute an Action
		cashier.pickAndExecuteAnAction();
		// Check Post-Conditions
		assertEquals("Paying customer's state should be none. It's not ",
				cashier.payingCustomers.get(0).state, State.none);
		assertTrue(
				"Customer One should have logged \"Received msgHereIsYourChange from cashier."
						+ " Change = 4.0\" but didn't. His log reads instead: "
						+ customerOne.log.getLastLoggedEvent().toString(),
				customerOne.log
						.containsString("Received msgHereIsYourChange from cashier. Change = 4.0"));
	}

	// Five Customers, all have enough money. Four Waiters. One market.
	public void testTwo() {

		// Check Pre-Conditions
		assertEquals("Cashier should have 0 payingCustomers. It doesn't.",
				cashier.payingCustomers.size(), 0);
		assertEquals("Cashier should have 0 markets to pay. It doesn't.",
				cashier.marketsToPay.size(), 0);
		assertEquals(
				"CashierAgent should have an empty event log before the Cashier's PayMarket is called."
						+ " Instead, the Cashier's event log reads: "
						+ cashier.log.toString(), 0, cashier.log.size());

		// Receive Message
		cashier.msgGetBill(waiterOne, customerOne, "Chicken");
		// Check Post-Conditions
		assertEquals("Cashier should have 1 payingCustomer. It doesn't.",
				cashier.payingCustomers.size(), 1);
		assertEquals("Paying customer's state should be Bill. It's not ",
				cashier.payingCustomers.get(0).state, State.Bill);

		// Execute an Action
		cashier.pickAndExecuteAnAction();
		// Check Post-Conditions
		assertEquals("Paying customer's state should be none. It's not ",
				cashier.payingCustomers.get(0).state, State.none);
		assertTrue(
				"WaiterOne should have logged \"Received msgHereIsBill from cashier. "
						+ "Customer owes 11.0\" but didn't. His log reads instead: "
						+ waiterOne.log.getLastLoggedEvent().toString(),
				waiterOne.log
						.containsString("Received msgHereIsBill from cashier. Customer owes 11.0"));

		// Receive Message
		cashier.msgPayMarket(marketOne, 12.0);
		// Check Post-Conditions
		assertEquals("Cashier should have 1 market to pay. It doesn't.",
				cashier.marketsToPay.size(), 1);

		// Execute an Action
		cashier.pickAndExecuteAnAction();
		// Check Post-Conditions
		assertEquals("Cashier should have 0 markets to pay. It doesn't.",
				cashier.marketsToPay.size(), 0);
		assertTrue(
				"Market One should have logged \"Received msgPayment from cashier."
						+ " Cashier payed $12.0\" but didn't. His log reads instead: "
						+ marketOne.log.getLastLoggedEvent().toString(),
				marketOne.log
						.containsString("Received msgPayment from cashier. Cashier payed $12.0"));

		// Receive Message
		cashier.msgGetBill(waiterTwo, customerTwo, "Pizza");
		// Check Post-Conditions
		assertEquals("Cashier should have 2 payingCustomers. It doesn't.",
				cashier.payingCustomers.size(), 2);
		assertEquals("Paying customer's state should be Bill. It's not ",
				cashier.payingCustomers.get(1).state, State.Bill);

		// Receive Message
		cashier.msgGetBill(waiterThree, customerThree, "Steak");
		// Check Post-Conditions
		assertEquals("Cashier should have 3 payingCustomers. It doesn't.",
				cashier.payingCustomers.size(), 3);
		assertEquals("Paying customer's state should be Bill. It's not ",
				cashier.payingCustomers.get(2).state, State.Bill);

		// Receive Message
		cashier.msgGetBill(waiterFour, customerFour, "Salad");
		// Check Post-Conditions
		assertEquals("Cashier should have 4 payingCustomers. It doesn't.",
				cashier.payingCustomers.size(), 4);
		assertEquals("Paying customer's state should be Bill. It's not ",
				cashier.payingCustomers.get(3).state, State.Bill);

		// Execute an Action
		cashier.pickAndExecuteAnAction();
		// Check Post-Conditions
		assertEquals("Paying customer's state should be none. It's not ",
				cashier.payingCustomers.get(1).state, State.none);
		assertTrue(
				"WaiterTwo should have logged \"Received msgHereIsBill from cashier. "
						+ "Customer owes 9.0\" but didn't. His log reads instead: "
						+ waiterTwo.log.getLastLoggedEvent().toString(),
				waiterTwo.log
						.containsString("Received msgHereIsBill from cashier. Customer owes 9.0"));

		// Execute an Action
		cashier.pickAndExecuteAnAction();
		// Check Post-Conditions
		assertEquals("Paying customer's state should be none. It's not ",
				cashier.payingCustomers.get(2).state, State.none);
		assertTrue(
				"WaiterThree should have logged \"Received msgHereIsBill from cashier. "
						+ "Customer owes 16.0\" but didn't. His log reads instead: "
						+ waiterThree.log.getLastLoggedEvent().toString(),
				waiterThree.log
						.containsString("Received msgHereIsBill from cashier. Customer owes 16.0"));

		// Execute an Action
		cashier.pickAndExecuteAnAction();
		// Check Post-Conditions
		assertEquals("Paying customer's state should be none. It's not ",
				cashier.payingCustomers.get(3).state, State.none);
		assertTrue(
				"WaiterFour should have logged \"Received msgHereIsBill from cashier. "
						+ "Customer owes 6.0\" but didn't. His log reads instead: "
						+ waiterFour.log.getLastLoggedEvent().toString(),
				waiterFour.log
						.containsString("Received msgHereIsBill from cashier. Customer owes 6.0"));

		// Receive Message
		cashier.msgPayment(customerOne, 15.0);
		// Check Post-Conditions
		assertEquals("Paying customer's state should be Payment. It's not ",
				cashier.payingCustomers.get(0).state, State.Payment);

		// Execute an Action
		cashier.pickAndExecuteAnAction();
		// Check Post-Conditions
		assertEquals("Paying customer's state should be none. It's not ",
				cashier.payingCustomers.get(0).state, State.none);
		assertTrue(
				"Customer One should have logged \"Received msgHereIsYourChange from cashier."
						+ " Change = 4.0\" but didn't. His log reads instead: "
						+ customerOne.log.getLastLoggedEvent().toString(),
				customerOne.log
						.containsString("Received msgHereIsYourChange from cashier. Change = 4.0"));

		// Receive Message
		cashier.msgPayment(customerTwo, 15.0);
		// Check Post-Conditions
		assertEquals("Paying customer's state should be Payment. It's not ",
				cashier.payingCustomers.get(1).state, State.Payment);

		// Execute an Action
		cashier.pickAndExecuteAnAction();
		// Check Post-Conditions
		assertEquals("Paying customer's state should be none. It's not ",
				cashier.payingCustomers.get(1).state, State.none);
		assertTrue(
				"Customer Two should have logged \"Received msgHereIsYourChange from cashier."
						+ " Change = 6.0\" but didn't. His log reads instead: "
						+ customerTwo.log.getLastLoggedEvent().toString(),
				customerTwo.log
						.containsString("Received msgHereIsYourChange from cashier. Change = 6.0"));

		// Receive Message
		cashier.msgPayment(customerThree, 19.0);
		// Check Post-Conditions
		assertEquals("Paying customer's state should be Payment. It's not ",
				cashier.payingCustomers.get(2).state, State.Payment);

		// Execute an Action
		cashier.pickAndExecuteAnAction();
		// Check Post-Conditions
		assertEquals("Paying customer's state should be none. It's not ",
				cashier.payingCustomers.get(2).state, State.none);
		assertTrue(
				"Customer Three should have logged \"Received msgHereIsYourChange from cashier."
						+ " Change = 3.0\" but didn't. His log reads instead: "
						+ customerThree.log.getLastLoggedEvent().toString(),
				customerThree.log
						.containsString("Received msgHereIsYourChange from cashier. Change = 3.0"));

		// Receive Message
		cashier.msgPayment(customerFour, 12.0);
		// Check Post-Conditions
		assertEquals("Paying customer's state should be Payment. It's not ",
				cashier.payingCustomers.get(3).state, State.Payment);

		// Execute an Action
		cashier.pickAndExecuteAnAction();
		// Check Post-Conditions
		assertEquals("Paying customer's state should be none. It's not ",
				cashier.payingCustomers.get(3).state, State.none);
		assertTrue(
				"Customer Four should have logged \"Received msgHereIsYourChange from cashier."
						+ " Change = 6.0\" but didn't. His log reads instead: "
						+ customerFour.log.getLastLoggedEvent().toString(),
				customerFour.log
						.containsString("Received msgHereIsYourChange from cashier. Change = 6.0"));

		// Receive Message
		cashier.msgGetBill(waiterOne, customerFive, "Chicken");
		// Check Post-Conditions
		assertEquals("Cashier should have 5 payingCustomers. It doesn't.",
				cashier.payingCustomers.size(), 5);
		assertEquals("Paying customer's state should be Bill. It's not ",
				cashier.payingCustomers.get(4).state, State.Bill);

		// Execute an Action
		cashier.pickAndExecuteAnAction();
		// Check Post-Conditions
		assertEquals("Paying customer's state should be none. It's not ",
				cashier.payingCustomers.get(4).state, State.none);
		assertTrue(
				"WaiterOne should have logged \"Received msgHereIsBill from cashier. "
						+ "Customer owes 11.0\" but didn't. His log reads instead: "
						+ waiterOne.log.getLastLoggedEvent().toString(),
				waiterOne.log
						.containsString("Received msgHereIsBill from cashier. Customer owes 11.0"));

		// Receive Message
		cashier.msgPayment(customerFive, 12.0);
		// Check Post-Conditions
		assertEquals("Paying customer's state should be Payment. It's not ",
				cashier.payingCustomers.get(4).state, State.Payment);

		// Execute an Action
		cashier.pickAndExecuteAnAction();
		// Check Post-Conditions
		assertEquals("Paying customer's state should be none. It's not ",
				cashier.payingCustomers.get(4).state, State.none);
		assertTrue(
				"Customer Five should have logged \"Received msgHereIsYourChange from cashier."
						+ " Change = 1.0\" but didn't. His log reads instead: "
						+ customerFive.log.getLastLoggedEvent().toString(),
				customerFive.log
						.containsString("Received msgHereIsYourChange from cashier. Change = 1.0"));
	}

	// One Customer, does not have enough money. One Waiter. One market.
	public void testThree() {

		// Check Pre-Conditions
		assertEquals("Cashier should have 0 payingCustomers. It doesn't.",
				cashier.payingCustomers.size(), 0);
		assertEquals("Cashier should have 0 markets to pay. It doesn't.",
				cashier.marketsToPay.size(), 0);
		assertEquals(
				"CashierAgent should have an empty event log before the Cashier's PayMarket is called."
						+ " Instead, the Cashier's event log reads: "
						+ cashier.log.toString(), 0, cashier.log.size());

		// Receive Message
		cashier.msgGetBill(waiterOne, customerOne, "Chicken");
		// Check Post-Conditions
		assertEquals("Cashier should have 1 payingCustomer. It doesn't.",
				cashier.payingCustomers.size(), 1);
		assertEquals("Paying customer's state should be Bill. It's not ",
				cashier.payingCustomers.get(0).state, State.Bill);

		// Execute an Action
		cashier.pickAndExecuteAnAction();
		// Check Post-Conditions
		assertEquals("Paying customer's state should be none. It's not ",
				cashier.payingCustomers.get(0).state, State.none);
		assertTrue(
				"WaiterOne should have logged \"Received msgHereIsBill from cashier. "
						+ "Customer owes 11.0\" but didn't. His log reads instead: "
						+ waiterOne.log.getLastLoggedEvent().toString(),
				waiterOne.log
						.containsString("Received msgHereIsBill from cashier. Customer owes 11.0"));

		// Receive Message
		cashier.msgPayMarket(marketOne, 12.0);
		// Check Post-Conditions
		assertEquals("Cashier should have 1 market to pay. It doesn't.",
				cashier.marketsToPay.size(), 1);

		// Execute an Action
		cashier.pickAndExecuteAnAction();
		// Check Post-Conditions
		assertEquals("Cashier should have 0 markets to pay. It doesn't.",
				cashier.marketsToPay.size(), 0);
		assertTrue(
				"Market One should have logged \"Received msgPayment from cashier."
						+ " Cashier payed $12.0\" but didn't. His log reads instead: "
						+ marketOne.log.getLastLoggedEvent().toString(),
				marketOne.log
						.containsString("Received msgPayment from cashier. Cashier payed $12.0"));

		// Receive Message
		cashier.msgPayment(customerOne, 5.0);
		// Check Post-Conditions
		assertEquals("Paying customer's state should be Payment. It's not ",
				cashier.payingCustomers.get(0).state, State.Payment);

		// Execute an Action
		cashier.pickAndExecuteAnAction();
		// Check Post-Conditions
		assertEquals("Paying customer's state should be none. It's not ",
				cashier.payingCustomers.get(0).state, State.none);
		assertTrue(
				"Customer One should have logged \"Received msgInDebt from cashier. "
						+ "Debt = 6.0\" but didn't. His log reads instead: "
						+ customerOne.log.getLastLoggedEvent().toString(),
				customerOne.log
						.containsString("Received msgInDebt from cashier. Debt = 6.0"));
	}

	// Five Customer, some have enough money, one is already in debt. Four
	// Waiters. One market.
	public void testFour() {
		
		// Check Pre-Conditions
		assertEquals("Cashier should have 0 payingCustomers. It doesn't.",
				cashier.payingCustomers.size(), 0);
		assertEquals("Cashier should have 0 markets to pay. It doesn't.",
				cashier.marketsToPay.size(), 0);
		assertEquals(
				"CashierAgent should have an empty event log before the Cashier's PayMarket is called."
						+ " Instead, the Cashier's event log reads: "
						+ cashier.log.toString(), 0, cashier.log.size());

		// Receive Message
		cashier.msgGetBill(waiterOne, customerOne, "Chicken");
		// Check Post-Conditions
		assertEquals("Cashier should have 1 payingCustomer. It doesn't.",
				cashier.payingCustomers.size(), 1);
		assertEquals("Paying customer's state should be Bill. It's not ",
				cashier.payingCustomers.get(0).state, State.Bill);

		// Execute an Action
		cashier.pickAndExecuteAnAction();
		// Check Post-Conditions
		assertEquals("Paying customer's state should be none. It's not ",
				cashier.payingCustomers.get(0).state, State.none);
		assertTrue(
				"WaiterOne should have logged \"Received msgHereIsBill from cashier. "
						+ "Customer owes 11.0\" but didn't. His log reads instead: "
						+ waiterOne.log.getLastLoggedEvent().toString(),
				waiterOne.log
						.containsString("Received msgHereIsBill from cashier. Customer owes 11.0"));
		
		// Receive Message
		cashier.msgPayment(customerOne, 5.0);
		// Check Post-Conditions
		assertEquals("Paying customer's state should be Payment. It's not ",
				cashier.payingCustomers.get(0).state, State.Payment);

		// Execute an Action
		cashier.pickAndExecuteAnAction();
		// Check Post-Conditions
		assertEquals("Paying customer's state should be none. It's not ",
				cashier.payingCustomers.get(0).state, State.none);
		assertTrue(
				"Customer One should have logged \"Received msgInDebt from cashier. "
						+ "Debt = 6.0\" but didn't. His log reads instead: "
						+ customerOne.log.getLastLoggedEvent().toString(),
				customerOne.log
						.containsString("Received msgInDebt from cashier. Debt = 6.0"));
		
		
		// Now the first customer is in debt
		
		
		// Check Pre-Conditions
		assertEquals("Cashier should have 1 payingCustomer. It doesn't.",
				cashier.payingCustomers.size(), 1);
		assertEquals("Cashier should have 0 markets to pay. It doesn't.",
				cashier.marketsToPay.size(), 0);
		assertEquals(
				"CashierAgent should have an empty event log before the Cashier's PayMarket is called."
						+ " Instead, the Cashier's event log reads: "
						+ cashier.log.toString(), 0, cashier.log.size());

		// Receive Message
		cashier.msgGetBill(waiterOne, customerOne, "Chicken");
		// Check Post-Conditions
		assertEquals("Cashier should have 1 payingCustomer. It doesn't.",
				cashier.payingCustomers.size(), 1);
		assertEquals("Paying customer's state should be Bill. It's not ",
				cashier.payingCustomers.get(0).state, State.Bill);

		// Execute an Action
		cashier.pickAndExecuteAnAction();
		// Check Post-Conditions
		assertEquals("Paying customer's state should be none. It's not ",
				cashier.payingCustomers.get(0).state, State.none);
		assertTrue(
				"WaiterOne should have logged \"Received msgHereIsBill from cashier. "
						+ "Customer owes 17.0\" but didn't. His log reads instead: "
						+ waiterOne.log.getLastLoggedEvent().toString(),
				waiterOne.log
						.containsString("Received msgHereIsBill from cashier. Customer owes 17.0"));

		// Receive Message
		cashier.msgPayMarket(marketOne, 12.0);
		// Check Post-Conditions
		assertEquals("Cashier should have 1 market to pay. It doesn't.",
				cashier.marketsToPay.size(), 1);

		// Execute an Action
		cashier.pickAndExecuteAnAction();
		// Check Post-Conditions
		assertEquals("Cashier should have 0 markets to pay. It doesn't.",
				cashier.marketsToPay.size(), 0);
		assertTrue(
				"Market One should have logged \"Received msgPayment from cashier."
						+ " Cashier payed $12.0\" but didn't. His log reads instead: "
						+ marketOne.log.getLastLoggedEvent().toString(),
				marketOne.log
						.containsString("Received msgPayment from cashier. Cashier payed $12.0"));

		// Receive Message
		cashier.msgGetBill(waiterTwo, customerTwo, "Pizza");
		// Check Post-Conditions
		assertEquals("Cashier should have 2 payingCustomers. It doesn't.",
				cashier.payingCustomers.size(), 2);
		assertEquals("Paying customer's state should be Bill. It's not ",
				cashier.payingCustomers.get(1).state, State.Bill);

		// Receive Message
		cashier.msgGetBill(waiterThree, customerThree, "Steak");
		// Check Post-Conditions
		assertEquals("Cashier should have 3 payingCustomers. It doesn't.",
				cashier.payingCustomers.size(), 3);
		assertEquals("Paying customer's state should be Bill. It's not ",
				cashier.payingCustomers.get(2).state, State.Bill);

		// Receive Message
		cashier.msgGetBill(waiterFour, customerFour, "Salad");
		// Check Post-Conditions
		assertEquals("Cashier should have 4 payingCustomers. It doesn't.",
				cashier.payingCustomers.size(), 4);
		assertEquals("Paying customer's state should be Bill. It's not ",
				cashier.payingCustomers.get(3).state, State.Bill);

		// Execute an Action
		cashier.pickAndExecuteAnAction();
		// Check Post-Conditions
		assertEquals("Paying customer's state should be none. It's not ",
				cashier.payingCustomers.get(1).state, State.none);
		assertTrue(
				"WaiterTwo should have logged \"Received msgHereIsBill from cashier. "
						+ "Customer owes 9.0\" but didn't. His log reads instead: "
						+ waiterTwo.log.getLastLoggedEvent().toString(),
				waiterTwo.log
						.containsString("Received msgHereIsBill from cashier. Customer owes 9.0"));

		// Execute an Action
		cashier.pickAndExecuteAnAction();
		// Check Post-Conditions
		assertEquals("Paying customer's state should be none. It's not ",
				cashier.payingCustomers.get(2).state, State.none);
		assertTrue(
				"WaiterThree should have logged \"Received msgHereIsBill from cashier. "
						+ "Customer owes 16.0\" but didn't. His log reads instead: "
						+ waiterThree.log.getLastLoggedEvent().toString(),
				waiterThree.log
						.containsString("Received msgHereIsBill from cashier. Customer owes 16.0"));

		// Execute an Action
		cashier.pickAndExecuteAnAction();
		// Check Post-Conditions
		assertEquals("Paying customer's state should be none. It's not ",
				cashier.payingCustomers.get(3).state, State.none);
		assertTrue(
				"WaiterFour should have logged \"Received msgHereIsBill from cashier. "
						+ "Customer owes 6.0\" but didn't. His log reads instead: "
						+ waiterFour.log.getLastLoggedEvent().toString(),
				waiterFour.log
						.containsString("Received msgHereIsBill from cashier. Customer owes 6.0"));

		// Receive Message
		cashier.msgPayment(customerOne, 21.0);
		// Check Post-Conditions
		assertEquals("Paying customer's state should be Payment. It's not ",
				cashier.payingCustomers.get(0).state, State.Payment);

		// Execute an Action
		cashier.pickAndExecuteAnAction();
		// Check Post-Conditions
		assertEquals("Paying customer's state should be none. It's not ",
				cashier.payingCustomers.get(0).state, State.none);
		assertTrue(
				"Customer One should have logged \"Received msgHereIsYourChange from cashier."
						+ " Change = 0.0\" but didn't. His log reads instead: "
						+ customerOne.log.getLastLoggedEvent().toString(),
				customerOne.log
						.containsString("Received msgHereIsYourChange from cashier. Change = 0.0"));

		// Receive Message
		cashier.msgPayment(customerTwo, 15.0);
		// Check Post-Conditions
		assertEquals("Paying customer's state should be Payment. It's not ",
				cashier.payingCustomers.get(1).state, State.Payment);

		// Execute an Action
		cashier.pickAndExecuteAnAction();
		// Check Post-Conditions
		assertEquals("Paying customer's state should be none. It's not ",
				cashier.payingCustomers.get(1).state, State.none);
		assertTrue(
				"Customer Two should have logged \"Received msgHereIsYourChange from cashier."
						+ " Change = 6.0\" but didn't. His log reads instead: "
						+ customerTwo.log.getLastLoggedEvent().toString(),
				customerTwo.log
						.containsString("Received msgHereIsYourChange from cashier. Change = 6.0"));

		// Receive Message
		cashier.msgPayment(customerThree, 10.0);
		// Check Post-Conditions
		assertEquals("Paying customer's state should be Payment. It's not ",
				cashier.payingCustomers.get(2).state, State.Payment);

		// Execute an Action
		cashier.pickAndExecuteAnAction();
		// Check Post-Conditions
		assertEquals("Paying customer's state should be none. It's not ",
				cashier.payingCustomers.get(2).state, State.none);
		assertTrue(/////////////////////////FIXXXXXXX                          FIX   Fix
				"Customer Three should have logged \"Received msgInDebt from cashier. "
						+ "Debt = 6.0\" but didn't. His log reads instead: "
						+ customerThree.log.getLastLoggedEvent().toString(),
				customerThree.log
						.containsString("Received msgInDebt from cashier. Debt = 6.0"));

		// Receive Message
		cashier.msgPayment(customerFour, 12.0);
		// Check Post-Conditions
		assertEquals("Paying customer's state should be Payment. It's not ",
				cashier.payingCustomers.get(3).state, State.Payment);

		// Execute an Action
		cashier.pickAndExecuteAnAction();
		// Check Post-Conditions
		assertEquals("Paying customer's state should be none. It's not ",
				cashier.payingCustomers.get(3).state, State.none);
		assertTrue(
				"Customer Four should have logged \"Received msgHereIsYourChange from cashier."
						+ " Change = 6.0\" but didn't. His log reads instead: "
						+ customerFour.log.getLastLoggedEvent().toString(),
				customerFour.log
						.containsString("Received msgHereIsYourChange from cashier. Change = 6.0"));

		// Receive Message
		cashier.msgGetBill(waiterOne, customerFive, "Chicken");
		// Check Post-Conditions
		assertEquals("Cashier should have 5 payingCustomers. It doesn't.",
				cashier.payingCustomers.size(), 5);
		assertEquals("Paying customer's state should be Bill. It's not ",
				cashier.payingCustomers.get(4).state, State.Bill);

		// Execute an Action
		cashier.pickAndExecuteAnAction();
		// Check Post-Conditions
		assertEquals("Paying customer's state should be none. It's not ",
				cashier.payingCustomers.get(4).state, State.none);
		assertTrue(
				"WaiterOne should have logged \"Received msgHereIsBill from cashier. "
						+ "Customer owes 11.0\" but didn't. His log reads instead: "
						+ waiterOne.log.getLastLoggedEvent().toString(),
				waiterOne.log
						.containsString("Received msgHereIsBill from cashier. Customer owes 11.0"));

		// Receive Message
		cashier.msgPayment(customerFive, 12.0);
		// Check Post-Conditions
		assertEquals("Paying customer's state should be Payment. It's not ",
				cashier.payingCustomers.get(4).state, State.Payment);

		// Execute an Action
		cashier.pickAndExecuteAnAction();
		// Check Post-Conditions
		assertEquals("Paying customer's state should be none. It's not ",
				cashier.payingCustomers.get(4).state, State.none);
		assertTrue(
				"Customer Five should have logged \"Received msgHereIsYourChange from cashier."
						+ " Change = 1.0\" but didn't. His log reads instead: "
						+ customerFive.log.getLastLoggedEvent().toString(),
				customerFive.log
						.containsString("Received msgHereIsYourChange from cashier. Change = 1.0"));
	}

	/*
	 * LAB 9 TEST public void testScenario() {
	 * assertEquals("Cashier should have 0 payingCustomers. It doesn't.",
	 * cashier.payingCustomers.size(), 0); cashier.msgGetBill(waiter, customer,
	 * "Chicken");
	 * assertEquals("Cashier should have 1 payingCustomer. It doesn't",
	 * cashier.payingCustomers.size(), 1); cashier.pickAndExecuteAnAction();
	 * assertTrue("Customer state should be none. It isn't.",
	 * cashier.payingCustomers.get(0).state == State.none); }
	 */
	/**
	 * This tests the cashier under very simple terms: one customer is ready to
	 * pay the exact bill.
	 */
	/*
	 * public void testOneNormalCustomerScenario() { //setUp() runs first before
	 * this test!
	 * 
	 * customer.cashier = cashier;//You can do almost anything in a unit test.
	 * 
	 * //check preconditions
	 * assertEquals("Cashier should have 0 bills in it. It doesn't."
	 * ,cashier.bills.size(), 0); assertEquals(
	 * "CashierAgent should have an empty event log before the Cashier's HereIsBill is called. Instead, the Cashier's event log reads: "
	 * + cashier.log.toString(), 0, cashier.log.size());
	 * 
	 * //step 1 of the test //public Bill(Cashier, Customer, int tableNum,
	 * double price) { Bill bill = new Bill(cashier, customer, 2, 7.98);
	 * cashier.HereIsBill(bill);//send the message from a waiter
	 * 
	 * //check postconditions for step 1 and preconditions for step 2
	 * assertEquals(
	 * "MockWaiter should have an empty event log before the Cashier's scheduler is called. Instead, the MockWaiter's event log reads: "
	 * + waiter.log.toString(), 0, waiter.log.size());
	 * 
	 * assertEquals("Cashier should have 1 bill in it. It doesn't.",
	 * cashier.bills.size(), 1);
	 * 
	 * assertFalse(
	 * "Cashier's scheduler should have returned false (no actions to do on a bill from a waiter), but didn't."
	 * , cashier.pickAndExecuteAnAction());
	 * 
	 * assertEquals(
	 * "MockWaiter should have an empty event log after the Cashier's scheduler is called for the first time. Instead, the MockWaiter's event log reads: "
	 * + waiter.log.toString(), 0, waiter.log.size());
	 * 
	 * assertEquals(
	 * "MockCustomer should have an empty event log after the Cashier's scheduler is called for the first time. Instead, the MockCustomer's event log reads: "
	 * + waiter.log.toString(), 0, waiter.log.size());
	 * 
	 * //step 2 of the test cashier.ReadyToPay(customer, bill);
	 * 
	 * //check postconditions for step 2 / preconditions for step 3 assertTrue(
	 * "CashierBill should contain a bill with state == customerApproached. It doesn't."
	 * , cashier.bills.get(0).state == cashierBillState.customerApproached);
	 * 
	 * assertTrue(
	 * "Cashier should have logged \"Received ReadyToPay\" but didn't. His log reads instead: "
	 * + cashier.log.getLastLoggedEvent().toString(),
	 * cashier.log.containsString("Received ReadyToPay"));
	 * 
	 * assertTrue(
	 * "CashierBill should contain a bill of price = $7.98. It contains something else instead: $"
	 * + cashier.bills.get(0).bill.netCost, cashier.bills.get(0).bill.netCost ==
	 * 7.98);
	 * 
	 * assertTrue(
	 * "CashierBill should contain a bill with the right customer in it. It doesn't."
	 * , cashier.bills.get(0).bill.customer == customer);
	 * 
	 * 
	 * //step 3 //NOTE: I called the scheduler in the assertTrue statement below
	 * (to succintly check the return value at the same time) assertTrue(
	 * "Cashier's scheduler should have returned true (needs to react to customer's ReadyToPay), but didn't."
	 * , cashier.pickAndExecuteAnAction());
	 * 
	 * //check postconditions for step 3 / preconditions for step 4 assertTrue(
	 * "MockCustomer should have logged an event for receiving \"HereIsYourTotal\" with the correct balance, but his last event logged reads instead: "
	 * + customer.log.getLastLoggedEvent().toString(),
	 * customer.log.containsString
	 * ("Received HereIsYourTotal from cashier. Total = 7.98"));
	 * 
	 * 
	 * assertTrue(
	 * "Cashier should have logged \"Received HereIsMyPayment\" but didn't. His log reads instead: "
	 * + cashier.log.getLastLoggedEvent().toString(),
	 * cashier.log.containsString("Received HereIsMyPayment"));
	 * 
	 * 
	 * assertTrue(
	 * "CashierBill should contain changeDue == 0.0. It contains something else instead: $"
	 * + cashier.bills.get(0).changeDue, cashier.bills.get(0).changeDue == 0);
	 * 
	 * 
	 * 
	 * //step 4 assertTrue(
	 * "Cashier's scheduler should have returned true (needs to react to customer's ReadyToPay), but didn't."
	 * , cashier.pickAndExecuteAnAction());
	 * 
	 * //check postconditions for step 4 assertTrue(
	 * "MockCustomer should have logged an event for receiving \"HereIsYourChange\" with the correct change, but his last event logged reads instead: "
	 * + customer.log.getLastLoggedEvent().toString(),
	 * customer.log.containsString
	 * ("Received HereIsYourChange from cashier. Change = 0.0"));
	 * 
	 * 
	 * assertTrue(
	 * "CashierBill should contain a bill with state == done. It doesn't.",
	 * cashier.bills.get(0).state == cashierBillState.done);
	 * 
	 * assertFalse(
	 * "Cashier's scheduler should have returned false (no actions left to do), but didn't."
	 * , cashier.pickAndExecuteAnAction());
	 * 
	 * 
	 * }//end one normal customer scenario
	 */

}
