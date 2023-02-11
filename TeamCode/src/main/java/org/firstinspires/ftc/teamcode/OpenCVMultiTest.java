package org.firstinspires.ftc.teamcode;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;
import org.openftc.easyopencv.OpenCvPipeline;

public class OpenCVMultiTest extends OpenCvPipeline {
    /*
    YELLOW  = Parking Left
    CYAN    = Parking Middle
    MAGENTA = Parking Right
     */
    public enum ParkingPosition {
        LEFT,
        CENTER,
        RIGHT
    }

    // TOPLEFT anchor point for the bounding box
    public Point[] SLEEVE_TOPLEFT_ANCHOR_POINT = {new Point(20, 70), new Point(20, 70), new Point(20, 70)};

    // Width and height for the bounding box
    public int[] REGION_WIDTH = {60, 60, 60};
    public int[] REGION_HEIGHT = {40, 40, 40};

    public double[] rgb = {-1, -1, -1};
    public String sleeveColor;
    public String[] color = {"Red", "Red", "Red"};
    // Color definitions
    private final Scalar
            YELLOW = new Scalar(255, 255, 0),
            CYAN = new Scalar(0, 255, 255),
            MAGENTA = new Scalar(255, 0, 255),
            RED = new Scalar(255, 0, 0),
            BLUE = new Scalar(0, 0, 255),
            GREEN = new Scalar(0, 255, 0);
    // Running variable storing the parking position
    private volatile SleeveDetection.ParkingPosition position = SleeveDetection.ParkingPosition.LEFT;

    @Override
    public Mat processFrame(Mat input) {
        Point[] sleeve_pointA = new Point[3];
        Point[] sleeve_pointB = new Point[3];
        Mat areaMat;
        Scalar sumColors;
        double minColor;
        double minColor2;
        for (int t = 0; t < 3;) {
            sleeve_pointA[t] = new Point(
                    SLEEVE_TOPLEFT_ANCHOR_POINT[t].x,
                    SLEEVE_TOPLEFT_ANCHOR_POINT[t].y);
            sleeve_pointB[t] = new Point(
                    SLEEVE_TOPLEFT_ANCHOR_POINT[t].x + REGION_WIDTH[t],
                    SLEEVE_TOPLEFT_ANCHOR_POINT[t].y + REGION_HEIGHT[t]);
            t++;
        }
        for (int i = 0; i < 3;) {
            // Get the submat frame, and then sum all the values
            areaMat = input.submat(new Rect(sleeve_pointA[i], sleeve_pointB[i]));
            sumColors = Core.sumElems(areaMat);
            // Get the minimum RGB value from every single channel
            minColor = Math.min(sumColors.val[0], Math.min(sumColors.val[1], sumColors.val[2]));
            rgb[0] = sumColors.val[0] / (REGION_HEIGHT[i] * REGION_WIDTH[i]);
            rgb[1] = sumColors.val[1] / (REGION_HEIGHT[i] * REGION_WIDTH[i]);
            rgb[2] = sumColors.val[2] / (REGION_HEIGHT[i] * REGION_WIDTH[i]);
            if (sumColors.val[0] == minColor) {
                position = SleeveDetection.ParkingPosition.CENTER;
                sleeveColor = "Cyan";
                minColor2 = Math.min(rgb[1], rgb[2]);
                if ((Math.abs(rgb[0] - minColor2) > 50)) {
                    color[i] = "Cyan ";
                    Imgproc.rectangle(
                            input,
                            sleeve_pointA[i],
                            sleeve_pointB[i],
                            CYAN,
                            2
                    );
                } else if (rgb[1] == minColor2) {
                    color[i] = "Blue";
                    Imgproc.rectangle(
                            input,
                            sleeve_pointA[i],
                            sleeve_pointB[i],
                            BLUE,
                            2
                    );
                }
                else {
                    color[i] = "Green";
                    Imgproc.rectangle(
                            input,
                            sleeve_pointA[i],
                            sleeve_pointB[i],
                            GREEN,
                            2
                    );
                }
            } else if (sumColors.val[1] == minColor) {
                position = SleeveDetection.ParkingPosition.RIGHT;
                sleeveColor = "Magenta";
                minColor2 = Math.min(rgb[0], rgb[2]);
                if ((Math.abs(rgb[1] - minColor2) > 50)) {
                    color[i] = "Purple";
                    Imgproc.rectangle(
                            input,
                            sleeve_pointA[i],
                            sleeve_pointB[i],
                            MAGENTA,
                            2
                    );
                } else if (rgb[0] == minColor2) {
                    color[i] = "Blue";
                    Imgproc.rectangle(
                            input,
                            sleeve_pointA[i],
                            sleeve_pointB[i],
                            BLUE,
                            2
                    );
                }
                else {
                    color[i] = "Red";
                    Imgproc.rectangle(
                            input,
                            sleeve_pointA[i],
                            sleeve_pointB[i],
                            RED,
                            2
                    );
                }
            } else {
                position = SleeveDetection.ParkingPosition.LEFT;
                sleeveColor = "Yellow";
                minColor2 = Math.min(rgb[0], rgb[1]);
                if ((Math.abs(rgb[2] - minColor2) > 50)) {
                    color[i] = "Yellow";
                    Imgproc.rectangle(
                            input,
                            sleeve_pointA[i],
                            sleeve_pointB[i],
                            YELLOW,
                            2
                    );
                } else if (rgb[0] == minColor2) {
                    color[i] = "Green";
                    Imgproc.rectangle(
                            input,
                            sleeve_pointA[i],
                            sleeve_pointB[i],
                            GREEN,
                            2
                    );
                }
                else {
                    color[i] = "Red";
                    Imgproc.rectangle(
                            input,
                            sleeve_pointA[i],
                            sleeve_pointB[i],
                            RED,
                            2
                    );
                }
            }
            // Release and return input
            areaMat.release();
            i++;
        } return input;
    }

    // Returns an enum being the current position where the robot will park
    public double[] getRGB() {
        return rgb;
    }
    public String getSleeveColor() {
        return sleeveColor;
    }
    public String getColor(int boxNum) {
        return color[boxNum];
    }
    public void newBoxes(double x, double y, int width, int height, double x1, double y1, int width1, int height1, double x2, double y2, int width2, int height2) {
        SLEEVE_TOPLEFT_ANCHOR_POINT[0] = new Point(x, y);
        REGION_WIDTH[0] = width;
        REGION_HEIGHT[0] = height;
        SLEEVE_TOPLEFT_ANCHOR_POINT[1] = new Point(x1, y1);
        REGION_WIDTH[1] = width1;
        REGION_HEIGHT[1] = height1;
        SLEEVE_TOPLEFT_ANCHOR_POINT[2] = new Point(x2, y2);
        REGION_WIDTH[2] = width2;
        REGION_HEIGHT[2] = height2;
    }
}
