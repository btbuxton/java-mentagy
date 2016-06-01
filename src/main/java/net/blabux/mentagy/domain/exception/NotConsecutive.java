package net.blabux.mentagy.domain.exception;

import net.blabux.mentagy.domain.Cell;

import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

public class NotConsecutive extends RuleViolation {
    private static final long serialVersionUID = -9111413557808763042L;

    final Collection<Cell> violations;

    public NotConsecutive(Set<Cell> badCells) {
        violations = badCells;
    }

    public String getMessage() {
        String violators = String.join(",", violations.stream().map(Cell::value).collect(Collectors.toList()));
        return "Not consecutive: " + violators;
    }

}
