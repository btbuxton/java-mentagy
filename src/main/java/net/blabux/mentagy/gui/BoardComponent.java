package net.blabux.mentagy.gui;

import java.awt.GridLayout;
import java.util.Arrays;
import java.util.List;
import java.util.stream.IntStream;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.BevelBorder;

import net.blabux.mentagy.domain.Board;

public class BoardComponent extends JPanel {
	private static final long serialVersionUID = 6041698819860165194L;
	JFrame frame;
	Board board;

	public BoardComponent() {
		initialize();
	}

	private void initialize() {
		initializeBoard();
		setBorder(new BevelBorder(BevelBorder.LOWERED));
		setLayout(new GridLayout(3, 3, 2, 2));
		IntStream.range(0, 3).forEach((y) -> {
			IntStream.range(0, 3).forEach((x) -> {
				add(new BoxComponent(board.box(x, y)));
			});
		});
	}

	private Board initializeBoard() {
		board = new Board();
		List<String> boardText=Arrays.asList("-----*","N-*J*-", "T--*--", "-*--*D", "*-Z*A-","---*-*");
		board.parse(boardText.stream());
		return board;
	}

}
