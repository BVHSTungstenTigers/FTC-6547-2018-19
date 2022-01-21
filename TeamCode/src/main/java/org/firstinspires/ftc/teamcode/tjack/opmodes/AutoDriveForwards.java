package org.firstinspires.ftc.teamcode.tjack.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.tjack.CustomLinearOpMode;

@Autonomous(name="[GAME] Drive Forwards")
public class AutoDriveForwards extends CustomLinearOpMode {

    private static final double FORWARD_SPEED = 0.5;

    @Override
    public void runOpMode() throws InterruptedException {
        super.start();

        getBot().getFrontLeftMotor().setPower(FORWARD_SPEED);
        getBot().getFrontRightMotor().setPower(FORWARD_SPEED);
        getBot().getBackLeftMotor().setPower(FORWARD_SPEED);
        getBot().getBackRightMotor().setPower(FORWARD_SPEED);

        //sets time
        sleep(1300);

        getBot().getFrontLeftMotor().setPower(0);
        getBot().getFrontRightMotor().setPower(0);
        getBot().getBackLeftMotor().setPower(0);
        getBot().getBackRightMotor().setPower(0);
    }
}