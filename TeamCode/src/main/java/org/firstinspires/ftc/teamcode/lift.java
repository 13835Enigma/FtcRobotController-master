package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotorEx;

@TeleOp(name="Lift", group="Linear Opmode")

public class lift extends LinearOpMode {
    private DcMotorEx Lift;
    private double speed;
    public void runOpMode() {
        Lift = hardwareMap.get(DcMotorEx.class, "Lift");
        Lift.setMode(DcMotorEx.RunMode.STOP_AND_RESET_ENCODER);
        Lift.setMode(DcMotorEx.RunMode.RUN_WITHOUT_ENCODER);
        Lift.setDirection(DcMotorEx.Direction.REVERSE);
        waitForStart();
        while (opModeIsActive()) {
            speed = 5000 * -gamepad1.left_stick_y;
            if (speed > 0 && Lift.getCurrentPosition() >= 3210) {
                speed = 0;
            }
            else if (speed < 0 && Lift.getCurrentPosition() <= 0) {
                speed = 0;
            }
            Lift.setVelocity(speed);
            telemetry.addData("Lift Pos: ", Lift.getCurrentPosition());
            telemetry.update();
        }//build.good.program=true

    }
}
