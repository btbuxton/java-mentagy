package net.blabux.mentagy.gui;

import net.blabux.mentagy.domain.Cell;
import net.blabux.mentagy.domain.Piece;
import net.blabux.mentagy.domain.exception.PieceParseException;
import net.blabux.mentagy.domain.exception.RuleViolation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetAdapter;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.event.*;
import java.awt.geom.Rectangle2D;
import java.io.IOException;

public class CellComponent extends JComponent {
    private static final Logger LOG = LoggerFactory.getLogger(CellComponent.class);
    private static final long serialVersionUID = -1992880940315958022L;
    final Cell cell;

    public CellComponent(Cell cell) {
        this.cell = cell;
        initialize();
    }

    @Override
    public void addNotify() {
        super.addNotify();
        Font biggerFont = getFont().deriveFont(48.0f).deriveFont(Font.BOLD);
        setFont(biggerFont);
    }

    @Override
    public boolean isFocusable() {
        return true;
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (cell.isLocked()) {
            g.setColor(Color.RED);
        } else {
            if (isFocusOwner()) {
                g.setColor(Color.BLUE);
            } else {
                g.setColor(Color.BLACK);
            }
        }
        Rectangle bounds = g.getClipBounds();
        g.drawOval(0, 0, bounds.width, bounds.height);
        g.setColor(Color.BLACK);
        if (cell.isAlphabetical()) {
            String value = cell.value();
            centerText(g, value);
        } else if (cell.isPeg()) {
            g.fillOval(10, 10, bounds.width - 20, bounds.height - 20);
        }
    }

    private void centerText(Graphics g, String text) {
        FontMetrics fm = g.getFontMetrics();
        Rectangle2D r = fm.getStringBounds(text, g);
        int x = (this.getWidth() - (int) r.getWidth()) / 2;
        int y = (this.getHeight() - (int) r.getHeight()) / 2 + fm.getAscent();
        g.drawString(text, x, y);
    }

    private void initialize() {
        setEnabled(true);
        setFocusable(true);
        setBorder(new LineBorder(Color.BLACK, 1));
        addMouseListener(new MouseListener() {

            @Override
            public void mouseClicked(MouseEvent e) {
                requestFocusInWindow();
            }

            @Override
            public void mouseEntered(MouseEvent e) {

            }

            @Override
            public void mouseExited(MouseEvent e) {

            }

            @Override
            public void mousePressed(MouseEvent e) {

            }

            @Override
            public void mouseReleased(MouseEvent e) {

            }
        });
        addFocusListener(new FocusListener() {

            @Override
            public void focusGained(FocusEvent e) {
                repaint();
            }

            @Override
            public void focusLost(FocusEvent e) {
                repaint();
            }

        });
        addKeyListener(new KeyListener() {

            @Override
            public void keyPressed(KeyEvent e) {
            }

            @Override
            public void keyReleased(KeyEvent e) {
            }

            @Override
            public void keyTyped(KeyEvent e) {
                Piece piece = null;
                char key = Character.toLowerCase(e.getKeyChar());
                if (cell.isLocked()) {
                    return;
                }
                if (' ' == key) {
                    piece = Piece.BLANK;
                } else {
                    try {
                        piece = Piece.parse(key);
                    } catch (PieceParseException ex) {
                        // IGNORE
                    }
                }
                if (null == piece) {
                    return;
                }
                if (!piece.isBlank() && cell.isUsedOnBoard(piece)) {
                    return;
                }
                Piece previous = cell.get();
                try {
                    cell.set(piece);
                    cell.checkRules();
                } catch (RuleViolation ex) {
                    LOG.warn("Rule Failed {}", ex.getMessage());
                    cell.set(previous);
                } catch (IllegalStateException ex) {
                    LOG.warn(ex.getMessage());
                }
                repaint();
            }

        });
        initializeDragTarget();
    }

    private void initializeDragTarget() {
        new DropTarget(this, new DropTargetAdapter() {
            @Override
            public void drop(DropTargetDropEvent dtde) {
                if (!cell.isBlank()) {
                    dtde.rejectDrop();
                    return;
                }
                dtde.acceptDrop(DnDConstants.ACTION_MOVE);
                boolean success;
                try {
                    Piece piece = (Piece) dtde.getTransferable().getTransferData(PieceTransferable.DATA_FLAVOR);
                    cell.set(piece);
                    cell.checkRules();
                    success = true;
                    repaint();
                } catch (UnsupportedFlavorException e) {
                    success = false;
                } catch (IOException e) {
                    success = false;
                } catch (RuleViolation e) {
                    success = false;
                    LOG.warn("Rule Failed {}", e.getMessage());
                }
                if (success) {
                    dtde.dropComplete(true);
                } else {
                    cell.set(Piece.BLANK);
                    dtde.dropComplete(false);
                }
            }
        });

    }

}
