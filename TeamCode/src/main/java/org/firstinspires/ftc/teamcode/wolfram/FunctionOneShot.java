package org.firstinspires.ftc.teamcode.wolfram;

import java.util.function.BooleanSupplier;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class FunctionOneShot implements Runnable {
    private final BooleanSupplier supplier;
    private final Runnable runnable;

    private boolean state;

    @Override
    public void run() {
        if (supplier.getAsBoolean() != state) state = !state;
        if (!state) runnable.run();
    }
}
