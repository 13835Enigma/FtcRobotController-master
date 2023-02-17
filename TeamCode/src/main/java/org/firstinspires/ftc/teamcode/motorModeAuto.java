package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

public class motorModeAuto {
    AutoAdvanced program = new AutoAdvanced();
    public void driveMode(DcMotor.RunMode modes1) {
        program.FR.setMode(modes1);
        program.FL.setMode(modes1);
        program.BR.setMode(modes1);
        program.BL.setMode(modes1);

    }
    public void allMode(DcMotor.RunMode modes1) {
        program.FR.setMode(modes1);
        program.FL.setMode(modes1);
        program.BR.setMode(modes1);
        program.BL.setMode(modes1);
    }
    public void driveDirect(DcMotorSimple.Direction direct) {
        program.FL.setDirection(direct);
        program.BL.setDirection(direct);
        if (direct.equals(DcMotorSimple.Direction.FORWARD)) {
            program.FR.setDirection(DcMotorSimple.Direction.REVERSE);
            program.BR.setDirection(DcMotorSimple.Direction.REVERSE);
        }
        else if (direct.equals(DcMotorSimple.Direction.REVERSE)) {
            program.FR.setDirection(DcMotorSimple.Direction.FORWARD);
            program.BR.setDirection(DcMotorSimple.Direction.FORWARD);
        }
    }
    public void drivePos(int FR, int BR, int FL, int BL) {
        program.FR.setTargetPosition(FR);
        program.BR.setTargetPosition(BR);
        program.FL.setTargetPosition(FL);
        program.BL.setTargetPosition(BL);
    }
    public void drivePosAll(int Pos) {
        program.FR.setTargetPosition(Pos);
        program.BR.setTargetPosition(Pos);
        program.FL.setTargetPosition(Pos);
        program.BL.setTargetPosition(Pos);
    }
    public void drivePow(double FR, double BR, double FL, double BL) {
        program.FR.setPower(FR);
        program.BR.setPower(BR);
        program.FL.setPower(FL);
        program.BL.setPower(BL);
    }
    public void drivePowAll(double Power) {
        program.FR.setPower(Power);
        program.BR.setPower(Power);
        program.FL.setPower(Power);
        program.BL.setPower(Power);
    }
    public void driveToPos(int FR, int BR, int FL, int BL, double Power) {
        driveMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        drivePos(FR, BR, FL, BL);
        driveMode(DcMotor.RunMode.RUN_TO_POSITION);
        drivePowAll(Power);
    }
    public void driveToPosAll(int Pos, double Power) {
        driveMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        drivePosAll(Pos);
        driveMode(DcMotor.RunMode.RUN_TO_POSITION);
        drivePowAll(Power);
    }
    public void driveVelo(int FR, int BR, int FL, int BL) {
        program.FR.setVelocity(FR);
        program.BR.setVelocity(BR);
        program.FL.setVelocity(FL);
        program.BL.setVelocity(BL);
    }
    public void driveVeloAll(int Velo) {
        driveVelo(Velo, Velo, Velo, Velo);
    }
    public void zeroPowDriveTrain(DcMotor.ZeroPowerBehavior zeropow) {
        program.FR.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        program.FL.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        program.BR.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        program.BL.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

    }
}
