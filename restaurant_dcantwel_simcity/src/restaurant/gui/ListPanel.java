package restaurant.gui;

import javax.swing.*;

import java.awt.*;

/**
 * Subpanel of restaurantPanel.
 * This holds the scroll panes for the customers and waiters
 */
public class ListPanel extends JPanel {
	
	JTabbedPane tabbedPane = new JTabbedPane();
    
    private CustomerListPanel customerListPanel;
    private WaiterListPanel   waiterListPanel;

    private RestaurantPanel restPanel;

    /**
     * Constructor for ListPanel.  Sets up all the gui
     *
     * @param rp   reference to the restaurant panel
     * @param type indicates if this is for customers or waiters
     */
    public ListPanel(RestaurantPanel rp) {
        restPanel = rp;
        
        customerListPanel = new CustomerListPanel(restPanel, "Customers");
        waiterListPanel   = new WaiterListPanel(restPanel, "Waiters");

        setLayout(new BoxLayout((Container) this, BoxLayout.Y_AXIS));
        
        // Creating the tabbed pane
        tabbedPane.addTab("  Customers  ", customerListPanel);
        tabbedPane.addTab("   Waiters   ", waiterListPanel);
        add(tabbedPane);        
    }
    
    public void setCustomerEnabled(String cName) {
    	customerListPanel.setCustomerEnabled(cName);
    }
    
    public void setWaiterBreakDisabled(String wName) {
    	waiterListPanel.setWaiterBreakDisabled(wName);
    }
    
    public void setWaiterBreakEnabled(String wName) {
    	waiterListPanel.setWaiterBreakEnabled(wName);
    }
    
    public void setWaiterOffBreak(String wName) {
    	waiterListPanel.setWaiterEnabled(wName);
    }

}
