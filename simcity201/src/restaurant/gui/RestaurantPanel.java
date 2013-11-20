package restaurant.gui;

import restaurant.CashierAgent;
import restaurant.CookAgent;
import restaurant.CustomerAgent;
import restaurant.HostAgent;
import restaurant.MarketAgent;
import restaurant.WaiterAgent;

import javax.swing.*;
import java.awt.*;
import java.util.Vector;

/**
 * Panel in frame that contains all the restaurant information,
 * including host, cook, waiters, and customers.
 */
public class RestaurantPanel extends JPanel {

    //Host, cook, waiters and customers
    private HostAgent host = new HostAgent("Sarah", this);
    private CashierAgent cashier = new CashierAgent("Cashier");

    private Vector<CustomerAgent> customers = new Vector<CustomerAgent>();
    private Vector<WaiterAgent> waiters = new Vector<WaiterAgent>();
    private Vector<MarketAgent> markets = new Vector<MarketAgent>();

    private JPanel    restLabel     = new JPanel();
    private ListPanel listPanel     = new ListPanel(this);
    private JPanel    group         = new JPanel();

    private RestaurantGui gui; //reference to main gui

    public RestaurantPanel(RestaurantGui gui) {
        this.gui = gui;
        //host.setGui(hostGui);
        
        gui.animationPanel.addGui(host.getCook().getGui());

        host.startThread();
        cashier.startThread();
        
        MarketAgent marketOne = new MarketAgent("Market_One");
        MarketAgent marketTwo = new MarketAgent("Market_Two");
        MarketAgent marketThree = new MarketAgent("Market_Three");
        
        markets.add(marketOne);
        markets.add(marketTwo);
        markets.add(marketThree);
        
        host.getCook().addMarket(marketOne);
        host.getCook().addMarket(marketTwo);
        host.getCook().addMarket(marketThree);

        setLayout(new GridLayout(1, 2, 10, 10));
        group.setLayout(new GridLayout(1, 2, 10, 10));

        group.add(listPanel);

        initRestLabel();
        add(restLabel);
        add(group);
    }

    /**
     * Sets up the restaurant label that includes the menu,
     * and host and cook information
     */
    private void initRestLabel() {
        JLabel label = new JLabel();
        restLabel.setLayout(new BorderLayout());
        label.setText(
                "<html><h3><u>Tonight's Staff</u></h3><table><tr><td>host:</td><td>" + host.getName() + "</td></tr></table><h3><u> Menu</u></h3><table><tr><td>Steak</td><td>$15.99</td></tr><tr><td>Chicken</td><td>$10.99</td></tr><tr><td>Salad</td><td>$5.99</td></tr><tr><td>Pizza</td><td>$8.99</td></tr></table><br></html>");

        restLabel.setBorder(BorderFactory.createRaisedBevelBorder());
        restLabel.add(label, BorderLayout.CENTER);
        restLabel.add(new JLabel("            "), BorderLayout.EAST);
        restLabel.add(new JLabel("            "), BorderLayout.WEST);
    }

    /**
     * When a customer or waiter is clicked, this function calls
     * updatedInfoPanel() from the main gui so that person's information
     * will be shown
     *
     * @param type indicates whether the person is a customer or waiter
     * @param name name of person
     */
    public void showInfo(String type, String name, boolean status) {

        if (type.equals("Customers")) {
        	
            for (int i = 0; i < customers.size(); i++) {
                CustomerAgent temp = customers.get(i);
                if (temp.getName() == name)
                    gui.updateInfoPanel(temp, status);
            }
        }
        /*
        if (type.equals("Waiters")) {
        	
        	for (int i = 0; i < waiters.size(); i++) {
        		WaiterAgent temp = waiters.get(i);
        		if (temp.getName() == name)
        			gui.updateInfoPanel(temp, status);
        	}
        }
        */
    }

    /**
     * Adds a customer or waiter to the appropriate list
     *
     * @param type indicates whether the person is a customer or waiter (later)
     * @param name name of person
     */
    public void addPerson(String type, String name, boolean status) {

    	if (type.equals("Customers")) {
    		CustomerAgent c = new CustomerAgent(name);	
    		CustomerGui   g = new CustomerGui(c, gui);

    		gui.animationPanel.addGui(g);
    		c.setHost(host);
    		c.setGui(g);
    		// status -> initiallyHungry
    		if (status) {
    			c.getGui().setHungry();
    		}
    		customers.add(c);
    		c.startThread();
    	}
    	
    	if (type.equals("Waiters")) {
    		WaiterAgent w = new WaiterAgent(name);
    		WaiterGui	g = new WaiterGui(w, gui);
    		
    		gui.animationPanel.addGui(g);
    		w.setGui(g);
    		// status will be used for waiter being on break or not
    		waiters.add(w);
    		host.addWaiter(w);
    	}
    }
    
    public HostAgent getHost() {
    	return host;
    }
    
    public CashierAgent getCashier() {
    	return cashier;
    }
    
    public void enableCustomerCheckBox(CustomerAgent agent) {
    	String cName = agent.getCustomerName();
    	listPanel.setCustomerEnabled(cName);
    }
    
    public void waiterGoOnBreak(String name) {
    	for (WaiterAgent waiter : waiters) {
    		if (waiter.getName().equals(name)) {
    			waiter.msgWantToGoOnBreak();
    		}
    	}
    }
    
    public void removeWaiterOnBreakSelection(WaiterAgent waiter) {
    	String wName = waiter.getName();
    	listPanel.setWaiterOffBreak(wName);
    }
    
    public void disableOnBreak(WaiterAgent waiter) {
    	String wName = waiter.getName();
    	listPanel.setWaiterBreakDisabled(wName);
    }
    
    public void enableOnBreak(WaiterAgent waiter) {
    	String wName = waiter.getName();
    	listPanel.setWaiterBreakEnabled(wName);
    }
    
    public void waiterGoOffBreak(String name) {
    	for (WaiterAgent waiter : waiters) {
    		if (waiter.getName().equals(name)) {
    			waiter.msgWantToGoOffBreak();
    		}
    	}
    }
    
    public void pause() {
    	host.pause();
    	host.getCook().pause();
    	cashier.pause();
    	for (CustomerAgent c : customers) {
    		c.getGui().pause();
    		c.pause();
    	}
    	for (WaiterAgent w : waiters) {
    		w.getGui().pause();
    		w.pause();
    	}
    	for (MarketAgent m : markets) {
    		m.pause();
    	}
    }
    
    public void restart() {
    	host.restart();
    	host.getCook().restart();
    	cashier.restart();
    	for (CustomerAgent c : customers) {
    		c.getGui().restart();
    		c.restart();
    	}
    	for (WaiterAgent w : waiters) {
    		w.getGui().restart();
    		w.restart();
    	}
    	for (MarketAgent m : markets) {
    		m.restart();
    	}
    }

}
