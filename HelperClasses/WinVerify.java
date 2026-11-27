import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
// 5 possible results:
// N = Not Decided Yet;
// T = Tied
// X = X wins
// O = O wins
// L = Something's wrong and I have severely messed up

class WinVerify {
    //same thing as main, however it's for menace
    public static String check(String board) {
        String winner = "";
        String[] splitBoard = board.split(" ");

        //Row
        for (int i = 0; i < splitBoard.length; i += 3) {
            if (splitBoard[i].equals(splitBoard[i+1]) && splitBoard[i+1].equals(splitBoard[i+2])) {
                if (splitBoard[i].equals("-")) {
                    continue;
                }
                winner = splitBoard[i];
                return winner;
            }
        }

        //Column
        for (int i = 0; i < 3; i++) {
            if (splitBoard[i].equals(splitBoard[i+3]) && splitBoard[i+3].equals(splitBoard[i+6])) {
                if (splitBoard[i].equals("-")) {
                    continue;
                }
                winner = splitBoard[i];
                return winner;
            }
        }

        //diagonals
        if (!splitBoard[0].equals("-") && splitBoard[0].equals(splitBoard[4]) && splitBoard[4].equals(splitBoard[8])) {
            winner = splitBoard[0];
            return winner;
        }
        if (!splitBoard[2].equals("-") && splitBoard[2].equals(splitBoard[4]) && splitBoard[4].equals(splitBoard[6])) {
            winner = splitBoard[2];
            return winner;
        }

        //tie or undecided?
        for (int i = 0; i < splitBoard.length; i++) {
            if (splitBoard[i].equals("-")) {
                winner = "N";
                return winner;
            }
        }
        winner = "T";
        return winner;
    }
    //args[0] is the file path
    public static void main(String[] args) {
        String winner = "";
        String[] board = new String[9];
        int count = 0;
        try (BufferedReader br = new BufferedReader(new FileReader(args[0]))) {
            String line = "";
            while ((line = br.readLine()) != null) {
                if (line.equals("board")) {
                    continue;
                }
                String[] splitLine = line.split(" ");
                for (String l : splitLine) {
                    board[count++] = l;
                }
            }
        } catch (IOException i) {
            System.out.println("L");
        }


        //Row
        for (int i = 0; i < board.length; i += 3) {
            if (board[i].equals(board[i+1]) && board[i+1].equals(board[i+2])) {
                if (board[i].equals("-")) {
                    continue;
                }
                winner = board[i];
                System.out.println(winner);
                return;
            }
        }

        //Column
        for (int i = 0; i < 3; i++) {
            if (board[i].equals(board[i+3]) && board[i+3].equals(board[i+6])) {
                if (board[i].equals("-")) {
                    continue;
                }
                winner = board[i];
                System.out.println(winner);
                return;
            }
        }

        //diagonals
        if (!board[0].equals("-") && board[0].equals(board[4]) && board[4].equals(board[8])) {
            winner = board[0];
            System.out.println(winner);
            return;
        }
        if (!board[2].equals("-") && board[2].equals(board[4]) && board[4].equals(board[6])) {
            winner = board[2];
            System.out.println(winner);
            return;
        }

        //tie or undecided?
        for (int i = 0; i < board.length; i++) {
            if (board[i].equals("-")) {
                winner = "N";
                System.out.println(winner);
                return;
            }
        }

        winner = "T";
        System.out.println(winner);
    }
}