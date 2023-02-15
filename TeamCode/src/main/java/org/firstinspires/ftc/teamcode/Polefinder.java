package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.openftc.easyopencv.OpenCvCamera;
import org.openftc.easyopencv.OpenCvCameraFactory;
import org.openftc.easyopencv.OpenCvCameraRotation;

@com.qualcomm.robotcore.eventloop.opmode.TeleOp(name = "Polefinder")

public class Polefinder extends LinearOpMode {
    private OpenCVMultiTest OpenCV;
    private OpenCvCamera camera;
    private String webcamName = "WebcamMain";
    double[] x = {200, 200, 200};
    double[] y = {46, 27, 100};
    double[] width = {77, 77, 77};
    double[] height = {31, 5, 5};
    boolean inSight = false;
    String middle;
    String left;
    String right;
    String dir;

    public void runOpMode() {
        int cameraMonitorViewId = hardwareMap.appContext.getResources().getIdentifier("cameraMonitorViewId", "id", hardwareMap.appContext.getPackageName());
        camera = OpenCvCameraFactory.getInstance().createWebcam(hardwareMap.get(WebcamName.class, webcamName), cameraMonitorViewId);
        OpenCV = new OpenCVMultiTest();
        camera.setPipeline(OpenCV);
        camera.openCameraDeviceAsync(new OpenCvCamera.AsyncCameraOpenListener() {
            @Override
            public void onOpened() {
                camera.startStreaming(320, 240, OpenCvCameraRotation.UPRIGHT);
            }

            @Override
            public void onError(int errorCode) {
            }
        });
        OpenCV.newBoxes(x[0], y[0], width[0], height[0], x[1], y[1], width[1], height[1], x[2], y[2], width[2], height[2]);
        waitForStart();
        while (opModeIsActive()) {
            middle = OpenCV.getColor(0);
            left = OpenCV.getColor(1);
            right = OpenCV.getColor(2);
            if (middle == "Yellow") inSight = true;
            else inSight = false;
            if (inSight = true) {
                if ((left == "Yellow") && (right != "Yellow")) {
                    dir = "Left";
                } else if ((left != "Yellow") && (right == "Yellow")) {
                    dir = "Right";
                } else if ((left == "Yellow") && (right == "Yellow")) {
                    dir = "Stop";
                } else dir = "Forward";
            }
            tel();
        }
    }
    public Boolean findPole() {
        OpenCV.newBoxes(x[0], y[0], width[0], height[0], x[1], y[1], width[1], height[1], x[2], y[2], width[2], height[2]);
        middle = OpenCV.getColor(0);
        left = OpenCV.getColor(1);
        right = OpenCV.getColor(2);
        if (middle == "Yellow") inSight = true;
        else inSight = false;
        if (inSight = true) {
            if ((left == "Yellow") && (right != "Yellow")) {
                dir = "Left";
            } else if ((left != "Yellow") && (right == "Yellow")) {
                dir = "Right";
            } else if ((left == "Yellow") && (right == "Yellow")) {
                dir = "Stop";
            } else dir = "Forward";
        }
        return inSight;
    }
    public void tel() {
        telemetry.addData("Direction: ", dir);
        telemetry.addData("In Sight: ", inSight);
        telemetry.addData("Color Main: ", OpenCV.getColor(0));
        telemetry.addData("Color Left: ", OpenCV.getColor(1));
        telemetry.addData("Color Right: ", OpenCV.getColor(2));
        telemetry.update();
    }
}
