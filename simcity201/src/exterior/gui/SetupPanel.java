/**
 * 
 */
package exterior.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.border.EtchedBorder;

import SimCity.Base.Building;
import SimCity.Base.God;
import SimCity.Base.Person.Morality;
import SimCity.Base.Person.Vehicle;

/**
 * @author Daniel
 * 
 */
public class SetupPanel extends JFrame {

	private final int WINDOWX = 1000;
	private final int WINDOWY = 255;

	private AnimationPanel animationPanel;

	private JTextField enterName = new JTextField("Enter Name of Person");
	private JButton createPerson = new JButton("Create Person");

	private JPanel professionsPanel = new JPanel();
	private JPanel optionsPanel = new JPanel();
	private JPanel mainPanel = new JPanel();

	private JPanel bankPanel = new JPanel();
	private JPanel marketPanel = new JPanel();
	private JPanel dannyRestPanel = new JPanel();
	private JPanel ericRestPanel = new JPanel();
	private JPanel jesseRestPanel = new JPanel();
	private JPanel brianRestPanel = new JPanel();
	private JPanel timRestPanel = new JPanel();
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

	// Danny Restaruant
	private JRadioButton dannyRestHost = new JRadioButton("Danny Host");
	private JRadioButton dannyRestWaiter = new JRadioButton("Danny Waiter");
	private JRadioButton dannyRestCook = new JRadioButton("Danny Cook");
	private JRadioButton dannyRestCashier = new JRadioButton("Danny Cashier");
	// Brian Restaurant
	private JRadioButton brianRestHost = new JRadioButton("Brian Host");
	private JRadioButton brianRestWaiter = new JRadioButton("Brian Waiter");
	private JRadioButton brianRestCook = new JRadioButton("Brian Cook");
	private JRadioButton brianRestCashier = new JRadioButton("Brian Cashier");
	// Jesse Restaurant
	private JRadioButton jesseRestHost = new JRadioButton("Jesse Host");
	private JRadioButton jesseRestWaiter = new JRadioButton("Jesse Waiter");
	private JRadioButton jesseRestCook = new JRadioButton("Jesse Cook");
	private JRadioButton jesseRestCashier = new JRadioButton("Jesse Cashier");
	// Eric Restaurant
	private JRadioButton ericRestHost = new JRadioButton("Eric Host");
	private JRadioButton ericRestWaiter = new JRadioButton("Eric Waiter");
	private JRadioButton ericRestCook = new JRadioButton("Eric Cook");
	private JRadioButton ericRestCashier = new JRadioButton("Eric Cashier");
	// Tim Restaurant
	private JRadioButton timRestHost = new JRadioButton("Tim Host");
	private JRadioButton timRestWaiter = new JRadioButton("Tim Waiter");
	private JRadioButton timRestCook = new JRadioButton("Tim Cook");
	private JRadioButton timRestCashier = new JRadioButton("Tim Cashier");
	

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
		setAlwaysOnTop(true);

		optionsPanel.setLayout(new BorderLayout());

		// --- Button Groups ---
		
		// bank
		profession.add(bankManager);
		profession.add(bankGuard);
		profession.add(bankTeller);
		profession.add(bankRobber);
		// market
		profession.add(marketClerk);
		profession.add(marketPacker);
		profession.add(marketDelivery);
		profession.add(marketManager);
		// danny rest
		profession.add(dannyRestCashier);
		profession.add(dannyRestCook);
		profession.add(dannyRestHost);
		profession.add(dannyRestWaiter);
		// brian rest
		profession.add(brianRestCashier);
		profession.add(brianRestCook);
		profession.add(brianRestHost);
		profession.add(brianRestWaiter);
		// jesse rest
		profession.add(jesseRestCashier);
		profession.add(jesseRestCook);
		profession.add(jesseRestHost);
		profession.add(jesseRestWaiter);
		// eric rest
		profession.add(ericRestCashier);
		profession.add(ericRestCook);
		profession.add(ericRestHost);
		profession.add(ericRestWaiter);
		// tim rest
		profession.add(timRestCashier);
		profession.add(timRestCook);
		profession.add(timRestHost);
		profession.add(timRestWaiter);
		
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
		
