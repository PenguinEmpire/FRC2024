package frc.robot.commands;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.DriveSubsystem;

public class AlignmentCommand extends Command {
    private DriveSubsystem driveSubsystem;
    
    public AlignmentCommand (DriveSubsystem driveSubsystem){
        this.driveSubsystem = driveSubsystem;
        addRequirements(this.driveSubsystem);
        setName("Alignment Command");
    }

    @Override
    public void initialize() {
        this.driveSubsystem.resetGyroscope();
        this.driveSubsystem.resetOdometry(new Pose2d());
        this.driveSubsystem.getNavX().setAngleAdjustment(0);
    }

    @Override
    public void execute(){
        this.driveSubsystem.drive(0.0, 0.0, 0.0, false, false);
    }

    @Override
    public void end(boolean interrupted){

    }

    @Override
    public boolean isFinished(){
        return true;
    }


    
}
