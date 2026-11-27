import java.util.Scanner;

/**
 * Tic-tac-toe bot using minimax. Developed by Claude
 *
 * Input:  a board string (first command-line argument, or stdin if none).
 *         9 cells read row by row, '-' = empty, e.g. "- - - - X - - - -".
 *         Newlines/extra whitespace and a leading "board" header word
 *         (as in board.txt) are ignored.
 * Output: a single number 1-9 on stdout, the square the bot should play:
 *             1 2 3
 *             4 5 6
 *             7 8 9
 *         Nothing else is ever written to stdout. Errors go to stderr with a
 *         non-zero exit code (1 = bad input, 2 = game already over).
 *
 * The bot always plays O (second); the human plays X and moves first.
 *
 * Usage (bash):
 *   botPlacement="$(java TicTacToeBot.java "$(cat board.txt)")"
 */
public class MiniMaxBot {

    private static final char EMPTY = '-';
    private static final char BOT = 'O';

    private static final int[][] LINES = {
        {0, 1, 2}, {3, 4, 5}, {6, 7, 8},   // rows
        {0, 3, 6}, {1, 4, 7}, {2, 5, 8},   // columns
        {0, 4, 8}, {2, 4, 6}               // diagonals
    };

    public static void main(String[] args) {
        String input;
        if (args.length > 0) {
            input = args[0];
        } else {
            Scanner sc = new Scanner(System.in);
            StringBuilder sb = new StringBuilder();
            while (sc.hasNextLine()) sb.append(sc.nextLine()).append('\n');
            input = sb.toString();
        }

        char[] board = parseBoard(input);

        // X moves first, so on the bot's (O's) turn X has exactly one more piece.
        if (count(board, 'X') != count(board, 'O') + 1) {
            fail(1, "invalid board for O to move (expected one more X than O).");
        }

        if (winner(board) != EMPTY || isFull(board)) fail(2, "game is already over.");

        System.out.println(bestMove(board, BOT) + 1);   // 1-9
    }

    // ---------- Minimax ----------

    private static int bestMove(char[] board, char bot) {
        char opp = other(bot);
        int bestScore = Integer.MIN_VALUE;
        int bestIdx = -1;

        for (int i = 0; i < 9; i++) {
            if (board[i] != EMPTY) continue;
            board[i] = bot;
            int score = minimax(board, opp, bot, 1);
            board[i] = EMPTY;
            if (score > bestScore) {
                bestScore = score;
                bestIdx = i;
            }
        }
        return bestIdx;
    }

    /**
     * @param toMove the player whose turn it is on this board
     * @param bot    the player we are optimizing for
     * @param depth  plies from the root (prefers faster wins / slower losses)
     */
    private static int minimax(char[] board, char toMove, char bot, int depth) {
        char w = winner(board);
        if (w == bot) return 10 - depth;
        if (w != EMPTY) return depth - 10;
        if (isFull(board)) return 0;

        boolean maximizing = (toMove == bot);
        int best = maximizing ? Integer.MIN_VALUE : Integer.MAX_VALUE;

        for (int i = 0; i < 9; i++) {
            if (board[i] != EMPTY) continue;
            board[i] = toMove;
            int score = minimax(board, other(toMove), bot, depth + 1);
            board[i] = EMPTY;
            best = maximizing ? Math.max(best, score) : Math.min(best, score);
        }
        return best;
    }

    // ---------- Helpers ----------

    private static char winner(char[] b) {
        for (int[] l : LINES) {
            if (b[l[0]] != EMPTY && b[l[0]] == b[l[1]] && b[l[1]] == b[l[2]]) {
                return b[l[0]];
            }
        }
        return EMPTY;
    }

    private static boolean isFull(char[] b) {
        for (char c : b) if (c == EMPTY) return false;
        return true;
    }

    private static int count(char[] b, char p) {
        int n = 0;
        for (char c : b) if (c == p) n++;
        return n;
    }

    private static char other(char p) {
        return p == 'X' ? 'O' : 'X';
    }

    private static char[] parseBoard(String s) {
        // Drop an optional "board" header word, then all whitespace.
        String compact = s.replaceAll("(?i)board", "").replaceAll("\\s+", "").toUpperCase();
        if (!compact.matches("[XO-]{9}")) {
            fail(1, "board must have exactly 9 cells of X, O or -, got: \"" + compact + "\"");
        }
        return compact.toCharArray();
    }

    private static void fail(int code, String msg) {
        System.err.println("Error: " + msg);
        System.exit(code);
    }
}