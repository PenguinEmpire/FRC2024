package frc.robot.commands;

import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.wpilibj2.command.CommandBase;
import edu.wpi.first.wpilibj2.command.Subsystem;
import edu.wpi.first.math.controller.PIDController;
import physical.ControlInput;
import frc.robot.Constants;
import frc.robot.subsystems.DriveSubsystem;
// import frc.robot.subsystems.VisionSubsystem;
// import frc.robot.subsystems.LightingSubsystem;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 * Swerve drive command that uses a drive subsystem.
 */
public class SwerveDriveCommand extends CommandBase {
    private final DriveSubsystem m_subsystem;
    // private final VisionSubsystem m_vs;
    // private final LightingSubsystem m_ls;
    private final ControlInput m_input;
    private PIDController autoAlignPID;
    private PIDController xPID;
    private PIDController yPID;
    private boolean m_wasTargeting;

    /**
     * Creates a new SwerveDriveCommand.
     *
     * @param subsystem The drive subsystem used by this command.
     * @param input The control inputt
     */
    public SwerveDriveCommand(DriveSubsystem subsystem, ControlInput input) {
        m_subsystem = subsystem;
        m_input = input;

        setName("SwerveDrive");
        autoAlignPID = new PIDController(0.0154, 0, 0);
        xPID = new PIDController(0.02, 0, 0);
        yPID = new PIDController(0.02, 0, 0);
        SmartDashboard.putBoolean("Inverted Pickup", false);
    }

    @Override
    public void initialize() {
        autoAlignPID.enableContinuousInput(0, 360);
        xPID.reset();
        yPID.reset();
        m_wasTargeting = false;
    }

    @Override
    public void execute() {
        // Execution logic goes here
        double forward = -m_input.getLeftJoystick().getRawAxis(1);
        double pow = 2;
        forward = linearDeadband(forward, 0.095);

        forward = Math.copySign(Math.pow(forward, pow), forward);

        double strafe = -m_input.getLeftJoystick().getRawAxis(0);
        strafe = linearDeadband(strafe, 0.095);

        strafe = Math.copySign(Math.pow(strafe, pow), strafe);

        double rotation = m_input.getRightJoystick().getRawAxis(2);
        rotation = Math.copySign(Math.pow(rotation, pow), rotation);
        rotation = linearDeadband(rotation, 0.095);

        boolean isFacingOtherSide = Math.abs(m_subsystem.getAngle()) > 90
                && Math.abs(m_subsystem.getAngle()) < 270;
        // if (isFacingOtherSide) {
        //     m_vs.setCustomPipeline(true);
        //     boolean isInverted = SmartDashboard.getBoolean("Inverted Pickup", false);
        //     if (!isInverted)
        //         m_vs.setPipeline(2);
        //     else
        //         m_vs.setPipeline(3);
        // } else {
        //     m_vs.setCustomPipeline(false);
        // }
        if (m_input.getLeftJoystick().getTrigger()) {
            double angleToGoalDegrees = Constants.Vision.LIMELIGHT_MOUNT_ANGLE_DEGREES + m_vs.getPitch();
            double angleToGoalRadians = Math.toRadians(angleToGoalDegrees);

            double distanceFromLimelightToGoalInches = (Constants.Vision.GOAL_HEIGHT_INCHES
                    - Constants.Vision.LIMELIGHT_LENS_HEIGHT_INCHES) / Math.tan(angleToGoalRadians);
            if (SmartDashboard.getBoolean("Enable Debug", false))
                SmartDashboard.putNumber("target dist", distanceFromLimelightToGoalInches);
            if (m_input.isConeMode())
                m_vs.setLED(true);
            double test = 0;
            if (isFacingOtherSide) {
                test = 180;
                forward *= -1;
            }
            double turnValue = -autoAlignPID.calculate(m_subsystem.getAngle(), test);
            if (!m_vs.hasTargets()) {
                if (m_wasTargeting) {
                    m_subsystem.drive(Units.metersPerSecond(forward * 0.4),
                            Units.metersPerSecond(0),
                            Units.radiansPerSecond(turnValue), false, false);
                }
                return;
            }
            m_wasTargeting = true;
            m_ls.setTemporaryColor(0, 255, 0);
            strafe = -MathUtil.clamp(yPID.calculate(m_vs.getYaw(), 0), -0.3, 0.3);

            m_subsystem.drive(Units.metersPerSecond(forward * 0.4),
                    Units.metersPerSecond(strafe),
                    Units.radiansPerSecond(MathUtil.clamp(MathUtil.clamp(turnValue, -0.4, 0.4),
                            -Constants.Physical.Drive.K_MAX_ANGULAR_VELOCITY,
                            Constants.Physical.Drive.K_MAX_ANGULAR_VELOCITY)),
                    false, false);
        } else {
            if (m_input.isConeMode())
                m_vs.setLED(false);
            m_subsystem.drive(Units.metersPerSecond(forward),
                    Units.metersPerSecond(strafe),
                    Units.radiansPerSecond(MathUtil.clamp(rotation * 0.8,
                            -Constants.Drive.K_MAX_ANGULAR_VELOCITY,
                            Constants.Drive.K_MAX_ANGULAR_VELOCITY)),
                    false, false);
            autoAlignPID.reset();
            yPID.reset();
        }

        if (m_input.getLeftJoystick().getTriggerReleased()) {
            // m_ls.releaseTemporaryColor();
            m_wasTargeting = false;
        }
    }
    

    @Override
    public void end(boolean interrupted) {
        // Cleanup logic goes here
    }

    @Override
    public boolean isFinished() {
        // Return true if the command should end, false if it should continue
        return false;
    }

    private double linearDeadband(double input, double deadband) {
        if (Math.abs(input) < deadband) {
            return 0;
        } else {
            return input - deadband;
        }
    }
}
