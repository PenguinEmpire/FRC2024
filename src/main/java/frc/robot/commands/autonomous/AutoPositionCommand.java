package frc.robot.commands.autonomous;

import javax.sound.midi.Sequence;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.IntakeSubsystem;
import frc.robot.subsystems.ShooterSubsystem;

public class AutoPositionCommand extends Command {
    private ShooterSubsystem shooterSubsystem;
    private IntakeSubsystem intakeSubsystem;

    private Sequence sequence;

    private enum Sequence {
        INTAKE_IN_POS,
        AMP_POS
    }

    public AutoPositionCommand(ShooterSubsystem shooterSubysstem, IntakeSubsystem intakeSubsystem) {
        this.shooterSubsystem = shooterSubsystem;
        this.intakeSubsystem = intakeSubsystem;
    }

    @Override
    public void execute() {
        if (sequence == Sequence.INTAKE_IN_POS) {
            intakeSubsystem.setPosition(0.0);
        } 
        
        // else if (sequence == Sequence.AMP_POS) {
        //     shooterSubsystem.setShooterPosition(0.0);
        // }
    }
}