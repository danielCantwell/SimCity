/**
 * 
 */
package housing.gui;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JPanel;
import javax.swing.Timer;

import SimCity.gui.Gui;

/**
 * @author Daniel
 *
 */
public class HousingAnimation extends JPanel implements ActionListener {
	
	private final int WINDOW_X = 640;
	private final int WINDOW_Y = 640;
	
	private final int xDoorPos	= 620;
	private final int xDoorSize	= 20;
	private final int yDoorPos	= 300;
	private final int yDoorSize	= 65;
	
	private final int xBedPos	= 10;
	private final int xBedSize	= 80;
	private final int yBedPos	= 10;
	private final int yBedSize	= 100;
	
	private final int xFridgePos	= 10;
	private final int xFridgeSize	= 60;
	private final int yFridgePos	= 570;
	private final int yFridgeSize	= 60;
	
	private final int xStovePos		= 300;
	private final int xStoveSize	= 300;
	private final int yStovePos		= 570;
	private final int yStoveSize	= 60;
	
	private final int xTablePos		= 100;
	private final int yTablePos		= 300;
	private final int xTableSize	= 100;
	private final int yTableSize	= 100;
	
	private final int xMailPos	= 600;
	private final int xMailSize	= 30;
	private final int yMailPos	= 150;
	private final int yMailSize	= 40;
	
	static final int TENANT_SPEED = 1;
	
    private ImageIcon iconFloor = new ImageIcon("images/floor1.png");
    private ImageIcon iconTable = new ImageIcon("images/table.png");
    private ImageIcon iconBed = new ImageIcon("images/bed.png");
    private ImageIcon iconStove = new ImageIcon("images/stove.png");
	
	private List<Gui> guis = Collections.synchronizedList(new ArrayList<Gui>());
	public List<Gui> getGuis(){
		return guis;
	}
	
	//private TenantRole tenant = new TenantRole();
	
	public HousingAnimation() {
		setSize(WINDOW_X, WINDOW_Y);
		setVisible(true);

		Timer timer = new Timer(TENANT_SPEED, this);
		timer.start();
	
		//addGui(tenant.getGui());
	}

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
		
		Graphics2D g2 = (Graphics2D) g;

		// Clear the screen by painting a rectangle the size of the frame
		g2.setColor(Color.DARK_GRAY);
		g2.fillRect(0, 0, WINDOW_X, WINDOW_Y);
		
		for (int x = 0; x < WINDOW_X/128; x++) {
		for (int y = 0; y < WINDOW_Y/128; y++) {
			iconFloor.paintIcon(this, g, x * 128, y * 128);
		}
		}
		
		// Draw Housing Layout
		g.setColor(Color.WHITE);
		g.fillRect(xDoorPos, yDoorPos, xDoorSize, yDoorSize);
		iconBed.paintIcon(this, g, xBedPos, yBedPos);
		iconBed.paintIcon(this, g, xBedPos + 120, yBedPos);
		iconBed.paintIcon(this, g, xBedPos + 240, yBedPos);
		iconBed.paintIcon(this, g, xBedPos + 360, yBedPos);
		iconBed.paintIcon(this, g, xBedPos, yBedPos + 140);
		iconBed.paintIcon(this, g, xBedPos + 120, yBedPos + 140);
		iconBed.paintIcon(this, g, xBedPos + 240, yBedPos + 140);
		iconBed.paintIcon(this, g, xBedPos + 360, yBedPos + 140);
		g.setColor(Color.GRAY);
		g.fillRect(xFridgePos, yFridgePos, xFridgeSize, yFridgeSize);
		iconStove.paintIcon(this, g, xStovePos, yStovePos);
		iconTable.paintIcon(this, g, xTablePos, yTablePos);
		iconTable.paintIcon(this, g, xTablePos + 200, yTablePos);
		iconTable.paintIcon(this, g, xTablePos, yTablePos + 130);
		iconTable.paintIcon(this, g, xTablePos + 200, yTablePos + 130);
		g.setColor(Color.DARK_GRAY);
		g.fillRect(xMailPos, yMailPos, xMailSize, yMailSize);
		
		// Draw Names for Housing Appliances
		g.setColor(Color.CYAN);
		g.drawString("Door", xDoorPos - 20, yDoorPos);
		g.drawString("Bed", xBedPos, yBedPos);
		g.drawString("Fridge", xFridgePos, yFridgePos);
		g.drawString("Stove", xStovePos, yStovePos);
		g.drawString("Table", xTablePos, yTablePos);
		g.drawString("Mail", xMailPos, yMailPos);

        synchronized (guis){
    		for (Gui gui : guis) {
    			if (gui.isPresent()) {
    				gui.draw(g2);
    			}
    		}
		}
	}
	
	public void addGui(TenantGui gui) {
		guis.add(gui);
	}
	public void removeGui(TenantGui gui){
		guis.remove(gui);
	}

}