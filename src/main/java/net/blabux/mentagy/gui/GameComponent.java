package net.blabux.mentagy.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.util.stream.IntStream;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.border.BevelBorder;
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
		result.add(Box.createHorizontalGlue());
		IntStream.range(0, 10).forEach((index) -> {
			result.add(createFreePiece());
		});
		result.add(Box.createHorizontalGlue());
		return result;
	}

	private JComponent createFreePiece() {
		JComponent result = new JComponent() {
			private static final long serialVersionUID = -66353523590534066L;

			@Override
			protected void paintComponent(Graphics g) {
				Rectangle bounds = g.getClipBounds();
				g.setColor(Color.DARK_GRAY);
				g.fillOval(bounds.x, bounds.y, bounds.width, bounds.height);
			}
		};
		result.setMinimumSize(new Dimension(32, 32));
		result.setMaximumSize(new Dimension(32, 32));
		result.setPreferredSize(new Dimension(32,32));
		return result;
	}

	private JComponent createLeftPanel() {
		JPanel result = new JPanel();
		result.setBorder(new EmptyBorder(5,5,5,5));
		result.setLayout(new BoxLayout(result,BoxLayout.Y_AXIS));
		result.add(Box.createVerticalGlue());
		IntStream.range(0, 10).forEach((index) -> {
			result.add(createFreePiece());
		});
		result.add(Box.createVerticalGlue());
		return result;
	}

	private JComponent createRightPanel() {
		JPanel result = new JPanel();
		result.setBorder(new EmptyBorder(5,5,5,5));
		result.setLayout(new BoxLayout(result,BoxLayout.Y_AXIS));
		result.add(Box.createVerticalGlue());
		IntStream.range(0, 10).forEach((index) -> {
			result.add(createFreePiece());
		});
		result.add(Box.createVerticalGlue());
		return result;
	}

	private JComponent createTopPanel() {
		JPanel result = new JPanel();
		result.setBorder(new EmptyBorder(5,5,5,5));
		result.setLayout(new BoxLayout(result,BoxLayout.X_AXIS));
		result.add(Box.createHorizontalGlue());
		IntStream.range(0, 10).forEach((index) -> {
			result.add(createFreePiece());
		});
		result.add(Box.createHorizontalGlue());
		return result;
	}

	private void initialize() {
		board = new BoardComponent();
		setLayout(new BorderLayout(2, 2));
		setBorder(new BevelBorder(BevelBorder.RAISED));
		add(board, BorderLayout.CENTER);
		add(createLeftPanel(), BorderLayout.WEST);
		add(createRightPanel(), BorderLayout.EAST);
		add(createTopPanel(), BorderLayout.NORTH);
		add(createBottomPanel(), BorderLayout.SOUTH);
	}
}
