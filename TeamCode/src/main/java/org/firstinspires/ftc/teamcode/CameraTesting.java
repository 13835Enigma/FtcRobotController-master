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

@com.qualcomm.robotcore.eventloop.opmode.TeleOp(name = "CameraTesting")
public class CameraTesting extends LinearOpMode {
    private OpenCV OpenCV;
    private OpenCvCamera camera;
    private String webcamName = "WebcamMain";
    double[] rgb;
    double[] x = {40, 90, 140};
    double[] y = {10, 25, 40};
    int[] width = {60, 60, 60};
    int[] height = {30, 10, 10};
    double[] LX = {0, 0, 0};
    double[] LY = {0, 0, 0};
    String box = "Main";


    public void runOpMode() {
        int cameraMonitorViewId = hardwareMap.appContext.getResources().getIdentifier("cameraMonitorViewId", "id", hardwareMap.appContext.getPackageName());
        camera = OpenCvCameraFactory.getInstance().createWebcam(hardwareMap.get(WebcamName.class, webcamName), cameraMonitorViewId);
        OpenCV = new OpenCV();
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
        OpenCV.newBox(x[0], y[0], width[0], height[0]);
        waitForStart();
        while (opModeIsActive()) {
            if (gamepad1.x) box = "Left";
            else if (gamepad1.b) box = "Right";
            else if (gamepad1.y) box = "Main";
            if (box == "Main") {
                LX[0] = gamepad1.left_stick_x / 50;
                LY[0] = gamepad1.left_stick_y / 50;
                if (((x[0] + width[0]) + LX[0]) <= 240) {
                    if ((x[0] + LX[0]) >= 0) {
                        x[0] += LX[0];
                    }
                }
                if (((y[0] + height[0]) + LY[0]) <= 320) {
                    if ((y[0] + LY[0]) >= 0) {
                        y[0] += LY[0];
                    }
                }
            }
            if (box == "Left") {
                LX[1] = gamepad1.left_stick_x / 50;
                LY[1] = gamepad1.left_stick_y / 50;
                if (((x[1] + width[1]) + LX[1]) <= 240) {
                    if ((x[1] + LX[1]) >= 0) {
                        x[1] += LX[1];
                    }
                }
                if (((y[1] + height[1]) + LY[1]) <= 320) {
                    if ((y[1] + LY[1]) >= 0) {
                        y[1] += LY[1];
                    }
                }
            }
            if (box == "Right") {
                LX[2] = gamepad1.left_stick_x / 50;
                LY[2] = gamepad1.left_stick_y / 50;
                if (((x[2] + width[2]) + LX[2]) <= 240) {
                    if ((x[2] + LX[2]) >= 0) {
                        x[2] += LX[2];
                    }
                }
                if (((y[2] + height[2]) + LY[2]) <= 320) {
                    if ((y[2] + LY[2]) >= 0) {
                        y[2] += LY[2];
                    }
                }
            }
            OpenCV.newBox(x[0], y[0], width[0], height[0]);
            OpenCV.newBox(x[1], y[1], width[1], height[1]);
            OpenCV.newBox(x[2], y[2], width[2], height[2]);
            rgb = OpenCV.getRGB();
            tel();
        }
    }
    public void tel() {
        telemetry.addData("stick: ", gamepad1.left_stick_x);
        telemetry.addData("X Main: ", x[0]);
        telemetry.addData("Y Main: ", y[0]);
        telemetry.addData("X Left: ", x[1]);
        telemetry.addData("Y Left: ", y[1]);
        telemetry.addData("X Right: ", x[2]);
        telemetry.addData("Y Right: ", y[2]);
        telemetry.addData("Red: ", rgb[0]);
        telemetry.addData("Green: ", rgb[1]);
        telemetry.addData("Blue", rgb[2]);
        telemetry.addData("Color: ", OpenCV.getColor(0));
        telemetry.addData("Sleeve Color: ", OpenCV.getSleeveColor());
        telemetry.update();
    }
}
