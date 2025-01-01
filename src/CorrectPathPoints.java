
import java.io.*;
import static java.lang.Math.*;
import java.util.*;

public class CorrectPathPoints {

    static String pathFileName = "path.txt";
    static String outputFileName = "newPathOutput.txt";

    final static double MAX_VEL = 15.6; // In cm/s
    final static double LINEAR_SLOW_ON_TURN = 0.1; // Bigger number causes the robot to slowdown more on turns
    final static double ANGULAR_SPEED_ON_TURN = 0.5; // Bigger number causes robot to turn faster

    public static void main(String[] args) throws IOException {
        try (Scanner sc = new Scanner(new File(pathFileName)); FileWriter myWriter = new FileWriter(outputFileName)) {
            ArrayList<String> lines = new ArrayList<>();
            do {
                lines.add(sc.nextLine());
            } while (sc.hasNextLine());

            boolean currPathIsReversed = lines.get(0).contains("REVERSED");

            ArrayList<String> printedLines = new ArrayList<>();

            printedLines.add("PATH(" + (currPathIsReversed ? "REVERSE" : "FORWARD") + ")" + ": " + lines.get(0).split("START ")[1]);
            for (int i = 1; i < lines.size(); i++) {
                String nextLine = lines.get(i);

                if (nextLine.contains("START")) {
                    currPathIsReversed = nextLine.contains("REVERSED");
                    printedLines.add("\n\nPATH(" + (currPathIsReversed ? "REVERSE" : "FORWARD") + ")" + ": " + nextLine.split("START ")[1]);
                    continue;
                }

                String[] splitLine = nextLine.split(",");
                printedLines.add("\n" + splitLine[0] + ", " + splitLine[1] + ", " + (splitLine.length > 3 ? convertAngle(Double.parseDouble(splitLine[3])) : findAngle(splitLine, lines.get(i + 1).split(","), currPathIsReversed)));
            }

            ArrayList<String> outputLines = new ArrayList<>();
            for (int i = 0; i < printedLines.size(); i++) {
                if (i == printedLines.size() - 1 || printedLines.get(i + 1).contains("PATH")) {
                    outputLines.add(printedLines.get(i) + ", 0.0, 0.0");
                } else if (printedLines.get(i).contains("PATH")) {
                    outputLines.add(printedLines.get(i));
                } else {
                    double currHeading = Double.parseDouble(printedLines.get(i).split(", ")[2]);
                    double nextHeading = Double.parseDouble(printedLines.get(i + 1).split(", ")[2]);

                    double headingError = currHeading - nextHeading;

                    if (headingError < -180) {
                        headingError = 360 + headingError;
                    } else if (headingError > 180) {
                        headingError = headingError - 360;
                    }

                    outputLines.add(printedLines.get(i) + ", " + (MAX_VEL - LINEAR_SLOW_ON_TURN * Math.abs(headingError)) + ", " + (headingError * ANGULAR_SPEED_ON_TURN));
                }
            }

            for (String line : outputLines) {
                myWriter.write(line);
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
