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
import exterior.astar.AStarTraversal;
import brianRest.*;

public class AnimationPanel extends JPanel implements ActionListener {
    private List<Gui> guis = new ArrayList<Gui>();
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
    private String consoleText = "";
    private Font font;
    public int currentID = 1;
    
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

		Timer trafficTimer = new Timer(5000, new ActionListener() {
			   public void actionPerformed(ActionEvent e) {
				   horizontalRedLight = !horizontalRedLight;
	    }});
		trafficTimer.start();
		
		Timer busTimer = new Timer(2000, new ActionListener() {
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
				for (int i = 0; i < CITY_SIZE * CITY_SIZE; i++) {
					if (getBuildingRect(i).contains(e.getX(), e.getY())) {
						gui.buildingFrame.setVisible(true);
						System.out.println("MOUSE PRESS ON BUILDING: " + i);
						gui.buildingFrame.setTitle("Building #" + i
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
						gui.buildingFrame.setTitle("Building #" + i
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
	
	@Override
    public void actionPerformed(ActionEvent arg0) {
        for (Gui gui : guis) {
            if (gui.isPresent()) {
                gui.updatePosition();
            }
        }
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

		for (int i = 0; i < CITY_SIZE*CITY_SIZE; i++) {
			for (int j = 0; (j < standees.get(i).size() && j < 6); j++) {
				iconClock.paintIcon(this, g, getBuildingX(i) + j*32 + 64, getBuildingY(i) - 64);
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
				else if (gui.getType() == "Car") {
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
        Action keyCtrlZ = new AbstractAction()
        {
             public void actionPerformed(ActionEvent e)
             {

                 //System.out.println("Spawning a new pedestrian.");
            	 //AStarTraversal aStarTraversal = new AStarTraversal(pedestrianGrid);
            	 //PersonGui g = new PersonGui(gui, aStarTraversal);
            	 //Person p = new Person("Jesse", g, "Bank.bankManagerRole", Vehicle.walk, Morality.good, new Money(100, 0), new Money(10, 0), 20, 3, "Apartment", (B_House)gui.buildingList.get(0), gui.buildingList.get(2));
            
            	 createPerson("Jesse", "Bank.bankManagerRole", Vehicle.walk, Morality.good, gui.buildingList.get(0), gui.buildingList.get(2), 1);
            	 createPerson("Brian", "Bank.tellerRole", Vehicle.walk, Morality.good, gui.buildingList.get(0), gui.buildingList.get(2), 1);
            	 createPerson("Matt", "Bank.bankCustomerRole", Vehicle.walk, Morality.good, gui.buildingList.get(0), gui.buildingList.get(2), 1);
            	 createPerson("Omar", "Bank.bankGuardRole", Vehicle.walk, Morality.good, gui.buildingList.get(0), gui.buildingList.get(2), 1);
             }
        };
        
        Action keyCtrlE = new AbstractAction()
        {
        	public void actionPerformed(ActionEvent e)
        	{
        		createPerson("EHost", "EricRestaurant.EricHost", Vehicle.walk, Morality.good, gui.buildingList.get(0), gui.buildingList.get(11), 1);
        		createPerson("ECustomer", "EricRestaurant.EricCustomer", Vehicle.walk, Morality.good, gui.buildingList.get(0), gui.buildingList.get(11), 1);
           	 	createPerson("EWaiter", "EricRestaurant.EricWaiter", Vehicle.walk, Morality.good, gui.buildingList.get(0), gui.buildingList.get(11), 1);
           	 	createPerson("EWaiter", "EricRestaurant.EricWaiter", Vehicle.walk, Morality.good, gui.buildingList.get(0), gui.buildingList.get(11), 1);
        		createPerson("ECustomer2", "EricRestaurant.EricCustomer", Vehicle.walk, Morality.good, gui.buildingList.get(0), gui.buildingList.get(11), 1);
           	 	createPerson("ECashier", "EricRestaurant.EricCashier", Vehicle.walk, Morality.good, gui.buildingList.get(0), gui.buildingList.get(11), 1);
           	 	createPerson("ECook", "EricRestaurant.EricCook", Vehicle.walk, Morality.good, gui.buildingList.get(0), gui.buildingList.get(11), 1); 
        	}
        };
        Action keyCtrlJ = new AbstractAction()
        {
        	public void actionPerformed(ActionEvent e)
        	{
        		createPerson("JHost", "jesseRest.JesseHost", Vehicle.walk, Morality.good, gui.buildingList.get(0), gui.buildingList.get(7), 1);
        		createPerson("JCustomer", "jesseRest.JesseCustomer", Vehicle.walk, Morality.good, gui.buildingList.get(0), gui.buildingList.get(7), 1);
           	 	createPerson("JWaiter", "jesseRest.JesseWaiter", Vehicle.walk, Morality.good, gui.buildingList.get(0), gui.buildingList.get(7), 1);
           	 	createPerson("JCashier", "jesseRest.JesseCashier", Vehicle.walk, Morality.good, gui.buildingList.get(0), gui.buildingList.get(7), 1);
           	 	createPerson("JCook", "jesseRest.JesseCook", Vehicle.walk, Morality.good, gui.buildingList.get(0), gui.buildingList.get(7), 1); 
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
                 createPerson("Will", "timRest.TimHostRole", Vehicle.car, Morality.good, gui.buildingList.get(13), gui.buildingList.get(10), 1);
                 createPerson("Johnson", "timRest.TimWaiterRole", Vehicle.walk, Morality.good, gui.buildingList.get(13), gui.buildingList.get(10), 1);
                 createPerson("Rob", "timRest.TimCookRole", Vehicle.walk, Morality.good, gui.buildingList.get(13), gui.buildingList.get(10), 1);
                 createPerson("Alex", "timRest.TimCashierRole", Vehicle.walk, Morality.good, gui.buildingList.get(13), gui.buildingList.get(10), 1);
             }
        };
        
        Action keyCtrl5 = new AbstractAction()
        {
             public void actionPerformed(ActionEvent e)
             {
                 createPerson("Tim", "timRest.TimCustomerRole", Vehicle.walk, Morality.good, gui.buildingList.get(1), gui.buildingList.get(10),1);
                 timScenarioPerson("Graham", "market.MarketPackerRole", Vehicle.walk, Morality.good, gui.buildingList.get(1), gui.buildingList.get(3));
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
            	 //CarGui g = new CarGui(gui, currentID);
              	 createPerson("Matt", "Bank.bankCustomerRole", Vehicle.bus, Morality.good, gui.buildingList.get(0), gui.buildingList.get(2), 1);

             }
        };
        
        Action keyCtrl0 = new AbstractAction()
        {
             public void actionPerformed(ActionEvent e)
             {
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
    }
    
    public Person createPerson(String name, String role, Vehicle v, Morality m, Building house, Building b, int shift){
    	 System.out.println("Spawning a new pedestrian.");
	   	 AStarTraversal aStarTraversal = new AStarTraversal(pedestrianGrid);
	   	 B_House bHouse = (B_House) house;
	   	 if (v == Vehicle.walk) {
	   		 PersonGui g = new PersonGui(gui, aStarTraversal);
	   		 Person p = new Person(name, g, role, v, m, new Money(100, 0), new Money(10, 0), 10, 4, bHouse.type, bHouse, b, shift);
	   		 g.setPerson(p);
	   		 addGui(g);
	   	 	 God.Get().addPerson(p);
	   	 	 p.startThread();
	   	 	 
	   	 	 return p;
	   	 } else if (v == Vehicle.car) {
	   		 currentID++;
	   		 CarGui g = new CarGui(gui, currentID);
	   		 Person p = new Person(name, g, role, v, m, new Money(100, 0), new Money(10, 0), 10, 4, bHouse.type, bHouse, b, shift);
	   		 g.setPerson(p);
	   		 addGui(g);
	   	 	 God.Get().addPerson(p);
	   	 	 p.startThread();   	 	 
	   	 	 
	   	 	 return p;
	   	 } else if (v == Vehicle.bus) {
	   		 PersonGui g = new PersonGui(gui, aStarTraversal);
	   		 Person p = new Person(name, g, role, v, m, new Money(100, 0), new Money(10, 0), 10, 4, bHouse.type, bHouse, b, shift);
	   		 g.setPerson(p);
	   		 addGui(g);
	   	 	 God.Get().addPerson(p);
	   	 	 p.startThread();
	   	 	 standees.get(p.building.getID()-1).add(p);
	   	 	 
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
	   	 Person p = new Person(name, g, role, v, m, new Money(100, 0), new Money(10, 0), 10, 4, "Apartment", (B_House)house, b, 1);
	   	 g.setPerson(p);
	   	 addGui(g);
	   	 God.Get().addPerson(p);
	   	 p.startThread();
	   	 
	   	 //test market
	   	 p.testMarket();
	   	 
	   	 return p;
    }
    
    protected Person timScenarioPerson(String name, String role, Vehicle v, Morality m, Building house, Building b){
        System.out.println("Spawning a new pedestrian.");
         AStarTraversal aStarTraversal = new AStarTraversal(pedestrianGrid);
         PersonGui g = new PersonGui(gui, aStarTraversal);
         Person p = new Person(name, g, role, v, m, new Money(100, 0), new Money(10, 0), 10, 4, "Apartment", (B_House)house, b, 1);
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
	 	
	 	currentID++;
	 	BusGui g2 = new BusGui(gui, currentID);
	 	addGui(g2);
    }

}
