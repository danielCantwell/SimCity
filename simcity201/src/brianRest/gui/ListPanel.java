package restaurant.gui;

import javax.swing.*;

import java.awt.*;
import java.awt.event.*;
import java.util.List;
import java.util.ArrayList;

/**
 * Subpanel of restaurantPanel.
 * This holds the scroll panes for the customers and, later, for waiters
 */
public class ListPanel extends JPanel implements ActionListener {

    public JScrollPane pane =
            new JScrollPane(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
                    JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
    private JPanel view = new JPanel();
    private List<JButton> list = new ArrayList<JButton>();
    private JButton addPersonB;

    private RestaurantPanel restPanel;
    private String type;
    
    private JPanel upperPanel = new JPanel();
    private JPanel lowerPanel = new JPanel();
    
    private JTextField nameField;
    private JLabel nameLabel;
    private JCheckBox hungryBox; //hungry box
    
    private final int nameFieldDimesionWidth = 150;
    private final int nameFieldDimensionHeight = 25;

    boolean hungryBoxAvailable = false;
    /**
     * Constructor for ListPanel.  Sets up all the gui
     *
     * @param rp   reference to the restaurant panel
     * @param type indicates if this is for customers or waiters
     */
    public ListPanel(RestaurantPanel rp, String type, boolean hungryBoxAvailable) {
        restPanel = rp;
        this.type = type;
        
        this.hungryBoxAvailable = hungryBoxAvailable;    
        addPersonB = new JButton("Add " + type);
        
        Dimension dim = new Dimension(restPanel.gui.WINDOWX/4, (int)(restPanel.gui.WINDOWY/2.5f));
        setLayout(new BoxLayout((Container) this, BoxLayout.Y_AXIS));
        setPreferredSize(dim);
        setMinimumSize(dim);
        setMaximumSize(dim);
        
        initializeGUI();
        
        upperPanel.add(nameLabel);
        upperPanel.add(nameField);    
        if (hungryBoxAvailable)
        lowerPanel.add(hungryBox);        
        lowerPanel.add(addPersonB);
        
        add(upperPanel);
        add(lowerPanel); 

        view.setLayout(new BoxLayout((Container) view, BoxLayout.Y_AXIS));
        pane.setViewportView(view);
        add(pane);
        
    }
    
    private void initializeGUI(){
    	//Name label
    	nameLabel = new JLabel("<html><b>" + type + "</html>");
        nameLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        //Name Field
        nameField = new JTextField();
        //nameField.setAlignmentX(Component.CENTER_ALIGNMENT);
        Dimension nameDim = new Dimension(nameFieldDimesionWidth,nameFieldDimensionHeight);
        nameField.setPreferredSize(nameDim);
        nameField.setMinimumSize(nameDim);
        nameField.setMaximumSize(nameDim);
        
        nameField.addKeyListener(new KeyListener(){
        	public void keyTyped(KeyEvent e){
        		
        	}
        	public void keyPressed (KeyEvent e){
        		
        	}
        	public void keyReleased (KeyEvent e){
        			if (hungryBoxAvailable)
	        		if (nameField.getText().length() == 0){
	        			hungryBox.setEnabled(false);
	        		}
	        		else hungryBox.setEnabled(true);
        	}
        });
        
        //Upper panel (where they add a new customer)
        Dimension custPanelDim = new Dimension(restPanel.gui.WINDOWX/2,restPanel.gui.WINDOWY/16);
        upperPanel.setPreferredSize(custPanelDim);
        upperPanel.setMinimumSize(custPanelDim);
        upperPanel.setMaximumSize(custPanelDim);
        upperPanel.setLayout(new BoxLayout(upperPanel, BoxLayout.X_AXIS));
        
        Dimension custPanelDim2 = new Dimension(restPanel.gui.WINDOWX/2,restPanel.gui.WINDOWY/16);
        lowerPanel.setPreferredSize(custPanelDim2);
        lowerPanel.setMinimumSize(custPanelDim2);
        lowerPanel.setMaximumSize(custPanelDim2);
        lowerPanel.setLayout(new BoxLayout(lowerPanel, BoxLayout.X_AXIS));

        
        //Hungry box
        if (hungryBoxAvailable){
        	hungryBox = new JCheckBox("Hungry?");
        	hungryBox.setEnabled(false);
        }
        
        //Add Person Button
        addPersonB.addActionListener(this);
        addPersonB.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        
    	
    }

    /**
     * Method from the ActionListener interface.
     * Handles the event of the add button being pressed
     */
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == addPersonB) {
        	// Chapter 2.19 describes showInputDialog()
            
            //Add the person
            if (nameField.getText().trim().length() != 0){
            	addPerson(nameField.getText()); 
            	
            	if (type == "Waiters"){
                	restPanel.addWaiter(nameField.getText());
                }
            }
            
            
        }
        else {
        	// Isn't the second for loop more beautiful?
        	for (JButton temp:list){
                if (e.getSource() == temp)
                    restPanel.showInfo(type, temp.getText());
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
        if (name != "") {
        	
            JButton button = new JButton(name);
            button.setBackground(Color.white);

            Dimension paneSize = pane.getSize();
            Dimension buttonSize = new Dimension(paneSize.width - 20,
                    (int) (paneSize.height / 7));
            button.setPreferredSize(buttonSize);
            button.setMinimumSize(buttonSize);
            button.setMaximumSize(buttonSize);
            button.addActionListener(this);
            list.add(button);
            view.add(button);
            if (hungryBoxAvailable) restPanel.addPerson(type, name, hungryBox.isSelected());
            else restPanel.addPerson(type, name, false);
            restPanel.showInfo(type, name);//puts hungry button on panel
            validate();
        }
    }
    
}
