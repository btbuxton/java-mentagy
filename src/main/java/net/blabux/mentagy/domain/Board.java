package net.blabux.mentagy.domain;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.stream.Stream;

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
		assert x >= 0 && x <= MAX;
		assert y >= 0 && y <= MAX;
		return cells[x][y];
	}

	public Box box(int x, int y) {
		assert x >= 0 && x <= MAX;
		assert y >= 0 && y <= MAX;
		return boxes[x / 2][y / 2];
	}

	public Stream<Cell> neighbors(int locX, int locY) {
		List<Cell> result = new ArrayList<>();
		for (int x = Math.max(0, locX - 1); x <= Math.min(MAX, locX + 1); x++) {
			for (int y = Math.max(0, locY - 1); y <= Math.min(MAX, locY + 1); y++) {
				if (x == locX && y == locY) {
					continue;
				}
				result.add(cells[x][y]);
			}
		}
		return result.stream();
	}

	public void put(int x, int y, Piece piece) {
		cell(x, y).set(piece);
		checkRules();
	}

	private void checkRules() {
		onlyOneVowelInRowOneColumn();
		allBoxesAreFilledInOrder();
		piecesAreInOrder();
	}

	private void allBoxesAreFilledInOrder() {// the previous boxes must be
												// filled before moving out
		Cell current = findMinimumStart();
		Box currentBox = current.box();

	}

	private Cell findMinimumStart() {
		return allCells().filter(Cell::isAlphabetical).sorted().findFirst()
				.get();
	}

	private Stream<Cell> allCells() {
		List<Cell> result = new ArrayList<>();
		cellsDo((cell) -> result.add(cell));
		return result.stream();
	}

	private void cellsDo(Consumer<Cell> consumer) {
		for (int x = 0; x < MAX; x++) {
			for (int y = 0; y < MAX; y++) {
				consumer.accept(cells[x][y]);
			}
		}
	}

	private void onlyOneVowelInRowOneColumn() {
		Predicate<? super Stream<Cell>> hasOneOrLessVowels = (row) -> {
			return row.filter(Cell::isVowel).count() <= 1;
		};
		rowStream().filter(hasOneOrLessVowels);
		columnStream().filter(hasOneOrLessVowels);

	}

	private Stream<Stream<Cell>> columnStream() {
		List<Stream<Cell>> result = new ArrayList<>();
		for (int x = 0; x < MAX; x++) {
			result.add(Arrays.asList(cells[x]).stream());
		}
		return result.stream();
	}

	private Stream<Stream<Cell>> rowStream() {
		List<Stream<Cell>> result = new ArrayList<>();
		for (int y = 0; y < MAX; y++) {
			List<Cell> row = new ArrayList<>();
			for (int x = 0; x < MAX; x++) {
				row.add(cells[x][y]);
			}
			result.add(row.stream());
		}
		return result.stream();
	}

	private void piecesAreInOrder() {

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
		for (int x = 0; x < i; x++) {
			for (int y = 0; y < j; y++) {
				consumer.accept(x, y);
			}
		}
	}

}
