package net.blabux.mentagy.gui;

import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;

import net.blabux.mentagy.domain.Piece;

public class PieceTransferable implements Transferable {
	public static final DataFlavor DATA_FLAVOR = new DataFlavor(Piece.class,
			Piece.class.getSimpleName());
	private final Piece piece;

	public PieceTransferable(Piece piece) {
		this.piece = piece;
	}

	@Override
	public Object getTransferData(DataFlavor flavor)
			throws UnsupportedFlavorException, IOException {
		if (flavor.equals(DATA_FLAVOR)) {
			return piece;
		} else {
			throw new UnsupportedFlavorException(flavor);
		}
	}

	@Override
	public DataFlavor[] getTransferDataFlavors() {
		return new DataFlavor[] { DATA_FLAVOR };
	}

	@Override
	public boolean isDataFlavorSupported(DataFlavor flavor) {
		return false;
	}

}
