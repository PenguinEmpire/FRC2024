package frc.robot.commands.autonomous;

import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.DriveSubsystem;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class TurnCommand extends Command {
    private DriveSubsystem driveSubsystem;
    private double rot;
    private double targetRot;
    private int ticks = 0;
    private PIDController pidController;

    public TurnCommand(DriveSubsystem driveSubsystem, double rot){
        this.driveSubsystem = driveSubsystem;
        this.rot = rot;

        addRequirements(driveSubsystem);
    }
    
    @Override
    public void initialize() {
        targetRot = driveSubsystem.getRawAngle() + rot;
    }

    public boolean hasReached(double tolerance) {
        if ((Math.abs(targetRot - driveSubsystem.getRawAngle())) <= tolerance) {
            return true;
        }
        return false;
    }

    @Override
    public void execute() {
        ticks++;
        driveSubsystem.drive(0, 0, targetRot, true, false);
    }

    @Override
    public void end(boolean interrupted) {
        driveSubsystem.drive(0, 0, 0, false, true);
    }

    @Override
    public boolean isFinished() {
        return hasReached(0.12);
    }


}
