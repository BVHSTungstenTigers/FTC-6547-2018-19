package org.firstinspires.ftc.teamcode.tjack.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.tjack.AllianceColor;

@Autonomous(name="[GAME] Forwards Red")
public class AutoDriveRed extends AutoDriveForwards {
    public AutoDriveRed() {
        super(AllianceColor.RED);
    }
}
