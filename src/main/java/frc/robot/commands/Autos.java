package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Commands;
import frc.robot.commands.autonomous.MoveCommandOdometry;
import frc.robot.commands.autonomous.TurnCommand;
import frc.robot.subsystems.DriveSubsystem;
import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;



public class Autos extends Command{

    public Command firstRoutine(DriveSubsystem driveSubsystem) {
        return Commands.sequence(
            Commands.parallel(
                new MoveCommandOdometry(driveSubsystem, new Pose2d(0.1, 0.1, new Rotation2d()), true, 0.5)
            )
        );
    }
    

}
