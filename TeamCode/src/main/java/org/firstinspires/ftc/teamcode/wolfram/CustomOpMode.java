package org.firstinspires.ftc.teamcode.wolfram;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import java.util.ArrayList;
import java.util.Collection;
import java.util.function.BooleanSupplier;

import lombok.Getter;

public abstract class CustomOpMode extends OpMode {
    @Getter
    private HardwareWolfram bot;

    private final Collection<FunctionOneShot> oneShots = new ArrayList<>();

    @Override
    public void init() {
        bot = new HardwareWolfram(hardwareMap);

        // Report
        telemetry.addData(">", "Robot Ready.");
        telemetry.update();
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
        oneShots.forEach(FunctionOneShot::run);
    }
}
