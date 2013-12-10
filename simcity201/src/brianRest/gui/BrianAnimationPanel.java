package brianRest.gui;

import javax.swing.*;

import brianRest.BrianTable;
import brianRest.interfaces.BrianHost;
import SimCity.Buildings.B_BrianRestaurant;
import SimCity.gui.Gui;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ConcurrentModificationException;
import java.util.List;
import java.util.ArrayList;

public class BrianAnimationPanel extends JPanel implements ActionListener {

    private final int WINDOWX = 640;
    private final int WINDOWY = 540;
    
    
    private final int TABLEWIDTH = 50;
    private final int TABLEHEIGHT = 50;
    
    private final int WaitingAreaX = 15;
    private final int WaitingAreaY = 15;
    private final int WaitingWidth = 70;
    private final int WaitingHeight = 400;
    
    
    private final int KitchenAreaX = 520;
    private final int KitchenAreaY = 50;
    private final int KitchenWidth = 10;
    private final int KitchenHeight = 50;
    
    private final int CashierX = 100;
    private final int CashierY = 15;
    private final int CashierW = 20;
    private final int CashierH = 20;
    
    private final int timerint = 1;
    public Timer timer;
    private List<Gui> guis = new ArrayList<Gui>();
    
    B_BrianRestaurant building;
    
    private BrianHost host;
    public void setHost(BrianHost host){
    	this.host = host;
    }
    
    public void setRestaurant(B_BrianRestaurant building){
    	this.building = building;
    }

    public BrianAnimationPanel() {
    	setSize(WINDOWX, WINDOWY);
        setVisible(true);
        
    	timer = new Timer(timerint, this );
    	timer.start();
    }

	public void actionPerformed(ActionEvent e) {
		try{
			synchronized(guis){
			for(Gui gui : guis) {
	            if (gui.isPresent()) {
	                gui.updatePosition();
	            }
	        }
			}
		}catch(ConcurrentModificationException e1){
			
		}
		repaint();  //Will have paintComponent called
	}
	
	public void toggleTimer(){
		if (timer.isRunning()){
			timer.stop();
		}
		else timer.start();
	}
	
	public String orderStandString = "";

    public void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D)g;

        //Clear the screen by painting a rectangle the size of the frame
        g2.setColor(getBackground());
        g2.fillRect(0, 0, WINDOWX, WINDOWY );
        
        if(building.hostFilled){
        	g2.setColor(Color.black);
        	g2.drawString("Host is in", 300, 20);
        }
        
        //Cashier
        if (building.cashierFilled){
	        g2.setColor(Color.green);
	        g2.fillRect(CashierX, CashierY, CashierW, CashierH);
	        g2.setColor(Color.black);
	        g2.drawString("Cashier", CashierX, CashierY);
        }
        
        
        //Waiting Area
        g2.setColor(Color.CYAN);
        g2.fillRect(WaitingAreaX, WaitingAreaY, WaitingWidth, WaitingHeight);
        g2.setColor(Color.black);
        g2.drawString("Waiting Area", WaitingAreaX, WaitingAreaY);
        
        
        //Table to give to waiters.
        g2.setColor(Color.gray);
        g2.fillRect(KitchenAreaX, KitchenAreaY, KitchenWidth, KitchenHeight);
        g2.setColor(Color.black);
        g2.drawString("Plating", KitchenAreaX-10, KitchenAreaY +25);
        
        //OrderStand
        g2.setColor(Color.blue);
        g2.fillRect(KitchenAreaX, KitchenAreaY + 100, KitchenWidth, KitchenHeight);
        g2.setColor(Color.black);
        g2.drawString("Blue OrderStand", KitchenAreaX-110, KitchenAreaY+100);
        g2.drawString(orderStandString, KitchenAreaX-110, KitchenAreaY+110);
        
        //Grills
        g2.setColor(Color.red);
        g2.fillRect(KitchenAreaX, KitchenAreaY + 50, KitchenWidth, KitchenHeight);
        g2.setColor(Color.black);
        g2.drawString("Grills", KitchenAreaX-10, KitchenAreaY + 75);
       
        for (BrianTable t : building.hostRole.tables){
        	//Here is the table
            g2.setColor(Color.ORANGE);
            g2.fillRect(t.getPosX(), t.getPosY(), TABLEWIDTH, TABLEHEIGHT);//200 and 250 need to be table params
        }

        
        synchronized (guis){
        for(Gui gui : guis) {
            if (gui.isPresent()) {
                gui.draw(g2);
            }
        }
        }
    }

    public void addGui(CustomerGui gui) {
        guis.add(gui);
    }

    public void addGui(WaiterGui gui) {
        guis.add(gui);
    }
    public void addGui(CookGui gui) {
        guis.add(gui);
    }
    
    public void removeGui(Gui gui){
    	
    	guis.remove(gui);
    }
}
