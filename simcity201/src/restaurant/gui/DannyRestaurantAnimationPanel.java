package restaurant.gui;

import javax.swing.*;

import SimCity.gui.Gui;
import restaurant.DannyCustomer;
import restaurant.DannyHost;
import restaurant.DannyHost.Table;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.ArrayList;

public class DannyRestaurantAnimationPanel extends JPanel implements ActionListener {

	private final int WINDOWX = 640;
	private final int WINDOWY = 640;

	static final int TABLE_X_SIZE = 50;
	static final int TABLE_Y_SIZE = 50;
	
	private final int xCookStation	= 180;
	private final int yCookStation	= 360;
	private final int xCookSize		= 60;
	private final int yCookSize		= 20;
	
	private final int xCashier		= 360;
	private final int yCashier		= 10;
	private final int xCashierSize	= 20;
	private final int yCashierSize	= 50;
	
	private final int xHost		= 10;
	private final int yHost		= 25;
	private final int xHostSize	= 50;
	private final int yHostSize	= 20;

	static final int HOST_SPEED = 20;

	private Image bufferImage;
	private Dimension bufferSize;

	private DannyHost hostAgent;

	private int NTABLES = 0;

	public Collection<Table> tables;

	private List<Gui> guis = Collections.synchronizedList(new ArrayList<Gui>());

	public DannyRestaurantAnimationPanel() {
		setSize(WINDOWX, WINDOWY);
		setVisible(true);

		bufferSize = this.getSize();

		tables = Collections.synchronizedList(new ArrayList<Table>(NTABLES));
		synchronized (tables) {
			for (int ix = 1; ix <= NTABLES; ix++) {
				tables.add(new Table(ix));// how you add to a collections
			}
		}
		
		Timer timer = new Timer(HOST_SPEED, this);
		timer.start();
	}

	public void setHost(DannyHost host) {
		hostAgent = host;
		NTABLES = host.NTABLES;
	}

	public void actionPerformed(ActionEvent e) {
		
		repaint(); // Will have paintComponent called
	}

	public void paintComponent(Graphics g) {
		Graphics2D g2 = (Graphics2D) g;

		// Clear the screen by painting a rectangle the size of the frame
		g2.setColor(Color.DARK_GRAY);
		g2.fillRect(0, 0, WINDOWX, WINDOWY);
		
		int i = 0;
		for (Table table : tables) {
			i++;
			g2.setColor(Color.ORANGE);
			g2.fillRect(table.xCoord, table.yCoord, TABLE_X_SIZE, TABLE_Y_SIZE);
			g2.setColor(Color.WHITE);
			g2.drawRect(table.xCoord, table.yCoord, TABLE_X_SIZE, TABLE_Y_SIZE);
			g2.setColor(Color.BLACK);
			g2.drawString(Integer.toString(i), table.xCoord + 22, table.yCoord + 30);
		}
		
		drawCookStation(g2);
		drawCashier(g2);
		drawHost(g2);

		

		for (Gui gui : guis) {
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
	
	public void addGui(CookGui gui) {
		guis.add(gui);
	}
	
	private void drawCookStation(Graphics2D g) {
		g.setColor(Color.RED);
		g.fillRect(xCookStation, yCookStation, xCookSize, yCookSize);
		g.fillRect(xCookStation - 60, yCookStation + 30, xCookSize + 10, yCookSize);
		g.fillRect(xCookStation + 50, yCookStation + 30, xCookSize + 10, yCookSize);
		g.setColor(Color.WHITE);
		g.drawRect(xCookStation, yCookStation, xCookSize, yCookSize);
		g.setColor(Color.BLACK);
		g.drawRect(xCookStation - 60, yCookStation + 30, xCookSize + 10, yCookSize);
		g.drawRect(xCookStation + 50, yCookStation + 30, xCookSize + 10, yCookSize);
		g.drawLine(xCookStation - 25, yCookStation + 30, xCookStation - 25, yCookStation + 50);
		g.drawLine(xCookStation - 42, yCookStation + 30, xCookStation - 42, yCookStation + 50);
		g.drawLine(xCookStation - 8, yCookStation + 30, xCookStation - 8, yCookStation + 50);
		g.drawLine(xCookStation + 67, yCookStation + 30, xCookStation + 67, yCookStation + 50);
		g.drawLine(xCookStation + 85, yCookStation + 30, xCookStation + 85, yCookStation + 50);
		g.drawLine(xCookStation + 102, yCookStation + 30, xCookStation + 102, yCookStation + 50);
		g.drawString("Cook", xCookStation + 15, yCookStation + 15);
		g.setColor(Color.PINK);
		g.drawString("Cooking", xCookStation - 60, yCookStation + 26);
		g.drawString("Plated",  xCookStation + 87, yCookStation + 26);
	}
	
	private void drawCashier(Graphics2D g) {
		g.setColor(Color.GREEN);
		g.fillRect(xCashier, yCashier, xCashierSize, yCashierSize);
		g.setColor(Color.WHITE);
		g.drawRect(xCashier, yCashier, xCashierSize, yCashierSize);
		g.setColor(Color.DARK_GRAY);
		g.drawString("$", xCashier + 6, yCashier + 20);
		g.drawString("$", xCashier + 6, yCashier + 40);
	}
	
	private void drawHost(Graphics2D g) {
		g.setColor(Color.BLUE);
		g.fillRect(xHost, yHost, xHostSize, yHostSize);
		g.setColor(Color.WHITE);
		g.drawRect(xHost, yHost, xHostSize, yHostSize);
		g.drawString("Host", xHost + 12, yHost + 15);
	}
	
	public class Table {
		DannyCustomer occupiedBy;
		int tableNumber;

		public int xCoord = 0;
		public int yCoord = 0;

		public Table(int tableNumber) {
			this.tableNumber = tableNumber;

			if (tableNumber == 1) {
				xCoord = 80;
				yCoord = 100;
			} else if (tableNumber == 2) {
				xCoord = 260;
				yCoord = 100;
			} else if (tableNumber == 3) {
				xCoord = 80;
				yCoord = 260;
			} else if (tableNumber == 4) {
				xCoord = 260;
				yCoord = 260;
			}
		}
	}

}
