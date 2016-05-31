package net.blabux.mentagy.loader;

import net.blabux.mentagy.domain.BoardDefinition;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;

import static net.blabux.mentagy.domain.BoardDefinition.PieceDefinition;

/**
 * Created by btbuxton on 5/30/16.
 */
public class SimpleBoardDefinitionLoader implements BoardDefinitionLoader {
    private static final int ROW_SIZE = 6;
    private static final int COLUMN_SIZE = 6;

    /*
     * Given a Reader, return the rows of the board in String form:
     * (-) - for empty space
     * (*) - for peg
     * (a-z) - for the various pieces
     * \n - ends current row starts another
     */
    @Override
    public BoardDefinition load(Reader reader) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(reader);
        BoardDefinition result = new BoardDefinition();
        for (int row = 0; row < ROW_SIZE; row++) {
            char[] buff = new char[COLUMN_SIZE];
            bufferedReader.read(buff);
            for (int column = 0; column < COLUMN_SIZE; column++) {
                BoardDefinition.PieceDefinition piece = null;
                char value = buff[column];
                if (Character.isAlphabetic(value)) {
                    piece = PieceDefinition.forAlpha(value);
                } else if ('*' == value) {
                    piece = PieceDefinition.PEG;
                } else {
                    piece = PieceDefinition.BLANK;
                }
                result.put(column, row, piece);
            }
            bufferedReader.readLine();
        }
        return result;
    }

}
