package exterior.gui;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsEnvironment;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.ConcurrentModificationException;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.Semaphore;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.ImageIcon;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.KeyStroke;
import javax.swing.Timer;

import SimCity.Base.Building;
import SimCity.Base.God;
import SimCity.Base.Person;
import SimCity.Base.Person.Morality;
import SimCity.Base.Person.Vehicle;
import SimCity.Buildings.B_House;
import SimCity.Globals.Money;
import SimCity.trace.AlertLog;
import SimCity.trace.AlertTag;
import exterior.astar.AStarTraversal;

public class AnimationPanel extends JPanel implements ActionListener {
    private List<Gui> guis = Collections.synchronizedList(new ArrayList<Gui>());
    private SimCityGui gui;
    private JScrollPane scrollPane;
    private boolean SHOW_RECT = false;
    private final int WINDOWX = 1920;
    private final int WINDOWY = 1920; //1472
    private final int TILESIZE = 64;
    private final int CITY_SIZE = 4;
    private List<ImageIcon> carsL = new ArrayList<ImageIcon>();
    private List<ImageIcon> carsR = new ArrayList<ImageIcon>();
    private List<ImageIcon> carsU = new ArrayList<ImageIcon>();
    private List<ImageIcon> carsD = new ArrayList<ImageIcon>();
    
    public HashMap<Integer, List<Person>> standees = new HashMap<Integer, List<Person>>();
    public final char[][] MAP = new char[][] {
    		
    	/* Map legend:
    		R: Road
    		S: Sidewalk
    		C: Crosswalk
    		B: Building
    	*/
    	
        {'R','R','R','R','R','R','R','R','R','R','R','R','R','R','R','R','R','R','R','R','R','R','R','R','R','R','R','R','R','R'},
        {'R','R','R','R','R','R','R','R','R','R','R','R','R','R','R','R','R','R','R','R','R','R','R','R','R','R','R','R','R','R'},
        {'R','R','S','S','S','S','S','C','C','S','S','S','S','S','C','C','S','S','S','S','S','C','C','S','S','S','S','S','R','R'},
        {'R','R','S','B','B','B','S','R','R','S','B','B','B','S','R','R','S','B','B','B','S','R','R','S','B','B','B','S','R','R'},
        {'R','R','S','B','B','B','S','R','R','S','B','B','B','S','R','R','S','B','B','B','S','R','R','S','B','B','B','S','R','R'},
        {'R','R','S','B','B','B','S','R','R','S','B','B','B','S','R','R','S','B','B','B','S','R','R','S','B','B','B','S','R','R'},
        {'R','R','S','S','S','S','S','C','C','S','S','S','S','S','C','C','S','S','S','S','S','C','C','S','S','S','S','S','R','R'},
        {'R','R','C','R','R','R','C','R','R','C','R','R','R','C','R','R','C','R','R','R','C','R','R','C','R','R','R','C','R','R'},
        {'R','R','C','R','R','R','C','R','R','C','R','R','R','C','R','R','C','R','R','R','C','R','R','C','R','R','R','C','R','R'},
        {'R','R','S','S','S','S','S','C','C','S','S','S','S','S','C','C','S','S','S','S','S','C','C','S','S','S','S','S','R','R'},
        {'R','R','S','B','B','B','S','R','R','S','B','B','B','S','R','R','S','B','B','B','S','R','R','S','B','B','B','S','R','R'},
        {'R','R','S','B','B','B','S','R','R','S','B','B','B','S','R','R','S','B','B','B','S','R','R','S','B','B','B','S','R','R'},
        {'R','R','S','B','B','B','S','R','R','S','B','B','B','S','R','R','S','B','B','B','S','R','R','S','B','B','B','S','R','R'},
        {'R','R','S','S','S','S','S','C','C','S','S','S','S','S','C','C','S','S','S','S','S','C','C','S','S','S','S','S','R','R'},
        {'R','R','C','R','R','R','C','R','R','C','R','R','R','C','R','R','C','R','R','R','C','R','R','C','R','R','R','C','R','R'},
        {'R','R','C','R','R','R','C','R','R','C','R','R','R','C','R','R','C','R','R','R','C','R','R','C','R','R','R','C','R','R'},
        {'R','R','S','S','S','S','S','C','C','S','S','S','S','S','C','C','S','S','S','S','S','C','C','S','S','S','S','S','R','R'},
        {'R','R','S','B','B','B','S','R','R','S','B','B','B','S','R','R','S','B','B','B','S','R','R','S','B','B','B','S','R','R'},
        {'R','R','S','B','B','B','S','R','R','S','B','B','B','S','R','R','S','B','B','B','S','R','R','S','B','B','B','S','R','R'},
        {'R','R','S','B','B','B','S','R','R','S','B','B','B','S','R','R','S','B','B','B','S','R','R','S','B','B','B','S','R','R'},
        {'R','R','S','S','S','S','S','C','C','S','S','S','S','S','C','C','S','S','S','S','S','C','C','S','S','S','S','S','R','R'},
        {'R','R','C','R','R','R','C','R','R','C','R','R','R','C','R','R','C','R','R','R','C','R','R','C','R','R','R','C','R','R'},
        {'R','R','C','R','R','R','C','R','R','C','R','R','R','C','R','R','C','R','R','R','C','R','R','C','R','R','R','C','R','R'},
        {'R','R','S','S','S','S','S','C','C','S','S','S','S','S','C','C','S','S','S','S','S','C','C','S','S','S','S','S','R','R'},
        {'R','R','S','B','B','B','S','R','R','S','B','B','B','S','R','R','S','B','B','B','S','R','R','S','B','B','B','S','R','R'},
        {'R','R','S','B','B','B','S','R','R','S','B','B','B','S','R','R','S','B','B','B','S','R','R','S','B','B','B','S','R','R'},
        {'R','R','S','B','B','B','S','R','R','S','B','B','B','S','R','R','S','B','B','B','S','R','R','S','B','B','B','S','R','R'},
        {'R','R','S','S','S','S','S','C','C','S','S','S','S','S','C','C','S','S','S','S','S','C','C','S','S','S','S','S','R','R'},
        {'R','R','R','R','R','R','R','R','R','R','R','R','R','R','R','R','R','R','R','R','R','R','R','R','R','R','R','R','R','R'},
        {'R','R','R','R','R','R','R','R','R','R','R','R','R','R','R','R','R','R','R','R','R','R','R','R','R','R','R','R','R','R'},
    };
    
    private Semaphore[][] pedestrianGrid = new Semaphore[WINDOWX/(TILESIZE)][WINDOWY/(TILESIZE)];
    public int[][] vehicleGrid = new int[WINDOWX/(TILESIZE)][WINDOWY/(TILESIZE)];
    public boolean horizontalRedLight = true;
    
    public boolean createAccidents = false;
    public boolean showRoleLabels = true;
    
    private String consoleText = "";
    private Font font;
    public int currentID = 1;
    public HashMap<Integer, Gui> idList = new HashMap<Integer, Gui>();
    
