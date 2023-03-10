package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.util.Range;

public class motors {
    Driveyboi program = new Driveyboi();
    double Ly, Lx, Rx, Ry, LT, RT;
    double FR, BR, FL, BL;
    double steer;
    double strafe;
    double mult;
    boolean LB, RB;
    public void setMotors(double Ly1, double Lx1, double Rx1, double Ry1, double LT1, double RT1, boolean LB1, boolean RB1, double Lmult, double Rmult) {
        Ly = Ly1;
        Lx = Lx1;
        Rx = Rx1;
        Ry = Ry1;
        LT = LT1;
        RT = RT1;
        LB = LB1;
        RB = RB1;
        //program.forward = Ly;
        //program.strafe = Range.clip(RT - LT, -1.0, 1.0);
        strafe = Lx;
        if (LB) mult = 5;
        else if (RB) mult = 1;
        else mult = 2.5;
        steer = Rx / 1.8;
        //program.left = Range.clip(Ly + program.steer, -1.0,1.0);
        //program.right = Range.clip(Ly - program.steer, -1.0, 1.0);
        FR = Range.clip(Ly + strafe, -1.0, 1.0);
        BR = Range.clip(Ly - strafe, -1.0, 1.0);
        FL = Range.clip(Ly - strafe, -1.0, 1.0);
        BL = Range.clip(Ly + strafe, -1.0, 1.0);
        program.FR.setVelocity(((Range.clip(FR + steer, -1.0, 1.0) + Rmult) * 5000) / mult);
        program.BR.setVelocity(((Range.clip(BR + steer, -1.0, 1.0) + Rmult) * 5000) / mult);
        program.FL.setVelocity(((Range.clip(FL - steer, -1.0, 1.0) + Lmult) * 5000) / mult);
        program.BL.setVelocity(((Range.clip(BL - steer, -1.0, 1.0) + Lmult) * 5000) / mult);
    }
}
