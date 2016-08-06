package net.blabux.mentagy.gui.undo;

import java.beans.PropertyChangeEvent;

import net.blabux.mentagy.domain.Cell;
import net.blabux.mentagy.domain.Piece;

public class SetPieceAction implements Undoable {
	final Cell cell;
	final Piece original;
	final Piece change;

	public SetPieceAction(PropertyChangeEvent event) {
		this((Cell) event.getSource(), (Piece) event.getOldValue(), (Piece) event.getNewValue());
	}

	public SetPieceAction(Cell cell, Piece original, Piece change) {
		this.cell = cell;
		this.original = original;
		this.change = change;
	}

	@Override
	public void doIt() {
		cell.forceSet(change);
	}

	@Override
	public void undoIt() {
		cell.forceSet(original);
	}
}