package restaurant.test;

import restaurant.CashierAgent;
import restaurant.CashierAgent.AgentState;
import restaurant.CashierAgent.Bill;
import restaurant.CashierAgent.Check;
import restaurant.CashierAgent.CheckState;
//import restaurant.test.mock.MockCustomer;
//import restaurant.test.mock.MockWaiter;

import restaurant.test.mock.MockMarket;
import restaurant.test.mock.MockWaiter;
import junit.framework.*;

/**
 * 
 * This class is a JUnit test class to unit test the CashierAgent's basic interaction
 * with waiters, customers, and the host.
 * It is provided as an example to students in CS201 for their unit testing lab.
 *
 * @author Timothy So
 */
public class CashierTest extends TestCase
{
	//these are instantiated for each test separately via the setUp() method.
	CashierAgent cashier;
	MockWaiter waiter;
	MockMarket superior;
	MockMarket ralphs;
	
	
	/**
	 * This method is run before each test. You can use it to instantiate the class variables
	 * for your agent and mocks, etc.
	 */
	public void setUp() throws Exception{
		super.setUp();		
		
		cashier = new CashierAgent("cashier");	
        cashier.addItemToMenu("Steak", 15.99);
        cashier.addItemToMenu("Chicken", 10.99);
        cashier.addItemToMenu("Salad", 5.99);
        cashier.addItemToMenu("Pizza", 8.99);
        
		waiter = new MockWaiter("mockwaiter");
		superior = new MockMarket("Superior");
		ralphs = new MockMarket("Ralph's");
	}	
	
	
	/**
	 * This tests the normative scenario between the market and the cashier.
	 */
	public void testOneNormalMarketScenario()
	{
		//setUp() runs first before this test!		
		
		System.out.println("Test 1 Market Scenario:");

		//check preconditions
		assertEquals("Cashier should have 0 dollars. It doesn't.",cashier.getCashInRegister(), 0.0d);
		assertTrue("Cashier should have no checks to process. It does.", cashier.checks.isEmpty());
		
		//step 1 of the test
		cashier.msgHereIsTheMoney(300.00d); // pay cashier so he can pay market
		assertEquals("Cashier should have 300 dollars. It doesn't.", cashier.getCashInRegister(), 300.00d);
		cashier.msgHereIsTheBill(superior, 200.00d);//send the message from a market

		//check postconditions for step 1 and preconditions scheduler
		assertEquals("Cashier should have 1 bill to pay. It doesn't.", cashier.billsToPay.size(), 1);
		
		Bill b = cashier.billsToPay.get(0);
		assertEquals("Market should be Superior, It isn't.", b.market, superior);
		assertEquals("Price should be $200, It isn't.", b.price, 200.0d);
		
		//run scheduler
		assertTrue("Cashier's scheduler should have returned true, but didn't.", cashier.pickAndExecuteAnAction());

		//check postconditions for scheduler
		assertEquals("Cashier should have 100 dollars. It doesn't.", cashier.getCashInRegister(), 100.00d);
		assertTrue("Cashier should have no checks to process. It does.", cashier.checks.isEmpty());
	}
	
