package net.blabux.mentagy.gui;

import java.awt.BorderLayout;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.stream.IntStream;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;

import net.blabux.mentagy.domain.Board;
import net.blabux.mentagy.domain.Piece;

public class GameComponent extends JPanel {
	private static final long serialVersionUID = 7786205238285692620L;
	private BoardComponent board;

	public GameComponent() {
		initialize();
	}

	private JComponent createBottomPanel(Iterator<Piece> pieces) {
		JPanel result = new JPanel();
		result.setBorder(createPanelBorder());
		result.setLayout(new BoxLayout(result, BoxLayout.X_AXIS));

		IntStream.range(0, 10).forEach((index) -> {
			result.add(Box.createHorizontalGlue());
			result.add(createFreePiece(pieces));
		});
		result.remove(0);
		return result;
	}

	private JComponent createFreePiece(Iterator<Piece> pieces) {
		return new PieceComponent(board, pieces.next());
	}

	private JComponent createLeftPanel(Iterator<Piece> pieces) {
		JPanel result = new JPanel();
		result.setBorder(createPanelBorder());
		result.setLayout(new BoxLayout(result, BoxLayout.Y_AXIS));

		IntStream.range(0, 8).forEach((index) -> {
			result.add(Box.createVerticalGlue(), 0);
			result.add(createFreePiece(pieces), 0);
		});
		result.remove(result.getComponentCount() - 1);
		return result;
	}

	private Border createPanelBorder() {
		return new EmptyBorder(5, 5, 5, 5);
	}

	private JComponent createRightPanel(Iterator<Piece> pieces) {
		JPanel result = new JPanel();
		result.setBorder(createPanelBorder());
		result.setLayout(new BoxLayout(result, BoxLayout.Y_AXIS));

		IntStream.range(0, 8).forEach((index) -> {
			result.add(Box.createVerticalGlue());
			result.add(createFreePiece(pieces));
		});
		result.remove(0);
		return result;
	}

	private JComponent createTopPanel(Iterator<Piece> pieces) {
		JPanel result = new JPanel();
		result.setBorder(createPanelBorder());
		result.setLayout(new BoxLayout(result, BoxLayout.X_AXIS));

		IntStream.range(0, 10).forEach((index) -> {
			result.add(Box.createHorizontalGlue());
			result.add(createFreePiece(pieces));
		});
		result.remove(0);
		return result;
	}

	private void initialize() {
		board = new BoardComponent(initializeBoard());
		setLayout(new BorderLayout(2, 2));
		Iterator<Piece> pieces = Piece.ALL.iterator();
		add(board, BorderLayout.CENTER);
		add(createLeftPanel(pieces), BorderLayout.WEST);
		add(createTopPanel(pieces), BorderLayout.NORTH);
		add(createRightPanel(pieces), BorderLayout.EAST);
		add(createBottomPanel(pieces), BorderLayout.SOUTH);
	}

	private Board initializeBoard() {
		Board newBoard = new Board();
		List<String> boardText=Arrays.asList("-----*","N-*J*-", "T--*--", "-*--*D", "*-Z*A-","---*-*");
		newBoard.parse(boardText.stream());
		return newBoard;
	}
}
