/**
 * 
 */
package restaurant.gui;

import java.awt.Container;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import restaurant.DannyAbstractWaiter;
import restaurant.DannyCook;

/**
 * @author Daniel
 * 
 */
public class DannyRestaurantPanel extends JPanel implements ActionListener {

	private final int WINDOWX = 640;
	private final int WINDOWY = 640;

	public DannyRestaurantAnimationPanel dap = new DannyRestaurantAnimationPanel();
	public JPanel infoPanel = new JPanel();
	public JPanel leftInfo = new JPanel();
	public JPanel rightInfo = new JPanel();

	public JLabel leftInfoLabel = new JLabel("Left info");
	public JLabel rightInfoLabel = new JLabel("Right info");

	public JScrollPane pane = new JScrollPane(
			JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
			JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
	private JPanel view = new JPanel();
	private List<People> list = new ArrayList<People>();

	public DannyRestaurantPanel() {
		setupInfoPanel();
		init();
		setVisible(true);
	}

	private void init() {
		setSize(WINDOWX, WINDOWY);
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		dap.setSize(WINDOWX, WINDOWY - 100);
		Dimension dim = new Dimension(WINDOWX, WINDOWY - 200);
		dap.setPreferredSize(dim);
		dap.setMaximumSize(dim);
		dap.setMinimumSize(dim);
		add(dap);
		add(infoPanel);
	}

	private void setupInfoPanel() {
		infoPanel.setSize(WINDOWX, 100);
		Dimension dim = new Dimension(WINDOWX, 200);
		infoPanel.setPreferredSize(dim);
		infoPanel.setMaximumSize(dim);
		infoPanel.setMinimumSize(dim);
		infoPanel.add(leftInfo);
		infoPanel.add(rightInfo);
		setupLeftInfo();
		setupRightInfo();
	}

	private void setupLeftInfo() {
		Dimension dim = new Dimension(200, 200);
		leftInfo.setPreferredSize(dim);
		leftInfo.setMaximumSize(dim);
		leftInfo.setMinimumSize(dim);
		leftInfo.setLayout(new BoxLayout(leftInfo, BoxLayout.Y_AXIS));
		leftInfo.add(leftInfoLabel);
	}

	private void setupRightInfo() {
		Dimension dim2 = new Dimension(200, 200);
		rightInfo.setPreferredSize(dim2);
		rightInfo.setMaximumSize(dim2);
		rightInfo.setMinimumSize(dim2);
		rightInfo.setLayout(new BoxLayout(rightInfo, BoxLayout.Y_AXIS));
		rightInfo.add(rightInfoLabel);

		view.setLayout(new BoxLayout((Container) view, BoxLayout.Y_AXIS));
		pane.setViewportView(view);

		Dimension dim = new Dimension(100, 100);
		pane.setPreferredSize(dim);
		pane.setMaximumSize(dim);
		pane.setMinimumSize(dim);
		rightInfo.add(pane);
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		// TODO Auto-generated method stub

	}

	private class People {
		JCheckBox button;
		DannyCook cook;

		private People(JCheckBox b, DannyCook c) {
			button = b;
			cook = c;
		}

	}

}
