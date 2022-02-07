/*This file is the autonomous mode made for spinning ducks and then parking in the alliance parking*/
package org.firstinspires.ftc.teamcode.tjack.opmodes;

import static org.firstinspires.ftc.teamcode.tjack.AllianceColor.RED;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.tjack.AllianceColor;
import org.firstinspires.ftc.teamcode.tjack.CustomLinearOpMode;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public abstract class AutoDucks extends CustomLinearOpMode {
    private static final double FORWARD_SPEED = 0.5;
    private static final double WHEEL_SPEED = -0.25;


    @Getter
    private final AllianceColor ALLIANCE_COLOR;

    @Override
    public void runOpMode() throws InterruptedException {
        super.runOpMode();
        waitForStart();
        //move forward from wall (back of bot is againt wall)

        getBot().getFrontLeftMotor().setPower(1);
        getBot().getFrontRightMotor().setPower(1);
        getBot().getBackLeftMotor().setPower(1);
        getBot().getBackRightMotor().setPower(1);

        //run time for drive forwards (overextends as a reassurance)
            //for blue- extends more (right side of bot hits wall if else)
        sleep(ALLIANCE_COLOR == RED ? 50 : 75);

        //turn towards duck spinner
        getBot().getFrontLeftMotor().setPower((ALLIANCE_COLOR == RED ? -1 : 1) * FORWARD_SPEED);
        getBot().getFrontRightMotor().setPower((ALLIANCE_COLOR == RED ? 1 : -1) * FORWARD_SPEED);
        getBot().getBackLeftMotor().setPower((ALLIANCE_COLOR == RED ? -1 : 1) * FORWARD_SPEED);
        getBot().getBackRightMotor().setPower((ALLIANCE_COLOR == RED ? 1 : -1) * FORWARD_SPEED);

        //run time for turn
        sleep(ALLIANCE_COLOR == RED ? 575 : 485);

        //move forward towards ducks
        getBot().getFrontLeftMotor().setPower(1 * FORWARD_SPEED);
        getBot().getFrontRightMotor().setPower(1 * FORWARD_SPEED);
        getBot().getBackLeftMotor().setPower(1 * FORWARD_SPEED);
        getBot().getBackRightMotor().setPower(1 * FORWARD_SPEED);

        //run time for drive forwards (overextends as a reassurance)
        sleep(ALLIANCE_COLOR == RED ? 350 : 330);

        //after it hits making it drive slowly to wheel
        getBot().getFrontLeftMotor().setPower(0.30 * FORWARD_SPEED);
        getBot().getFrontRightMotor().setPower(0.30 * FORWARD_SPEED);
        getBot().getBackLeftMotor().setPower(0.30 * FORWARD_SPEED);
        getBot().getBackRightMotor().setPower(0.30 * FORWARD_SPEED);

        //run time for drive forwards extended
        sleep(2000);

        getBot().getFrontLeftMotor().setPower(0);
        getBot().getFrontRightMotor().setPower(0);
        getBot().getBackLeftMotor().setPower(0);
        getBot().getBackRightMotor().setPower(0);

        sleep(200);

        //spin duck
        getBot().getDuckWheelMotor1().setPower((ALLIANCE_COLOR == RED ? 1 : -1) * WHEEL_SPEED);
        getBot().getDuckWheelMotor2().setPower((ALLIANCE_COLOR == RED ? 1 : -1) * WHEEL_SPEED);

        //run time for duck spinner
        sleep(5050);

        getBot().getDuckWheelMotor1().setPower(0);
        getBot().getDuckWheelMotor2().setPower(0);

        sleep(400);


        /*drive into house-
        note: for mecha wheels (side-to-side) two wheels turn outwards the opposite turn inwards*/
        getBot().getFrontLeftMotor().setPower((ALLIANCE_COLOR == RED ? 1 : -1) * FORWARD_SPEED);
        getBot().getFrontRightMotor().setPower((ALLIANCE_COLOR == RED ? -1 : 1) * FORWARD_SPEED);
        getBot().getBackLeftMotor().setPower((ALLIANCE_COLOR == RED ? 1 : -1) * FORWARD_SPEED);
        getBot().getBackRightMotor().setPower((ALLIANCE_COLOR == RED ? -1 : 1) * FORWARD_SPEED);

        //changed the time (so it wouldnt over extend angle)
        sleep(ALLIANCE_COLOR == RED ? 425 : 520);

        //move forward towards ally parking
        getBot().getFrontLeftMotor().setPower((ALLIANCE_COLOR == RED ? 1 : 1) * FORWARD_SPEED);
        getBot().getFrontRightMotor().setPower((ALLIANCE_COLOR == RED ? 1 : 1) * FORWARD_SPEED);
        getBot().getBackLeftMotor().setPower((ALLIANCE_COLOR == RED ? 1 : 1) * FORWARD_SPEED);
        getBot().getBackRightMotor().setPower((ALLIANCE_COLOR == RED ? 1 : 1) * FORWARD_SPEED);

        //run time for drive forwards (overextends as a reassurance)
        sleep(ALLIANCE_COLOR == RED ? 900 : 850);

        getBot().getFrontLeftMotor().setPower(0);
        getBot().getFrontRightMotor().setPower(0);
        getBot().getBackLeftMotor().setPower(0);
        getBot().getBackRightMotor().setPower(0);

        sleep(ALLIANCE_COLOR == RED ? 1000 : 1000);

        getBot().getFrontLeftMotor().setPower((ALLIANCE_COLOR == RED ? -1 : -1) * FORWARD_SPEED);
        getBot().getFrontRightMotor().setPower((ALLIANCE_COLOR == RED ? -1 : -1) * FORWARD_SPEED);
        getBot().getBackLeftMotor().setPower((ALLIANCE_COLOR == RED ? -1 : -1) * FORWARD_SPEED);
        getBot().getBackRightMotor().setPower((ALLIANCE_COLOR == RED ? -1 : -1) * FORWARD_SPEED);

        sleep(ALLIANCE_COLOR == RED ? 50 : 50);

        getBot().getFrontLeftMotor().setPower(0);
        getBot().getFrontRightMotor().setPower(0);
        getBot().getBackLeftMotor().setPower(0);
        getBot().getBackRightMotor().setPower(0);


    }
}