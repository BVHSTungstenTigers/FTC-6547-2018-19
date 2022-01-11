package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;

@Autonomous(name="Spin The Damn Motor Please!")
public class SpinTheDamnMotorPls extends LinearOpMode {
    private DcMotor test_motor;

    @Override
    public void runOpMode() {
        test_motor = hardwareMap.get(DcMotor.class, "motor1");
        telemetry.addData("Status", "Initialized");
        telemetry.update();

        waitForStart();

        // run until the end of the match (driver presses STOP)
        test_motor.getController().setMotorPower(3, .5);
        while (opModeIsActive()) {
            telemetry.addData("Status", "Running");
            telemetry.update();

        }
    }

}
