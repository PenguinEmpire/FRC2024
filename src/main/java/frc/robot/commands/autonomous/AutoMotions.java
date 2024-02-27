package frc.robot.commands.autonomous;

import java.util.function.BooleanSupplier;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.ParallelCommandGroup;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.WaitCommand;
import frc.robot.commands.PositionCommand;
import frc.robot.subsystems.IntakeSubsystem;
import frc.robot.subsystems.ShooterSubsystem;

public class AutoMotions extends Command {
    private ShooterSubsystem shooterSubsystem;
    private IntakeSubsystem intakeSubsystem;
    private Sequence sequence;

    private enum Sequence {
        INTAKE_IN_POS,
        AMP_POS
    }

    public AutoMotions(ShooterSubsystem shooterSubsystem, IntakeSubsystem intakeSubsystem) {
        this.shooterSubsystem = shooterSubsystem;
        this.intakeSubsystem = intakeSubsystem;
    }

    public void execute() {

    }

    public Command intakeAutoMotion() {
        return new ParallelCommandGroup(
                new PositionCommand(shooterSubsystem, intakeSubsystem, PositionCommand.Position.INTAKE_OUT),
                new PositionCommand(shooterSubsystem, intakeSubsystem, PositionCommand.Position.ARM_GROUND_PICKUP),
                new SequentialCommandGroup(
                        new WaitCommand(1.5),
                        new ParallelCommandGroup(
                                shooterSubsystem.runFeeder().onlyWhile(shooterSubsystem::hasRing),
                                intakeSubsystem.runRollers().onlyWhile(shooterSubsystem::hasRing)),
                        shooterSubsystem.runFeeder().withTimeout(0.25),
                        new PositionCommand(shooterSubsystem, intakeSubsystem,
                                PositionCommand.Position.INTAKE_IN_PICKUP),
                        new PositionCommand(shooterSubsystem, intakeSubsystem, PositionCommand.Position.BASE),
                        new WaitCommand(1),
                        shooterSubsystem.reverseFeeder().until(shooterSubsystem::hasRing),
                        shooterSubsystem.reverseFeeder().onlyWhile(shooterSubsystem::hasRing)));

    }

    // used to shoot from middle
    public Command shootingMiddleAutoMotion() {
        return new SequentialCommandGroup(
                new PositionCommand(shooterSubsystem, intakeSubsystem, PositionCommand.Position.INTAKE_IN_SHOOT),
                new PositionCommand(shooterSubsystem, intakeSubsystem, PositionCommand.Position.SAFE_OR_SPEAKER),
                shooterSubsystem.runFarShooterRoutine());
    }

    // used to shoot from against the speaker
    public Command shootingClosestAutoMotion() {
        return new SequentialCommandGroup(
                new PositionCommand(shooterSubsystem, intakeSubsystem, PositionCommand.Position.INTAKE_OUT),
                new PositionCommand(shooterSubsystem, intakeSubsystem, PositionCommand.Position.SAFE_OR_SPEAKER),
                shooterSubsystem.runCloseShooterRoutine());
    }

    // used to shoot from far out
    public Command shootingFarAutoMotion() {
        return new SequentialCommandGroup(
                new PositionCommand(shooterSubsystem, intakeSubsystem, PositionCommand.Position.INTAKE_OUT),
                new PositionCommand(shooterSubsystem, intakeSubsystem, PositionCommand.Position.FAR_SHOOTING),
                shooterSubsystem.runFarShooterRoutine());
    }
}