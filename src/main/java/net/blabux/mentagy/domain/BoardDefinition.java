package net.blabux.mentagy.domain;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.stream.Stream;

/**
 * Created by btbuxton on 5/30/16.
 */
public class BoardDefinition {
    public static final int BOARD_SIZE = 6;
    final PieceDefinition pieces[][];

    public BoardDefinition() {
        pieces = new PieceDefinition[BOARD_SIZE][BOARD_SIZE];
        for (int index = 0; index < BOARD_SIZE; index++) {
            Arrays.fill(pieces[index], PieceDefinition.BLANK);
        }
    }

    public void apply(Board board) {
        for (int row = 0; row < BOARD_SIZE; row++) {
            for (int column = 0; column < BOARD_SIZE; column++) {
                Cell cell = board.cell(column, row);
                PieceDefinition pieceDef = pieces[row][column];
                if (PieceDefinition.BLANK == pieceDef) {
                    cell.set(Piece.BLANK);
                    cell.unlock();
                } else if (PieceDefinition.PEG == pieceDef) {
                    cell.set(Piece.PEG);
                    cell.lock();
                } else {
                    cell.set(Piece.parse(pieceDef.getCharacter()));
                    cell.lock();
                }
            }
        }
    }

    /*
     * Only for tests
     */
    public Stream<String> toStringRows() {
        ArrayList<String> result = new ArrayList<String>();
        for (int row = 0; row < BOARD_SIZE; row++) {
            StringBuilder builder = new StringBuilder();
            for (int column = 0; column < BOARD_SIZE; column++) {
                PieceDefinition pieceDef = pieces[row][column];
                if (PieceDefinition.BLANK == pieceDef) {
                    builder.append('+');
                } else if (PieceDefinition.PEG == pieceDef) {
                    builder.append('*');
                } else {
                    builder.append(pieceDef.getCharacter());
                }
            }
            result.add(builder.toString());
        }
        return result.stream();
    }

    public void put(int column, int row, PieceDefinition pieceDef) {
        pieces[row][column] = pieceDef;
    }

    public static class PieceDefinition {
        public static final PieceDefinition PEG = new PieceDefinition('*');
        public static final PieceDefinition BLANK = new PieceDefinition(' ');
        private final char internal;

        public static PieceDefinition forAlpha(char alpha) {
            char candidate = Character.toLowerCase(alpha);
            if (!Character.isAlphabetic(candidate)) {
                throw new IllegalStateException(String.valueOf(candidate) + " is not alphabetic");
            }
            return new PieceDefinition(candidate);
        }

        private PieceDefinition(char internal) {
            this.internal = internal;
        }

        char getCharacter() {
            return internal;
        }
    }
}
