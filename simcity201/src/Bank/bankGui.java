package Bank;

import housing.gui.TenantGui;


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

public class bankGui extends JPanel implements ActionListener {
	
	private final int WINDOW_X = 640;
	private final int WINDOW_Y = 640;
	
	private final int yCounterPos = 150;
	private final int yCounterSize = 30;
	private final int xCounterPos = 320;
	private final int xCounterSize = 300;
	
	/* Extras I might add in later to make the roles more realistic.
	private final int yVaultPos = 1;
	private final int yVaultSize = 1;
	private final int xVaultPos = 1;
	private final int xVaultSize = 1;
	
	private final int xCouches = 1;
	private final int yCouches = 1;
	*/
	
	private final int tellerSpeed = 20;
	
	private List<Gui> guis = Collections.synchronizedList(new ArrayList<Gui>());
	
	public bankGui() {
		setSize(WINDOW_X, WINDOW_Y);
		setVisible(true);

		Timer timer = new Timer(tellerSpeed, this);
		timer.start();
	}
	
	public void actionPerformed(ActionEvent arg0) {
		repaint();
	}
	
	public void paintComponent(Graphics g) {
		
		Graphics2D g2 = (Graphics2D) g;
		g2.setColor(Color.WHITE);
		g2.fillRect(0, 0, WINDOW_X, WINDOW_Y);
		
		g.setColor(Color.WHITE);
		g.fillRect(xCounterSize, yCounterSize, xCounterPos, yCounterPos);
		//g.fillRect(xVaultSize, yVaultSize, xVaultPos, yVaultPos);
		
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
	
	public void addGui(tellerGui tGui, bankCustomerGui bcGui, bankGuardGui gGui) {
		guis.add(tGui);
		guis.add(bcGui);
		guis.add(gGui);
	}
}
