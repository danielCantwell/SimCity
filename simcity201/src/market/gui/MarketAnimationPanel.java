package market.gui;

import javax.swing.*;

import market.MarketClerkRole;
import market.MarketDeliveryPersonRole;
import market.MarketManagerRole;
import market.MarketPackerRole;
import SimCity.Base.Person;
import SimCity.gui.Gui;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Collection;
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
	
	private String name;
	
	private final int WINDOWX = 680;
	private final int WINDOWY = 680;
	
	private final int xPersonSize	= 20;
	private final int yPersonSize	= 20;

	static final int SPEED = 5;

	private Image bufferImage;
	private Dimension bufferSize;

	private MarketManagerRole managerRole;
	private MarketClerkRole clerkRole;
    private MarketPackerRole packerRole;
    private MarketDeliveryPersonRole deliveryPersonRole;
    
    private List<Person> people = Collections.synchronizedList(new ArrayList<Person>());

	//public List<MarketPackerGui> packers = Collections.synchronizedList(new ArrayList<MarketPackerGui>());
	//public List<MarketClerkGui> clerks = Collections.synchronizedList(new ArrayList<MarketClerkGui>());
	//public List<MarketDeliveryPersonGui> deliveryPeople = Collections.synchronizedList(new ArrayList<MarketDeliveryPersonGui>());

	private List<Gui> guis = Collections.synchronizedList(new ArrayList<Gui>());

	public MarketAnimationPanel(String name) {
		setSize(WINDOWX, WINDOWY);
		setVisible(true);
        Timer timer = new Timer(1, this);
        timer.start();
		
		this.name = name;
        
        Person manager = new Person("market.MarketManagerRole");
        addGui(manager.gui);
        //manager.msgCreateRole(managerRole);
        managerRole = (MarketManagerRole) manager.mainRole;
        manager.startThread();

        Person clerk = new Person("market.MarketClerkRole");
        addGui(clerk.gui);
        //clerk.msgCreateRole(clerkRole);
        clerk.startThread();
        
        Person packer = new Person("market.MarketPackerRole");
        addGui(packer.gui);
        //packer.msgCreateRole(packerRole);
        packerRole = (MarketPackerRole) packer.mainRole;
        packer.startThread();
        
        Person deliveryPerson = new Person("market.MarketDeliveryPersonRole");
        addGui(deliveryPerson.gui);
        //deliveryPerson.msgCreateRole(deliveryPersonRole);
        deliveryPerson.startThread();

        initializeLocations();
        
        managerRole.addClerk((MarketClerkRole) clerk.mainRole);
        managerRole.addPacker((MarketPackerRole) packer.mainRole);
        managerRole.addDeliveryPerson((MarketDeliveryPersonRole) deliveryPerson.mainRole);
        managerRole.msgWantFood("Chano's", "Steak", 10);
		bufferSize = this.getSize();
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
        g2.fillRect(180, 360, 500, 10);
        
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
        managerRole.getGui().setLocations(locations);
        managerRole.initializeInventory();
        
        // test
        packerRole.getGui().setLocations(locations);
    }
}
