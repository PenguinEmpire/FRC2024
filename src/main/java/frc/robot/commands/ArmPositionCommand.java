package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.Command;

public class ArmPositionCommand extends Command {
    private ArmSubsystem m_subsystem;
    private Position m_pos;
    private int m_ticks;

    public ArmPositionCommand(ArmSubsystem subsystem, Position pos) {
        this.m_subsystem = subsystem;
        this.m_pos = pos;
    }

    @Override
    public void initialize() {
        m_ticks = 0;
    }

    @Override
    public void execute() {
        m_ticks++;

        if (m_pos == Position.GROUND) {
            // Your GROUND logic here
        } else if (m_pos == Position.MID) {
            // Your MID logic here
        } else if (m_pos == Position.HIGH) {
            // Your HIGH logic here
        } else if (m_pos == Position.PICKUP) {
            // Your PICKUP logic here
        } else if (m_pos == Position.CHECKUP) {
            // Your CHECKUP logic here
        } else if (m_pos == Position.HOME) {
            // Your HOME logic here
        }
    }

    @Override
    public void end(boolean interrupted) {
        // Your end logic here
    }

    @Override
    public boolean isFinished() {
        return (m_pos == Position.HOME || m_pos == Position.CHECKUP) ? m_ticks > 85 : true;
    }
}