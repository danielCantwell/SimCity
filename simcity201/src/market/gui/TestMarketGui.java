package market.gui;

import java.awt.BorderLayout;

import javax.swing.JFrame;

public class TestMarketGui extends JFrame
{
    /**
     * 
     */
    private static final long serialVersionUID = 8675232995950198044L;
    MarketAnimationPanel animationPanel = new MarketAnimationPanel("Ralph's");
    
    public TestMarketGui()
    {
        int WINDOWX = 800;
        int WINDOWY = 800;
        
        setBounds(50, 50, WINDOWX, WINDOWY);
        setLayout(new BorderLayout());
        this.add(animationPanel);
    }
    
    public static void main(String[] args) {
        TestMarketGui gui = new TestMarketGui();
        gui.setTitle("csci201 Market");
        gui.setVisible(true);
        gui.setResizable(false);
        gui.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
}
