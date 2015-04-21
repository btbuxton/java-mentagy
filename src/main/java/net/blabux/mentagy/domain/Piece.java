package net.blabux.mentagy.domain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import net.blabux.mentagy.domain.exception.PieceParseException;

public class Piece implements Comparable<Piece> {
	private static final char PEG_CHAR = '*';
	private static final char BLANK_CHAR = '-';
	public final static Piece BLANK = new Piece(BLANK_CHAR);
	public final static Piece PEG = new Piece(PEG_CHAR);
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

	public static Piece parse(char value) {
		if (BLANK_CHAR == value) {
			return BLANK;
		}
		if (PEG_CHAR == value) {
			return PEG;
		}
		value = Character.toLowerCase(value);
		if ('a' <= value && 'z' >= value) {
			return ALL.get(value - 'a');
		}
		throw new PieceParseException(value);
	}

	private Piece(char value) {
		this.value = value;
	}

	public boolean isBlank() {
		return this == BLANK;
	}

	public boolean isPeg() {
		return this == PEG;
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
		if (isPeg()) {
			return -1;
		}
		if (another.isPeg()) {
			return 1;
		}
		if (value < another.value)
			return -1;
		else
			return 1;
	}

	@Override
	public boolean equals(Object obj) {
		try {
			return equals((Piece) obj);
		} catch (ClassCastException ex) {
			return false;
		}
	}

	public boolean equals(Piece another) {
		return value == another.value;
	}

	public boolean isAlphabetical() {
		return !(isBlank() || isPeg());
	}

	public Piece next() {
		if (value == 'z') {
			return null;
		}
		return ALL.get(value - 'a' + 1);
	}
}
