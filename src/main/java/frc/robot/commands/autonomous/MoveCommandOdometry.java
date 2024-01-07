package frc.robot.commands.autonomous;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.DriveSubsystem;
import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class MoveCommandOdometry extends Command {
    private DriveSubsystem driveSubsystem;
    private Pose2d targetPosition;
    private PIDController xPID;
    private PIDController yPID;
    private PIDController turnPID;

    private double ticks = 0;
    private boolean keepVelocity;
    private double maxSpeed;

    public MoveCommandOdometry(DriveSubsystem driveSubsystem, Pose2d targetPosition, boolean keepVelocity, double maxSpeed){
        this.driveSubsystem = driveSubsystem;
        this.targetPosition = targetPosition;
        this.keepVelocity = false;
        this.maxSpeed = 0.5;

        xPID = new PIDController(1.35, 0, 0);
        yPID = new PIDController(1.35, 0, 0);
        turnPID = new PIDController(0.0154, 0, 0);
        addRequirements(driveSubsystem);
    }

    @Override
    public void initialize() {
        xPID.reset();
        yPID.reset();
        turnPID.reset();

        xPID.setTolerance(0.02, 0.2);
        yPID.setTolerance(0.02, 0.2);
        turnPID.setTolerance(0.02, 0.2);
        turnPID.enableContinuousInput(-180, 180);

        ticks = 0;
    }

    @Override
    public void execute(){
        ticks++;
        Pose2d currentPose = driveSubsystem.getPosition();
        

        double xValue = xPID.calculate(currentPose.getX(), targetPosition.getX());
        double yValue = yPID.calculate(currentPose.getY(), targetPosition.getY());
        double turnValue = -turnPID.calculate(currentPose.getRotation().getDegrees(), targetPosition.getRotation().getDegrees());
        
        double tickLength = 20;

        if(ticks > tickLength) ticks = tickLength;

        double maxDrive = maxSpeed * (ticks / tickLength);
        double maxRot = 0.4 * (ticks / tickLength);

        driveSubsystem.drive(clamp(xValue, -maxDrive, maxDrive), clamp(yValue, -maxDrive, maxDrive), clamp(turnValue, -maxRot, maxRot), true, false);
    }
   
    @Override
    public void end (boolean interrupted){
        driveSubsystem.drive(0, 0, 0, false, true);
    }

    @Override
    public boolean isFinished (){
        double posError = keepVelocity ? 0.02 : 0.009;
        double velError = keepVelocity ? 9999 : 0.1;
        if(
            (xPID.getPositionError() < posError && Math.abs(xPID.getVelocityError()) < velError) &&
            (yPID.getPositionError() < posError && Math.abs(yPID.getVelocityError()) < velError)
        
        ) {
            System.out.println("-----------------Completed Command------------------" );
            return true;
        }

        return false;
    }

    public double clamp(double value, double min, double max) {
        return Math.max(min, Math.min(max, value));
    }



}




    
