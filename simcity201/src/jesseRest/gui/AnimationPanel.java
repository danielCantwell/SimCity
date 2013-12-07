package jesseRest.gui;

import javax.swing.*;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.ArrayList;

import jesseRest.JesseCook;
import jesseRest.JesseHost;
import jesseRest.JesseCook.Order;

public class AnimationPanel extends JPanel implements ActionListener {

    private final int WINDOWX = 640;
    private final int WINDOWY = 640;
    private final int TABLESIZE = 50;
    private final int TABLEX = TABLESIZE;
    private final int TABLEY = TABLESIZE;
    private final int PADDING = 30;
    private final int COUNTER_H = 200;
    private JesseHost host;
    private JesseCook cook;
    
	public List<String> counterItems = new ArrayList<String>();
	public List<String> grillItems = new ArrayList<String>();
	
    private Image bufferImage;
    private Dimension bufferSize;

    private List<Gui> guis = new ArrayList<Gui>();

    public AnimationPanel() {
    	setSize(WINDOWX, WINDOWY);
        setVisible(true);
        
        bufferSize = this.getSize();
 
    	Timer timer = new Timer(20, this);
    	timer.start();
    }

	public void actionPerformed(ActionEvent e) {
		 for(Gui gui : guis) {
	            if (gui.isPresent()) {
	                gui.updatePosition();
	            }
	        }
		repaint();  //Will have paintComponent called
	}

    public void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D)g;

        // Clear the screen by painting a rectangle the size of the frame
        g2.setColor(getBackground());
        g2.fillRect(0, 0, WINDOWX, WINDOWY );

        // Draw the tables.
        g2.setColor(Color.ORANGE);
//		COMMENTED OUT SINCE STATIC TABLES    
       // for (int i = 0; i < host.getTables().size(); i++) {
        for (int i = 0; i < 3; i++) {
            g2.fillRect(TABLEX*2 + (2*i)*TABLESIZE, TABLEY*2, TABLESIZE, TABLESIZE);
        }
        
        // Draw the counters for the cook
        g2.setColor(Color.lightGray);
        g2.fillRect(WINDOWX - 40, WINDOWY - COUNTER_H, 40, COUNTER_H);
        g2.fillRect(WINDOWX - 160, WINDOWY - COUNTER_H, 40, COUNTER_H);
     
        g2.setColor(Color.black);
        
        if (grillItems.size() > 0) {
        	for (int i = 0; i < grillItems.size(); i++) {
        		g2.drawString(grillItems.get(i), WINDOWX - 30, WINDOWY - COUNTER_H + 20 + i*PADDING);
        	}
        }
        if (counterItems.size() > 0) {
        	for (int i = 0; i < counterItems.size(); i++) {
        		g2.drawString(counterItems.get(i), WINDOWX - 150, WINDOWY - COUNTER_H + 20 + i*PADDING);
        	}
        }
        
        g2.setColor(Color.blue);
        g2.fillRect(WINDOWX - 90, WINDOWY - COUNTER_H/2 - 20, 20, 20);

       

        for(Gui gui : guis) {
            if (gui.isPresent()) {
                gui.draw(g2);
            }
        }
    }

    public void addGui(CustomerGui gui) {
        guis.add(gui);
    }

    public void addGui(WaiterGui gui) {
        guis.add(gui);
    }
    
    public void removeGui(CustomerGui gui) {
    	guis.remove(gui);
    }
    
    public void removeGui(WaiterGui gui) {
    	guis.remove(gui);
    }
    
    public void setHost(JesseHost hostToAdd) {
    	host = hostToAdd;
    }
    
    public void setCook(JesseCook cookToAdd) {
    	cook = cookToAdd;
    }
}
