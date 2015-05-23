package net.blabux.mentagy.gui;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetAdapter;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.geom.Rectangle2D;
import java.io.IOException;

import javax.swing.JComponent;
import javax.swing.border.LineBorder;

import net.blabux.mentagy.domain.Cell;
import net.blabux.mentagy.domain.Piece;
import net.blabux.mentagy.domain.exception.PieceParseException;
import net.blabux.mentagy.domain.exception.RuleViolation;

public class CellComponent extends JComponent {
	private static final long serialVersionUID = -1992880940315958022L;
	final Cell cell;

	public CellComponent(Cell cell) {
		this.cell = cell;
		initialize();
	}

	@Override
	public void addNotify() {
		super.addNotify();
		Font biggerFont = getFont().deriveFont(48.0f).deriveFont(Font.BOLD);
		setFont(biggerFont);
	}

	@Override
	public boolean isFocusable() {
		return true;
	}

	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		if (isFocusOwner()) {
			g.setColor(Color.BLUE);
		} else {
			g.setColor(Color.RED);
		}
		Rectangle bounds = g.getClipBounds();
		g.drawOval(0, 0, bounds.width, bounds.height);
		g.setColor(Color.BLACK);
		if (cell.isAlphabetical()) {
			String value = cell.value();
			centerText(g, value);
		} else if (cell.isPeg()) {
			g.fillOval(10, 10, bounds.width - 20, bounds.height - 20);
		}
	}

	private void centerText(Graphics g, String text) {
		FontMetrics fm = g.getFontMetrics();
		Rectangle2D r = fm.getStringBounds(text, g);
		int x = (this.getWidth() - (int) r.getWidth()) / 2;
		int y = (this.getHeight() - (int) r.getHeight()) / 2 + fm.getAscent();
		g.drawString(text, x, y);
	}

	private void initialize() {
		setEnabled(true);
		setFocusable(true);
		setBorder(new LineBorder(Color.BLACK, 1));
		addMouseListener(new MouseListener() {

			@Override
			public void mouseClicked(MouseEvent e) {
				requestFocusInWindow();
			}

			@Override
			public void mouseEntered(MouseEvent e) {

			}

			@Override
			public void mouseExited(MouseEvent e) {

			}

			@Override
			public void mousePressed(MouseEvent e) {

			}

			@Override
			public void mouseReleased(MouseEvent e) {

			}
		});
		addFocusListener(new FocusListener() {

			@Override
			public void focusGained(FocusEvent e) {
				repaint();
			}

			@Override
			public void focusLost(FocusEvent e) {
				repaint();
			}

		});
		addKeyListener(new KeyListener() {

			@Override
			public void keyPressed(KeyEvent e) {
			}

			@Override
			public void keyReleased(KeyEvent e) {
			}

			@Override
			public void keyTyped(KeyEvent e) {
				Piece piece = null;
				char key = Character.toLowerCase(e.getKeyChar());
				if (' ' == key) {
					piece = Piece.BLANK;
				}
				try {
					piece = Piece.parse(key);
				} catch (PieceParseException ex) {
					// IGNORE
				}
				if (null == piece) {
					return;
				}
				Piece previous = cell.get();
				cell.set(piece);
				try {
					cell.checkRules();
				} catch (RuleViolation ex) {
					cell.set(previous);
					//ex.printStackTrace(); // TODO...more here
				}
				repaint();
			}

		});
		initializeDragTarget();
	}

	private void initializeDragTarget() {
		new DropTarget(this, new DropTargetAdapter() {
			@Override
			public void drop(DropTargetDropEvent dtde) {
				if (!cell.isBlank()) {
					dtde.rejectDrop();
					return;
				}
				dtde.acceptDrop(DnDConstants.ACTION_MOVE);

				try {
					Piece piece = (Piece) dtde.getTransferable().getTransferData(PieceTransferable.DATA_FLAVOR);
					cell.set(piece);
					try {
						cell.checkRules();
					} catch (RuleViolation e) {
						cell.set(Piece.BLANK);
					}
					repaint();
				} catch (UnsupportedFlavorException e) {
					dtde.dropComplete(false);
				} catch (IOException e) {
					dtde.dropComplete(false);
				}
				dtde.dropComplete(true);
			}
		});

	}

}
