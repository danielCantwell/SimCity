package market;

import java.util.*;

import Bank.tellerRole;
import SimCity.Base.God;
import SimCity.Base.Person;
import SimCity.Base.Role;
import SimCity.Globals.Money;
import SimCity.gui.Gui;
import market.gui.MarketManagerGui;
import market.interfaces.MarketManager;

/**
 * Market Manager Agent
 * 
 * @author Timothy So
 */
public class MarketManagerRole extends Role implements MarketManager {
	
		private MarketManagerGui gui = new MarketManagerGui(this);
		
		/**
		 * Data
		 */
		
		public tellerRole teller;
		
		public Map<String, Inventory> inventory = Collections.synchronizedMap(new HashMap<String, Inventory>());
		public List<MyPacker> packers = Collections.synchronizedList(new ArrayList<MyPacker>());
		public List<MyClerk> clerks = Collections.synchronizedList(new ArrayList<MyClerk>());
    public List<MyDeliveryPerson> deliveryPeople = Collections.synchronizedList(new ArrayList<MyDeliveryPerson>());

    public List<Order> orders = Collections.synchronizedList(new ArrayList<Order>());
    
    public Money money;
    public int accountNumber;
    
    /**
     * Constructors
     */
    
		public MarketManagerRole() {
			super();
		}

		/** 
		 * Messages
		 */
		
		public void msgWantFood(int id, String choice, int amount)
		{
		    Order o = new Order(id, choice, amount, OrderType.Restaurant);
		    o.state = OrderState.Pending;
		    orders.add(o);
		    System.out.println("Order " + God.Get().getBuilding(id) + " added.");
	        stateChanged();
		}
		
		public void msgFulfillOrder(int id, String choice, int amount)
    {
        Order o = new Order(id, choice, amount, OrderType.Customer);
        o.state = OrderState.Pending;
        orders.add(o);   
        stateChanged();
    }
	
		public void msgOrderPacked(int id, String choice, int amount)
    {
	    Order other = new Order(id, choice, amount, OrderType.None);
        synchronized(orders)
        {
            for (Order order : orders)
            {
                if (order.equals(other))
                {
                    order.state = OrderState.Ready;
                }
            }
        }
        System.out.println("Recieved " + choice + ".");
        stateChanged();
    }
	
    public void msgHereIsTheMoney(int id, Money amount)
    {
        money.add(amount);
        synchronized(orders)
        {
            for (Order o : orders)
            {
                if (o.id == id && o.getCost().equals(amount))
                {
                    orders.remove(o);
                    break;
                }
            }
        }
        stateChanged();
    }
    
    public void msgWithdrawalSuccessful(Money amount)
    {
        money.add(amount);
        stateChanged();
    }
    
    public void msgDepositSuccessful(Money amount)
    {
        money.subtract(amount);
        stateChanged();
    }
    
    public void msgGrabbingItem(String item, int amount)
    {
        inventory.get(item).amount -= amount;
        gui.updateInventory(item, inventory.get(item).amount, inventory.get(item).location);
        stateChanged();
    }

    @Override
    protected void enterBuilding() {
        // TODO Auto-generated method stub
        
    }
    
    public void workOver()
    {
        // TODO Auto-generated method stub   
    }

		/**
		 * Scheduler. Determine what action is called for, and do it.
		 */
		protected boolean pickAndExecuteAnAction() {
			
		    // if money > MAX_AMOUNT
		    
		    // if money < MIN_AMOUNT
	
	        synchronized(orders)
	        {
	    	    for (Order o : orders)
	    	    {
	    	        if (o.state == OrderState.Pending)
	    	        {
	    	            synchronized(packers)
	    	            {
	        	            for (MyPacker packer : packers)
	        	            {
	        	                if (packer.state == PackerState.Idle)
	        	                {
	        	                    startOrder(o);
	        	                    return true;
	        	                }
	        	            }
	    	            }
	    	        }
	    	    }
	        }
	
	        synchronized(orders)
	        {
	    	    for (Order o : orders)
	            {
	                if (o.state == OrderState.Ready)
	                {
	                    if (o.type == OrderType.Restaurant)
	                    {
	                        synchronized(deliveryPeople)
	                        {
	                            for (MyDeliveryPerson dP : deliveryPeople)
	                            {
	                                if (dP.state == DeliveryPersonState.Idle)
	                                {
	                                    deliverOrder(o);
	                                    return true;
	                                }
	                            }
	                        }
	                    }
	                    else if (o.type == OrderType.Customer)
	                    {
	                        giveOrder(o);
	                        return true;
	                    }
	                }
	            }
	        }
		    
			return false;
			// we have tried all our rules and found
			// nothing to do. So return false to main loop of abstract agent
			// and wait.
		}
	
		/**
		 * Actions
		 */
	
		private void deposit(Money amount)
		{
		    teller.requestDeposit(accountNumber, amount);
		}
	
    private void withdraw(Money amount)
    {
        teller.requestWithdraw(accountNumber, amount);
    }
    
    private void startOrder(Order order)
    {
        MyPacker myPacker = null;
        int minOrder = Integer.MAX_VALUE;
        for (MyPacker mP : packers)
        {
            if (mP.state == PackerState.Idle && mP.orderCount < minOrder)
            {
                minOrder = mP.orderCount;
                myPacker = mP;
            }
        }
        if (myPacker != null)
        {
            myPacker.packer.msgPackage(order.id, order.choice, order.amount, inventory.get(order.choice).location);
            order.state = OrderState.Processing;
        }
    }
    
