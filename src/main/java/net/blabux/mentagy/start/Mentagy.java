package net.blabux.mentagy.start;

import net.blabux.mentagy.domain.Board;
import net.blabux.mentagy.domain.BoardDefinition;
import net.blabux.mentagy.gui.GameComponent;
import net.blabux.mentagy.loader.BoardDefinitionLoader;
import net.blabux.mentagy.loader.SimpleBoardDefinitionLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.awt.event.KeyEvent;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.List;

public class Mentagy {
    private static final Logger LOG = LoggerFactory.getLogger(Mentagy.class);
    private BoardDefinition currentDef;
    private JFrame frame;

    public static void main(final String[] args) {
        Mentagy instance = new Mentagy();
        instance.start();
    }

    private void start() {
        frame = new JFrame("Mentagy");
        final Board board = initialBoard();
        frame.add(new GameComponent(board));
        frame.setSize(480, 480);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        createMenuBar(board);
        frame.setVisible(true);

    }

    private Board initialBoard() {
        currentDef = loadDef("Initial", "1");
        Board newBoard = new Board();
        currentDef.apply(newBoard);
        return newBoard;
    }

    private void createMenuBar(Board board) {
        JMenuBar menuBar = new JMenuBar();
        JMenu gameMenu = createGameMenu(board);
        menuBar.add(gameMenu);
        JMenu puzzleMenu = createPuzzleMenu(board);
        menuBar.add(puzzleMenu);
        frame.setJMenuBar(menuBar);
    }

    private JMenu createGameMenu(Board board) {
        JMenu menu = new JMenu("Game");
        menu.setMnemonic(KeyEvent.VK_G);
        JMenuItem restart = new JMenuItem("Reset");
        menu.add(restart);
        restart.setMnemonic(KeyEvent.VK_R);
        restart.setToolTipText("Resets board");
        restart.addActionListener((event) -> {
            refresh(board);
            frame.repaint();
        });

        JMenuItem exit = new JMenuItem("Exit");
        menu.add(exit);
        exit.setMnemonic(KeyEvent.VK_E);
        exit.setToolTipText("Exit");
        exit.addActionListener((event) -> {
            frame.dispatchEvent(new WindowEvent(frame, WindowEvent.WINDOW_CLOSING));
        });
        return menu;
    }

    private JMenu createPuzzleMenu(Board board) {
        JMenu menu = new JMenu("Puzzles");
        menu.setMnemonic(KeyEvent.VK_P);
        createPuzzleItems(menu, board);
        return menu;
    }

    private void createPuzzleItems(JMenu menu, Board board) {
        String[] levels = new String[]{"Initial", "Beginner"};
        for (String each : levels) {
            JMenu levelMenu = new JMenu(each);
            menu.add(levelMenu);
            //do something else here
            JMenuItem item = new JMenuItem("1");
            levelMenu.add(item);
            item.addActionListener((event) -> {
                BoardDefinition def = loadDef(each, "1");
                if (null != def) {
                    currentDef = def;
                }
                refresh(board);
            });
        }
    }

    private BoardDefinition loadDef(String level, String number) {
        BoardDefinitionLoader loader = new SimpleBoardDefinitionLoader();
        BoardDefinition def = null;
        try {
            InputStream stream = getClass().getResourceAsStream("/puzzles/" + level + "/" + number + ".txt");
            try {
                InputStreamReader reader = new InputStreamReader(stream);
                def = loader.load(reader);
            } finally {
                stream.close();
            }
        } catch (IOException ex) {
            LOG.warn("Loading failed", ex);
        }
        return def;
    }

    private void refresh(Board board) {
        if (null != currentDef) {
            currentDef.apply(board);
        }
        frame.repaint();
    }
}
