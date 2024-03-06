package frc.robot.commands.autonomous;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Commands;
import edu.wpi.first.wpilibj2.command.ParallelCommandGroup;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.WaitCommand;
import frc.robot.Constants.Vision;
import frc.robot.commands.PositionCommand;
import frc.robot.commands.PositionCommand.Position;
import frc.robot.subsystems.IntakeSubsystem;
import frc.robot.subsystems.ShooterSubsystem;

public class AutoMotions extends Command {
    private ShooterSubsystem shooterSubsystem;
    private IntakeSubsystem intakeSubsystem;

    public AutoMotions(ShooterSubsystem shooterSubsystem, IntakeSubsystem intakeSubsystem) {
        this.shooterSubsystem = shooterSubsystem;
        this.intakeSubsystem = intakeSubsystem;
    }

    public void execute() {

    }

    // tamper with feeding timing for far distances
    public Command intakeAutoMotion() {
        return new ParallelCommandGroup(
                new PositionCommand(shooterSubsystem, intakeSubsystem, PositionCommand.Position.ARM_GROUND_PICKUP),
                new SequentialCommandGroup(
                        Commands.race(
                            shooterSubsystem.runFeeder().until(shooterSubsystem::hasRing),
                            new WaitCommand(2.5)
                        ),
                        new PositionCommand(shooterSubsystem, intakeSubsystem, PositionCommand.Position.AUTO_BASE)
                )
        );
    }

    public Command shootingAutoMotion() {
        return new ParallelCommandGroup(
                setShooterAutoPos(),
                runFeederWithTimeout()
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
        return shooterSubsystem.runFeeder().withTimeout(2.0);
    }

    public Command setShooterAutoPos() {
        return new PositionCommand(shooterSubsystem, intakeSubsystem, PositionCommand.Position.SAFE_OR_SPEAKER);
    }

    // used to shoot from middle - need to change time
    public Command shootingMiddleAutoMotion() {
        return new SequentialCommandGroup(
                shooterSubsystem.runShooterRoutine(4));
    }


    // used to shoot from against the speaker
    public Command shootingClosestAutoMotion() {
        return new SequentialCommandGroup(
                new PositionCommand(shooterSubsystem, intakeSubsystem, PositionCommand.Position.INTAKE_OUT),
                new PositionCommand(shooterSubsystem, intakeSubsystem, PositionCommand.Position.SPEAKER),
                shooterSubsystem.runShooterRoutine(3.0));
    }

    // used to shoot from far out - need to change time (shooter)
    public Command shootingFarAutoMotion() {
        return shooterSubsystem.runFeeder().withTimeout(2.0);
    }
}