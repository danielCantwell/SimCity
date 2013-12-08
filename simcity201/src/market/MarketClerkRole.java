package market;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import SimCity.Base.God;
import SimCity.Base.Person;
import SimCity.Base.Role;
import SimCity.Globals.Money;
import market.gui.MarketClerkGui;
import market.interfaces.MarketClerk;
import market.interfaces.MarketCustomer;
import market.interfaces.MarketManager;

/**
 * Market Manager Agent
 * 
 * @author Timothy So
 */
public class MarketClerkRole extends Role implements MarketClerk {
	
	private static final int MAX_DOLLARS = 100;
    private static final int MAX_CENTS = 0;
    private static final int DOLLARS_TO_KEEP = 0;
    private static final int CENTS_TO_KEEP = 0;

    private MarketClerkGui gui = new MarketClerkGui(this);
	
	/**
	 * Data
	 */
	
    public MarketManager manager;
    public MarketCustomer customer;
    public boolean orderTaken = false;
    public List<Order> orders = Collections.synchronizedList(new ArrayList<Order>());
    
    public Money money = new Money(0, 0);
	    
	public MarketClerkRole() {
		super();
	}

	/** 
	 * Messages
	 */
	
	public void msgTakeOrder(MarketCustomer customer)
	{
	    this.customer = customer;
	    orderTaken = false;
	    stateChanged();
	}

    public void msgWantFood(int id, String choice, int amount)
    {
        orders.add(new Order(id, choice, amount));
        stateChanged();
    }

    public void msgGiveToCustomer(int id, String food, int amount)
    {
        synchronized(orders)
        {
            for (Order o : orders)
            {
                if (o.equals(new Order(id, food, amount)))
                {
                    o.state = OrderState.Ready;
                }
            }
        }
        stateChanged();
    }

    public void msgHereIsMoney(int id, Money price)
    {
        Order order = null;
        synchronized(orders)
        {
            for (Order o : orders)
            {
                if (o.id == id && o.state == OrderState.Payment)
                {
                    order = o;
                }
            }
        }
        if (order != null)
        {
            money.add(price);
            order.state = OrderState.Paid;
        }
        stateChanged();
    }

    public void workOver()
    {
        myPerson.Do("Closing time.");
        exitBuilding(myPerson);
    }

	/**
	 * Scheduler. Determine what action is called for, and do it.
	 */
	public boolean pickAndExecuteAnAction() {
		
	    if (money.isGreaterThan(new Money(MAX_DOLLARS, MAX_CENTS)))
	    {
	        storeMoney(new Money(DOLLARS_TO_KEEP, CENTS_TO_KEEP));
	        return true;
	    }
	    if (customer != null && !orderTaken)
	    {
	        takeOrder();
	        return true;
	    }
        Order order = null;
        synchronized(orders)
        {
            for (Order o : orders)
            {
                if (o.state == OrderState.Paid)
                {
                    order = o;
                }
            }
        }
        if (order != null)
        {
            notifyManager(order);
            return true;
        }
        synchronized(orders)
        {
            for (Order o : orders)
            {
                if (o.state == OrderState.Pending)
                {
                    order = o;
                }
            }
        }
        if (order != null)
        {
            makeOrder(order);
            return true;
        }
        synchronized(orders)
        {
            for (Order o : orders)
            {
                if (o.state == OrderState.Ready)
                {
                    order = o;
                }
            }
        }
        if (order != null)
        {
            giveOrder(order);
            return true;
        }
	    
		return false;
		// we have tried all our rules and found
		// nothing to do. So return false to main loop of abstract agent
		// and wait.
	}

    /**
	 * Actions
	 */

	private void takeOrder()
	{
	    customer.msgWhatDoYouWant(this);
	    orderTaken = true;
	}
	
	private void makeOrder(Order order)
	{
	    order.state = OrderState.Processing;
	    manager.msgFulfillOrder(this, order.id, order.choice, order.amount);
	}
	
	private void giveOrder(Order order)
	{
	    MarketCustomerRole customer = null;
	    for (int i = 0; i < God.Get().getPerson(order.id).roles.size(); i++)
	    {
	        if (God.Get().getPerson(order.id).roles.get(i) instanceof MarketCustomerRole)
	        {
	            customer = (MarketCustomerRole)God.Get().getPerson(order.id).roles.get(i);
	        }
	    }
	    if (customer != null)
	    {
	        customer.msgHereIsYourFood(order.choice, order.amount, ((MarketManagerRole) manager).getPriceOf(order.choice));
	        order.state = OrderState.Payment;
	    }
	}

    private void notifyManager(Order order)
    {
        manager.msgIAmFree(this);
        orders.remove(order);
    }
	
	private void storeMoney(Money amountToKeep)
	{
	    if (money.isGreaterThan(amountToKeep))
	    {
	        manager.msgHereIsTheMoney(money.subtract(amountToKeep));
	        money = amountToKeep;
	    }
	}
	
    /**
     * Utilities
     */

	public void setGui(MarketClerkGui gui) {
		this.gui = gui;
	}

	public MarketClerkGui getGui() { return gui; }

    @Override
    protected void enterBuilding() {
        // TODO Auto-generated method stub
        
    }
    
    public void setPerson(Person person)
    {
        super.setPerson(person);
    }
    
    public void setManager(MarketManager mainRole)
    {
        this.manager = mainRole;
    }

    /**
     * Inner Classes
     */
	
	public enum OrderState { Pending, Processing, Ready, Payment, Paid };
	
	public class Order
	{
		int id;
	    String choice;
	    int amount;
	    public OrderState state;
	    
	    Order(int id, String choice, int amount)
	    {
	        this.id = id;
	        this.choice = choice;
	        this.amount = amount;
	        state = OrderState.Pending;
	    }
        
        public boolean equals(Order other)
        {
            return id == other.id && choice.equals(other.choice) && amount == other.amount;
        }
	}
}
