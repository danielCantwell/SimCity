package exterior.gui;

import javax.imageio.ImageIO;
import javax.swing.*;

import SimCity.Base.Building;
import SimCity.Base.Person;
import SimCity.Base.Person.Morality;
import SimCity.Base.Person.Vehicle;
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
    
	private ImageIcon iconPedR = new ImageIcon("images/a_pedestrian_r.gif");
	private ImageIcon iconPedD = new ImageIcon("images/a_pedestrian_d.gif");
	private ImageIcon iconPedL = new ImageIcon("images/a_pedestrian_l.gif");
	private ImageIcon iconPedU = new ImageIcon("images/a_pedestrian_u.gif");
	private ImageIcon iconRoad = new ImageIcon("images/t_road.png");
	private ImageIcon iconSide = new ImageIcon("images/t_side_r.png");
	private ImageIcon iconCrossV = new ImageIcon("images/t_cross_v.png");
	private ImageIcon iconCrossH = new ImageIcon("images/t_cross_h.png");
	
    public AnimationPanel() {
        setSize(WINDOWX, WINDOWY);
        setVisible(true);
    	Timer timer = new Timer(1, this);
    	timer.start();
    	
    	// Set up semaphore grid - sidewalks and crosswalks are open
        for (int y = 0; y < WINDOWY/(TILESIZE); y++) {
        for (int x = 0; x < WINDOWX/(TILESIZE); x++) {
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
            	for (int i = 0; i < CITY_SIZE*CITY_SIZE; i++) {
            		if (getBuildingRect(i).contains(e.getX(), e.getY())) {
            			gui.buildingFrame.setVisible(true);
            			System.out.println("MOUSE PRESS ON BUILDING: " + i);
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
            	for (int i = 0; i < CITY_SIZE*CITY_SIZE; i++) {
            		if (getBuildingRect(i).contains(e.getX(), e.getY())) {
            			//System.out.println("MOUSE ON BUILDING: " + i);
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
        g2.fillRect(0, 0, WINDOWX, WINDOWY );
        
        // Draw the city based on the map array
        for (int y = 0; y < WINDOWY/TILESIZE; y++) {
        for (int x = 0; x < WINDOWX/TILESIZE; x++) {
        	if (MAP[x][y] == 'R') {
        		if (SHOW_RECT) {
        			g2.setColor(Color.DARK_GRAY);
        			g2.fillRect(x*TILESIZE, y*TILESIZE, TILESIZE, TILESIZE);
        		}

        		iconRoad.paintIcon(this, g, x*TILESIZE, y*TILESIZE);
        	}
        	if (MAP[x][y] == 'S') {
        		if (SHOW_RECT) {
        			g2.setColor(Color.LIGHT_GRAY);
        			g2.fillRect(x*TILESIZE, y*TILESIZE, TILESIZE, TILESIZE);
        		}

        		iconSide.paintIcon(this, g, x*TILESIZE, y*TILESIZE);
        	}
        	if (MAP[x][y] == 'C') {
        		if (SHOW_RECT) {
        			g2.setColor(Color.GRAY);
        			g2.fillRect(x*TILESIZE, y*TILESIZE, TILESIZE, TILESIZE);
        		}
        		
        		if (MAP[x-1][y] == 'C' || MAP[x+1][y] == 'C') {
        			iconCrossH.paintIcon(this, g, x*TILESIZE, y*TILESIZE);
        		} 
        		if (MAP[x][y-1] == 'C' || MAP[x][y+1] == 'C') {
        			iconCrossV.paintIcon(this, g, x*TILESIZE, y*TILESIZE);
        		} 
        	}
        	if (MAP[x][y] == 'B') {
        		if (SHOW_RECT) {
        			g2.setColor(Color.ORANGE);
        			g2.fillRect(x*TILESIZE, y*TILESIZE, TILESIZE, TILESIZE);
        		}
        	}
        }
        }
        
        for(Gui gui : guis) {
            if (gui.isPresent()) {
                gui.updatePosition();
            }
        }

        for(Gui gui : guis) {
            if (gui.isPresent()) {
                gui.draw(g2);
                if (gui.getRotation() == 0) {
                	iconPedR.paintIcon(this, g, gui.getX() + 16, gui.getY() + 32);
                } else if (gui.getRotation() == 1) {
                	iconPedD.paintIcon(this, g, gui.getX() + 32, gui.getY() + 16);
                } else if (gui.getRotation() == 2) {
                	iconPedL.paintIcon(this, g, gui.getX() + 16, gui.getY() + 00);
                } else if (gui.getRotation() == 3) {
                	iconPedU.paintIcon(this, g, gui.getX() + 00, gui.getY() + 16);
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
                 System.out.println("Spawning a new pedestrian.");
            	 AStarTraversal aStarTraversal = new AStarTraversal(pedestrianGrid);
            	 PersonGui g = new PersonGui(gui, aStarTraversal);
            	 Person p = new Person("Jesse", g, "Bank.bankCustomerRole", Vehicle.walk, Morality.good, new Money(100, 0), new Money(10, 0), 20, 3, "Apartment", (B_House)gui.buildingList.get(0), gui.buildingList.get(2));
            	 g.setPerson(p);
            	 addGui(g);
            	 p.startThread();
             }
        };

        getInputMap(this.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_N, KeyEvent.CTRL_MASK), stringCtrlN);
        getActionMap().put(stringCtrlN, keyCtrlN);
    }
}
