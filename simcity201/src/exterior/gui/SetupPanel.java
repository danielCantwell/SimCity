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
	private final int WINDOWY = 330;

	private AnimationPanel animationPanel;

	private JTextField enterName = new JTextField("Enter Name of Person");
	private JButton createPerson = new JButton("Create Person");

	private JPanel professionsPanel = new JPanel();
	private JPanel optionsPanel = new JPanel();
	private JPanel mainPanel = new JPanel();
	private JPanel scenarioPanel = new JPanel();

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

	// --------------- Profession ------------------

	private ButtonGroup profession = new ButtonGroup();

	private JRadioButton bankManager = new JRadioButton("Bank Manager");
	private JRadioButton bankTeller = new JRadioButton("Bank Teller");
	private JRadioButton bankGuard = new JRadioButton("Bank Guard");
	private JRadioButton bankRobber = new JRadioButton("Bank Robber");
	private JRadioButton bankCustomer = new JRadioButton("Bank Customer");

	private JRadioButton marketManager = new JRadioButton("Market Manager");
	private JRadioButton marketClerk = new JRadioButton("Market Clerk");
	private JRadioButton marketPacker = new JRadioButton("Market Packer");
	private JRadioButton marketDelivery = new JRadioButton("Market Delivery");
	private JRadioButton marketCustomer = new JRadioButton("Market Customer");

	// Danny Restaruant
	private JRadioButton dannyRestHost = new JRadioButton("Danny Host");
	private JRadioButton dannyRestWaiter = new JRadioButton("Danny Waiter");
	private JRadioButton dannyRestCook = new JRadioButton("Danny Cook");
	private JRadioButton dannyRestCashier = new JRadioButton("Danny Cashier");
	private JRadioButton dannyRestCustomer = new JRadioButton("Danny Customer");
	// Brian Restaurant
	private JRadioButton brianRestHost = new JRadioButton("Brian Host");
	private JRadioButton brianRestWaiter = new JRadioButton("Brian Waiter");
	private JRadioButton brianRestCook = new JRadioButton("Brian Cook");
	private JRadioButton brianRestCashier = new JRadioButton("Brian Cashier");
	private JRadioButton brianRestCustomer = new JRadioButton("Brian Customer");
	// Jesse Restaurant
	private JRadioButton jesseRestHost = new JRadioButton("Jesse Host");
	private JRadioButton jesseRestWaiter = new JRadioButton("Jesse Waiter");
	private JRadioButton jesseRestCook = new JRadioButton("Jesse Cook");
	private JRadioButton jesseRestCashier = new JRadioButton("Jesse Cashier");
	private JRadioButton jesseRestCustomer = new JRadioButton("Jesse Customer");
	// Eric Restaurant
	private JRadioButton ericRestHost = new JRadioButton("Eric Host");
	private JRadioButton ericRestWaiter = new JRadioButton("Eric Waiter");
	private JRadioButton ericRestCook = new JRadioButton("Eric Cook");
	private JRadioButton ericRestCashier = new JRadioButton("Eric Cashier");
	private JRadioButton ericRestCustomer = new JRadioButton("Eric Customer");
	// Tim Restaurant
	private JRadioButton timRestHost = new JRadioButton("Tim Host");
	private JRadioButton timRestWaiter = new JRadioButton("Tim Waiter");
	private JRadioButton timRestCook = new JRadioButton("Tim Cook");
	private JRadioButton timRestCashier = new JRadioButton("Tim Cashier");
	private JRadioButton timRestCustomer = new JRadioButton("Tim Customer");

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

	// --------------- Scenarios -------------------

	private JButton dannyRestScenario = new JButton("Test Danny Restaurant");
	private JButton ericRestScenario = new JButton("Test Eric Restaurant");
	private JButton s1 = new JButton("Normative Scenario One");
	private JButton s2 = new JButton("Normative Scenario Two");
	private JButton s3 = new JButton("Scenario Three - Does Nothing");
	private JButton s4 = new JButton("Scenario Four - Does Nothing");

	/**
	 * Constructor
	 * 
	 * @param ap
	 */
	public SetupPanel(AnimationPanel ap) {
		animationPanel = ap;

		setSize(WINDOWX, WINDOWY);
		setBounds(0, 0, WINDOWX, WINDOWY);
		setVisible(true);
		setTitle("Setup");
		setLayout(new BorderLayout());
		setAlwaysOnTop(true);

		optionsPanel.setLayout(new BorderLayout());

		vehicleWalk.setSelected(true);
		apartmentTenant.setSelected(true);
		moralityGood.setSelected(true);
		modeNormal.setSelected(true);

		// --- Button Groups ---

		// bank
		profession.add(bankManager);
		profession.add(bankGuard);
		profession.add(bankTeller);
		profession.add(bankRobber);
		profession.add(bankCustomer);
		// market
		profession.add(marketClerk);
		profession.add(marketPacker);
		profession.add(marketDelivery);
		profession.add(marketManager);
		profession.add(marketCustomer);
		// danny restaurant
		profession.add(dannyRestCashier);
		profession.add(dannyRestCook);
		profession.add(dannyRestHost);
		profession.add(dannyRestWaiter);
		profession.add(dannyRestCustomer);
		// brian restaurant
		profession.add(brianRestCashier);
		profession.add(brianRestCook);
		profession.add(brianRestHost);
		profession.add(brianRestWaiter);
		profession.add(brianRestCustomer);
		// jesse restaurant
		profession.add(jesseRestCashier);
		profession.add(jesseRestCook);
		profession.add(jesseRestHost);
		profession.add(jesseRestWaiter);
		profession.add(jesseRestCustomer);
		// eric restaurant
		profession.add(ericRestCashier);
		profession.add(ericRestCook);
		profession.add(ericRestHost);
		profession.add(ericRestWaiter);
		profession.add(ericRestCustomer);
		// tim restaurant
		profession.add(timRestCashier);
		profession.add(timRestCook);
		profession.add(timRestHost);
		profession.add(timRestWaiter);
		profession.add(timRestCustomer);

		// housing
		housing.add(apartmentTenant);
		housing.add(houseTenant);

		// transportation
		transportation.add(vehicleBus);
		transportation.add(vehicleCar);
		transportation.add(vehicleWalk);

		// morality
		morality.add(moralityGood);
		morality.add(moralityBad);

		// mode
		mode.add(modeNormal);
		mode.add(modeCompatibility);

		// --- Panels ---

		mainPanel.add(enterName);
		mainPanel.add(createPerson);

		bankPanel.setLayout(new GridLayout(5, 1));
		bankPanel.add(bankManager);
		bankPanel.add(bankGuard);
		bankPanel.add(bankTeller);
		bankPanel.add(bankRobber);
		bankPanel.add(bankCustomer);

		marketPanel.setLayout(new GridLayout(5, 1));
		marketPanel.add(marketClerk);
		marketPanel.add(marketPacker);
		marketPanel.add(marketDelivery);
		marketPanel.add(marketManager);
		marketPanel.add(marketCustomer);

		dannyRestPanel.setLayout(new GridLayout(5, 1));
		dannyRestPanel.add(dannyRestCashier);
		dannyRestPanel.add(dannyRestCook);
		dannyRestPanel.add(dannyRestHost);
		dannyRestPanel.add(dannyRestWaiter);
		dannyRestPanel.add(dannyRestCustomer);

		brianRestPanel.setLayout(new GridLayout(5, 1));
		brianRestPanel.add(brianRestCashier);
		brianRestPanel.add(brianRestCook);
		brianRestPanel.add(brianRestHost);
		brianRestPanel.add(brianRestWaiter);
		brianRestPanel.add(brianRestCustomer);

		jesseRestPanel.setLayout(new GridLayout(5, 1));
		jesseRestPanel.add(jesseRestCashier);
		jesseRestPanel.add(jesseRestCook);
		jesseRestPanel.add(jesseRestHost);
		jesseRestPanel.add(jesseRestWaiter);
		jesseRestPanel.add(jesseRestCustomer);

		ericRestPanel.setLayout(new GridLayout(5, 1));
		ericRestPanel.add(ericRestCashier);
		ericRestPanel.add(ericRestCook);
		ericRestPanel.add(ericRestHost);
		ericRestPanel.add(ericRestWaiter);
		ericRestPanel.add(ericRestCustomer);

		timRestPanel.setLayout(new GridLayout(5, 1));
		timRestPanel.add(timRestCashier);
		timRestPanel.add(timRestCook);
		timRestPanel.add(timRestHost);
		timRestPanel.add(timRestWaiter);
		timRestPanel.add(timRestCustomer);

		housingPanel.setBorder(new EtchedBorder(Color.BLACK, Color.CYAN));
		housingPanel.add(apartmentTenant);
		housingPanel.add(houseTenant);

		transportationPanel
				.setBorder(new EtchedBorder(Color.BLACK, Color.CYAN));
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

		// Scenario Panel

		scenarioPanel.setLayout(new GridLayout(2, 3));

		scenarioPanel.add(dannyRestScenario);
		scenarioPanel.add(ericRestScenario);
		scenarioPanel.add(s1);
		scenarioPanel.add(s2);
		scenarioPanel.add(s3);
		scenarioPanel.add(s4);

		// --- Main Frame ---

		mainPanel.setBorder(new EtchedBorder(Color.BLACK, Color.CYAN));

		add(optionsPanel, BorderLayout.PAGE_START);
		add(mainPanel, BorderLayout.CENTER);
		add(scenarioPanel, BorderLayout.PAGE_END);

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
				String role = "";
				Vehicle v = Vehicle.walk;
				Morality m = Morality.good;
				Building house = animationPanel.getGui().buildingList.get(0);
				Building b = animationPanel.getGui().buildingList.get(2);

				// bank roles
				if (bankManager.isSelected())
					role = "Bank.bankManagerRole";
				else if (bankGuard.isSelected())
					role = "Bank.bankGuardRole";
				else if (bankTeller.isSelected())
					role = "Bank.tellerRole";
				else if (bankRobber.isSelected())
					role = "Bank.RobberRole";
				else if (bankCustomer.isSelected())
					role = "Bank.bankCustomerRole";

				// market roles
				else if (marketClerk.isSelected())
					role = "market.MarketClerkRole";
				else if (marketPacker.isSelected())
					role = "market.MarketPackerRole";
				else if (marketDelivery.isSelected())
					role = "market.MarketDeliveryRole";
				else if (marketManager.isSelected())
					role = "market.MarketManagerRole";
				else if (marketCustomer.isSelected())
					role = "market.MarketCustomerRole";

				// danny restaurant roles
				else if (dannyRestHost.isSelected())
					role = "restaurant.DannyHost";
				else if (dannyRestCashier.isSelected())
					role = "restaurant.DannyCashier";
				else if (dannyRestCook.isSelected())
					role = "restaurant.DannyCook";
				else if (dannyRestWaiter.isSelected())
					role = "restaurant.DannyWaiter";
				else if (dannyRestCustomer.isSelected())
					role = "restaurant.DannyCustomer";

				// jesse restaurant roles
				else if (jesseRestCashier.isSelected())
					role = "jesseRest.JesseCashier";
				else if (jesseRestCook.isSelected())
					role = "jesseRest.JesseCook";
				else if (jesseRestHost.isSelected())
					role = "jesseRest.JesseHost";
				else if (jesseRestWaiter.isSelected())
					role = "jesseRest.JesseWaiter";
				else if (jesseRestCustomer.isSelected())
					role = "jesseRest.JesseCustomer";

				// brian restaurant roles
				else if (brianRestHost.isSelected())
					role = "brianRest.BrianHostRole";
				else if (brianRestCashier.isSelected())
					role = "brianRest.BrianCashierRole";
				else if (brianRestCook.isSelected())
					role = "brianRest.BrianCookRole";
				else if (brianRestWaiter.isSelected())
					role = "brianRest.BrianWaiterRole";
				else if (brianRestCustomer.isSelected())
					role = "briantRest.BrianCustomerRole";

				// eric restaurant roles
				else if (ericRestHost.isSelected())
					role = "EricRestaurant.EricHost";
				else if (ericRestCashier.isSelected())
					role = "EricRestaurant.EricCashier";
				else if (ericRestCook.isSelected())
					role = "EricRestaurant.EricCook";
				else if (ericRestWaiter.isSelected())
					role = "EricRestaurant.EricWaiter";
				else if (ericRestCustomer.isSelected())
					role = "EricRestaurant.EricCustomer";

				// tim restaurant roles
				else if (timRestHost.isSelected())
					role = "timRest.TimHostRole";
				else if (timRestCashier.isSelected())
					role = "timRest.timCashier";
				else if (timRestCook.isSelected())
					role = "timRest.TimCookRole";
				else if (timRestWaiter.isSelected())
					role = "timRest.TimWaiterRole";
				else if (timRestCustomer.isSelected())
					role = "timRest.TimCustomerRole";

				// transportation selection
				if (vehicleBus.isSelected())
					v = Vehicle.bus;
				else if (vehicleCar.isSelected())
					v = Vehicle.car;
				else if (vehicleWalk.isSelected())
					v = Vehicle.walk;

				// morality selection
				if (moralityGood.isSelected())
					m = Morality.good;
				else if (moralityBad.isSelected())
					m = Morality.crook;

				// apartment selection
				if (apartmentTenant.isSelected())
					house = selectApartment();
				else if (houseTenant.isSelected())
					house = selectHouse();

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

				animationPanel.createPerson(name, role, v, m, house, b, 1);
			}
		});

		// --------------- SCENARIO BUTTONS ---------------
		dannyRestScenario.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				animationPanel.createPerson("D_Host_One", "restaurant.DannyHost",
						Vehicle.walk, Morality.good,
						animationPanel.getGui().buildingList.get(0),
						animationPanel.getGui().buildingList.get(9), 1);
				animationPanel.createPerson("D_Host_Two", "restaurant.DannyHost",
						Vehicle.walk, Morality.good,
						animationPanel.getGui().buildingList.get(0),
						animationPanel.getGui().buildingList.get(9), 2);
				animationPanel.createPerson("D_Cashier_One",
						"restaurant.DannyCashier", Vehicle.walk, Morality.good,
						animationPanel.getGui().buildingList.get(0),
						animationPanel.getGui().buildingList.get(9), 1);
				animationPanel.createPerson("D_Cashier_Two",
						"restaurant.DannyCashier", Vehicle.walk, Morality.good,
						animationPanel.getGui().buildingList.get(0),
						animationPanel.getGui().buildingList.get(9), 2);
				animationPanel.createPerson("D_Cook_One", "restaurant.DannyCook",
						Vehicle.walk, Morality.good,
						animationPanel.getGui().buildingList.get(0),
						animationPanel.getGui().buildingList.get(9), 1);
				animationPanel.createPerson("D_Cook_Two", "restaurant.DannyCook",
						Vehicle.walk, Morality.good,
						animationPanel.getGui().buildingList.get(0),
						animationPanel.getGui().buildingList.get(9), 2);
				animationPanel.createPerson("D_Waiter_One",
						"restaurant.DannyWaiter", Vehicle.walk, Morality.good,
						animationPanel.getGui().buildingList.get(0),
						animationPanel.getGui().buildingList.get(9), 1);
				animationPanel.createPerson("D_Waiter_Two",
						"restaurant.DannyWaiter", Vehicle.walk, Morality.good,
						animationPanel.getGui().buildingList.get(0),
						animationPanel.getGui().buildingList.get(9), 2);
				animationPanel.createPerson("D_Customer_One",
						"restaurant.DannyCustomer", Vehicle.walk,
						Morality.good,
						animationPanel.getGui().buildingList.get(1),
						animationPanel.getGui().buildingList.get(9), 1);
				animationPanel.createPerson("D_Customer_Two",
						"restaurant.DannyCustomer", Vehicle.walk,
						Morality.good,
						animationPanel.getGui().buildingList.get(1),
						animationPanel.getGui().buildingList.get(9), 2);
			}
		});

		ericRestScenario.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				animationPanel.createPerson("EHost", "EricRestaurant.EricHost",
						Vehicle.walk, Morality.good,
						animationPanel.getGui().buildingList.get(0),
						animationPanel.getGui().buildingList.get(11), 1);
				animationPanel.createPerson("ECustomer",
						"EricRestaurant.EricCustomer", Vehicle.walk,
						Morality.good,
						animationPanel.getGui().buildingList.get(0),
						animationPanel.getGui().buildingList.get(11), 1);
				animationPanel.createPerson("EWaiter",
						"EricRestaurant.EricWaiter", Vehicle.walk,
						Morality.good,
						animationPanel.getGui().buildingList.get(0),
						animationPanel.getGui().buildingList.get(11), 1);
				animationPanel.createPerson("EWaiter",
						"EricRestaurant.EricWaiter", Vehicle.walk,
						Morality.good,
						animationPanel.getGui().buildingList.get(0),
						animationPanel.getGui().buildingList.get(11), 1);
				animationPanel.createPerson("ECustomer2",
						"EricRestaurant.EricCustomer", Vehicle.walk,
						Morality.good,
						animationPanel.getGui().buildingList.get(0),
						animationPanel.getGui().buildingList.get(11), 1);
				animationPanel.createPerson("ECashier",
						"EricRestaurant.EricCashier", Vehicle.walk,
						Morality.good,
						animationPanel.getGui().buildingList.get(0),
						animationPanel.getGui().buildingList.get(11), 1);
				animationPanel.createPerson("ECook", "EricRestaurant.EricCook",
						Vehicle.walk, Morality.good,
						animationPanel.getGui().buildingList.get(0),
						animationPanel.getGui().buildingList.get(11), 1);
			}
		});

		s1.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				runScenarioOne();
			}
		});

		s2.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				runScenarioTwo();
			}
		});

		s3.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				runScenarioThree();
			}
		});

		s4.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				runScenarioFour();
			}
		});
	}

	private Building selectApartment() {
		if (God.Get().getBHouse(0).numTenants < 8) {
			God.Get().getBHouse(0).incrementNumTenants();
			return animationPanel.getGui().buildingList.get(0);
		} else if (God.Get().getBHouse(1).numTenants < 8) {
			God.Get().getBHouse(1).incrementNumTenants();
			return animationPanel.getGui().buildingList.get(1);
		} else {
			God.Get().getBHouse(4).incrementNumTenants();
			return animationPanel.getGui().buildingList.get(4);
		}
	}

	private Building selectHouse() {
		if (God.Get().getBHouse(12).numTenants < 8) {
			God.Get().getBHouse(12).incrementNumTenants();
			return animationPanel.getGui().buildingList.get(12);
		} else if (God.Get().getBHouse(13).numTenants < 8) {
			God.Get().getBHouse(13).incrementNumTenants();
			return animationPanel.getGui().buildingList.get(13);
		} else if (God.Get().getBHouse(12).numTenants < 8) {
			God.Get().getBHouse(14).incrementNumTenants();
			return animationPanel.getGui().buildingList.get(14);
		} else {
			God.Get().getBHouse(15).incrementNumTenants();
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
			building = 9;
			break;
		case "Jesse":
			building = 7;
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

	public void runScenarioOne() {

		createDannyRestaurantPeople(Vehicle.walk);

		createEricRestaurantPeople(Vehicle.walk);

		createBrianRestaurantPeople(Vehicle.walk);

		createJesseRestaurantPeople(Vehicle.walk);

		createTimRestaurantPeople(Vehicle.walk);

		createBankPeople(Vehicle.walk);

		createMarketPeople(Vehicle.walk);
	}

	private void runScenarioTwo() {
		createDannyRestaurantPeople(Vehicle.walk);

		createEricRestaurantPeople(Vehicle.car);

		createBrianRestaurantPeople(Vehicle.bus);

		createJesseRestaurantPeople(Vehicle.walk);

		createTimRestaurantPeople(Vehicle.car);

		createBankPeople(Vehicle.bus);

		createMarketPeople(Vehicle.walk);
	}

	private void runScenarioThree() {

	}

	private void runScenarioFour() {

	}

	private void createDannyRestaurantPeople(Vehicle vehicle) {
		// housing building 0
		animationPanel.createPerson("D_Host_One", "restaurant.DannyHost",
				vehicle, Morality.good,
				animationPanel.getGui().buildingList.get(0),
				animationPanel.getGui().buildingList.get(9), 1);
		animationPanel.createPerson("D_Host_Two", "restaurant.DannyHost",
				vehicle, Morality.good,
				animationPanel.getGui().buildingList.get(0),
				animationPanel.getGui().buildingList.get(9), 2);
		animationPanel.createPerson("D_Cashier_One", "restaurant.DannyCashier",
				vehicle, Morality.good,
				animationPanel.getGui().buildingList.get(0),
				animationPanel.getGui().buildingList.get(9), 1);
		animationPanel.createPerson("D_Cashier_Two", "restaurant.DannyCashier",
				vehicle, Morality.good,
				animationPanel.getGui().buildingList.get(0),
				animationPanel.getGui().buildingList.get(9), 2);
		animationPanel.createPerson("D_Cook_One", "restaurant.DannyCook",
				vehicle, Morality.good,
				animationPanel.getGui().buildingList.get(0),
				animationPanel.getGui().buildingList.get(9), 1);
		animationPanel.createPerson("D_Cook_Two", "restaurant.DannyCook",
				vehicle, Morality.good,
				animationPanel.getGui().buildingList.get(0),
				animationPanel.getGui().buildingList.get(9), 1);
		animationPanel.createPerson("D_Waiter_One", "restaurant.DannyWaiter",
				vehicle, Morality.good,
				animationPanel.getGui().buildingList.get(0),
				animationPanel.getGui().buildingList.get(9), 1);
		animationPanel.createPerson("D_Waiter_Two", "restaurant.DannyWaiter",
				vehicle, Morality.good,
				animationPanel.getGui().buildingList.get(0),
				animationPanel.getGui().buildingList.get(9), 2);
		// housing building 1
		animationPanel.createPerson("D_Customer", "restaurant.DannyCustomer",
				vehicle, Morality.good,
				animationPanel.getGui().buildingList.get(1),
				animationPanel.getGui().buildingList.get(9), 1);
	}

	private void createEricRestaurantPeople(Vehicle vehicle) {
		animationPanel.createPerson("E_Host_One", "EricRestaurant.EricHost",
				vehicle, Morality.good,
				animationPanel.getGui().buildingList.get(1),
				animationPanel.getGui().buildingList.get(11), 1);
		animationPanel.createPerson("E_Host_Two", "EricRestaurant.EricHost",
				vehicle, Morality.good,
				animationPanel.getGui().buildingList.get(1),
				animationPanel.getGui().buildingList.get(11), 2);
		animationPanel.createPerson("E_Customer",
				"EricRestaurant.EricCustomer", vehicle, Morality.good,
				animationPanel.getGui().buildingList.get(1),
				animationPanel.getGui().buildingList.get(11), 1);
		animationPanel.createPerson("E_Waiter_One",
				"EricRestaurant.EricWaiter", vehicle, Morality.good,
				animationPanel.getGui().buildingList.get(1),
				animationPanel.getGui().buildingList.get(11), 1);
		animationPanel.createPerson("E_Waiter_Two",
				"EricRestaurant.EricWaiter", vehicle, Morality.good,
				animationPanel.getGui().buildingList.get(1),
				animationPanel.getGui().buildingList.get(11), 2);
		animationPanel.createPerson("E_Cashier_One",
				"EricRestaurant.EricCashier", vehicle, Morality.good,
				animationPanel.getGui().buildingList.get(1),
				animationPanel.getGui().buildingList.get(11), 1);
		animationPanel.createPerson("E_Cashier_Two",
				"EricRestaurant.EricCashier", vehicle, Morality.good,
				animationPanel.getGui().buildingList.get(1),
				animationPanel.getGui().buildingList.get(11), 2);
		// housing building 4
		animationPanel.createPerson("E_Cook_One", "EricRestaurant.EricCook",
				vehicle, Morality.good,
				animationPanel.getGui().buildingList.get(4),
				animationPanel.getGui().buildingList.get(11), 1);
		animationPanel.createPerson("E_Cook_Two", "EricRestaurant.EricCook",
				vehicle, Morality.good,
				animationPanel.getGui().buildingList.get(4),
				animationPanel.getGui().buildingList.get(11), 2);
	}

	private void createBrianRestaurantPeople(Vehicle vehicle) {
		animationPanel.createPerson("B_Host_One", "brianRest.BrianHostRole",
				vehicle, Morality.good,
				animationPanel.getGui().buildingList.get(4),
				animationPanel.getGui().buildingList.get(6), 1);
		animationPanel.createPerson("B_Host_Two", "brianRest.BrianHostRole",
				vehicle, Morality.good,
				animationPanel.getGui().buildingList.get(4),
				animationPanel.getGui().buildingList.get(6), 2);
		animationPanel.createPerson("B_Customer",
				"brianRest.BrianCustomerRole", vehicle, Morality.good,
				animationPanel.getGui().buildingList.get(4),
				animationPanel.getGui().buildingList.get(6), 1);
		animationPanel.createPerson("B_Waiter_One",
				"brianRest.BrianWaiterRole", vehicle, Morality.good,
				animationPanel.getGui().buildingList.get(4),
				animationPanel.getGui().buildingList.get(6), 1);
		animationPanel.createPerson("B_Waiter_Two",
				"brianRest.BrianWaiterRole", vehicle, Morality.good,
				animationPanel.getGui().buildingList.get(4),
				animationPanel.getGui().buildingList.get(6), 2);
		animationPanel.createPerson("B_Cashier_One",
				"brianRest.BrianCashierRole", vehicle, Morality.good,
				animationPanel.getGui().buildingList.get(4),
				animationPanel.getGui().buildingList.get(6), 1);
		// housing building 12
		animationPanel.createPerson("B_Cashier_Two",
				"brianRest.BrianCashierRole", vehicle, Morality.good,
				animationPanel.getGui().buildingList.get(12),
				animationPanel.getGui().buildingList.get(6), 2);
		animationPanel.createPerson("B_Cook_One", "brianRest.BrianCookRole",
				vehicle, Morality.good,
				animationPanel.getGui().buildingList.get(12),
				animationPanel.getGui().buildingList.get(6), 1);
		animationPanel.createPerson("B_Cook_One", "brianRest.BrianCookRole",
				vehicle, Morality.good,
				animationPanel.getGui().buildingList.get(12),
				animationPanel.getGui().buildingList.get(6), 2);
	}

	private void createJesseRestaurantPeople(Vehicle vehicle) {
		animationPanel.createPerson("J_Host_One", "jesseRest.JesseHost",
				vehicle, Morality.good,
				animationPanel.getGui().buildingList.get(12),
				animationPanel.getGui().buildingList.get(7), 1);
		animationPanel.createPerson("J_Host_Two", "jesseRest.JesseHost",
				vehicle, Morality.good,
				animationPanel.getGui().buildingList.get(12),
				animationPanel.getGui().buildingList.get(7), 2);
		animationPanel.createPerson("J_Customer", "jesseRest.JesseCustomer",
				vehicle, Morality.good,
				animationPanel.getGui().buildingList.get(12),
				animationPanel.getGui().buildingList.get(7), 1);
		animationPanel.createPerson("J_Waiter_One", "jesseRest.JesseWaiter",
				vehicle, Morality.good,
				animationPanel.getGui().buildingList.get(12),
				animationPanel.getGui().buildingList.get(7), 1);
		animationPanel.createPerson("J_Waiter_Two", "jesseRest.JesseWaiter",
				vehicle, Morality.good,
				animationPanel.getGui().buildingList.get(12),
				animationPanel.getGui().buildingList.get(7), 2);
		// housing building 13
		animationPanel.createPerson("J_Cashier_One", "jesseRest.JesseCashier",
				vehicle, Morality.good,
				animationPanel.getGui().buildingList.get(13),
				animationPanel.getGui().buildingList.get(7), 1);
		animationPanel.createPerson("J_Cashier_Two", "jesseRest.JesseCashier",
				vehicle, Morality.good,
				animationPanel.getGui().buildingList.get(13),
				animationPanel.getGui().buildingList.get(7), 2);
		animationPanel.createPerson("J_Cook_One", "jesseRest.JesseCook",
				vehicle, Morality.good,
				animationPanel.getGui().buildingList.get(13),
				animationPanel.getGui().buildingList.get(7), 1);
		animationPanel.createPerson("J_Cook_Two", "jesseRest.JesseCook",
				vehicle, Morality.good,
				animationPanel.getGui().buildingList.get(13),
				animationPanel.getGui().buildingList.get(7), 2);
	}

	private void createTimRestaurantPeople(Vehicle vehicle) {
		animationPanel.createPerson("T_Host_One", "timRest.TimHostRole",
				vehicle, Morality.good,
				animationPanel.getGui().buildingList.get(13),
				animationPanel.getGui().buildingList.get(10), 1);
		animationPanel.createPerson("T_Host_Two", "timRest.TimHostRole",
				vehicle, Morality.good,
				animationPanel.getGui().buildingList.get(13),
				animationPanel.getGui().buildingList.get(10), 2);
		animationPanel.createPerson("T_Host_One", "timRest.TimWaiterRole",
				vehicle.walk, Morality.good,
				animationPanel.getGui().buildingList.get(13),
				animationPanel.getGui().buildingList.get(10), 1);
		animationPanel.createPerson("T_Host_Two", "timRest.TimWaiterRole",
				vehicle.walk, Morality.good,
				animationPanel.getGui().buildingList.get(13),
				animationPanel.getGui().buildingList.get(10), 2);
		// housing building 14
		animationPanel.createPerson("T_Cook_One", "timRest.TimCookRole",
				vehicle, Morality.good,
				animationPanel.getGui().buildingList.get(14),
				animationPanel.getGui().buildingList.get(10), 1);
		animationPanel.createPerson("T_Cook_Two", "timRest.TimCookRole",
				vehicle, Morality.good,
				animationPanel.getGui().buildingList.get(14),
				animationPanel.getGui().buildingList.get(10), 2);
		animationPanel.createPerson("T_Cashier_One", "timRest.TimCashierRole",
				vehicle, Morality.good,
				animationPanel.getGui().buildingList.get(14),
				animationPanel.getGui().buildingList.get(10), 1);
		animationPanel.createPerson("T_Cashier_Two", "timRest.TimCashierRole",
				vehicle, Morality.good,
				animationPanel.getGui().buildingList.get(14),
				animationPanel.getGui().buildingList.get(10), 2);
		animationPanel.createPerson("T_Customer", "timRest.TimCustomerRole",
				vehicle, Morality.good,
				animationPanel.getGui().buildingList.get(14),
				animationPanel.getGui().buildingList.get(10), 1);
	}

	private void createBankPeople(Vehicle vehicle) {
		animationPanel.createPerson("BankManager_One", "Bank.bankManagerRole",
				vehicle, Morality.good,
				animationPanel.getGui().buildingList.get(14),
				animationPanel.getGui().buildingList.get(2), 1);
		animationPanel.createPerson("BankManager_Two", "Bank.bankManagerRole",
				vehicle, Morality.good,
				animationPanel.getGui().buildingList.get(14),
				animationPanel.getGui().buildingList.get(2), 2);
		animationPanel.createPerson("BankTeller_One", "Bank.tellerRole",
				vehicle, Morality.good,
				animationPanel.getGui().buildingList.get(14),
				animationPanel.getGui().buildingList.get(2), 1);
		// housing building 15
		animationPanel.createPerson("BankTeller_Two", "Bank.tellerRole",
				vehicle, Morality.good,
				animationPanel.getGui().buildingList.get(15),
				animationPanel.getGui().buildingList.get(2), 2);
		animationPanel.createPerson("BankCustomer", "Bank.bankCustomerRole",
				vehicle, Morality.good,
				animationPanel.getGui().buildingList.get(15),
				animationPanel.getGui().buildingList.get(2), 1);
		animationPanel.createPerson("BankGuard_One", "Bank.bankGuardRole",
				vehicle, Morality.good,
				animationPanel.getGui().buildingList.get(15),
				animationPanel.getGui().buildingList.get(2), 1);
		animationPanel.createPerson("BankGuard_Two", "Bank.bankGuardRole",
				vehicle, Morality.good,
				animationPanel.getGui().buildingList.get(15),
				animationPanel.getGui().buildingList.get(2), 2);
	}

	private void createMarketPeople(Vehicle vehicle) {
		animationPanel.createPerson("MarketManager_One", "market.MarketManagerRole",
				vehicle, Morality.good,
				animationPanel.getGui().buildingList.get(15),
				animationPanel.getGui().buildingList.get(3), 1);
		animationPanel.createPerson("MarketManager_Two", "market.MarketManagerRole",
				vehicle, Morality.good,
				animationPanel.getGui().buildingList.get(15),
				animationPanel.getGui().buildingList.get(3), 2);
		animationPanel.createPerson("MarketClerk_One", "market.MarketClerkRole",
				vehicle, Morality.good,
				animationPanel.getGui().buildingList.get(15),
				animationPanel.getGui().buildingList.get(3), 1);
		animationPanel.createPerson("MarketClerk_Two", "market.MarketClerkRole",
				vehicle, Morality.good,
				animationPanel.getGui().buildingList.get(15),
				animationPanel.getGui().buildingList.get(3), 2);
		// housing building -- extra people
		animationPanel.createPerson("MarketPacker_One", "market.MarketPackerRole",
				vehicle, Morality.good,
				animationPanel.getGui().buildingList.get(15),
				animationPanel.getGui().buildingList.get(3), 1);
		animationPanel.createPerson("MarketPacker_Two", "market.MarketPackerRole",
				vehicle, Morality.good,
				animationPanel.getGui().buildingList.get(14),
				animationPanel.getGui().buildingList.get(3), 2);
		animationPanel.createPerson("MarketDelivery_One",
				"market.MarketDeliveryPersonRole", vehicle, Morality.good,
				animationPanel.getGui().buildingList.get(13),
				animationPanel.getGui().buildingList.get(3), 1);
		animationPanel.createPerson("MarketDelivery_Two",
				"market.MarketDeliveryPersonRole", vehicle, Morality.good,
				animationPanel.getGui().buildingList.get(12),
				animationPanel.getGui().buildingList.get(3), 2);
		animationPanel.createPerson("MarketCustomer",
				"market.MarketCustomerRole", vehicle, Morality.good,
				animationPanel.getGui().buildingList.get(15),
				animationPanel.getGui().buildingList.get(3), 1);
	}
}
