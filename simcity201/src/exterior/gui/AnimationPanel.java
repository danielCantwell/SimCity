package exterior.gui;

import javax.imageio.ImageIO;
import javax.swing.*;

import SimCity.Base.Building;
import SimCity.Base.God;
import SimCity.Base.Person;
import SimCity.Base.Person.GoAction;
import SimCity.Base.Person.Intent;
import SimCity.Base.Person.Morality;
import SimCity.Base.Person.Vehicle;
import SimCity.Buildings.B_Bank;
import SimCity.Buildings.B_House;
import SimCity.Globals.Money;
import exterior.astar.AStarTraversal;

import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.concurrent.*; 
import java.util.List;

import exterior.astar.*;

public class AnimationPanel extends JPanel implements ActionListener {
    private List<Gui> guis = new ArrayList<Gui>();
    private SimCityGui gui;
    private final boolean SHOW_RECT = false;
    private final int WINDOWX = 1920;
    private final int WINDOWY = 1920; //1472
    private final int TILESIZE = 64;
    private final int CITY_SIZE = 4;
    private final char[][] MAP = new char[][] {
    		
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
    private String consoleText = "";
    
    private ImageIcon iconPedR = new ImageIcon("images/a_pedestrian_r.gif");
    private ImageIcon iconPedD = new ImageIcon("images/a_pedestrian_d.gif");
    private ImageIcon iconPedL = new ImageIcon("images/a_pedestrian_l.gif");
    private ImageIcon iconPedU = new ImageIcon("images/a_pedestrian_u.gif");
    private ImageIcon iconRoad = new ImageIcon("images/t_road.png");
    private ImageIcon iconRoadVL = new ImageIcon("images/t_road_vl.png");
    private ImageIcon iconRoadVR = new ImageIcon("images/t_road_vr.png");
    private ImageIcon iconRoadHL = new ImageIcon("images/t_road_hl.png");
    private ImageIcon iconRoadHR = new ImageIcon("images/t_road_hr.png");
    private ImageIcon iconCrossV = new ImageIcon("images/t_cross_v.png");
    private ImageIcon iconCrossH = new ImageIcon("images/t_cross_h.png");
    private ImageIcon iconBuildingA = new ImageIcon("images/t_building1.png");
    private ImageIcon iconBuildingB = new ImageIcon("images/t_building2.png");
	
	public AnimationPanel() {
		setSize(WINDOWX, WINDOWY);
		setVisible(true);
		Timer timer = new Timer(3, this);
		timer.start();

		God.Get().setAnimationPanel(this);

		// Set up semaphore grid - sidewalks and crosswalks are open
		for (int y = 0; y < WINDOWY / (TILESIZE); y++) {
			for (int x = 0; x < WINDOWX / (TILESIZE); x++) {
				pedestrianGrid[x][y] = new Semaphore(1, true);
				if (MAP[x][y] == 'R' || MAP[x][y] == 'B') {
					try {
						pedestrianGrid[x][y].acquire();
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		}

		addCommands();

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
				for (int i = 0; i < CITY_SIZE * CITY_SIZE; i++) {
					if (getBuildingRect(i).contains(e.getX(), e.getY())) {
						gui.buildingFrame.setVisible(true);
						System.out.println("MOUSE PRESS ON BUILDING: " + i);
						gui.buildingFrame.setTitle("Building #" + (i + 1)
								+ " - " + gui.buildingList.get(i).getTag());
					}
				}
			}
		});

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
				for (int i = 0; i < CITY_SIZE * CITY_SIZE; i++) {
					if (getBuildingRect(i).contains(e.getX(), e.getY())) {
						gui.buildingFrame.setVisible(true);
						System.out.println("MOUSE PRESS ON BUILDING: " + i);
						gui.buildingFrame.setTitle("Building #" + (i + 1)
								+ " - " + gui.buildingList.get(i).getTag());
						gui.cardLayout.show(gui.buildingPanels, "" + i);
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
				consoleText = "";
				for (int i = 0; i < CITY_SIZE * CITY_SIZE; i++) {
					if (getBuildingRect(i).contains(e.getX(), e.getY())) {
						consoleText = "Click to Enter Building" + i + ": " + gui.buildingList.get(i).getTag();
					}
				}
			}
		});
	}

	public void actionPerformed(ActionEvent e) {
		repaint();
	}

