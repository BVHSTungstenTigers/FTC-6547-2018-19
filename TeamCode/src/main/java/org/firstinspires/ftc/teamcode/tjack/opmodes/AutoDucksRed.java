package org.firstinspires.ftc.teamcode.tjack.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.tjack.AllianceColor;
import org.firstinspires.ftc.teamcode.tjack.CustomOpMode;

@Autonomous(name="[GAME] Auto Ducks Red")
public class AutoDucksRed extends AutoDucks {
    public AutoDucksRed() {
        super(AllianceColor.BLUE);
    }
}
