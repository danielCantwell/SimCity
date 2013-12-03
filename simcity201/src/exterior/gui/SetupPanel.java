/**
 * 
 */
package exterior.gui;

import java.awt.BorderLayout;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;

/**
 * @author Daniel
 *
 */
public class SetupPanel extends JFrame {
	
    private final int WINDOWX = 600;
    private final int WINDOWY = 600;
    
    private JTextField enterName		= new JTextField();
    private JButton createPerson		= new JButton();
    
    private JPanel optionsPanel			= new JPanel();
    private JPanel mainPanel			= new JPanel();
    
    private JPanel professionPanel		= new JPanel();
    private JPanel housingPanel			= new JPanel();
    private JPanel transportationPanel	= new JPanel();
    private JPanel moralityPanel		= new JPanel();
    private JPanel modePanel			= new JPanel();
    
    private JLabel professionLabel		= new JLabel("Profession");
    private JLabel housingLabel			= new JLabel("Housing");
    private JLabel transportationLabel	= new JLabel("Transportation");
    private JLabel moralityLabel		= new JLabel("Morality");
    private JLabel modeLabel			= new JLabel("Mode");
    
    
    // --------------- Profession ------------------
    
    private ButtonGroup profession			= new ButtonGroup();
    
    private JRadioButton bankManager		= new JRadioButton("Bank Manager");
    private JRadioButton bankTeller			= new JRadioButton("Bank Teller");
    private JRadioButton bankGuard			= new JRadioButton("Bank Guard");
    
    private JRadioButton marketManager		= new JRadioButton("Market Manager");
    private JRadioButton marketClerk		= new JRadioButton("Market Clerk");
    private JRadioButton marketPacker		= new JRadioButton("Market Packer");
    private JRadioButton marketDelivery		= new JRadioButton("Market Delivery");
    
    private JRadioButton restHost			= new JRadioButton("Restaurant Host");
    private JRadioButton restWaiter			= new JRadioButton("Restaurant Waiter");
    private JRadioButton restCook			= new JRadioButton("Restaurant Cook");
    private JRadioButton restCashier		= new JRadioButton("Restaurant Cashier");
    
    // --------------- Living Arrangement ----------
    
    private ButtonGroup housing				= new ButtonGroup();
    
    private JRadioButton houseOwner			= new JRadioButton("House Owner");
    private JRadioButton houseTenant		= new JRadioButton("House Tenant");
    private JRadioButton apartmentOwner		= new JRadioButton("Apartment Owner");
    private JRadioButton apartmentTenant	= new JRadioButton("Apartment Tenant");
    
    // --------------- Mode of Transportation ------
    
    private ButtonGroup transportation		= new ButtonGroup();
    
    private JRadioButton vehicleCar			= new JRadioButton("Car");
    private JRadioButton vehicleBus			= new JRadioButton("Bus");
    private JRadioButton vehicleWalk		= new JRadioButton("Walk");
    
    // --------------- Morality --------------------
    
    private ButtonGroup morality			= new ButtonGroup();
    
    private JRadioButton moralityGood		= new JRadioButton("Good");
    private JRadioButton moralityBad		= new JRadioButton("Bad");
    
    // --------------- Compatibility Mode ----------
    
    private ButtonGroup mode				= new ButtonGroup();
    
    private JRadioButton modeNormal			= new JRadioButton("Normal Mode");
    private JRadioButton modeCompatibility	= new JRadioButton("Compatibility Mode");
    
    
    public SetupPanel() {
    	setSize(WINDOWX, WINDOWY);
    	setBounds(50, 50, WINDOWX, WINDOWY);
    	setVisible(true);
    	setTitle("Setup");
    	
    	optionsPanel.setLayout(new BorderLayout());
    	
    	// --- Button Groups ---
    	
    	profession.add(bankManager);
    	profession.add(bankGuard);
    	profession.add(bankTeller);
    	profession.add(marketClerk);
    	profession.add(marketPacker);
    	profession.add(marketDelivery);
    	profession.add(marketManager);
    	profession.add(restCashier);
    	profession.add(restCook);
    	profession.add(restHost);
    	profession.add(restWaiter);
    	
    	housing.add(apartmentOwner);
    	housing.add(apartmentTenant);
    	housing.add(houseOwner);
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
    	
    	professionPanel.add(bankManager);
    	professionPanel.add(bankGuard);
    	professionPanel.add(bankTeller);
    	professionPanel.add(marketClerk);
    	professionPanel.add(marketPacker);
    	professionPanel.add(marketDelivery);
    	professionPanel.add(marketManager);
    	professionPanel.add(restCashier);
    	professionPanel.add(restCook);
    	professionPanel.add(restHost);
    	professionPanel.add(restWaiter);
    	
    	housingPanel.add(apartmentOwner);
    	housingPanel.add(apartmentTenant);
    	housingPanel.add(houseOwner);
    	housingPanel.add(houseTenant);
    	
    	transportationPanel.add(vehicleBus);
    	transportationPanel.add(vehicleCar);
    	transportationPanel.add(vehicleWalk);
    	
    	moralityPanel.add(moralityGood);
    	moralityPanel.add(moralityBad);
    	
    	modePanel.add(modeNormal);
    	modePanel.add(modeCompatibility);
    	
    	optionsPanel.add(professionPanel, BorderLayout.NORTH);
    	optionsPanel.add(housingPanel, BorderLayout.WEST);
    	optionsPanel.add(transportationPanel, BorderLayout.CENTER);
    	optionsPanel.add(moralityPanel, BorderLayout.EAST);
    	optionsPanel.add(modePanel, BorderLayout.SOUTH);
    	
    	// --- Main Frame ---
    	
    	add(optionsPanel);
    	add(mainPanel);
    }

}