	public void paintComponent(Graphics g) {
		Graphics2D g2 = (Graphics2D) g;
		g2.setColor(getBackground());
		g2.fillRect(0, 0, WINDOWX, WINDOWY);

		// Draw the city based on the map array
		for (int y = 0; y < WINDOWY / TILESIZE; y++) {
			for (int x = 0; x < WINDOWX / TILESIZE; x++) {
				if (MAP[x][y] == 'R') {
					if (SHOW_RECT) {
						g2.setColor(Color.DARK_GRAY);
						g2.fillRect(x * TILESIZE, y * TILESIZE, TILESIZE,
								TILESIZE);
					}

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
						} else {
							iconRoad.paintIcon(this, g, x * TILESIZE, y
									* TILESIZE);
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
				if (MAP[x][y] == 'S') {
					if (SHOW_RECT) {
						g2.setColor(Color.LIGHT_GRAY);
						g2.fillRect(x * TILESIZE, y * TILESIZE, TILESIZE,
								TILESIZE);
					}

					if (MAP[x][y + 1] == 'S' && MAP[x + 1][y] == 'S'
							&& MAP[x + 1][y + 1] == 'B') {
						for (int i = 0; i < 16; i++) {
							if (getBuildingRect(i).contains(x * TILESIZE + TILESIZE * 2, y * TILESIZE + TILESIZE * 2)) {
								if (gui.buildingList.get(i).getTag() == "B_Bank"
										|| gui.buildingList.get(i).getTag() == "B_Market"
										|| gui.buildingList.get(i).getTag() == "B_Restaurant") {
									iconBuildingB.paintIcon(this, g, x * TILESIZE, y * TILESIZE);
								} else {
									iconBuildingA.paintIcon(this, g, x * TILESIZE, y * TILESIZE);
								}
								
								if (consoleText.indexOf(Character.forDigit(i, 10)) != -1) {
									g2.drawString(consoleText, (x+1) * TILESIZE, (y+1) * TILESIZE - 10);
								}
							}
						}

					}
				}
				if (MAP[x][y] == 'C') {
					if (SHOW_RECT) {
						g2.setColor(Color.GRAY);
						g2.fillRect(x * TILESIZE, y * TILESIZE, TILESIZE,
								TILESIZE);
					}

					if (MAP[x - 1][y] == 'C' || MAP[x + 1][y] == 'C') {
						iconCrossH.paintIcon(this, g, x * TILESIZE, y
								* TILESIZE);
					}
					if (MAP[x][y - 1] == 'C' || MAP[x][y + 1] == 'C') {
						iconCrossV.paintIcon(this, g, x * TILESIZE, y
								* TILESIZE);
					}
				}
				if (MAP[x][y] == 'B') {
					if (SHOW_RECT) {
						g2.setColor(Color.ORANGE);
						g2.fillRect(x * TILESIZE, y * TILESIZE, TILESIZE,
								TILESIZE);
					}
				}
			}
		}

		for (Gui gui : guis) {
			if (gui.isPresent()) {
				gui.updatePosition();
			}
		}

		for (Gui gui : guis) {
			if (gui.isPresent()) {
				gui.draw(g2);
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
		}
	}
    
    public void addGui(Gui gui) {
    	guis.add(gui);
    }
    
    public void setGui(SimCityGui g) {
    	gui = g;
    }
    
    private Rectangle getBuildingRect(int buildingNumber) {
    	return new Rectangle(((buildingNumber % CITY_SIZE) * 7 + 3)*TILESIZE, ((buildingNumber / CITY_SIZE) * 7 + 3)*TILESIZE, TILESIZE*3, TILESIZE*3);
    }
    
    protected void addCommands()
    {
        String stringCtrlN = "CTRL N";
        Action keyCtrlN = new AbstractAction()
        {
             public void actionPerformed(ActionEvent e)
             {
                 //System.out.println("Spawning a new pedestrian.");
            	 //AStarTraversal aStarTraversal = new AStarTraversal(pedestrianGrid);
            	 //PersonGui g = new PersonGui(gui, aStarTraversal);
            	 //Person p = new Person("Jesse", g, "Bank.bankManagerRole", Vehicle.walk, Morality.good, new Money(100, 0), new Money(10, 0), 20, 3, "Apartment", (B_House)gui.buildingList.get(0), gui.buildingList.get(2));
            	 
            	 
            	 //createPerson("Jesse", "Bank.bankManagerRole", Vehicle.walk, Morality.good, gui.buildingList.get(0), gui.buildingList.get(2));
            	 //createPerson("Brian", "Bank.tellerRole", Vehicle.walk, Morality.good, gui.buildingList.get(0), gui.buildingList.get(2));
            	 createPerson("Matt", "Bank.bankCustomerRole", Vehicle.walk, Morality.good, gui.buildingList.get(0), gui.buildingList.get(2));
            	 //createPerson("Omar", "Bank.bankGuardRole", Vehicle.walk, Morality.good, gui.buildingList.get(0), gui.buildingList.get(2));

            	 //g.setPerson(p);
            	 //addGui(g);
            	 //God.Get().addPerson(p);
            	 //p.startThread();
             }
        };
        
        Action keyCtrlM = new AbstractAction()
        {
             public void actionPerformed(ActionEvent e)
             {
            	 createPerson("Matt", "Bank.bankManagerRole", Vehicle.walk, Morality.good, gui.buildingList.get(0), gui.buildingList.get(2));
             }
        };
        
        Action keyCtrlG = new AbstractAction()
        {
             public void actionPerformed(ActionEvent e)
             {
            	 createPerson("Matt", "Bank.bankGuardRole", Vehicle.walk, Morality.good, gui.buildingList.get(0), gui.buildingList.get(2));
             }
        };
        
        Action keyCtrlT = new AbstractAction()
        {
             public void actionPerformed(ActionEvent e)
             {
            	 createPerson("Matt", "Bank.tellerRole", Vehicle.walk, Morality.good, gui.buildingList.get(0), gui.buildingList.get(2));
             }
        };
        
        Action keyCtrlP = new AbstractAction()
        {
             public void actionPerformed(ActionEvent e)
             {
            	marketScenarioPerson("Marketman", "Bank.bankCustomerRole", Vehicle.walk, Morality.good, gui.buildingList.get(0), gui.buildingList.get(2));
             }
        };
        

        getInputMap(this.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_N, KeyEvent.CTRL_MASK), stringCtrlN);
        getActionMap().put(stringCtrlN, keyCtrlN);
        String stringCtrlM = "CTRL M";
        getInputMap(this.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_M, KeyEvent.CTRL_MASK), stringCtrlM);
        getActionMap().put(stringCtrlM, keyCtrlM);
        String stringCtrlT = "CTRL T";
        getInputMap(this.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_T, KeyEvent.CTRL_MASK), stringCtrlT);
        getActionMap().put(stringCtrlT, keyCtrlT);
        String stringCtrlG = "CTRL G";
        getInputMap(this.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_G, KeyEvent.CTRL_MASK), stringCtrlG);
        getActionMap().put(stringCtrlG, keyCtrlG);
        String stringCtrlP = "CTRL P";
        getInputMap(this.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_P, KeyEvent.CTRL_MASK), stringCtrlP);
        getActionMap().put(stringCtrlP, keyCtrlP);
    }
    
    protected Person createPerson(String name, String role, Vehicle v, Morality m, Building house, Building b){
    	 System.out.println("Spawning a new pedestrian.");
	   	 AStarTraversal aStarTraversal = new AStarTraversal(pedestrianGrid);
	   	 PersonGui g = new PersonGui(gui, aStarTraversal);
	   	 Person p = new Person(name, g, role, v, m, new Money(100, 0), new Money(10, 0), 10, 4, "Apartment", (B_House)house, b);
	   	 g.setPerson(p);
	   	 addGui(g);
	   	 God.Get().addPerson(p);
	   	 p.startThread();
	   	 
	   	 //test market
	   	 //p.testMarket();
	   	 
	   	 return p;
    }
    
    protected Person marketScenarioPerson(String name, String role, Vehicle v, Morality m, Building house, Building b){
    	System.out.println("Spawning a new pedestrian.");
	   	 AStarTraversal aStarTraversal = new AStarTraversal(pedestrianGrid);
	   	 PersonGui g = new PersonGui(gui, aStarTraversal);
	   	 Person p = new Person(name, g, role, v, m, new Money(100, 0), new Money(10, 0), 10, 4, "Apartment", (B_House)house, b);
	   	 g.setPerson(p);
	   	 addGui(g);
	   	 God.Get().addPerson(p);
	   	 p.startThread();
	   	 
	   	 //test market
	   	 p.testMarket();
	   	 
	   	 return p;
    }

}
