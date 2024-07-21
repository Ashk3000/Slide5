
import java.awt.*;
import javax.swing.*;

/**
 *
 * @author Ashk3000
 */

public class Board extends JComponent {

	Tile[][] board = new Tile[5][5];

	public Board() {
		for (int x = 0; x <= 4; x++) {
			for (int y = 0; y <= 4; y++) {
				board[x][y] = new Tile(x, y, this);
			}
		}
	}

	@Override
	public void paintComponent(Graphics g) {
		Graphics2D g2 = (Graphics2D) g;
		g2.rotate(Math.toRadians(135));
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

		for (int x = 0; x <= 4; x++) {
			for (int y = 0; y <= 4; y++) {
				board[x][y].draw(g2);
			}
		}
	}

	public void print() {
		for (int row = 0; row < 5 * 2 - 1; row++) {
			int rowWidth = -Math.abs(row - 4) + 4;

			for (int i = 0; i <= 5 - rowWidth; i++) {
				System.out.print("  ");
			}

			for (int i = 0; i <= rowWidth; i++) {
				int y = i + Math.max(0, row - 4);
				int x = row - y;

				switch (board[x][y].piece) {
					case NOTHING ->
						System.out.print("#");
					case BLACK ->
						System.out.print("X");
					case WHITE ->
						System.out.print("O");
					default ->
						System.err.println("board[" + x + "][" + y + "].piece is not NOTHING, BLACK, or WHITE");
				}
				System.out.print("   ");
			}
			System.out.println();
		}
	}

	public void printNum() {
		for (int row = 0; row < 5 * 2 - 1; row++) {
			int rowWidth = -Math.abs(row - 4) + 4;

			for (int i = 0; i <= 5 - rowWidth; i++) {
				System.out.print("  ");
			}

			for (int i = 0; i <= rowWidth; i++) {
				int y = i + Math.max(0, row - 4);
				int x = row - y;

				switch (board[x][y].piece) {
					case NOTHING ->
						System.out.print("#");
					case BLACK ->
						System.out.print("X");
					case WHITE ->
						System.out.print("O");
					default ->
						System.err.println("board[" + x + "][" + y + "].piece is not NOTHING, BLACK, or WHITE");
				}
				System.out.print("   ");
			}
			System.out.println();
		}
	}

	public boolean[] winCheck() {
		boolean xWin = false;
		boolean oWin = false;

		for (int x = 0; x <= 4; x++) {
			Tile.Piece firstTilePiece = board[x][0].piece;
			boolean connecting = true;
			for (int y = 0; y <= 4; y++) {
				if (board[x][y].piece != firstTilePiece) {
					connecting = false;
				}
			}
			if (connecting) {
				if (firstTilePiece == Tile.Piece.BLACK) {
					xWin = true;
				} else if (firstTilePiece == Tile.Piece.WHITE) {
					oWin = true;
				}
			}
		}

		for (int y = 0; y <= 4; y++) {
			Tile.Piece firstTilePiece = board[0][y].piece;

			boolean connecting = true;
			for (int x = 0; x <= 4; x++) {
				if (board[x][y].piece != firstTilePiece) {
					connecting = false;
				}
			}
			if (connecting) {
				if (firstTilePiece == Tile.Piece.BLACK) {
					xWin = true;
				} else if (firstTilePiece == Tile.Piece.WHITE) {
					oWin = true;
				}
			}
		}

		Tile.Piece topLeftTilePiece = board[0][0].piece;

		boolean connectingTLtoBR = true;
		for (int i = 0; i <= 4; i++) {
			if (board[i][i].piece != topLeftTilePiece) {
				connectingTLtoBR = false;
			}
		}
		if (connectingTLtoBR) {
			if (topLeftTilePiece == Tile.Piece.BLACK) {
				xWin = true;
			} else if (topLeftTilePiece == Tile.Piece.WHITE) {
				oWin = true;
			}
		}

		Tile.Piece topRightTilePiece = board[4][0].piece;

		boolean connectingTRtoBL = true;
		for (int i = 0; i <= 4; i++) {
			if (board[4 - i][i].piece != topRightTilePiece) {
				connectingTRtoBL = false;
			}
		}
		if (connectingTRtoBL) {
			if (topRightTilePiece == Tile.Piece.BLACK) {
				xWin = true;
			} else if (topRightTilePiece == Tile.Piece.WHITE) {
				oWin = true;
			}
		}

		return new boolean[]{xWin, oWin}; // {black, white}
	}

	public void pushDown(int x, Tile.Piece TilePiece) {
		int pushAmount = 3;

		for (int y = 0; y <= 3; y++) {
			if (board[x][y].piece == Tile.Piece.NOTHING) {
				pushAmount = y - 1;
				break;
			}
		}
		for (int y = pushAmount; y >= 0; y--) {
			board[x][y + 1].setPiece(board[x][y].piece);
		}
		board[x][0].setPiece(TilePiece);
	}

	public void pushLeft(int y, Tile.Piece TilePiece) {
		int pushAmount = 3;

		for (int x = 0; x <= 3; x++) {
			if (board[x][y].piece == Tile.Piece.NOTHING) {
				pushAmount = x-1;
				break;
			}
		}
		for (int x = pushAmount; x >= 0; x--) {
			board[x + 1][y].setPiece(board[x][y].piece);
		}
		board[0][y].setPiece(TilePiece);
	}
}
