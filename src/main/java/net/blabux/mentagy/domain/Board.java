package net.blabux.mentagy.domain;

import java.io.StringReader;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import net.blabux.mentagy.domain.exception.BoardParseException;
import net.blabux.mentagy.domain.exception.BoxNotFilled;
import net.blabux.mentagy.domain.exception.MoreThanOneVowel;
import net.blabux.mentagy.domain.exception.NotConsecutive;
import net.blabux.mentagy.domain.exception.RuleViolation;

public class Board {
	private static final int MAX = 6;
	final Cell[][] cells;
	final Box[][] boxes;

	public Board() {
		cells = new Cell[MAX][MAX];
		initializeCells();
		boxes = new Box[3][3];
		initializeBoxes();
	}

	public Cell cell(int x, int y) {
		assert x >= 0 && x < MAX;
		assert y >= 0 && y < MAX;
		return cells[x][y];
	}

	public Box box(int x, int y) {
		assert x >= 0 && x < MAX / 2;
		assert y >= 0 && y < MAX / 2;
		return boxes[x][y];
	}

	public Stream<Cell> neighbors(int locX, int locY) {
		// temp is needed to keep compiler happy
		Stream<Stream<Cell>> temp = IntStream
				.range(Math.max(0, locX - 1), Math.min(MAX, locX + 2))
				.mapToObj(
						(x) -> IntStream
								.range(Math.max(0, locY - 1),
										Math.min(MAX, locY + 2))
								.filter((y) -> !(x == locX && y == locY))
								.mapToObj((y) -> cells[x][y]));
		return temp.reduce(Stream.empty(), Stream::concat);
	}

	public void put(int x, int y, Piece piece) throws RuleViolation {
		cell(x, y).set(piece);
		checkRules();
	}

	public void parse(Stream<String> rows) {
		Iterator<String> iterator = rows.iterator();
		IntStream.range(0, MAX).forEachOrdered((y) -> {
			StringReader reader = new StringReader(iterator.next());
			IntStream.range(0, MAX).forEachOrdered((x) -> {
				try {
					Piece piece = Piece.parse((char) reader.read());
					cell(x, y).set(piece);
					if (piece.isPeg() || piece.isAlphabetical()) {
						cell(x,y).lock();
					}
				} catch (Exception ex) {
					throw new BoardParseException(ex);
				}
			});
		});
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

	void checkRules() throws RuleViolation {
		onlyOneVowelInRowOneColumn();
		allBoxesAreFilledInOrder();
		piecesAreInOrder();
	}

	private void allBoxesAreFilledInOrder() throws RuleViolation {
		//TODO doesn't catch violations early enough
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

	private Cell findMinimumStart() {
		return allCells().filter(Cell::isAlphabetical).sorted().findFirst()
				.get();
	}

	private Stream<Cell> allCells() {
		return columnStream().reduce(Stream.empty(), Stream::concat);
	}

	private Stream<Stream<Cell>> columnStream() {
		return IntStream.range(0, MAX).mapToObj((x) -> Arrays.stream(cells[x]));
	}

	private Stream<Stream<Cell>> rowStream() {
		return IntStream.range(0, MAX).mapToObj(
				(y) -> IntStream.range(0, MAX).mapToObj((x) -> cells[x][y]));
	}

	private void onlyOneVowelInRowOneColumn() throws RuleViolation {
		Function<Stream<Stream<Cell>>, Stream<List<Cell>>> findVowels = (stream) -> {
			return stream
					.map((row) -> row.filter(Cell::isVowel).collect(
							Collectors.toList())).collect(Collectors.toList())
					.stream().filter((list) -> list.size() > 1);
		};
		Stream<List<Cell>> badRows = findVowels.apply(rowStream());
		Stream<List<Cell>> badColumns = findVowels.apply(columnStream());
		List<List<Cell>> violations = Stream.concat(badRows, badColumns)
				.collect(Collectors.toList());
		if (violations.size() > 0) {
			throw new MoreThanOneVowel(violations);
		}
	}

	private void piecesAreInOrder() throws RuleViolation {
		//TODO fix
		Cell current = findMinimumStart();
		while (null != current) {
			Cell next = current.next();
			if (null == next)
				break;
			if (current.neighbors().noneMatch((cell) -> cell.equals(next))) {
				throw new NotConsecutive(current, next);
			}
			current = next;
		}
	}

	private void initializeCells() {
		fill(MAX, MAX, (x, y) -> {
			cells[x][y] = new Cell(this, x, y);
		});
	}

	private void initializeBoxes() {
		fill(3, 3, (x, y) -> {
			boxes[x][y] = new Box(this, x, y);
		});
	}

	private void fill(int i, int j, BiConsumer<Integer, Integer> consumer) {
		for (int y = 0; y < i; y++) {
			for (int x = 0; x < j; x++) {
				consumer.accept(x, y);
			}
		}
	}

}
