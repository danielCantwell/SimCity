package Bank.gui;

import housing.gui.TenantGui;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import Bank.*;
import Bank.interfaces.Customer;
import Bank.interfaces.Guard;
import Bank.interfaces.Teller;

import javax.swing.ImageIcon;
import javax.swing.JPanel;
import javax.swing.Timer;

import SimCity.gui.Gui;

public class bankGui extends JPanel implements ActionListener {

	private final int WINDOW_X = 640;
	private final int WINDOW_Y = 640;

	private final int yCounterPos = 80;
	private final int yCounterSize = 30;
	private final int xCounterPos = 150;
	private final int xCounterSize = 300;

	/* Extras I might add in later to make the roles more realistic.
	private final int yVaultPos = 1;
	private final int yVaultSize = 1;
	private final int xVaultPos = 1;
	private final int xVaultSize = 1;

	private final int xCouches = 1;
	private final int yCouches = 1;
	 */

	private final int tellerSpeed = 3;
	//private Teller teller = new tellerRole();
	//private Guard guard = new bankGuardRole();
	//private Customer cust = new bankCustomerRole();;
	private List<Gui> guis = Collections.synchronizedList(new ArrayList<Gui>());
    private ImageIcon iconFloor = new ImageIcon("images/floor3.png");
    
	public bankGui() {
		setSize(WINDOW_X, WINDOW_Y);
		setVisible(true);
		//addGui(teller.getGui());
		//addGui(cust.getGui());
		//addGui(guard.getGui());
		Timer timer = new Timer(tellerSpeed, this);
		timer.start();
	}

	public void actionPerformed(ActionEvent arg0) {
	    synchronized(guis)
	    {
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
		g2.setColor(Color.WHITE);
		g2.fillRect(0, 0, WINDOW_X, WINDOW_Y);

		for (int x = 0; x < WINDOW_X/128; x++) {
		for (int y = 0; y < WINDOW_Y/128; y++) {
			iconFloor.paintIcon(this, g, x * 128, y * 128);
		}
		}

		g.setColor(Color.LIGHT_GRAY);
		g.fillRect(xCounterPos, yCounterPos, xCounterSize, yCounterSize);
		g.setColor(Color.LIGHT_GRAY);
		g.fillRect(620, 320, 30, 60);
		synchronized(guis) {
			for (Gui gui : guis) {
				if (gui.isPresent()) {
					gui.draw(g2);
				}
			}
		}
	}

	public void addGui(tellerGui tGui){ 
		guis.add(tGui);
	}
	public void addGui(bankCustomerGui bcGui) {
		guis.add(bcGui);
	}
	public void addGui(bankGuardGui gGui) {
		guis.add(gGui);
	}
	public void removeGui(tellerGui gui) {
		guis.remove(gui);
	}
	public void removeGui(bankGuardGui gui) {
		guis.remove(gui);
	}
}