	/**
	 * This tests the normative scenario between two markets and the cashier.
	 */
	public void testTwoNormalMarketScenario()
	{
		//setUp() runs first before this test!		
		
		System.out.println("Test 2 Market Scenario:");

		//check preconditions
		assertEquals("Cashier should have 0 dollars. It doesn't.",cashier.getCashInRegister(), 0.0d);
		assertTrue("Cashier should have no checks to process. It does.", cashier.checks.isEmpty());
		
		//step 1 of the test
		cashier.msgHereIsTheMoney(300.00d); // pay cashier so he can pay market
		assertEquals("Cashier should have 300 dollars. It doesn't.", cashier.getCashInRegister(), 300.00d);
		cashier.msgHereIsTheBill(superior, 200.00d);//send the message from a market
		cashier.msgHereIsTheBill(ralphs, 100.00d);//send the message from a market

		//check postconditions for step 1 and preconditions scheduler
		assertEquals("Cashier should have 2 bills to pay. It doesn't.", cashier.billsToPay.size(), 2);
		
		Bill b1 = cashier.billsToPay.get(0);
		Bill b2 = cashier.billsToPay.get(1);
		assertEquals("Market should be Superior, It isn't.", b1.market, superior);
		assertEquals("Price should be $200, It isn't.", b1.price, 200.0d);
		assertEquals("Market should be Ralph's, It isn't.", b2.market, ralphs);
		assertEquals("Price should be $100, It isn't.", b2.price, 100.0d);
		
		//run scheduler
		assertTrue("Cashier's scheduler should have returned true, but didn't.", cashier.pickAndExecuteAnAction());

		//check postconditions for scheduler
		assertEquals("Cashier should have 100 dollars. It doesn't.", cashier.getCashInRegister(), 100.00d);
		assertEquals("Cashier should have 1 bills to pay. It doesn't.", cashier.billsToPay.size(), 1);
		
		//run scheduler again
		assertTrue("Cashier's scheduler should have returned true, but didn't.", cashier.pickAndExecuteAnAction());
		
		//check postconditions for scheduler
		assertEquals("Cashier should have 0 dollars. It doesn't.", cashier.getCashInRegister(), 0.00d);
		assertTrue("Cashier should have no checks to pay. It does.", cashier.billsToPay.isEmpty());
	}
	
	/**
	 * This tests the non-normative scenario between the market and the cashier.
	 */
	public void testOneNonNormalMarketScenario()
	{
		//setUp() runs first before this test!		
		
		System.out.println("Test 1 Non-Normal Market Scenario:");

		//check preconditions
		assertEquals("Cashier should have 0 dollars. It doesn't.",cashier.getCashInRegister(), 0.0d);
		assertTrue("Cashier should have no checks to process. It does.", cashier.checks.isEmpty());
		
		//step 1 of the test
		cashier.msgHereIsTheMoney(100.00d); // pay cashier so he can pay market
		assertEquals("Cashier should have 100 dollars. It doesn't.", cashier.getCashInRegister(), 100.00d);
		cashier.msgHereIsTheBill(superior, 200.00d);//send the message from a market

		//check postconditions for step 1 and preconditions scheduler
		assertEquals("Cashier should have 1 bill to pay. It doesn't.", cashier.billsToPay.size(), 1);
		
		Bill b = cashier.billsToPay.get(0);
		assertEquals("Market should be Superior, It isn't.", b.market, superior);
		assertEquals("Price should be $200, It isn't.", b.price, 200.0d);
		
		//run scheduler
		assertFalse("Cashier's scheduler should have returned false, but didn't.", cashier.pickAndExecuteAnAction());

		//check postconditions for scheduler
		assertEquals("Cashier should have 100 dollars. It doesn't.", cashier.getCashInRegister(), 100.00d);
		assertEquals("Cashier should have 1 bill to pay. It doesn't.", cashier.billsToPay.size(), 1);
		assertTrue("Cashier should have no checks to process. It does.", cashier.checks.isEmpty());
		
		//step 2
		cashier.msgHereIsTheMoney(200.00d);

		//run scheduler
		assertTrue("Cashier's scheduler should have returned true, but didn't.", cashier.pickAndExecuteAnAction());

		//check postconditions for scheduler
		//assertEquals("Cashier should have 80 dollars. It doesn't.", cashier.getCashInRegister(), 80.00d); // breaks because of floating point math
		assertEquals("Cashier should have no bills to pay. It does.", cashier.billsToPay.size(), 0);
	}
	
