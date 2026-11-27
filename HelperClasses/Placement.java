import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

//args[0] - where to place
//args[1] - who placed it (X or O)
//args[2] - filepath of the board.txt
class Placement {
    public static void main(String[] args) {
        //we subtract by 1 to maintain 0 based index consistency
        int location = Integer.parseInt(args[0]) - 1;
        int row = 0;
        int targetRow = location / 3;
        int targetColumn = location % 3;
        String[] newLines = new String[4];
        try (BufferedReader br = new BufferedReader(new FileReader(args[2]))) {
            String line = "";
            while ((line = br.readLine()) != null) {
                if (line.equals("board")) {
                    newLines[0] = line;
                    continue;
                }
                if (targetRow == row) {
                    String[] specificRow = line.split(" ");
                    String target = specificRow[targetColumn];
                    if (target.equals("-")) {
                        specificRow[targetColumn] = args[1];
                        line = "";
                        for (int i = 0; i < 3; i++) {
                            if (i == 2) {
                                line += specificRow[i];
                            } else {
                                line += specificRow[i] + " ";
                            }
                        }
                        System.out.println("Success");
                    } else {
                        System.out.println("Failure");
                    }
                }
                newLines[row + 1] = line;
                row++;
            }
        } catch (Exception i) {
            System.out.println("uhoh");
        }
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(args[2]))) {
            for (String newLine : newLines) {
                bw.write(newLine);
                bw.newLine();
            }
        } catch (IOException i) {
            System.out.println("uhoh");
        }
    }
}
