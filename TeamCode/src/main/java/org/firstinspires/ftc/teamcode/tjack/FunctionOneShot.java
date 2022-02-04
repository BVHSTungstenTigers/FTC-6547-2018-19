/*Custom Function we made to make things easier for code we want to execute once when a button is pressed.*/
package org.firstinspires.ftc.teamcode.tjack;

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
