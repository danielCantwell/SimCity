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
	
	private final int xDoorPos	= 600;
	private final int xDoorSize	= 20;
	private final int yDoorPos	= 300;
	private final int yDoorSize	= 50;
	
	private final int xBedPos	= 50;
	private final int xBedSize	= 100;
	private final int yBedPos	= 50;
	private final int yBedSize	= 60;
	
	private final int xFridgePos	= 100;
	private final int xFridgeSize	= 30;
	private final int yFridgePos	= 400;
	private final int yFridgeSize	= 30;
	
	private final int xStovePos		= 300;
	private final int xStoveSize	= 100;
	private final int yStovePos		= 600;
	private final int yStoveSize	= 30;
	
	private final int xMailPos	= 600;
	private final int xMailSize	= 20;
	private final int yMailPos	= 50;
	private final int yMailSize	= 30;
	
	static final int TENANT_SPEED = 20;
	
	private List<Gui> guis = Collections.synchronizedList(new ArrayList<Gui>());
	
	public HousingAnimation() {
		setSize(WINDOW_X, WINDOW_Y);
		setVisible(true);

		Timer timer = new Timer(TENANT_SPEED, this);
		timer.start();
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
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
		g.fillRect(xMailPos, yMailPos, xMailSize, yMailSize);

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
	}
	
	public void addGui(TenantGui gui) {
		guis.add(gui);
	}

}
