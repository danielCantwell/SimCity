package market.gui;

import java.awt.BorderLayout;
import java.util.concurrent.Semaphore;

import javax.swing.JFrame;

import market.MarketCustomerRole;
import market.MarketDeliveryPersonRole;
import market.MarketManagerRole;
import SimCity.Base.Person;
import SimCity.Base.Person.Morality;
import SimCity.Base.Person.Vehicle;
import SimCity.Buildings.B_House;
import SimCity.Globals.Money;
import SimCity.gui.Gui;
import exterior.astar.AStarTraversal;
import exterior.gui.PersonGui;
import exterior.gui.SimCityGui;

public class TestMarketGui extends JFrame
{
    /**
     * 
     */
    private static final long serialVersionUID = 8675232995950198044L;
    private static final int TILESIZE = 64;
    SimCityGui city = new SimCityGui();
    MarketAnimationPanel animationPanel = new MarketAnimationPanel("Ralph's", city);
    
    private Semaphore[][] pedestrianGrid = new Semaphore[1920/(TILESIZE)][1920/(TILESIZE)];
    
    public TestMarketGui()
    {
        int WINDOWX = 800;
        int WINDOWY = 800;
        
        setBounds(50, 50, WINDOWX, WINDOWY);
        setLayout(new BorderLayout());
        this.add(animationPanel);
        
        AStarTraversal aStarTraversal = new AStarTraversal(pedestrianGrid);
        
        PersonGui pGui = new PersonGui(city, aStarTraversal);
        Person p = new Person("Customer", pGui, "market.MarketCustomerRole", Vehicle.walk, Morality.good, new Money(100, 0), new Money(10, 0), 20, 3, "Apartment", (B_House)city.buildingList.get(0), animationPanel.getBMarket());
        p.mainRole.setActive(true);
        ((MarketCustomerRole) p.mainRole).setManager((MarketManagerRole)animationPanel.manager.mainRole);
        Gui cGui = ((MarketCustomerRole) p.mainRole).getGui();
        pGui.setPerson(p);
        animationPanel.addGui(cGui);
        p.startThread();
    }
    
    public static void main(String[] args) {
        TestMarketGui gui = new TestMarketGui();
        gui.setTitle("csci201 Market");
        gui.setVisible(true);
        gui.setResizable(false);
        gui.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
}
