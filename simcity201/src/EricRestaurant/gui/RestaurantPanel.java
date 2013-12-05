package EricRestaurant.gui;

import javax.swing.*;

import EricRestaurant.EricCashier;
import EricRestaurant.EricCook;
import EricRestaurant.EricCustomer;
import EricRestaurant.EricHost;
import EricRestaurant.EricWaiter;
import EricRestaurant.interfaces.Cashier;
import EricRestaurant.interfaces.Waiter;

import java.awt.*;
import java.awt.event.*;
import java.util.Vector;

/**
 * Panel in frame that contains all the restaurant information,
 * including host, cook, waiters, and customers.
 */
public class RestaurantPanel extends JPanel {

	//Host, cook, waiters and customers
	private EricWaiter waiter = new EricWaiter("joe");
	private EricCook cook = new EricCook("Cook");
	private EricCashier cashier = new EricCashier();
	private EricHost host = new EricHost("Host");
	private HostGui hostGui = new HostGui(waiter);
	private CookGui cookGui = new CookGui(cook);

	private Vector<EricCustomer> customers = new Vector<EricCustomer>();
	private Vector<EricWaiter> waiters = new Vector<EricWaiter>();
	private JPanel restLabel = new JPanel();
	public ListPanel customerPanel = new ListPanel(this, "Customers");
	public ListPanel waiterPanel = new ListPanel(this, "Waiters");

	private JPanel group = new JPanel();
	//private ImageIcon icon = new ImageIcon("//Users//Eric//Downloads//Smiley.jpg");
	//private JLabel picture = new JLabel(icon);

	private RestaurantGui gui; //reference to main gui

	public RestaurantPanel(RestaurantGui gui) {
		this.gui = gui;
		host.setWaiter(waiter);
		host.setCook(cook);
		host.setCashier(cashier);
//		cook.addMarket(market);
		cook.setGui(cookGui);
//		market.setCashier(cashier);
		
//		waiter.setGui(hostGui);	
//		waiter.setHost(host);
//		waiter.startThread();
		gui.animationPanel.addGui(hostGui);
		gui.animationPanel.addGui(cookGui);
//		host.startThread();
//		cook.startThread();
//		cashier.startThread();
//		market.startThread();

		setLayout(new GridLayout(1, 2, 30, 30));
		group.setLayout(new GridLayout(1, 2, 30, 30));

		group.add(customerPanel);
		group.add(waiterPanel);

		initRestLabel();
		add(restLabel);
		add(group);
		//add(picture);
	}

	/**
	 * Sets up the restaurant label that includes the menu,
	 * and host and cook information
	 */
	private void initRestLabel() {
		JLabel label = new JLabel();
		//restLabel.setLayout(new BoxLayout((Container)restLabel, BoxLayout.Y_AXIS));
		restLabel.setLayout(new BorderLayout());
		label.setText(
				"<html><h3><u>Tonight's Staff</u></h3><table><tr><td>host:</td><td>" + host.getName() + "</td></tr></table><h3><u> Menu</u></h3><table><tr><td>Steak</td><td>$15.99</td></tr><tr><td>Chicken</td><td>$10.99</td></tr><tr><td>Salad</td><td>$5.99</td></tr><tr><td>Pizza</td><td>$8.99</td></tr></table><br></html>");

		restLabel.setBorder(BorderFactory.createRaisedBevelBorder());
		restLabel.add(label, BorderLayout.CENTER);
		restLabel.add(new JLabel("               "), BorderLayout.EAST);
		restLabel.add(new JLabel("               "), BorderLayout.WEST);
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
				Cashier temp = customers.get(i);
				if (temp.getName() == name)
					gui.updateInfoPanel(temp);
			}
		}
		if (type.equals("Waiters")) {
			for (int i = 0; i < waiters.size(); i++) {
				Waiter temp = waiters.get(i);
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
//	public void addPerson(String type, String name) {
//
//		if (type.equals("Customers")) {
//			EricCustomer c = new EricCustomer(name);	
//			//CustomerGui g = new CustomerGui(c, gui);
//
//			gui.animationPanel.addGui(g);// dw
//			c.setHost(host);
//			c.setGui(g);
//			c.setWaiter(waiter);
//			c.setCash(cashier);
//			customers.add(c);
////			c.startThread();
//		}
//		if (type.equals("Waiters")) {
//			EricWaiter w = new EricWaiter(name);
//			HostGui hg = new HostGui(w);
//			gui.animationPanel.addGui(hg);// dw
//			w.setHost(host);
//			w.setGui(hg);
////			w.startThread();
//			w.setCashier(cashier);
//			waiters.add(w);
//		}
//	}

//	public void pause() {
//		host.pauseButton();
//		waiter.pauseButton();
//		for(EricCustomer customer : customers) {
//			customer.pauseButton();
//		}
//	}
}
