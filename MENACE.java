import java.io.*;
import java.util.ArrayList;
import java.util.Random;

public class MENACE {
    public static boolean boardCheck(String board, String comparison) {
        //format should be "1 2 3 4 5 6 7 8 9"
        //we are going to compare all the rotations and reflections
        int[] normal = {0, 1, 2, 3, 4, 5, 6, 7, 8};
        int[] rotated90 = {6, 3, 0, 7, 4, 1, 8, 5, 2};
        int[] rotated180 = {8, 7, 6, 5, 4, 3, 2, 1, 0};
        int[] rotated270 = {2, 5, 8, 1, 4, 7, 0, 3, 6};
        int[] mirrorHorizontal = {2, 1, 0, 5, 4, 3, 8, 7, 6};
        int[] mirrorVertical = {6, 7, 8, 3, 4, 5, 0, 1, 2};
        int[] topLefttoBottomRightMirror = {0, 3, 6, 1, 4, 7, 2, 5, 8};
        int[] topRighttoBottomLeftMirror = {8, 5, 2, 7, 4, 1, 6, 3, 0};
        int[][] transformations = {normal, rotated90, rotated180, rotated270, mirrorHorizontal, mirrorVertical, topLefttoBottomRightMirror, topRighttoBottomLeftMirror};

        boolean isEqual = false;
        for (int[] transformation : transformations) {
            String[] transformedBoard = new String[9];

            for (int i = 0; i < 9; i++) {
                transformedBoard[i] = board.split(" ")[transformation[i]];
            }

            isEqual = true;
            for (int i = 0; i < 9; i++) {
                if (!transformedBoard[i].equals(comparison.split(" ")[i])) {
                    isEqual = false;
                    break;
                }
            }
            if (isEqual) {
                //System.out.println(Arrays.toString(board));
                break;
            }
        }
        return isEqual;
    }

    public static ArrayList<String> getBoards(String fileName) {
        ArrayList<String> csv = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            String line = "";
            while ((line = br.readLine()) != null) {
                csv.add(line);
            }
        } catch (Exception e) {

        }

        return csv;
    }

    //will return Line
    public static String findBoard(ArrayList<String> csv, String currentBoard) {
        //attempts to find the board
        //boardLine will be the entire line
        String boardLine = null;

        int count = 0; //count needs to keep track so that i can store board_number
        for (String line : csv) {
            String[] splitLine = line.split(",");
            //disregard the first line
            if (splitLine[0].equals("board_number")) {
                count++;
                continue;
            }
            count++;

            //rebuilds the board from the csv file
            String comparisonBoard = "";
            for (int i = 1; i < 10; i++) {
                comparisonBoard += splitLine[i];
                if (i != 9) {
                    comparisonBoard += " ";
                }
            }

            if (boardCheck(currentBoard, comparisonBoard)) {
                boardLine = line;
                break;
            }
        }


        if (boardLine == null) { //board not found, insert into the csv
            try (PrintWriter out = new PrintWriter(new FileWriter("BoardPositions.csv", true))) {
                out.println();
                out.print(count);
                out.print(",");
                String[] splitBoard = currentBoard.split(" ");
                for (String positions : splitBoard) {
                    out.print(positions);
                    out.print(",");
                }

                for (int i = 10; i < 19; i++) {
                    if (splitBoard[i - 10].equals("-")) {
                        out.print(3);
                    } else {
                        out.print(0);
                    }
                    if (i != 18) {
                        out.print(",");
                    }
                }
            } catch (Exception i) {

            }
        } else {
            return boardLine;
        }

        return findBoard(getBoards("BoardPositions.csv"),currentBoard);
    }

    //find which position menace played
    //adjusts accordingly
    public static void Analyze() {

    }


    //plays the game
    public static void main(String[] args) { //
        Random rand = new Random();
        //makes menace build (menace plays first)
        //args[0] is the board given

        String currentBoard = args[0];
        if (args.length > 1) {

            return;
        }
        ArrayList<String> csv = getBoards("BoardPositions.csv");

        String[] boardLine = findBoard(csv, currentBoard).split(",");
        try (PrintWriter out = new PrintWriter(new FileWriter("board_position_storage.csv", true))) {
            out.println(currentBoard);
        } catch (Exception e) {

        }

        int beads = 0;
        for (int i = 10; i < 19; i++) {
            beads += Integer.parseInt(boardLine[i]);
        }
        int selection = rand.nextInt(1, beads + 1);  //it is exclusive
        int chosen = 0;

        for (int i = 10; i < 19; i++) {
            selection -= Integer.parseInt(boardLine[i]);
            if (selection <= 0) {
                chosen = i;
                break;
            }
        }

        //builds the board
        String outputBoard = "";
        for (int i = 1; i < 10; i++) {
            if (i != chosen - 9) {
                outputBoard += boardLine[i];
            } else {
                outputBoard += "X";
            }

            if (i != 9) {
                outputBoard += " ";
            }

        }
        System.out.println(outputBoard);
    }
}

