package net.blabux.mentagy.domain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Piece implements Comparable<Piece> {
	public final static Piece BLANK = new Piece(' ');
	public final static Piece FILLER = new Piece('*');
	public final static List<Piece> ALL;

	final char value;
	Piece next;

	static {
		List<Piece> temp = new ArrayList<Piece>();
		for (char c = 'a'; c <= 'z'; c++) {
			temp.add(new Piece(c));
		}
		ALL = Collections.unmodifiableList(temp);
	}

	private Piece(char value) {
		this.value = value;
	}

	public boolean isBlank() {
		return this == BLANK;
	}

	public boolean isFiller() {
		return this == FILLER;
	}

	public boolean isVowel() {
		return value == 'a' || value == 'e' || value == 'i' || value == 'o'
				|| value == 'u' || value == 'y';
	}

	public String value() {
		return new String(new char[] { value });
	}

	@Override
	public int compareTo(Piece another) {
		if (value == another.value)
			return 0;
		if (isBlank()) {
			return -1;
		}
		if (another.isBlank()) {
			return 1;
		}
		if (isFiller()) {
			return -1;
		}
		if (another.isFiller()) {
			return 1;
		}
		if (value < another.value)
			return -1;
		else
			return 1;
	}

	public boolean isAlphabetical() {
		return !(isBlank() || isFiller());
	}

	public Piece next() {
		if (value == 'z') {
			return null;
		}
		return ALL.get(value - 'a');
	}
}
