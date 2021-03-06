package net.blabux.mentagy.gui;

import net.blabux.mentagy.domain.Piece;

import javax.swing.*;
import java.awt.*;
import java.awt.datatransfer.Transferable;
import java.awt.dnd.*;
import java.awt.geom.Rectangle2D;

public class PieceComponent extends JComponent {
    private static final int SIZE = 32;
    private static final long serialVersionUID = -5251681235742245097L;
    private final BoardComponent board;
    private final Piece original;
    private Piece piece;

    public PieceComponent(BoardComponent board, Piece piece) {
        this.board = board;
        this.original = piece;
        this.piece = piece;
        initialize();
    }

    @Override
    public void addNotify() {
        super.addNotify();
        int size = SIZE;
        setInitialSize(size);
        Font biggerFont = getFont().deriveFont((float) size / 2).deriveFont(Font.BOLD);
        setFont(biggerFont);
    }

    // TODO make this unduplicated with CellComponent
    private void centerText(Graphics g, Rectangle bounds, String text) {
        FontMetrics fm = g.getFontMetrics();
        Rectangle2D r = fm.getStringBounds(text, g);
        int x = bounds.x + (bounds.width - (int) r.getWidth()) / 2;
        int y = bounds.y + (bounds.height - (int) r.getHeight()) / 2 + fm.getAscent();
        g.setColor(Color.BLACK);
        g.drawString(text, x, y);
    }

    private void initialize() {
        DragSource ds = new DragSource();
        ds.createDefaultDragGestureRecognizer(this, DnDConstants.ACTION_MOVE, new DragGestureListener() {
            @Override
            public void dragGestureRecognized(DragGestureEvent dge) {
                if (isOnBoard()) {
                    return;
                }
                Image image = createImage(SIZE, SIZE);
                paint(image.getGraphics());
                Transferable transferable = new PieceTransferable(piece);
                DragSourceListener dsl = new DragSourceAdapter() {
                    @Override
                    public void dragDropEnd(DragSourceDropEvent dsde) {
                        repaint();
                    }
                };
                ds.startDrag(dge, Cursor.getPredefinedCursor(Cursor.MOVE_CURSOR), image, new Point(0, 0), transferable,
                        dsl);
            }
        });
        board.getBoard().addPieceMovedListener((event) -> {
            if (piece == event.getOldValue() || piece == event.getNewValue()) {
                repaint();
            }
        });
    }

    private boolean isOnBoard() {
        return board.getBoard().isUsed(original);
    }

    private void setInitialSize(int size) {
        setMinimumSize(new Dimension(size, size));
        setMaximumSize(new Dimension(size, size));
        setPreferredSize(new Dimension(size, size));
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Rectangle bounds = g.getClipBounds();
        if (piece.isPeg()) {
            g.setColor(Color.BLACK);
        } else {
            g.setColor(Color.WHITE);
        }
        g.fillOval(bounds.x, bounds.y, bounds.width - 1, bounds.height - 1);
        g.setColor(Color.BLACK);
        g.drawOval(bounds.x, bounds.y, bounds.width - 1, bounds.height - 1);
        if (!(piece.isBlank() || isOnBoard())) {
            centerText(g, bounds, piece.value());
        }
    }
}
