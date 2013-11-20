package exterior.gui;

import javax.swing.*;

import java.awt.*;

/**
 * Main GUI class.
 * Contains the main frame and subsequent panels
 */
public class SimCityGui extends JFrame {
	AnimationPanel animationPanel = new AnimationPanel();

    /**
     * Constructor for RestaurantGui class.
     * Sets up all the gui components.
     */
    public SimCityGui() {
        int WINDOWX = 1920;
        int WINDOWY = 1920;
    	setBounds(0, 0, WINDOWX, WINDOWY);
    	setLayout(new BorderLayout());
    	
    	Dimension animDim = new Dimension(WINDOWX, WINDOWY);
    	animationPanel.setPreferredSize(animDim);
    	animationPanel.setMinimumSize(animDim);
    	animationPanel.setMaximumSize(animDim);
    	animationPanel.setGui(this);

    	JScrollPane scrollPane = new JScrollPane(animationPanel);
    	add(scrollPane, BorderLayout.CENTER);
    }
    
    /**
     * Main routine to get gui started
     */
    public static void main(String[] args) {
        SimCityGui gui = new SimCityGui();
        gui.setTitle("SimCity 201: Team 33");
        gui.setVisible(true);
        gui.setResizable(false);
        gui.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
}
