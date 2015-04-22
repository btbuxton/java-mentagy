package net.blabux.mentagy.start;

import net.blabux.mentagy.gui.BoardComponent;

public class Mentagy {
	private BoardComponent panel;
	
	public static void main(String[] args) {
		Mentagy instance = new Mentagy();
		instance.start();
	}

	private void start() {
		panel = new BoardComponent();
		panel.show();
	}

}
