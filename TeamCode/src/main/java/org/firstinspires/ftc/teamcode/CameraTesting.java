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
    double x = 40;
    double y = 10;
    int width = 60;
    int height = 30;
    double LX = 0;
    double LY = 0;


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
        OpenCV.newBox(x, y, width, height);
        waitForStart();
        while (opModeIsActive()) {
            LX = gamepad1.left_stick_x / 50;
            LY = gamepad1.left_stick_y / 50;
            if (((x + width) + LX) <= 240) {
                if ((x + LX) >= 0) {
                    x += LX;
                }
            }
            if (((y + height) + LY) <= 320) {
                if ((y + LY) >= 0) {
                    y += LY;
                }
            }
            OpenCV.newBox(x, y, width, height);
            rgb = OpenCV.getRGB();
            tel();
        }
    }
    public void tel() {
        telemetry.addData("stick: ", gamepad1.left_stick_x);
        telemetry.addData("X: ", x);
        telemetry.addData("Y: ", y);
        telemetry.addData("W: ", width);
        telemetry.addData("H: ", height);
        telemetry.addData("Red: ", rgb[0]);
        telemetry.addData("Green: ", rgb[1]);
        telemetry.addData("Blue", rgb[2]);
        telemetry.addData("Color: ", OpenCV.getColor());
        telemetry.addData("Sleeve Color: ", OpenCV.getSleeveColor());
        telemetry.update();
    }
}
