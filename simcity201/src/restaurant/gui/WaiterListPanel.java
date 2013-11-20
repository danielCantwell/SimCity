/**
 * 
 */
package restaurant.gui;

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
public class WaiterListPanel extends JPanel implements ActionListener {
	
	// Scroll View that contains the list of Customers
		public JScrollPane pane = new JScrollPane
				(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		private JPanel view = new JPanel();
	    private List<JButton>   nameList 	 = new ArrayList<JButton>();
	    private List<JCheckBox> checkboxList = new ArrayList<JCheckBox>();
	    
		// Directly part of the ListPanel, creating the customer
	    private JButton    addPersonButton  = new JButton("Add");
	    private JTextField enterName        = new JTextField("Name");
		
	    // Constants for the GridBagLayout
		final static boolean SHOULD_FILL     = true;
		final static boolean SHOULD_WEIGHT_X = true;
		final static boolean RIGHT_TO_LEFT   = true;
		
		private RestaurantPanel restPanel;
		private String type;
		
		/*
		 * Constructor
		 */
		public WaiterListPanel(RestaurantPanel restPanel, String type) {
			
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
			
	        Dimension addWaiterPanelSize = new Dimension(paneSize.width / 2,
	                (int) (paneSize.height / 7));
			
			// Customer Creation Panel
			JPanel addWaiterPanel = new JPanel();
			addWaiterPanel.setLayout(new GridLayout(1, 2));
			
			// Components of Customer Creation Panel
			addPersonButton.addActionListener(this);
			
			// Adding Components to Customer Creation Panel
			addWaiterPanel.add(addPersonButton);
			addWaiterPanel.add(enterName);
			
			// Add Customer Creation Panel to CustomerListPanel
			c.fill = GridBagConstraints.HORIZONTAL;
		    c.gridx = 0;
		    c.gridy = 0;
			
			add(addWaiterPanel, c);
			
			c.fill = GridBagConstraints.HORIZONTAL;
		    c.gridx = 0;
		    c.gridy = 1;
		    c.ipady = 160;
			
			// Existing Customers List Panel
			view.setLayout(new GridLayout(0, 2));
	        pane.setViewportView(view);
	        
	        add(pane, c);
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			/*
			 * If the "Add" button is clicked, a waiter will be
			 * added with the name in the enterName text box
			 */
			if (e.getSource() == addPersonButton) {
	        	addPerson(enterName.getText());
				enterName.setText(null);
	        }
			else {
				/*
				 * If a waiter in the list is selected, check if
				 * he is available (the checkbox) and show his information
				 * in the information panel
				 */
	            for (int i = 0; i < nameList.size(); i++) {
	                JButton temp = nameList.get(i);
	                if (e.getSource() == temp) {
	                	boolean available = !checkboxList.get(i).isSelected();
	                    restPanel.showInfo(type, temp.getText(), available);
	                }
	            }
	            /*
	             * If a waiter's available checkbox is clicked,
	             * show that waiter's information in the
	             * information panel
	             */
	        	for (int i = 0; i < checkboxList.size(); i++) {
	        		JCheckBox temp = checkboxList.get(i);
	        		if (e.getSource() == temp) {
	        			boolean available = !checkboxList.get(i).isSelected();
	        			restPanel.showInfo(type, nameList.get(i).getText(), available);
	        			if (checkboxList.get(i).isSelected()){
	        				restPanel.waiterGoOnBreak(nameList.get(i).getText());
	        			}
	        			else  {
	        				restPanel.waiterGoOffBreak(nameList.get(i).getText());
	        			}
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
		    public void addPerson(String name) {
		        if (name != null) {
		        	
		        	Dimension paneSize = pane.getSize();
		            Dimension buttonSize = new Dimension(paneSize.width - 75,
		                    (int) (paneSize.height / 7));
		        	
		        	// Button with customer name that will be added to the list
		            JButton nameButton = new JButton(name);
		            nameButton.setBackground(Color.white);
		            nameButton.setPreferredSize(buttonSize);
		            nameButton.setMinimumSize(buttonSize);
		            nameButton.setMaximumSize(buttonSize);
		            
		            nameButton.addActionListener(this);
		            
		            // Checkbox for the waiter that will be added to the list
		            JCheckBox availableCheckBox = new JCheckBox("Break");
		            availableCheckBox.setBackground(Color.white);
		            
		            availableCheckBox.addActionListener(this);
		            
		            // Add the button and checkbox to the data lists
		            nameList.add(nameButton);
		            checkboxList.add(availableCheckBox);
		            
		            // Add the button and checkbox to the view lists
		            view.add(nameButton);
		            view.add(availableCheckBox);
		            
		            // AddPerson to list
		            restPanel.addPerson(type, name, false);
		            // Put on break button on panel
		            restPanel.showInfo(type, name, false);
		            validate();
		        }
		    }
		    
		    public void setWaiterEnabled(String wName) {
		    	for (int i = 0; i < nameList.size(); i++) {
		    		JButton temp = nameList.get(i);
		    		if (temp.getText() == wName) {
		    			checkboxList.get(i).setSelected(false);
		    			checkboxList.get(i).setEnabled(true);
		    		}
		    	}
		    }
		    
		    public void setWaiterBreakDisabled(String wName) {
		    	for (int i = 0; i < nameList.size(); i++) {
		    		JButton temp = nameList.get(i);
		    		if (temp.getText() == wName) {
		    			checkboxList.get(i).setEnabled(false);
		    		}
		    	}
		    }
		    
		    public void setWaiterBreakEnabled(String wName) {
		    	for (int i = 0; i < nameList.size(); i++) {
		    		JButton temp = nameList.get(i);
		    		if (temp.getText() == wName) {
		    			checkboxList.get(i).setEnabled(true);
		    		}
		    	}
		    }

}
