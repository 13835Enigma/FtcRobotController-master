package org.firstinspires.ftc.teamcode;

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.Range;

import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;
import org.firstinspires.ftc.robotcore.external.navigation.Orientation;
import org.openftc.easyopencv.OpenCvCamera;
import org.openftc.easyopencv.OpenCvCameraFactory;
import org.openftc.easyopencv.OpenCvCameraRotation;


@TeleOp(name="Driveyboi", group="Linear Opmode")

public class Driveyboi extends LinearOpMode {
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
    double Lims[] = {1, 0.68, 0.87, 0.32};
    double TiltPos = 0.45;
    double ClawPos = 0.67;
    private OpenCvCamera camera;
    private OpenCVMultiTest OpenCV;

    private String webcamName = "WebcamMain";
    String dir = "Stop";

    boolean PoleFind = false;
    boolean inSight = false;
    double[] poleMult = {0, 0 };
    Polefinder poleFinder = new Polefinder();

    public void runOpMode() {
        motorMode driveMode = new motorMode();
        motors motors = new motors();
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
        hardWareDecl();
        driveMode.driveMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        driveMode.driveMode(DcMotor.RunMode.RUN_USING_ENCODER);
        driveMode.driveDirect(DcMotorSimple.Direction.FORWARD);
        driveMode.zeroPowDriveTrain(DcMotor.ZeroPowerBehavior.BRAKE);
        Lift.setMode(DcMotorEx.RunMode.STOP_AND_RESET_ENCODER);
        Lift.setMode(DcMotorEx.RunMode.RUN_USING_ENCODER);
        Lift.setDirection(DcMotorEx.Direction.REVERSE);
        Lift.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        imuParameters = new BNO055IMU.Parameters();
        imuParameters.angleUnit = BNO055IMU.AngleUnit.DEGREES;
        imuParameters.accelUnit = BNO055IMU.AccelUnit.METERS_PERSEC_PERSEC;
        imuParameters.loggingEnabled = false;
        imu.initialize(imuParameters);
        telemetry.update();
        waitForStart();
        while (opModeIsActive()) {
            Lift.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
            angles = imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX, AngleUnit.DEGREES);
            motors.setMotors(gamepad1.left_stick_y, gamepad1.left_stick_x, gamepad1.right_stick_x,gamepad1.right_stick_y, gamepad1.left_trigger, gamepad1.right_trigger, gamepad1.left_bumper, gamepad1.right_bumper, poleMult[0], poleMult[1]);
            lift();
            Claw();
            tel();
        }
    }
    public void lift() {
        if ((Lift.getCurrentPosition() > 0) && (Lift.getCurrentPosition() < 3210)) {
            Lift.setVelocity(5000 * (Range.clip(gamepad1.right_trigger - gamepad1.left_trigger, -1.0 ,1.0) + 0.001));
        }
        else if (Lift.getCurrentPosition() <= 0) {
            Lift.setVelocity(5000 * gamepad1.right_trigger);
        }
        else if (Lift.getCurrentPosition() >= 3210) {
            Lift.setVelocity(5000 * gamepad1.left_trigger);
        }
        /*if (gamepad1.dpad_up && (Lift.getCurrentPosition() < 3020)) {
            speed = 150;
            liftSnap = true;
        }
        else if (gamepad1.dpad_up && ((Lift.getCurrentPosition() < 3210) && (Lift.getCurrentPosition() >= 3020))) {
            speed = 30;
            liftSnap = true;
        }
        else if (gamepad1.dpad_down && (Lift.getCurrentPosition() > 190)) {
            speed = -150;
            liftSnap = true;
        }
        else if (gamepad1.dpad_down && ((Lift.getCurrentPosition() > 0) && (Lift.getCurrentPosition() <= 190))) {
            speed = -30;
            liftSnap = true;
        }
        else speed = 0;
        if ((speed != 0) && liftSnap == true) LiftTarget = Lift.getCurrentPosition() + speed;
        if (gamepad1.y) {
            if ((getRuntime() - time) > 0.4) {
                if (liftPos < 3) {
                    liftPos += 1;
                }
                time = getRuntime();
            }
            liftSnap = false;
        }
        else if (gamepad1.a) {
            if ((getRuntime() - time) > 0.3) {
                if (liftPos > 0) {
                    liftPos -= 1;
                }
                time = getRuntime();
            }
            liftSnap = false;
        }
        else if (gamepad1.x) {
            liftPos = 0;
            liftSnap = false;
        }
        if (liftSnap == false) {
            if (liftPos == 0) LiftTarget = 0;
            else if (liftPos == 1) LiftTarget = 1470;
            else if (liftPos == 2) LiftTarget = 2240;
            else if (liftPos == 3) LiftTarget = 3000;
        }
        Lift.setTargetPosition(LiftTarget);
         */
    }
    public void Claw() {
        if (gamepad1.dpad_up && (Tilt.getPosition() < Lims[2])) {
            TiltPos += 0.01;
        }
        else if (gamepad1.dpad_down && (Tilt.getPosition() > Lims[3])) {
            TiltPos -= 0.01;
        }
        else if (gamepad1.b) {
            TiltPos = 0.45;
        }
        else if (gamepad1.y) TiltPos = 0.87;
        if (gamepad1.left_stick_button) {
            ClawPos = 0.68;
        }
        else if (gamepad1.right_stick_button) {
            ClawPos = 1;
        }
        Tilt.setPosition(TiltPos);
        Claw.setPosition(ClawPos);
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
        telemetry.addData("Pole Direct: ", dir);
        telemetry.addData("Pole Finder: ", PoleFind);
        telemetry.addData("Angle: ", angles.firstAngle + 180);
        telemetry.addData("Lift: ", Lift.getCurrentPosition());
        telemetry.addData("Tilt: ", Tilt.getPosition());
        telemetry.addData("Claw: ", Claw.getPosition());
        telemetry.addData("Triggers: ", Range.clip((gamepad1.left_trigger * 5000) - (gamepad1.right_trigger * 5000), -5000.0, 5000.0));
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
}
