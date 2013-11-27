package restaurant.gui;

import restaurant.CustomerAgent;
import restaurant.WaiterAgent;
import restaurant.interfaces.Customer;
import restaurant.interfaces.Waiter;

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
	//JFrame animationFrame = new JFrame("Restaurant Animation");
	DannyRestaurantAnimationPanel animationPanel = new DannyRestaurantAnimationPanel();
	
    /* restPanel holds 2 panels
     * 1) the staff listing, menu, and lists of current customers all constructed
     *    in RestaurantPanel()
     * 2) the infoPanel about the clicked Customer (created just below)
     */    
    private RestaurantPanel restPanel = new RestaurantPanel(this);
    
    public static final int WINDOWX = 1200; 
    public static final int WINDOWY = 450; 
    private final int bigBoundX = 50;
    private final int bigBoundY = 50;
  
    private JPanel bigRestaurantPanel;
    private JPanel bigAnimationPanel;
    
    private JPanel botPanelLeft;
    private JPanel botPanelRight;
    
    private JPanel waiterPanel;
    private JLabel waiterLabel;
    private JTextField waiterNameField;
    private JButton waiterButton;
    private JPanel topWaiterPanel;
    private JPanel botWaiterPanel;
    
    //Switch from waiter to customers
    private JButton waiterView;
    private JButton customerView;
    
    /* infoPanel holds information about the clicked customer, if there is one*/
    private JPanel infoPanel;
    private JLabel infoLabel; //part of infoPanel
    private JCheckBox stateCB;//part of infoLabel
    
    private JPanel bottomPanel;
    private JPanel underBottomPanel;
    private JLabel brianLabel;
    private JButton pauseButton;
    
    //Create a list panel of waiter
    
    private final int waiterFieldX = 150;
    private final int waiterFieldY = 25;

    private Object currentPerson;/* Holds the agent that the info is about.
    								Seems like a hack */
   
    /**
     * Constructor for RestaurantGui class.
     * Sets up all the gui components.
     */
    public RestaurantGui() {
        
        //----------------------- *Finds the host. ------------------ Important step: Caching the host in the AnimationPanel
        animationPanel.setHost(restPanel.getHost());
        
        //Makes a new of everything
        bigRestaurantPanel = new JPanel();
        bigAnimationPanel = new JPanel();
        botPanelLeft = new JPanel();
        botPanelRight = new JPanel();
        waiterPanel = new JPanel();
        waiterLabel = new JLabel("<html>" + "New Waiter Name: " + "</b></html>");
        waiterNameField = new JTextField();
        waiterButton = new JButton("Add Waiter");
        bottomPanel = new JPanel();
        underBottomPanel = new JPanel();
        pauseButton = new JButton("Pause");
        topWaiterPanel = new JPanel();
        botWaiterPanel = new JPanel();
        
        //Switch views between waiter and customer
        waiterView = new JButton("Mange Waiters");
        customerView = new JButton("Manage Customers");
        
        //Initialize add waiter button
        waiterButton.addActionListener(this);
        
   
        //-------------------------------
        setBounds(bigBoundX,bigBoundY,WINDOWX , WINDOWY);
        setLayout(new BoxLayout((Container) getContentPane(), BoxLayout.X_AXIS));
        bigRestaurantPanel.setLayout(new BoxLayout(bigRestaurantPanel, BoxLayout.Y_AXIS));
        bigAnimationPanel.setLayout(new BoxLayout(bigAnimationPanel, BoxLayout.Y_AXIS));
        
        //Sets all the dimensions of the panels.
        initializePanels();
        
        //All adds below
        addTo(animationPanel, bigAnimationPanel);
        addTo(restPanel, bigRestaurantPanel);
        addTo(infoLabel, infoPanel);
        addTo(stateCB, infoPanel);
        addTo (infoPanel, botPanelLeft);
        addTo(brianLabel, underBottomPanel);
        addTo(pauseButton, underBottomPanel);
        //addTo(topWaiterPanel, waiterPanel);
        //addTo(botWaiterPanel, waiterPanel);
        
        //Waiter view button.
        addTo(customerView, waiterPanel);
        addTo(waiterView, waiterPanel);
  
        //addTo(waiterListPanel, waiterPanel);
        addTo(waiterPanel, botPanelRight);
        addTo(botPanelLeft, bottomPanel);
        addTo(botPanelRight, bottomPanel);
        addTo(bottomPanel, bigRestaurantPanel);
        addTo(underBottomPanel, bigRestaurantPanel);
        add(bigRestaurantPanel);
        add(bigAnimationPanel);
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
          //Should checkmark be there? 
            stateCB.setSelected(customer.getGui().isHungry());
          //Is customer hungry? Hack. Should ask customerGui
            stateCB.setEnabled(!customer.getGui().isHungry());
          // Hack. Should ask customerGui
            infoLabel.setText(
               "<html> <b><u><i>Customer Information</i></u></b> <br>    Name: " + customer.getName() + "<br>Money: $"+ customer.getMoney()+" </html>");
        }
        
        if (person instanceof restaurant.WaiterAgent){
        	WaiterAgent waiter = (WaiterAgent) person;
        	stateCB.setText("Want break?");
        	stateCB.setSelected(waiter.getGUI().isOnBreak());
        	stateCB.setEnabled(!waiter.getGUI().isOnBreak());
        	infoLabel.setText( "<html> <b><u><i>Waiter Information</i></u></b> <br>    Name: " + waiter.getName() + " </html>");
        }
        infoPanel.validate();
    }
    
    private void addTo(JComponent c, JComponent d){
    	d.add(c);
    }
    
    //Initialization helper functions
    private void initializePanels(){
    	//Big panel sizes
        Dimension bigDim = new Dimension(WINDOWX/2, (int) (WINDOWY-20));
        bigRestaurantPanel.setPreferredSize(bigDim);
        bigRestaurantPanel.setMinimumSize(bigDim);
        bigRestaurantPanel.setMaximumSize(bigDim);
        bigAnimationPanel.setPreferredSize(bigDim);
        bigAnimationPanel.setMinimumSize(bigDim);
        bigAnimationPanel.setMaximumSize(bigDim);

        //Animation Panel
        animationPanel.setPreferredSize(bigDim);
        animationPanel.setMinimumSize(bigDim);
        animationPanel.setMaximumSize(bigDim);
        animationPanel.setBounds(WINDOWX/2, 50, WINDOWX/2, WINDOWY);
        
        //Restaurant panel size
        Dimension restDim = new Dimension(WINDOWX/2, (int) (WINDOWY*.5));
        restPanel.setPreferredSize(restDim);
        restPanel.setMinimumSize(restDim);
        restPanel.setMaximumSize(restDim);
        
        //Information panel size
        Dimension infoDim = new Dimension(WINDOWX, (int)(WINDOWY * .3));
        infoPanel = new JPanel();
        infoPanel.setPreferredSize(infoDim);
        infoPanel.setMinimumSize(infoDim);
        infoPanel.setMaximumSize(infoDim);
        //infoPanel.setLayout(new GridLayout(1, 2, 30, 0));
        
        //Bottom panel dimensions.
        Dimension bottomDim = new Dimension(WINDOWX/2, (int)(WINDOWY * .30));
        bottomPanel.setPreferredSize(bottomDim);
        bottomPanel.setMinimumSize(bottomDim);
        bottomPanel.setMaximumSize(bottomDim);
        bottomPanel.setLayout(new BoxLayout(bottomPanel, BoxLayout.X_AXIS));
        Dimension underBotDim = new Dimension(WINDOWX/2, (int)(WINDOWY * .1));
        underBottomPanel.setPreferredSize(underBotDim);
        underBottomPanel.setMinimumSize(underBotDim);
        underBottomPanel.setMaximumSize(underBotDim);
        underBottomPanel.setLayout(new BoxLayout(underBottomPanel, BoxLayout.X_AXIS));
        
        //Bottom Panel contains information and waiter panel
        Dimension botSeparation = new Dimension(WINDOWX/4, (int) (WINDOWY * .3));
        botPanelLeft.setPreferredSize(botSeparation);
        botPanelLeft.setMinimumSize(botSeparation);
        botPanelLeft.setMaximumSize(botSeparation);
        botPanelRight.setPreferredSize(botSeparation);
        botPanelRight.setMinimumSize(botSeparation);
        botPanelRight.setMaximumSize(botSeparation);
        botPanelRight.setLayout(new BoxLayout(botPanelRight, BoxLayout.Y_AXIS));
        botPanelLeft.setLayout(new BoxLayout(botPanelLeft, BoxLayout.Y_AXIS));
        
        //Waiter panel
        Dimension waiterDim = new Dimension(WINDOWX/2, (int)(WINDOWY * .3));
        waiterPanel.setPreferredSize(waiterDim);
        waiterPanel.setMinimumSize(waiterDim);
        waiterPanel.setMaximumSize(waiterDim);
        waiterPanel.setLayout(new BoxLayout(waiterPanel, BoxLayout.X_AXIS));
        
        Dimension waiterSplitDim = new Dimension(WINDOWX/2, (int)(WINDOWY * .3 / 3));
        topWaiterPanel.setPreferredSize(waiterSplitDim);
        topWaiterPanel.setMinimumSize(waiterSplitDim);
        topWaiterPanel.setMaximumSize(waiterSplitDim);
        Dimension waiterSplitDim2 = new Dimension(WINDOWX/2, (int)(WINDOWY * .3 / 4));
        botWaiterPanel.setPreferredSize(waiterSplitDim2);
        botWaiterPanel.setMinimumSize(waiterSplitDim2);
        botWaiterPanel.setMaximumSize(waiterSplitDim2);
        botWaiterPanel.setLayout(new BoxLayout(botWaiterPanel, BoxLayout.X_AXIS));
        topWaiterPanel.setLayout(new BoxLayout(topWaiterPanel, BoxLayout.X_AXIS));
        
        //Waiter label and text field and add button to add waiter.
        Dimension waiterNameFieldDim = new Dimension(waiterFieldX,waiterFieldY);
        waiterNameField.setPreferredSize(waiterNameFieldDim);
        waiterNameField.setMinimumSize(waiterNameFieldDim);
        waiterNameField.setMaximumSize(waiterNameFieldDim);
        topWaiterPanel.add(waiterLabel);
        topWaiterPanel.add(waiterNameField);
        botWaiterPanel.add(waiterButton);
        
        //Switch between waiter and customer view
        waiterView.addActionListener(this);
        customerView.addActionListener(this);
        
        //Initialize checkbox.
        stateCB = new JCheckBox();
        stateCB.setVisible(false);
        stateCB.addActionListener(this);
        //Initialize information label "Add to make customers"
        infoLabel = new JLabel(); 
        infoLabel.setText("<html><i><u>Customer Information</u> <br> No Customer Information Available</i></html>");
        
        //My name panel (belongs in the bottom panel
        brianLabel = new JLabel("<html><pre><i>Hi I'm Brian!</i></pre></html>");   
        ImageIcon icon = new ImageIcon("C:/Users/Brian/Documents/School/usc2013Fall/restaurant_brianych/src/Mario-icon.png");
        brianLabel.setIcon(icon);
        bottomPanel.setLayout(new BoxLayout(bottomPanel, BoxLayout.X_AXIS));
        
        //Pause Button
        initializePauseButton();
        
      //Make all the borders.
       // bigRestaurantPanel.setBorder(BorderFactory.createRaisedSoftBevelBorder());
        //bigAnimationPanel.setBorder(BorderFactory.createTitledBorder("Animation"));
       // infoPanel.setBorder(BorderFactory.createTitledBorder("Information"));
        waiterPanel.setBorder(BorderFactory.createTitledBorder("Creation Panel"));
        //topWaiterPanel.setBorder(BorderFactory.createTitledBorder(""));
        //botWaiterPanel.setBorder(BorderFactory.createTitledBorder(""));
    }
    
    private void initializePauseButton(){
    	pauseButton.addActionListener(new ActionListener() 
    	{ 
    		public void actionPerformed(ActionEvent e) {
    			if (e.getSource() == pauseButton){
    	    		if (pauseButton.getText() == "Pause"){
    	    			pauseButton.setText("Resume");
    	    		}
    	    		else
    	    		{
    	    			pauseButton.setText("Pause");
    	    		}
    	    		restPanel.Pause();
    	    	}
    		}
    	});
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
                return;
            }
            
            if (currentPerson instanceof WaiterAgent){
            	WaiterAgent w = (WaiterAgent) currentPerson;
            	w.getGUI().wantBreak(true);
            	stateCB.setEnabled(false);
            	return;
            }
            
            return;
        }
        
        
        if (e.getSource() == waiterView){
        	//System.out.println("waiver view");
        	restPanel.switchViews(2);
        	return;
        }
        if (e.getSource() == customerView){
        	restPanel.switchViews(1);
        	return;
        }
        
        if (e.getSource() == waiterButton){
			if(waiterNameField.getText().trim().length()>0){
				System.out.println(waiterNameField.getText() + " was hired.");
				restPanel.addWaiter(waiterNameField.getText());
			}
			return;
    	}
    }
    
    
    /**
     * Message sent from a customer gui to enable that customer's
     * "I'm hungry" checkbox.
     *
     * @param c reference to the customer
     */
    public void setCustomerEnabled(Customer c) {
        if (currentPerson instanceof CustomerAgent) {
            Customer cust = (Customer) currentPerson;
            if (c.equals(cust)) {
                stateCB.setEnabled(true);
                stateCB.setSelected(false);
            }
        }
    }
    
    public void setWaiterEnabled(Waiter c){
    	if (currentPerson instanceof WaiterAgent){
    		Waiter w = (Waiter) currentPerson;
    		if (c.equals(w)){
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
        gui.setTitle("csci201 Restaurant");
        gui.setVisible(true);
        gui.setResizable(false);
        gui.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
}
