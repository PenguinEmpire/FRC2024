package org.penguinempire.commands.autonomous;

import org.penguinempire.commands.PositionCommand;
import org.penguinempire.subsystems.ClimberSubsystem;
import org.penguinempire.subsystems.IntakeSubsystem;
import org.penguinempire.subsystems.ShooterSubsystem;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Commands;
import edu.wpi.first.wpilibj2.command.ParallelCommandGroup;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.WaitCommand;

public class AutoMotions extends Command {
    private ShooterSubsystem shooterSubsystem;
    private IntakeSubsystem intakeSubsystem;
    private ClimberSubsystem climberSubsystem;

    public AutoMotions(ShooterSubsystem shooterSubsystem, IntakeSubsystem intakeSubsystem, ClimberSubsystem climberSubsystem) {
        this.shooterSubsystem = shooterSubsystem;
        this.intakeSubsystem = intakeSubsystem;
        this.climberSubsystem = climberSubsystem;
    }

    public void execute() {

    }

    // tamper with feeding timing for far distances
    public Command intakeAutoMotion() {
        return new ParallelCommandGroup(
                new PositionCommand(shooterSubsystem, intakeSubsystem, climberSubsystem, PositionCommand.Position.ARM_GROUND_PICKUP),
                new SequentialCommandGroup(
                        Commands.race(
                            shooterSubsystem.runFeeder().until(shooterSubsystem::hasRing),
                            new WaitCommand(2.5)
                        ),
                        new PositionCommand(shooterSubsystem, intakeSubsystem, climberSubsystem, PositionCommand.Position.AUTO_BASE)
                )
        );
    }

    public Command shootingAutoMotion() {
        return new ParallelCommandGroup(
                setShooterAutoPos(),
                new SequentialCommandGroup(
                    new WaitCommand(0.2),
                    runFeederWithTimeout()
                )
        );
    }

    public Command runIntake() {
        return intakeSubsystem.runRollers();
    }

    public Command runShooter() {
        return shooterSubsystem.runShooter();
    }

    // tamper with timing
    public Command runFeederWithTimeout() {
        return shooterSubsystem.runFeeder().withTimeout(0.5);
    }

    public Command setShooterAutoPos() {
        return new PositionCommand(shooterSubsystem, intakeSubsystem, climberSubsystem, PositionCommand.Position.SAFE_OR_SPEAKER);
    }

    // used to shoot from middle - need to change time
    public Command shootingMiddleAutoMotion() {
        return new SequentialCommandGroup(
                shooterSubsystem.runShooterRoutine(4));
    }


    // used to shoot from against the speaker
    public Command shootingClosestAutoMotion() {
        return new SequentialCommandGroup(
                new PositionCommand(shooterSubsystem, intakeSubsystem, climberSubsystem, PositionCommand.Position.SPEAKER),
                shooterSubsystem.runShooterRoutine(2));
    }

    // used to shoot from far out - need to change time (shooter)
    public Command shootingFarAutoMotion() {
        return shooterSubsystem.runFeeder().withTimeout(2.0);
    }
}