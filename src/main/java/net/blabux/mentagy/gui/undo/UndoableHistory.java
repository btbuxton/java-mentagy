package net.blabux.mentagy.gui.undo;

import java.util.ArrayList;
import java.util.List;

public class UndoableHistory {
	private final List<Undoable> undoables;
	private volatile int index;
	private volatile boolean isInAction;

	public UndoableHistory() {
		undoables = new ArrayList<>();
		isInAction = false;
		clear();
	}

	public void addUndoableAction(Undoable latest) {
		undoables.add(++index, latest);
	}

	public void undo() {
		try {
			isInAction = true;
			undoables.get(index--).undoIt();
		} finally {
			isInAction = false;
		}
	}

	public void redo() {
		try {
			isInAction = true;
			undoables.get(++index).doIt();
		} finally {
			isInAction = false;
		}
	}

	public boolean canUndo() {
		return index >= 0;
	}

	public boolean canRedo() {
		return index < (undoables.size() - 1);
	}

	public void clear() {
		undoables.clear();
		index = -1;
	}

	public boolean isInAction() {
		return isInAction;
	}
}
