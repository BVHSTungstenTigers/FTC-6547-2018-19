package org.firstinspires.ftc.teamcode.wolfram.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.teamcode.wolfram.CustomOpMode;

@TeleOp(name = "Field-Relative (?) Controller")
public class TeleOpFieldRelative extends CustomOpMode {
    private boolean fieldRelative;
    private double speedModifier = 0.7;
    private int targetPosition;

    @Override
    public void init() {
        super.init();

        registerOneShot(() -> gamepad1.x, () -> fieldRelative = !fieldRelative);
        targetPosition = getBot().getArmMotor().getCurrentPosition();
        getBot().getArmMotor().setTargetPosition(targetPosition);
        getBot().getArmMotor().setMode(DcMotor.RunMode.RUN_TO_POSITION);
    }

    @Override
    public void loop() {
        super.loop();

        // Set the speed modifier
        if (gamepad1.a) speedModifier = 0.25;
        else if (gamepad1.b) speedModifier = 0.5;
        else if (gamepad1.y) speedModifier = 1;

        //
        // DRIVING
        //

        // Update the IMU and adjust Offset.
        if (gamepad1.start) { // Reset offset to power-on state
            getBot().setImuAngleOffset(0);
            getBot().setImuAngleOffset(-getBot().getIMUAngle());
        }

        // Left joystick translates to overall movement
        // Rotation is controlled by the horizontal on right joystick
        // Credits to https://ftcforum.firstinspires.org/forum/ftc-technology/android-studio/6361-mecanum-wheels-drive-code-example for the initial code

        // Combine the arrow keys and joystick
        double x = gamepad1.left_stick_x;
        double y = -gamepad1.left_stick_y;

        // Down and right take priority if the dpad's broken
        if (gamepad1.dpad_down) y = -1;
        else if (gamepad1.dpad_up) y = 1;
        if (gamepad1.dpad_right) x = 1;
        else if (gamepad1.dpad_left) x = -1;

        // If any over 1, don't go over 1
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

        if (fieldRelative) joystickAngle -= Math.toRadians(getBot().getIMUAngle());

        // Set the motor powers
        getBot().getFrontLeftMotor().setPower((r * Math.cos(joystickAngle) + rightX) * speedModifier);
        getBot().getBackLeftMotor().setPower((r * Math.sin(joystickAngle) + rightX) * speedModifier);
        getBot().getFrontRightMotor().setPower((r * Math.sin(joystickAngle) - rightX) * speedModifier);
        getBot().getBackRightMotor().setPower((r * Math.cos(joystickAngle) - rightX) * speedModifier);

        //
        // Claw
        //


        if (getBot().getClawServo() != null) {
            getBot().getClawServo().setPosition(gamepad2.left_trigger);
        }
        if (getBot().getArmMotor() != null) {
            if (gamepad2.dpad_up) targetPosition += 5;
            if (gamepad2.dpad_down) targetPosition -= 5;
            targetPosition = Math.max(getBot().getMinArmPosition(), Math.min(getBot().getMaxArmPosition(), targetPosition));
            getBot().getArmMotor().setTargetPosition(targetPosition);
            getBot().getArmMotor().setPower(.25);
        }
        if (getBot().getWheelMotor() != null) {
            getBot().getWheelMotor().setPower((gamepad2.right_bumper ? 1 : (gamepad2.left_bumper ? -1 : 0)) * speedModifier);
        }

        //
        // Telemetry
        //

        sleep(50);

        getBot().dumpTelemetry(telemetry);
        telemetry.addData("Field Relative", fieldRelative);
        telemetry.update();
    }
}
