package org.firstinspires.ftc.teamcode.tjack.opmodes;

import static org.firstinspires.ftc.teamcode.tjack.AllianceColor.RED;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.tjack.AllianceColor;
import org.firstinspires.ftc.teamcode.tjack.CustomLinearOpMode;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public abstract class AutoDucks extends CustomLinearOpMode {
    //is the forward speed var needed...? -joss
    private static final double FORWARD_SPEED = 0.5;
    private static final double WHEEL_SPEED = -0.25;


    @Getter
    private final AllianceColor ALLIANCE_COLOR;

    @Override
    public void runOpMode() throws InterruptedException {
        super.runOpMode();
        waitForStart();
        //move forward from wall

        getBot().getFrontLeftMotor().setPower(1);
        getBot().getFrontRightMotor().setPower(1);
        getBot().getBackLeftMotor().setPower(1);
        getBot().getBackRightMotor().setPower(1);

        //run time for drive forwards (overextends as a reassurance)
        if(ALLIANCE_COLOR == RED){
            sleep(50);
        }
        else {
            //for blue (right side hits wall if else)
            sleep(120);
        }
        //turn towards duck spinner

        getBot().getFrontLeftMotor().setPower((ALLIANCE_COLOR == RED ? -1 : 1) * FORWARD_SPEED);
        getBot().getFrontRightMotor().setPower((ALLIANCE_COLOR == RED ? 1 : -1) * FORWARD_SPEED);
        getBot().getBackLeftMotor().setPower((ALLIANCE_COLOR == RED ? -1 : 1) * FORWARD_SPEED);
        getBot().getBackRightMotor().setPower((ALLIANCE_COLOR == RED ? 1 : -1) * FORWARD_SPEED);

        if(ALLIANCE_COLOR == RED){
            sleep(540);
        }
        else {
            //for blue
            sleep(450);
        }

        //move forward towards ducks

        getBot().getFrontLeftMotor().setPower(1 * FORWARD_SPEED);
        getBot().getFrontRightMotor().setPower(1 * FORWARD_SPEED);
        getBot().getBackLeftMotor().setPower(1 * FORWARD_SPEED);
        getBot().getBackRightMotor().setPower(1 * FORWARD_SPEED);

        //run time for drive forwards (overextends as a reassurance)
        sleep(300);

        //after it hits making it drive slowly to wheel
        getBot().getFrontLeftMotor().setPower(0.30 * FORWARD_SPEED);
        getBot().getFrontRightMotor().setPower(0.30 * FORWARD_SPEED);
        getBot().getBackLeftMotor().setPower(0.30 * FORWARD_SPEED);
        getBot().getBackRightMotor().setPower(0.30 * FORWARD_SPEED);

        //run time for drive forwards extended
        sleep(1900);

        getBot().getFrontLeftMotor().setPower(0);
        getBot().getFrontRightMotor().setPower(0);
        getBot().getBackLeftMotor().setPower(0);
        getBot().getBackRightMotor().setPower(0);

        sleep(200);

        //spin duck

        getBot().getDuckWheelMotor1().setPower((ALLIANCE_COLOR == RED ? 1 : 1) * WHEEL_SPEED);
        getBot().getDuckWheelMotor2().setPower((ALLIANCE_COLOR == RED ? 1 : 1) * WHEEL_SPEED);

        //run time for duck spinner
        sleep(5050);

        getBot().getDuckWheelMotor1().setPower(0);
        getBot().getDuckWheelMotor2().setPower(0);

        sleep(400);


        /*drive into house-
        note: for mecha wheels (side-to-side) two wheels turn outwards the opposite turn inwards*/
        //changed the red code don't know if it will work
        getBot().getFrontLeftMotor().setPower((ALLIANCE_COLOR == RED ? 1 : -1) * FORWARD_SPEED);
        getBot().getFrontRightMotor().setPower((ALLIANCE_COLOR == RED ? -1 : 1) * FORWARD_SPEED);
        getBot().getBackLeftMotor().setPower((ALLIANCE_COLOR == RED ? 1 : -1) * FORWARD_SPEED);
        getBot().getBackRightMotor().setPower((ALLIANCE_COLOR == RED ? -1 : 1) * FORWARD_SPEED);

        //changed the time (so it wouldnt over extend angle)
        if(ALLIANCE_COLOR == RED){
            sleep(425);
        }
        else {
            //for blue
            sleep(900);
        }

        //move forward towards ally parking
        getBot().getFrontLeftMotor().setPower((ALLIANCE_COLOR == RED ? 1 : 1) * FORWARD_SPEED);
        getBot().getFrontRightMotor().setPower((ALLIANCE_COLOR == RED ? 1 : 1) * FORWARD_SPEED);
        getBot().getBackLeftMotor().setPower((ALLIANCE_COLOR == RED ? 1 : 1) * FORWARD_SPEED);
        getBot().getBackRightMotor().setPower((ALLIANCE_COLOR == RED ? 1 : 1) * FORWARD_SPEED);

        //run time for drive forwards (overextends as a reassurance)
        sleep(850);

        getBot().getFrontLeftMotor().setPower(0);
        getBot().getFrontRightMotor().setPower(0);
        getBot().getBackLeftMotor().setPower(0);
        getBot().getBackRightMotor().setPower(0);
    }
}