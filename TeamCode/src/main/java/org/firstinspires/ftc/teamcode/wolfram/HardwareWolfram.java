package org.firstinspires.ftc.teamcode.wolfram;

import android.os.Environment;

import androidx.annotation.Nullable;

import com.qualcomm.ftccommon.SoundPlayer;
import com.qualcomm.ftcrobotcontroller.BuildConfig;
import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.hardware.bosch.JustLoggingAccelerationIntegrator;
import com.qualcomm.hardware.rev.RevBlinkinLedDriver;
import com.qualcomm.robotcore.hardware.AnalogInput;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorController;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.PIDFCoefficients;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.ServoController;
import com.qualcomm.robotcore.util.ReadWriteFile;

import org.firstinspires.ftc.robotcontroller.external.samples.SensorBNO055IMU;
import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;
import org.firstinspires.ftc.robotcore.external.navigation.Orientation;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Arrays;
import java.util.EnumSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

/**
 * This is the class that stores the hardware profile of the bot
 * The bot's name is Wolfram. Wolfram is not an API or anything.
 */
@Getter
public class HardwareWolfram {
    private final HardwareMap map;
    private final Set<TelemetryFlag> telemetryFlags = EnumSet.allOf(TelemetryFlag.class); // Change to empty set to reduce lag

    // Chasis motors
    private final DcMotor frontLeftMotor; // frontLeft
    private final DcMotor frontRightMotor; // frontRight
    private final DcMotor backLeftMotor; // backLeft
    private final DcMotor backRightMotor; // backRight

    // Claw
    private final Servo clawServo; // claw, optional
    private final DcMotorEx armMotor; // arm, optional
    private final int maxArmPosition = 285; // START THE ARM ON TOP OF THE HARDWARE STOP, NOT THE GROUND
    private final int minArmPosition = 0;
    private final double armPidValue = 25;

    // Wheel
    private final DcMotor wheelMotor; // wheel, optional

    // Other components
    @Nullable
    private final RevBlinkinLedDriver blinkinLedDriver; // lights, optional

    @Nullable
    private final AnalogInput limitSwitch; // inputSwitch, optional
    private final float limitSwitchCutoff = 1.5f;

    // IMU
    private final BNO055IMU imu; // imu
    private Orientation angles;
    @Setter
    private double imuAngleOffset;

    private final File soundDir = new File(Environment.getExternalStorageDirectory(), "/sound");
    private final File pidfDir = new File(Environment.getExternalStorageDirectory(), "PID");

    public HardwareWolfram(@NonNull HardwareMap map) {
        this.map = map;

        // Make sure dirs exist
        if (!soundDir.exists()) soundDir.mkdirs();
        if (!pidfDir.exists()) pidfDir.mkdirs();

        // Load chasis the motors
        frontLeftMotor = map.get(DcMotor.class, "frontLeft");
        frontRightMotor = map.get(DcMotor.class, "frontRight");
        backLeftMotor = map.get(DcMotor.class, "backLeft");
        backRightMotor = map.get(DcMotor.class, "backRight");

        // Load the claw, arm, and wheel motors
        clawServo = map.tryGet(Servo.class, "claw");
        armMotor = map.tryGet(DcMotorEx.class, "arm");
        wheelMotor = map.tryGet(DcMotor.class, "wheel");

        if (clawServo != null) {
            clawServo.scaleRange(0.3, 0.5); // tested experimentally
        }

        // Make them stationary
        frontLeftMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        frontRightMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        backLeftMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        backRightMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        // Invert needed motors
        frontLeftMotor.setDirection(DcMotorSimple.Direction.REVERSE);
        frontRightMotor.setDirection(DcMotorSimple.Direction.FORWARD);
        backLeftMotor.setDirection(DcMotorSimple.Direction.REVERSE);
        backRightMotor.setDirection(DcMotorSimple.Direction.FORWARD);

        // Load lights
        blinkinLedDriver = map.tryGet(RevBlinkinLedDriver.class, "lights");

        // Load limit switch
        limitSwitch = map.tryGet(AnalogInput.class, "limitSwitch");

        // Load the IMU
        BNO055IMU.Parameters parameters = new BNO055IMU.Parameters();
        parameters.angleUnit = BNO055IMU.AngleUnit.DEGREES;
        parameters.accelUnit = BNO055IMU.AccelUnit.METERS_PERSEC_PERSEC;
        parameters.calibrationDataFile = "BNO055IMUCalibration.json"; // see the calibration sample opmode TODO Where is this file and is there a default?
        parameters.loggingEnabled = true;
        parameters.loggingTag = "IMU";
        // parameters.accelerationIntegrationAlgorithm = new JustLoggingAccelerationIntegrator();
        parameters.mode = BNO055IMU.SensorMode.IMU;

        // Retrieve and initialize the IMU. We expect the IMU to be attached to an I2C port
        // on a Core Device Interface Module, configured to be a sensor of type "AdaFruit IMU",
        // and named "imu".
        imu = map.get(BNO055IMU.class, "imu");
        imu.initialize(parameters);
        updateIMU();
    }

