package net.blabux.mentagy.domain;

import net.blabux.mentagy.domain.exception.*;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.StringReader;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class Board {
    private static final String PIECE_MOVED_PROPERTY = "pieceMoved";
    private static final int MAX = 6;
    final Cell[][] cells;
    final Box[][] boxes;
    final PropertyChangeSupport propertyChangeSupport;

    public Board() {
        propertyChangeSupport = new PropertyChangeSupport(this);
        cells = new Cell[MAX][MAX];
        initializeCells();
        boxes = new Box[3][3];
        initializeBoxes();
    }

    public Box box(int x, int y) {
        assert x >= 0 && x < MAX / 2;
        assert y >= 0 && y < MAX / 2;
        return boxes[x][y];
    }

    public Cell cell(int x, int y) {
        assert x >= 0 && x < MAX;
        assert y >= 0 && y < MAX;
        return cells[x][y];
    }

    public boolean isUsed(Piece piece) {
        return allCells().anyMatch((each) -> {
            return piece.equals(each.get());
        });
    }

    public Stream<Cell> neighbors(int locX, int locY) {
        // temp is needed to keep compiler happy
        Stream<Stream<Cell>> temp = IntStream.range(Math.max(0, locX - 1), Math.min(MAX, locX + 2))
                .mapToObj((x) -> IntStream.range(Math.max(0, locY - 1), Math.min(MAX, locY + 2))
                        .filter((y) -> !(x == locX && y == locY)).mapToObj((y) -> cells[x][y]));
        return temp.reduce(Stream.empty(), Stream::concat);
    }

    public Stream<String> output() {
        return rowStream().map((row) -> {
            StringBuilder builder = new StringBuilder();
            row.forEach((cell) -> {
                builder.append(cell.value());
            });
            return builder.toString();
        });
    }

    public void parse(Stream<String> rows) {
        Iterator<String> iterator = rows.iterator();
        IntStream.range(0, MAX).forEachOrdered((y) -> {
            StringReader reader = new StringReader(iterator.next());
            IntStream.range(0, MAX).forEachOrdered((x) -> {
                try {
                    Piece piece = Piece.parse((char) reader.read());
                    if (piece.isPeg() || piece.isAlphabetical()) {
                        cell(x, y).lock();
                    } else {
                        cell(x, y).unlock();
                    }
                    cell(x, y).forceSet(piece);
                } catch (Exception ex) {
                    throw new BoardParseException(ex);
                }
            });
        });
    }

    public void put(int x, int y, Piece piece) throws RuleViolation {
        cell(x, y).set(piece);
        checkRules();
    }

    public void addPieceMovedListener(PropertyChangeListener listener) {
        propertyChangeSupport.addPropertyChangeListener(PIECE_MOVED_PROPERTY, listener);
    }

    public void removePieceMovedListener(PropertyChangeListener listener) {
        propertyChangeSupport.removePropertyChangeListener(PIECE_MOVED_PROPERTY, listener);
    }

    private void allBoxesAreFilledInOrder() throws RuleViolation {
        Cell current = findMinimumStart();
        Box currentBox = current.box();
        while (null != current) {
            Box nextBox = current.box();
            if (!currentBox.equals(nextBox)) {
                if (!currentBox.isFilled()) {
                    throw new BoxNotFilled(currentBox);
                }
                currentBox = nextBox;
            }
            current = current.next();
        }
    }

    private Stream<Cell> allCells() {
        return columnStream().reduce(Stream.empty(), Stream::concat);
    }

    private Stream<Stream<Cell>> columnStream() {
        return IntStream.range(0, MAX).mapToObj((x) -> Arrays.stream(cells[x]));
    }

    private void fill(int i, int j, BiConsumer<Integer, Integer> consumer) {
        for (int y = 0; y < i; y++) {
            for (int x = 0; x < j; x++) {
                consumer.accept(x, y);
            }
        }
    }

    private Cell findMinimumStart() {
        return allCells().filter(Cell::isAlphabetical).sorted().findFirst().get();
    }

    private void initializeBoxes() {
        fill(3, 3, (x, y) -> {
            boxes[x][y] = new Box(this, x, y);
        });
    }

    private void initializeCells() {
        fill(MAX, MAX, (x, y) -> {
            cells[x][y] = new Cell(this, x, y);
        });
    }

    private void onlyOneVowelInRowOneColumn() throws RuleViolation {
        Function<Stream<Stream<Cell>>, Stream<List<Cell>>> findVowels = (stream) -> {
            return stream.map((row) -> row.filter(Cell::isVowel).collect(Collectors.toList()))
                    .collect(Collectors.toList()).stream().filter((list) -> list.size() > 1);
        };
        Stream<List<Cell>> badRows = findVowels.apply(rowStream());
        Stream<List<Cell>> badColumns = findVowels.apply(columnStream());
        List<List<Cell>> violations = Stream.concat(badRows, badColumns).collect(Collectors.toList());
        if (violations.size() > 0) {
            throw new MoreThanOneVowel(violations);
        }
    }

    private void piecesAreInOrder() throws RuleViolation {
        Set<Piece> played = allCells().map(Cell::get).filter(Piece::isAlphabetical).collect(Collectors.toSet());
        Set<Cell> badCells = allCells().filter(Cell::isAlphabetical).filter((cell) -> {
            Piece next = cell.get().next();
            Piece prev = cell.get().previous();
            Predicate<Piece> check = (piece) -> {
                return cell.neighbors().anyMatch((toCheck) -> {
                    return toCheck.get().equals(piece);
                });
            };
            if (next != null && played.contains(next)) {
                return !check.test(next);
            }
            if (prev != null && played.contains(prev)) {
                return !check.test(prev);
            }
            return false;
        }).collect(Collectors.toSet());
        if (!badCells.isEmpty()) {
            throw new NotConsecutive(badCells);
        }
    }

    private Stream<Stream<Cell>> rowStream() {
        return IntStream.range(0, MAX).mapToObj((y) -> IntStream.range(0, MAX).mapToObj((x) -> cells[x][y]));
    }

    void checkRules() throws RuleViolation {
        onlyOneVowelInRowOneColumn();
        allBoxesAreFilledInOrder();
        piecesAreInOrder();
    }

    void pieceMoved(Piece oldPiece, Piece newPiece) {
        propertyChangeSupport.firePropertyChange(PIECE_MOVED_PROPERTY, oldPiece, newPiece);
    }

}
