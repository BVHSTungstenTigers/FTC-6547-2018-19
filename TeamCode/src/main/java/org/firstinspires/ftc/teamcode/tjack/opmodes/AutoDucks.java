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
    private static final double WHEEL_SPEED = -0.25;

    @Getter
    private final AllianceColor ALLIANCE_COLOR;

    @Override
    public void start() {
        super.start();

        //move forward towards ducks

        getBot().getFrontLeftMotor().setPower((ALLIANCE_COLOR == RED ? -1 : 1) * FORWARD_SPEED);
        getBot().getFrontRightMotor().setPower((ALLIANCE_COLOR == RED ? -1 : 1) * FORWARD_SPEED);

        /*trying to see if this will make it move forward
        getBot().getBackLeftMotor().setPower((ALLIANCE_COLOR == RED ? 1 : -1) * FORWARD_SPEED);
        getBot().getBackRightMotor().setPower((ALLIANCE_COLOR == RED ? 1 : -1) * FORWARD_SPEED);
        */

        getBot().getBackLeftMotor().setPower((ALLIANCE_COLOR == RED ? -1 : 1) * FORWARD_SPEED);
        getBot().getBackRightMotor().setPower((ALLIANCE_COLOR == RED ? -1 : 1) * FORWARD_SPEED);

        //run time
        sleep(720);

        getBot().getFrontLeftMotor().setPower(0);
        getBot().getFrontRightMotor().setPower(0);
        getBot().getBackLeftMotor().setPower(0);
        getBot().getBackRightMotor().setPower(0);

        sleep(200);

        //spin duck

        getBot().getDuckWheelMotor1().setPower((ALLIANCE_COLOR == RED ? -1 : 1) * WHEEL_SPEED);

        sleep(5050);

        getBot().getDuckWheelMotor1().setPower(0);
        System.out.println("1");
        sleep(200);
        System.out.println("2");
        //test
        getBot().getDuckWheelMotor1().setPower(1);

        sleep(200);

        /*drive into house-
        note: for omni wheels (side-to-side) two wheels turn outwards the opposite turn inwards*/
        getBot().getFrontLeftMotor().setPower((ALLIANCE_COLOR == RED ? 1 : -1) * FORWARD_SPEED);
        getBot().getFrontRightMotor().setPower((ALLIANCE_COLOR == RED ? -1 : 1) * FORWARD_SPEED);
        getBot().getBackLeftMotor().setPower((ALLIANCE_COLOR == RED ? 1 : -1) * FORWARD_SPEED);
        getBot().getBackRightMotor().setPower((ALLIANCE_COLOR == RED ? -1 : 1) * FORWARD_SPEED);

        sleep(500);

        getBot().getFrontLeftMotor().setPower(0);
        getBot().getFrontRightMotor().setPower(0);
        getBot().getBackLeftMotor().setPower(0);
        getBot().getBackRightMotor().setPower(0);
    }
}
