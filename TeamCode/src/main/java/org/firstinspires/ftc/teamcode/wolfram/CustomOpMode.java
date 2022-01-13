package org.firstinspires.ftc.teamcode.wolfram;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.util.ElapsedTime;

import java.util.ArrayList;
import java.util.Collection;
import java.util.function.BooleanSupplier;

import lombok.Getter;

public abstract class CustomOpMode extends OpMode {
    @Getter
    private HardwareWolfram bot;

    private long totalLoops = 0;
    private ElapsedTime time = new ElapsedTime();

    private final Collection<FunctionOneShot> oneShots = new ArrayList<>();

    @Override
    public void init() {
        bot = new HardwareWolfram(hardwareMap);

        // Report
        telemetry.addData(">", "Robot Ready.");
        telemetry.addAction(getBot()::updateIMU);
        telemetry.update();
    }

    @Override
    public void start() {
        super.start();
        time.reset();
    }

    protected void registerOneShot(BooleanSupplier supplier, Runnable runnable) {
        oneShots.add(new FunctionOneShot(supplier, runnable));
    }

    protected void sleep(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    @Override
    public void loop() {
        totalLoops++;
        telemetry.addData("Total Loops", totalLoops);
        telemetry.addData("Average TPS", totalLoops / time.seconds());

        oneShots.forEach(FunctionOneShot::run);
    }
}
