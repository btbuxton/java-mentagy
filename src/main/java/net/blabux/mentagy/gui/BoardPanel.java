package net.blabux.mentagy.gui;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class BoardPanel extends JPanel {
	private static final long serialVersionUID = 6041698819860165194L;
	
	JFrame frame;
	
	public void show() {
		frame = new JFrame("Mentagy");
		frame.add(this);
		frame.setSize(640, 480);
		frame.setVisible(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

}
