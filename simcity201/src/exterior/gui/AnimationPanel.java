package exterior.gui;

import housing.roles.OwnerRole;

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
import java.util.List;
import java.util.Random;
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
import exterior.astar.AStarTraversal;

public class AnimationPanel extends JPanel implements ActionListener {
    private List<Gui> guis = new ArrayList<Gui>();
    private SimCityGui gui;
    private JScrollPane scrollPane;
    private boolean SHOW_RECT = false;
    private final int WINDOWX = 1920;
    private final int WINDOWY = 1920; //1472
    private final int TILESIZE = 64;
    private final int CITY_SIZE = 4;
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
    private String consoleText = "";
    private Font font;
    private int currentID = 1;
    
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

		Timer trafficTimer = new Timer(10000, new ActionListener() {
			   public void actionPerformed(ActionEvent e) {
				   horizontalRedLight = !horizontalRedLight;
	    }});
		trafficTimer.start();
     
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
						consoleText = "Click to Enter #" + i + ": " + gui.buildingList.get(i).getTag();
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

		/*
        for (int y = 0; y < 30; y++) {
        	for (int x = 0; x < 30; x++) {
        		System.out.print(vehicleGrid[x][y] + " ");
        	}
            System.out.println("");
        }
        System.out.println("====");
		*/
		
		// Draw the city based on the map array
		for (int y = 0; y < WINDOWY / TILESIZE; y++) {
			for (int x = 0; x < WINDOWX / TILESIZE; x++) {
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
				}
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
									if (gui.buildingList.get(i).getTag() == "B_Bank"
											|| gui.buildingList.get(i).getTag() == "B_Market"
											|| gui.buildingList.get(i).getTag() == "B_Restaurant") {
										iconBuildingB.paintIcon(this, g, x * TILESIZE, y * TILESIZE);
									} else {
										iconBuildingA.paintIcon(this, g, x * TILESIZE, y * TILESIZE);
									}
									
									if (consoleText.indexOf(i + "") != -1) {
										g2.drawString(consoleText, (x+1) * TILESIZE, (y+1) * TILESIZE - 10);
									}
								}
							}
						}
					}
				}
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
				if (MAP[x][y] == 'B') {
					if (SHOW_RECT) {
						g2.setColor(Color.ORANGE);
						g2.fillRect(x * TILESIZE, y * TILESIZE, TILESIZE, TILESIZE);
					}
				}
			}
			
			g2.setColor(Color.WHITE);
			g2.setFont(font);
			g2.drawString("Time of Day: " + God.Get().getHour() + ":00", 20 + scrollPane.getHorizontalScrollBar().getValue(), 30 + scrollPane.getVerticalScrollBar().getValue());
            g2.drawString("Current Day: " + (God.Get().getDay()+1), 20 + scrollPane.getHorizontalScrollBar().getValue(), 60 + scrollPane.getVerticalScrollBar().getValue());
            g2.drawString("Horizontal red light? : " + horizontalRedLight, 20 + scrollPane.getHorizontalScrollBar().getValue(), 90 + scrollPane.getVerticalScrollBar().getValue());
		}

		for (Gui gui : guis) {
			if (gui.isPresent()) {
				gui.updatePosition();
			}
		}

		for (Gui gui : guis) {
			if (gui.isPresent()) {
				gui.draw(g2);
				if (gui.getType() == "Person") {
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
    
    private Rectangle getBuildingRect(int buildingNumber) {
    	return new Rectangle(((buildingNumber % CITY_SIZE) * 7 + 3)*TILESIZE, ((buildingNumber / CITY_SIZE) * 7 + 3)*TILESIZE, TILESIZE*3, TILESIZE*3);
    }
    
    protected void addCommands()
    {
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
            	 createPerson("Matt", "Bank.bankManagerRole", Vehicle.car, Morality.good, gui.buildingList.get(0), gui.buildingList.get(2));
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
        
        Action keyCtrlY = new AbstractAction()
        {
             public void actionPerformed(ActionEvent e)
             {
                createPerson("Manny", "market.MarketManagerRole", Vehicle.car, Morality.good, gui.buildingList.get(0), gui.buildingList.get(3));
             }
        };
        
        Action keyCtrlK = new AbstractAction()
        {
             public void actionPerformed(ActionEvent e)
             {
                 createPerson("Clark", "market.MarketClerkRole", Vehicle.walk, Morality.good, gui.buildingList.get(0), gui.buildingList.get(3));
             }
        };
        
        Action keyCtrlP = new AbstractAction()
        {
             public void actionPerformed(ActionEvent e)
             {
                 createPerson("Parker", "market.MarketPackerRole", Vehicle.walk, Morality.good, gui.buildingList.get(0), gui.buildingList.get(3));
             }
        };
        
        Action keyCtrl3 = new AbstractAction()
        {
             public void actionPerformed(ActionEvent e)
             {
                 marketScenarioPerson("Customer", "Bank.tellerRole", Vehicle.walk, Morality.good, gui.buildingList.get(0), gui.buildingList.get(2));
             }
        };
        
        Action keyCtrlC = new AbstractAction()
        {
             public void actionPerformed(ActionEvent e)
             {
            	SHOW_RECT = !SHOW_RECT;
            	if (SHOW_RECT) {
            		System.out.println("Entering compatibility mode.");
            		System.out.println("To view people in compatibility mode, change SHOW_RECT in Gui.java to true.");
            	} else {
            		System.out.println("Exiting compatibility mode.");
            	}
             }
        };
        
        Action keyCtrlQ = new AbstractAction()
        {
             public void actionPerformed(ActionEvent e)
             {
            	 currentID++;
            	 CarGui g = new CarGui(gui, currentID);
            	 addGui(g);
             }
        };
        
        String stringCtrlN = "CTRL N";
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
        String stringCtrlC = "CTRL C";
        getInputMap(this.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_C, KeyEvent.CTRL_MASK), stringCtrlC);
        getActionMap().put(stringCtrlC, keyCtrlC);
        String stringCtrlQ = "CTRL Q";
        getInputMap(this.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_Q, KeyEvent.CTRL_MASK), stringCtrlQ);
        getActionMap().put(stringCtrlQ, keyCtrlQ);
    }
    
    public Person createPerson(String name, String role, Vehicle v, Morality m, Building house, Building b){
    	 System.out.println("Spawning a new pedestrian.");
	   	 AStarTraversal aStarTraversal = new AStarTraversal(pedestrianGrid);
	   	 if (v == Vehicle.walk) {
	   		 PersonGui g = new PersonGui(gui, aStarTraversal);
	   		 Person p = new Person(name, g, role, v, m, new Money(100, 0), new Money(10, 0), 10, 4, "Apartment", (B_House)house, b);
	   		 g.setPerson(p);
	   		 addGui(g);
	   	 	 God.Get().addPerson(p);
	   	 	 p.startThread();
	   	 	 return p;
	   	 } else if (v == Vehicle.car) {
	   		 currentID++;
	   		 CarGui g = new CarGui(gui, currentID);
	   		 Person p = new Person(name, g, role, v, m, new Money(100, 0), new Money(10, 0), 10, 4, "Apartment", (B_House)house, b);
	   		 g.setPerson(p);
	   		 addGui(g);
	   	 	 God.Get().addPerson(p);
	   	 	 p.startThread();
	   	 	 return p;
	   	 }
	   	 
	   	 //test market
	   	 //p.testMarket();
	   	 
	   	 return null;
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
    
    public void setScrollPane(JScrollPane s) {
    	scrollPane = s;
    }
    
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

}
