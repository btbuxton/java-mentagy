package net.blabux.mentagy.domain;

import java.util.stream.Stream;

public class Cell implements Comparable<Cell> {
	final Board board;
	final int x;
	final int y;
	Piece piece;

	public Cell(Board board, int x, int y) {
		this.board = board;
		this.x = x;
		this.y = y;
		blank();
	}

	public boolean isBlank() {
		return piece.isBlank();
	}

	public void blank() {
		this.piece = Piece.BLANK;
	}

	public boolean isVowel() {
		return piece.isVowel();
	}

	public boolean isAlphabetical() {
		return piece.isAlphabetical();
	}

	public void set(Piece piece) {
		assert this.piece.isBlank();
		this.piece = piece;
	}

	public Box box() {
		return board.box(x, y);
	}

	public Stream<Cell> neighbors() {
		return board.neighbors(x, y);
	}

	@Override
	public int compareTo(Cell another) {
		return piece.compareTo(another.piece);
	}

	public Cell next() {
		if (!piece.isAlphabetical()) {
			return null;
		}
		Piece next = piece.next();
		if (null == next) {
			return null;
		}
		return board.neighbors(x, y).filter((cell) -> cell.piece.equals(next))
				.findAny().get();
	}

	public String value() {
		return piece.value();
	}

	public String toString() {
		return new StringBuilder().append(x).append('@').append(y).append('=')
				.append(value()).toString();
	}

	public boolean isPeg() {
		return piece.isPeg();
	}

}
