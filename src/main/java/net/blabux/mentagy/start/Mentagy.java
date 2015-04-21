package net.blabux.mentagy.start;

import net.blabux.mentagy.gui.BoardPanel;

public class Mentagy {
	private BoardPanel panel;
	
	public static void main(String[] args) {
		Mentagy instance = new Mentagy();
		instance.start();
	}

	private void start() {
		panel = new BoardPanel();
		panel.show();
	}

}
