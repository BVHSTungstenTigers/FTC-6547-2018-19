package org.firstinspires.ftc.teamcode;

import com.qualcomm.hardware.rev.RevBlinkinLedDriver;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.robotcore.external.navigation.Position;
import org.firstinspires.ftc.robotcore.external.navigation.Velocity;

/**
 * Created by Drew from 11874 on 3/24/2019.
 */

@Autonomous(name = "dapat WORLDS V2")
public class RedDepatWorldsV2 extends theColt {

    @Override
    public void runOpMode() {
        telemetry.addData("You broke it Harrison", "");
        telemetry.update();

        INIT(hardwareMap);
        lights.setPattern(RevBlinkinLedDriver.BlinkinPattern.STROBE_RED);

        initIMU();

        initTfod();

        zeroEncoders();

        angleZzeroValue = 45;

        //  hanger.setPower(.7);
        // while (opModeIsActive() && !isLimitSwitchPressed())
        // {
        //     telemetry.addData("limit switch pressed?", isLimitSwitchPressed());
        //     telemetry.addData("limit switch volatage", limitSwitch.getVoltage());
        //     telemetry.update();
        // }
        // hanger.setPower(0);
        // telemetry.log().add("hanger calibrated");
        tfod.activate();
        lights.setPattern(RevBlinkinLedDriver.BlinkinPattern.HEARTBEAT_WHITE);

        while (!isStarted()) //scan until the program starts
        {
            scanMinerals();
        }
        //if the program was started and the gold mineral wasn't detected, default to the GOLD_MINERAL_LEFT position
        if (goldMineralLocation == GOLD_MINERAL_UNKNOWN) {
            goldMineralLocation = GOLD_MINERAL_LEFT;
            telemetry.log().add("gold mineral UNKNOWN, defaulting to LEFT");
        } else telemetry.log().add("gold Mineral location " + goldMineralLocation);

        lights.setPattern(RevBlinkinLedDriver.BlinkinPattern.SKY_BLUE);

        imu.startAccelerationIntegration(new Position(), new Velocity(), 1000); //Start IMU

        outputTelemetry();

        lowerRobot();

        DriveforLength(.5,-.4); //drive away from lander
        
        TurnPID(325,2);
        //TurnPIDandLowerArm(0,1,.7,.5); //turn to face to depot, prepare to deplot team marker
        lowerArm(.95,.95,2.5);
        
        strafeLeft(-.3,600);

        outtake(1); //deploy team marker into depot
        sleep(1.0); //wait one second
        stopIntake(); //stop outtaking
        
        strafeLeft(.3,600);

        turnToFaceMineral(0,325,270,.95,.1,2);
        lowerArm(1,.1,1); //lower to grab mineral

        intake(1); //intake mineral
        sleep(.2); //wait a bit
        turnToFaceMineral(10,315, 280,1,.1,1); //turn a bit, will at least knock the gold mineral off

        //Score in lander

        lowerArm(.4,.5,2); //raise the arm a bit

        stopIntake(); //stop the intake, the mineral is already in there
        
        TurnPID(145,2);

        lowerArm(.5,.95,2); //turn to face lander, raise are to prepare to score

        outtake(.8); //deploy mineral in lander
        sleep(1.0); //wait one second
        stopIntake(); //stop outtaking

        //go to creator
        
        strafeLeft(.3,400);

        DriveFieldRealtiveDistance(.6,45,2);
        //DriveforLength(2,-.6);

        TurnPID(90,2);  //turn to face wall
        
        DriveforLength(1.5,-.6);
        
        strafeLeft(-.6,1000);

        //DriveToPointPID(60,3,2,90); //go to wall

        lowerArm(1,.7,2); //lower arm to grab mineral

        intake(1);

        telemetry.log().add("DONE");
        while (opModeIsActive());


    }
    void turnToFaceMineral(double degreesLEFT, double degreesCENTER, double degreesRIGHT, double armlowerPercent, double armExtendpercent, double time)
    {
        if (goldMineralLocation==GOLD_MINERAL_RIGHT) TurnPID(degreesRIGHT, time);
        else if (goldMineralLocation==GOLD_MINERAL_CENTER) TurnPID(degreesCENTER, time);
        else if (goldMineralLocation==GOLD_MINERAL_LEFT) TurnPID(degreesLEFT, time);
        lowerArm(armlowerPercent, armExtendpercent, time);
    }
}
