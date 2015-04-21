package net.blabux.mentagy.domain.exception;

import java.util.List;

import net.blabux.mentagy.domain.Cell;

public class MoreThanOneVowel extends RuleViolation {
	public MoreThanOneVowel(List<List<Cell>> lines) {
		
	}

	private static final long serialVersionUID = 4024604172641505606L;

}
