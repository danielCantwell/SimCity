package exterior.gui;

import housing.gui.HousingAnimation;

import javax.swing.*;

import market.gui.MarketAnimationPanel;
import Bank.gui.bankGui;
import SimCity.Base.Building;
import SimCity.Base.God;
import SimCity.Buildings.B_Bank;
import SimCity.Buildings.B_BrianRestaurant;
import SimCity.Buildings.B_House;
import SimCity.Buildings.B_Market;
import SimCity.Buildings.B_DannyRestaurant;
import SimCity.Buildings.B_EricRestaurant;

import java.awt.*;
import java.util.ArrayList;

/**
 * Main GUI class.
 * Contains the main frame and subsequent panels
 */
public class SimCityGui extends JFrame {
	public AnimationPanel animationPanel = new AnimationPanel();
	public ArrayList<Building> buildingList = new ArrayList<Building>();
	CardLayout cardLayout = new CardLayout();
	JPanel buildingPanels = new JPanel();
	ArrayList<JPanel> buildingPanelList = new ArrayList<JPanel>();
	public JFrame buildingFrame;
	
	public SetupPanel setupPanel;
	
    public SimCityGui() {
    	setupPanel = new SetupPanel(animationPanel);
    	
        int WINDOWX = 1920;
        int WINDOWY = 1920; //1472;
        int BFRAMEX = 640;
        int BFRAMEY = 640;
    	setBounds(0, 0, WINDOWX, WINDOWY);
    	setLayout(new BorderLayout());
    	
    	God.Get().setSimGui(this);
    	
    	Dimension animDim = new Dimension(WINDOWX, WINDOWY);
    	animationPanel.setPreferredSize(animDim);
    	animationPanel.setMinimumSize(animDim);
    	animationPanel.setMaximumSize(animDim);
    	animationPanel.setGui(this);

    	JScrollPane scrollPane = new JScrollPane(animationPanel);
    	add(scrollPane, BorderLayout.CENTER);
    	animationPanel.setScrollPane(scrollPane);
    	
    	buildingPanels.setLayout(cardLayout);
    	buildingPanels.setMinimumSize(new Dimension(500, 250));
    	buildingPanels.setMaximumSize(new Dimension(500, 250));
    	buildingPanels.setPreferredSize(new Dimension(500, 250));
    	buildingPanels.setBackground(Color.yellow);
    	
    	//Create the BuildingPanel for each Building object
    	for (int i = 0; i < 16; i++) {
    		Building b = null;
    		JPanel buildingPanel = null;
    		// Add Apartments:
    		if (i == 0 || i == 1 || i == 4) {
        		buildingPanel = new HousingAnimation();
      			b = new B_House("Apartment", i, buildingPanel, ((i % 4) * 7 + 3)*64, ((i / 4) * 7 + 3)*64);
    		}
    		// Add Banks:
    		if (i == 2 || i == 8) {
        		buildingPanel = new bankGui();
        		b = new B_Bank(i, buildingPanel, ((i % 4) * 7 + 3)*64, ((i / 4) * 7 + 3)*64);
    		}
    		// Add Markets:
    		if (i == 3 || i == 5) {
        		buildingPanel = new MarketAnimationPanel("Costco");
        		b = new B_Market(i, buildingPanel, ((i % 4) * 7 + 3)*64, ((i / 4) * 7 + 3)*64);
        		((MarketAnimationPanel) buildingPanel).setBMarket((B_Market)b);
    		}
    		// Add Restaurants:
    		if (i==6){
    			buildingPanel = new brianRest.gui.BrianAnimationPanel();
    			b = new B_BrianRestaurant(i, buildingPanel, ((i % 4) * 7 + 3)*64, ((i / 4) * 7 + 3)*64);
    		}
    		if (i == 7) {
        		buildingPanel = new restaurant.gui.DannyRestaurantAnimationPanel();
        		b = new B_DannyRestaurant(i, buildingPanel, ((i % 4) * 7 + 3)*64, ((i / 4) * 7 + 3)*64);
    		}
    		if (i == 9 || i == 10) {
        		buildingPanel = new restaurant.gui.DannyRestaurantAnimationPanel();
        		b = new B_DannyRestaurant(i, buildingPanel, ((i % 4) * 7 + 3)*64, ((i / 4) * 7 + 3)*64);
    		}
    		if(i == 11) {
    			buildingPanel = new EricRestaurant.gui.AnimationPanel();
        		b = new B_EricRestaurant(i, buildingPanel, ((i % 4) * 7 + 3)*64, ((i / 4) * 7 + 3)*64);
    		}
    		// Add Houses:
    		if (i == 12 || i == 13 || i == 14 || i == 15) {
        		buildingPanel = new HousingAnimation();
        		b = new B_House("House", i, buildingPanel, ((i % 4) * 7 + 3)*64, ((i / 4) * 7 + 3)*64);
    		}
    		if (b != null && buildingPanel != null)
    		{
    			buildingPanelList.add(buildingPanel);
    			buildingPanels.add(buildingPanel, "" + i);
    			buildingList.add(b);
    			God.Get().addBuilding(b);
    		}
    		else
    		{
    			System.err.println("Building not initialized correctly in SimCityGui.java!");
    		}
    	}
    	
    	buildingFrame = new JFrame();
    	buildingFrame.add(buildingPanels);
    	buildingFrame.setTitle("Building");
    	buildingFrame.setVisible(false);
        buildingFrame.setResizable(false);
        buildingFrame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        
    	Dimension bframeDim = new Dimension(BFRAMEX, BFRAMEY);
    	buildingFrame.setPreferredSize(bframeDim);
    	buildingFrame.setMinimumSize(bframeDim);
    	buildingFrame.setMaximumSize(bframeDim);
    }
    
    public static void main(String[] args) {
        SimCityGui gui = new SimCityGui();
        gui.setTitle("SimCity 201: Team 33");
        gui.setVisible(true);
        gui.setResizable(true);
        gui.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
}
