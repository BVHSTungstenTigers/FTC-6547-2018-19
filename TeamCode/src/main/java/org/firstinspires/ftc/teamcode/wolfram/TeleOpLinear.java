package org.firstinspires.ftc.teamcode.wolfram;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

@TeleOp(name="Control Linear")
public class TeleOpLinear extends CustomOpMode {
    @Override
    public void loop() {
        super.loop();

        // Left joystick translates to overall movement
        // Rotation is controlled by the horizontal on right joystick
        // Credits to https://ftcforum.firstinspires.org/forum/ftc-technology/android-studio/6361-mecanum-wheels-drive-code-example for the initial code
        double r = Math.hypot(gamepad1.left_stick_x, gamepad1.left_stick_y);
        double robotAngle = Math.atan2(gamepad1.left_stick_y, gamepad1.left_stick_x) - Math.PI / 4;
        double rightX = gamepad1.right_stick_x;

        getBot().getFrontLeftMotor().setPower(r * Math.cos(robotAngle) + rightX);
        getBot().getBackLeftMotor().setPower(r * Math.sin(robotAngle) + rightX);
        getBot().getFrontRightMotor().setPower(r * Math.sin(robotAngle) - rightX);
        getBot().getBackRightMotor().setPower(r * Math.cos(robotAngle) - rightX);
    }
}
