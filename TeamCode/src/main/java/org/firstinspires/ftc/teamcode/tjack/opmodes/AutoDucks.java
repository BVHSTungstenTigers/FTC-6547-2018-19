package org.firstinspires.ftc.teamcode.tjack.opmodes;

import static org.firstinspires.ftc.teamcode.tjack.AllianceColor.RED;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.tjack.AllianceColor;
import org.firstinspires.ftc.teamcode.tjack.CustomOpMode;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public abstract class AutoDucks extends CustomOpMode {
    private static final double FORWARD_SPEED = 0.5;
    private static final double WHEEL_SPEED = -0.1;

    @Getter
    private final AllianceColor ALLIANCE_COLOR;

    @Override
    public void start() {
        super.start();

        //move left towards ducks

        getBot().getFrontLeftMotor().setPower((ALLIANCE_COLOR == RED ? -1 : 1) * FORWARD_SPEED);
        getBot().getFrontRightMotor().setPower((ALLIANCE_COLOR == RED ? -1 : 1) * FORWARD_SPEED);
        getBot().getBackLeftMotor().setPower((ALLIANCE_COLOR == RED ? 1 : -1) * FORWARD_SPEED);
        getBot().getBackRightMotor().setPower((ALLIANCE_COLOR == RED ? 1 : -1) * FORWARD_SPEED);

        sleep(2000);

        getBot().getFrontLeftMotor().setPower(0);
        getBot().getFrontRightMotor().setPower(0);
        getBot().getBackLeftMotor().setPower(0);
        getBot().getBackRightMotor().setPower(0);

        sleep(200);

        //spin duck

        getBot().getDuckWheelMotor1().setPower((ALLIANCE_COLOR == RED ? 1 : -1) * WHEEL_SPEED);

        sleep(5000);

        getBot().getDuckWheelMotor1().setPower(0);

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
