package net.blabux.mentagy.gui;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;

import javax.swing.JPanel;

import net.blabux.mentagy.domain.Cell;

public class CellComponent extends JPanel {
	private static final long serialVersionUID = -1992880940315958022L;
	final Cell cell;
	
	public CellComponent(Cell cell) {
		this.cell = cell;
		initialize();
	}

	private void initialize() {
		
	}
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		Rectangle bounds = g.getClipBounds();
		g.setColor(Color.RED);
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
}
