package org.firstinspires.ftc.teamcode.wolfram;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

@TeleOp(name="Controller Simple POV Linear")
public class TeleOpPOVLinear extends CustomOpMode {
    private double multiplier = 0.5;

    @Override
    public void loop() {
        super.loop();

        // Left joystick translates to overall movement
        // Rotation is controlled by the horizontal on right joystick
        // Credits to https://ftcforum.firstinspires.org/forum/ftc-technology/android-studio/6361-mecanum-wheels-drive-code-example for the initial code
        if (gamepad1.a) multiplier = 0.6;
        else if (gamepad1.b) multiplier = 0.3;
        else if (gamepad1.x) multiplier = 0.1;
        else if (gamepad1.y) multiplier = 1;

        double x = gamepad1.left_stick_x + gamepad1.touchpad_finger_1_x;
        double y = -gamepad1.left_stick_y + gamepad1.touchpad_finger_1_y;
        double max = Math.max(x, y);
        if (max > 1) {
            x /= max;
            y /= max;
        }

        double r = Math.hypot(x, y);
        double robotAngle = Math.atan2(y, x) - Math.PI / 4;
        double rightX = gamepad1.right_stick_x;

        getBot().getFrontLeftMotor().setPower(multiplier * (r * Math.cos(robotAngle) + rightX));
        getBot().getBackLeftMotor().setPower(multiplier * (r * Math.sin(robotAngle) + rightX));
        getBot().getFrontRightMotor().setPower(multiplier * (r * Math.sin(robotAngle) - rightX));
        getBot().getBackRightMotor().setPower(multiplier * (r * Math.cos(robotAngle) - rightX));

        getBot().dumpTelemetry(telemetry);
        telemetry.update();
    }
}
