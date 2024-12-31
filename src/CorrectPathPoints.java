
import java.io.*;
import static java.lang.Math.*;
import java.util.*;

public class CorrectPathPoints {

    static String pathFileName = "path.txt";
    static String outputFileName = "newPathOutput.txt";

    public static void main(String[] args) throws IOException {
        try (Scanner sc = new Scanner(new File(pathFileName)); FileWriter myWriter = new FileWriter(outputFileName)) {
            ArrayList<String> lines = new ArrayList<>();
            do {
                lines.add(sc.nextLine());
            } while (sc.hasNextLine());

            boolean currPathIsReversed = lines.get(0).contains("REVERSED");

            myWriter.write("PATH: " + lines.get(0).split("START ")[1] + "(" + (currPathIsReversed ? "REVERSE" : "FORWARD") + ")");
            for (int i = 1; i < lines.size(); i++) {
                String nextLine = lines.get(i);

                if (nextLine.contains("START")) {
                    currPathIsReversed = nextLine.contains("REVERSED");
                    myWriter.write("\n\nPATH: " + nextLine.split("START ")[1] + "(" + (currPathIsReversed ? "REVERSE" : "FORWARD") + ")");
                    continue;
                }

                String[] splitLine = nextLine.split(",");
                myWriter.write("\n" + splitLine[0] + ", " + splitLine[1] + ", " + "100.0" + ", " + (splitLine.length > 3 ? convertAngle(Double.parseDouble(splitLine[3])) : findAngle(splitLine, lines.get(i + 1).split(","), currPathIsReversed)));
            }
        }
    }

    private static double convertAngle(double angle) {
        double newAngle = -angle + 90.0;
        if (newAngle < 0.0) {
            newAngle += 360.0;
        }
        return newAngle;
    }

    private static double findAngle(String[] currPt, String[] nextPt, boolean reversed) {
        double angle = atan2(Double.parseDouble(nextPt[1]) - Double.parseDouble(currPt[1]), Double.parseDouble(nextPt[0]) - Double.parseDouble(currPt[0])) * (180.0 / PI);
        if (angle < 0.0) {
            angle += 360.0;
        }
        if (reversed) {
            angle -= 180.0;
        }
        if (angle < 0.0) {
            angle += 360.0;
        }

        return angle;
    }
}
