package net.blabux.mentagy.domain.exception;

import net.blabux.mentagy.domain.Cell;

import java.util.Collection;
import java.util.Set;

public class NotConsecutive extends RuleViolation {
    private static final long serialVersionUID = -9111413557808763042L;

    final Collection<Cell> violations;

    public NotConsecutive(Set<Cell> badCells) {
        violations = badCells;
    }

}