    // Image icon variables:
    private ImageIcon iconPedR = new ImageIcon("images/a_pedestrian_r.gif");
    private ImageIcon iconPedD = new ImageIcon("images/a_pedestrian_d.gif");
    private ImageIcon iconPedL = new ImageIcon("images/a_pedestrian_l.gif");
    private ImageIcon iconPedU = new ImageIcon("images/a_pedestrian_u.gif");
    private ImageIcon iconPedW = new ImageIcon("images/ped_waiting.png");
    private ImageIcon iconRoad = new ImageIcon("images/t_road.png");
    private ImageIcon iconRoadVL = new ImageIcon("images/t_road_vl.png");
    private ImageIcon iconRoadVR = new ImageIcon("images/t_road_vr.png");
    private ImageIcon iconRoadHL = new ImageIcon("images/t_road_hl.png");
    private ImageIcon iconRoadHR = new ImageIcon("images/t_road_hr.png");
    private ImageIcon iconCrossV = new ImageIcon("images/t_cross_v.png");
    private ImageIcon iconCrossH = new ImageIcon("images/t_cross_h.png");
    private ImageIcon iconBuildingA = new ImageIcon("images/t_building1.png");
    private ImageIcon iconBuildingB = new ImageIcon("images/t_building2.png");
    private ImageIcon iconBuildingHouse = new ImageIcon("images/housetop.png");
    private ImageIcon iconBuildingBank = new ImageIcon("images/banktop.png");
    private ImageIcon iconBuildingRest = new ImageIcon("images/restauranttop.png");
    private ImageIcon iconBuildingMark = new ImageIcon("images/markettop.png");
    private ImageIcon iconCar1R = new ImageIcon("images/car1_r.png");
    private ImageIcon iconCar1D = new ImageIcon("images/car1_d.png");
    private ImageIcon iconCar1L = new ImageIcon("images/car1_l.png");
    private ImageIcon iconCar1U = new ImageIcon("images/car1_u.png");
    private ImageIcon iconCar2R = new ImageIcon("images/car2_r.png");
    private ImageIcon iconCar2D = new ImageIcon("images/car2_d.png");
    private ImageIcon iconCar2L = new ImageIcon("images/car2_l.png");
    private ImageIcon iconCar2U = new ImageIcon("images/car2_u.png");
    private ImageIcon iconCar3R = new ImageIcon("images/car3_r.png");
    private ImageIcon iconCar3D = new ImageIcon("images/car3_d.png");
    private ImageIcon iconCar3L = new ImageIcon("images/car3_l.png");
    private ImageIcon iconCar3U = new ImageIcon("images/car3_u.png");
    private ImageIcon iconCar4R = new ImageIcon("images/car4_r.png");
    private ImageIcon iconCar4D = new ImageIcon("images/car4_d.png");
    private ImageIcon iconCar4L = new ImageIcon("images/car4_l.png");
    private ImageIcon iconCar4U = new ImageIcon("images/car4_u.png");
    private ImageIcon iconCar5R = new ImageIcon("images/car5_r.png");
    private ImageIcon iconCar5D = new ImageIcon("images/car5_d.png");
    private ImageIcon iconCar5L = new ImageIcon("images/car5_l.png");
    private ImageIcon iconCar5U = new ImageIcon("images/car5_u.png");
    private ImageIcon iconCar6R = new ImageIcon("images/car6_r.png");
    private ImageIcon iconCar6D = new ImageIcon("images/car6_d.png");
    private ImageIcon iconCar6L = new ImageIcon("images/car6_l.png");
    private ImageIcon iconCar6U = new ImageIcon("images/car6_u.png");
    private ImageIcon iconBusR = new ImageIcon("images/bus_r.png");
    private ImageIcon iconBusD = new ImageIcon("images/bus_d.png");
    private ImageIcon iconBusL = new ImageIcon("images/bus_l.png");
    private ImageIcon iconBusU = new ImageIcon("images/bus_u.png");
    private ImageIcon iconSun = new ImageIcon("images/iconSun.png");
    private ImageIcon iconMoon = new ImageIcon("images/iconMoon.png");
    private ImageIcon iconClock = new ImageIcon("images/iconClock.png");
    private ImageIcon iconRedLightH = new ImageIcon("images/redlightH.png");
    private ImageIcon iconRedLightV = new ImageIcon("images/redlightV.png");
	
