package org.firstinspires.ftc.teamcode.wolfram.opmodes;

import com.qualcomm.ftccommon.SoundPlayer;
import com.qualcomm.hardware.rev.RevBlinkinLedDriver;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

import org.firstinspires.ftc.teamcode.wolfram.CustomOpMode;
import org.firstinspires.ftc.teamcode.wolfram.HardwareWolfram;

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

        // Add telemetry for limit switch and LED
        getBot().dumpTelemetry(telemetry);
        telemetry.update();

        // Wait 50ms to avoid to much insanity
        sleep(50);
    }
}