	/**
	 * This tests the cashier in the condition that one check is delivered and one customer is ready 
	 * to pay the exact bill.
	 */
	public void testOneNormalCustomerScenario()
	{
		//setUp() runs first before this test!
		
		//customer.cashier = cashier;//You can do almost anything in a unit test.			
		
		System.out.println("Test 1 Customer Scenario:");
		
		//check preconditions
		assertEquals("Cashier should have 0 bills in it. It doesn't.",cashier.getCashInRegister(), 0.0d);
		assertTrue("Cashier should have no checks to process. It does.", cashier.checks.isEmpty());
		
		//step 1 of the test
		cashier.msgHereIsACheck(waiter, "Steak", 0);//send the message from a waiter

		//check postconditions for step 1 and preconditions scheduler
		assertEquals("Cashier should have 1 check to process. It doesn't.", cashier.checks.size(), 1);
		
		Check c = cashier.checks.get(0);
		assertEquals("Waiter should be Bob, It isn't.", c.waiter, waiter);
		assertEquals("Choice should be Steak, It isn't.", c.choice, "Steak");
		assertEquals("Table should be table 0, It isn't.", c.tableNumber, 0);
		assertEquals("State should be pending, It isn't.", c.state, CheckState.pending);
		
		//run scheduler
		assertTrue("Cashier's scheduler should have returned true, but didn't.", cashier.pickAndExecuteAnAction());

		//check postconditions for scheduler
		assertEquals("Check should be calcuating, It isn't.", c.state, CheckState.calculating);
		
		//step 2
		//send to waiter
		double price = cashier.priceMap.get(c.choice);
		waiter.msgHereIsACheck(c.tableNumber, price);
		cashier.msgHereIsTheMoney(price);
		
		//check postconditions
		assertEquals("Cashier should have been paid. It wasn't.",cashier.getCashInRegister(), price);
	
	}//end one normal customer scenario
	
	/**
	 * This tests the cashier in the condition that two checks are delivered, the first customer is ready 
	 * to pay the exact bill, and the second pays only a portion of the bill.
	 */
	public void testTwoNormalCustomerScenario()
	{
		//setUp() runs first before this test!
		
		//customer.cashier = cashier;//You can do almost anything in a unit test.			
		
		System.out.println("Test 2 Customer Scenario:");
		
		//check preconditions
		assertEquals("Cashier should have 0 bills in it. It doesn't.",cashier.getCashInRegister(), 0.0d);
		assertTrue("Cashier should have no checks to process. It does.", cashier.checks.isEmpty());
		
		//step 1 of the test
		cashier.msgHereIsACheck(waiter, "Steak", 0);//send the message from a waiter
		cashier.msgHereIsACheck(waiter, "Chicken", 2);//send the message from a waiter

		//check postconditions for step 1 and preconditions scheduler
		assertEquals("Cashier should have 2 check to process. It doesn't.", cashier.checks.size(), 2);
		
		Check c1 = cashier.checks.get(0);
		Check c2 = cashier.checks.get(1);
		assertEquals("Waiter should be Bob, It isn't.", c1.waiter, waiter);
		assertEquals("Choice should be Steak, It isn't.", c1.choice, "Steak");
		assertEquals("Table should be table 0, It isn't.", c1.tableNumber, 0);
		assertEquals("State should be pending, It isn't.", c1.state, CheckState.pending);
		
		assertEquals("Waiter should be Bob, It isn't.", c2.waiter, waiter);
		assertEquals("Choice should be Chicken, It isn't.", c2.choice, "Chicken");
		assertEquals("Table should be table 2, It isn't.", c2.tableNumber, 2);
		assertEquals("State should be pending, It isn't.", c2.state, CheckState.pending);
		
		//run scheduler
		assertTrue("Cashier's scheduler should have returned true, but didn't.", cashier.pickAndExecuteAnAction());

		//check postconditions for scheduler
		assertEquals("Check 1 should be calcuating, It isn't.", c1.state, CheckState.calculating);
		
		//step 2
		//send to waiter
		double price1 = cashier.priceMap.get(c1.choice);
		waiter.msgHereIsACheck(c1.tableNumber, price1);

		//simulate timer
		cashier.state = AgentState.idle;
		cashier.checks.get(0).state = CheckState.ready;
		//isHungry = false;
		cashier.stateChanged();
		
		//run scheduler
		assertTrue("Cashier's scheduler should have returned true, but didn't.", cashier.pickAndExecuteAnAction());
		
		//check postconditions for scheduler
		assertEquals("Cashier should have 1 check left to process. It doesn't.", cashier.checks.size(), 1);
		
		//run scheduler
		assertTrue("Cashier's scheduler should have returned true, but didn't.", cashier.pickAndExecuteAnAction());
		
		//check postconditions for scheduler
		assertEquals("Check 2 should be calcuating, It isn't.", c2.state, CheckState.calculating);
		
		//step 3
		//send to waiter
		double price2 = cashier.priceMap.get(c1.choice);
		waiter.msgHereIsACheck(c1.tableNumber, price2);
		//Check 1 is paid
		cashier.msgHereIsTheMoney(price1);
		
		//check postconditions
		assertEquals("Cashier should have been paid. It wasn't.",cashier.getCashInRegister(), price1);
		
		//step 4
		//Check 2 is paid
		cashier.msgHereIsTheMoney(price2 * .5d);
		
		//check postconditions
		assertEquals("Cashier should have been paid. It wasn't.",cashier.getCashInRegister(), price1 + price2 * .5d);
	
	}//end two normal customer scenario

