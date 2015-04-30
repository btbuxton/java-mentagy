package net.blabux.mentagy.gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.geom.Rectangle2D;

import javax.swing.JComponent;

public class PieceComponent extends JComponent {
	@Override
	public void addNotify() {
		super.addNotify();
		int size = 32;
		setInitialSize(size);
		Font biggerFont = getFont().deriveFont((float) size / 2).deriveFont(
				Font.BOLD);
		setFont(biggerFont);
	}

	// TODO make this unduplicated with CellComponent
	private void centerText(Graphics g, Rectangle bounds, String text) {
		FontMetrics fm = g.getFontMetrics();
		Rectangle2D r = fm.getStringBounds(text, g);
		int x = bounds.x + (bounds.width - (int) r.getWidth()) / 2;
		int y = bounds.y + (bounds.height - (int) r.getHeight()) / 2
				+ fm.getAscent();
		g.setColor(Color.BLACK);
		g.drawString(text, x, y);
	}

	private void setInitialSize(int size) {
		setMinimumSize(new Dimension(size, size));
		setMaximumSize(new Dimension(size, size));
		setPreferredSize(new Dimension(size, size));
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		Rectangle bounds = g.getClipBounds();
		g.setColor(Color.WHITE);
		g.fillOval(bounds.x, bounds.y, bounds.width - 1, bounds.height - 1);
		g.setColor(Color.BLACK);
		g.drawOval(bounds.x, bounds.y, bounds.width - 1, bounds.height - 1);
		centerText(g, bounds, "a");
	}
}
