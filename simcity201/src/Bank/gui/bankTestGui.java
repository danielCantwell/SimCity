package Bank.gui;

import java.awt.BorderLayout;

import javax.swing.JFrame;

public class bankTestGui extends JFrame
{
    /**
     * 
     */
    bankGui bankGui = new bankGui();
    
    public bankTestGui()
    {
        int WINDOWX = 640;
        int WINDOWY = 640;
        
        setBounds(50, 50, WINDOWX, WINDOWY);
        setLayout(new BorderLayout());
        this.add(bankGui);
    }
    
    public static void main(String[] args) {
    	bankTestGui gui = new bankTestGui();
        gui.setTitle("csci201 Bank");
        gui.setVisible(true);
        gui.setResizable(false);
        gui.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
}
