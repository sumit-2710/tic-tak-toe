import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Random;

public class TicTacToe extends JFrame {
    private JButton[][] buttons = new JButton[3][3];
    private boolean playerXTurn = true;
    private boolean playWithComputer = false;
    private String playerXName = "Player X";
    private String playerOName = "Player O";
    private JLabel statusLabel = new JLabel("Welcome to Tic-Tac-Toe!");

    public TicTacToe() {
        setTitle("Tic-Tac-Toe");
        setSize(400, 450);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Status label
        statusLabel.setHorizontalAlignment(SwingConstants.CENTER);
        statusLabel.setFont(new Font("Arial", Font.BOLD, 16));
        add(statusLabel, BorderLayout.NORTH);

        // Game board
        JPanel boardPanel = new JPanel(new GridLayout(3, 3));
        Font buttonFont = new Font("Arial", Font.BOLD, 60);
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                JButton button = new JButton("");
                button.setFont(buttonFont);
                buttons[i][j] = button;
                button.addActionListener(new ButtonClickListener(i, j));
                boardPanel.add(button);
            }
        }
        add(boardPanel, BorderLayout.CENTER);

        // Initialize game
        initializeGame();

        setVisible(true);
    }

    private void initializeGame() {
        // Mode selection
        String[] options = {"Play with Person", "Play with Computer"};
        int mode = JOptionPane.showOptionDialog(this, "Choose Game Mode:", "Game Mode",
                JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, options, options[0]);
        playWithComputer = (mode == 1);

        // Get player names
        playerXName = JOptionPane.showInputDialog(this, "Enter name for Player X:", "Player X");
        if (playerXName == null || playerXName.trim().isEmpty()) {
            playerXName = "Player X";
        }

        if (playWithComputer) {
            playerOName = "Computer";
        } else {
            playerOName = JOptionPane.showInputDialog(this, "Enter name for Player O:", "Player O");
            if (playerOName == null || playerOName.trim().isEmpty()) {
                playerOName = "Player O";
            }
        }

        statusLabel.setText(playerXName + "'s Turn (X)");
    }

    private class ButtonClickListener implements ActionListener {
        private int row;
        private int col;

        public ButtonClickListener(int i, int j) {
            this.row = i;
            this.col = j;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            JButton button = buttons[row][col];
            if (!button.getText().equals("")) {
                return; // Cell already occupied
            }

            if (playerXTurn) {
                button.setText("X");
                statusLabel.setText(playerOName + "'s Turn (O)");
            } else {
                button.setText("O");
                statusLabel.setText(playerXName + "'s Turn (X)");
            }

            button.setEnabled(false);

            if (checkWin()) {
                String winner = playerXTurn ? playerXName : playerOName;
                JOptionPane.showMessageDialog(null, winner + " wins!");
                resetBoard();
                return;
            } else if (isBoardFull()) {
                JOptionPane.showMessageDialog(null, "It's a tie!");
                resetBoard();
                return;
            }

            playerXTurn = !playerXTurn;

            if (playWithComputer && !playerXTurn) {
                computerMove();
            }
        }
    }

    private void computerMove() {
        // Simple AI: Random move
        Random rand = new Random();
        int row, col;
        do {
            row = rand.nextInt(3);
            col = rand.nextInt(3);
        } while (!buttons[row][col].getText().equals(""));

        buttons[row][col].setText("O");
        buttons[row][col].setEnabled(false);
        statusLabel.setText(playerXName + "'s Turn (X)");

        if (checkWin()) {
            JOptionPane.showMessageDialog(null, playerOName + " wins!");
            resetBoard();
            return;
        } else if (isBoardFull()) {
            JOptionPane.showMessageDialog(null, "It's a tie!");
            resetBoard();
            return;
        }

        playerXTurn = true;
    }

    private boolean checkWin() {
        String symbol = playerXTurn ? "X" : "O";

        // Check rows and columns
        for (int i = 0; i < 3; i++) {
            if (buttons[i][0].getText().equals(symbol) &&
                buttons[i][1].getText().equals(symbol) &&
                buttons[i][2].getText().equals(symbol)) {
                return true;
            }
            if (buttons[0][i].getText().equals(symbol) &&
                buttons[1][i].getText().equals(symbol) &&
                buttons[2][i].getText().equals(symbol)) {
                return true;
            }
        }

        // Check diagonals
        if (buttons[0][0].getText().equals(symbol) &&
            buttons[1][1].getText().equals(symbol) &&
            buttons[2][2].getText().equals(symbol)) {
            return true;
        }
        if (buttons[0][2].getText().equals(symbol) &&
            buttons[1][1].getText().equals(symbol) &&
            buttons[2][0].getText().equals(symbol)) {
            return true;
        }

        return false;
    }

    private boolean isBoardFull() {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (buttons[i][j].getText().equals("")) {
                    return false;
                }
            }
        }
        return true;
    }

    private void resetBoard() {
        int response = JOptionPane.showConfirmDialog(this, "Do you want to play again?", "Play Again",
                JOptionPane.YES_NO_OPTION);
        if (response == JOptionPane.NO_OPTION) {
            System.exit(0);
        }

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                buttons[i][j].setText("");
                buttons[i][j].setEnabled(true);
            }
        }

        playerXTurn = true;
        statusLabel.setText(playerXName + "'s Turn (X)");
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new TicTacToe());
    }
}
