package net.blabux.mentagy.gui;

import net.blabux.mentagy.domain.Box;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;

public class BoxComponent extends JPanel {
    private static final long serialVersionUID = 255506494908535945L;
    final Box box;

    public BoxComponent(Box box) {
        this.box = box;
        initialize();
    }

    private void initialize() {
        setBorder(new LineBorder(Color.BLACK, 2));
        setLayout(new GridLayout(2, 2, 2, 2));
        box.cells().forEach((cell) -> {
            add(new CellComponent(cell));
        });
    }
}
