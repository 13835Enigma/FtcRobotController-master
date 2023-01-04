package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

public class motorMode {
    Driveyboi program = new Driveyboi();
    public void changeDriveTrain(DcMotor.RunMode modes1) {
        program.FR.setMode(modes1);
        program.FL.setMode(modes1);
        program.BR.setMode(modes1);
        program.BL.setMode(modes1);

    }
    public void changeAll(DcMotor.RunMode modes1) {
        program.FR.setMode(modes1);
        program.FL.setMode(modes1);
        program.BR.setMode(modes1);
        program.BL.setMode(modes1);
    }
    public void setDirectionDriveTrain(DcMotorSimple.Direction direct) {

        program.FL.setDirection(direct);
        program.BL.setDirection(direct);
        if (direct.equals(DcMotorSimple.Direction.FORWARD)) {
            program.BR.setDirection(DcMotorSimple.Direction.REVERSE);
            program.FR.setDirection(DcMotorSimple.Direction.REVERSE);
        }
        if (direct.equals(DcMotorSimple.Direction.REVERSE)) {
            program.BR.setDirection(DcMotorSimple.Direction.REVERSE);
            program.FR.setDirection(DcMotorSimple.Direction.REVERSE);
        }
    }
}
