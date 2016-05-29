package net.blabux.mentagy.gui;

import net.blabux.mentagy.domain.Board;

import javax.swing.*;
import javax.swing.border.BevelBorder;
import java.awt.*;
import java.util.stream.IntStream;

public class BoardComponent extends JPanel {
    private static final long serialVersionUID = 6041698819860165194L;
    JFrame frame;
    Board board;

    public BoardComponent(Board board) {
        initialize(board);
    }

    public Board getBoard() {
        return board;
    }

    private void setBoard(Board board) {
        Board orig = this.board;
        this.board = board;
        firePropertyChange("board", orig, board);
    }

    private void initialize(Board board) {
        setBoard(board);
        setBorder(new BevelBorder(BevelBorder.LOWERED));
        setLayout(new GridLayout(3, 3, 2, 2));
        IntStream.range(0, 3).forEach((y) -> {
            IntStream.range(0, 3).forEach((x) -> {
                add(new BoxComponent(board.box(x, y)));
            });
        });
    }

}
