/**
 * 
 */
package exterior.gui;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.LayoutManager;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;

import SimCity.Base.Building;
import SimCity.Base.God;
import SimCity.Base.Person.Morality;
import SimCity.Base.Person.Vehicle;

/**
 * @author Daniel
 * 
 */
public class SetupPanel extends JFrame {

	private final int WINDOWX = 500;
	private final int WINDOWY = 500;

	private AnimationPanel animationPanel;

	private JTextField enterName = new JTextField("Enter Name of Person");
	private JButton createPerson = new JButton("Create Person");

	private JPanel optionsPanel = new JPanel();
	private JPanel mainPanel = new JPanel();

	private JPanel professionPanel = new JPanel();
	private JPanel housingPanel = new JPanel();
	private JPanel transportationPanel = new JPanel();
	private JPanel moralityPanel = new JPanel();
	private JPanel modePanel = new JPanel();

	private JLabel professionLabel = new JLabel("Profession");
	private JLabel housingLabel = new JLabel("Housing");
	private JLabel transportationLabel = new JLabel("Transportation");
	private JLabel moralityLabel = new JLabel("Morality");
	private JLabel modeLabel = new JLabel("Mode");

	// --------------- Profession ------------------

	private ButtonGroup profession = new ButtonGroup();

	private JRadioButton bankManager = new JRadioButton("Bank Manager");
	private JRadioButton bankTeller = new JRadioButton("Bank Teller");
	private JRadioButton bankGuard = new JRadioButton("Bank Guard");
	private JRadioButton bankRobber = new JRadioButton("Bank Robber");

	private JRadioButton marketManager = new JRadioButton("Market Manager");
	private JRadioButton marketClerk = new JRadioButton("Market Clerk");
	private JRadioButton marketPacker = new JRadioButton("Market Packer");
	private JRadioButton marketDelivery = new JRadioButton("Market Delivery");

	private JRadioButton restHost = new JRadioButton("Restaurant Host");
	private JRadioButton restWaiter = new JRadioButton("Restaurant Waiter");
	private JRadioButton restCook = new JRadioButton("Restaurant Cook");
	private JRadioButton restCashier = new JRadioButton("Restaurant Cashier");

	// --------------- Living Arrangement ----------

	private ButtonGroup housing = new ButtonGroup();

	private JRadioButton houseTenant = new JRadioButton("House");
	private JRadioButton apartmentTenant = new JRadioButton("Apartment");

	// --------------- Mode of Transportation ------

	private ButtonGroup transportation = new ButtonGroup();

	private JRadioButton vehicleCar = new JRadioButton("Car");
	private JRadioButton vehicleBus = new JRadioButton("Bus");
	private JRadioButton vehicleWalk = new JRadioButton("Walk");

	// --------------- Morality --------------------

	private ButtonGroup morality = new ButtonGroup();

	private JRadioButton moralityGood = new JRadioButton("Good");
	private JRadioButton moralityBad = new JRadioButton("Bad");

	// --------------- Compatibility Mode ----------

	private ButtonGroup mode = new ButtonGroup();

	private JRadioButton modeNormal = new JRadioButton("Normal Mode");
	private JRadioButton modeCompatibility = new JRadioButton(
			"Compatibility Mode");

