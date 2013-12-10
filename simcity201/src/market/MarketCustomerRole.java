package market;

import java.awt.Point;
import java.util.concurrent.Semaphore;

import SimCity.Base.God;
import SimCity.Base.Person.Vehicle;
import SimCity.Base.Role;
import SimCity.Globals.Money;
import SimCity.trace.AlertTag;
import market.gui.MarketCustomerGui;
import market.interfaces.MarketCustomer;

/**
 * Market Manager Agent
 * 
 * @author Timothy So
 */
public class MarketCustomerRole extends Role implements MarketCustomer {
	
	private MarketCustomerGui gui = new MarketCustomerGui(this);
	
	private Semaphore inTransit = new Semaphore(0, true);
	
	/**
	 * Data
	 */
	
    public MarketManagerRole manager;
    public enum AgentState { Idle, WaitingToHearBack, GoToLine, InLine, GoToCounter, Ordering, BeingServed, Served, Leaving, Left };
    public AgentState state = AgentState.Idle;
    public MarketClerkRole clerk;
    public Point destination = null;
    public Money amountOwed = new Money(0, 0);
	
    private int numOrders = 0;
    
	public MarketCustomerRole() {
		super();
	}

	/** 
	 * Messages
	 */
	
	public void msgPleaseTakeANumber(Point location)
	{
        state = AgentState.GoToLine;
        this.destination = location;
        stateChanged();
	}
    
    public void msgWhatDoYouWant(MarketClerkRole clerk)
    {
        state = AgentState.GoToCounter;
        this.destination = manager.getLocation(clerk);
        this.clerk = clerk;
        stateChanged();
    }

    public void msgHereIsYourFood(String food, int amount, Money price)
    {
        for (int i = 0; i < amount; i++)
        {
            if (food.equals("Car"))
            {
                myPerson.vehicle = Vehicle.car;
                myPerson.animPanel.getNewCarGui(myPerson);
            }
            myPerson.inventory.add(food);
        }
        amountOwed.add(price);
        numOrders--;
        if (numOrders <= 0)
        {
            state = AgentState.Served;
        }
        stateChanged();
    }

    public void msgGuiArrivedAtLine()
    {
        state = AgentState.InLine;
        inTransit.release();
        stateChanged();
    }

    public void msgGuiArrivedAtClerk()
    {
        state = AgentState.Ordering;
        inTransit.release();
        stateChanged();
    }

    public void msgGuiArrivedAtDoor()
    {
        state = AgentState.Left;
        inTransit.release();
        stateChanged();
    }

    public void workOver()
    {
        myPerson.Do("Market has closed for the day...");
    }

	/**
	 * Scheduler. Determine what action is called for, and do it.
	 */
	public boolean pickAndExecuteAnAction() {
	    if (destination != null)
        {
	        if (state == AgentState.GoToCounter)
	        {
    	        goToClerk(destination);
                return true;
    	    }
    	    else if (state == AgentState.GoToLine)
    	    {
    	        goToLine(destination);
    	        return true;
    	    }
        }
	    if (state == AgentState.Idle)
		{
		    askForClerk();
            return true;
		}
		else if (state == AgentState.Ordering)
		{
		    order();
            return true;
		}
		else if (state == AgentState.Served)
		{
		    pay();
            return true;
		}
		else if (state == AgentState.Leaving)
		{
		    leave();
		    return true;
		}
		else if (state == AgentState.Left)
		{
	        Info(AlertTag.Market, "I have " + myPerson.money + " and I'm leaving the building.");
		    exitBuilding(myPerson);
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
	
	private void askForClerk()
	{
	    manager.msgWantClerk(this, God.Get().persons.indexOf(myPerson));
	    state = AgentState.WaitingToHearBack;
	}
	
	private void goToClerk(Point location)
	{
	    gui.DoGoToCounter(location);
	    try
        {
            inTransit.acquire();
        }
        catch (InterruptedException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
	}
	
	private void goToLine(Point location)
	{
	    gui.DoGoToLine(location);
	    try
        {
            inTransit.acquire();
        }
        catch (InterruptedException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
	}
	
	private void order()
	{
	    for (int i = 0; i < myPerson.groceryList.size(); i++)
	    {
    	    clerk.msgWantFood(God.Get().persons.indexOf(myPerson), myPerson.groceryList.get(i).item, myPerson.groceryList.get(i).amount);
    	    numOrders++;
    	    Do(AlertTag.Market, "I would like " + myPerson.groceryList.get(i).amount + " " + myPerson.groceryList.get(i).item + "(s).");
	    }
	    if (myPerson.money.isGreaterThan(700, 0) && myPerson.vehicle != Vehicle.car)
	    {
	        clerk.msgWantFood(God.Get().persons.indexOf(myPerson), "Car", 1);
            numOrders++;
            Do(AlertTag.Market, "I think it's time for me to buy a car.");
	    }
	    state = AgentState.BeingServed;
	}
	
	private void pay()
	{
	    clerk.msgHereIsMoney(God.Get().persons.indexOf(myPerson), amountOwed);
	    // subtract appropriate money
	    System.out.println(myPerson.getMoney());//debug
	    myPerson.setMoney(myPerson.getMoney().subtract(amountOwed));
	    System.out.println(myPerson.getMoney());
	    state = AgentState.Leaving;
	}
	
	private void leave()
	{
	    gui.DoGoToDoor();
	    try
        {
            inTransit.acquire();
        }
        catch (InterruptedException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
	}

    /**
     * Utilities
     */
	
	public void setManager(MarketManagerRole manager)
	{
	    this.manager = manager;
	}

	public void setGui(MarketCustomerGui gui) {
		this.gui = gui;
	}

	public MarketCustomerGui getGui() { return gui; }

    @Override
    protected void enterBuilding() {
        if (!myPerson.building.getOpen()){
            myPerson.Do("Market is closed...");
            exitBuilding(myPerson);
        }
    }

    /**
     * Inner Classes
     */
	
	public enum OrderState { Pending, Processing, Ready };
	
	public class Order
	{
	    String choice;
	    int amount;
	    OrderState state;
	    
	    Order(String choice, int amount)
	    {
	        this.choice = choice;
	        this.amount = amount;
	        state = OrderState.Pending;
	    }
	    
	    public boolean equals(Order other)
	    {
	        return choice.equals(other.choice) && amount == other.amount;
	    }
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return "MarketCustomerRole";
	}
}
