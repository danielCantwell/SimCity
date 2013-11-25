package exterior.gui;

import javax.swing.*;

import java.awt.*;

/**
 * Main GUI class.
 * Contains the main frame and subsequent panels
 */
public class SimCityGui extends JFrame {
	AnimationPanel animationPanel = new AnimationPanel();
	CardLayout cardLayout = new CardLayout();
	JPanel buildingPanels = new JPanel();
	
    public SimCityGui() {
        int WINDOWX = 1920;
        int WINDOWY = 1920; //1472;
    	setBounds(0, 0, WINDOWX, WINDOWY);
    	setLayout(new BorderLayout());
    	
    	Dimension animDim = new Dimension(WINDOWX, WINDOWY);
    	animationPanel.setPreferredSize(animDim);
    	animationPanel.setMinimumSize(animDim);
    	animationPanel.setMaximumSize(animDim);
    	animationPanel.setGui(this);

    	JScrollPane scrollPane = new JScrollPane(animationPanel);
    	add(scrollPane, BorderLayout.CENTER);
    	
    	buildingPanels.setLayout(cardLayout);
    	buildingPanels.setMinimumSize(new Dimension(500, 250));
    	buildingPanels.setMaximumSize(new Dimension(500, 250));
    	buildingPanels.setPreferredSize(new Dimension(500, 250));
    	buildingPanels.setBackground(Color.yellow);
    	
    	//Create the BuildingPanel for each Building object
    	for (int i = 0; i < 16; i++) {
    		JPanel buildingPanel = new JPanel();
    		JLabel tempLabel = new JLabel("" + i);
    		buildingPanel.add(tempLabel);
    		buildingPanels.add(buildingPanel, "" + i);	
    	}
    	
    	add(BorderLayout.EAST, buildingPanels);
    }
    
    public static void main(String[] args) {
        SimCityGui gui = new SimCityGui();
        gui.setTitle("SimCity 201: Team 33");
        gui.setVisible(true);
        gui.setResizable(false);
        gui.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
}
