package net.blabux.mentagy.gui;

import java.awt.BorderLayout;
import java.util.stream.IntStream;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

public class GameComponent extends JPanel {
	private static final long serialVersionUID = 7786205238285692620L;
	private BoardComponent board;

	public GameComponent() {
		initialize();
	}

	private JComponent createBottomPanel() {
		JPanel result = new JPanel();
		result.setBorder(new EmptyBorder(5,5,5,5));
		result.setLayout(new BoxLayout(result,BoxLayout.X_AXIS));

		IntStream.range(0, 10).forEach((index) -> {
			result.add(Box.createHorizontalGlue());
			result.add(createFreePiece());
		});
		result.add(Box.createHorizontalGlue());
		return result;
	}

	private JComponent createFreePiece() {
		return new PieceComponent();
	}

	private JComponent createLeftPanel() {
		JPanel result = new JPanel();
		result.setBorder(new EmptyBorder(5,5,5,5));
		result.setLayout(new BoxLayout(result,BoxLayout.Y_AXIS));

		IntStream.range(0, 10).forEach((index) -> {
			result.add(Box.createVerticalGlue());
			result.add(createFreePiece());
		});
		result.add(Box.createVerticalGlue());
		return result;
	}

	private JComponent createRightPanel() {
		JPanel result = new JPanel();
		result.setBorder(new EmptyBorder(5,5,5,5));
		result.setLayout(new BoxLayout(result,BoxLayout.Y_AXIS));

		IntStream.range(0, 10).forEach((index) -> {
			result.add(Box.createVerticalGlue());
			result.add(createFreePiece());
		});
		result.add(Box.createVerticalGlue());
		return result;
	}

	private JComponent createTopPanel() {
		JPanel result = new JPanel();
		result.setBorder(new EmptyBorder(5,5,5,5));
		result.setLayout(new BoxLayout(result,BoxLayout.X_AXIS));

		IntStream.range(0, 10).forEach((index) -> {
			result.add(Box.createHorizontalGlue());
			result.add(createFreePiece());
		});
		result.add(Box.createHorizontalGlue());
		return result;
	}

	private void initialize() {
		board = new BoardComponent();
		setLayout(new BorderLayout(2, 2));
		add(board, BorderLayout.CENTER);
		add(createLeftPanel(), BorderLayout.WEST);
		add(createRightPanel(), BorderLayout.EAST);
		add(createTopPanel(), BorderLayout.NORTH);
		add(createBottomPanel(), BorderLayout.SOUTH);
	}
}
