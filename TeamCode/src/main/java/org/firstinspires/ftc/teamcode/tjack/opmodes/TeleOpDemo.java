package org.firstinspires.ftc.teamcode.tjack.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.teamcode.tjack.CustomOpMode;
import org.firstinspires.ftc.teamcode.tjack.TelemetryFlag;

import java.util.EnumSet;

@TeleOp(name = "[DEMO] Bot Display")
public class TeleOpDemo extends CustomOpMode {
    private boolean fieldRelative = true;
    private int targetPosition;

    public TeleOpDemo() {
        super(EnumSet.of(TelemetryFlag.ARM, TelemetryFlag.CLAW));
    }

    @Override
    public void init() {
        super.init();
        //controller 1 set field relative
        registerOneShot(() -> gamepad1.x, () -> fieldRelative = !fieldRelative);

        //set arm positions (fixed)
        registerOneShot(() -> gamepad1.y, () -> targetPosition = 100);
        registerOneShot(() -> gamepad1.a, () -> targetPosition = 50);

        // Setup PID bs
        if (getBot().getArmMotor() != null) {
            // Unknown
            // PIDFCoefficients coefficients = new PIDFCoefficients(getBot().getArmPidValue() / 10, getBot().getArmPidValue() / 100, 0, getBot().getArmPidValue());
            // getBot().getArmMotor().setPIDFCoefficients(DcMotor.RunMode.RUN_USING_ENCODER, coefficients);
            targetPosition = getBot().getArmMotor().getCurrentPosition();
            getBot().getArmMotor().setTargetPosition(targetPosition);
            getBot().getArmMotor().setPower(1);

            getBot().getArmMotor().setMode(DcMotor.RunMode.RUN_TO_POSITION);
        }
    }

    @Override
    public void loop() {
        super.loop();
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
        getBot().getFrontLeftMotor().setPower((r * Math.cos(joystickAngle) + rightX));
        getBot().getBackLeftMotor().setPower((r * Math.sin(joystickAngle) + rightX));
        getBot().getFrontRightMotor().setPower((r * Math.sin(joystickAngle) - rightX));
        getBot().getBackRightMotor().setPower((r * Math.cos(joystickAngle) - rightX));

        //
        // Claw
        //

        // Arm Manual
        if (getBot().getArmMotor() != null) {
            getBot().getArmMotor().setTargetPosition(targetPosition);
        }

        // Duck Wheel 1 and 2
        if (getBot().getDuckWheelMotor1() != null && getBot().getDuckWheelMotor2() != null) {
            //use "a" to turn duck wheel on and off (duck wheels)
            getBot().getDuckWheelMotor1().setPower(gamepad1.b ? 1 : 0);
            getBot().getDuckWheelMotor2().setPower(gamepad1.b ? 1 : 0);
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
