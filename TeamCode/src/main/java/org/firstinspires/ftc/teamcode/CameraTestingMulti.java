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
    double[] x = {50, 20, 40};
    double[] y = {50, 20, 40};
    int[] width = {60, 60, 60};
    int[] height = {30, 10, 10};
    String box = "Main";


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
                camera.startStreaming(320,240, OpenCvCameraRotation.SIDEWAYS_LEFT);
            }

            @Override
            public void onError(int errorCode) {}
        });
        OpenCV.newBoxes(x[0], y[0], width[0], height[0], x[1], y[1], width[1], height[1], x[2], y[2], width[2], height[2]);
        waitForStart();
        while (opModeIsActive()) {
            LX = -gamepad1.left_stick_y / 50;
            LY = gamepad1.left_stick_x / 50;
            if (gamepad1.x) box = "Left";
            else if (gamepad1.b) box = "Right";
            else if (gamepad1.y) box = "Main";
            if (box == "Main") {
                if (((x[0] + width[0]) + LX) <= 240) {
                    if ((x[0] + LX) >= 0) {
                        x[0] += LX;
                    }
                }
                if (((y[0] + height[0]) + LY) <= 320) {
                    if ((y[0] + LY) >= 0) {
                        y[0] += LY;
                    }
                }
            }
            if (box == "Left") {
                if (((x[1] + width[1]) + LX) <= 240) {
                    if ((x[1] + LX) >= 0) {
                        x[1] += LX;
                    }
                }
                if (((y[1] + height[1]) + LY) <= 320) {
                    if ((y[1] + LY) >= 0) {
                        y[1] += LY;
                    }
                }
            }
            if (box == "Right") {
                if (((x[2] + width[2]) + LX) <= 240) {
                    if ((x[2] + LX) >= 0) {
                        x[2] += LX;
                    }
                }
                if (((y[2] + height[2]) + LY) <= 320) {
                    if ((y[2] + LY) >= 0) {
                        y[2] += LY;
                    }
                }
            }
            OpenCV.newBoxes(x[0], y[0], width[0], height[0], x[1], y[1], width[1], height[1], x[2], y[2], width[2], height[2]);
            rgb = OpenCV.getRGB();
            tel();
        }
    }
    public void tel() {
        telemetry.addData("X Main: ", x[0]);
        telemetry.addData("Y Main: ", y[0]);
        telemetry.addData("X Left: ", x[1]);
        telemetry.addData("Y Left: ", y[1]);
        telemetry.addData("X Right: ", x[2]);
        telemetry.addData("Y Right: ", y[2]);
        telemetry.addData("Red: ", rgb[0]);
        telemetry.addData("Green: ", rgb[1]);
        telemetry.addData("Blue", rgb[2]);
        telemetry.addData("Color Main: ", OpenCV.getColor(0));
        telemetry.addData("Color Left: ", OpenCV.getColor(1));
        telemetry.addData("Color Right: ", OpenCV.getColor(2));
        telemetry.update();
    }
}

