
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

public class ConvertPathToCode {

    // GOAL Output Style:
    // vector<RAMSETEPoint> path;
    // RAMSETEPoint pt0(0, 0, 0, 0, true, true, true, 0, 0, false);
    // path.push_back(pt);
    //RAMSETEPoint pt1(0, 0, 0, 0, true, true, true, 0, 0, false);
    // path.push_back(pt1);
    static String pathFileName = "newPathOutput.txt";
    static String outputFileName = "codeOutput.txt";

    public static void main(String[] args) throws IOException {
        try (Scanner sc = new Scanner(new File(pathFileName)); FileWriter myWriter = new FileWriter(outputFileName)) {
            myWriter.write("// These paths were generated using a path planner along with custom helper functions written in Java\n\n");

            int currPointNum = 0;
            String firstLine = sc.nextLine();
            String currVectName = firstLine.split(": ")[1].replaceAll(" ", "_");

            myWriter.write(firstLine.contains("REVERSE") ? "// Reverse bot movement path\n" : "// Forward bot movement path\n");
            myWriter.write("vector<RAMSETEPoint> " + currVectName + ";\n");

            while (sc.hasNextLine()) {
                String nextLine = sc.nextLine();
                if (nextLine.length() == 0) {
                } else if (nextLine.contains("PATH")) {
                    myWriter.write(nextLine.contains("REVERSED") ? "\n// Reverse bot movement path\n" : "\n// Forward bot movement path\n");
                    String pathName = nextLine.split(": ")[1].replaceAll(" ", "_");
                    myWriter.write("vector<RAMSETEPoint> " + pathName + ";\n");
                    currVectName = pathName;
                } else {
                    myWriter.write("RAMSETEPoint pt" + currPointNum + " (" + nextLine + ", 0, false, false, false, false);\n");
                    myWriter.write(currVectName + ".push_back(pt" + currPointNum + ");\n");
                    currPointNum++;
                }
            }
        }
    }
}
