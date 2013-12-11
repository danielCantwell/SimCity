package exterior.gui;

import housing.gui.HousingAnimation;

import javax.swing.*;

import timRest.gui.TimAnimationPanel;
import market.gui.MarketAnimationPanel;
import Bank.gui.bankGui;
import SimCity.Base.Building;
import SimCity.Base.God;
import SimCity.Buildings.B_Bank;
import SimCity.Buildings.B_BrianRestaurant;
import SimCity.Buildings.B_House;
import SimCity.Buildings.B_JesseRestaurant;
import SimCity.Buildings.B_Market;
import SimCity.Buildings.B_DannyRestaurant;
import SimCity.Buildings.B_EricRestaurant;
import SimCity.Buildings.B_TimRest;
import SimCity.trace.AlertLog;
import SimCity.trace.AlertTag;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.WindowEvent;
import java.io.File;
import java.util.ArrayList;

/**
 * Main GUI class.
 * Contains the main frame and subsequent panels
 */
public class SimCityGui extends JFrame{
	public AnimationPanel animationPanel = new AnimationPanel();
	public ArrayList<Building> buildingList = new ArrayList<Building>();
	CardLayout cardLayout = new CardLayout();
	JPanel buildingPanels = new JPanel();
	ArrayList<JPanel> buildingPanelList = new ArrayList<JPanel>();
	public JFrame buildingFrame;
	
	public SetupPanel setupPanel;
	
    public SimCityGui() {
    	setupPanel = new SetupPanel(animationPanel);
    	
    	// get the size of the monitor
    	Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int WINDOWX = screenSize.width-640;
        int WINDOWY = WINDOWX;
        int ANIMATIONX = 1920;
        int ANIMATIONY = 1920;
        int BFRAMEX = 640;
        int BFRAMEY = 640;
    	setBounds(640, 0, WINDOWX, WINDOWY);
    	setLayout(new BorderLayout());
    	
    	God.Get().setSimGui(this);
    	
    	Dimension animDim = new Dimension(ANIMATIONX, ANIMATIONY);
    	animationPanel.setPreferredSize(animDim);
    	animationPanel.setMinimumSize(animDim);
    	animationPanel.setMaximumSize(animDim);
    	animationPanel.setGui(this);

    	JScrollPane scrollPane = new JScrollPane(animationPanel);
    	// allows scrolling to go faster
    	scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        scrollPane.getHorizontalScrollBar().setUnitIncrement(16);
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
    			buildingPanel = new brianRest.gui.BrianRestaurantPanel();
    			b = new B_BrianRestaurant(i, buildingPanel, ((i % 4) * 7 + 3)*64, ((i / 4) * 7 + 3)*64);
    		}
    		if (i == 7) {
        		buildingPanel = new jesseRest.gui.AnimationPanel();
        		b = new B_JesseRestaurant(i, buildingPanel, ((i % 4) * 7 + 3)*64, ((i / 4) * 7 + 3)*64);
    		}
    		if (i == 9) {
        		buildingPanel = new restaurant.gui.DannyRestaurantAnimationPanel();
        		b = new B_DannyRestaurant(i, buildingPanel, ((i % 4) * 7 + 3)*64, ((i / 4) * 7 + 3)*64);
    		}
            if(i == 10) {
                buildingPanel = new timRest.gui.TimAnimationPanel();
                b = new B_TimRest(i, buildingPanel, ((i % 4) * 7 + 3)*64, ((i / 4) * 7 + 3)*64);
                ((TimAnimationPanel) buildingPanel).setBTimRest((B_TimRest)b);
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
        buildingFrame.setLocation(0, 0);
    	buildingFrame.add(buildingPanels);
    	buildingFrame.setTitle("Building");
    	buildingFrame.setVisible(false);
        buildingFrame.setResizable(false);
        buildingFrame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        
    	Dimension bframeDim = new Dimension(BFRAMEX, BFRAMEY);
    	buildingFrame.setPreferredSize(bframeDim);
    	buildingFrame.setMinimumSize(bframeDim);
    	buildingFrame.setMaximumSize(bframeDim);
    	buildingFrame.addComponentListener(new ComponentAdapter() {
	    		public void componentHidden(ComponentEvent e) {
	    			God.Get().playSound("doorclose", false);
	    		}
    		});
    }
    
    public static void main(String[] args) {
        SimCityGui gui = new SimCityGui();
        gui.setTitle("SimCity 201: Team 33");
        gui.setVisible(true);
        gui.setResizable(true);
        gui.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    int apCount = 0;
	public void HardReset(){
		/*God.Get().HardReset();
		
		animationPanel.getGui().removeAll();
		animationPanel = new AnimationPanel();*/
		switch (apCount){
		case 0: AlertLog.getInstance().logInfo(AlertTag.God, "God", "Nice try bud."); break;
		case 1: AlertLog.getInstance().logWarning(AlertTag.God, "God", "I can't believe you were stupid enough to press this again."); break;
		case 2: AlertLog.getInstance().logWarning(AlertTag.God, "God", "You know this button doesn't do shit."); break;
		case 3: AlertLog.getInstance().logError(AlertTag.God, "God", "You've got serious issues in your life."); break;
		case 4: AlertLog.getInstance().logError(AlertTag.God, "God", "How many licks does it take to get to ---- STOP PRESSING THIS BUTTON"); break;
		case 5: AlertLog.getInstance().logError(AlertTag.God, "God", "I dare you to press me again"); break;
		case 6: System.exit(0);
		default: break;
		}
		if (apCount <= 6)
			apCount ++;
		if (apCount >6)
			apCount = 0;
		
	}
}
