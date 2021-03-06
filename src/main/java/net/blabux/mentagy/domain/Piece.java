package net.blabux.mentagy.domain;

import net.blabux.mentagy.domain.exception.PieceParseException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Piece implements Comparable<Piece> {
    public final static List<Piece> ALL;
    private static final char PEG_CHAR = '*';
    public final static Piece PEG = new Piece(PEG_CHAR);
    private static final char BLANK_CHAR = '-';
    public final static Piece BLANK = new Piece(BLANK_CHAR);
    private final static List<Piece> ALL_ALPHA;

    static {
        List<Piece> alphas = new ArrayList<Piece>();
        for (char c = 'a'; c <= 'z'; c++) {
            alphas.add(new Piece(c));
        }
        ALL_ALPHA = Collections.unmodifiableList(alphas);
        List<Piece> all = new ArrayList<Piece>(alphas);
        for (int i = 0; i < 10; i++) {
            all.add(Piece.PEG);
        }
        ALL = Collections.unmodifiableList(all);
    }

    private final char value;

    private Piece(char value) {
        this.value = value;
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
            return ALL_ALPHA.get(value - 'a');
        }
        throw new PieceParseException(value);
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

    @Override
    public int hashCode() {
        return value;
    }

    public boolean isAlphabetical() {
        return !(isBlank() || isPeg());
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

    public Piece next() {
        if (value == 'z') {
            return null;
        }
        return ALL_ALPHA.get(value - 'a' + 1);
    }

    public Piece previous() {
        if ('a' == value) {
            return null;
        }
        return ALL_ALPHA.get(value - 'a' - 1);
    }

    public String value() {
        return new String(new char[]{value});
    }
    
    public String toString() {
    	return value();
    }
    
}
