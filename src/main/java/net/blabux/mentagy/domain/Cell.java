package net.blabux.mentagy.domain;

import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Stream;

import net.blabux.mentagy.domain.exception.RuleViolation;

public class Cell implements Comparable<Cell> {
	final Board board;
	final int x;
	final int y;
	Piece piece;
	boolean locked;

	public Cell(Board board, int x, int y) {
		this.board = board;
		this.x = x;
		this.y = y;
		this.locked = false;
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

	public Piece get() {
		return piece;
	}

	public void set(Piece piece) {
		if (this.piece.equals(piece)) {
			return;
		}
		if (piece.isAlphabetical() && !this.piece.isBlank()) {
			throw new IllegalStateException();
		}
		if (this.piece.isPeg()) {
			throw new IllegalStateException();
		}
		if (piece.isBlank() && !this.piece.isAlphabetical()) {
			throw new IllegalStateException();
		}
		if (locked) {
			throw new IllegalStateException();
		}
		this.piece = piece;
	}

	public Box box() {
		return board.box(x / 2, y / 2);
	}

	public Stream<Cell> neighbors() {
		return board.neighbors(x, y);
	}

	@Override
	public int compareTo(Cell another) {
		return piece.compareTo(another.piece);
	}

	public Cell next() {
		return series(Piece::next);
	}

	public Cell previous() {
		return series(Piece::previous);
	}

	private Cell series(Function<Piece, Piece> nextFunc) {
		if (!piece.isAlphabetical()) {
			return null;
		}
		Piece next = nextFunc.apply(piece);
		if (null == next) {
			return null;
		}
		Optional<Cell> optional = board.neighbors(x, y)
				.filter((cell) -> cell.piece.equals(next)).findAny();
		if (optional.isPresent()) {
			return optional.get();
		}
		return null;
	}

	public String value() {
		return piece.value();
	}

	@Override
	public String toString() {
		return new StringBuilder().append(x).append('@').append(y).append('=')
				.append(value()).toString();
	}

	public boolean isPeg() {
		return piece.isPeg();
	}

	public void lock() {
		locked = true;
	}

	public void checkRules() throws RuleViolation {
		board.checkRules();
	}

	@Override
	public int hashCode() {
		return piece.hashCode();
	}

}
