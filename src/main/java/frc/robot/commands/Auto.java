package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Commands;
import frc.robot.commands.autonomous.MoveCommandOdometry;
import frc.robot.subsystems.DriveSubsystem;
import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.geometry.Translation2d;

public class Auto extends Command {

    // basic back out
    public Command firstRoutine(DriveSubsystem driveSubsystem) {
        return Commands.sequence(
            Commands.parallel(
                new MoveCommandOdometry(driveSubsystem, new Pose2d(0.5, 0.0, new Rotation2d(0.0)), true,
                    0.5)));
    }

    // can be whatever needed
    public Command secondRoutine(DriveSubsystem driveSubsystem) {
        return new MoveCommandOdometry(driveSubsystem, new Pose2d(0.1, 0.0, new Rotation2d(Math.toDegrees(0))), true, 0.5);
    }
}