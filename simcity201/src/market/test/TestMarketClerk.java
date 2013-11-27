
package market.test;
import java.awt.Point;
import java.util.HashMap;
import java.util.Map;

import SimCity.Base.God;
import SimCity.Base.Person;
import SimCity.Globals.Money;
import market.MarketClerkRole;
import market.MarketPackerRole;
import market.MarketClerkRole.OrderState;
import market.gui.MarketClerkGui;
import market.gui.MarketPackerGui;
import market.interfaces.MarketCustomer;
import market.test.mock.MockMarketCustomer;
import market.test.mock.MockMarketManager;
import junit.framework.TestCase;

public class TestMarketClerk extends TestCase
{
    MarketClerkRole clerk;
    MarketClerkGui clerkGui;

    MockMarketManager manager;
    MockMarketCustomer customer;
    
    Person person;
    Person cPerson;

    /**
     * This method is run before each test. You can use it to instantiate the
     * class variables for your agent and mocks, etc.
     */
    public void setUp() throws Exception {
        super.setUp();

        person = new Person("market.MarketClerkRole");

        clerk = (MarketClerkRole)person.mainRole;
        clerk.myPerson = person;
        clerk.myPerson.house = "Apartment";
        
        clerkGui = new MarketClerkGui(clerk);
        clerk.setGui(clerkGui);
        
        manager = new MockMarketManager();
        clerk.setManager(manager);
        
        cPerson = new Person("market.MarketCustomerRole");
        customer = new MockMarketCustomer();
        God.Get().addPerson(cPerson);
    }
    
    /**
     * Tests taking an order from one customer
     */
    public void testOneClerk() {
        
        // check preconditions
        assertTrue("Clerk should have no customer, it does.", clerk.customer == null);
        assertEquals("Clerk should have no orders, it doesn't.", clerk.orders.size(), 0);
        
        // step 1
        clerk.msgTakeOrder(customer);
        
        assertTrue("Clerk should have a customer, it doesn't.", clerk.customer == customer);

        // step 2 (scheduler)
        clerk.pickAndExecuteAnAction();
        
        // step 3
        clerk.msgWantFood(0, "Steak", 2);

        // check postconditions
        assertEquals("Clerk should have 1 orders, it doesn't.", clerk.orders.size(), 1);

        // step 4 (scheduler)
        clerk.pickAndExecuteAnAction();
        
        // check postconditions
        assertEquals("Order should be processing, it isn't.", clerk.orders.get(0).state, OrderState.Processing);

        // step 5
        // send message from gui
        clerk.msgGiveToCustomer(0, "Steak", 2);
        
        assertEquals("Order should be ready, it isn't.", clerk.orders.get(0).state, OrderState.Ready);

        
        // Rest of test doesn't work because cannot add mock customer to god
        /*
        // step 6 (scheduler)
        clerk.pickAndExecuteAnAction();
        // check postconditions
        assertEquals("Order should be paying, it isn't.", clerk.orders.get(0).state, OrderState.Payment);

        // step 7
        // send message from gui
        // note, money is made up
        clerk.msgHereIsMoney(0, new Money(100, 0));
        
        assertEquals("Order should be Paid, it isn't.", clerk.orders.get(0).state, OrderState.Paid);

        // step 8 (scheduler)
        clerk.pickAndExecuteAnAction();
        
        // check postconditions
        assertEquals("Clerk should have no orders, it doesn't.", clerk.orders.size(), 0);*/
    }
}
