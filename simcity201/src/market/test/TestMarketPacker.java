package market.test;
import java.awt.Point;
import java.util.HashMap;
import java.util.Map;

import SimCity.Base.Person;
import market.MarketPackerRole;
import market.MarketPackerRole.AgentLocation;
import market.MarketPackerRole.AgentState;
import market.MarketPackerRole.OrderState;
import market.gui.MarketPackerGui;
import market.test.mock.MockMarketManager;
import junit.framework.TestCase;

public class TestMarketPacker extends TestCase
{
    MarketPackerRole packer;
    MarketPackerGui packerGui;

    MockMarketManager manager;
    
    Person person;

    /**
     * This method is run before each test. You can use it to instantiate the
     * class variables for your agent and mocks, etc.
     */
    public void setUp() throws Exception {
        super.setUp();
        
        packer = new MarketPackerRole();
        packerGui = new MarketPackerGui(packer);
        
        Map<Integer, Point> locations = new HashMap<Integer, Point>();
        locations.put(0, new Point(400, 400));
        locations.put(1, new Point(420, 400));
        locations.put(2, new Point(440, 400));
        packerGui.setLocations(locations);
        
        manager = new MockMarketManager();
        packer.setGui(packerGui);
        packer.setManager(manager);
        person = new Person("market.MarketPackerRole");

        packer.myPerson = person;
        packer.myPerson.house = "Apartment";
    }
    
    /**
     * Tests one order from manager
     */
    public void testOnePacker() {
        
        // check preconditions
        assertTrue("Packer should have no orders, it does.", packer.orders.isEmpty());
        assertEquals("Packer should be idle, it isn't.", packer.state, AgentState.Idle);
        
        // step 1
        packer.msgPackage(2, "Steak", 2, 0);

        // check postconditions
        assertEquals("Packer should have 1 orders, it does.", packer.orders.size(), 1);
        assertEquals("Packer should be idle, it isn't.", packer.state, AgentState.Idle);

        // step 2 (scheduler)
        packer.pickAndExecuteAnAction();
        
        // check postconditions
        assertEquals("Packer should be packing, it isn't.", packer.state, AgentState.Packing);
        assertEquals("Location should be in transit, it isn't.", packer.location, AgentLocation.Transit);
        
        // step 3
        // send message from gui
        packer.msgGuiArrivedAtItem();
        
        assertEquals("Location should be item, it isn't.", packer.location, AgentLocation.Item);

        // step 4 (scheduler)
        packer.pickAndExecuteAnAction();
        
        // check postconditions
        assertEquals("Order should be ready, it isn't.", packer.orders.get(0).state, OrderState.Ready);
        assertEquals("Location should be in transit, it isn't.", packer.location, AgentLocation.Transit);

        // step 5
        // send message from gui
        packer.msgGuiArrivedAtCounter();
        
        assertEquals("Location should be counter, it isn't.", packer.location, AgentLocation.Counter);
    }
    
    /**
     * Tests multiple orders from manager
     */
    public void testTwoPacker() {
        
        // check preconditions
        assertTrue("Packer should have no orders, it does.", packer.orders.isEmpty());
        assertEquals("Packer should be idle, it isn't.", packer.state, AgentState.Idle);
        
        // step 1
        packer.msgPackage(2, "Steak", 2, 0);
        packer.msgPackage(1, "Chicken", 4, 1);
        packer.msgPackage(3, "Basil", 1, 2);

        for (int i = 3; i > 0; i--)
        {
            // check postconditions
            assertEquals("Packer should have " + i + " orders, it does.", packer.orders.size(), i);
            assertEquals("Packer should be idle, it isn't.", packer.state, AgentState.Idle);
    
            // step 2 (scheduler)
            packer.pickAndExecuteAnAction();
            
            // check postconditions
            assertEquals("Packer should be packing, it isn't.", packer.state, AgentState.Packing);
            assertEquals("Location should be in transit, it isn't.", packer.location, AgentLocation.Transit);
            
            // step 3
            // send message from gui
            packer.msgGuiArrivedAtItem();
            
            assertEquals("Location should be item, it isn't.", packer.location, AgentLocation.Item);
    
            // step 4 (scheduler)
            packer.pickAndExecuteAnAction();
            
            // check postconditions
            assertEquals("Order should be ready, it isn't.", packer.orders.get(i-1).state, OrderState.Ready);
            assertEquals("Location should be in transit, it isn't.", packer.location, AgentLocation.Transit);
    
            // step 5
            // send message from gui
            packer.msgGuiArrivedAtCounter();
            
            assertEquals("Location should be counter, it isn't.", packer.location, AgentLocation.Counter);

            // step 6 (scheduler)
            packer.pickAndExecuteAnAction();
        }
    }
}
