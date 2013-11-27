package restaurant.gui;

import javax.swing.*;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.ArrayList;

import restaurant.HostAgent;

public class AnimationPanel extends JPanel implements ActionListener
{
	private static final long serialVersionUID = -3483013053395877018L;
	private final int WINDOWX = 600;
    private final int WINDOWY = 600;
    private Image bufferImage;
    private Dimension bufferSize;

    private List<Gui> guis = new ArrayList<Gui>();
    private HostAgent host;

    public AnimationPanel() {
    	setSize(WINDOWX, WINDOWY);
        setVisible(true);
        
        bufferSize = this.getSize();
 
    	Timer timer = new Timer(20, this );
    	timer.start();
    }

	public void actionPerformed(ActionEvent e) {
		repaint();  //Will have paintComponent called
	}

    public void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D)g;

        //Clear the screen by painting a rectangle the size of the frame
        g2.setColor(getBackground());
        g2.fillRect(0, 0, WINDOWX, WINDOWY );

        //Here are the tables
        g2.setColor(Color.ORANGE);
        //Prevent null pointer
        if (host.getTables() != null)
        {
	        //Iterate through tables
        	ArrayList<Point> tablePositions = new ArrayList<Point>();
        	tablePositions.addAll(host.getTablePositions().values());
	        for (Point table : tablePositions)
	        {
	        	g2.fillRect(table.x, table.y, 50, 50);
	        }
	        g2.setColor(Color.BLACK);
	        for (int i = 0; i < tablePositions.size(); i++)
	        {
	        	g2.drawString(host.getTableIcons().get(i), tablePositions.get(i).x+25, tablePositions.get(i).y+35);
	        }
        }

        for(Gui gui : guis) {
            if (gui.isPresent()) {
                gui.updatePosition();
            }
        }

        for(Gui gui : guis) {
            if (gui.isPresent()) {
                gui.draw(g2);
            }
        }
    }
    
    public void setHost(HostAgent host)
    {
    	this.host = host;
    }

    public void addGui(CustomerGui gui) {
        guis.add(gui);
    }

    public void addGui(HostGui gui) {
        guis.add(gui);
    }

    public void addGui(WaiterGui gui) {
        guis.add(gui);
    }

    public void addGui(CookGui gui) {
        guis.add(gui);
    }
}
