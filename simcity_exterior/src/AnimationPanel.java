import javax.swing.*;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.concurrent.*; 
import java.util.List;

import astar.*;

public class AnimationPanel extends JPanel implements ActionListener {
    private List<Gui> guis = new ArrayList<Gui>();
    private SimCityGui gui;
    private final int WINDOWX = 1920;
    private final int WINDOWY = 1920;
    private final int TILESIZE = 64;
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
    
    private Semaphore[][] pedestrianGrid = new Semaphore[WINDOWX/(TILESIZE/2)][WINDOWY/(TILESIZE/2)];
    
    public AnimationPanel() {
        setSize(WINDOWX, WINDOWY);
        setVisible(true);
    	Timer timer = new Timer(1, this);
    	timer.start();
    	
        for (int x = 0; x < WINDOWX/(TILESIZE/2); x++) {
        for (int y = 0; y < WINDOWY/(TILESIZE/2); y++) {
        	pedestrianGrid[x][y] = new Semaphore(1, true);
        }
        }
        
        for (int i = 0; i < WINDOWX/TILESIZE; i++) {
        for (int j = 0; j < WINDOWY/TILESIZE; j++) {
        	if (MAP[i][j] == 'R' || MAP[i][j] == 'B') {
        		try {
    				pedestrianGrid[i*2][j*2].acquire();
    				pedestrianGrid[i*2+1][j*2].acquire();
    				pedestrianGrid[i*2][j*2+1].acquire();
    				pedestrianGrid[i*2+1][j*2+1].acquire();
    			} catch (InterruptedException e) {
    				e.printStackTrace();
    			}
        	}
        }
        }
        
        for (int x = 0; x < WINDOWX/(TILESIZE/2); x++) {
        for (int y = 0; y < WINDOWY/(TILESIZE/2); y++) {
        	System.out.print(pedestrianGrid[x][y].availablePermits() + " ");
        }
        	System.out.println("");
        }
        
    	for (int p = 0; p < 1; p++) {
    		AStarTraversal aStarTraversal = new AStarTraversal(pedestrianGrid);
    		PersonGui g = new PersonGui(gui, aStarTraversal);
    		addGui(g);
    		//CarGui h = new CarGui(gui);
    		//addGui(h);
    	}
    }

	public void actionPerformed(ActionEvent e) {
		repaint();
	}

    public void paintComponent(Graphics g) {
        // Clear the screen by painting a rectangle the size of the frame
        Graphics2D g2 = (Graphics2D) g;
        g2.setColor(getBackground());
        g2.fillRect(0, 0, WINDOWX, WINDOWY );
        
        for (int x = 0; x < 30; x++) {
        for (int y = 0; y < 30; y++) {
        	if (MAP[y][x] == 'R') {
        		g2.setColor(Color.DARK_GRAY);
        		g2.fillRect(x*TILESIZE, y*TILESIZE, TILESIZE, TILESIZE);
        	}
        	if (MAP[y][x] == 'S') {
        		g2.setColor(Color.LIGHT_GRAY);
        		g2.fillRect(x*TILESIZE, y*TILESIZE, TILESIZE, TILESIZE);
        	}
        	if (MAP[y][x] == 'C') {
        		g2.setColor(Color.GRAY);
        		g2.fillRect(x*TILESIZE, y*TILESIZE, TILESIZE, TILESIZE);
        	}
        	if (MAP[y][x] == 'B') {
        		g2.setColor(Color.ORANGE);
        		g2.fillRect(x*TILESIZE, y*TILESIZE, TILESIZE, TILESIZE);
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
            }
        }
    }
    
    public void addGui(Gui gui) {
    	guis.add(gui);
    }
    
    public void setGui(SimCityGui g) {
    	gui = g;
    }
}
