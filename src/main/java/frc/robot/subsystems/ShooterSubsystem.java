package frc.robot.subsystems;

import com.revrobotics.CANSparkMax;
import com.revrobotics.RelativeEncoder;
import com.revrobotics.SparkPIDController;
import com.revrobotics.CANSparkBase.IdleMode;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.CommandScheduler;
import edu.wpi.first.wpilibj2.command.Commands;
import edu.wpi.first.wpilibj2.command.ParallelCommandGroup;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import edu.wpi.first.wpilibj2.command.WaitCommand;
import frc.robot.ControlInput;
import frc.robot.module.Joint;

import com.revrobotics.ColorSensorV3;
import com.revrobotics.ColorMatchResult;
import com.revrobotics.ColorMatch;
import edu.wpi.first.wpilibj.util.Color; 
import edu.wpi.first.wpilibj.I2C;


public class ShooterSubsystem extends SubsystemBase {

    private final CANSparkMax feederMotor;
    // used for output rollers
    private final CANSparkMax shooterMotor;
// change
    private final I2C.Port i2cPort = I2C.Port.kOnboard;
    private final ColorSensorV3  m_colorSensor = new ColorSensorV3(i2cPort);
    private final ColorMatch m_colorMatcher = new ColorMatch();
    private final Color kOrangeTarget = new Color(0.08, 0.355, 0.563);
    private final Color kBaseTarget = new Color(64 / 255, 63 / 255, 62 / 255);
    private Joint arm;
    private Joint shooter;
    //  private DigitalInput proximitySensor;
    private DigitalInput ColorSensor;
    private double intakeFeederSpeed;
    private double shooterSpeed;

    private VisionSubsystem visionSubsystem;
    private LightingSubsystem lightingSubystem;

    private SparkPIDController shooterPIDController;
    private RelativeEncoder shooterEncoder;

    private double shooterOffset;

    private ControlInput controlInput;

    private boolean continuousRun;

    public ShooterSubsystem(int feederID, int shooterID, ControlInput controlInput, VisionSubsystem vs,
            LightingSubsystem ls) {
        arm = new Joint("shooterArm", 11, 0.7, 0, 0, 0, 0, -0.25, 0.25, true, null, 0, false);
        shooter = new Joint("shooterEnt", 20, 0.95, 0.01, 0.2, 0, 0, -0.3, 0.3, false, null, 0, false);

        feederMotor = new CANSparkMax(feederID, CANSparkMax.MotorType.kBrushless);
        shooterMotor = new CANSparkMax(shooterID, CANSparkMax.MotorType.kBrushless);
        shooterMotor.setIdleMode(IdleMode.kBrake);
        shooterEncoder = shooterMotor.getEncoder();
        shooterPIDController = shooterMotor.getPIDController();
        m_colorSensor.configureColorSensor(ColorSensorV3.ColorSensorResolution.kColorSensorRes16bit, ColorSensorV3.ColorSensorMeasurementRate.kColorRate50ms, ColorSensorV3.GainFactor.kGain3x);
        m_colorMatcher.setConfidenceThreshold(85);
        m_colorMatcher.addColorMatch(kOrangeTarget);
        m_colorMatcher.addColorMatch(kBaseTarget);

        SmartDashboard.putNumber("Shooter Speed", 1);
        SmartDashboard.putNumber("Intake Feeder Speed", 0.8);
        SmartDashboard.putNumber("Shooter Angle Offset", 0.05);

        this.lightingSubystem = ls;
        this.visionSubsystem = vs;

        this.controlInput = controlInput;
    }

    @Override
    public void periodic() {
        intakeFeederSpeed = SmartDashboard.getNumber("Intake Feeder Speed", 0.8);
        shooterSpeed = SmartDashboard.getNumber("Shooter Speed", 1);
        shooterOffset = SmartDashboard.getNumber("Shooter Angle Offset", 0.05);

        SmartDashboard.putBoolean("Has Ring", hasRing());
        SmartDashboard.putNumber("Shooter RPM", (shooterEncoder.getVelocity() / 5676) * 100);
        arm.periodic();
        shooter.periodic();
        lightingSubystem.setPulsing(hasRing());
      

        if (continuousRun) {
            setShooterVision();
        }
    }

    public Command setContinuousRun(boolean state) {
        return Commands.runOnce(
                () -> continuousRun = state);
    }

    public boolean hasRing() {
    
        final Color detectedColor = m_colorSensor.getColor();
        final ColorMatchResult match = m_colorMatcher.matchClosestColor(detectedColor);
        return match.color != kOrangeTarget;
    }

    // these methods are for legacy setting the intake speed, do not use these if
    // possible
    // public Command setIntakeState(boolean enabled) {
    // return Commands.runOnce(() -> feederMotor.set(enabled ? feederSpeed : 0));
    // }

    // public Command setShooterState(boolean enabled) {
    // return Commands.runOnce(() -> feederMotor.set(enabled ? shooterSpeed : 0));
    // }

    public Command runFeeder() {
        return Commands.runEnd(
                () -> feederMotor.set(intakeFeederSpeed),
                () -> feederMotor.set(0));
    }

    public Command reverseFeeder() {
        return Commands.runEnd(
                () -> feederMotor.set(-1),
                () -> feederMotor.set(0));
    }

    public Command runShooter() {
        return Commands.runEnd(
                () -> shooterMotor.set(shooterSpeed),
                () -> shooterMotor.set(0));

    }

    public Command startShooter() {
        return Commands.runOnce(
                () -> shooterMotor.set(shooterSpeed));
    }

    public Command endShooter() {
        return Commands.runOnce(
                () -> shooterMotor.set(0.0));
    }

    public Command endFeeder() {
        return Commands.runOnce(
            () -> feederMotor.set(0.0)
        );
    }

    // need to tune timings
    public Command runAmpShooterRoutine() {
        return new ParallelCommandGroup(
                runShooter().withTimeout(1),
                runFeeder().withTimeout(0.5));
    }
 
    /*
     * for auto -
     * close shooting (speaker): 3 seconds
     * middle shooting (during auto): 4 seconds
     * far shooting (during auto): 6 seconds
     */

    /*
     * for teleop (probably but i want to have speaker and safe same time) -
     * speaker: 3 seconds
     * safe: 5 seconds
     */
    public Command runShooterRoutine(double runTime) {
        return new ParallelCommandGroup(
                runShooter().withTimeout(runTime),
                new SequentialCommandGroup(
                        new WaitCommand(runTime - 1),
                        runFeeder().withTimeout(0.5)));
    }

    public void setArmPosition(double pos) {
        arm.setPosition(pos);
    }

    public void setShooterPosition(double pos) {
        shooter.setPosition(pos);
    }

    public void setShooterVision() {
        if (visionSubsystem.hasTargets()) {
            double x = visionSubsystem.getY();
            double output = (-0.00015152 * Math.pow(x, 2)) + (0.0126955 * x) + 0.904188;
            setShooterPosition(output + shooterOffset);
        }
    }

    public boolean isSafeMode() {
        return controlInput.isSafeMode();
    }
}
