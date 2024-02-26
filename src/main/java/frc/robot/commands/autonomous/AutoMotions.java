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

    private final DigitalInput sensor;
    private final BooleanSupplier sensorBooleanSupplier;
    private final BooleanSupplier negSensorBooleanSupplier;

    private enum Sequence {
        INTAKE_IN_POS,
        AMP_POS
    }

    public AutoMotions(ShooterSubsystem shooterSubsystem, IntakeSubsystem intakeSubsystem) {
        this.shooterSubsystem = shooterSubsystem;
        this.intakeSubsystem = intakeSubsystem;

        sensor = new DigitalInput(0);
        sensorBooleanSupplier = () -> !sensor.get();
        negSensorBooleanSupplier = () -> sensor.get();
    }

    @Override
    public void execute() {
        if (sequence == Sequence.INTAKE_IN_POS) {
            intakeSubsystem.setPosition(0.0);
        }

        // else if (sequence == Sequence.AMP_POS) {
        // shooterSubsystem.setShooterPosition(0.0);
        // }
    }

    public Command intakeAutoMotion() {
        return new ParallelCommandGroup(
                new PositionCommand(shooterSubsystem, intakeSubsystem, PositionCommand.Position.INTAKE_OUT),
                new PositionCommand(shooterSubsystem, intakeSubsystem, PositionCommand.Position.ARM_GROUND_PICKUP),
                new SequentialCommandGroup(new WaitCommand(0.1),
                        new ParallelCommandGroup(
                                shooterSubsystem.runFeeder().until(sensorBooleanSupplier),
                                intakeSubsystem.runRollers().until(sensorBooleanSupplier)),
                        shooterSubsystem.runFeeder().withTimeout(0.25),
                        new PositionCommand(shooterSubsystem, intakeSubsystem,
                                PositionCommand.Position.INTAKE_IN_PICKUP),
                        new PositionCommand(shooterSubsystem, intakeSubsystem, PositionCommand.Position.BASE),
                        new WaitCommand(1),
                        shooterSubsystem.reverseFeeder().until(sensorBooleanSupplier),
                        shooterSubsystem.reverseFeeder().until(negSensorBooleanSupplier)));
    }
}