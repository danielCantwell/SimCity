package jesseRest.gui;

import javax.swing.*;

import java.awt.*;
import java.awt.event.*;
import java.util.Vector;

import jesseRest.CashierAgent;
import jesseRest.CookAgent;
import jesseRest.CustomerAgent;
import jesseRest.HostAgent;
import jesseRest.MarketAgent;
import jesseRest.WaiterAgent;

/**
 * Panel in frame that contains all the restaurant information,
 * including host, cook, waiters, and customers.
 */
public class RestaurantPanel extends JPanel implements ActionListener {

    //Host, cook, waiters and customers
    private HostAgent host = new HostAgent("Host");
    private CookAgent cook = new CookAgent("Cook");
    private CashierAgent cashier = new CashierAgent("Cashier");
    
    private MarketAgent market1 = new MarketAgent("Market 1");
    private MarketAgent market2 = new MarketAgent("Market 2");
    private MarketAgent market3 = new MarketAgent("Market 3");
    
    private Vector<CustomerAgent> customers = new Vector<CustomerAgent>();
    private Vector<WaiterAgent> waiters = new Vector<WaiterAgent>();
    
    private JPanel restLabel = new JPanel();
    private ListPanel customerPanel = new ListPanel(this, "Customers");
    private ListPanel waiterPanel = new ListPanel(this, "Waiters");
    private JPanel group = new JPanel();
    
    private JButton addTable = new JButton("Add Table");
    private JButton pauseSim = new JButton("Pause");
    private JPanel actions = new JPanel();
    public boolean isPaused = false;
    private boolean flakeToggle = true;
    private RestaurantGui gui;

    public RestaurantPanel(RestaurantGui gui) {
        this.gui = gui;

        addTable.addActionListener(this);
        pauseSim.addActionListener(this);
        host.startThread();
        cook.startThread();
        cashier.startThread();
        
        market1.startThread();
        market2.startThread();
        market3.startThread();
        cook.addMarket(market1);
        cook.addMarket(market2);
        cook.addMarket(market3);
        cook.setAnimationPanel(gui.animationPanel);
        market1.setCook(cook);
        market2.setCook(cook);
        market3.setCook(cook);
        market1.setCashier(cashier);
        market2.setCashier(cashier);
        market3.setCashier(cashier);
        
        setLayout(new GridLayout(1, 2, 20, 20));
        group.setLayout(new GridLayout(1, 2, 10, 10));

        group.add(customerPanel);
        group.add(waiterPanel);

        initRestLabel();
        add(restLabel);
        add(group);
        addNonNormativeShortcuts();
    }

    /**
     * Sets up the restaurant label that includes the menu,
     * and host and cook information
     */
    private void initRestLabel() {
        JLabel label = new JLabel();
        restLabel.setLayout(new BorderLayout());
        label.setText(
                "<html><h3><u>Tonight's Staff</u></h3><table><tr><td>Host:</td><td>" + host.getName() + "</td></tr></table><h3><u> Menu</u></h3><table><tr><td>Steak</td><td>$15.99</td></tr><tr><td>Chicken</td><td>$10.99</td></tr><tr><td>Salad</td><td>$5.99</td></tr><tr><td>Pizza</td><td>$8.99</td></tr></table><br></html>");
        restLabel.setBorder(BorderFactory.createRaisedBevelBorder());
        restLabel.add(label, BorderLayout.CENTER);
        restLabel.add(new JLabel("               "), BorderLayout.EAST);
        restLabel.add(new JLabel("               "), BorderLayout.WEST);
        actions.add(addTable);
        actions.add(pauseSim);
        restLabel.add(actions, BorderLayout.SOUTH);
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
        else if (type.equals("Waiters")) {
            for (int i = 0; i < waiters.size(); i++) {
                WaiterAgent temp = waiters.get(i);
                if (temp.getName() == name)
                    gui.updateInfoPanel(temp);
            }
        }
    }

