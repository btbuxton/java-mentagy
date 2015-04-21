package net.blabux.mentagy.domain;

public class NotConsecutive extends RuleViolation {
	final Cell previous;
	final Cell next;
	
	public NotConsecutive(Cell current, Cell next) {
		this.previous = current;
		this.next = next;
	}

	private static final long serialVersionUID = -9111413557808763042L;

}