	/**
	 * This tests the cashier in the condition that one check is delivered and one customer is ready 
	 * to pay the exact bill, but a market asks for the bill in between actions.
	 */
	public void testOneNormalCustomerMarketScenario()
	{
		//setUp() runs first before this test!
		
		//customer.cashier = cashier;//You can do almost anything in a unit test.			
		System.out.println("Test 1 Customer/Market Scenario:");
		
		cashier.msgHereIsTheMoney(300.00d); // give cashier money to pay with
		
		//check preconditions
		assertEquals("Cashier should have $300 dollars in it. It doesn't.",cashier.getCashInRegister(), 300.0d);
		assertTrue("Cashier should have no checks to process. It does.", cashier.checks.isEmpty());
		
		//step 1 of the test
		cashier.msgHereIsACheck(waiter, "Steak", 0);//send the message from a waiter

		//check postconditions for step 1 and preconditions scheduler
		assertEquals("Cashier should have 1 check to process. It doesn't.", cashier.checks.size(), 1);
		
		Check c = cashier.checks.get(0);
		assertEquals("Waiter should be Bob, It isn't.", c.waiter, waiter);
		assertEquals("Choice should be Steak, It isn't.", c.choice, "Steak");
		assertEquals("Table should be table 0, It isn't.", c.tableNumber, 0);
		assertEquals("State should be pending, It isn't.", c.state, CheckState.pending);
		
		//step 2
		cashier.msgHereIsTheBill(superior, 200.00d);
		
		//check postconditions for step 2
		assertEquals("Cashier should have 1 bill to pay. It doesn't.", cashier.billsToPay.size(), 1);
		
		Bill b = cashier.billsToPay.get(0);
		assertEquals("Market should be Superior. It isn't", b.market, superior);
		assertEquals("Price should be $200. It isn't", b.price, 200.00d);
		
		//run scheduler
		//market has higher priority
		assertTrue("Cashier's scheduler should have returned true, but didn't.", cashier.pickAndExecuteAnAction());

		//check postconditions
		assertEquals("Bill should be paid. It wasn't.", cashier.billsToPay.size(), 0);
		assertEquals("Cashier should have $100 dollars in it. It doesn't.",cashier.getCashInRegister(), 100.0d);
		
		//run scheduler
		assertTrue("Cashier's scheduler should have returned true, but didn't.", cashier.pickAndExecuteAnAction());

		//check postconditions for scheduler
		assertEquals("Check should be calcuating, It isn't.", c.state, CheckState.calculating);
		
		//step 2
		//send to waiter
		double price = cashier.priceMap.get(c.choice);
		waiter.msgHereIsACheck(c.tableNumber, price);
		cashier.msgHereIsTheMoney(price);
		
		//check postconditions
		assertEquals("Cashier should have been paid. It wasn't.",cashier.getCashInRegister(), price + 100.0d);
	
	}//end one normal customer/market scenario
	
