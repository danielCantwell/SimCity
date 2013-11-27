package restaurant.gui;

import restaurant.CashierAgent;
import restaurant.CookAgent;
import restaurant.CustomerAgent;
import restaurant.HostAgent;
import restaurant.WaiterAgent;
import restaurant.MarketAgent;
import restaurant.interfaces.Host;

import javax.swing.*;

import agent.Agent;

import java.awt.*;
import java.util.ArrayList;
import java.util.Vector;

/**
 * Panel in frame that contains all the restaurant information,
 * including host, cook, waiters, and customers.
 */
public class RestaurantPanel extends JPanel {

    //Host, cook, waiters and customers
    private HostAgent host = new HostAgent("Omar");
    private CookAgent cook = new CookAgent("Brian");
    private CashierAgent cashier = new CashierAgent("Grant");
    private ArrayList<MarketAgent> markets = new ArrayList<MarketAgent>();
    
    //List of Agents for pausing.
    private Vector<Agent> agents = new Vector<Agent>();
    
    //List of Waiting customers.
    private Vector<CustomerAgent> customers = new Vector<CustomerAgent>();
    int numberOfWaiters = 0;
    
    CookGui cookgui;

    private JPanel restLabel = new JPanel();
    private ListPanel customerPanel;
    private ListPanel waiterListPanel;

    private int viewIndex = 1; //Are you viewing the customer, waiter, etc panel.
    private JPanel group = new JPanel();
    private JPanel menu = new JPanel();
    private JPanel customerSide = new JPanel();

    public RestaurantGui gui; //reference to main gui
 

    public RestaurantPanel(RestaurantGui gui) {
        this.gui = gui;
        
        cookgui = new CookGui(cook, gui);
        gui.animationPanel.addGui(cookgui);
        cook.setGUI(cookgui);
        
        customerPanel = new ListPanel(this, "Customers", true);
        waiterListPanel = new ListPanel(this,"Waiters", false);
        
        for (int i=0; i<3; i++){
        	markets.add(new MarketAgent(i+". Market", cashier));
        	markets.get(i).startThread();
        	agents.add(markets.get(i));
        	cook.addMarket(markets.get(i));
        }    
        
        host.startThread(); //Hack only one host.
        cook.startThread(); //Hack only one cook.
        cashier.startThread(); //Hack only one cashier
        
        agents.add(host); //Hack for only having one cook.
        agents.add(cook); //Hack for only having one cook.
        agents.add(cashier); //Hack only one cashier;
        
        setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
        
        initRestLabel();
        customerSide.add(customerPanel);
        
        add(restLabel);
        customerSide.add(group);
    }
    
    public void switchViews(int view){
    	
    	switch(view){
	    	case 1: 
	    		if(viewIndex != 1){
	    			customerSide.removeAll();
	    			customerSide.add(customerPanel);
	    			viewIndex = 1;
	    			customerSide.revalidate();
	    			customerSide.repaint();
	    			
	    		}
	    		break;
	    	case 2:
	    		if (viewIndex != 2){
	    			customerSide.removeAll();
	    			customerSide.add(waiterListPanel);
	    			viewIndex = 2;
	    			customerSide.revalidate();
	    			customerSide.repaint();
	    		}
	    		break;
	    	default: break;
    	}
    }
    
    public void addWaiter(String waiterName){
    	WaiterAgent w = new WaiterAgent(waiterName, host, cook, cashier, numberOfWaiters);
    	WaiterGui waitergui = new WaiterGui(w, gui);
    	w.setGUI(waitergui);
    	gui.animationPanel.addGui(waitergui);
    	host.addWaiter(w);
    	agents.add(w);
    	w.startThread();
    	numberOfWaiters++;
    }
    
    public Host getHost(){
    	return host;
    }

    /**
     * Sets up the restaurant label that includes the menu,
     * and host and cook information
     */
    private void initRestLabel() {
        JLabel label = new JLabel();
        Dimension restDim = new Dimension((int)(gui.WINDOWX/2), (int)(gui.WINDOWY/2));
        restLabel.setLayout(new BoxLayout((Container)restLabel, BoxLayout.X_AXIS));
        restLabel.setMinimumSize(restDim);
        restLabel.setMaximumSize(restDim);
        restLabel.setPreferredSize(restDim);
        
        //restLabel.setLayout(new BorderLayout());
        Dimension restCatDim = new Dimension(gui.WINDOWX/4, gui.WINDOWY/2);
        menu.setMinimumSize(restCatDim);
        menu.setMaximumSize(restCatDim);
        menu.setPreferredSize(restCatDim);
        customerSide.setMinimumSize(restCatDim);
        customerSide.setMaximumSize(restCatDim);
        customerSide.setPreferredSize(restCatDim);
        restLabel.add(menu);
        restLabel.add(customerSide);
        
        label.setText(
                "<html><h3><u>Tonight's Staff</u></h3><table><tr><td>host:</td><td>" + host.getName() + "</td></tr></table><h3><u> Menu</u></h3><table><tr><td>Steak</td><td>$15.99</td></tr><tr><td>Chicken</td><td>$10.99</td></tr><tr><td>Salad</td><td>$5.99</td></tr><tr><td>Pizza</td><td>$8.99</td></tr></table><br></html>");
        menu.add(label);
        
        menu.add(label, BorderLayout.CENTER);
        menu.add(new JLabel("               "), BorderLayout.EAST);
        menu.add(new JLabel("               "), BorderLayout.WEST);
        
        add(restLabel);
        
    }

    /**
     * When a customer or waiter is clicked, this function calls
     * updatedInfoPanel() from the main gui so that person's information
     * will be shown
     *
     * @param type indicates whether the person is a customer or waiter
     * @param name name of person
     */
    public void showInfo(String type, String name) {

        if (type.equals("Customers")) {

            for (int i = 0; i < customers.size(); i++) {
                CustomerAgent temp = customers.get(i);
                if (temp.getName() == name)
                    gui.updateInfoPanel(temp);
            }
        }
        
        if (type.equals("Waiters")){
        	for(Agent a : agents){
                if (a instanceof WaiterAgent){
                	WaiterAgent w = (WaiterAgent)a;
                	if (w.getName().equals(name)) {
                		gui.updateInfoPanel(w);
                	}
                }
            }
        }
    }
    
    public void Pause(){
    	gui.animationPanel.toggleTimer();
    	for(Agent a : agents){
    		a.Pause();
    	}
    }

    /**
     * Adds a customer or waiter to the appropriate list
     *
     * @param type indicates whether the person is a customer or waiter (later)
     * @param name name of person
     */
    
    public void addPerson(String type, String name, boolean isHungry) {

    	if (type.equals("Customers")) {
    		CustomerAgent c = new CustomerAgent(name, cashier);	
    		CustomerGui g = new CustomerGui(c, gui, host);

    		gui.animationPanel.addGui(g);// dw
 
    		c.setHost(host);
    		c.setGui(g);
    		
    		if (isHungry){
    			c.getGui().setHungry();;
    		}
    		
    		//Add the newly created customer to the list of customers
    		customers.add(c);
    		//Add the newly created customer to the list of agents (for pausing)
    		agents.add(c);
    		
    		c.startThread();	
    		
    	}
    }
    

}
