/*Debugging File*/
package org.firstinspires.ftc.teamcode.tjack.opmodes;

import com.qualcomm.hardware.rev.RevBlinkinLedDriver;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.teamcode.tjack.CustomOpMode;

import java.io.File;

@TeleOp(name="Hardware Test")
public class TeleOpDebug extends CustomOpMode {

    private RevBlinkinLedDriver.BlinkinPattern pattern = RevBlinkinLedDriver.BlinkinPattern.HOT_PINK;

    private File goldSound;
    private File silverSound;

    @Override
    public void init() {
        super.init();

        //Load sounds
        silverSound = getBot().getSoundFile("silver.wav");
        goldSound = getBot().getSoundFile("silver.wav");

        // Load oneshots
        // Start = change blinking pattern
        if (getBot().getBlinkinLedDriver() != null) {
            getBot().getBlinkinLedDriver().setPattern(pattern);
            registerOneShot(() -> gamepad1.start, () -> getBot().getBlinkinLedDriver().setPattern(pattern = pattern.next()));
        }

        // Left + Right trigger = silver and gold sounds
        if (silverSound.exists()) registerOneShot(() -> gamepad1.left_trigger >= 0.95f, () -> getBot().playSound(silverSound));
        if (goldSound.exists()) registerOneShot(() -> gamepad1.right_trigger >= 0.95f, () -> getBot().playSound(goldSound));

        if (getBot().getArmMotor() != null) {
            getBot().getArmMotor().setTargetPosition(getBot().getArmMotor().getCurrentPosition());
            getBot().getArmMotor().setMode(DcMotor.RunMode.RUN_TO_POSITION);
        }

        // Report
        telemetry.addData("Silver Exists", silverSound.exists());
        telemetry.addData("Gold Exists", goldSound.exists());
        telemetry.update();
    }

    @Override
    public void loop() {
        super.loop();

        // Map A / B to Front, and X / Y to back
        double debugMotorSpeed = 0.25;
        getBot().getFrontLeftMotor().setPower(gamepad1.a ? debugMotorSpeed : 0);
        getBot().getFrontRightMotor().setPower(gamepad1.b ? debugMotorSpeed : 0);
        getBot().getBackLeftMotor().setPower(gamepad1.x ? debugMotorSpeed : 0);
        getBot().getBackRightMotor().setPower(gamepad1.y ? debugMotorSpeed : 0);

        if (getBot().getArmMotor() != null) {
            getBot().getArmMotor().setTargetPosition((int) (gamepad1.left_trigger * getBot().getMaxArmPosition()));
        }

        // Wheels
        if (getBot().getDuckWheelMotor1() != null) {
            getBot().getDuckWheelMotor1().setPower(gamepad1.right_trigger * debugMotorSpeed);
        }
        if (getBot().getDuckWheelMotor2() != null) {
            getBot().getDuckWheelMotor2().setPower(gamepad1.right_trigger * debugMotorSpeed);
        }

        if (getBot().getClawServo0() != null) {
            getBot().getClawServo0().setPosition(gamepad1.right_stick_x);
        }

        if (getBot().getClawServo1() != null) {
            getBot().getClawServo1().setPosition(gamepad1.right_stick_x);
        }

        // Add telemetry for limit switch and LED
        getBot().dumpTelemetry(telemetry);
        telemetry.update();

        // Wait 50ms to avoid to much insanity
        sleep(50);
    }
}