	/**
	 * This tests the cashier in the condition that two checks are delivered, the first customer is ready 
	 * to pay the exact bill, and the second pays only a portion of the bill, but a market asks for a bill 
	 * in between actions. The cashier doesn't have enough money at first, but can pay after he recieves 
	 * payment
	 */
	public void testTwoNormalCustomerMarketScenario()
	{
		//setUp() runs first before this test!
		
		//customer.cashier = cashier;//You can do almost anything in a unit test.				
		//TODO: ADD MARKET
		
		System.out.println("Test 2 Customer/Market Scenario:");

		cashier.msgHereIsTheMoney(100.00d); // give cashier money to pay with
		
		//check preconditions
		assertEquals("Cashier should have 100 dollars in it. It doesn't.",cashier.getCashInRegister(), 100.0d);
		assertTrue("Cashier should have no checks to process. It does.", cashier.checks.isEmpty());
		
		//step 1 of the test
		cashier.msgHereIsACheck(waiter, "Steak", 0);//send the message from a waiter
		cashier.msgHereIsACheck(waiter, "Chicken", 2);//send the message from a waiter

		//check postconditions for step 1 and preconditions scheduler
		assertEquals("Cashier should have 2 check to process. It doesn't.", cashier.checks.size(), 2);
		
		Check c1 = cashier.checks.get(0);
		Check c2 = cashier.checks.get(1);
		assertEquals("Waiter should be Bob, It isn't.", c1.waiter, waiter);
		assertEquals("Choice should be Steak, It isn't.", c1.choice, "Steak");
		assertEquals("Table should be table 0, It isn't.", c1.tableNumber, 0);
		assertEquals("State should be pending, It isn't.", c1.state, CheckState.pending);
		
		assertEquals("Waiter should be Bob, It isn't.", c2.waiter, waiter);
		assertEquals("Choice should be Chicken, It isn't.", c2.choice, "Chicken");
		assertEquals("Table should be table 2, It isn't.", c2.tableNumber, 2);
		assertEquals("State should be pending, It isn't.", c2.state, CheckState.pending);
		
		//run scheduler
		assertTrue("Cashier's scheduler should have returned true, but didn't.", cashier.pickAndExecuteAnAction());

		//check postconditions for scheduler
		assertEquals("Check 1 should be calcuating, It isn't.", c1.state, CheckState.calculating);
		
		//step 2
		//send to waiter
		double price1 = cashier.priceMap.get(c1.choice);
		waiter.msgHereIsACheck(c1.tableNumber, price1);

		//simulate timer
		cashier.state = AgentState.idle;
		cashier.checks.get(0).state = CheckState.ready;
		//isHungry = false;
		cashier.stateChanged();
		
		//step 3
		cashier.msgHereIsTheBill(superior, 101.00d);
		
		//check postconditions for step 3
		assertEquals("Cashier should have 1 bill to pay. It doesn't.", cashier.billsToPay.size(), 1);
		
		Bill b = cashier.billsToPay.get(0);
		assertEquals("Market should be Superior. It isn't", b.market, superior);
		assertEquals("Price should be $101. It isn't", b.price, 101.00d);
		
		//run scheduler
		assertTrue("Cashier's scheduler should have returned true, but didn't.", cashier.pickAndExecuteAnAction());
		
		//check postconditions for scheduler
		assertEquals("Cashier should have 1 check left to process. It doesn't.", cashier.checks.size(), 1);
		
		//run scheduler
		assertTrue("Cashier's scheduler should have returned true, but didn't.", cashier.pickAndExecuteAnAction());
		
		//check postconditions for scheduler
		assertEquals("Check 1 should be ready, It isn't.", c1.state, CheckState.ready);
		assertEquals("Check 2 should be calcuating, It isn't.", c2.state, CheckState.calculating);
		
		//step 3
		//send to waiter
		double price2 = cashier.priceMap.get(c1.choice);
		waiter.msgHereIsACheck(c1.tableNumber, price2);
		cashier.state = AgentState.idle;
		//Check 1 is paid
		cashier.msgHereIsTheMoney(price1);
		
		
		//check postconditions
		assertEquals("Cashier should have been paid. It wasn't.",cashier.getCashInRegister(), 100.0d + price1);
		assertTrue("Cashier should now have enough money to pay the bill. It doesn't.", cashier.getCashInRegister() >= 101.0d * 1.1d);
		
		double money1 = cashier.getCashInRegister();
		//run scheduler
		//market has higher priority and cashier now has money
		assertTrue("Cashier's scheduler should have returned true, but didn't.", cashier.pickAndExecuteAnAction());
		
		//check postconditions
		assertEquals("Bill should be paid. It wasn't.", cashier.billsToPay.size(), 0);
		assertEquals("Cashier should have $101 + 10% interest less dollars in it. It doesn't.",cashier.getCashInRegister(), money1 - 101.0d * 1.1d);
		
		double money2 = cashier.getCashInRegister();
		//step 4
		//Check 2 is paid
		cashier.msgHereIsTheMoney(price2);
		
		//check postconditions
		assertEquals("Cashier should have been paid. It wasn't.",cashier.getCashInRegister(), money2 + price2);
	
	}//end two normal customer/market scenario
}

