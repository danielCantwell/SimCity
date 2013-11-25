package market.gui;

import javax.swing.*;

import exterior.gui.SimCityGui;
import market.MarketClerkRole;
import market.MarketDeliveryPersonRole;
import market.MarketManagerRole;
import market.MarketPackerRole;
import SimCity.Base.God;
import SimCity.Base.Person;
import SimCity.Buildings.B_Restaurant;
import SimCity.gui.Gui;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;

public class MarketAnimationPanel extends JPanel implements ActionListener {

		/**
		 * 
		 * @author Timothy So
		 * The inner view of a market building for SimCity201, Team 33
		 *
		 */
		private static final long serialVersionUID = 6251178680876605372L;
		
		private SimCityGui cityGui;
		
		private String name;
		
		private final int WINDOWX = 640;
		private final int WINDOWY = 640;
		
		private final int xPersonSize	= 20;
		private final int yPersonSize	= 20;
	
		static final int SPEED = 5;
	
		private Image bufferImage;
		private Dimension bufferSize;
	
		private Person manager;
    private Person clerk;
    private Person packer;
    private Person deliveryPerson;
    
    private B_Restaurant chanos;
    
    private List<Person> people = Collections.synchronizedList(new ArrayList<Person>());

		//public List<MarketPackerGui> packers = Collections.synchronizedList(new ArrayList<MarketPackerGui>());
		//public List<MarketClerkGui> clerks = Collections.synchronizedList(new ArrayList<MarketClerkGui>());
		//public List<MarketDeliveryPersonGui> deliveryPeople = Collections.synchronizedList(new ArrayList<MarketDeliveryPersonGui>());
	
		private List<Gui> guis = Collections.synchronizedList(new ArrayList<Gui>());
		public MarketAnimationPanel(String name)
		{
				setSize(WINDOWX, WINDOWY);
				setVisible(true);
		        Timer timer = new Timer(10, this);
		        timer.start();
				
				this.name = name;
        
        manager = new Person("market.MarketManagerRole");
        addGui(manager.gui);
        //manager.msgCreateRole(managerRole);
        manager.startThread();

        clerk = new Person("market.MarketClerkRole");
        addGui(clerk.gui);
        ((MarketClerkRole) clerk.mainRole).setManager((MarketManagerRole) manager.mainRole);
        clerk.startThread();
        
        packer = new Person("market.MarketPackerRole");
        addGui(packer.gui);
        ((MarketPackerRole) packer.mainRole).setManager((MarketManagerRole) manager.mainRole);
        packer.startThread();
        
        deliveryPerson = new Person("market.MarketDeliveryPersonRole");
        addGui(deliveryPerson.gui);
        ((MarketDeliveryPersonRole) deliveryPerson.mainRole).setManager((MarketManagerRole) manager.mainRole);
        deliveryPerson.startThread();

        initializeLocations();
        
        ((MarketManagerRole) manager.mainRole).addClerk((MarketClerkRole) clerk.mainRole);
        ((MarketManagerRole) manager.mainRole).addPacker((MarketPackerRole) packer.mainRole);
        ((MarketManagerRole) manager.mainRole).addDeliveryPerson((MarketDeliveryPersonRole) deliveryPerson.mainRole);
        
    		/*chanos = (B_Restaurant) God.Get().findRandomRestaurant();
        ((MarketManagerRole) manager.mainRole).msgWantFood(chanos.getID(), "Steak", 10);
        ((MarketManagerRole) manager.mainRole).msgWantFood(chanos.getID(), "Salad", 10);
        ((MarketManagerRole) manager.mainRole).msgWantFood(chanos.getID(), "Tomatoes", 10);
        */bufferSize = this.getSize();
	}

	public void actionPerformed(ActionEvent e) {
		repaint(); // Will have paintComponent called
	}

	public void paintComponent(Graphics g) {
		Graphics2D g2 = (Graphics2D) g;

		// Clear the screen by painting a rectangle the size of the frame
		g2.setColor(Color.LIGHT_GRAY);
		g2.fillRect(0, 0, WINDOWX, WINDOWY);

		g2.setColor(Color.YELLOW);
		// counter
		g2.fillRect(0, 150, 190, 10);
        g2.fillRect(180, 160, 10, 200);
        g2.fillRect(180, 360, 460, 10);
        
        // shelves
        g2.fillRect(200, 40, 160, 20);
        g2.fillRect(400, 40, 160, 20);
        g2.fillRect(400, 100, 160, 20);
		
		for (Gui gui : guis) {
			if (gui.isPresent()) {
				gui.updatePosition();
			}
		}

		for (Gui gui : guis) {
			if (gui.isPresent()) {
				gui.draw(g2);
			}
		}
		
		// draw building name
		g2.setColor(Color.BLACK);
		g2.drawString(name, 20, 20);
	}

	public void addGui(MarketPackerGui gui) {
		guis.add(gui);
	}
	
	public void addGui(MarketClerkGui gui) {
		guis.add(gui);
	}
	
	public void addGui(MarketDeliveryPersonGui gui) {
		guis.add(gui);
	}
	/*
    public void addGui(MarketCustomerGui gui) {
        guis.add(gui);
    }*/
    
    public void addGui(MarketManagerGui gui) {
        guis.add(gui);
    }

    public void addGui(Gui gui) {
        guis.add(gui);
    }
    
    public void initializeLocations()
    {
        Map<Integer, Point> locations = Collections.synchronizedMap(new HashMap<Integer, Point>());
        int locCount = 0;
        for (int i = 200; i < 360; i+=20)
        {
            locations.put(locCount, new Point(i, 40));
            locCount++;
        }
        for (int i = 200; i < 360; i+=20)
        {
            locations.put(locCount, new Point(i, 50));
            locCount++;
        }
        for (int i = 400; i < 560; i+=20)
        {
            locations.put(locCount, new Point(i, 40));
            locCount++;
        }
        for (int i = 400; i < 560; i+=20)
        {
            locations.put(locCount, new Point(i, 50));
            locCount++;
        }
        for (int i = 400; i < 560; i+=20)
        {
            locations.put(locCount, new Point(i, 100));
            locCount++;
        }
        for (int i = 400; i < 560; i+=20)
        {
            locations.put(locCount, new Point(i, 110));
            locCount++;
        }
        ((MarketManagerGui) manager.gui).setLocations(locations);
        ((MarketManagerRole) manager.mainRole).initializeInventory();
        
        // test
        ((MarketPackerGui) packer.gui).setLocations(locations);
    }
    
    /**
     * Debug
     */
    
    public void debugTestRestaurant()
    {
        chanos = (B_Restaurant) God.Get().findRandomRestaurant();
    	((MarketManagerRole) manager.mainRole).msgWantFood(chanos.getID(), "Steak", 10);
    	((MarketManagerRole) manager.mainRole).msgWantFood(chanos.getID(), "Salad", 10);
    	((MarketManagerRole) manager.mainRole).msgWantFood(chanos.getID(), "Tomatoes", 10);
  	}
}
