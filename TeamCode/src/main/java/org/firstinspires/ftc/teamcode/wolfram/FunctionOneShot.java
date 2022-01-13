package org.firstinspires.ftc.teamcode.wolfram;

import java.util.function.BooleanSupplier;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class FunctionOneShot implements Runnable {
    private final BooleanSupplier supplier;
    private final Runnable runnable;

    private boolean pastValue;

    @Override
    public void run() {
        boolean currentValue = supplier.getAsBoolean();
        if (currentValue != pastValue) {
            if (currentValue) runnable.run();
            pastValue = currentValue;
        }
    }
}
