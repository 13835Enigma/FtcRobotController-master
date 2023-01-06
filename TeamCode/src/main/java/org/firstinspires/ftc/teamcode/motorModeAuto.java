package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

public class motorModeAuto {
    Autonomous program = new Autonomous();
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
            program.BR.setDirection(DcMotorSimple.Direction.REVERSE);
            program.FR.setDirection(DcMotorSimple.Direction.REVERSE);
        }
        else if (direct.equals(DcMotorSimple.Direction.REVERSE)) {
            program.BR.setDirection(DcMotorSimple.Direction.REVERSE);
            program.FR.setDirection(DcMotorSimple.Direction.REVERSE);
        }
    }
    public void drivePos(int FR, int BR, int FL, int BL) {
        program.FR.setTargetPosition(FR);
        program.BR.setTargetPosition(BR);
        program.FL.setTargetPosition(FL);
        program.BL.setTargetPosition(BL);
    }
    public void drivePow(double Power) {
        program.FR.setPower(Power);
        program.BR.setPower(Power);
        program.FL.setPower(Power);
        program.BL.setPower(Power);
    }
    public void runToPos(int FR, int BR, int FL, int BL, double Power) {
        driveMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        drivePos(FR, BR, FL, BL);
        driveMode(DcMotor.RunMode.RUN_TO_POSITION);
        drivePow(Power);
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

}
