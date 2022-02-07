/* This file is the auto mode for driving forwards into the warehouse. Essentially it tells the
bot to drive forward for a set amount of milliseconds*/

package org.firstinspires.ftc.teamcode.tjack.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.tjack.AllianceColor;
import org.firstinspires.ftc.teamcode.tjack.CustomLinearOpMode;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class AutoDriveForwards extends CustomLinearOpMode {

    private static final double FORWARD_SPEED = 0.5;

    @Getter
    private final AllianceColor allianceColor;



    @Override
    public void runOpMode() throws InterruptedException {
        super.runOpMode();
        waitForStart();

        getBot().getFrontLeftMotor().setPower(FORWARD_SPEED);
        getBot().getFrontRightMotor().setPower(FORWARD_SPEED);
        getBot().getBackLeftMotor().setPower(FORWARD_SPEED);
        getBot().getBackRightMotor().setPower(FORWARD_SPEED);

        //sets time
        sleep(1300);

        getBot().getFrontLeftMotor().setPower(allianceColor == AllianceColor.RED ? -FORWARD_SPEED : FORWARD_SPEED);
        getBot().getFrontRightMotor().setPower(allianceColor == AllianceColor.RED ? FORWARD_SPEED : -FORWARD_SPEED);
        getBot().getBackLeftMotor().setPower(allianceColor == AllianceColor.RED ? -FORWARD_SPEED : FORWARD_SPEED);
        getBot().getBackRightMotor().setPower(allianceColor == AllianceColor.RED ? FORWARD_SPEED : -FORWARD_SPEED);

        sleep(520);

        getBot().getFrontLeftMotor().setPower(0);
        getBot().getFrontRightMotor().setPower(0);
        getBot().getBackLeftMotor().setPower(0);
        getBot().getBackRightMotor().setPower(0);


    }
}