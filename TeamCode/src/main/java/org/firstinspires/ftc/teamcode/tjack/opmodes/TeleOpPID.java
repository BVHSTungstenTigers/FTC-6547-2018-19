package org.firstinspires.ftc.teamcode.tjack.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.teamcode.tjack.CustomOpMode;

@TeleOp(name = "[GAME] Field-Relative PID-Arm")
public class TeleOpPID extends CustomOpMode {
    private boolean fieldRelative;
    private double targetPosition;

    @Override
    public void init() {
        super.init();

        registerOneShot(() -> gamepad1.x, () -> fieldRelative = !fieldRelative);

        registerOneShot(() -> gamepad2.a, () -> getBot().getArmMotor().setTargetPosition(100));
        registerOneShot(() -> gamepad2.b, () -> getBot().getArmMotor().setTargetPosition(200));
        registerOneShot(() -> gamepad2.x, () -> getBot().getArmMotor().setTargetPosition(getBot().getMaxArmPosition()));
        registerOneShot(() -> gamepad2.y, () -> getBot().getArmMotor().setTargetPosition(getBot().getMinArmPosition()));

        // Setup PID bs
        if (getBot().getArmMotor() != null) {
            // Unknown
            // PIDFCoefficients coefficients = new PIDFCoefficients(getBot().getArmPidValue() / 10, getBot().getArmPidValue() / 100, 0, getBot().getArmPidValue());
            // getBot().getArmMotor().setPIDFCoefficients(DcMotor.RunMode.RUN_USING_ENCODER, coefficients);
            targetPosition = getBot().getArmMotor().getCurrentPosition();
            getBot().getArmMotor().setTargetPosition((int) targetPosition);
            getBot().getArmMotor().setPower(1);

            getBot().getArmMotor().setMode(DcMotor.RunMode.RUN_TO_POSITION);
        }
    }

    @Override
    public void loop() {
        super.loop();

        // Left + right bumper = sneak + spring buttons.
        // Hold them down
        double speedModifierA = gamepad1.left_bumper ? 0.3 : (gamepad1.right_bumper ? 1 : 0.7);
        double speedModifierB = gamepad2.left_bumper ? 0.3 : (gamepad2.right_bumper ? 1 : 0.7);

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

        // If any over 1, don't go over 1- arm motor limits
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
        getBot().getFrontLeftMotor().setPower((r * Math.cos(joystickAngle) + rightX) * speedModifierA);
        getBot().getBackLeftMotor().setPower((r * Math.sin(joystickAngle) + rightX) * speedModifierA);
        getBot().getFrontRightMotor().setPower((r * Math.sin(joystickAngle) - rightX) * speedModifierA);
        getBot().getBackRightMotor().setPower((r * Math.cos(joystickAngle) - rightX) * speedModifierA);

        //
        // Claw
        //

        // Servo
        // Removed to promote only manual control with A/B

        if (getBot().getClawServo() != null) { // Don't trigger if a button control has been done to override this
            double position = getBot().getClawServo().getPosition();
            position = position + gamepad2.left_trigger - gamepad2.right_trigger;
            if (position > 1) position = 1;
            if (position < 0) position = 0;
            getBot().getClawServo().setPosition(position);
        }


        // Arm Manual
        if (getBot().getArmMotor() != null) {
            targetPosition += gamepad2.left_stick_y * 5 * speedModifierB;

            if (targetPosition > getBot().getMaxArmPosition()) targetPosition = getBot().getMaxArmPosition();
            if (targetPosition < getBot().getMinArmPosition()) targetPosition = getBot().getMinArmPosition();

            getBot().getArmMotor().setTargetPosition((int) targetPosition);
        }

        // Wheel 1 and 2
        if (getBot().getWheelMotor1() != null) {
            getBot().getWheelMotor1().setPower(gamepad2.right_stick_x * speedModifierB);
        }

        if (getBot().getWheelMotor2() != null) {
            getBot().getWheelMotor2().setPower(gamepad2.right_stick_x * speedModifierB);
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
