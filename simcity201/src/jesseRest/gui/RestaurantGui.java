package jesseRest.gui;

import javax.swing.*;

import java.awt.*;
import java.awt.event.*;

import jesseRest.CookAgent;
import jesseRest.CustomerAgent;
import jesseRest.HostAgent;
import jesseRest.WaiterAgent;
/**
 * Main GUI class.
 * Contains the main frame and subsequent panels
 */
public class RestaurantGui extends JFrame implements ActionListener {
    /* The GUI has two frames, the control frame (in variable gui) 
     * and the animation frame, (in variable animationFrame within gui)
     */
	AnimationPanel animationPanel = new AnimationPanel();
	
    /* restPanel holds 2 panels
     * 1) the staff listing, menu, and lists of current customers all constructed
     *    in RestaurantPanel()
     * 2) the infoPanel about the clicked Customer (created just below)
     */    
    private RestaurantPanel restPanel = new RestaurantPanel(this);
	private HostAgent host = restPanel.getHost();
	private CookAgent cook = restPanel.getCook();
    
    /* infoPanel holds information about the clicked customer, if there is one*/
    private JPanel infoPanel;
    private JLabel infoLabel; //part of infoPanel
    private JPanel namePanel;
    private JLabel nameLabel;
    private JLabel imageLabel;
    private JCheckBox stateCB;//part of infoLabel

    private Object currentPerson;/* Holds the agent that the info is about.
    								Seems like a hack */

    /**
     * Constructor for RestaurantGui class.
     * Sets up all the gui components.
     */
    public RestaurantGui() {
        int WINDOWX = 800;
        int WINDOWY = 800;
    	setBounds(0, 0, WINDOWX, WINDOWY);
    	setLayout(new BorderLayout());
    	
        Dimension restDim = new Dimension(WINDOWX, (int) (WINDOWY * .4));
        restPanel.setPreferredSize(restDim);
        restPanel.setMinimumSize(restDim);
        restPanel.setMaximumSize(restDim);
        add(restPanel, BorderLayout.SOUTH);
        
    	animationPanel.setHost(host); 
    	animationPanel.setCook(cook);
    	Dimension animDim = new Dimension(800, 400);
    	animationPanel.setPreferredSize(animDim);
    	animationPanel.setMinimumSize(animDim);
    	animationPanel.setMaximumSize(animDim);
    	add(animationPanel, BorderLayout.CENTER);
        
        // Now, setup the info panel
        Dimension infoDim = new Dimension(WINDOWX, (int) (WINDOWY * .1));
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
        infoLabel.setText("<html><pre><i>Click Add to make customers</i></pre></html>");
        infoPanel.add(infoLabel);
        infoPanel.add(stateCB);
        add(infoPanel, BorderLayout.NORTH);
    }
    /**
     * updateInfoPanel() takes the given customer (or, for v3, Host) object and
     * changes the information panel to hold that person's info.
     *
     * @param person customer (or waiter) object
     */
    public void updateInfoPanel(Object person) {
        stateCB.setVisible(true);
        currentPerson = person;

        if (person instanceof CustomerAgent) {
            CustomerAgent customer = (CustomerAgent) person;
            stateCB.setText("Hungry?");
            stateCB.setSelected(customer.getGui().isHungry());
            stateCB.setEnabled(!customer.getGui().isHungry());
            infoLabel.setText("<html><pre>     Name: " + customer.getName() + " </pre></html>");
        }
        else if (person instanceof WaiterAgent) {
        	WaiterAgent waiter = (WaiterAgent) person;
        	
        	if (waiter.isOnBreak) {
        		stateCB.setEnabled(true);
        		stateCB.setText("Finish Break");
        		stateCB.setSelected(true);
        	} else if (waiter.waitingForBreak) {
        		stateCB.setEnabled(false);
        		stateCB.setText("Waiting for Break");
        		stateCB.setSelected(true);
        	} else {
        		stateCB.setText("Ask for Break");
        		stateCB.setEnabled(true);
        		stateCB.setSelected(false);
        	}

            infoLabel.setText("<html><pre>     Name: " + waiter.getName() + " </pre></html>");

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
            if (currentPerson instanceof WaiterAgent) {
            	WaiterAgent w = (WaiterAgent) currentPerson;
            	w.getGui().setOnBreak(stateCB.isSelected());
            	
            	if (stateCB.isSelected()) {
            		if (w.isOnBreak) {
            			stateCB.setText("Finish Break");
            			stateCB.setEnabled(true);
            		} else if (w.waitingForBreak) {
            			stateCB.setText("Waiting for Break");
            			stateCB.setEnabled(false);
            		} else {
            			stateCB.setSelected(false);
            		}
            	} else {
            		stateCB.setText("Ask for Break");
            	}
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
        }
    }
    /**
     * Main routine to get gui started
     */
    public static void main(String[] args) {
        RestaurantGui gui = new RestaurantGui();
        gui.setTitle("Jesse Chand's CS201 Restaurant");
        gui.setVisible(true);
        gui.setResizable(false);
        gui.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
}
