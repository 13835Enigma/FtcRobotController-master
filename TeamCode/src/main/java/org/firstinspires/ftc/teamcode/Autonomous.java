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

@com.qualcomm.robotcore.eventloop.opmode.Autonomous(name = "Autonomous")
public class Autonomous  extends LinearOpMode {
    private SleeveDetection sleeveDetection;
    private OpenCvCamera camera;
    BNO055IMU.Parameters imuParameters;
    Orientation angles;
    public BNO055IMU imu;
    static DcMotorEx Lift;
    static DcMotorEx FR;
    static DcMotorEx FL;
    static DcMotorEx BR;
    static DcMotorEx BL;
    static Servo Tilt;
    static Servo Claw;
    double Lims[] = {0.94, 0.68, 0.87, 0.14};
    double TiltPos = 0.5;
    double ClawPos = 0.67;
    private String webcamName = "WebcamMain";
    int strafeVal = 400;
    private String color;

    public void runOpMode() {
        motorModeAuto driveMode = new motorModeAuto();
        hardWareDecl();
        driveMode.driveDirect(DcMotorSimple.Direction.REVERSE);
        Lift.setMode(DcMotorEx.RunMode.STOP_AND_RESET_ENCODER);
        Lift.setTargetPosition(0);
        Lift.setMode(DcMotorEx.RunMode.RUN_TO_POSITION);
        Lift.setDirection(DcMotorEx.Direction.REVERSE);
        imuParameters = new BNO055IMU.Parameters();
        imuParameters.angleUnit = BNO055IMU.AngleUnit.DEGREES;
        imuParameters.accelUnit = BNO055IMU.AccelUnit.METERS_PERSEC_PERSEC;
        imuParameters.loggingEnabled = false;
        imu.initialize(imuParameters);
        int cameraMonitorViewId = hardwareMap.appContext.getResources().getIdentifier("cameraMonitorViewId", "id", hardwareMap.appContext.getPackageName());
        camera = OpenCvCameraFactory.getInstance().createWebcam(hardwareMap.get(WebcamName.class, webcamName), cameraMonitorViewId);
        sleeveDetection = new SleeveDetection();
        camera.setPipeline(sleeveDetection);
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
        Claw.setPosition(1);
        Tilt.setPosition(0.87);
        Lift.setPower(0.4);
        waitForStart();
        sleep(300);
        color = sleeveDetection.getColor();
        sleep(200);
        driveMode.driveToPos(-strafeVal,strafeVal,strafeVal,-strafeVal, 0.3);
        while (FL.isBusy()) {}
        Lift.setTargetPosition(1500);
        while (Lift.isBusy()) {}
        driveMode.driveToPosAll(270, 0.4);
        while (FL.isBusy()) {}
        Tilt.setPosition(0.5);
        sleep(300);
        Lift.setTargetPosition(1000);
        while (Lift.isBusy()) {}
        Claw.setPosition(0.68);
        sleep(200);
        driveMode.driveToPosAll(-200, 0.4);
        while(FL.isBusy()) {}
        Lift.setTargetPosition(0);
        Claw.setPosition(1);
        switch (color) {
            case "Cyan": Center();
            break;
            case "Magenta": Right();
            break;
            case "Yellow": Left();
            break;
        }
        while (FL.isBusy()) {}
        angles = imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX, AngleUnit.DEGREES);
        tel();
    }
    public void tel() {
        telemetry.addData("Angle: ", angles.firstAngle);
        telemetry.addData("Lift: ", Lift.getCurrentPosition());
        telemetry.addData("Tilt: ", Tilt.getPosition());
        telemetry.addData("Claw: ", Claw.getPosition());
        telemetry.addData("ROTATION: ", sleeveDetection.getPosition());
        telemetry.addData("Color: ", sleeveDetection.getColor());
        double[] rgb = sleeveDetection.getRGB();
        telemetry.addData("Red: ", rgb[0]);
        telemetry.addData("Green: ", rgb[1]);
        telemetry.addData("Blue", rgb[2]);
        telemetry.update();
    }
    public void hardWareDecl() {
        imu = hardwareMap.get(BNO055IMU.class, "imu");
        Lift = hardwareMap.get(DcMotorEx.class, "Lift");
        Tilt = hardwareMap.get(Servo.class, "Tilt");
        Claw = hardwareMap.get(Servo.class, "Claw");
        FR = hardwareMap.get(DcMotorEx.class, "FR");
        FL = hardwareMap.get(DcMotorEx.class, "FL");
        BR = hardwareMap.get(DcMotorEx.class, "BR");
        BL = hardwareMap.get(DcMotorEx.class, "BL");
    }
    public void Center() {
        motorModeAuto driveMode = new motorModeAuto();
        driveMode.driveToPos(strafeVal,-strafeVal,-strafeVal, strafeVal, 0.2);
        while (FL.isBusy()) {}
        driveMode.driveToPosAll(800, 0.3);
    }
    public void Right() {
        motorModeAuto driveMode = new motorModeAuto();
        driveMode.driveToPos(-strafeVal,strafeVal,strafeVal, -strafeVal, 0.2);
        while (FL.isBusy()) {}
        driveMode.driveToPosAll(800, 0.3);
    }
    public void Left() {
        motorModeAuto driveMode = new motorModeAuto();
        driveMode.driveToPos(3*strafeVal,-3*strafeVal,-3*strafeVal, 3*strafeVal, 0.2);
        while (FL.isBusy()) {}
        driveMode.driveToPosAll(800, 0.3);
    }
}
