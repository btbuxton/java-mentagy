package net.blabux.mentagy.domain;

public class RuleViolation extends Exception {
	private static final long serialVersionUID = 1979476305504070239L;

	public RuleViolation() {
		super();
	}

	public RuleViolation(String message) {
		super(message);
	}

}