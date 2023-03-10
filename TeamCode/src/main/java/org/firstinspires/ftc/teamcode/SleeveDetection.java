package org.firstinspires.ftc.teamcode;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;
import org.openftc.easyopencv.OpenCvPipeline;

public class SleeveDetection extends OpenCvPipeline {
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
    public Point SLEEVE_TOPLEFT_ANCHOR_POINT = new Point(120, 94);

    // Width and height for the bounding box
    public int REGION_WIDTH = 65;
    public int REGION_HEIGHT = 50;

    public double[] rgb = {-1, -1, -1};
    public String color;
    // Color definitions
    private final Scalar
            YELLOW = new Scalar(255, 255, 0),
            CYAN = new Scalar(0, 255, 255),
            MAGENTA = new Scalar(255, 0, 255);

    // Anchor point definitions
    Point sleeve_pointA = new Point(
            SLEEVE_TOPLEFT_ANCHOR_POINT.x,
            SLEEVE_TOPLEFT_ANCHOR_POINT.y);
    Point sleeve_pointB = new Point(
            SLEEVE_TOPLEFT_ANCHOR_POINT.x + REGION_WIDTH,
            SLEEVE_TOPLEFT_ANCHOR_POINT.y + REGION_HEIGHT);

    // Running variable storing the parking position
    private volatile ParkingPosition position = ParkingPosition.LEFT;

    @Override
    public Mat processFrame(Mat input) {
        // Get the submat frame, and then sum all the values
        Mat areaMat = input.submat(new Rect(sleeve_pointA, sleeve_pointB));
        Scalar sumColors = Core.sumElems(areaMat);

        // Get the minimum RGB value from every single channel
        double minColor = Math.min(sumColors.val[0], Math.min(sumColors.val[1], sumColors.val[2]));
        rgb[0] = sumColors.val[0] / (REGION_HEIGHT * REGION_WIDTH);
        rgb[1] = sumColors.val[1] / (REGION_HEIGHT * REGION_WIDTH);
        rgb[2] = sumColors.val[2] / (REGION_HEIGHT * REGION_WIDTH);

        // Change the bounding box color based on the sleeve color
        if (sumColors.val[0] == minColor) {
            position = ParkingPosition.CENTER;
            color = "Cyan";
            Imgproc.rectangle(
                    input,
                    sleeve_pointA,
                    sleeve_pointB,
                    CYAN,
                    2
            );
        } else if (sumColors.val[1] == minColor) {
            position = ParkingPosition.RIGHT;
            color = "Magenta";
            Imgproc.rectangle(
                    input,
                    sleeve_pointA,
                    sleeve_pointB,
                    MAGENTA,
                    2
            );
        } else {
            position = ParkingPosition.LEFT;
            color = "Yellow";
            Imgproc.rectangle(
                    input,
                    sleeve_pointA,
                    sleeve_pointB,
                    YELLOW,
                    2
            );
        }

        // Release and return input
        areaMat.release();
        return input;
    }

    // Returns an enum being the current position where the robot will park
    public ParkingPosition getPosition() {
        return position;
    }

    public double[] getRGB() {
        return rgb;
    }

    public String getColor() {
        return color;
    }
}