	public AnimationPanel() {
		setSize(WINDOWX, WINDOWY);
		setVisible(true);
		Timer timer = new Timer(3, this);
		timer.start();
		God.Get().playSound("ambience", true);
		
		// Add cars to their respective lists
		carsL.add(iconCar1L);
		carsL.add(iconCar2L);
		carsL.add(iconCar3L);
		carsL.add(iconCar4L);
		carsL.add(iconCar5L);
		carsL.add(iconCar6L);
		carsR.add(iconCar1R);
		carsR.add(iconCar2R);
		carsR.add(iconCar3R);
		carsR.add(iconCar4R);
		carsR.add(iconCar5R);
		carsR.add(iconCar6R);
		carsU.add(iconCar1U);
		carsU.add(iconCar2U);
		carsU.add(iconCar3U);
		carsU.add(iconCar4U);
		carsU.add(iconCar5U);
		carsU.add(iconCar6U);
		carsD.add(iconCar1D);
		carsD.add(iconCar2D);
		carsD.add(iconCar3D);
		carsD.add(iconCar4D);
		carsD.add(iconCar5D);
		carsD.add(iconCar6D);
		
		God.Get().setAnimationPanel(this);
		
		// Map a number of bus standees to each building
		for (int i = 0; i < CITY_SIZE*CITY_SIZE; i++) {
			List<Person> l = new ArrayList<Person>();
			standees.put(i, l);
		}

		// Set up semaphore grid - sidewalks and crosswalks are open
		for (int y = 0; y < WINDOWY / (TILESIZE); y++) {
			for (int x = 0; x < WINDOWX / (TILESIZE); x++) {
				pedestrianGrid[x][y] = new Semaphore(1, true);
				vehicleGrid[x][y] = 0;
				if (MAP[x][y] == 'R' || MAP[x][y] == 'B') {
					try {
						pedestrianGrid[x][y].acquire();
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
				if (MAP[x][y] == 'S' || MAP[x][y] == 'B') {
					vehicleGrid[x][y] = 1;
				}
			}
		}

		addCommands();

		// Import font
		InputStream is;
		try {
			is = new FileInputStream("images/Minecraftia.ttf");
			font = Font.createFont(Font.TRUETYPE_FONT, is);
		    font = font.deriveFont(Font.PLAIN,12);
		    GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
		    ge.registerFont(font);
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		} catch (FontFormatException e1) {
			e1.printStackTrace();
		} catch (IOException e1) {
			e1.printStackTrace();
		}

		// This switches horizontal / vertical roads' red lights
		Timer trafficTimer = new Timer(5000, new ActionListener() {
			   public void actionPerformed(ActionEvent e) {
				   horizontalRedLight = !horizontalRedLight;
	    }});
		trafficTimer.start();
		
		// Spawn the buses at the beginning
		Timer busTimer = new Timer(500, new ActionListener() {
			   public void actionPerformed(ActionEvent e) {
				   if (currentID == 1) {
					   spawnBuses();
				   }
	    }});
		busTimer.start();
     
		this.addMouseListener(new MouseListener() {
			@Override
			public void mouseReleased(MouseEvent e) {
			}

			@Override
			public void mousePressed(MouseEvent e) {
			}

			@Override
			public void mouseExited(MouseEvent e) {
			}

			@Override
			public void mouseEntered(MouseEvent e) {
			}

			@Override
			public void mouseClicked(MouseEvent e) {
				// Upon mouse click, enter a building
				for (int i = 0; i < CITY_SIZE * CITY_SIZE; i++) {
					if (getBuildingRect(i).contains(e.getX(), e.getY())) {
						gui.buildingFrame.setVisible(true);
						gui.buildingFrame.setTitle("Building #" + i
								+ " - " + gui.buildingList.get(i).getTag());
						gui.cardLayout.show(gui.buildingPanels, "" + i);
						God.Get().playSound("dooropen", false);
					}
				}
			}
		});

		this.addMouseMotionListener(new MouseMotionListener() {
			@Override
			public void mouseDragged(MouseEvent e) {
			}

			@Override
			public void mouseMoved(MouseEvent e) {
				// Hover over a building to display its title
				consoleText = "";
				for (int i = 0; i < CITY_SIZE * CITY_SIZE; i++) {
					if (getBuildingRect(i).contains(e.getX(), e.getY())) {
						consoleText = "Click to Enter #" + i + ": " + gui.buildingList.get(i).getTag();
					}
				}
			}
		});
	}
	
	@Override
	// Update the GUIs
    public void actionPerformed(ActionEvent arg0) {
		try {
			synchronized(guis) {
		        for (Gui gui : guis) {
		            if (gui.isPresent()) {
		                gui.updatePosition();
		            }
		        }
		        repaint();
			}
		} catch (ConcurrentModificationException e) {
			
		}
	}

	public void paintComponent(Graphics g) {
		Graphics2D g2 = (Graphics2D) g;
		g2.setColor(getBackground());
		g2.fillRect(0, 0, WINDOWX, WINDOWY);
		
		// Draw the city based on the map array
		for (int y = 0; y < WINDOWY / TILESIZE; y++) {
			for (int x = 0; x < WINDOWX / TILESIZE; x++) {
				// Draw the road textures
				if (MAP[x][y] == 'R') {
					if (SHOW_RECT) {
						g2.setColor(Color.DARK_GRAY);
						g2.fillRect(x * TILESIZE, y * TILESIZE, TILESIZE,
								TILESIZE);
					} else {
						if (x != 0 && x != 29 && y != 0 && y != 29) {
							if (MAP[x - 1][y] == 'S') {
								iconRoadVL.paintIcon(this, g, x * TILESIZE, y
										* TILESIZE);
							} else if (MAP[x + 1][y] == 'S') {
								iconRoadVR.paintIcon(this, g, x * TILESIZE, y
										* TILESIZE);
							} else if (MAP[x][y - 1] == 'S') {
								iconRoadHL.paintIcon(this, g, x * TILESIZE, y
										* TILESIZE);
							} else if (MAP[x][y + 1] == 'S') {
								iconRoadHR.paintIcon(this, g, x * TILESIZE, y
										* TILESIZE);
							} else if (x <= 1 || x >= 28 || y <= 1 || y >= 28) {
								iconRoad.paintIcon(this, g, x * TILESIZE, y * TILESIZE);
							}
						} else {
							if (x == 0 && MAP[x + 2][y] == 'S') {
								iconRoadVL.paintIcon(this, g, x * TILESIZE, y
										* TILESIZE);
							} else if (x == 29 && MAP[x - 2][y] == 'S') {
								iconRoadVR.paintIcon(this, g, x * TILESIZE, y
										* TILESIZE);
							} else if (y == 0 && MAP[x][y + 2] == 'S') {
								iconRoadHL.paintIcon(this, g, x * TILESIZE, y
										* TILESIZE);
							} else if (y == 29 && MAP[x][y - 2] == 'S') {
								iconRoadHR.paintIcon(this, g, x * TILESIZE, y
										* TILESIZE);
							} else {
								iconRoad.paintIcon(this, g, x * TILESIZE, y
										* TILESIZE);
							}
						}
					}
				}
				// Draw the sidewalk textures
				if (MAP[x][y] == 'S') {
					if (SHOW_RECT) {
						g2.setColor(Color.LIGHT_GRAY);
						g2.fillRect(x * TILESIZE, y * TILESIZE, TILESIZE,
								TILESIZE);
					} else {
						if (MAP[x][y + 1] == 'S' && MAP[x + 1][y] == 'S'
								&& MAP[x + 1][y + 1] == 'B') {
							for (int i = 0; i < 16; i++) {
								if (getBuildingRect(i).contains(x * TILESIZE + TILESIZE * 2, y * TILESIZE + TILESIZE * 2)) {
									if (gui.buildingList.get(i).getTag() == "B_Bank") {
										iconBuildingBank.paintIcon(this, g, x * TILESIZE, y * TILESIZE);
									} else if (gui.buildingList.get(i).getTag() == "B_Market") {
										iconBuildingMark.paintIcon(this, g, x * TILESIZE, y * TILESIZE);
									} else if (gui.buildingList.get(i).getTag() == "B_House") {
										iconBuildingHouse.paintIcon(this, g, x * TILESIZE, y * TILESIZE);
									} else {
										iconBuildingRest.paintIcon(this, g, x * TILESIZE, y * TILESIZE);
									}

									if (consoleText.indexOf(i + "") != -1) {
										g2.drawString(consoleText, (x+1) * TILESIZE, (y+1) * TILESIZE - 10);
									}
								}
							}
						}
					}
				}
				// Draw the crosswalk textures
				if (MAP[x][y] == 'C') {
					if (SHOW_RECT) {
						g2.setColor(Color.GRAY);
						g2.fillRect(x * TILESIZE, y * TILESIZE, TILESIZE, TILESIZE);
					} else {
						if (MAP[x - 1][y] == 'C' || MAP[x + 1][y] == 'C') {
							iconCrossH.paintIcon(this, g, x * TILESIZE, y * TILESIZE);
						}
						if (MAP[x][y - 1] == 'C' || MAP[x][y + 1] == 'C') {
							iconCrossV.paintIcon(this, g, x * TILESIZE, y * TILESIZE);
						}
					}
				}
				// Draw the building textures
				if (MAP[x][y] == 'B') {
					if (SHOW_RECT) {
						g2.setColor(Color.ORANGE);
						g2.fillRect(x * TILESIZE, y * TILESIZE, TILESIZE, TILESIZE);
					}
				}
				
				if (x != 0 && x != 29 && y != 0 && y != 29 && !SHOW_RECT) {
					if (MAP[x][y] == 'R' && MAP[x-1][y] == 'C' && MAP[x][y-1] == 'C') {
						iconRoad.paintIcon(this, g, x * TILESIZE, y * TILESIZE);
						iconRoad.paintIcon(this, g, (x+1) * TILESIZE, y * TILESIZE);
						iconRoad.paintIcon(this, g, x * TILESIZE, (y+1) * TILESIZE);
						iconRoad.paintIcon(this, g, (x+1) * TILESIZE, (y+1) * TILESIZE);
						if (horizontalRedLight) {
							iconRedLightH.paintIcon(this, g, x * TILESIZE, y * TILESIZE);
						} else {
							iconRedLightV.paintIcon(this, g, x * TILESIZE, y * TILESIZE);
						}
					}
				}
			}
			
			// Draw the time / day icons and text
			g2.setColor(Color.WHITE);
			g2.setFont(font);
			g2.drawString("" + God.Get().getHour() + ":00", 46 + scrollPane.getHorizontalScrollBar().getValue(), 33 + scrollPane.getVerticalScrollBar().getValue());
            g2.drawString("Day #" + God.Get().getDay(), 46 + scrollPane.getHorizontalScrollBar().getValue(), 63 + scrollPane.getVerticalScrollBar().getValue());
			iconClock.paintIcon(this, g, 20 + scrollPane.getHorizontalScrollBar().getValue(), 20 + scrollPane.getVerticalScrollBar().getValue());
			if (God.Get().getHour() < 6 || God.Get().getHour() >= 18) {
				iconMoon.paintIcon(this, g, 20 + scrollPane.getHorizontalScrollBar().getValue(), 50 + scrollPane.getVerticalScrollBar().getValue());
			} else {
				iconSun.paintIcon(this, g, 20 + scrollPane.getHorizontalScrollBar().getValue(), 50 + scrollPane.getVerticalScrollBar().getValue());
			}
		}

		// Draw bus standee icons
		for (int i = 0; i < CITY_SIZE*CITY_SIZE; i++) {
			for (int j = 0; (j < standees.get(i).size() && j < 6); j++) {
				iconPedW.paintIcon(this, g, getBuildingX(i) + j*32 + 64, getBuildingY(i) - 64);
			}
		}

		synchronized(guis) {
			for (Gui gui : guis) {
				if (gui.isPresent()) {
					gui.draw(g2);
					// Draw Person GUIs and their labels
					if (gui.getType() == "Person") {
						if (showRoleLabels) {
							g.setColor(getColorFromString(gui.getPerson().mainRoleString.substring(0, 3)));
							String stype = "";
							String target = gui.getPerson().getMainRoleString();
							if (target.equals("timRest.TimHostRole") || target.equals("EricRestaurant.EricHost") || target.equals("brianRest.BrianHostRole") || target.equals("jesseRest.JesseHost") || target.equals("restaurant.DannyHost")) {
								stype = "Host";
							} else if (target.equals("restaurant.DannyCashier") || target.equals("jesseRest.JesseCashier") || target.equals("brianRest.BrianCashierRole") || target.equals("EricRestaurant.EricCashier") || target.equals("timRest.timCashier")) {
								stype = "Cash";
							} else if (target.equals("restaurant.DannyCook") || target.equals("jesseRest.JesseCook") || target.equals("brianRest.BrianCookRole") || target.equals("EricRestaurant.EricCook") || target.equals("timRest.TimCookRole")) {
								stype = "Cook";
							} else if (target.equals("restaurant.DannyWaiter") || target.equals("jesseRest.JesseWaiter") || target.equals("brianRest.BrianWaiterRole") || target.equals("EricRestaurant.EricWaiter") || target.equals("timRest.TimWaiterRole")) {
								stype = "Wait";
							} else if (target.equals("restaurant.DannyPCWaiter") || target.equals("jesseRest.JessePCWaiter") || target.equals("brianRest.BrianPCWaiterRole") || target.equals("EricRestaurant.EricPCWaiter") || target.equals("timRest.TimPCWaiterRole")) {
								stype = "PCWr";
							} else if (target.equals("restaurant.DannyCustomer") || target.equals("jesseRest.JesseCustomer") || target.equals("briantRest.BrianCustomerRole") || target.equals("EricRestaurant.EricCustomer") || target.equals("timRest.TimCustomerRole")) {
								stype = "Cust";
							} else if (target.equals("Bank.bankManagerRole")) {
								stype = "Mngr";
							} else if (target.equals("Bank.bankGuardRole")) {
								stype = "Guard";
							} else if (target.equals("Bank.tellerRole")) {
								stype = "Telr";
							} else if (target.equals("Bank.RobberRole")) {
								stype = "Robr";
							} else if (target.equals("Bank.bankCustomerRole")) {
								stype = "Cust";
							} else if (target.equals("market.MarketClerkRole")) {
								stype = "Clrk";
							} else if (target.equals("market.MarketPackerRole")) {
								stype = "Pckr";
							} else if (target.equals("market.MarketDeliveryPersonRole")) {
								stype = "Dlvy";
							} else if (target.equals("market.MarketManagerRole")) {
								stype = "Mngr";
							} else if (target.equals("market.MarketCustomerRole")) {
								stype = "Cust";
							} else {
								stype = "Wndr";
							}
							
							g.drawString(stype, gui.getX() + 16, gui.getY());
						}
						
						if (gui.getRotation() == 0) {
							iconPedR.paintIcon(this, g, gui.getX() + 16,
									gui.getY() + 32);
						} else if (gui.getRotation() == 1) {
							iconPedD.paintIcon(this, g, gui.getX() + 32,
									gui.getY() + 16);
						} else if (gui.getRotation() == 2) {
							iconPedL.paintIcon(this, g, gui.getX() + 16,
									gui.getY() + 00);
						} else if (gui.getRotation() == 3) {
							iconPedU.paintIcon(this, g, gui.getX() + 00,
									gui.getY() + 16);
						}
					}
					// Draw Car GUIs and their labels
					else if (gui.getType() == "Car") {
						g.setColor(getColorFromString(gui.getPerson().mainRoleString.substring(0, 3)));
						String stype = "";
						String target = gui.getPerson().getMainRoleString();
						if (target.equals("timRest.TimHostRole") || target.equals("EricRestaurant.EricHost") || target.equals("brianRest.BrianHostRole") || target.equals("jesseRest.JesseHost") || target.equals("restaurant.DannyHost")) {
							stype = "Host";
						} else if (target.equals("restaurant.DannyCashier") || target.equals("jesseRest.JesseCashier") || target.equals("brianRest.BrianCashierRole") || target.equals("EricRestaurant.EricCashier") || target.equals("timRest.timCashier")) {
							stype = "Cash";
						} else if (target.equals("restaurant.DannyCook") || target.equals("jesseRest.JesseCook") || target.equals("brianRest.BrianCookRole") || target.equals("EricRestaurant.EricCook") || target.equals("timRest.TimCookRole")) {
							stype = "Cook";
						} else if (target.equals("restaurant.DannyWaiter") || target.equals("jesseRest.JesseWaiter") || target.equals("brianRest.BrianWaiterRole") || target.equals("EricRestaurant.EricWaiter") || target.equals("timRest.TimWaiterRole")) {
							stype = "Wait";
						} else if (target.equals("restaurant.DannyPCWaiter") || target.equals("jesseRest.JessePCWaiter") || target.equals("brianRest.BrianPCWaiterRole") || target.equals("EricRestaurant.EricPCWaiter") || target.equals("timRest.TimPCWaiterRole")) {
							stype = "PCWr";
						} else if (target.equals("restaurant.DannyCustomer") || target.equals("jesseRest.JesseCustomer") || target.equals("briantRest.BrianCustomerRole") || target.equals("EricRestaurant.EricCustomer") || target.equals("timRest.TimCustomerRole")) {
							stype = "Cust";
						} else if (target.equals("Bank.bankManagerRole")) {
							stype = "Mngr";
						} else if (target.equals("Bank.bankGuardRole")) {
							stype = "Guard";
						} else if (target.equals("Bank.tellerRole")) {
							stype = "Telr";
						} else if (target.equals("Bank.RobberRole")) {
							stype = "Robr";
						} else if (target.equals("Bank.bankCustomerRole")) {
							stype = "Cust";
						} else if (target.equals("market.MarketClerkRole")) {
							stype = "Clrk";
						} else if (target.equals("market.MarketPackerRole")) {
							stype = "Pckr";
						} else if (target.equals("market.MarketDeliveryPersonRole")) {
							stype = "Dlvy";
						} else if (target.equals("market.MarketManagerRole")) {
							stype = "Mngr";
						} else if (target.equals("market.MarketCustomerRole")) {
							stype = "Cust";
						} else {
							stype = "Wndr";
						}
						
						g.drawString(stype, gui.getX() + 16, gui.getY());
						
						if (gui.getRotation() == 0) {
							carsR.get(gui.getID() % 6).paintIcon(this, g, gui.getX(),
									gui.getY());
						} else if (gui.getRotation() == 1) {
							carsD.get(gui.getID() % 6).paintIcon(this, g, gui.getX(),
									gui.getY());
						} else if (gui.getRotation() == 2) {
							carsL.get(gui.getID() % 6).paintIcon(this, g, gui.getX(),
									gui.getY());
						} else if (gui.getRotation() == 3) {
							carsU.get(gui.getID() % 6).paintIcon(this, g, gui.getX(),
									gui.getY());
						}
					}
					// Draw the Bus GUIs
					else if (gui.getType() == "Bus") {
						if (gui.getRotation() == 0) {
							iconBusR.paintIcon(this, g, gui.getX(),
									gui.getY());
						} else if (gui.getRotation() == 1) {
							iconBusD.paintIcon(this, g, gui.getX(),
									gui.getY());
						} else if (gui.getRotation() == 2) {
							iconBusL.paintIcon(this, g, gui.getX(),
									gui.getY());
						} else if (gui.getRotation() == 3) {
							iconBusU.paintIcon(this, g, gui.getX(),
									gui.getY());
						}
					}
				}
			}
		}
	}
    
	// Given a char, find a color (for labels)
	public Color getColorFromString(String s) {
		if (s.equals("Ban")) {
			return Color.red;
		} else if (s.equals("mar")) {
			return Color.orange;
		} else if (s.equals("res")) {
			return Color.yellow;
		} else if (s.equals("bri")) {
			return Color.green;
		} else if (s.equals("jes")) {
			return Color.blue;
		} else if (s.equals("Eri")) {
			return Color.magenta;
		} else if (s.equals("tim")) {
			return Color.pink;
		} else {
			return Color.white;
		}
	}
    public void addGui(Gui gui) {
    	guis.add(gui);
    }
    
    public void setGui(SimCityGui g) {
    	gui = g;
    }
    
    public SimCityGui getGui() {
    	return gui;
    }
    
    // Returns a rectangle boundary for a building number
    private Rectangle getBuildingRect(int buildingNumber) {
    	return new Rectangle(((buildingNumber % CITY_SIZE) * 7 + 3)*TILESIZE, ((buildingNumber / CITY_SIZE) * 7 + 3)*TILESIZE, TILESIZE*3, TILESIZE*3);
    }
    
    // Keyboard shortcuts
    protected void addCommands()
    {
        Action keyCtrlZ = new AbstractAction()
        {
             public void actionPerformed(ActionEvent e)
             {

            	 //First Shift
            	 createPerson("EHost", "EricRestaurant.EricHost", Vehicle.car, Morality.good, gui.buildingList.get(0), gui.buildingList.get(11), 1);
         		 createPerson("ECustomer", "EricRestaurant.EricCustomer", Vehicle.walk, Morality.good, gui.buildingList.get(0), gui.buildingList.get(11), 1);
         		 createPerson("EECustomer", "EricRestaurant.EricCustomer", Vehicle.walk, Morality.good, gui.buildingList.get(0), gui.buildingList.get(11), 1);
            	 createPerson("EWaiter", "EricRestaurant.EricWaiter", Vehicle.walk, Morality.good, gui.buildingList.get(0), gui.buildingList.get(11), 1);
            	 createPerson("EPCWaiter", "EricRestaurant.EricPCWaiter", Vehicle.walk, Morality.good, gui.buildingList.get(0), gui.buildingList.get(11), 1);
            	 createPerson("ECashier", "EricRestaurant.EricCashier", Vehicle.walk, Morality.good, gui.buildingList.get(0), gui.buildingList.get(11), 1);
            	 createPerson("ECook", "EricRestaurant.EricCook", Vehicle.walk, Morality.good, gui.buildingList.get(0), gui.buildingList.get(11), 1); 
             }
        };
        Action keyCtrlR = new AbstractAction()
        {
             public void actionPerformed(ActionEvent e)
             {

            	 //First Shift
            	 createPerson("BManager", "Bank.bankManagerRole", Vehicle.walk, Morality.good, gui.buildingList.get(0), gui.buildingList.get(2), 1);
            	 createPerson("BTeller", "Bank.tellerRole", Vehicle.walk, Morality.good, gui.buildingList.get(0), gui.buildingList.get(2), 1);
            	 createPerson("BRobber", "Bank.RobberRole", Vehicle.walk, Morality.good, gui.buildingList.get(0), gui.buildingList.get(2), 1);
            	 createPerson("BCustomer", "Bank.bankCustomerRole", Vehicle.walk, Morality.good, gui.buildingList.get(0), gui.buildingList.get(2), 1);
            	 createPerson("BGuard", "Bank.bankGuardRole", Vehicle.walk, Morality.good, gui.buildingList.get(0), gui.buildingList.get(2), 1);
               	 createPerson("BTeller", "Bank.tellerRole", Vehicle.walk, Morality.good, gui.buildingList.get(0), gui.buildingList.get(2), 1);
            	 createPerson("BRobber", "Bank.RobberRole", Vehicle.walk, Morality.good, gui.buildingList.get(0), gui.buildingList.get(2), 1);
            	 createPerson("BCustomer", "Bank.bankCustomerRole", Vehicle.walk, Morality.good, gui.buildingList.get(0), gui.buildingList.get(2), 1);
            	 createPerson("BGuard", "Bank.bankGuardRole", Vehicle.walk, Morality.good, gui.buildingList.get(0), gui.buildingList.get(2), 1);
               	 createPerson("BTeller", "Bank.tellerRole", Vehicle.walk, Morality.good, gui.buildingList.get(0), gui.buildingList.get(2), 1);
            	 createPerson("BRobber", "Bank.RobberRole", Vehicle.walk, Morality.good, gui.buildingList.get(0), gui.buildingList.get(2), 1);
            	 createPerson("BCustomer", "Bank.bankCustomerRole", Vehicle.walk, Morality.good, gui.buildingList.get(0), gui.buildingList.get(2), 1);
            	 createPerson("BGuard", "Bank.bankGuardRole", Vehicle.walk, Morality.good, gui.buildingList.get(0), gui.buildingList.get(2), 1);
             }
        };
        Action keyCtrlE = new AbstractAction()
        {
        	public void actionPerformed(ActionEvent e)
        	{
        		//First Shift
                createPerson("MManager", "market.MarketManagerRole", Vehicle.car, Morality.good, gui.buildingList.get(1), gui.buildingList.get(3),1);
                createPerson("MClerk", "market.MarketClerkRole", Vehicle.walk, Morality.good, gui.buildingList.get(1), gui.buildingList.get(3), 1);
                createPerson("MPacker", "market.MarketPackerRole", Vehicle.walk, Morality.good, gui.buildingList.get(1), gui.buildingList.get(3),1);
                createPerson("MDelivery", "market.MarketDeliveryPersonRole", Vehicle.car, Morality.good, gui.buildingList.get(1), gui.buildingList.get(3),1);
           	 	createPerson("BRobber", "Bank.RobberRole", Vehicle.walk, Morality.good, gui.buildingList.get(1), gui.buildingList.get(2), 1);
           	 	createPerson("BCustomer", "Bank.bankCustomerRole", Vehicle.walk, Morality.good, gui.buildingList.get(1), gui.buildingList.get(2), 1);
           	    createPerson("BGuard", "Bank.bankGuardRole", Vehicle.walk, Morality.good, gui.buildingList.get(1), gui.buildingList.get(2), 1);
        		createPerson("BManager", "Bank.bankManagerRole", Vehicle.car, Morality.good, gui.buildingList.get(1), gui.buildingList.get(2), 1);
                createPerson("BTeller", "Bank.tellerRole", Vehicle.walk, Morality.good, gui.buildingList.get(1), gui.buildingList.get(2), 1);
        		createPerson("EHost", "EricRestaurant.EricHost", Vehicle.car, Morality.good, gui.buildingList.get(0), gui.buildingList.get(11), 1);
        		createPerson("ECustomer", "EricRestaurant.EricCustomer", Vehicle.walk, Morality.good, gui.buildingList.get(0), gui.buildingList.get(11), 1);
        		createPerson("ECustomer2", "EricRestaurant.EricCustomer", Vehicle.walk, Morality.good, gui.buildingList.get(0), gui.buildingList.get(11), 1);
           	 	createPerson("EWPCaiter", "EricRestaurant.EricPCWaiter", Vehicle.walk, Morality.good, gui.buildingList.get(0), gui.buildingList.get(11), 1);
           	 	createPerson("EWaiter", "EricRestaurant.EricWaiter", Vehicle.walk, Morality.good, gui.buildingList.get(0), gui.buildingList.get(11), 1);
           	 	createPerson("ECashier", "EricRestaurant.EricCashier", Vehicle.walk, Morality.good, gui.buildingList.get(0), gui.buildingList.get(11), 1);
           	 	createPerson("ECook", "EricRestaurant.EricCook", Vehicle.walk, Morality.good, gui.buildingList.get(0), gui.buildingList.get(11), 1); 
           	 	
           	 	//Second Shift
           	 	createPerson("MManager", "market.MarketManagerRole", Vehicle.car, Morality.good, gui.buildingList.get(1), gui.buildingList.get(3),2);
           	 	createPerson("MClerk", "market.MarketClerkRole", Vehicle.walk, Morality.good, gui.buildingList.get(1), gui.buildingList.get(3), 2);
           	 	createPerson("MPacker", "market.MarketPackerRole", Vehicle.walk, Morality.good, gui.buildingList.get(1), gui.buildingList.get(3),2);
           	 	createPerson("MDelivery", "market.MarketDeliveryPersonRole", Vehicle.car, Morality.good, gui.buildingList.get(1), gui.buildingList.get(3),2);
         		createPerson("EHost", "EricRestaurant.EricHost", Vehicle.walk, Morality.good, gui.buildingList.get(0), gui.buildingList.get(11), 2);
           	 	createPerson("EWaiter", "EricRestaurant.EricWaiter", Vehicle.walk, Morality.good, gui.buildingList.get(0), gui.buildingList.get(11), 2);
           	 	createPerson("ECashier", "EricRestaurant.EricCashier", Vehicle.walk, Morality.good, gui.buildingList.get(0), gui.buildingList.get(11), 2);
           	 	createPerson("ECook", "EricRestaurant.EricCook", Vehicle.walk, Morality.good, gui.buildingList.get(0), gui.buildingList.get(11), 2); 
           	 	createPerson("BGuard", "Bank.bankGuardRole", Vehicle.walk, Morality.good, gui.buildingList.get(0), gui.buildingList.get(2), 2);
           	 	createPerson("BManager", "Bank.bankManagerRole", Vehicle.walk, Morality.good, gui.buildingList.get(0), gui.buildingList.get(2), 2);
           	 	createPerson("BTeller", "Bank.tellerRole", Vehicle.walk, Morality.good, gui.buildingList.get(0), gui.buildingList.get(2), 2);
        	}
        };
        Action keyCtrlJ = new AbstractAction()
        {
        	public void actionPerformed(ActionEvent e)
        	{
//        		createPerson("BGuard", "Bank.bankGuardRole", Vehicle.walk, Morality.good, gui.buildingList.get(0), gui.buildingList.get(2), 1);
//        		createPerson("BManager", "Bank.bankManagerRole", Vehicle.walk, Morality.good, gui.buildingList.get(0), gui.buildingList.get(2), 1);
//                createPerson("BTeller", "Bank.tellerRole", Vehicle.walk, Morality.good, gui.buildingList.get(0), gui.buildingList.get(2), 1);
        		createPerson("JHost", "jesseRest.JesseHost", Vehicle.walk, Morality.good, gui.buildingList.get(0), gui.buildingList.get(7), 1);
        		createPerson("JCustomer", "jesseRest.JesseCustomer", Vehicle.walk, Morality.good, gui.buildingList.get(0), gui.buildingList.get(7), 1);
           	 	createPerson("JPCWaiter", "jesseRest.JessePCWaiter", Vehicle.walk, Morality.good, gui.buildingList.get(0), gui.buildingList.get(7), 1);
           	 	createPerson("JCashier", "jesseRest.JesseCashier", Vehicle.walk, Morality.good, gui.buildingList.get(0), gui.buildingList.get(7), 1);
           	 	createPerson("JCook", "jesseRest.JesseCook", Vehicle.walk, Morality.good, gui.buildingList.get(0), gui.buildingList.get(7), 1); 
//                createPerson("Host", "brianRest.BrianHostRole", Vehicle.walk, Morality.good, gui.buildingList.get(0), gui.buildingList.get(6), 1);
//           	 createPerson("Customer", "brianRest.BrianCustomerRole", Vehicle.walk, Morality.good, gui.buildingList.get(0), gui.buildingList.get(6), 1);
//           	 createPerson("PCWaiter", "brianRest.BrianPCWaiterRole", Vehicle.walk, Morality.good, gui.buildingList.get(4), gui.buildingList.get(6), 1);
//           	 createPerson("Cashier", "brianRest.BrianCashierRole", Vehicle.walk, Morality.good, gui.buildingList.get(0), gui.buildingList.get(6), 1);
//           	 createPerson("Cook", "brianRest.BrianCookRole", Vehicle.walk, Morality.good, gui.buildingList.get(0), gui.buildingList.get(6), 1); 
        	}
        };
       
        
        Action keyCtrlY = new AbstractAction()
        {
             public void actionPerformed(ActionEvent e)
             {

                createPerson("Manny", "market.MarketManagerRole", Vehicle.car, Morality.good, gui.buildingList.get(1), gui.buildingList.get(3),1);
             }
        };
        
        Action keyCtrlK = new AbstractAction()
        {
             public void actionPerformed(ActionEvent e)
             {
                 createPerson("Clark", "market.MarketClerkRole", Vehicle.walk, Morality.good, gui.buildingList.get(1), gui.buildingList.get(3), 1);

             }
        };
        
        Action keyCtrlP = new AbstractAction()
        {
             public void actionPerformed(ActionEvent e)
             {
                 createPerson("Parker", "market.MarketPackerRole", Vehicle.walk, Morality.good, gui.buildingList.get(1), gui.buildingList.get(3),1);
                 createPerson("Del", "market.MarketDeliveryPersonRole", Vehicle.car, Morality.good, gui.buildingList.get(1), gui.buildingList.get(3),1);
             }
        };
        
        Action keyCtrl3 = new AbstractAction()
        {
             public void actionPerformed(ActionEvent e)
             {
                 marketScenarioPerson("Customer", "Bank.tellerRole", Vehicle.walk, Morality.good, gui.buildingList.get(1), gui.buildingList.get(2));
             }
        };
        
        Action keyCtrl4 = new AbstractAction()
        {
             public void actionPerformed(ActionEvent e)
             {
                 createPerson("THost", "timRest.TimHostRole", Vehicle.car, Morality.good, gui.buildingList.get(13), gui.buildingList.get(10), 1);
                 createPerson("TWaiter", "timRest.TimWaiterRole", Vehicle.walk, Morality.good, gui.buildingList.get(13), gui.buildingList.get(10), 1);
                 createPerson("TCook", "timRest.TimCookRole", Vehicle.walk, Morality.good, gui.buildingList.get(13), gui.buildingList.get(10), 1);
                 createPerson("TCashier", "timRest.TimCashierRole", Vehicle.walk, Morality.good, gui.buildingList.get(13), gui.buildingList.get(10), 1);
             }
        };
        
        Action keyCtrl5 = new AbstractAction()
        {
             public void actionPerformed(ActionEvent e)
             {
                 createPerson("TCustomer", "timRest.TimCustomerRole", Vehicle.walk, Morality.good, gui.buildingList.get(1), gui.buildingList.get(10),1);
                 timScenarioPerson("Graham", "market.MarketPackerRole", Vehicle.walk, Morality.good, gui.buildingList.get(1), gui.buildingList.get(3));
             }
        };
        
        Action keyCtrlC = new AbstractAction()
        {
             public void actionPerformed(ActionEvent e)
             {
            	SHOW_RECT = !SHOW_RECT;
            	if (SHOW_RECT) {
        			AlertLog.getInstance().logDebug(AlertTag.God, "God (GUI)", "Entering compatibility mode.");
        			AlertLog.getInstance().logDebug(AlertTag.God, "God (GUI)", "To view guis in compatibility mode, change SHOW_RECT in Gui.java to true.");
            	} else {
        			AlertLog.getInstance().logDebug(AlertTag.God, "God (GUI)", "Exiting compatibility mode.");
            	}
             }
        };
        
        Action keyCtrlA = new AbstractAction()
        {
             public void actionPerformed(ActionEvent e)
             {
            	createAccidents = !createAccidents;
            	if (createAccidents) {
        			AlertLog.getInstance().logDebug(AlertTag.God, "God (GUI)", "Cars' accident chance set to 100%.");
            	} else {
        			AlertLog.getInstance().logDebug(AlertTag.God, "God (GUI)", "Cars' accident chance reset to default 20%.");
            	}
             }
        };
        
        Action keyCtrlQ = new AbstractAction()
        {
             public void actionPerformed(ActionEvent e)
             {
              	 createPerson("Matt", "Bank.bankCustomerRole", Vehicle.bus, Morality.good, gui.buildingList.get(0), gui.buildingList.get(2), 1);
             }
        };
        
        Action keyCtrl0 = new AbstractAction()
        {
             public void actionPerformed(ActionEvent e)
             {
            	 //createPerson("Customer", "none", Vehicle.walk, Morality.good, gui.buildingList.get(0), gui.buildingList.get(6), 1);

            	 createPerson("Host", "brianRest.BrianHostRole", Vehicle.walk, Morality.good, gui.buildingList.get(0), gui.buildingList.get(6), 1);
            	 createPerson("Customer", "brianRest.BrianCustomerRole", Vehicle.walk, Morality.good, gui.buildingList.get(0), gui.buildingList.get(6), 1);
            	 createPerson("Customer", "brianRest.BrianCustomerRole", Vehicle.walk, Morality.good, gui.buildingList.get(0), gui.buildingList.get(6), 1);
            	 createPerson("PCWaiter", "brianRest.BrianPCWaiterRole", Vehicle.walk, Morality.good, gui.buildingList.get(4), gui.buildingList.get(6), 1);
            	 createPerson("PCWaiter", "brianRest.BrianPCWaiterRole", Vehicle.walk, Morality.good, gui.buildingList.get(0), gui.buildingList.get(6), 1);

            	 createPerson("Cashier", "brianRest.BrianCashierRole", Vehicle.walk, Morality.good, gui.buildingList.get(0), gui.buildingList.get(6), 1);
            	 createPerson("Cook", "brianRest.BrianCookRole", Vehicle.walk, Morality.good, gui.buildingList.get(0), gui.buildingList.get(6), 1);   
            	 
            	 //shift 2
            	 createPerson("Host", "brianRest.BrianHostRole", Vehicle.walk, Morality.good, gui.buildingList.get(4), gui.buildingList.get(6), 2);
            	 createPerson("Waiter", "brianRest.BrianWaiterRole", Vehicle.walk, Morality.good, gui.buildingList.get(4), gui.buildingList.get(6), 2);
            	 createPerson("Waiter", "brianRest.BrianWaiterRole", Vehicle.walk, Morality.good, gui.buildingList.get(4), gui.buildingList.get(6), 2);

            	 createPerson("Cashier", "brianRest.BrianCashierRole", Vehicle.walk, Morality.good, gui.buildingList.get(4), gui.buildingList.get(6), 2);
            	 createPerson("Cook", "brianRest.BrianCookRole", Vehicle.walk, Morality.good, gui.buildingList.get(4), gui.buildingList.get(6), 2); 
             
             }
        };
        String stringCtrlR = "CTRL R";
        getInputMap(this.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_R, KeyEvent.CTRL_MASK), stringCtrlR);
        getActionMap().put(stringCtrlR, keyCtrlR);
        String stringCtrl0 = "CTRL 0";
        getInputMap(this.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_0, KeyEvent.CTRL_MASK), stringCtrl0);
        getActionMap().put(stringCtrl0, keyCtrl0);
        String stringCtrlJ = "CTRL J";
        getInputMap(this.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_J, KeyEvent.CTRL_MASK), stringCtrlJ);
        getActionMap().put(stringCtrlJ, keyCtrlJ);
        String stringCtrlE = "CTRL E";
        getInputMap(this.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_E, KeyEvent.CTRL_MASK), stringCtrlE);
        getActionMap().put(stringCtrlE, keyCtrlE);
        String stringCtrlZ = "CTRL Z";
        getInputMap(this.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_Z, KeyEvent.CTRL_MASK), stringCtrlZ);
        getActionMap().put(stringCtrlZ, keyCtrlZ);
        String stringCtrlY = "CTRL Y";
        getInputMap(this.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_Y, KeyEvent.CTRL_MASK), stringCtrlY);
        getActionMap().put(stringCtrlY, keyCtrlY);
        String stringCtrlK = "CTRL K";
        getInputMap(this.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_K, KeyEvent.CTRL_MASK), stringCtrlK);
        getActionMap().put(stringCtrlK, keyCtrlK);
        String stringCtrlP = "CTRL P";
        getInputMap(this.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_P, KeyEvent.CTRL_MASK), stringCtrlP);
        getActionMap().put(stringCtrlP, keyCtrlP);
        String stringCtrl3 = "CTRL 3";
        getInputMap(this.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_3, KeyEvent.CTRL_MASK), stringCtrl3);
        getActionMap().put(stringCtrl3, keyCtrl3);
        String stringCtrl4 = "CTRL 4";
        getInputMap(this.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_4, KeyEvent.CTRL_MASK), stringCtrl4);
        getActionMap().put(stringCtrl4, keyCtrl4);
        String stringCtrl5 = "CTRL 5";
        getInputMap(this.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_5, KeyEvent.CTRL_MASK), stringCtrl5);
        getActionMap().put(stringCtrl5, keyCtrl5);
        String stringCtrlC = "CTRL C";
        getInputMap(this.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_C, KeyEvent.CTRL_MASK), stringCtrlC);
        getActionMap().put(stringCtrlC, keyCtrlC);
        String stringCtrlQ = "CTRL Q";
        getInputMap(this.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_Q, KeyEvent.CTRL_MASK), stringCtrlQ);
        getActionMap().put(stringCtrlQ, keyCtrlQ);
        String stringCtrlA = "CTRL A";
        getInputMap(this.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_A, KeyEvent.CTRL_MASK), stringCtrlA);
        getActionMap().put(stringCtrlA, keyCtrlA);
    }
    
    // Create a person 
    public Person createPerson(String name, String role, Vehicle v, Morality m, Building house, Building b, int shift){
	   	 AStarTraversal aStarTraversal = new AStarTraversal(pedestrianGrid);
	   	 B_House bHouse = (B_House) house;
	   	 if (v == Vehicle.walk) {
	   		 currentID++;
	   		 PersonGui g = new PersonGui(gui, currentID, aStarTraversal);
	   		 idList.put(currentID, g);
	   		 
	   		 Person p = new Person(name, g, role, v, m, new Money(500, 0), new Money(10, 0), 30, 4, bHouse.type, bHouse, b, shift);
	   		 p.setAnimPanel(this);
	   		 g.setPerson(p);
	   		 addGui(g);
	   		 if (God.Get().canAddPerson(p)){
	   	 	 God.Get().addPerson(p);
	   	 	 p.startThread();
	   		 }else return null;
	   	 	 
	   	 	 return p;
	   	 } else if (v == Vehicle.car) {
	   		 currentID++;
	   		 CarGui g = new CarGui(gui, currentID, createAccidents);
	   		 idList.put(currentID, g);
	   		 
	   		 Person p = new Person(name, g, role, v, m, new Money(500, 0), new Money(10, 0), 30, 4, bHouse.type, bHouse, b, shift);
	   		 p.setAnimPanel(this);
	   		 g.setPerson(p);
	   		 addGui(g);
	   		if (God.Get().canAddPerson(p)){
	   	 	 God.Get().addPerson(p);
	   	 	 p.startThread();   	 	 
	   		}else return null;
	   	 	 
	   	 	 return p;
	   	 } else if (v == Vehicle.bus) {
	   		 currentID++;
	   		 PersonGui g = new PersonGui(gui, currentID, aStarTraversal);
	   		 idList.put(currentID, g);
	   		 
	   		 Person p = new Person(name, g, role, v, m, new Money(500, 0), new Money(10, 0), 30, 4, bHouse.type, bHouse, b, shift);
	   		 p.setAnimPanel(this);
	   		 g.setPerson(p);
	   		 addGui(g);
	   		if (God.Get().canAddPerson(p)){
	   	 	 God.Get().addPerson(p);
	   	 	 p.startThread();
	   		}else return null;
	   	 	 
	   	 	 return p;
	   	 }
	   	 
	   	 return null;
    }
    
    // Create a person
    public Person createPerson(String name, String role, Vehicle v, Morality m, Money money, int hunger, Building house, Building b, int shift){
	   	 AStarTraversal aStarTraversal = new AStarTraversal(pedestrianGrid);
	   	 B_House bHouse = (B_House) house;
	   	 if (v == Vehicle.walk) {
	   		 currentID++;
	   		 PersonGui g = new PersonGui(gui, currentID, aStarTraversal);
	   		 idList.put(currentID, g);
	   		 
	   		 Person p = new Person(name, g, role, v, m, money, new Money(10, 0), hunger, 4, bHouse.type, bHouse, b, shift);
	   		 p.setAnimPanel(this);
	   		 g.setPerson(p);
	   		 addGui(g);
	   		 if (God.Get().canAddPerson(p)){
	   	 	 God.Get().addPerson(p);
	   	 	 p.startThread();
	   		 }else return null;
	   	 	 
	   	 	 return p;
	   	 } else if (v == Vehicle.car) {
	   		 currentID++;
	   		 CarGui g = new CarGui(gui, currentID, createAccidents);
	   		 idList.put(currentID, g);
	   		 
	   		 Person p = new Person(name, g, role, v, m, new Money(500, 0), new Money(10, 0), 30, 4, bHouse.type, bHouse, b, shift);
	   		 p.setAnimPanel(this);
	   		 g.setPerson(p);
	   		 addGui(g);
	   		if (God.Get().canAddPerson(p)){
	   	 	 God.Get().addPerson(p);
	   	 	 p.startThread();   	 	 
	   		}else return null;
	   	 	 
	   	 	 return p;
	   	 } else if (v == Vehicle.bus) {
	   		 currentID++;
	   		 PersonGui g = new PersonGui(gui, currentID, aStarTraversal);
	   		 idList.put(currentID, g);
	   		 
	   		 Person p = new Person(name, g, role, v, m, new Money(500, 0), new Money(10, 0), 30, 4, bHouse.type, bHouse, b, shift);
	   		 p.setAnimPanel(this);
	   		 g.setPerson(p);
	   		 addGui(g);
	   		if (God.Get().canAddPerson(p)){
	   	 	 God.Get().addPerson(p);
	   	 	 p.startThread();
	   		}else return null;
	   	 	 
	   	 	 return p;
	   	 }
	   	 
	   	 return null;
   }
    
    protected Person marketScenarioPerson(String name, String role, Vehicle v, Morality m, Building house, Building b){
	   	 AStarTraversal aStarTraversal = new AStarTraversal(pedestrianGrid);
   		 currentID++;
	   	 PersonGui g = new PersonGui(gui, currentID, aStarTraversal);
   		 idList.put(currentID, g);
   		 
	   	 Person p = new Person(name, g, role, v, m, new Money(100, 0), new Money(10, 0), 10, 4, "Apartment", (B_House)house, b, 1);
   		 p.setAnimPanel(this);
	   	 g.setPerson(p);
	   	 addGui(g);
	   	 God.Get().addPerson(p);
	   	 p.startThread();
	   	 
	   	 //test market
	   	 p.testMarket();
	   	 
	   	 return p;
    }
    
    protected Person timScenarioPerson(String name, String role, Vehicle v, Morality m, Building house, Building b){
         AStarTraversal aStarTraversal = new AStarTraversal(pedestrianGrid);
   		 currentID++;
         PersonGui g = new PersonGui(gui, currentID, aStarTraversal);
   		 idList.put(currentID, g);
   		 
         Person p = new Person(name, g, role, v, m, new Money(100, 0), new Money(10, 0), 10, 4, "Apartment", (B_House)house, b, 1);
   		 p.setAnimPanel(this);
         g.setPerson(p);
         addGui(g);
         God.Get().addPerson(p);
         p.startThread();
         
         //test market
         p.testTim();
         
         return p;
    }
    
    public void setScrollPane(JScrollPane s) {
    	scrollPane = s;
    }
    
    public void enterBus(Person p) {
  	 	 standees.get(p.building.getID()).add(p);
    }
    
    // Clears vehicle grid upon exiting a space
    public void clearVGrid(int id) {
		for (int i = 0; i < 30; i++) {
		for (int j = 0; j < 30; j++) {
			if (vehicleGrid[i][j] == id) {
				vehicleGrid[i][j] = 0;
				break;
			}
		}
		}
    }
    
    public void setVGrid(int x, int y, int id) {
    	vehicleGrid[x][y] = id;
    }
    
    public void setShowRect(boolean show) {
    	SHOW_RECT = show;
    }
    
    public int getBuildingX(int position) {
    	return ((position % 4) * 7 + 2)*64;
    }
    
    public int getBuildingY(int position) {
    	return ((position / 4) * 7 + 7)*64;
    }
    
    public void spawnBuses() {
	 	currentID++;
	 	BusGui g1 = new BusGui(gui, currentID);
	 	addGui(g1);
  		idList.put(currentID, g1);
	 	
	 	currentID++;
	 	BusGui g2 = new BusGui(gui, currentID);
	 	addGui(g2);
  		idList.put(currentID, g2);
    }
    
    // In event of a PersonGui losing their car
    public PersonGui getNewGui(Person p) {
    	guis.remove(p.gui);
        AStarTraversal aStarTraversal = new AStarTraversal(pedestrianGrid);
  		currentID++;
        PersonGui g = new PersonGui(gui, currentID, aStarTraversal);
  		idList.put(currentID, g);
  		g.setPerson(p);
  		addGui(g);
        return g;
    }
    
    // In event of a PersonGui gaining a car
    public CarGui getNewCarGui(Person p) {
    	guis.remove(p.gui);
  		currentID++;
  		CarGui g = new CarGui(gui, currentID, createAccidents);
  		idList.put(currentID, g);
  		g.setPerson(p);
  		addGui(g);
        return g;
    }
    
    public void SendMangersHome(){
    	God.Get().getOffWork(God.Get().getBuilding(6));
    }
}
