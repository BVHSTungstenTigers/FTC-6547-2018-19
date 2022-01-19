package org.firstinspires.ftc.teamcode.tjack.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.tjack.CustomOpMode;

@Autonomous(name="[GAME] Drive Forwards")
public class AutoDucksRed extends CustomOpMode {
    private static final double FORWARD_SPEED = 0.5;
    private static final double WHEEL_SPEED = 0.1;
    @Override
    public void start() {
        super.start();

        //move left towards ducks

        getBot().getFrontLeftMotor().setPower(-FORWARD_SPEED);
        getBot().getFrontRightMotor().setPower(-FORWARD_SPEED);
        getBot().getBackLeftMotor().setPower(FORWARD_SPEED);
        getBot().getBackRightMotor().setPower(FORWARD_SPEED);

        sleep(2000);

        getBot().getFrontLeftMotor().setPower(0);
        getBot().getFrontRightMotor().setPower(0);
        getBot().getBackLeftMotor().setPower(0);
        getBot().getBackRightMotor().setPower(0);

        sleep(200);

        //spin duck

        getBot().getDuckWheelMotor2().setPower(WHEEL_SPEED);

        sleep(5000);

        getBot().getDuckWheelMotor2().setPower(0);

        sleep(200);

        //drive into house

        getBot().getFrontLeftMotor().setPower(-FORWARD_SPEED);
        getBot().getFrontRightMotor().setPower(-FORWARD_SPEED);
        getBot().getBackLeftMotor().setPower(FORWARD_SPEED);
        getBot().getBackRightMotor().setPower(FORWARD_SPEED);

        sleep(500);

        getBot().getFrontLeftMotor().setPower(0);
        getBot().getFrontRightMotor().setPower(0);
        getBot().getBackLeftMotor().setPower(0);
        getBot().getBackRightMotor().setPower(0);

    }
}