		bankPanel.setLayout(new GridLayout(4, 1));
		bankPanel.add(bankManager);
		bankPanel.add(bankGuard);
		bankPanel.add(bankTeller);
		bankPanel.add(bankRobber);
		
		marketPanel.setLayout(new GridLayout(4,1));
		marketPanel.add(marketClerk);
		marketPanel.add(marketPacker);
		marketPanel.add(marketDelivery);
		marketPanel.add(marketManager);
		
		dannyRestPanel.setLayout(new GridLayout(4,1));
		dannyRestPanel.add(dannyRestCashier);
		dannyRestPanel.add(dannyRestCook);
		dannyRestPanel.add(dannyRestHost);
		dannyRestPanel.add(dannyRestWaiter);
		
		brianRestPanel.setLayout(new GridLayout(4,1));
		brianRestPanel.add(brianRestCashier);
		brianRestPanel.add(brianRestCook);
		brianRestPanel.add(brianRestHost);
		brianRestPanel.add(brianRestWaiter);
		
		jesseRestPanel.setLayout(new GridLayout(4,1));
		jesseRestPanel.add(jesseRestCashier);
		jesseRestPanel.add(jesseRestCook);
		jesseRestPanel.add(jesseRestHost);
		jesseRestPanel.add(jesseRestWaiter);
		
		ericRestPanel.setLayout(new GridLayout(4,1));
		ericRestPanel.add(ericRestCashier);
		ericRestPanel.add(ericRestCook);
		ericRestPanel.add(ericRestHost);
		ericRestPanel.add(ericRestWaiter);
		
		timRestPanel.setLayout(new GridLayout(4,1));
		timRestPanel.add(timRestCashier);
		timRestPanel.add(timRestCook);
		timRestPanel.add(timRestHost);
		timRestPanel.add(timRestWaiter);

		housingPanel.setBorder(new EtchedBorder(Color.BLACK, Color.CYAN));
		housingPanel.add(apartmentTenant);
		housingPanel.add(houseTenant);

		transportationPanel.setBorder(new EtchedBorder(Color.BLACK, Color.CYAN));
		transportationPanel.add(vehicleBus);
		transportationPanel.add(vehicleCar);
		transportationPanel.add(vehicleWalk);

		moralityPanel.setBorder(new EtchedBorder(Color.BLACK, Color.CYAN));
		moralityPanel.add(moralityGood);
		moralityPanel.add(moralityBad);

		modePanel.setBorder(new EtchedBorder(Color.BLACK, Color.CYAN));
		modePanel.add(modeNormal);
		modePanel.add(modeCompatibility);
		
		professionsPanel.setBorder(new EtchedBorder(Color.BLACK, Color.CYAN));
		professionsPanel.setLayout(new GridLayout(1, 7));
		professionsPanel.add(bankPanel);
		professionsPanel.add(marketPanel);
		professionsPanel.add(dannyRestPanel);
		professionsPanel.add(brianRestPanel);
		professionsPanel.add(jesseRestPanel);
		professionsPanel.add(ericRestPanel);
		professionsPanel.add(timRestPanel);

		optionsPanel.add(professionsPanel, BorderLayout.NORTH);
		optionsPanel.add(housingPanel, BorderLayout.WEST);
		optionsPanel.add(transportationPanel, BorderLayout.CENTER);
		optionsPanel.add(moralityPanel, BorderLayout.EAST);
		optionsPanel.add(modePanel, BorderLayout.SOUTH);

		// --- Main Frame ---

		mainPanel.setBorder(new EtchedBorder(Color.BLACK, Color.CYAN));
		
		
		add(optionsPanel, BorderLayout.PAGE_START);
		add(mainPanel, BorderLayout.PAGE_END);
		
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

				if (bankManager.isSelected())			//////////////
					role = "Bank.bankManagerRole";		//
				else if (bankGuard.isSelected())		//
					role = "Bank.bankGuardRole";		//	Bank
				else if (bankTeller.isSelected())		//		Roles
					role = "Bank.tellerRole";			//
				else if (bankRobber.isSelected())		//
					role = "Bank.RobberRole";			//////////////
				
