package net.blabux.mentagy.start;

import net.blabux.mentagy.domain.Board;
import net.blabux.mentagy.gui.GameComponent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.awt.event.KeyEvent;
import java.awt.event.WindowEvent;
import java.util.Arrays;
import java.util.List;

public class Mentagy {
    private static final Logger LOG = LoggerFactory.getLogger(Mentagy.class);

    public static void main(final String[] args) {
        Mentagy instance = new Mentagy();
        instance.start();
    }

    private void start() {
        JFrame frame = new JFrame("Mentagy");
        final Board board = initialBoard();
        frame.add(new GameComponent(board));
        frame.setSize(480, 480);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        createMenuBar(frame, board);
        frame.setVisible(true);

    }

    private Board initialBoard() {
        Board newBoard = new Board();
        reload(newBoard);
        return newBoard;
    }

    private void reload(Board board) {
        List<String> boardText = Arrays.asList("-----*", "N-*J*-", "T--*--", "-*--*D", "*-Z*A-", "---*-*");
        board.parse(boardText.stream());
    }

    private void createMenuBar(JFrame frame, Board board) {
        JMenuBar menuBar = new JMenuBar();
        JMenu gameMenu = new JMenu("Game");
        menuBar.add(gameMenu);
        gameMenu.setMnemonic(KeyEvent.VK_G);
        JMenuItem restart = new JMenuItem("Restart");
        gameMenu.add(restart);
        restart.setMnemonic(KeyEvent.VK_R);
        restart.setToolTipText("Resets board");
        restart.addActionListener((event) -> {
            reload(board);
            frame.repaint();
        });
        JMenuItem exit = new JMenuItem("Exit");
        gameMenu.add(exit);
        exit.setMnemonic(KeyEvent.VK_E);
        exit.setToolTipText("Exit");
        exit.addActionListener((event) -> {
            frame.dispatchEvent(new WindowEvent(frame, WindowEvent.WINDOW_CLOSING));
        });
        frame.setJMenuBar(menuBar);
    }

}
