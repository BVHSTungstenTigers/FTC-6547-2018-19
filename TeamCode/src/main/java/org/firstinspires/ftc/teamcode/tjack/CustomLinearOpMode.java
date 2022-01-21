package org.firstinspires.ftc.teamcode.tjack;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.util.ElapsedTime;

import java.util.ArrayList;
import java.util.Collection;
import java.util.EnumSet;
import java.util.Set;
import java.util.function.BooleanSupplier;

import lombok.Getter;
/*To explain it shortly, for our Autonomous mode because of its OpMode it throws
exceptions where there are loops and in our AutoDucks we have a loop within a loop
(sleep in a sleep) so it throws and exception and stops the code. Having it call upon
a different OpMode (CustomLinearOpMode) from the "CustomOpMode" it won't throw anymore.*/

public abstract class CustomLinearOpMode extends LinearOpMode {
    @Getter
    private HardwareTJack bot;

    private final Set<TelemetryFlag> flags;

    public CustomLinearOpMode() {
        this(EnumSet.allOf(TelemetryFlag.class));
    }

    public CustomLinearOpMode(Set<TelemetryFlag> flags) {
        this.flags = flags;
    }

    @Override
    public void runOpMode() throws InterruptedException {
        bot = new HardwareTJack(hardwareMap, flags);
    }
}
