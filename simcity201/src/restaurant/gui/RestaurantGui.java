package restaurant.gui;

import restaurant.CustomerAgent;
import restaurant.WaiterAgent;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
/**
 * Main GUI class.
 * Contains the main frame and subsequent panels
 */
public class RestaurantGui extends JFrame implements ActionListener {
    /* The GUI has two frames, the control frame (in variable gui) 
     * and the animation frame, (in variable animationFrame within gui)
     */
	DannyRestaurantAnimationPanel animationPanel = new DannyRestaurantAnimationPanel();
	
	private JPanel interactivePanel = new JPanel();
	
    /* restPanel holds 2 panels
     * 1) the staff listing, menu, and lists of current customers all constructed
     *    in RestaurantPanel()
     * 2) the infoPanel about the clicked Customer (created just below)
     */    
    public RestaurantPanel restPanel = new RestaurantPanel(this);
    
    /* infoPanel holds information about the clicked customer, if there is one*/
    private JPanel    infoPanel;
    private JLabel 	  infoLabel; //part of infoPanel
    private JCheckBox stateCB;//part of infoLabel
    
    private JButton pauseButton;
    boolean paused = false;

    private Object currentPerson;/* Holds the agent that the info is about.
    								Seems like a hack */
    
    static int MAIN_FRAME_ROWS      = 1;
    static int MAIN_FRAME_COLS      = 2;
    static int MAIN_FRAME_X_SPACING = 10;
    static int MAIN_FRAME_Y_SPACING = 10;

    /**
     * Constructor for RestaurantGui class.
     * Sets up all the gui components.
     */
    public RestaurantGui() {
        int WINDOWX = 800;
        int WINDOWY = 450;
        
        animationPanel.setHost(restPanel.getHost());

        // Animation Panel Bounds = (100+WINDOWX, 50, 100+WINDOWX, 100+WINDOWY);
        
        setBounds(50, 50, WINDOWX, WINDOWY);
        setLayout(new GridLayout(MAIN_FRAME_ROWS, MAIN_FRAME_COLS, 
        		MAIN_FRAME_X_SPACING, MAIN_FRAME_Y_SPACING));
        
    	interactivePanel.setLayout(new BorderLayout(20, 20));

        Dimension restDim = new Dimension((int) (WINDOWX * .5), (int) (WINDOWY * .5));
        restPanel.setPreferredSize(restDim);
        restPanel.setMinimumSize(restDim);
        restPanel.setMaximumSize(restDim);
        // Changed to Border Layout
        interactivePanel.add(BorderLayout.NORTH, restPanel);
        
        // Now, setup the info panel
        Dimension infoDim = new Dimension((int) (WINDOWX * .5), (int) (WINDOWY * .25));
        infoPanel = new JPanel();
        infoPanel.setPreferredSize(infoDim);
        infoPanel.setMinimumSize(infoDim);
        infoPanel.setMaximumSize(infoDim);
        infoPanel.setBorder(BorderFactory.createTitledBorder("Information"));

        stateCB = new JCheckBox();
        stateCB.setVisible(false);
        stateCB.addActionListener(this);

        infoPanel.setLayout(new GridLayout(1, 2, 30, 0));
        
        infoLabel = new JLabel(); 
        infoLabel.setText("<html><pre><i>Click Add to\n make customers</i></pre></html>");
        infoPanel.add(infoLabel);
        infoPanel.add(stateCB);
        // Changed to Border Layout
        interactivePanel.add(BorderLayout.CENTER, infoPanel);
        
        pauseButton = new JButton("P A U S E");
        pauseButton.addActionListener(this);
        interactivePanel.add(BorderLayout.SOUTH, pauseButton);
        
        add(interactivePanel);
        add(animationPanel);
    }
    /**
     * updateInfoPanel() takes the given customer (or, for v3, Host) object and
     * changes the information panel to hold that person's info.
     *
     * @param person customer (or waiter) object
     */
    public void updateInfoPanel(Object person, boolean status) {
        stateCB.setVisible(true);
        currentPerson = person;

        if (person instanceof CustomerAgent) {
            CustomerAgent customer = (CustomerAgent) person;
            if (status) {
            	customer.getGui().setHungry();
            }
            stateCB.setText("Hungry?");
          //Should checkmark be there? 
            stateCB.setSelected(customer.getGui().isHungry());
          //Is customer hungry? Hack. Should ask customerGui
            stateCB.setEnabled(!customer.getGui().isHungry());
          // Hack. Should ask customerGui
            infoLabel.setText(
               "<html><pre>     Name: " + customer.getName() + " </pre></html>");
        }
        
        if (person instanceof WaiterAgent) {
        	WaiterAgent waiter = (WaiterAgent) person;
        	if (status) {
        		waiter.getGui().setAvailable();
        	}
        	stateCB.setText("Available?");
        	stateCB.setSelected(waiter.getGui().isAvailable());
        	stateCB.setEnabled(!waiter.getGui().isAvailable());
        	
        	infoLabel.setText(
                    "<html><pre>     Name: " + waiter.getName() + " </pre></html>");
        }
        
        infoPanel.validate();
    }
    /**
     * Action listener method that reacts to the checkbox being clicked;
     * If it's the customer's checkbox, it will make him hungry
     * For v3, it will propose a break for the waiter.
     */
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == stateCB) {
            if (currentPerson instanceof CustomerAgent) {
                CustomerAgent c = (CustomerAgent) currentPerson;
                c.getGui().setHungry();
                stateCB.setEnabled(false);
            }
        }
        if (e.getSource() == pauseButton) {
        	if (!paused) {
        		System.out.println("pause");
        		paused = true;
        		restPanel.pause();
        	}
        	else if (paused) {
        		System.out.println("restart");
        		paused = false;
        		restPanel.restart();
        	}
        }
    }
    /**
     * Message sent from a customer gui to enable that customer's
     * "I'm hungry" checkbox.
     *
     * @param c reference to the customer
     */
    public void setCustomerEnabled(CustomerAgent c) {
        if (currentPerson instanceof CustomerAgent) {
            CustomerAgent cust = (CustomerAgent) currentPerson;
            if (c.equals(cust)) {
                stateCB.setEnabled(true);
                stateCB.setSelected(false);
            }
            restPanel.enableCustomerCheckBox(c);
        }
    }
    /**
     * Main routine to get gui started
     */
    /*public static void main(String[] args) {
        RestaurantGui gui = new RestaurantGui();
        gui.setTitle("csci201 Restaurant");
        gui.setVisible(true);
        gui.setResizable(false);
        gui.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
    */
}
