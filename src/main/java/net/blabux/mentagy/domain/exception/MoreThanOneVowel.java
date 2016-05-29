package net.blabux.mentagy.domain.exception;

import net.blabux.mentagy.domain.Cell;

import java.util.List;

public class MoreThanOneVowel extends RuleViolation {
    private static final long serialVersionUID = 4024604172641505606L;

    public MoreThanOneVowel(List<List<Cell>> lines) {

    }

}
