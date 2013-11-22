/**
 * 
 */
package housing.test;


import java.awt.BorderLayout;

import javax.swing.JFrame;

import housing.gui.HousingAnimation;

/**
 * @author Daniel
 *
 */
public class HousingGuiTest extends JFrame {
	
	HousingAnimation animationPanel = new HousingAnimation();
	
	public HousingGuiTest() {
		int WINDOWX = 800;
        int WINDOWY = 800;
        
        setBounds(50, 50, WINDOWX, WINDOWY);
        setLayout(new BorderLayout());
        this.add(animationPanel);
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		HousingGuiTest gui = new HousingGuiTest();
        gui.setTitle("SimCity House");
        gui.setVisible(true);
        gui.setResizable(false);
        gui.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

}
