package EricRestaurant.gui;

import javax.swing.*;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ConcurrentModificationException;
import java.util.List;
import java.util.ArrayList;

import jesseRest.gui.WaiterGui;

public class AnimationPanel extends JPanel implements ActionListener {


	private final int WINDOWX = 640;
	private final int WINDOWY = 640;
	private static int x = 200;
	private static int y = 250;
	private static int w = 50;
	private static int h = 50;
	private int tables = 3;

	private Image bufferImage;
	private Dimension bufferSize;

	private List<Gui> guis = new ArrayList<Gui>();

	public AnimationPanel() {
		setSize(WINDOWX, WINDOWY);
		setVisible(true);

		bufferSize = this.getSize();

		Timer timer = new Timer(20, this );
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
		Graphics2D g3 = (Graphics2D)g;
		Graphics2D g4 = (Graphics2D)g;
		Graphics2D g5 = (Graphics2D)g;

		//Clear the screen by painting a rectangle the size of the frame
		g2.setColor(getBackground());
		g2.fillRect(0, 0, WINDOWX, WINDOWY );

		//Here is the table
		for (int i = 0; i < tables; i++){
			g2.setColor(Color.ORANGE);
			g2.fillRect(x+i*100, y, w, h);//200 and 250 need to be table params
		}
		g3.setColor(Color.GRAY);
		g3.fillRect(400, 70, 100, 20);
		g4.setColor(Color.BLACK);
		g4.fillRect(400, 0, 100, 20);
		g5.setColor(Color.pink);
		g5.fillRect(500, 0, 30, 30);

		try {
			synchronized(guis) {
				for(Gui gui : guis) {
					if (gui.isPresent()) {
						gui.draw(g2);
					}
				}
			}
		}
		catch(ConcurrentModificationException e) {

		}
	}

	public void addGui(CustomerGui gui) {
		guis.add(gui);
	}

	public void addGui(HostGui gui) {
		guis.add(gui);
	}
	public void addGui(CookGui gui) {
		guis.add(gui);
	}

	public void removeGui(CustomerGui gui) {
		guis.remove(gui);
	}


	public void removeGui(WaiterGui waiterGui) {
		guis.remove(waiterGui);
	}

	public void removeGui(CookGui cookGui) {
		guis.remove(cookGui);		
	}

	public void removeGui(HostGui hostGui) {
		guis.remove(hostGui);		
	}	
}
