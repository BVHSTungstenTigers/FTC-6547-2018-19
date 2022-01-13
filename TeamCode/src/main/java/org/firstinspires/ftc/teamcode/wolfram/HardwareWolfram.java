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
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;
import org.firstinspires.ftc.robotcore.external.navigation.Orientation;

import java.io.File;

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

    // Chasis motors
    private final DcMotor frontLeftMotor; // frontLeft
    private final DcMotor frontRightMotor; // frontRight
    private final DcMotor backLeftMotor; // backLeft
    private final DcMotor backRightMotor; // backRight

    // Claw
    private final DcMotor clawMotor; // claw, optional
    private final DcMotor armMotor; //arm, optional

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

    private final File soundDir = new File(Environment.getExternalStorageDirectory(), "/FIRST/blocks/sound");

    public HardwareWolfram(@NonNull HardwareMap map) {
        this.map = map;

        // Load chasis the motors
        frontLeftMotor = map.get(DcMotor.class, "frontLeft");
        frontRightMotor = map.get(DcMotor.class, "frontRight");
        backLeftMotor = map.get(DcMotor.class, "backLeft");
        backRightMotor = map.get(DcMotor.class, "backRight");

        // Load the claw motors
        clawMotor = map.get(DcMotor.class, "claw");
        armMotor = map.get(DcMotor.class, "arm");

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
        parameters.mode = BNO055IMU.SensorMode.GYRONLY;

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

        // Update the IMU data
        telemetry.addAction(this::updateIMU);
        telemetry.addData("IMU Angle", getIMUAngle());
        telemetry.addData("IMU Angle Offset", getImuAngleOffset());

        // Limit switch
        if (getLimitSwitch() != null) {
            telemetry.addData("Limit Switch Voltage", getLimitSwitch().getVoltage());
            telemetry.addData("Limit Switch Triggered", isLimitSwitchTriggered());
        }

        // Motors
        telemetry.addData("Front Left Power", getFrontLeftMotor().getPowerFloat());
        telemetry.addData("Front Right Power", getFrontRightMotor().getPowerFloat());
        telemetry.addData("Back Left Power", getBackLeftMotor().getPowerFloat());
        telemetry.addData("Back Right Power", getBackRightMotor().getPowerFloat());
        telemetry.addData("Limit Switch Triggered", isLimitSwitchTriggered());
    }

    public void updateIMU() {
        this.angles = imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX, AngleUnit.DEGREES);
    }

    public double getIMUAngle() {
        double currentAngle = (angles.firstAngle + imuAngleOffset + 360) % 360;
        return currentAngle > 180 ? 180 - currentAngle : currentAngle;
    }
}
