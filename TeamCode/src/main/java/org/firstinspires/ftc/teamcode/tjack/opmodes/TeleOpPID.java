package org.firstinspires.ftc.teamcode.tjack.opmodes;
//bot IP address adb connect 192.168.43.1

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.teamcode.tjack.CustomOpMode;
import org.firstinspires.ftc.teamcode.tjack.TelemetryFlag;

import java.util.EnumSet;

@TeleOp(name = "[GAME] Field-Relative PID-Arm")
public class TeleOpPID extends CustomOpMode {
    private boolean fieldRelative = true;
    private double targetPosition;
    private boolean motorToggle = true;

    public TeleOpPID() {
        super(EnumSet.of(TelemetryFlag.ARM, TelemetryFlag.CLAW));
    }

    @Override
    public void init() {
        super.init();
        //controller 1 set field relative
        registerOneShot(() -> gamepad1.x, () -> fieldRelative = !fieldRelative);

        //controller 2 arm level controls (one shot- executes code once)
        //bottom level
        registerOneShot(() -> gamepad2.a, () -> targetPosition = 50);
        //middle hub
        registerOneShot(() -> gamepad2.y, () -> targetPosition = 100);
        //top level
        registerOneShot(() -> gamepad2.b, () -> targetPosition = 150 );

        //arm motor power toggle (= operator returns assigned value)
        registerOneShot(() -> gamepad2.x, () -> getBot().getArmMotor().setPower((motorToggle = !motorToggle) ? 1 : 0));
        //start power on arm motor


        //Close claw
        registerOneShot(() -> gamepad2.right_trigger > 0, () -> {
            getBot().getClawServo0().setPosition(1);
            getBot().getClawServo1().setPosition(1);
        });

        //Open claw
        registerOneShot(() -> gamepad2.left_trigger > 0, () -> {
            getBot().getClawServo0().setPosition(0.5);
            getBot().getClawServo1().setPosition(0.5);
        });

        /*targetPosition = getBot().getMinArmPosition() does not work*/

        /* [removed function] max (behind)
        registerOneShot(() -> gamepad2.x, () -> targetPosition = getBot().getMaxArmPosition());*/


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

        // CTRL 1: DRIVE SPEED: Left + right bumper = sneak + spring buttons
        // Hold them down
        double speedModifierA = gamepad1.left_bumper ? 0.3 : (gamepad1.right_bumper ? 1 : 0.7);

        // CTRL 2 ARM SPEED: Left + right bumper = sneak + spring buttons.
        // Hold them down
        double speedModifierB = gamepad2.left_bumper ? 0.3 : (gamepad2.right_bumper ? 1 : 0.7);

        /*//angle speed (make it happen slowly)
        double speedModifierC = 0.90;*/

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
        //adjust to make turn less- for angles
        double rightX = gamepad1.right_stick_x * 0.8;

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

        if (getBot().getClawServo0() != null) { // Don't trigger if a button control has been done to override this
            double position = getBot().getClawServo0().getPosition();
            position += (gamepad2.dpad_right?0.12:0) - (gamepad2.dpad_left?0.12:0);
            if (position > 1) position = 1;
            if (position < 0) position = 0;
            getBot().getClawServo0().setPosition(position);
            getBot().getClawServo1().setPosition(position);


        }


        // Arm Manual
        if (getBot().getArmMotor() != null) {
            targetPosition -= gamepad2.left_stick_y * 5 * speedModifierB;

            if (targetPosition > getBot().getMaxArmPosition()) targetPosition = getBot().getMaxArmPosition();
            if (targetPosition < 0) targetPosition = 0; //was get arm position min value

            getBot().getArmMotor().setTargetPosition((int) targetPosition);
        }

        // Duck Wheel 1 and 2
        if (getBot().getDuckWheelMotor1() != null) {
            getBot().getDuckWheelMotor1().setPower(gamepad2.right_stick_x * speedModifierB);
        }

        if (getBot().getDuckWheelMotor2() != null) {
            getBot().getDuckWheelMotor2().setPower(gamepad2.right_stick_x * speedModifierB);
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
