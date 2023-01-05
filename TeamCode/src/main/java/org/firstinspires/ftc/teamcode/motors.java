package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.util.Range;

public class motors {
    Driveyboi program = new Driveyboi();
    double Ly, Lx, Rx, Ry, LT, RT;
    boolean LB, RB;
    public void setMotors(double Ly1, double Lx1, double Rx1, double Ry1, double LT1, double RT1, boolean LB1, boolean RB1) {
        Ly = Ly1;
        Lx = Lx1;
        Rx = Rx1;
        Ry = Ry1;
        LT = LT1;
        RT = RT1;
        LB = LB1;
        RB = RB1;
        //program.forward = Ly;
        program.strafe = Range.clip(RT - LT, -1.0, 1.0);
        if (LB) program.mult = 5;
        else if (RB) program.mult = 1;
        else program.mult = 2.5;
        //program.steer = Rx / 1.8;
        //program.left = Range.clip(Ly + program.steer, -1.0,1.0);
        //program.right = Range.clip(Ly - program.steer, -1.0, 1.0);
        program.FR.setVelocity((Range.clip(Ry + program.strafe, -1.0, 1.0) * 5000) / program.mult);
        program.BR.setVelocity((Range.clip(Ry - program.strafe, -1.0, 1.0) * 5000) / program.mult);
        program.FL.setVelocity((Range.clip(Ly - program.strafe, -1.0, 1.0) * 5000) / program.mult);
        program.BL.setVelocity((Range.clip(Ly + program.strafe, -1.0, 1.0) * 5000) /program.mult);
    }
}
