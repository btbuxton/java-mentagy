package net.blabux.mentagy.gui;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JPanel;
import javax.swing.border.LineBorder;

import net.blabux.mentagy.domain.Cell;
import net.blabux.mentagy.domain.Piece;
import net.blabux.mentagy.domain.exception.PieceParseException;
import net.blabux.mentagy.domain.exception.RuleViolation;

public class CellComponent extends JPanel {
	private static final long serialVersionUID = -1992880940315958022L;
	final Cell cell;
	
	public CellComponent(Cell cell) {
		this.cell = cell;
		initialize();
	}

	private void initialize() {
		setEnabled(true);
		setFocusable(true);
		setBorder(new LineBorder(Color.BLACK, 1));
		addMouseListener(new MouseListener() {
			
			@Override
			public void mouseReleased(MouseEvent e) {
				
			}
			
			@Override
			public void mousePressed(MouseEvent e) {
				
			}
			
			@Override
			public void mouseExited(MouseEvent e) {
				
			}
			
			@Override
			public void mouseEntered(MouseEvent e) {
				
			}
			
			@Override
			public void mouseClicked(MouseEvent e) {
				requestFocusInWindow();
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
			public void keyTyped(KeyEvent e) {
				Piece piece = null;
				char key = Character.toLowerCase(e.getKeyChar());
				if (' ' == key) {
					piece = Piece.BLANK;
				}
				try {
					piece = Piece.parse(key);
				} catch(PieceParseException ex) {
					//IGNORE
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
					ex.printStackTrace(); //TODO...more here
				}
				repaint();
			}

			@Override
			public void keyPressed(KeyEvent e) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void keyReleased(KeyEvent e) {
				// TODO Auto-generated method stub
				
			}
			
		});
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
			//g.setFont(Font)
			g.drawString(value, bounds.width / 2, bounds.height /2);
		} else if(cell.isPeg()) {
			g.fillOval(10, 10, bounds.width - 20 , bounds.height - 20);
		}
	}
	@Override
	public boolean isFocusable() {
		return true;
	}
	
}
