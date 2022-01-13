package org.firstinspires.ftc.teamcode.wolfram;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.util.ElapsedTime;

@Autonomous(name="Drive By Time")
public class AutoDriveByTime extends CustomOpMode {
    private static final double FORWARD_SPEED = 0.6;
    private static final double TURN_SPEED = 0.5;
    private final ElapsedTime runtime = new ElapsedTime();

    @Override
    public void start() {
        super.start();

        getBot().getFrontLeftMotor().setPower(FORWARD_SPEED);
        getBot().getFrontRightMotor().setPower(FORWARD_SPEED);
        runtime.reset();
        while (runtime.seconds() < 3.0) {
            telemetry.addData("Path", "Leg 1: %2.5f S Elapsed", runtime.seconds());
            telemetry.update();
        }

        getBot().getFrontLeftMotor().setPower(TURN_SPEED);
        getBot().getFrontRightMotor().setPower(-TURN_SPEED);
        runtime.reset();
        while (runtime.seconds() < 1.0) {
            telemetry.addData("Path", "Leg 2: %2.5f S Elapsed", runtime.seconds());
            telemetry.update();
        }

        getBot().getFrontLeftMotor().setPower(-FORWARD_SPEED);
        getBot().getFrontRightMotor().setPower(-FORWARD_SPEED);
        runtime.reset();
        while (runtime.seconds() < 1.0) {
            telemetry.addData("Path", "Leg 1: %2.5f S Elapsed", runtime.seconds());
            telemetry.update();
        }

        getBot().getFrontLeftMotor().setPower(0);
        getBot().getFrontRightMotor().setPower(0);

        telemetry.addData("Path", "Complete");
        telemetry.update();
    }
}
