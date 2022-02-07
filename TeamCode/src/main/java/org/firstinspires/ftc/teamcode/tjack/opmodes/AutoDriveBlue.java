package org.firstinspires.ftc.teamcode.tjack.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.tjack.AllianceColor;

@Autonomous(name="[GAME] Forwards Blue")
public class AutoDriveBlue extends AutoDriveForwards {
    public AutoDriveBlue() {
        super(AllianceColor.BLUE);
    }
}
