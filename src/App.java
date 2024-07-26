
import java.awt.Dimension;
import java.awt.event.*;
import java.util.InputMismatchException;
import java.util.Scanner;
import java.util.concurrent.CountDownLatch;
import javax.swing.*;

/**
 *
 * @author Ashk3000
 */
public class App {

    public static void spotToMove(int spot, Board board, Tile.Piece currentPiece) {
        if (spot <= 4) {
            board.pushDown(4 - spot, currentPiece);
        } else if (spot > 4) {
            board.pushLeft(spot - 5, currentPiece);
        } else {

        }
    }

    public static int getSpotConsole(Scanner scanner) {
        int input = -1;

        System.out.println("Spot [1-10]");
        while (input > 10 || input < 1) {
            try {
                input = scanner.nextInt();
            } catch (InputMismatchException e) {
                input = -1;
            }
            scanner.nextLine();
            if (input > 10 || input < 0) {
                System.out.println("Invalid input");
            } else if (input == 0) {
                input = 10;
            }
        }

        return input - 1;
    }

    public static int getSpotButton(Scanner scanner, JButton[] buttons) {
        final CountDownLatch latch = new CountDownLatch(1);
        final int[] output = {-1};

        for (int i = 0; i <= 9; i++) {
            JButton button = buttons[i];
            final int spot = i;

            button.setEnabled(true);

            button.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    output[0] = spot;
                    latch.countDown();
                }
            });
        }

        try {
            latch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        for (JButton button : buttons) {
            button.setEnabled(false);
        }

        return output[0];
    }

    public static boolean winCheck(Board board, Tile.Piece currentPiece) { // returns true if the game has ended, and prints a message
        boolean[] winningPieces = board.winCheck();

        if (winningPieces[0] && winningPieces[1]) {
            board.print();
            if (winningPieces[0]) {
                System.out.println("X wins!");
                return true;
            } else if (winningPieces[1]) {
                System.out.println("O wins!");
                return true;
            }
            return true;
        } else if (winningPieces[0]) {
            board.print();
            System.out.println("X wins!");
            return true;
        } else if (winningPieces[1]) {
            board.print();
            System.out.println("O wins!");
            return true;
        }
        return false;
    }

    public static JButton[] makeButtons(Board board, JLayeredPane panel, Scanner scanner) { // generate JButtons, position them in panel, and return list of them
        JButton[] buttons = new JButton[10];
        for (int i = 0; i <= 9; i++) {
            JButton button = new JButton();
            buttons[i] = button;

            double y = Math.abs(i - 4.5);
            int yPos = (int) (y) * 38;
            int xPos = i * 38;
            if (i > 4) {
                xPos += 53;
            }

            button.setBounds(xPos, yPos, 50, 50);

            panel.add(button, Integer.valueOf((int) y));

            button.setEnabled(false);

            button.repaint();
        }

        return buttons;
    }

    public static void game(Scanner scanner, JPanel parent) {
        Tile.Piece currentPiece = Tile.Piece.WHITE;

        Board board = new Board();

        JLayeredPane panel = new JLayeredPane();
        panel.setLayout(null);
        parent.add(panel);

        board.setBounds(0, 0, 445, 413);
        panel.setBounds(0, 0, 445, 413);
        panel.add(board, Integer.valueOf(-1));

        JButton[] buttons = makeButtons(board, panel, scanner);

        while (true) {
            board.print();

            if (currentPiece == Tile.Piece.BLACK) { 
                System.out.println("X's turn");
                //int spot = getSpotButton(scanner, buttons); // user turn
                //spotToMove(spot, board, currentPiece);
                spotToMove(board.bestMove(currentPiece, 6), board, currentPiece); // bot turn

            } else if (currentPiece == Tile.Piece.WHITE) { 
                System.out.println("O's turn");
                spotToMove(board.bestMove(currentPiece, 6), board, currentPiece); // bot turn
            }


            if (winCheck(board, currentPiece)) {
                return;
            }

            switch (currentPiece) {
                case BLACK ->
                    currentPiece = Tile.Piece.WHITE;
                case WHITE ->
                    currentPiece = Tile.Piece.BLACK;
                default ->
                    System.err.println("currentPiece is not BLACK or WHITE");
            }
        }

    }

    public static void main(String[] args) throws Exception {

        JFrame frame = new JFrame("Slide 5");

        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });

        JPanel panel = new JPanel();

        frame.add(panel);
        panel.setBounds(0, 0, 445, 413);

        //frame.setSize(445, 413);
        frame.setMinimumSize(new Dimension(461, 452));

        frame.setLayout(null);
        panel.setLayout(null);

        frame.setVisible(true);

        Scanner scanner = new Scanner(System.in);

        game(scanner, panel);
    }
}
