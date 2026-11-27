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
    public static String findBoard(ArrayList<String> csv, String currentBoard, String fileName) {
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
            try (PrintWriter out = new PrintWriter(new FileWriter(fileName, true))) {
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

        return findBoard(getBoards(fileName),currentBoard, fileName);
    }

    //plays the game
    public static void main(String[] args) { //
        Random rand = new Random();
        //makes menace build (menace plays first)
        //args[0] is the board given
        //args[1] is the mode menace should be running on
        //args[2] is the filepath for MenaceTable.csv
        //args[3] is the filepath for PositionStorage.csv
        String currentBoard = args[0];  //board.txt is different than "- - - - - - - - -" needed
        String PositionStorage = args[2];
        String MenaceTable = args[3];
        
        String[] splitCurrentBoard = currentBoard.replace("board\n", "").split("\\s+"); // replace to get rid of 'board'
        currentBoard = String.join(" ", splitCurrentBoard);

        //find which position menace played
        //adjusts accordingly
        switch (args[1]) {
            case "learn": {  //probably give args[0] as the winning/losing board
                int howManyBeadsToAdd = 0;

                switch (WinVerify.check(currentBoard)) {
                    case "X" -> {
                        howManyBeadsToAdd = 3;
                    }
                    case "O" -> {
                        howManyBeadsToAdd = -1;
                    }
                    case "T" -> {
                        howManyBeadsToAdd = 1;
                    }
                }

                ArrayList<String> boardMoves = new ArrayList<>();
                try (BufferedReader br = new BufferedReader(new FileReader(PositionStorage))) {
                    String line = "";
                    while ((line = br.readLine()) != null) {
                        boardMoves.add(line);
                    }
                } catch (Exception e) {
                }

                ArrayList<String> csvFile = new ArrayList<>();
                try (BufferedReader br = new BufferedReader(new FileReader(MenaceTable))) {
                    String line = "";
                    while ((line = br.readLine()) != null) {
                        if (line.split(",")[0].equals("board_number")) {
                            csvFile.add(line);
                            continue;
                        }

                        String comparisonBoard = "";
                        for (int i = 1; i < 10; i++) {
                            comparisonBoard += line.split(",")[i];
                            if (i != 9) {
                                comparisonBoard += " ";
                            }
                        }

                        for (String board : boardMoves) {
                            if (boardCheck(board.split(",")[0], comparisonBoard)) {  //boardMoves is a csv remember
                                String[] splitLine = line.split(",");

                                for (int i = 0; i < splitLine.length; i++) {
                                    if (Integer.parseInt(board.split(",")[1]) == i) {  //change value
                                        int result = Integer.parseInt(splitLine[i]) + howManyBeadsToAdd;
                                        splitLine[i] = Integer.toString(result);
                                    }
                                }


                                line = String.join(",", splitLine);
                                break;
                            }
                        }

                        csvFile.add(line);
                    }

                } catch (Exception e) {
                    e.getMessage();
                }
                
                if (!csvFile.isEmpty()) {
                    try (PrintWriter out = new PrintWriter(new FileWriter(MenaceTable, false))) {
                        for (int i = 0; i < csvFile.size(); i++) {
                            if (i != csvFile.size() - 1) {
                                out.println(csvFile.get(i));
                            } else {
                                out.print(csvFile.get(i));
                            }
                        }
                        for (String line : csvFile) {
                            
                        }
                    } catch (Exception e) {
                        e.getMessage();
                    }
                }
                
                break;
            }
            //restarts the PositionStorage.csv
            case "start": { //intentionally fall through
                try (PrintWriter out = new PrintWriter(new FileWriter(PositionStorage, false))) {
                    out.print("");
                } catch (Exception e) {
                    e.getMessage();
                }
            }
            default: {
                ArrayList<String> csv = getBoards(MenaceTable);

                String[] boardLine = findBoard(csv, currentBoard, MenaceTable).split(",");

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

                //we need to store which bead was placed
                try (PrintWriter out = new PrintWriter(new FileWriter(PositionStorage, true))) {
                    out.print(currentBoard);
                    out.print(",");
                    out.println(chosen);
                } catch (Exception e) {

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
                //we need to print out the position where MENACE placed for our start.sh
                System.out.println(chosen - 9);
            }
        }
    }
}