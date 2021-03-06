package timRest.gui;

import javax.swing.*;
import javax.swing.GroupLayout.Alignment;

import SimCity.Base.Person;
import SimCity.Buildings.B_Market;
import SimCity.Buildings.B_TimRest;
import SimCity.gui.Gui;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.util.Collections;
import java.util.List;
import java.util.ArrayList;

import timRest.TimHostRole;

public class TimAnimationPanel extends JPanel implements ActionListener
{
	private static final long serialVersionUID = -3483013053395877018L;
	private final int WINDOWX = 640;
    private final int WINDOWY = 640;
    private Image bufferImage;
    private Dimension bufferSize;

    private List<Gui> guis = Collections.synchronizedList(new ArrayList<Gui>());
    private TimHostRole hostRole;
    private ImageIcon iconFloor = new ImageIcon("images/floor1.png");
    private ImageIcon iconTable = new ImageIcon("images/table.png");
    private ImageIcon resizedTable;
    private Image img = iconTable.getImage();
    private BufferedImage bi = new BufferedImage(img.getWidth(null), img.getHeight(null), BufferedImage.TYPE_INT_ARGB);
    
    //public Person host;
    private B_TimRest restaurant;

    public TimAnimationPanel() {
    	setSize(WINDOWX, WINDOWY);
        setVisible(true);
        
        bufferSize = this.getSize();
        
        Graphics graphics = bi.createGraphics();
        graphics.drawImage(img, 0, 0, 50, 50, null);
        resizedTable = new ImageIcon(bi);
 
    	Timer timer = new Timer(10, this );
    	timer.start();
    }
    
    public void setBTimRest(B_TimRest rest){
        restaurant = rest;
        //market.setManager((MarketManagerRole) manager.mainRole);
        restaurant.panel = this;
    }
    public B_TimRest getBMarket(){return restaurant;}

    @Override
    public void actionPerformed(ActionEvent arg0) {
        synchronized (guis){
            for (Gui gui : guis) {
                if (gui.isPresent()) {
                    gui.updatePosition();
                }
            }
        }
        repaint();
    }

    public void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D)g;
        //Clear the screen by painting a rectangle the size of the frame
        g2.setColor(getBackground());
        g2.fillRect(0, 0, WINDOWX, WINDOWY );

		for (int x = 0; x < WINDOWX/128; x++) {
		for (int y = 0; y < WINDOWY/128; y++) {
			iconFloor.paintIcon(this, g, x * 128, y * 128);
		}
		}
		
        //Here are the tables
        g2.setColor(Color.ORANGE);
        //Prevent null pointer
        if (hostRole != null && hostRole.getTables() != null)
        {
	        //Iterate through tables
        	ArrayList<Point> tablePositions = new ArrayList<Point>();
        	tablePositions.addAll(hostRole.getTablePositions().values());
	        for (Point table : tablePositions)
	        {
	            resizedTable.paintIcon(this, g2, table.x, table.y);
	        }
	        g2.setColor(Color.BLACK);
	        for (int i = 0; i < tablePositions.size(); i++)
	        {
	        	g2.drawString(hostRole.getTableIcons().get(i), tablePositions.get(i).x+25, tablePositions.get(i).y+35);
	        }
        }
        else
        {
            g2.setColor(Color.RED);
            g2.fillRect(200, 280, 240, 80);
            g2.setColor(Color.BLACK);
            g2.setFont(new Font("Times", Font.BOLD, 24));
            g2.drawString("Grand Opening Today!", 210, 330);
        }

        synchronized(guis)
        {
            for (Gui gui : guis) {
                if (gui.isPresent()) {
                    gui.draw(g2);
                }
            }
        }
    }
    
    public void setHost(TimHostRole host)
    {
    	this.hostRole = host;
    }

    public void addGui(Gui gui) {
        synchronized(guis)
        {
            guis.add(gui);
        }
    }
    
    public void removeGui(Gui gui)
    {
        synchronized(guis)
        {
            guis.remove(gui);
        }
    }
}
