package org.penguinempire.commands;

import org.penguinempire.commands.autonomous.AutoMotions;
import org.penguinempire.commands.autonomous.MoveCommandOdometry;
import org.penguinempire.commands.autonomous.MoveCommandVision;
import org.penguinempire.subsystems.ClimberSubsystem;
import org.penguinempire.subsystems.DriveSubsystem;
import org.penguinempire.subsystems.IntakeSubsystem;
import org.penguinempire.subsystems.ShooterSubsystem;
import org.penguinempire.subsystems.VisionSubsystem;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Commands;
import edu.wpi.first.wpilibj2.command.ParallelCommandGroup;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;

public class Autos extends Command {
    private DriveSubsystem driveSubsystem;
    private ShooterSubsystem shooterSubsystem;
    private IntakeSubsystem intakeSubsystem;
    private AutoMotions autoMotion;
    private VisionSubsystem visionSubsystem;
    private ClimberSubsystem climberSubsystem;

    public Autos(DriveSubsystem ds, AutoMotions aM, ShooterSubsystem sS, IntakeSubsystem iS, VisionSubsystem vS, ClimberSubsystem cS) {
        this.driveSubsystem = ds;
        this.shooterSubsystem = sS;
        this.intakeSubsystem = iS;
        this.autoMotion = aM;
        this.visionSubsystem = vS;
        this.climberSubsystem = cS;
    }

    public Command test() {
        return Commands.race(
            autoMotion.runIntake(),
            autoMotion.runShooter(),
            new SequentialCommandGroup(
                new PositionCommand(shooterSubsystem, intakeSubsystem, climberSubsystem, PositionCommand.Position.OUT_OF_AUTO_POSITION),
                new ParallelCommandGroup(
                        autoMotion.intakeAutoMotion(),
                        new MoveCommandOdometry(driveSubsystem, new Pose2d(0.809, 1.107, new Rotation2d(0.5)), true,
                                0.75),
                        new PositionCommand(shooterSubsystem, intakeSubsystem, climberSubsystem, PositionCommand.Position.SAFE_OR_SPEAKER)),
                new MoveCommandVision(driveSubsystem, true, 0.75, visionSubsystem),
                autoMotion.shootingMiddleAutoMotion(),
                new ParallelCommandGroup(
                    autoMotion.intakeAutoMotion(),
                    new MoveCommandOdometry(driveSubsystem, new Pose2d(0.866, 0.452, new Rotation2d(0.3)), true, 0.75)
                ),
                new MoveCommandVision(driveSubsystem, true, 0.75, visionSubsystem),
                autoMotion.shootingMiddleAutoMotion(),
                new ParallelCommandGroup(
                    autoMotion.intakeAutoMotion(),
                    new MoveCommandOdometry(driveSubsystem, new Pose2d(1.01, -0.725, new Rotation2d(-0.5)), true, 0.75)
                ),
                new MoveCommandVision(driveSubsystem, true, 0.75, visionSubsystem),
                autoMotion.shootingMiddleAutoMotion()

        )
        );
    }
}
