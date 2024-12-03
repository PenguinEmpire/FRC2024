package org.penguinempire.commands;

import org.penguinempire.subsystems.ClimberSubsystem;
import org.penguinempire.subsystems.IntakeSubsystem;
import org.penguinempire.subsystems.ShooterSubsystem;

import edu.wpi.first.wpilibj2.command.Command;

@SuppressWarnings("unused")
public class PositionCommand extends Command {

    // SAFE_OR_SPEAKER: puts the shooter in speaker or safe pos
    // INTAKE_OUT: assumes the arms are out and moves the intake to pickup
    // INTAKE_IN_PICKUP: folds intake into frame for ring to fit
    // INTAKE_IN_SHOOT: moves intake out a bit for ring to shoot
    // BASE: resets arms and shooter (0's them)
    // ARM_GROUND_PICKUP: lifts the arm and shooter a bit for smooth intake
    // FAR_SHOOTING: (used in auto) lifts arms and shooter to be able to shoot from
    // far
    // CLOSE_SHOOTING: (used in auto) does the same thing as above but positions for
    // a bit closer
    // START_POSITION: moves everything to fit in frame
    // game_mode: intake moves to ground and shooter lifts up
    // AMP: arms move up and shooter moves down
    // OUT_OF_AUTO_POSITION: moves arms up a bit for intake to move down as auto
    // starts
    // HOME: puts arm and shooter to 0 and intake to inside frame
    // trap: arms push and shooter moves and deposits

    public enum Position {
        SAFE_OR_SPEAKER,
        SPEAKER,
        INTAKE_OUT,
        INTAKE_IN_PICKUP,
        INTAKE_IN_SHOOT,
        START_POSITION,
        BASE,
        AUTO_BASE,
        TRAP,
        FAR_SHOOTING,
        MIDDLE_SHOOTING,
        ARM_GROUND_PICKUP,
        OUT_OF_AUTO_POSITION,
        AMP,
        HOME
    };

    private ShooterSubsystem shooterSubsystem;
    private IntakeSubsystem intakeSubsystem;
    private ClimberSubsystem climberSubystem;
    private Position pos;

    private int m_ticks = 0;

    public PositionCommand(ShooterSubsystem shooterSubsystem, IntakeSubsystem intakeSubsystem, ClimberSubsystem climberSubsystem, Position pos) {
        this.shooterSubsystem = shooterSubsystem;
        this.intakeSubsystem = intakeSubsystem;
        this.climberSubystem = climberSubsystem;
        this.pos = pos;
    }

    @Override
    public void initialize() {
        m_ticks = 0;
    }

    @Override
    public void execute() {
        m_ticks++;

        switch (pos) {
            case SAFE_OR_SPEAKER:
                shooterSubsystem.setShooterVision();
                shooterSubsystem.setArmPosition(0.0);
                // shooterSubsystem.setShooterPosition(shooterSubsystem.isSafeMode() ? 0.75 : 1.03);
                break;
            case SPEAKER:
                shooterSubsystem.setShooterPosition(1.03);
                break;
            case INTAKE_OUT:
                intakeSubsystem.setPosition(4.12);
                break;
            case INTAKE_IN_PICKUP:
                intakeSubsystem.setPosition(5.87);
                break;
            case START_POSITION:
                if (m_ticks < 15) {
                    shooterSubsystem.setArmPosition(5.6);
                } else if (m_ticks < 60) {
                    intakeSubsystem.setPosition(6.27);
                    shooterSubsystem.setShooterPosition(0.0);
                } else if (m_ticks < 120) {
                    shooterSubsystem.setArmPosition(5.91);
                }
                break;
            case BASE:
                shooterSubsystem.setArmPosition(0.0);
                shooterSubsystem.setShooterPosition(0.0);
                break;
            case ARM_GROUND_PICKUP:
                shooterSubsystem.setArmPosition(6.07);
                shooterSubsystem.setShooterPosition(0.88);
                break;
            case INTAKE_IN_SHOOT:
                intakeSubsystem.setPosition(5.5);
                break;
            case FAR_SHOOTING:
                intakeSubsystem.setPosition(0.0);
                break;
            case MIDDLE_SHOOTING:
                intakeSubsystem.setPosition(0.73);
                break;
            case OUT_OF_AUTO_POSITION:
                if (m_ticks < 15) {
                    shooterSubsystem.setArmPosition(5.75);
                } else if (m_ticks < 60) {
                    intakeSubsystem.setPosition(4.12);
                } else if (m_ticks < 90) {
                    shooterSubsystem.setArmPosition(0);
                }
                break;
            case AMP:
                shooterSubsystem.setArmPosition(4.74);
                shooterSubsystem.setShooterPosition(0.76);
                intakeSubsystem.setPosition(5.23);
                break;
            case HOME:
                shooterSubsystem.setArmPosition(0.0);
                shooterSubsystem.setShooterPosition(0.0);
                intakeSubsystem.setPosition(5.75);
                break;
            case AUTO_BASE:
                shooterSubsystem.setArmPosition(0.0);
                break;
            default:
                break;
        }
    }

    public boolean isFinished() {
        return (pos == Position.TRAP || pos == Position.START_POSITION || pos == Position.OUT_OF_AUTO_POSITION) ? m_ticks > 85
                : true;
    }

}