	public SetupPanel(AnimationPanel ap) {
		animationPanel = ap;

		setSize(WINDOWX, WINDOWY);
		setBounds(50, 50, WINDOWX, WINDOWY);
		setVisible(true);
		setTitle("Setup");
		setLayout(new BorderLayout());

		optionsPanel.setLayout(new BorderLayout());

		// --- Button Groups ---

		profession.add(bankManager);
		profession.add(bankGuard);
		profession.add(bankTeller);
		profession.add(bankRobber);
		profession.add(marketClerk);
		profession.add(marketPacker);
		profession.add(marketDelivery);
		profession.add(marketManager);
		profession.add(restCashier);
		profession.add(restCook);
		profession.add(restHost);
		profession.add(restWaiter);

		housing.add(apartmentTenant);
		housing.add(houseTenant);

		transportation.add(vehicleBus);
		transportation.add(vehicleCar);
		transportation.add(vehicleWalk);

		morality.add(moralityGood);
		morality.add(moralityBad);

		mode.add(modeNormal);
		mode.add(modeCompatibility);

		// --- Panels ---

		mainPanel.add(enterName);
		mainPanel.add(createPerson);
		
		professionPanel.setLayout(new GridLayout(12, 1));

		professionPanel.add(bankManager);
		professionPanel.add(bankGuard);
		professionPanel.add(bankTeller);
		professionPanel.add(bankRobber);
		professionPanel.add(marketClerk);
		professionPanel.add(marketPacker);
		professionPanel.add(marketDelivery);
		professionPanel.add(marketManager);
		professionPanel.add(restCashier);
		professionPanel.add(restCook);
		professionPanel.add(restHost);
		professionPanel.add(restWaiter);

		housingPanel.add(apartmentTenant);
		housingPanel.add(houseTenant);

		transportationPanel.add(vehicleBus);
		transportationPanel.add(vehicleCar);
		transportationPanel.add(vehicleWalk);

		moralityPanel.add(moralityGood);
		moralityPanel.add(moralityBad);

		modePanel.add(modeNormal);
		modePanel.add(modeCompatibility);

		optionsPanel.add(professionPanel, BorderLayout.WEST);
		optionsPanel.add(housingPanel, BorderLayout.NORTH);
		optionsPanel.add(transportationPanel, BorderLayout.CENTER);
		optionsPanel.add(moralityPanel, BorderLayout.EAST);
		optionsPanel.add(modePanel, BorderLayout.SOUTH);

		// --- Main Frame ---

		add(optionsPanel, BorderLayout.NORTH);
		add(mainPanel, BorderLayout.SOUTH);
		
		modeCompatibility.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				animationPanel.setShowRect(true);
			}
		});
		
		modeNormal.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				animationPanel.setShowRect(false);
			}
		});

		createPerson.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				String name = enterName.getText();
				String role = "Bank.bankCustomerRole"; // default i guess?
				Vehicle v = Vehicle.walk;
				Morality m = Morality.good;
				Building house = animationPanel.getGui().buildingList.get(0);
				Building b = animationPanel.getGui().buildingList.get(2);

				if (bankManager.isSelected())
					role = "Bank.bankManagerRole";
				else if (bankGuard.isSelected())
					role = "Bank.bankGuardRole";
				else if (bankTeller.isSelected())
					role = "Bank.tellerRole";
				else if (bankRobber.isSelected())
					role = "Bank.RobberRole";
				else if (marketClerk.isSelected())
					role = "market.MarketClerkRole";
				else if (marketPacker.isSelected())
					role = "market.MarketPackerRole";
				else if (marketDelivery.isSelected())
					role = "market.MarketDeliveryRole";
				else if (marketManager.isSelected())
					role = "market.MarketManagerRole";

				if (vehicleBus.isSelected())
					v = Vehicle.bus;
				else if (vehicleCar.isSelected())
					v = Vehicle.car;
				else if (vehicleWalk.isSelected())
					v = Vehicle.walk;

				if (moralityGood.isSelected())
					m = Morality.good;
				else if (moralityBad.isSelected())
					m = Morality.crook;

				if (apartmentTenant.isSelected()) house = selectApartment();
				else if (houseTenant.isSelected()) house = selectHouse();

				// b = getWorkplace(role);

				animationPanel.createPerson(name, role, v, m, house, b);
			}
		});
	}

	private Building selectApartment() {
		if (God.Get().getBHouse(0).getOwner().myTenants.size() < 8) {
			return animationPanel.getGui().buildingList.get(0);
		} else if (God.Get().getBHouse(1).getOwner().myTenants.size() < 8) {
			return animationPanel.getGui().buildingList.get(1);
		} else {
			return animationPanel.getGui().buildingList.get(4);
		}
	}

	private Building selectHouse() {
		if (God.Get().getBHouse(12).getOwner().myTenants.size() < 8) {
			return animationPanel.getGui().buildingList.get(12);
		} else if (God.Get().getBHouse(13).getOwner().myTenants.size() < 8) {
			return animationPanel.getGui().buildingList.get(13);
		} else if (God.Get().getBHouse(14).getOwner().myTenants.size() < 8) {
			return animationPanel.getGui().buildingList.get(14);
		} else {
			return animationPanel.getGui().buildingList.get(15);
		}
	}

	private Building getWorkplace(String role) {
		switch (role) {
		default:
			return null;
		}
	}

}
