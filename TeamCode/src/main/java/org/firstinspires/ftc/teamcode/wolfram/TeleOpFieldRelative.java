package org.firstinspires.ftc.teamcode.wolfram;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

@TeleOp(name = "Field-Relative (?) Controller")
public class TeleOpFieldRelative extends CustomOpMode {
    private boolean fieldRelative;
    private double speedModifier = 0.7;

    @Override
    public void init() {
        super.init();

        registerOneShot(() -> gamepad1.x, () -> fieldRelative = !fieldRelative);
    }

    @Override
    public void loop() {
        super.loop();

        // Set the speed modifier
        if (gamepad1.a) speedModifier = 0.6;
        else if (gamepad1.b) speedModifier = 0.3;
        else if (gamepad1.x) speedModifier = 0.1;
        else if (gamepad1.y) speedModifier = 1;

        // Update the IMU and adjust Offset. No clue what this does
        if (gamepad1.right_trigger >= .7 && gamepad1.left_trigger >= .7) {
            getBot().setImuAngleOffset(-22.5);
        } else if (gamepad1.left_bumper && gamepad1.right_bumper && getBot().getImuAngleOffset() == -45) {
            getBot().setImuAngleOffset(0);
        } else if (gamepad1.left_bumper && gamepad1.right_bumper) {
            getBot().setImuAngleOffset(0);
            getBot().setImuAngleOffset(-getBot().getImuAngleOffset());
        }

        // Left joystick translates to overall movement
        // Rotation is controlled by the horizontal on right joystick
        // Credits to https://ftcforum.firstinspires.org/forum/ftc-technology/android-studio/6361-mecanum-wheels-drive-code-example for the initial code

        // Combine the arrow keys and joystick
        double x = gamepad1.left_stick_x + gamepad1.touchpad_finger_1_x;
        double y = gamepad1.left_stick_y + gamepad1.touchpad_finger_1_y;
        double max = Math.max(x, y);
        if (max > 1) {
            x /= max;
            y /= max;
        }

        // I have no idea what this math means
        // It might as well be greek (oh wait math uses greek letters)
        double r = Math.hypot(x, y);
        double joystickAngle = Math.atan2(y, x) - Math.PI / 4;
        double rightX = gamepad1.right_stick_x;

        if (fieldRelative) joystickAngle -= getBot().getIMUAngle();

        // Set the motor powers
        getBot().getFrontLeftMotor().setPower((r * Math.cos(joystickAngle) + rightX) * speedModifier);
        getBot().getBackLeftMotor().setPower((r * Math.sin(joystickAngle) + rightX) * speedModifier);
        getBot().getFrontRightMotor().setPower((r * Math.sin(joystickAngle) - rightX) * speedModifier);
        getBot().getBackRightMotor().setPower((r * Math.cos(joystickAngle) - rightX) * speedModifier);

        // Telemetry
        getBot().dumpTelemetry(telemetry);
        telemetry.addData("Field Relative", fieldRelative);
        telemetry.update();
    }
}
