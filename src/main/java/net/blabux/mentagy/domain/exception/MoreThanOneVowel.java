package net.blabux.mentagy.domain.exception;

import net.blabux.mentagy.domain.Cell;

import java.util.List;
import java.util.stream.Collectors;

public class MoreThanOneVowel extends RuleViolation {
    private static final long serialVersionUID = 4024604172641505606L;
    final List<List<Cell>> violations;

    public MoreThanOneVowel(List<List<Cell>> lines) {
        violations = lines;
    }

    public String getMessage() {
        String violators = String.join(",", violations.stream().map((row) -> {
            return String.join("-", row.stream().map(Cell::value).collect(Collectors.toList()));
        }).collect(Collectors.toList()));

        return "More than One Vowel: " + violators;
    }
}
