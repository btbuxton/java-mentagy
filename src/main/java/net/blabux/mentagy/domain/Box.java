package net.blabux.mentagy.domain;

import java.util.ArrayList;
import java.util.Collection;
import java.util.stream.Stream;

public class Box {
	final Board board;
	final Collection<Cell> cells;

	public Box(Board board, int x, int y) {
		this.board = board;
		cells = new ArrayList<Cell>(4);
		cells.add(board.cell(x * 2, y * 2));
		cells.add(board.cell(x * 2 + 1, y * 2));
		cells.add(board.cell(x * 2, y * 2 + 1));
		cells.add(board.cell(x * 2 + 1, y * 2 + 1));
	}

	public boolean isFilled() {
		return getCellsStream().noneMatch(Cell::isBlank);
	}
	
	public Stream<Cell> cells() {
		return cells.stream();
	}

	@Override
	public boolean equals(Object obj) {
		try {
			return equals((Box)obj);
		} catch(ClassCastException ex) {
			return false;
		}
	}
	
	public boolean equals(Box another) {
		return this == another;
	}

	private Stream<Cell> getCellsStream() {
		return cells.stream();
	}

}
