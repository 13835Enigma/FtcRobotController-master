package org.firstinspires.ftc.teamcode;


import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;
import org.firstinspires.ftc.robotcore.external.navigation.Orientation;
import org.openftc.easyopencv.OpenCvCamera;
import org.openftc.easyopencv.OpenCvCameraFactory;
import org.openftc.easyopencv.OpenCvCameraRotation;

@com.qualcomm.robotcore.eventloop.opmode.Autonomous(name = "AutoAdvanced")
public class AutoAdvanced extends LinearOpMode {
    private OpenCV OpenCV = new OpenCV();
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
    int driveVal;
    int parkingDriveVal;
    public String color;
    public String sideLR = "Left";
    boolean lock = false;
    int mult;
    double targetAngle;
    boolean onTarget;
    double currentAngle;
    double turnValHelper;
    double turnVal;

    public void runOpMode() {
        motorModeAuto driveMode = new motorModeAuto();
        hardWareDecl();
        driveMode.driveDirect(DcMotorSimple.Direction.REVERSE);
        driveMode.zeroPowDriveTrain(DcMotor.ZeroPowerBehavior.BRAKE);
        Lift.setMode(DcMotorEx.RunMode.STOP_AND_RESET_ENCODER);
        Lift.setTargetPosition(0);
        Lift.setMode(DcMotorEx.RunMode.RUN_TO_POSITION);
        Lift.setDirection(DcMotorEx.Direction.REVERSE);
        imuParameters = new BNO055IMU.Parameters();
        imuParameters.angleUnit = BNO055IMU.AngleUnit.DEGREES;
        imuParameters.accelUnit = BNO055IMU.AccelUnit.METERS_PERSEC_PERSEC;
        imuParameters.loggingEnabled = false;
        imu.initialize(imuParameters);
        angles = imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX, AngleUnit.DEGREES);
        int cameraMonitorViewId = hardwareMap.appContext.getResources().getIdentifier("cameraMonitorViewId", "id", hardwareMap.appContext.getPackageName());
        camera = OpenCvCameraFactory.getInstance().createWebcam(hardwareMap.get(WebcamName.class, webcamName), cameraMonitorViewId);
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
        Claw.setPosition(1);
        do {
            if (gamepad1.dpad_left) {
                sideLR = "Left";
                targetAngle = 270;
                mult = 1;
                OpenCV.newBox(111, 174, 72, 53);
                parkingDriveVal = 400 * mult;
            }
            if (gamepad1.dpad_right) {
                sideLR = "Right";
                targetAngle = 90;
                mult = -1;
                OpenCV.newBox(115, 23, 72, 53);
            }
            if (gamepad1.start) lock = true;
            tel();

        } while (lock == false);
        waitForStart();
        color = OpenCV.getSleeveColor();
        driveVal = 580 * mult;
        sleep(300);
        Tilt.setPosition(0.87);
        Lift.setPower(0.4);
        driveMode.driveToPos(-driveVal,driveVal,driveVal,-driveVal, 0.3);
        while (FL.isBusy()) {}
        Lift.setTargetPosition(1300);
        while (Lift.isBusy()) {}
        driveMode.driveToPosAll(230, 0.4);
        while (FL.isBusy()) {}
        Tilt.setPosition(0.47);
        Lift.setTargetPosition(1000);
        while (Lift.isBusy()) {}
        Claw.setPosition(0.68);
        sleep(100);
        Tilt.setPosition(0.87);
        driveMode.driveToPosAll(-260, 0.4);
        while(FL.isBusy()) {}
        driveVal = 400 * mult;
        driveMode.driveToPos(driveVal,-driveVal,-driveVal, driveVal, 0.2);
        while(FL.isBusy()) {}
        driveMode.driveToPosAll(-100, 0.1);
        while(FL.isBusy()) {}
        driveMode.driveToPosAll(2000, 0.2);
        while(FL.isBusy()) {}
        driveMode.driveToPosAll(-300, 0.2);
        while(FL.isBusy()) {}
        Lift.setTargetPosition(350);
        turnToPos(targetAngle);
        Tilt.setPosition(0.47);
        Claw.setPosition(0.68);
        driveMode.driveToPosAll(730, 0.2);
        while(FL.isBusy()) {}
        Claw.setPosition(1);
        sleep(200);
        driveMode.driveToPosAll(-30, 0.2);
        while(FL.isBusy()) {}
        Lift.setTargetPosition(1300);
        while (Lift.isBusy()) {}
        driveMode.driveToPosAll(-445, 0.2);
        while (FL.isBusy()) {}
        turnToPos(0);
        driveMode.driveToPosAll(80, 0.2);
        while (FL.isBusy()) {}
        Lift.setTargetPosition(1000);
        while (Lift.isBusy()) {}
        Claw.setPosition(0.68);
        sleep(100);
        Tilt.setPosition(0.87);
        sleep(100);
        driveMode.driveToPosAll(-170, 0.4);
        while (FL.isBusy()) {}
        Claw.setPosition(1);
        Lift.setTargetPosition(0);
        driveVal = 400 * mult;
        switch (color) {
            case "Cyan": Center();
            break;
            case "Magenta": Right();
            break;
            case "Yellow": Left();
            break;
        }
        while (Lift.isBusy()) {}
        tel();
    }
    public void tel() {
        telemetry.addData("Side: ", sideLR);
        telemetry.addData("Color: ", OpenCV.getSleeveColor());
        telemetry.addData("On Target: ", onTarget);
        telemetry.addData("Angle: ", angles.firstAngle + 180);
        telemetry.addData("TurnValHelper: ", turnValHelper);
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
        driveMode.driveToPos(driveVal,-driveVal,-driveVal, driveVal, 0.2);
        while (FL.isBusy()) {}
    }
    public void Left() {
        motorModeAuto driveMode = new motorModeAuto();
        driveMode.driveToPos(3*driveVal,-3*driveVal,-3*driveVal, 3*driveVal, 0.2);
        while (FL.isBusy()) {}
    }
    public void Right() {
        motorModeAuto driveMode = new motorModeAuto();
        driveMode.driveToPos(-driveVal,driveVal,driveVal, -driveVal, 0.2);
        while (FL.isBusy()) {}
    }
    public void turnToPos(double target) {
        motorModeAuto driveMode = new motorModeAuto();
        onTarget = false;
        driveMode.driveMode(DcMotor.RunMode.RUN_USING_ENCODER);
        while (!onTarget) {
            angles = imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX, AngleUnit.DEGREES);
            currentAngle = angles.firstAngle + 180;
            targetAngle = target;
            tel();
            turnValHelper = targetAngle - currentAngle;
            if (turnValHelper < 0) {
                if (turnValHelper <= -180) {
                    turnValHelper += 360;
                }
            } else if (turnValHelper >= 0) {
                if (turnValHelper >= 180) {
                    turnValHelper -= 360;
                }
            }
            if (Math.abs(turnValHelper) <= 1) turnValHelper = 0;
            if (Math.abs(turnValHelper) >= 70) {
                if (turnValHelper < 0) turnValHelper = -70;
                else if (turnValHelper > 0) turnValHelper = 70;
            }
            turnVal = turnValHelper / 70;
            FR.setVelocity(turnVal * 700);
            BR.setVelocity(turnVal * 700);
            FL.setVelocity(-turnVal * 700);
            BL.setVelocity(-turnVal * 700);
            if (Math.abs(turnValHelper) <= 1) {
                onTarget = true;
                FR.setVelocity(0);
                BR.setVelocity(0);
                FL.setVelocity(0);
                BL.setVelocity(0);
            }
        }
    }
}