    public File getSoundFile(String name) {
        return new File(soundDir, name);
    }

    public void playSound(File file) {
        SoundPlayer.getInstance().startPlaying(map.appContext, file);
    }

    public boolean isLimitSwitchTriggered() {
        assert limitSwitch != null;
        return limitSwitch.getVoltage() >= getLimitSwitchCutoff();
    }

    public void dumpTelemetry(Telemetry telemetry) {
        // Versions
        telemetry.addData("Version", BuildConfig.APP_BUILD_TIME);

        if (getTelemetryFlags().contains(TelemetryFlag.IMU)) {
            telemetry.addData("IMU Angle", getIMUAngle());
            telemetry.addData(
                     "IMU Angle Offset", getImuAngleOffset());
        }

        // Limit switch
        if (getTelemetryFlags().contains(TelemetryFlag.LIMIT_SWITCH) && getLimitSwitch() != null) {
            telemetry.addData("Limit Switch Voltage", getLimitSwitch().getVoltage());
            telemetry.addData("Limit Switch Triggered", isLimitSwitchTriggered());
        }

        // Motors
        if (getTelemetryFlags().contains(TelemetryFlag.CHASIS)) {
            telemetry.addData("Front Left Power", getFrontLeftMotor().getPower());
            telemetry.addData("Front Right Power", getFrontRightMotor().getPower());
            telemetry.addData("Back Left Power", getBackLeftMotor().getPower());
            telemetry.addData("Back Right Power", getBackRightMotor().getPower());
        }

        if (getTelemetryFlags().contains(TelemetryFlag.WHEEL)) {
            telemetry.addData("Wheel Power", getWheelMotor().getPower());
        }

        if (getTelemetryFlags().contains(TelemetryFlag.ARM)) {
            telemetry.addData("Arm Power", getArmMotor().getPower());
            telemetry.addData("Arm Position", getArmMotor().getCurrentPosition());
            telemetry.addData("Arm Target Position", getArmMotor().getTargetPosition());
            telemetry.addData("Arm PID Using Encoder", getArmMotor().getPIDFCoefficients(DcMotor.RunMode.RUN_USING_ENCODER).toString());
            telemetry.addData("Arm PID To Position", getArmMotor().getPIDFCoefficients(DcMotor.RunMode.RUN_TO_POSITION).toString());
            telemetry.addData("Arm Target Position", getArmMotor().getTargetPosition());
        }

        if (getTelemetryFlags().contains(TelemetryFlag.CLAW)) {
            telemetry.addData("Claw Position", getClawServo().getPosition());
        }
    }

    public void updateIMU() {
        this.angles = imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX, AngleUnit.DEGREES);
    }

    public double getIMUAngle() {
        double currentAngle = (angles.firstAngle + imuAngleOffset + 360) % 360;
        return currentAngle > 180 ?  currentAngle - 360 : currentAngle;
    }
}
