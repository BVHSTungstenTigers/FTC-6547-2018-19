package org.firstinspires.ftc.teamcode.custom;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

@TeleOp(name="Emergency we are super behind and im in a panic pls help")
public class EmergencyTeleop extends OpMode {
    public DcMotor frontLeft = null;
    public DcMotor frontRight = null;
    public DcMotor backLeft = null;
    public DcMotor backRight = null;
    @Override
    public void init() {
        // Create motors
        frontLeft = hardwareMap.get(DcMotor.class, "frontLeft");
        frontRight = hardwareMap.get(DcMotor.class, "frontRight");
        backLeft = hardwareMap.get(DcMotor.class, "backLeft");
        backRight = hardwareMap.get(DcMotor.class, "backRight");

        // Make them stationary
        frontLeft.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        frontRight.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        backLeft.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        backRight.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        // Invert the left
        frontLeft.setDirection(DcMotorSimple.Direction.REVERSE);
        frontRight.setDirection(DcMotorSimple.Direction.REVERSE);

        // Report
        telemetry.addData(">", "Robot Ready.");
        telemetry.update();
    }

    @Override
    public void loop() {
        // The joystick goes negative when pushed forwards, so negate it
        double tangent = gamepad1.left_stick_x;
        double normal = -gamepad1.left_stick_y;

        double max = Math.max(Math.abs(tangent), Math.abs(normal));
        if (max > 1.0) {
            tangent /= max;
            normal /= max;
        }

        frontLeft.setPower(gamepad1.a ? 0.25 : normal);
        frontRight.setPower(gamepad1.b ? 0.25 : normal);
        backLeft.setPower(gamepad1.x ? 0.25 : normal);
        backRight.setPower(gamepad1.y ? 0.25 : normal);

        telemetry.addData("Normal", normal);
        telemetry.addData("Tangent", tangent);

        try {
            Thread.sleep(50);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