				else if (marketClerk.isSelected())		//////////////
					role = "market.MarketClerkRole";	//
				else if (marketPacker.isSelected())		//
					role = "market.MarketPackerRole";	//	Market
				else if (marketDelivery.isSelected())	//		Roles
					role = "market.MarketDeliveryRole";	//
				else if (marketManager.isSelected())	//
					role = "market.MarketManagerRole";	//////////////
				
				else if (dannyRestHost.isSelected())	//////////////
					role = "restaurant.DannyHost";		//
				else if (dannyRestCashier.isSelected())	//
					role = "restaurant.DannyCashier";	//	Danny Rest
				else if (dannyRestCook.isSelected())	//		Roles
					role = "restaurant.DannyCook";		//
				else if (dannyRestWaiter.isSelected())	//
					role = "restaurant.DannyWaiter";	//////////////
				
				else if (jesseRestCashier.isSelected())	//////////////
					role = "jesseRest.JesseCashier";	//
				else if (jesseRestCook.isSelected())	//
					role = "jesseRest.JesseCook";		//	Jesse Rest
				else if (jesseRestHost.isSelected())	//		Role
					role = "jesseRest.JesseHost";		//
				else if (jesseRestWaiter.isSelected())	//
					role = "jesseRest.JesseWaiter";		//////////////
				
				else if (brianRestHost.isSelected())	//////////////
					role = "brianRest.BrianHostRole";	//
				else if (brianRestCashier.isSelected())	//
					role = "brianRest.BrianCashierRole";//	Brian Rest
				else if (brianRestCook.isSelected())	//		Roles
					role = "brianRest.BrianCookRole";	//
				else if (brianRestWaiter.isSelected())	//
					role = "brianRest.BrianWaiterRole";	//////////////
				
				else if (ericRestHost.isSelected())		//////////////
					role = "EricRestaurant.EricHost";	//
				else if (ericRestCashier.isSelected())	//
					role = "EricRestaurant.EricCashier";//	Eric Rest
				else if (ericRestCook.isSelected())		//		Roles
					role = "EricRestaurant.EricCook";	//
				else if (ericRestWaiter.isSelected())	//
					role = "EricRestaurant.EricWaiter";	//////////////
				
				else if (timRestHost.isSelected())		//////////////
					role = "timRest.TimHostRole";		//
				else if (timRestCashier.isSelected())	//
					role = "timRest.timCashier";		//	Tim Rest
				else if (timRestCook.isSelected())		//		Roles
					role = "timRest.TimCookRole";		//
				else if (timRestWaiter.isSelected())	//
					role = "timRest.TimWaiterRole";		//////////////

				if (vehicleBus.isSelected())			//////////////
					v = Vehicle.bus;					//
				else if (vehicleCar.isSelected())		//	Transportation
					v = Vehicle.car;					//		Selection
				else if (vehicleWalk.isSelected())		//
					v = Vehicle.walk;					//////////////

				if (moralityGood.isSelected())			//////////////
					m = Morality.good;					//	Morality
				else if (moralityBad.isSelected())		//		Selection
					m = Morality.crook;					//////////////

				if (apartmentTenant.isSelected()) house = selectApartment();	//	Apartment
				else if (houseTenant.isSelected()) house = selectHouse();		//		Selection
				
				// Set Workplace Building
				
				if (role.contains("bank"))
					b = getWorkplace("bank");
				else if (role.contains("market"))
					b = getWorkplace("market");
				else if (role.contains("Danny"))
					b = getWorkplace("Danny");
				else if (role.contains("Jesse"))
					b = getWorkplace("Jesse");
				else if (role.contains("Brian"))
					b = getWorkplace("Brian");
				else if (role.contains("Eric"))
					b = getWorkplace("Eric");
				else if (role.contains("tim") || role.contains("Tim"))
					b = getWorkplace("Tim");

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
		int building;
		
		switch (role) {
		case "bank":
			building = 2;
			break;
		case "market":
			building = 3;
			break;
		case "Danny":
			building = 7;
			break;
		case "Jesse":
			building = 9;
			break;
		case "Brian":
			building = 6;
			break;
		case "Eric":
			building = 11;
			break;
		case "Tim":
			building = 10;
			break;
		default:
			return null;
		}
		
		return animationPanel.getGui().buildingList.get(building);
	}

}
