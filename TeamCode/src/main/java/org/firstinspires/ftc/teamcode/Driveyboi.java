package org.firstinspires.ftc.teamcode;

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;
import org.firstinspires.ftc.robotcore.external.navigation.Orientation;


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
    int speed;
    int LiftTarget;
    double strafe;
    double mult;
    double Lims[] = {0.94, 0.68, 0.87, 0.14};
    double TiltPos = 0.5;
    double ClawPos = 0.67;

    public void runOpMode() {
        motorMode driveMode = new motorMode();
        motors motors = new motors();
        hardWareDecl();
        driveMode.changeDriveTrain(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        driveMode.changeDriveTrain(DcMotor.RunMode.RUN_USING_ENCODER);
        driveMode.setDirectionDriveTrain(DcMotorSimple.Direction.FORWARD);
        Lift.setMode(DcMotorEx.RunMode.STOP_AND_RESET_ENCODER);
        Lift.setTargetPosition(0);
        Lift.setMode(DcMotorEx.RunMode.RUN_TO_POSITION);
        Lift.setDirection(DcMotorEx.Direction.REVERSE);
        imuParameters = new BNO055IMU.Parameters();
        imuParameters.angleUnit = BNO055IMU.AngleUnit.DEGREES;
        imuParameters.accelUnit = BNO055IMU.AccelUnit.METERS_PERSEC_PERSEC;
        imuParameters.loggingEnabled = false;
        imu.initialize(imuParameters);
        telemetry.update();
        waitForStart();
        while (opModeIsActive()) {
            Lift.setPower(1);
            angles = imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX, AngleUnit.DEGREES);
            motors.setMotors(gamepad1.left_stick_y, gamepad1.left_stick_x, gamepad1.right_stick_x,gamepad1.right_stick_y, gamepad1.left_trigger, gamepad1.right_trigger, gamepad1.left_stick_button, gamepad1.right_stick_button);
            lift();
            Claw();
            tel();
        }
    }
    public void lift() {
        if (gamepad1.y && (Lift.getCurrentPosition() < 3020)) {
            speed = 190;
        }
        else if (gamepad1.y && ((Lift.getCurrentPosition() < 3210) && (Lift.getCurrentPosition() >= 3020))) {
            speed = 30;
        }
        else if (gamepad1.a && (Lift.getCurrentPosition() > 190)) {
            speed = -190;
        }
        else if (gamepad1.a && ((Lift.getCurrentPosition() > 0) && (Lift.getCurrentPosition() <= 190))) {
            speed = -30;
        }
        else speed = 0;
        if (speed != 0) LiftTarget = Lift.getCurrentPosition() + speed;
        Lift.setTargetPosition(LiftTarget);

    }
    public void Claw() {
        if (gamepad1.dpad_up && (Tilt.getPosition() < Lims[2])) {
            TiltPos += 0.01;
        }
        else if (gamepad1.dpad_down && (Tilt.getPosition() > Lims[3])) {
            TiltPos -= 0.01;
        }
        else if (gamepad1.dpad_left) {
            TiltPos = 0.5;
        }
        if (gamepad1.right_bumper && (Claw.getPosition() < Lims[0])) {
            ClawPos += 0.01;
        }
        else if (gamepad1.left_bumper && (Claw.getPosition() > Lims[1])) {
            ClawPos -= 0.01;
        }
        Tilt.setPosition(TiltPos);
        Claw.setPosition(ClawPos);
    }
    public void tel() {
        telemetry.addData("Angle: ", angles.firstAngle);
        telemetry.addData("Lift: ", Lift.getCurrentPosition());
        telemetry.addData("Tilt: ", Tilt.getPosition());
        telemetry.addData("Claw: ", Claw.getPosition());
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
