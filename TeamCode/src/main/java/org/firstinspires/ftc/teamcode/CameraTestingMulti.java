package org.firstinspires.ftc.teamcode;


import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.robotcore.external.hardware.camera.Camera;
import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;
import org.firstinspires.ftc.robotcore.external.navigation.Orientation;
import org.openftc.easyopencv.OpenCvCamera;
import org.openftc.easyopencv.OpenCvCameraFactory;
import org.openftc.easyopencv.OpenCvCameraRotation;

@com.qualcomm.robotcore.eventloop.opmode.TeleOp(name = "CameraTestingMulti")
public class CameraTestingMulti extends LinearOpMode {
    private OpenCVMultiTest OpenCV;
    private OpenCvCamera camera;
    private String webcamName = "WebcamMain";
    double[] rgb;
    double LX = 0;
    double LY = 0;
    double RX = 0;
    double RY = 0;
    String dir = "Stop";
    double[] x = {200, 200, 200};
    double[] y = {46, 27, 100};
    double[] width = {77, 77, 77};
    double[] height = {31, 5, 5};
    String box = "Main";
    boolean PoleFind = false;
    boolean inSight = false;
    double[] poleMult = {0, 0 };
    Polefinder poleFinder = new Polefinder();


    public void runOpMode() {
        int cameraMonitorViewId = hardwareMap.appContext.getResources().getIdentifier("cameraMonitorViewId", "id", hardwareMap.appContext.getPackageName());
        camera = OpenCvCameraFactory.getInstance().createWebcam(hardwareMap.get(WebcamName.class, webcamName), cameraMonitorViewId);
        OpenCV = new OpenCVMultiTest();
        camera.setPipeline(OpenCV);
        camera.openCameraDeviceAsync(new OpenCvCamera.AsyncCameraOpenListener()
        {
            @Override
            public void onOpened()
            {
                camera.startStreaming(320,240, OpenCvCameraRotation.UPRIGHT);
            }

            @Override
            public void onError(int errorCode) {}
        });
        OpenCV.newBoxes(x[0], y[0], width[0], height[0], x[1], y[1], width[1], height[1], x[2], y[2], width[2], height[2]);
        waitForStart();
        while (opModeIsActive()) {
            LX = -gamepad1.left_stick_y / 50;
            LY = gamepad1.left_stick_x / 50;
            RX = -gamepad1.right_stick_y / 150;
            RY = gamepad1.right_stick_x / 150;
            if (gamepad1.x) box = "Left";
            else if (gamepad1.b) box = "Right";
            else if (gamepad1.y) box = "Main";
            if (box == "Main") {
                if (((x[0] + width[0]) + LX) <= 320) {
                    if ((x[0] + LX) >= 0) {
                        x[0] += LX;
                    }
                    if ((width[0] + RX) >= 0) {
                        width[0] += RX;
                    }
                }
                if (((y[0] + height[0]) + LY) <= 240) {
                    if ((y[0] + LY) >= 0) {
                        y[0] += LY;
                    }
                    if ((height[0] + RY) >= 0) {
                        height[0] += RY;
                    }
                }
            }
            if (box == "Left") {
                if (((x[1] + width[1]) + LX + RX) <= 240) {
                    if ((x[1] + LX) >= 0) {
                        x[1] += LX;
                    }
                    if ((width[1] + RX) >= 0) {
                        width[1] += RX;
                    }
                }
                if (((y[1] + height[1]) + LY) <= 320) {
                    if ((y[1] + LY) >= 0) {
                        y[1] += LY;
                    }
                    if ((height[1] + RY) >= 0) {
                        height[1] += RY;
                    }
                }
            }
            if (box == "Right") {
                if (((x[2] + width[2]) + LX) <= 240) {
                    if ((x[2] + LX) >= 0) {
                        x[2] += LX;
                    }
                    if ((width[2] + RX) >= 0) {
                        width[2] += RX;
                    }
                }
                if (((y[2] + height[2]) + LY) <= 320) {
                    if ((y[2] + LY) >= 0) {
                        y[2] += LY;
                    }
                    if ((height[2] + RY) >= 0) {
                        height[2] += RY;
                    }
                }
            }
            OpenCV.newBoxes(x[0], y[0], width[0], height[0], x[1], y[1], width[1], height[1], x[2], y[2], width[2], height[2]);
            rgb = OpenCV.getRGB();
            tel();
        }
    }
    public void poleFind() {
        if (gamepad1.a) PoleFind = true;
        else if (gamepad1.a) PoleFind = false;
        if (PoleFind == true) {
            poleFinder.findPole();
            dir = poleFinder.dir;
            inSight = poleFinder.inSight;
            if (dir == "Left") {
                poleMult[0] = -0.2;
                poleMult[1] = 0.2;
            }
            else if (dir == "Right") {
                poleMult[0] = 0.2;
                poleMult[1] = -0.2;
            }
            else if (dir == "Forward") {
                poleMult[0] = 0.2;
                poleMult[1] = 0.2;
            }
            else if (dir == "Stop") {
                poleMult[0] = 0;
                poleMult[1] = 0;
                PoleFind = false;
            }
        }
    }
    public void tel() {
        telemetry.addData("X Main: ", x[0]);
        telemetry.addData("Y Main: ", y[0]);
        telemetry.addData("Width Main: ", width[0]);
        telemetry.addData("Height Main: ", height[0]);
        telemetry.addData("Color Main: ", OpenCV.getColor(0));
        telemetry.addData("X Left: ", x[1]);
        telemetry.addData("Y Left: ", y[1]);
        telemetry.addData("Width Left: ", width[1]);
        telemetry.addData("Height Left: ", height[1]);
        telemetry.addData("Color Left: ", OpenCV.getColor(1));
        telemetry.addData("X Right: ", x[2]);
        telemetry.addData("Y Right: ", y[2]);
        telemetry.addData("Width Main: ", width[2]);
        telemetry.addData("Height Right: ", height[2]);
        telemetry.addData("Color Right: ", OpenCV.getColor(2));
        telemetry.addData("Pole Direct: ", dir);
        telemetry.addData("Pole Finder: ", PoleFind);
        telemetry.update();
    }
}

