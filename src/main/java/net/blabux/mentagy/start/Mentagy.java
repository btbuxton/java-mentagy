package net.blabux.mentagy.start;

import java.awt.BorderLayout;
import java.awt.event.KeyEvent;
import java.awt.event.WindowEvent;
import java.beans.PropertyChangeListener;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.blabux.mentagy.domain.Board;
import net.blabux.mentagy.domain.BoardDefinition;
import net.blabux.mentagy.gui.GameComponent;
import net.blabux.mentagy.gui.undo.SetPieceAction;
import net.blabux.mentagy.gui.undo.UndoableHistory;
import net.blabux.mentagy.loader.BoardDefinitionLoader;
import net.blabux.mentagy.loader.SimpleBoardDefinitionLoader;

public class Mentagy {
	private static final Logger LOG = LoggerFactory.getLogger(Mentagy.class);
	private BoardDefinition currentDef;
	private JFrame frame;
	private JLabel statusPanel;
	private UndoableHistory history;

	public static void main(final String[] args) {
		Mentagy instance = new Mentagy();
		instance.start();
	}

	private Mentagy() {
		history = new UndoableHistory();
	}

	private void start() {
		frame = new JFrame("Mentagy");
		frame.setLayout(new BorderLayout());
		final Board board = initialBoard();
		GameComponent gameComp = new GameComponent(board);
		frame.add(gameComp, BorderLayout.CENTER);
		JLabel label = new JLabel("Welcome to Mentagy!");
		label.setBorder(BorderFactory.createLoweredBevelBorder());
		frame.add(label, BorderLayout.PAGE_END);
		statusPanel = label;
		gameComp.addPropertyChangeListener(GameComponent.RULE_FAILED_PROPERTY, (event) -> {
			statusPanel.setText(String.valueOf(event.getNewValue()));
		});
		frame.setSize(480, 500);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		createMenuBar(board);
		frame.setVisible(true);

	}

	private Board initialBoard() {
		currentDef = loadDef("Initial", "1");
		Board newBoard = new Board();
		currentDef.apply(newBoard);
		newBoard.addPieceMovedListener(createPieceChangeListener());
		return newBoard;
	}

	private PropertyChangeListener createPieceChangeListener() {
		final PropertyChangeListener listener = (event) -> {
			if (!history.isInAction()) {
				LOG.info("Adding: "+event);
				history.addUndoableAction(new SetPieceAction(event));
			}
		};
		return listener;
	}

	private void createMenuBar(Board board) {
		JMenuBar menuBar = new JMenuBar();
		JMenu gameMenu = createGameMenu(board);
		menuBar.add(gameMenu);
		JMenu puzzleMenu = createPuzzleMenu(board);
		menuBar.add(puzzleMenu);
		menuBar.add(Box.createHorizontalGlue());
		JMenuItem helpMenu = createHelpMenu();
		menuBar.add(helpMenu);
		frame.setJMenuBar(menuBar);
	}

	private JMenu createHelpMenu() {
		JMenu menu = new JMenu("Help");
		menu.setMnemonic(KeyEvent.VK_H);
		JMenuItem about = new JMenuItem("About");
		about.setMnemonic(KeyEvent.VK_A);
		menu.add(about);
		about.addActionListener((event) -> {
			JOptionPane.showMessageDialog(frame, createAboutMessage(), "About", JOptionPane.INFORMATION_MESSAGE);
		});
		JMenuItem instructions = new JMenuItem("Instructions");
		menu.add(instructions);
		instructions.setMnemonic(KeyEvent.VK_I);
		instructions.addActionListener((event) -> {
			JOptionPane.showMessageDialog(frame, createInstructionsDialog(), "Instructions",
					JOptionPane.INFORMATION_MESSAGE);
		});
		return menu;
	}

	private String createInstructionsDialog() {
		return "Drag outside piece to board or click an empty space and type the letter.\nGo to http://www.mentagy.com for rules";
	}

	private String createAboutMessage() {
		return "Made with love by Blaine Buxton\nhttps://github.com/btbuxton/java-mentagy";
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

		JMenuItem undo = new JMenuItem("Undo");
		menu.add(undo);
		undo.setMnemonic(KeyEvent.VK_U);
		undo.setToolTipText("Undo last move");
		undo.addActionListener((event) -> {
			history.undo();
			frame.repaint();
		});
		undo.addHierarchyListener((event) -> {
			undo.setEnabled(history.canUndo());
		});

		JMenuItem redo = new JMenuItem("Redo");
		menu.add(redo);
		redo.setMnemonic(KeyEvent.VK_D);
		redo.setToolTipText("Redo last undo");
		redo.addActionListener((event) -> {
			history.redo();
			frame.repaint();
		});
		redo.addHierarchyListener((event) -> {
			redo.setEnabled(history.canRedo());
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
		String[] levels = new String[] { "Initial", "Beginner" };
		for (String each : levels) {
			JMenu levelMenu = new JMenu(each);
			menu.add(levelMenu);
			int index = 1;
			while (hasDef(each, String.valueOf(index))) {
				final String indexString = String.valueOf(index);
				JMenuItem item = new JMenuItem(indexString);
				levelMenu.add(item);
				item.addActionListener((event) -> {
					BoardDefinition def = loadDef(each, indexString);
					if (null != def) {
						currentDef = def;
					}
					refresh(board);
				});
				index++;
			}
		}
	}

	private boolean hasDef(String level, String number) {
		URL url = getDefResource(level, number);
		return null != url;
	}

	private BoardDefinition loadDef(String level, String number) {
		BoardDefinitionLoader loader = new SimpleBoardDefinitionLoader();
		BoardDefinition def = null;
		try {
			InputStream stream = getDefResource(level, number).openStream();
			try {
				InputStreamReader reader = new InputStreamReader(stream);
				def = loader.load(reader);
			} finally {
				stream.close();
			}
		} catch (IOException ex) {
			LOG.warn("Loading failed", ex);
			statusPanel.setText("Failed loading: " + level + ":" + number);
		}
		return def;
	}

	private URL getDefResource(String level, String number) {
		return getClass().getResource("/puzzles/" + level + "/" + number + ".txt");
	}

	private void refresh(Board board) {
		if (null != currentDef) {
			currentDef.apply(board);
		}
		history.clear();
		frame.repaint();
	}
}