    /**
     * Adds a customer or waiter to the appropriate list
     *
     * @param type indicates whether the person is a customer or waiter (later)
     * @param name name of person
     */
    public void addPerson(String type, String name, boolean isChecked) {
    	if (type.equals("Customers")) {
    		CustomerAgent c = new CustomerAgent(name);	
    		CustomerGui g = new CustomerGui(c, gui);
    		gui.animationPanel.addGui(g);
  
    		if (isChecked) g.setHungry();
    		c.setHost(host);
    		c.setCashier(cashier);
    		c.setGui(g);
    		g.setPosition(customers.size());
    		c.position = customers.size();
    		customers.add(c);
    		c.startThread();
    	} else if (type.equals("Waiters")) {
    		WaiterAgent w = new WaiterAgent(name);
    		WaiterGui g = new WaiterGui(w);
    		gui.animationPanel.addGui(g);
    		
            w.setCook(cook);
            w.setHost(host);
            w.setCashier(cashier);
    		w.setGui(g);
    		g.setPosition(waiters.size());
    		waiters.add(w);
    		host.addWaiter(w);
    		w.startThread();
    	}
    }
    
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == addTable) {
        	host.addTable();
        }
        if (e.getSource() == pauseSim) {
        	if (!isPaused) {
        		System.out.println("Pausing simulation.");
        		host.pauseAgent();
        		cook.pauseAgent();
        		for (CustomerAgent c : customers) {
        			c.pauseAgent();
        		}
        		for (WaiterAgent w : waiters) {
        			w.pauseAgent();
        		}
        		market1.pauseAgent();
        		market2.pauseAgent();
        		market3.pauseAgent();
        		pauseSim.setText("Resume");
        	} else {
        		System.out.println("Resuming simulation.");
        		host.restartAgent();
        		cook.restartAgent();
        		for (CustomerAgent c : customers) {
        			c.restartAgent();
        		}
        		for (WaiterAgent w : waiters) {
        			w.restartAgent();
        		}
        		market1.restartAgent();
        		market2.restartAgent();
        		market3.restartAgent();
        		pauseSim.setText("Pause");
        	}
    		isPaused = !isPaused;
        }
    }
    
    // Keyboard shortcuts - use these to test non-normatives.
    // Check the readme for more information.
    protected void addNonNormativeShortcuts()
    {
        String stringCtrlI = "CTRL I";
        String stringCtrlD = "CTRL D";
        String stringCtrlF = "CTRL F";
        String stringCtrlS = "CTRL S";
        String stringCtrlA = "CTRL A";
        String stringCtrlR = "CTRL R";
        Action keyCtrlI = new AbstractAction()
        {
             public void actionPerformed(ActionEvent e)
             {
                 System.out.println("Customers will be impatient and leave the restuarant line.");
                 for (CustomerAgent c : customers) {
                	 c.msgLeavingBecauseImpatient();
                 }
             }
        };
        Action keyCtrlD = new AbstractAction()
        {
             public void actionPerformed(ActionEvent e)
             {
                 System.out.println("All customers have money decreased to $0.");
                 for (CustomerAgent c : customers) {
                	 c.money = 0;
                 }
             }
        };
        Action keyCtrlF = new AbstractAction()
        {
             public void actionPerformed(ActionEvent e)
             {
            	 flakeToggle = !flakeToggle;
                 System.out.println("Toggling customers being flakes or not (Default is yes). Is flake? :" + flakeToggle);
                 for (CustomerAgent c : customers) {
                	 c.ordersFoodWhenCantAfford = flakeToggle;
                 }
             }
        };
        Action keyCtrlS = new AbstractAction()
        {
             public void actionPerformed(ActionEvent e)
             {
                 System.out.println("Cashier money reduced to $0.");
                 cashier.money = 0;
             }
        };
        Action keyCtrlA = new AbstractAction()
        {
             public void actionPerformed(ActionEvent e)
             {
                 System.out.println("Cashier money raised by $100.");
                 cashier.money += 100;
             }
        };
        Action keyCtrlR = new AbstractAction()
        {
             public void actionPerformed(ActionEvent e)
             {
                 System.out.println("Market 1 inventory raised to 20 for each item.");
                 market1.inventory.put("Steak", new Integer(20));
                 market1.inventory.put("Chicken", new Integer(20));
                 market1.inventory.put("Pizza", new Integer(20));
                 market1.inventory.put("Salad", new Integer(20));
             }
        };
       
        getInputMap(this.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_I, KeyEvent.CTRL_MASK), stringCtrlI);
        getActionMap().put(stringCtrlI, keyCtrlI);
        getInputMap(this.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_D, KeyEvent.CTRL_MASK), stringCtrlD);
        getActionMap().put(stringCtrlD, keyCtrlD);
        getInputMap(this.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_F, KeyEvent.CTRL_MASK), stringCtrlF);
        getActionMap().put(stringCtrlF, keyCtrlF);
        getInputMap(this.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_S, KeyEvent.CTRL_MASK), stringCtrlS);
        getActionMap().put(stringCtrlS, keyCtrlS);
        getInputMap(this.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_A, KeyEvent.CTRL_MASK), stringCtrlA);
        getActionMap().put(stringCtrlA, keyCtrlA);
        getInputMap(this.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_R, KeyEvent.CTRL_MASK), stringCtrlR);
        getActionMap().put(stringCtrlR, keyCtrlR);
    }
    
    public HostAgent getHost() {
    	return host;
    }
    
    public CookAgent getCook() {
    	return cook;
    }
}
