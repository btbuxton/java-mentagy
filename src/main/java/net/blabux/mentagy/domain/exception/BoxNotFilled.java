package net.blabux.mentagy.domain.exception;

import net.blabux.mentagy.domain.Box;

public class BoxNotFilled extends RuleViolation {
    private static final long serialVersionUID = -2947404131932127651L;

    final Box box;

    public BoxNotFilled(Box box) {
        this.box = box;
    }

    public Box getBox() {
        return box;
    }

}
