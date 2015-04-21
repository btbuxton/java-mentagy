package net.blabux.mentagy.domain;

public class PieceParseException extends RuntimeException {
	private static final long serialVersionUID = 3629700079003123049L;
	
	final char offendingChar;
	
	public PieceParseException(char value) {
		offendingChar = value;
	}
	
	public char getOffendingChar() {
		return offendingChar;
	}

}
