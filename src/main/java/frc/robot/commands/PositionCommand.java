package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.IntakeSubsystem;
import frc.robot.subsystems.ShooterSubsystem;

public class PositionCommand extends Command {

    // speaker: puts the shooter in speaker pos
    // intake_out: assumes the arms are out and moves the intake to pickup
    // intake_in: folds intake into frame
    // game_mode: intake moves to ground and shooter lifts up
    // amp: arms move up and shooter moves down
    // trap: arms push and shooter moves and deposits
    // pickup: moves arms and shooter to pickup from the station

    public enum Position {
        SPEAKER,
        INTAKE_OUT,
        INTAKE_IN,
        GAME_MODE,
        AMP,
        TRAP,
        PICKUP
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
            shooterSubsystem.setShooterPosition(0.0);

        } else if (pos == Position.INTAKE_OUT) {
            intakeSubsystem.setPosition(2.188);

        } else if (pos == Position.INTAKE_IN) {
            intakeSubsystem.setPosition(0.0);

        } else if (pos == Position.GAME_MODE) {
            if (m_ticks < 15) {
                intakeSubsystem.setPosition(0.0);

            } else if (m_ticks < 30) {
                shooterSubsystem.setShooterPosition(0.0);

            }
        } else if (pos == Position.AMP) {
            shooterSubsystem.setPosition(0.0);
            shooterSubsystem.setShooterPosition(0.0);

        } else if (pos == Position.TRAP) {
            if (m_ticks < 30) {
                shooterSubsystem.setPosition(0.0);

            } else if (m_ticks < 50) {
                shooterSubsystem.setShooterPosition(0.0);

            }
        } else if (pos == Position.PICKUP) {
            if (m_ticks < 20) {
                shooterSubsystem.setPosition(0.0);

            } else if (m_ticks < 30) {
                shooterSubsystem.setShooterPosition(0.0);

            }
        }
    }

    public boolean isFinished() {
        return (pos == Position.TRAP || pos == Position.GAME_MODE) ? m_ticks > 0
                : true;
    }

}
