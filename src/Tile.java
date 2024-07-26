import java.awt.*;

/**
 *
 * @author Ashk3000
 */
public class Tile {

	public enum Piece {
		WHITE, BLACK, NOTHING // BLACK: X, green; WHITE: O, pink; NOTHING: no piece, no winner
	}

	public static Piece togglePiece(Piece piece) {
		if (piece == Piece.WHITE) {
			return Piece.BLACK;
		} else if (piece == Piece.BLACK) {
			return Piece.WHITE;
		} else {
			return Piece.NOTHING;
		}
	}

	public Piece piece;

	private Board board;

	private int x;
	private int y;

	public Tile() {
		piece = Piece.NOTHING;
	}

	public Tile(int x, int y, Board board) {
		piece = Piece.NOTHING;

		this.board = board;

		this.x = x;
		this.y = y;
	}

	public Tile clone(Board board) {
		Tile outputTile = new Tile(x, y, board);
		outputTile.setPiece(piece);
		return outputTile;
	}

	public void setPiece(Piece piece) {
		this.piece = piece;

		if (board != null) {
			board.repaint();
		}
	}

	public void draw(Graphics2D g2) {

		

		int xPos = (x*55) - 135;
		int yPos = (y*-55) - 179 - 50;

		g2.setColor(Color.GRAY);
		g2.fillRect(xPos, yPos, 50, 50);

		switch (piece) {
			case NOTHING ->
				g2.setColor(Color.BLUE);
			case BLACK ->
				g2.setColor(Color.GREEN);
			case WHITE ->
				g2.setColor(Color.PINK);
		}

		g2.fillOval(xPos, yPos, 50, 50);
	}
}
