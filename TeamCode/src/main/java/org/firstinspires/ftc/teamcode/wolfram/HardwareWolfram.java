package org.firstinspires.ftc.teamcode.wolfram;

import android.os.Environment;

import com.qualcomm.ftccommon.SoundPlayer;
import com.qualcomm.hardware.rev.RevBlinkinLedDriver;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;

import java.io.File;

import lombok.Getter;
import lombok.NonNull;

/**
 * This is the class that stores the hardware profile of the bot
 * The bot's name is Wolfram. Wolfram is not an API or anything.
 */
@Getter
public class HardwareWolfram {
    private final HardwareMap map;

    private final DcMotor frontLeftMotor; // frontLeft
    private final DcMotor frontRightMotor; // frontRight
    private final DcMotor backLeftMotor; // backLeft
    private final DcMotor backRightMotor; // backRight

    private final RevBlinkinLedDriver blinkinLedDriver; // lights

    private final File soundDir = new File(Environment.getExternalStorageDirectory(), "/FIRST/blocks/sound");

    public HardwareWolfram(@NonNull HardwareMap map) {
        this.map = map;

        // Load the motors
        frontLeftMotor = map.get(DcMotor.class, "frontLeft");
        frontRightMotor = map.get(DcMotor.class, "frontRight");
        backLeftMotor = map.get(DcMotor.class, "backLeft");
        backRightMotor = map.get(DcMotor.class, "backRight");

        // Make them stationary
        frontLeftMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        frontRightMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        backLeftMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        backRightMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        // Invert the left
        frontLeftMotor.setDirection(DcMotorSimple.Direction.REVERSE);
        frontRightMotor.setDirection(DcMotorSimple.Direction.REVERSE);

        // Load lights
        blinkinLedDriver = map.get(RevBlinkinLedDriver.class, "lights");
    }

    public File getSoundFile(String name) {
        return new File(soundDir, name);
    }

    public void playSound(File file) {
        SoundPlayer.getInstance().startPlaying(map.appContext, file);
    }
}
