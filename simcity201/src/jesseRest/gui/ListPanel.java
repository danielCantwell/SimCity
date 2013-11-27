package jesseRest.gui;

import javax.swing.*;

import java.awt.*;
import java.awt.event.*;
import java.util.List;
import java.util.ArrayList;

import jesseRest.JesseCustomer;
import jesseRest.JesseHost;

/**
 * Subpanel of restaurantPanel.
 * This holds the scroll panes for the customers and, later, for waiters
 */
public class ListPanel extends JPanel implements ActionListener {

    public JScrollPane pane = new JScrollPane(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
    private JPanel view = new JPanel();
    private JPanel group = new JPanel();
    private List<JButton> list = new ArrayList<JButton>();
    private JButton addPersonB = new JButton("Add");
    private JTextField textField = new JTextField();
    private JCheckBox checkBox = new JCheckBox();
    private RestaurantPanel restPanel;
    private String type;

    /**
     * Constructor for ListPanel.  Sets up all the gui
     *
     * @param rp   reference to the restaurant panel
     * @param type indicates if this is for customers or waiters
     */
    public ListPanel(RestaurantPanel rp, String type) {
        restPanel = rp;
        this.type = type;
        
        setLayout(new BoxLayout((Container) this, BoxLayout.Y_AXIS));

        textField = new JTextField();
        checkBox = new JCheckBox();
        checkBox.addActionListener(this);
        
        if (type == "Customers") {
        	checkBox.setText("Hungry?");
        } else {
        	checkBox.setVisible(false);
        }

        addPersonB.addActionListener(this);
        
        group.setLayout(new BorderLayout());
        group.add(new JLabel("<html><u>" + type + "</u><br></html>"), BorderLayout.NORTH);
        group.add(textField, BorderLayout.SOUTH);
        group.add(addPersonB, BorderLayout.WEST);
        group.add(checkBox, BorderLayout.EAST);
        add(group);
        
        view.setLayout(new BoxLayout((Container) view, BoxLayout.Y_AXIS));
        pane.setViewportView(view);
        pane.setPreferredSize(new Dimension(200,350));
        add(pane);
    }

    /**
     * Method from the ActionListener interface.
     * Handles the event of the add button being pressed
     */
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == addPersonB) {
        	if (restPanel.isPaused) {
        		System.out.println("Resume the simulation to add a person.");
        	} else {    
        		addPerson(textField.getText());
        		textField.setText("");
        	}
        }
        else {
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
        if (name != null) {
            Dimension paneSize = pane.getSize();
            Dimension buttonSize = new Dimension(paneSize.width - 20, (int) (paneSize.height / 10));
            JButton button = new JButton(name);
            button.setBackground(Color.white);
            button.setPreferredSize(buttonSize);
            button.setMinimumSize(buttonSize);
            button.setMaximumSize(buttonSize);
            button.addActionListener(this);
            list.add(button);
            view.add(button);
            restPanel.addPerson(type, name, checkBox.isSelected());
            restPanel.showInfo(type, name);
            validate();
        }
    }
}
