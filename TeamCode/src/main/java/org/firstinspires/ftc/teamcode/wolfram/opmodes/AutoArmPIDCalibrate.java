package org.firstinspires.ftc.teamcode.wolfram.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.PIDFCoefficients;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.wolfram.CustomOpMode;

// implementation of https://docs.google.com/document/d/1tyWrXDfMidwYyP_5H4mZyVgaEswhOC35gvdmP-V-5hA/edit#

@Autonomous(name="Calibrate Arm PID")
public class AutoArmPIDCalibrate extends CustomOpMode {
    @Override
    public void start() {
        super.start();
        ElapsedTime runtime = new ElapsedTime();

        // implement site
        getBot().getArmMotor().setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        getBot().getArmMotor().setPower(1);
        double maxVelocity = -1;

        while (runtime.seconds() < 1.0) {
            double currentVelocity = getBot().getArmMotor().getVelocity();
            if (currentVelocity > maxVelocity) maxVelocity = currentVelocity;
        }

        // calculate the PIDF components
        double p = 32767 / maxVelocity * 0.1;
        double i = 32767 / maxVelocity * 0.01;
        double f = 32767 / maxVelocity;

        // save them to a file for use later
        getBot().writePIDF("arm", new PIDFCoefficients(p, i, 0, f));

        // write to tel just in case
        telemetry.addData("Max Velocity", maxVelocity);
        telemetry.addData("P", p);
        telemetry.addData("I", p);
        telemetry.addData("F", p);
        telemetry.update();
    }
}
