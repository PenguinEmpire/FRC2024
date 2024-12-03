package frc.robot.commands.autonomous;

import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.DriveSubsystem;
import frc.robot.subsystems.VisionSubsystem;

public class MoveCommandVision extends Command {
    private final VisionSubsystem v_subsystem;
    private final DriveSubsystem m_subsystem;

    private final PIDController turnPID;

    public MoveCommandVision(DriveSubsystem subsystem, boolean keepVelocity, double maxSpeed, VisionSubsystem vsubsystem) {
        m_subsystem = subsystem;
        v_subsystem = vsubsystem;
        turnPID = new PIDController(0.08, 0, 0);
        addRequirements(m_subsystem);
    }

    @Override
    public void initialize() {
        turnPID.reset();
    }

    public double dclamp(double x, double r) {
        return x > r ? r : (x < -r ? -r : x);
    }

    @Override
    public void execute() {
        m_subsystem.drive(0, 0, getAutoTargetingHeading(), false, false);
    }


    public double getAutoTargetingHeading() {
        if (v_subsystem.hasTargets()) {
            double distanceRotFromTarget = v_subsystem.getX();
            turnPID.setSetpoint(0);
            final double rotPIDVal = dclamp(turnPID.calculate(distanceRotFromTarget), 0.5);

            // if the targets exist and the distance is accurate but the robot still goes
            // away from the target, invert this.
            boolean pidInvert = true;
            double rotation = (pidInvert ? -1 : 1) * rotPIDVal;
            return rotation;
        } else {
            double rotation = 0.5;
            return rotation;
        }
    }

    @Override
    public void end(boolean interrupted) {
        m_subsystem.drive(0, 0, 0, false, false);
    }

    @Override
    public boolean isFinished() {
        return turnPID.getPositionError() < 0.2;
    }
}
