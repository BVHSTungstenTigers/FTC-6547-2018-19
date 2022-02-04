/*This is are hardware file where we load the motors, servos, duck wheels, and other mechanics.
We also load telemetry (essentially stats) onto the Driver hub through this file. Other variables
such as max/min arm position are set here as well.*/
package org.firstinspires.ftc.teamcode.tjack;

import android.os.Environment;

import androidx.annotation.Nullable;

import com.qualcomm.ftccommon.SoundPlayer;
import com.qualcomm.ftcrobotcontroller.BuildConfig;
import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.hardware.rev.RevBlinkinLedDriver;
import com.qualcomm.robotcore.hardware.AnalogInput;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;
import org.firstinspires.ftc.robotcore.external.navigation.Orientation;

import java.io.File;
import java.util.EnumSet;
import java.util.Set;

import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

/**
 * This is the class that stores the hardware profile of the bot
 * The bot's name is TJack. TJack is not an API or anything.
 */
@Getter
public class HardwareTJack {
    private final HardwareMap map;
    private final Set<TelemetryFlag> telemetryFlags; // Change to empty set to reduce lag

    // Chasis motors
    private final DcMotor frontLeftMotor; // frontLeft
    private final DcMotor frontRightMotor; // frontRight
    private final DcMotor backLeftMotor; // backLeft
    private final DcMotor backRightMotor; // backRight

    // Claw and Arm
    private final Servo clawServo0; // claw, optional
    private final Servo clawServo1; // claw, optional
    private final DcMotorEx armMotor; // arm, optional
    private final int maxArmPosition = 300; /* START THE ARM ON TOP OF THE HARDWARE STOP, NOT THE GROUND*/
    private final int minArmPosition = 0; // min value

    // Wheel
    private final DcMotor duckWheelMotor1; // wheel1, optional
    private final DcMotor duckWheelMotor2; // wheel2, optional

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

    public HardwareTJack(@NonNull HardwareMap map, @NonNull Set<TelemetryFlag> telemetryFlags) {
        this.map = map;
        this.telemetryFlags = telemetryFlags;

        // Make sure dirs exist
        if (!soundDir.exists()) soundDir.mkdirs();
        if (!pidfDir.exists()) pidfDir.mkdirs();

        // Load chasis the motors
        frontLeftMotor = map.get(DcMotor.class, "frontLeft");
        frontRightMotor = map.get(DcMotor.class, "frontRight");
        backLeftMotor = map.get(DcMotor.class, "backLeft");
        backRightMotor = map.get(DcMotor.class, "backRight");

        // Load the claw, arm, and wheel motors
        clawServo0 = map.tryGet(Servo.class, "clawLeft");
        clawServo1 = map.tryGet(Servo.class, "clawRight");
        armMotor = map.tryGet(DcMotorEx.class, "arm");
        duckWheelMotor1 = map.tryGet(DcMotor.class, "wheel1");
        duckWheelMotor2 = map.tryGet(DcMotor.class, "wheel2");

        if (clawServo0 != null) {
            clawServo0.scaleRange(0, 0.20); // tested experimentally
        }

        if (clawServo1 != null) {
            clawServo1.scaleRange(0, 0.20); // tested experimentally
        }

        if (duckWheelMotor1 != null) { // wheel should break
            duckWheelMotor1.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        }
        if (duckWheelMotor2 != null) { // wheel should break
            duckWheelMotor2.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        }

        // Make them stationary
        frontLeftMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        frontRightMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        backLeftMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        backRightMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        // Invert needed motors
        clawServo1.setDirection(Servo.Direction.REVERSE);
        frontLeftMotor.setDirection(DcMotorSimple.Direction.REVERSE);
        frontRightMotor.setDirection(DcMotorSimple.Direction.FORWARD);
        backLeftMotor.setDirection(DcMotorSimple.Direction.REVERSE);
        backRightMotor.setDirection(DcMotorSimple.Direction.FORWARD);

        // Brake on zero-power
        frontLeftMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        frontRightMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        backLeftMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        backRightMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);


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
            telemetry.addData("IMU Raw Angle 1", angles.firstAngle);
            telemetry.addData("IMU Raw Angle 2", angles.secondAngle);
            telemetry.addData("IMU Raw Angle 3", angles.thirdAngle);
            telemetry.addData(
                     "IMU Angle Offset", getImuAngleOffset());
        }

        // Limit switch
        if (getTelemetryFlags().contains(TelemetryFlag.LIMIT_SWITCH)) {
            if (getLimitSwitch() != null) {
                telemetry.addData("Limit Switch Voltage", getLimitSwitch().getVoltage());
                telemetry.addData("Limit Switch Triggered", isLimitSwitchTriggered());
            } else {
                telemetry.addLine("Limit Switch Not Found");
            }
        }

        // Motors
        //wheels
        if (getTelemetryFlags().contains(TelemetryFlag.CHASIS)) {
            telemetry.addData("Front Left Power", getFrontLeftMotor().getPower());
            telemetry.addData("Front Right Power", getFrontRightMotor().getPower());
            telemetry.addData("Back Left Power", getBackLeftMotor().getPower());
            telemetry.addData("Back Right Power", getBackRightMotor().getPower());
        }

        //duck wheels
        if (getTelemetryFlags().contains(TelemetryFlag.WHEEL)) {
            if (getDuckWheelMotor1() != null) {
                telemetry.addData("Wheel 1 Power", getDuckWheelMotor1().getPower());
            } else {
                telemetry.addLine("Wheel 1 Not Found");
            }

            if (getDuckWheelMotor2() != null) {
                telemetry.addData("Wheel 2 Power", getDuckWheelMotor2().getPower());
            } else {
                telemetry.addLine("Wheel 2 Not Found");
            }
        }

        //telemetry
        if (getTelemetryFlags().contains(TelemetryFlag.ARM)) {
            if (getArmMotor() != null) {
                telemetry.addData("Arm Power", getArmMotor().getPower());
                telemetry.addData("Arm Position", getArmMotor().getCurrentPosition());
                telemetry.addData("Arm Target Position", getArmMotor().getTargetPosition());
                telemetry.addData("Arm PID Using Encoder", getArmMotor().getPIDFCoefficients(DcMotor.RunMode.RUN_USING_ENCODER).toString());
                telemetry.addData("Arm PID To Position", getArmMotor().getPIDFCoefficients(DcMotor.RunMode.RUN_TO_POSITION).toString());
                telemetry.addData("Arm Target Position", getArmMotor().getTargetPosition());
            } else {
                telemetry.addLine("Arm Motor Not Found");
            }
        }

        //servo
        if (getTelemetryFlags().contains(TelemetryFlag.CLAW)) {
            if (getClawServo0() != null) {
                telemetry.addData("Claw Position", getClawServo0().getPosition());
            } else {
                telemetry.addLine("Claw Not Found");
            }
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
