package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.IntakeSubsystem;
import frc.robot.subsystems.ShooterSubsystem;

public class PositionCommand extends Command {

    // SPEAKER: puts the shooter in speaker pos
    // INTAKE_OUT: assumes the arms are out and moves the intake to pickup
    // INTAKE_IN_PICKUP: folds intake into frame for ring to fit
    // INTAKE_IN_SHOOT: moves intake out a bit for ring to shoot
    // BASE: resets arms and shooter (0's them)
    // ARM_GROUND_PICKUP: lifts the arm and shooter a bit for smooth intake
    // game_mode: intake moves to ground and shooter lifts up
    // amp: arms move up and shooter moves down
    // trap: arms push and shooter moves and deposits
    // pickup: moves arms and shooter to pickup from the station

    public enum Position {
        SPEAKER,
        INTAKE_OUT,
        INTAKE_IN_PICKUP,
        INTAKE_IN_SHOOT,
        GAME_MODE,
        BASE,
        TRAP,
        PICKUP,
        ARM_GROUND_PICKUP,

    };

    private ShooterSubsystem shooterSubsystem;
    private IntakeSubsystem intakeSubsystem;
    private Position pos;

    private int m_ticks = 0;

    public PositionCommand(ShooterSubsystem shooterSubsystem, IntakeSubsystem intakeSubsystem, Position pos) {
        this.shooterSubsystem = shooterSubsystem;
        this.intakeSubsystem = intakeSubsystem;
        this.pos = pos;
    }

    @Override
    public void initialize() {
        m_ticks = 0;
    }

    @Override
    public void execute() {
        m_ticks++;

        if (pos == Position.SPEAKER) {
            shooterSubsystem.setShooterPosition(0.87);
        } else if (pos == Position.INTAKE_OUT) {
            intakeSubsystem.setPosition(4.12);

        } else if (pos == Position.INTAKE_IN_PICKUP) {
            intakeSubsystem.setPosition(5.87);

        } else if (pos == Position.GAME_MODE) {
            if (m_ticks < 15) {
                intakeSubsystem.setPosition(0.0);

            } else if (m_ticks < 30) {
                shooterSubsystem.setShooterPosition(0.0);

            }
        } else if (pos == Position.BASE) {
            shooterSubsystem.setArmPosition(0.0);
            shooterSubsystem.setShooterPosition(0.0);

        } else if (pos == Position.ARM_GROUND_PICKUP) {
            shooterSubsystem.setArmPosition(6.07);
            shooterSubsystem.setShooterPosition(0.95);
        } else if (pos == Position.INTAKE_IN_SHOOT){
            intakeSubsystem.setPosition(5.23);
        }
    }

    public boolean isFinished() {
        return (pos == Position.TRAP || pos == Position.GAME_MODE) ? m_ticks > 85
                : true;
    }

}
