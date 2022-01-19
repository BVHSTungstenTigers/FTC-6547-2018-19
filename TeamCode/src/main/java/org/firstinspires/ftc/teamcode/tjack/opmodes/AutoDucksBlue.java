package org.firstinspires.ftc.teamcode.tjack.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.tjack.AllianceColor;
import org.firstinspires.ftc.teamcode.tjack.CustomOpMode;

@Autonomous(name="[GAME] Auto Ducks Blue")
public class AutoDucksBlue extends AutoDucks {
    public AutoDucksBlue() {
        super(AllianceColor.BLUE);
    }
}