    private void deliverOrder(Order order)
    {
        MyDeliveryPerson myDP = null;
        for (MyDeliveryPerson mD : deliveryPeople)
        {
            if (mD.state == DeliveryPersonState.Idle)
            {
                myDP = mD;
                break;
            }
        }
        if (myDP != null)
        {
            myDP.deliveryPerson.msgMakeDelivery(order.id, order.choice, order.amount);
            order.state = OrderState.Ready;
            orders.remove(order);
        }
    }
    
    private void giveOrder(Order order)
    {
        MyClerk myClerk = null;
        for (MyClerk mC : clerks)
        {
            for (Order o : mC.orders)
            {
                if (o.equals(order))
                {
                    myClerk = mC;
                }
            }
        }
        if (myClerk != null)
        {
            myClerk.clerk.msgGiveToCustomer(order.id, order.choice, order.amount);
            order.state = OrderState.Ready;
            orders.remove(order);
        }
    }
	
    /**
     * Utilities
     * @param gui 
     */
	
    public void initializeInventory(MarketManagerGui gui)
    {
        this.gui = gui;
        // initialize inventory
        addItemToInventory("Steak", new Money(7, 00), 10, 0);
        addItemToInventory("Chicken", new Money(5, 00), 10, 29);
        addItemToInventory("Pizza", new Money(4, 00), 10, 2);
        addItemToInventory("Salad", new Money(3, 00), 10, 6);
        
        addItemToInventory("Salmon", new Money(7, 00), 10, 37);
        addItemToInventory("Tomatoes", new Money(5, 00), 10, 14);
        addItemToInventory("Bread", new Money(4, 00), 10, 38);
        addItemToInventory("Salt", new Money(3, 00), 10, 24);
        addItemToInventory("Pepper", new Money(7, 00), 10, 43);
        addItemToInventory("Onions", new Money(5, 00), 10, 30);
        addItemToInventory("Sausage", new Money(4, 00), 10, 12);
        addItemToInventory("Ice Cream", new Money(3, 00), 10, 8);
    }
    
		public void addItemToInventory(String name, Money price, int amount, int location)
		{
	        inventory.put(name, new Inventory(name, price, amount, location));
		}
		
		public void addPacker(MarketPackerRole r)
		{
		    packers.add(new MyPacker(r));
		}
		
		public void addDeliveryPerson(MarketDeliveryPersonRole r)
		{
	        deliveryPeople.add(new MyDeliveryPerson(r));
		}
		
		public void addClerk(MarketClerkRole r)
		{
	        clerks.add(new MyClerk(r));
		}
	
		public void setGui(MarketManagerGui gui) {
			this.gui = gui;
		}
	
		
		public MarketManagerGui getGui() { return gui; }
		
		public void setPerson(Person person)
		{
		    super.setPerson(person);
		}

    /**
     * Inner Classes
     */
	
		public enum PackerState { Idle, Packing };
		
		public class MyPacker 
		{
			MarketPackerRole packer;
			PackerState state;
			int orderCount;
			
			MyPacker(MarketPackerRole packer) 
			{
				this.packer = packer;
				state = PackerState.Idle;
				orderCount = 0;
			}
		}
    
    public enum ClerkState { Idle, Busy };
    
    public class MyClerk 
    {
        MarketClerkRole clerk;
        ClerkState state;
        List<Order> orders = Collections.synchronizedList(new ArrayList<Order>());
        
        MyClerk(MarketClerkRole clerk) 
        {
            this.clerk = clerk;
            state = ClerkState.Idle;
        }
    }
    
    public enum DeliveryPersonState { Idle, Delivering };
    
    public class MyDeliveryPerson 
    {
        MarketDeliveryPersonRole deliveryPerson;
        DeliveryPersonState state;
        
        MyDeliveryPerson(MarketDeliveryPersonRole deliveryPerson) 
        {
            this.deliveryPerson = deliveryPerson;
            state = DeliveryPersonState.Idle;
        }
    }

    public enum OrderState { Pending, Processing, Ready };
    public enum OrderType { None, Restaurant, Customer };
    
    public class Order
    {
    		int id;
        String choice;
        int amount;
        OrderState state;
        OrderType type;
        
        Order(int id, String choice, int amount, OrderType type)
        {
            this.id = id;
            this.choice = choice;
            this.amount = amount;
            this.type = type;
            state = OrderState.Pending;
        }
        
        public boolean equals(Order other)
        {
            return other.id == id && other.choice.equals(choice) && other.amount == amount;
        }
        
        public Money getCost()
        {
            return inventory.get(choice).getCost(amount);
        }
    }
    
    public class Inventory
    {
        private String name;
        private Money price;
        private int amount;
        private int location;
        
        Inventory(String name, Money price, int amount, int location)
        {
            this.name = name;
            this.price = price;
            this.amount = amount;
            this.location = location;
            gui.updateInventory(name, amount, location);
        }
        
        void changeAmount(int amount)
        {
            this.amount = amount;
            gui.updateInventory(name, amount, location);
        }
        
        void changeLocation(int location)
        {
            this.location = location;
            gui.updateInventory(name, amount, location);
        }
        
        Money getCost(int amount)
        {
            Money m = new Money(0, 0);
            for (int i = 0; i < amount; i++)
            {
                m.add(price);
            }
            return m;
        }
        
        int getAmount()
        {
            return amount;
        }
        
        int getLocation()
        {
            return location;
        }
    }
}
