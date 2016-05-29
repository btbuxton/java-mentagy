package net.blabux.mentagy.start;

import javax.swing.JFrame;

import net.blabux.mentagy.gui.GameComponent;

public class Mentagy {
	public static void main(final String[] args) {
		Mentagy instance = new Mentagy();
		instance.start();
	}

	private void start() {
		JFrame frame = new JFrame("Mentagy");
		frame.add(new GameComponent());
		frame.setSize(480, 480);
		frame.setVisible(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

	}

}
