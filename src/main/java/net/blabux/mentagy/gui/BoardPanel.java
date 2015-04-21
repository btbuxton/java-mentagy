package net.blabux.mentagy.gui;

import java.util.Arrays;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JPanel;

import net.blabux.mentagy.domain.Board;

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
	
	private Board intializeBoard() {
		Board board = new Board();
		List<String> boardText=Arrays.asList("-----*","N-*J*-", "T--*--", "-*--*D", "*-Z*A-","---*-*");
		board.parse(boardText.stream());
		return board;
	}

}
