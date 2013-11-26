/**
 * 
 */
package housing.gui;

import housing.roles.TenantRole;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

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
	private final int xBedSize	= 200;
	private final int yBedPos	= 10;
	private final int yBedSize	= 90;
	
	private final int xFridgePos	= 10;
	private final int xFridgeSize	= 60;
	private final int yFridgePos	= 570;
	private final int yFridgeSize	= 60;
	
	private final int xStovePos		= 300;
	private final int xStoveSize	= 300;
	private final int yStovePos		= 570;
	private final int yStoveSize	= 60;
	
	private final int xTablePos		= 200;
	private final int yTablePos		= 300;
	private final int xTableSize	= 100;
	private final int yTableSize	= 100;
	
	private final int xMailPos	= 600;
	private final int xMailSize	= 30;
	private final int yMailPos	= 150;
	private final int yMailSize	= 40;
	
	static final int TENANT_SPEED = 3;
	
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
		
		// Draw Housing Layout
		g.setColor(Color.WHITE);
		g.fillRect(xDoorPos, yDoorPos, xDoorSize, yDoorSize);
		g.fillRect(xBedPos, yBedPos, xBedSize, yBedSize);
		g.fillRect(xFridgePos, yFridgePos, xFridgeSize, yFridgeSize);
		g.fillRect(xStovePos, yStovePos, xStoveSize, yStoveSize);
		g.fillRect(xTablePos, yTablePos, xTableSize, yTableSize);
		g.fillRect(xMailPos, yMailPos, xMailSize, yMailSize);

		

		for (Gui gui : guis) {
			if (gui.isPresent()) {
				gui.draw(g2);
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