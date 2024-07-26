
import java.awt.*;
import javax.swing.*;

/**
 *
 * @author Ashk3000
 */
public class Board extends JComponent {

    Tile[][] board = new Tile[5][5]; // TODO: rename to boardArray

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

    public boolean[] winCheck() { // returns: {black won, white won}
        return winCheck(board);
    }

    static boolean[] winCheck(Tile[][] board) { // returns: {black won, white won} NOTE: IF BOTH PLAYERS WIN AT THE SAME TIME THE PLAYER WHO CAUSED IT WINS
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

    public int[] longestChain() { // returns: {black longest, white longest} (does not include diagonals that do not point towards a corner)
        return longestChain(board);
    }

    static int[] longestChain(Tile[][] board) { // returns: {black longest, white longest} (does not include diagonals that do not point towards a corner)
        int xLongest = 0;
        int oLongest = 0;

        for (int x = 0; x <= 4; x++) {
            for (int startY = 0; startY <= 4; startY++) {
                Tile.Piece firstTilePiece = board[x][startY].piece;
                for (int y = startY; y <= 4; y++) {
                    if (board[x][y].piece != firstTilePiece) {
                        if (firstTilePiece == Tile.Piece.BLACK && y - startY > xLongest) {
                            xLongest = y - startY;
                        } else if (firstTilePiece == Tile.Piece.WHITE && y - startY > oLongest) {
                            oLongest = y - startY;
                        }
                        break;
                    }
                }
            }
        }

        for (int y = 0; y <= 4; y++) {
            for (int startX = 0; startX <= 4; startX++) {
                Tile.Piece firstTilePiece = board[startX][y].piece;
                for (int x = startX; x <= 4; x++) {
                    if (board[x][y].piece != firstTilePiece) {
                        if (firstTilePiece == Tile.Piece.BLACK && x - startX > xLongest) {
                            xLongest = x - startX;
                        } else if (firstTilePiece == Tile.Piece.WHITE && x - startX > oLongest) {
                            oLongest = x - startX;
                        }
                        break;
                    }
                }
            }
        }

        for (int startI = 0; startI <= 4; startI++) {
            Tile.Piece firstTilePiece = board[startI][startI].piece;
            for (int i = startI; i <= 4; i++) {
                if (board[i][i].piece != firstTilePiece) {
                    if (firstTilePiece == Tile.Piece.BLACK && i - startI > xLongest) {
                        xLongest = i - startI;
                    } else if (firstTilePiece == Tile.Piece.WHITE && i - startI > oLongest) {
                        oLongest = i - startI;
                    }
                    break;
                }
            }
        }

        for (int startI = 0; startI <= 4; startI++) {
            Tile.Piece firstTilePiece = board[4 - startI][startI].piece;
            for (int i = startI; i <= 4; i++) {
                if (board[4 - i][i].piece != firstTilePiece) {
                    if (firstTilePiece == Tile.Piece.BLACK && i - startI > xLongest) {
                        xLongest = i - startI;
                    } else if (firstTilePiece == Tile.Piece.WHITE && i - startI > oLongest) {
                        oLongest = i - startI;
                    }
                    break;
                }
            }
        }

        return new int[]{xLongest, oLongest}; // {black, white}
    }

    public void pushDown(int x, Tile.Piece piece) {
        pushDown(x, piece, board);
    }

    static void pushDown(int x, Tile.Piece piece, Tile[][] board) {
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
        board[x][0].setPiece(piece);
    }

    public void pushLeft(int y, Tile.Piece piece) {
        pushLeft(y, piece, board);
    }

    static void pushLeft(int y, Tile.Piece piece, Tile[][] board) {
        int pushAmount = 3;

        for (int x = 0; x <= 3; x++) {
            if (board[x][y].piece == Tile.Piece.NOTHING) {
                pushAmount = x - 1;
                break;
            }
        }
        for (int x = pushAmount; x >= 0; x--) {
            board[x + 1][y].setPiece(board[x][y].piece);
        }
        board[0][y].setPiece(piece);
    }

    /**
     *
     * Warning: replacing board array with modified clone may ruin references to
     * tiles from the original board
     *
     * @param boardArray The boardArray to clone
     * @return A clone of boardArray
     */
    static Tile[][] cloneBoardArray(Tile[][] boardArray) {
        Tile[][] outputBoardArray = new Tile[5][5];
        for (int x = 0; x <= 4; x++) {
            for (int y = 0; y <= 4; y++) {
                outputBoardArray[x][y] = boardArray[x][y].clone(null);
            }
        }
        return outputBoardArray;
    }

	public int bestMove(Tile.Piece playerPiece, int depth) {
		Tile.Piece otherPlayerPiece = Tile.togglePiece(playerPiece);
		

		int[] paths = new int[10];

		for (int i = 0; i <= 9; i++) {
            Tile[][] tempBoard = cloneBoardArray(board);
            if (i <= 4) {
                pushDown(4 - i, playerPiece, tempBoard);
            } else {
                pushLeft(i - 5, playerPiece, tempBoard);
            }
            
			paths[i] = minimax(otherPlayerPiece, depth - 1, tempBoard);
        }

		if (playerPiece == Tile.Piece.BLACK) {
			int minIndex = 0;
			for (int pathIndex = 0; pathIndex <= 9; pathIndex++) {
				if (paths[pathIndex] <= paths[minIndex]) {
					minIndex = pathIndex;
				}
			}
			return minIndex;
		} else {
			int maxIndex = 0;
			for (int pathIndex = 0; pathIndex <= 9; pathIndex++) {
				if (paths[pathIndex] > paths[maxIndex]) {
					maxIndex = pathIndex;
				}
			}
			return maxIndex;
		}
	}

    static int minimax(Tile.Piece playerPiece, int depth, Tile[][] board) { // evaluates the best move
		Tile.Piece otherPlayerPiece = Tile.togglePiece(playerPiece);

        if (depth == 0) {
            return evaluate(board, playerPiece);
        }

        boolean[] winCheck = winCheck(board);

		if (winCheck[0] && winCheck[1]) { // If both have connections whoever caused it wins
            if (playerPiece == Tile.Piece.BLACK) {
                return -10;
            } else {
                return 10;
            }
        }

        if (winCheck[0]) { // Black win
            return -10;
        }

        if (winCheck[1]) { // White win
            return 10;
        }

        int[] paths = new int[10];

        for (int i = 0; i <= 9; i++) {
            Tile[][] tempBoard = cloneBoardArray(board);
            if (i <= 4) {
                pushDown(4 - i, playerPiece, tempBoard);
            } else {
                pushLeft(i - 5, playerPiece, tempBoard);
            }

            paths[i] = minimax(otherPlayerPiece, depth - 1, tempBoard);
        }

        if (playerPiece == Tile.Piece.BLACK) {
			int min = paths[0];
			for (int path : paths) {
				if (path < min) {
					min = path;
				}
			}
			return min;
		} else {
			int max = paths[0];
			for (int path : paths) {
				if (path > max) {
					max = path;
				}
			}
			return max;
		}
    }

    /**
     * evaluate which player is winning returns int -10 (black) to 10 (white)
     *
     * @return A scale indicating whos winning -10 (black) to 10 (white)
     */
    public int evaluate(Tile.Piece playerPiece) {
        return evaluate(board, playerPiece);
    }

    static int evaluate(Tile[][] board, Tile.Piece playerPiece) {

        boolean[] winCheck = winCheck(board);

        if (winCheck[0]) { // Black win
            return -10;
        }

        if (winCheck[1]) { // White win
            return 10;
        }

        if (!winCheck[0] && !winCheck[1]) { // No win
            int[] longestChain = longestChain(board);
            return longestChain[1] - longestChain[0]; // -4 to 4 (-5 or 5 would result in a win)
        }

        // If both have connections whoever caused it wins
        if (playerPiece == Tile.Piece.BLACK) {
            return -10;
        } else {
            return 10;
        }
    }
}
