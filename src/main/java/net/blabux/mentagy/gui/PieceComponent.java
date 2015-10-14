package net.blabux.mentagy.gui;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.datatransfer.Transferable;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DragGestureEvent;
import java.awt.dnd.DragGestureListener;
import java.awt.dnd.DragSource;
import java.awt.dnd.DragSourceAdapter;
import java.awt.dnd.DragSourceDropEvent;
import java.awt.dnd.DragSourceListener;
import java.awt.geom.Rectangle2D;

import javax.swing.JComponent;

import net.blabux.mentagy.domain.Piece;

public class PieceComponent extends JComponent {
	private static final int SIZE = 32;
	private static final long serialVersionUID = -5251681235742245097L;
	private Piece piece;

	public PieceComponent(Piece piece) {
		this.piece = piece;
		initialize();
	}

	@Override
	public void addNotify() {
		super.addNotify();
		int size = SIZE;
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

	private void initialize() {
		DragSource ds = new DragSource();
		ds.createDefaultDragGestureRecognizer(this,
				DnDConstants.ACTION_MOVE, new DragGestureListener() {
			@Override
			public void dragGestureRecognized(DragGestureEvent dge) {
				Image image = createImage(SIZE, SIZE);
				paint(image.getGraphics());
				Transferable transferable = new PieceTransferable(piece);
				DragSourceListener dsl = new DragSourceAdapter() {
					@Override
					public void dragDropEnd(DragSourceDropEvent dsde) {
						//TODO do nothing
					}
				};
				ds.startDrag(dge, Cursor.getPredefinedCursor(Cursor.MOVE_CURSOR), image, new Point(0,0), transferable, dsl);
			}
		});
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
		if (piece.isPeg()) {
			g.setColor(Color.BLACK);
		} else {
			g.setColor(Color.WHITE);
		}
		g.fillOval(bounds.x, bounds.y, bounds.width - 1, bounds.height - 1);
		g.setColor(Color.BLACK);
		g.drawOval(bounds.x, bounds.y, bounds.width - 1, bounds.height - 1);
		centerText(g, bounds, piece.value());
	}
}
