package exterior.gui;

import housing.gui.HousingAnimation;

import javax.swing.*;

import Bank.gui.bankGui;
import SimCity.Base.Building;
import SimCity.Buildings.B_Bank;
import SimCity.Buildings.B_House;
import SimCity.Buildings.B_Market;
import SimCity.Buildings.B_Restaurant;

import java.awt.*;
import java.util.ArrayList;

/**
 * Main GUI class.
 * Contains the main frame and subsequent panels
 */
public class SimCityGui extends JFrame {
	AnimationPanel animationPanel = new AnimationPanel();
	ArrayList<Building> buildingList = new ArrayList<Building>();
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
    		// Add Apartments:
    		if (i == 0 || i == 1 || i == 4) {
        		HousingAnimation buildingPanel = new HousingAnimation();
        		buildingPanels.add(buildingPanel, "" + i);	
            	buildingList.add(new B_House(i, buildingPanel, ((i % 4) * 7 + 3)*64, ((i / 4) * 7 + 3)*64));
    		}
    		// Add Banks:
    		if (i == 2 || i == 8) {
        		bankGui buildingPanel = new bankGui();
        		buildingPanels.add(buildingPanel, "" + i);	
            	buildingList.add(new B_House(i, buildingPanel, ((i % 4) * 7 + 3)*64, ((i / 4) * 7 + 3)*64));
    		}
    		// Add Markets:
    		if (i == 3 || i == 5) {
        		JPanel buildingPanel = new JPanel();
        		buildingPanels.add(buildingPanel, "" + i);	
            	buildingList.add(new B_Market(i, buildingPanel, ((i % 4) * 7 + 3)*64, ((i / 4) * 7 + 3)*64));
    		}
    		// Add Restaurants:
    		if (i == 6 || i == 7 || i == 9 || i == 10 || i == 11) {
        		JPanel buildingPanel = new JPanel();
        		buildingPanels.add(buildingPanel, "" + i);	
            	buildingList.add(new B_Restaurant(i, buildingPanel, ((i % 4) * 7 + 3)*64, ((i / 4) * 7 + 3)*64));
    		}
    		// Add Houses:
    		if (i == 12 || i == 13 || i == 14 || i == 15) {
        		HousingAnimation buildingPanel = new HousingAnimation();
        		buildingPanels.add(buildingPanel, "" + i);	
            	buildingList.add(new B_House(i, buildingPanel, ((i % 4) * 7 + 3)*64, ((i / 4) * 7 + 3)*64));
    		}
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
