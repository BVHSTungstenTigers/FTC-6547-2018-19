package org.firstinspires.ftc.teamcode.wolfram;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

@TeleOp(name = "Controller Field Relative")
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

        // Update the IMU and adjust Offset
        getBot().updateIMU();
        if (gamepad1.right_trigger >= .7 && gamepad1.left_trigger >= .7) {
            getBot().setImuAngleOffset(-22.5);
        }

        if (gamepad1.left_bumper && gamepad1.right_bumper && getBot().getImuAngleOffset() == -45) {
            getBot().setImuAngleOffset(0);
        } else if (gamepad1.left_bumper && gamepad1.right_bumper) {
            getBot().setImuAngleOffset(0);
            getBot().setImuAngleOffset(-getBot().getImuAngleOffset());
        }

        // Left joystick translates to overall movement
        // Rotation is controlled by the horizontal on right joystick
        // Credits to https://ftcforum.firstinspires.org/forum/ftc-technology/android-studio/6361-mecanum-wheels-drive-code-example for the initial code
        double frontLeft;
        double backLeft;
        double frontRight;
        double backRight;

        // I have no idea what this math means
        // It might as well be greek (oh wait math uses greek letters)
        if (fieldRelative) {
            double r = Math.hypot(gamepad1.left_stick_x, gamepad1.left_stick_y);
            double joystickAngle = Math.atan2(gamepad1.left_stick_y, gamepad1.left_stick_x) - Math.PI / 4;
            double robotAngle = getBot().getIMUAngle();
            double rightX = gamepad1.right_stick_x;

            frontLeft = r * Math.cos(joystickAngle - robotAngle) + rightX;
            backLeft = r * Math.sin(joystickAngle - robotAngle) + rightX;
            frontRight = r * Math.sin(joystickAngle - robotAngle) - rightX;
            backRight = r * Math.cos(joystickAngle - robotAngle) - rightX;
        } else {
            frontLeft = -gamepad1.left_stick_y + gamepad1.left_stick_x + gamepad1.right_stick_x;
            frontRight = -gamepad1.left_stick_y - gamepad1.left_stick_x - gamepad1.right_stick_x;
            backLeft = -gamepad1.left_stick_y - gamepad1.left_stick_x + gamepad1.right_stick_x;
            backRight = -gamepad1.left_stick_y + gamepad1.left_stick_x - gamepad1.right_stick_x;
        }

        // Set the motor powers
        if (gamepad1.y) speedModifier = .25;
        if (gamepad1.b && !gamepad1.start) speedModifier = .7;
        if (gamepad1.a && !gamepad1.start) speedModifier = 1;

        getBot().getFrontLeftMotor().setPower(frontLeft * speedModifier);
        getBot().getBackLeftMotor().setPower(backLeft * speedModifier);
        getBot().getFrontRightMotor().setPower(frontRight * speedModifier);
        getBot().getBackRightMotor().setPower(backRight * speedModifier);

        // Telemetry
        getBot().dumpTelemetry(telemetry);
        telemetry.addData("Field Relative", fieldRelative);
        telemetry.update();
    }
}
