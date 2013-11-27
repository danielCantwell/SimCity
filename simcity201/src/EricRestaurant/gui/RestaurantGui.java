package EricRestaurant.gui;

import static java.lang.System.out;

import javax.swing.*;

import EricRestaurant.EricCustomer;
import EricRestaurant.EricHost;
import EricRestaurant.EricWaiter;
import EricRestaurant.interfaces.Cashier;
import EricRestaurant.interfaces.Customer;
import EricRestaurant.interfaces.Waiter;

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
	JFrame animationFrame = new JFrame("Restaurant Animation");
	
	
	AnimationPanel animationPanel = new AnimationPanel();
	/* restPanel holds 2 panels
	 * 1) the staff listing, menu, and lists of current customers all constructed
	 *    in RestaurantPanel()
	 * 2) the infoPanel about the clicked Customer (created just below)
	 */    
	private RestaurantPanel restPanel = new RestaurantPanel(this);


	/* infoPanel holds information about the clicked customer, if there is one*/
	private JPanel overallPanel;
	private JPanel bigPanel;
	private JPanel infoPanel; //private JPanel infoPanel2; 
	private JLabel infoLabel; //part of infoPanel
	private JPanel infoPanel2; // second info panel for entering info
	private JPanel infoPanel3; //Waiter entering panel
	private JCheckBox stateCB;//part of infoLabel
	private JCheckBox stateCB2;
	private JCheckBox stateWB;
	private JCheckBox stateWB2;
	private JButton addCust;
	private JButton addWait;
	private JButton pause;
	private JButton Break;
	private JButton Break2;
	boolean sketch = false;
	private Object currentPerson;/* Holds the agent that the info is about.
    								Seems like a hack */
	private Object currentWaiter;
	private JTextField inputN;
	private JTextField inputW;

	/**
	 * Constructor for RestaurantGui class.
	 * Sets up all the gui components.
	 */
	public RestaurantGui() {
		int WINDOWX = 1500;
		int WINDOWY = 725;
		Dimension infoDim = new Dimension((int)(WINDOWX*0.5), (int) (WINDOWY * 0.15));
		Dimension infoDim2 = new Dimension((int)(WINDOWX*0.5), (int) (WINDOWY * 0.1));
		Dimension dims = new Dimension(750, 800);
		Dimension dims2 = new Dimension(1500, 800);

		
		
//		
//		animationFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//		animationFrame.setBounds(100+WINDOWX, 50 , 550, 450);
//		animationFrame.setVisible(true);
//		animationFrame.add(animationPanel); 
		animationPanel.setVisible(true);

		setBounds(50, 50, WINDOWX, WINDOWY);

		setLayout(new BoxLayout((Container) getContentPane(), 
				BoxLayout.Y_AXIS));

		Dimension restDim = new Dimension((int) (WINDOWX * .5), (int) (WINDOWY * .55));
		restPanel.setPreferredSize(restDim);
		restPanel.setMinimumSize(restDim);
		restPanel.setMaximumSize(restDim);
		//add(restPanel);


		
		
		// Now, setup the info panel
		

		infoPanel = new JPanel();
		infoPanel.setPreferredSize(infoDim2);
		infoPanel.setMinimumSize(infoDim2);
		infoPanel.setMaximumSize(infoDim2);
		infoPanel.setBorder(BorderFactory.createTitledBorder("Information"));

//		infoPanel.setLayout(new GridLayout(1, 2, 30, 0));

		infoLabel = new JLabel(); 
		infoLabel.setText("<html><pre><i>Fill in the Customer and Waiter info below</i></pre></html>");
		infoPanel.add(infoLabel);
		stateCB = new JCheckBox();
		stateCB.setVisible(false);
		stateCB.addActionListener(this);
		Break2 = new JButton("Off Break");
		Break2.setVisible(false);
		Break2.addActionListener(this);
		Break = new JButton("Break");
		Break.setVisible(false);
		Break.addActionListener(this);
//		stateWB = new JCheckBox();
//		stateWB.setVisible(false);
//		stateWB.addActionListener(this);
//		stateWB2 = new JCheckBox();
//		stateWB2.setVisible(false);
//		stateWB2.addActionListener(this);
		infoPanel.add(stateCB);
		infoPanel.add(Break);
		infoPanel.add(Break2);
//		infoPanel.add(stateWB);
//		infoPanel.add(stateWB2);
		//add(infoPanel);

//Customer Info entering panel
		
		infoPanel2 = new JPanel();
		infoPanel2.setPreferredSize(infoDim);
		infoPanel2.setMinimumSize(infoDim);
		infoPanel2.setMaximumSize(infoDim);
		infoPanel2.setBorder(BorderFactory.createTitledBorder("Enter Customer Info"));
		infoPanel2.setLayout(new GridLayout(2, 1, 30, 0));


		inputN = new JTextField();
		infoPanel2.add(inputN);
		addCust = new JButton("Add Customer");
		addCust.addActionListener(this);
		pause = new JButton("Pause");
		pause.addActionListener(this);
		stateCB2 = new JCheckBox("Hungry?", false);
		stateCB2.setVisible(true);
		stateCB2.addActionListener(this);
		infoPanel2.add(stateCB2);
		infoPanel2.add(addCust);
		infoPanel2.add(pause);
		//add(infoPanel2);
//Waiter info entering panel
		
		infoPanel3 = new JPanel();
		infoPanel3.setPreferredSize(infoDim2);
		infoPanel3.setMinimumSize(infoDim2);
		infoPanel3.setMaximumSize(infoDim2);
		infoPanel3.setBorder(BorderFactory.createTitledBorder("Enter Waiter Info"));
		infoPanel3.setLayout(new GridLayout(1, 2, 30, 0));
		
		inputW = new JTextField();
		infoPanel3.add(inputW);
		//out.println("Waiter has been added: " + inputW.getText());
		addWait = new JButton("Add Waiter");
		addWait.addActionListener(this);
		infoPanel3.add(addWait);
		//add(infoPanel3);
		
		bigPanel = new JPanel();
		bigPanel.setPreferredSize(dims);
		bigPanel.setMinimumSize(dims);
		bigPanel.setMaximumSize(dims);
		bigPanel.add(restPanel);
		bigPanel.add(infoPanel);
		bigPanel.add(infoPanel2);
		bigPanel.add(infoPanel3);
		
		
//		add(bigPanel);

		
		
//		setLayout(new BoxLayout((Container) getContentPane(), 
//				BoxLayout.X_AXIS));
//		
		overallPanel = new JPanel();
		overallPanel.setPreferredSize(dims2);
		overallPanel.setMinimumSize(dims2);
		overallPanel.setMaximumSize(dims2);
		overallPanel.setLayout(new BorderLayout());
		overallPanel.setBorder(BorderFactory.createTitledBorder("Restaurant V2"));
		overallPanel.add(bigPanel, BorderLayout.EAST);
		overallPanel.add(animationPanel, BorderLayout.CENTER);
		
		add(overallPanel);
		
	}
	/**
	 * updateInfoPanel() takes the given customer (or, for v3, Host) object and
	 * changes the information panel to hold that person's info.
	 *
	 * @param person customer (or waiter) object
	 */
	public void updateInfoPanel(Object person) {
		stateCB.setVisible(false);
//		stateWB.setVisible(false);
		Break.setVisible(false);
		Break2.setVisible(false);
		currentPerson = person;

		if (person instanceof EricCustomer) {
			stateCB.setVisible(true);
			Cashier customer = (Cashier) person;
			stateCB.setText("Hungry?");
			//Should checkmark be there? 
			stateCB.setSelected(customer.getGui().isHungry());
			//Is customer hungry? Hack. Should ask customerGui
			stateCB.setEnabled(!customer.getGui().isHungry());
			// Hack. Should ask customerGui
			infoLabel.setText(
					"<html><pre>     Name: " + customer.getName() + " </pre></html>");
			infoPanel.validate();

		}
		if (person instanceof EricWaiter) {
			//stateWB.setVisible(true);
			Break.setVisible(true);
			Break2.setVisible(true);
			Waiter waiter = (Waiter) person;
			//stateWB.setText("On Break");
			//stateWB2.setText("Off Break");
			infoLabel.setText("<html><pre>     Name: " + waiter.getName() + " </pre></html>");
			infoPanel.validate();

		}
	}
	/**
	 * Action listener method that reacts to the checkbox being clicked;
	 * If it's the customer's checkbox, it will make him hungry
	 * For v3, it will propose a break for the waiter.
	 */
	public void actionPerformed(ActionEvent e) {
//		if (e.getSource() == pause) {
//			restPanel.pause();		
//			}

		if (e.getSource() == addCust) {
			restPanel.customerPanel.addPerson(inputN.getText());
			out.println("Customer has been added: " + inputN.getText());
			if (sketch == true) {
				sketch = false;
				if (currentPerson instanceof EricCustomer) {
					Cashier c = (Cashier) currentPerson;
					c.getGui().setHungry();
					stateCB2.setEnabled(true);
					stateCB2.setSelected(false);
				}
			}
		}
		
		if (e.getSource() == addWait) {
			restPanel.waiterPanel.addPerson(inputW.getText());
			if (currentPerson instanceof EricWaiter) {
				System.out.println("Waiter " + inputW.getText() + " added.");
				Waiter w = (Waiter) currentPerson;
				w.getGui().setWaiter();
			}
		}

		if (e.getSource() == stateCB2) {
			sketch = true;
		}

		if (e.getSource() == stateCB) {
			if (currentPerson instanceof EricCustomer) {
				Cashier c = (Cashier) currentPerson;
				c.getGui().setHungry();
				stateCB.setEnabled(false);
			}
		}
		if (e.getSource() == Break) {
			if(currentPerson instanceof EricWaiter) {
				Waiter w = (Waiter) currentPerson;
				w.askBreak();
				Break.setVisible(true);
				Break2.setVisible(true);
			}
		}
		if (e.getSource() == Break2) {
			if(currentPerson instanceof EricWaiter) {
				Waiter w = (Waiter) currentPerson;
				w.offBreak();
				Break.setVisible(true);
				Break2.setVisible(true);
			}
		}

//		if (e.getSource() == stateWB) {
//			if(currentPerson instanceof WaiterAgent) {
//				WaiterAgent w = (WaiterAgent) currentPerson;
//				w.askBreak();
//				stateWB.setEnabled(false);
//				stateWB2.setVisible(true);
//				stateWB.setVisible(false);
//				stateWB.setSelected(false);
//
//			}
//		}
//		if (e.getSource() == stateWB2) {
//			if(currentPerson instanceof WaiterAgent) {
//				WaiterAgent w = (WaiterAgent) currentPerson;
//				w.offBreak();
//				stateWB2.setVisible(false);
//				stateWB2.setSelected(false);
//				stateWB.setEnabled(true);
//				stateWB.setVisible(true);
//			}
//		}
	}
	/**
	 * Message sent from a customer gui to enable that customer's
	 * "I'm hungry" checkbox.
	 *
	 * @param c reference to the customer
	 */
	public void setCustomerEnabled(Customer c) {
		if (currentPerson instanceof EricCustomer) {
			Customer cust = (Customer) currentPerson;
			if (c.equals(cust)) {
				stateCB.setEnabled(true);
				stateCB.setSelected(false);
			}
			if (c.equals(cust)) {
				stateCB2.setEnabled(true);
				stateCB2.setSelected(false);
			}
		}
	}
	/**
	 * Main routine to get gui started
	 */
	public static void main(String[] args) {
		RestaurantGui gui = new RestaurantGui();
		gui.setTitle("csci201 Restaurant");
		gui.setVisible(true);
		gui.setResizable(false);
		gui.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
}
