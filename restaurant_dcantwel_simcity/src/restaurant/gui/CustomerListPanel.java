/**
 * 
 */
package restaurant.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.ComponentOrientation;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;

/**
 * @author Daniel
 *
 */
public class CustomerListPanel extends JPanel implements ActionListener {
	
	// Scroll View that contains the list of Customers
	public JScrollPane pane = new JScrollPane
			(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
	private JPanel view = new JPanel();
    private List<JButton>   nameList 	 = new ArrayList<JButton>();
    private List<JCheckBox> checkboxList = new ArrayList<JCheckBox>();
    
	// Directly part of the ListPanel, creating the customer
    private JButton    addPersonButton  = new JButton("Add");
    private JTextField enterName        = new JTextField("Name");
    private JCheckBox  initiallyHungry  = new JCheckBox();
    
    private boolean startsHungry = false;
	
    // Constants for the GridBagLayout
	final static boolean SHOULD_FILL     = true;
	final static boolean SHOULD_WEIGHT_X = true;
	final static boolean RIGHT_TO_LEFT   = true;
	
	private RestaurantPanel restPanel;
	private String type;
	
	/*
	 * Constructor
	 */
	public CustomerListPanel(RestaurantPanel restPanel, String type) {
		
		this.restPanel = restPanel;
		this.type = type;
		
		// GridBagLayout Stuff
		if (RIGHT_TO_LEFT) {
            setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
        }
		
		setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		
		if (SHOULD_FILL) {
		    //natural height, maximum width
		    c.fill = GridBagConstraints.HORIZONTAL;
		}
		
		if (SHOULD_WEIGHT_X) {
			c.weightx = 0.5;
		}
		
		// GridBagLayout Stuff ^^^^^^^^
		
		Dimension paneSize = this.getSize();
		
        Dimension addCustomerPanelSize = new Dimension(paneSize.width / 2,
                (int) (paneSize.height / 7));
		
		// Customer Creation Panel
		JPanel addCustomerPanel = new JPanel();
		addCustomerPanel.setLayout(new BorderLayout());
		
		// Components of Customer Creation Panel
		addPersonButton.addActionListener(this);
		
		// Adding Components to Customer Creation Panel
		addCustomerPanel.add(BorderLayout.WEST,   addPersonButton);
		addCustomerPanel.add(BorderLayout.CENTER, enterName);
		addCustomerPanel.add(BorderLayout.EAST,   initiallyHungry);
		
		// Add Customer Creation Panel to CustomerListPanel
		c.fill = GridBagConstraints.HORIZONTAL;
	    c.gridx = 0;
	    c.gridy = 0;
		
		add(addCustomerPanel, c);
		
		c.fill = GridBagConstraints.HORIZONTAL;
	    c.gridx = 0;
	    c.gridy = 1;
	    c.ipady = 160; // view height
		
		// Existing Customers List Panel
		view.setLayout(new GridLayout(0, 2));
        pane.setViewportView(view);
        
        add(pane, c);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		/*
		 * If the "Add" button is clicked, a customer will be
		 * added with the name in the enterName text box
		 */
		if (e.getSource() == addPersonButton) {
			if (initiallyHungry.isSelected()) {
				startsHungry = true;
			} else {
				startsHungry = false;
			}
        	addPerson(enterName.getText(), startsHungry);
			enterName.setText(null);
			initiallyHungry.setSelected(false);
        }
		else {
			/*
			 * If a customer in the list is selected, check if
			 * he is hungry (the checkbox) and show his information
			 * in the information panel
			 */
            for (int i = 0; i < nameList.size(); i++) {
                JButton temp = nameList.get(i);
                if (e.getSource() == temp) {
                	boolean hungry = checkboxList.get(i).isSelected();
                    restPanel.showInfo(type, temp.getText(), hungry);
                }
            }
            /*
             * If a customer's hungry checkbox is clicked,
             * show that customer's information in the
             * information panel
             */
        	for (int i = 0; i < checkboxList.size(); i++) {
        		JCheckBox temp = checkboxList.get(i);
        		if (e.getSource() == temp) {
        			boolean hungry = checkboxList.get(i).isSelected();
        			restPanel.showInfo(type, nameList.get(i).getText(), hungry);
        			checkboxList.get(i).setEnabled(false);
        		}
        	}
		}
	}
		
		/**
	     * If the add button is pressed, this function creates
	     * a spot for it in the scroll pane, and tells the restaurant panel
	     * to add a new person.
	     *
	     * @param name name of new person
	     */
	    public void addPerson(String name, boolean isHungry) {
	        if (name != null) {
	        	
	        	Dimension paneSize = pane.getSize();
	            Dimension buttonSize = new Dimension(paneSize.width - 80,
	                    (int) (paneSize.height / 7));
	        	
	        	// Button with customer name that will be added to the list
	            JButton nameButton = new JButton(name);
	            nameButton.setBackground(Color.white);
	            nameButton.setPreferredSize(buttonSize);
	            nameButton.setMinimumSize(buttonSize);
	            nameButton.setMaximumSize(buttonSize);
	            
	            nameButton.addActionListener(this);
	            
	            // Checkbox for the customer that will be added to the list
	            JCheckBox hungryCheckBox = new JCheckBox("Hungry");
	            hungryCheckBox.setBackground(Color.white);
	            if (isHungry) {
	            	hungryCheckBox.setSelected(true);
	            	hungryCheckBox.setEnabled(false);
				}
	            
	            hungryCheckBox.addActionListener(this);
	            
	            // Add the button and checkbox to the data lists
	            nameList.add(nameButton);
	            checkboxList.add(hungryCheckBox);
	            
	            // Add the button and checkbox to the view lists
	            view.add(nameButton);
	            view.add(hungryCheckBox);
	            
	            // AddPerson to list
	            restPanel.addPerson(type, name, isHungry);
	            // Put hungry button on panel
	            restPanel.showInfo(type, name, isHungry);
	            validate();
	        }
	    }
	    
	    public void setCustomerEnabled(String cName) {
	    	for (int i = 0; i < nameList.size(); i++) {
	    		JButton temp = nameList.get(i);
	    		if (temp.getText() == cName) {
	    			checkboxList.get(i).setEnabled(true);
	    			checkboxList.get(i).setSelected(false);
	    		}
	    	}
	    }

}
