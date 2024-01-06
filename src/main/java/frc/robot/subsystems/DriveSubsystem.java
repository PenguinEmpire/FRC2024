package frc.robot.subsystems;

import com.kauailabs.navx.frc.AHRS;
import edu.wpi.first.wpilibj.I2C.Port;

import frc.robot.Constants;

import edu.wpi.first.wpilibj.SerialPort;
import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.kinematics.SwerveDriveKinematics;
import edu.wpi.first.math.kinematics.SwerveDriveOdometry;
import edu.wpi.first.math.kinematics.SwerveModulePosition;
import edu.wpi.first.wpilibj.smartdashboard.Field2d;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import physical.SwerveModule;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class DriveSubsystem extends SubsystemBase {

    private final Field2d field;
    private final AHRS navX;
    private final SwerveDriveKinematics kinematics;
    private final SwerveModule frontLeftModule;
    private final SwerveModule frontRightModule;
    private final SwerveModule backLeftModule;
    private final SwerveModule backRightModule;
    // private final SwerveDriveOdometry odometry;

    private double rotateToAngleRate;
    private final PIDController turnController;
    private Pose2d m_location;
    private int m_ticks;
    private boolean driveEnabled;

    // Constants
    private static final double kP = 0.0134;
    private static final double kI = 0.00;
    private static final double kD = 0.00;
    private static final double kF = 0.00;
    private static final double kToleranceDegrees = 2.0;



    public DriveSubsystem() {
        field = new Field2d();
        navX = new AHRS(SerialPort.Port.kUSB);
        

        frontLeftModule = new SwerveModule(
            "FrontLeft",
            Constants.Drive.Module.FRONTLEFT.getDriveMotorCanID(),
            Constants.Drive.Module.FRONTLEFT.getTurnMotorCanID(),
            Constants.Drive.Module.FRONTLEFT.getAnalogEncoderPort(),
            Constants.Drive.Module.FRONTLEFT.getLocation(),
            Constants.Drive.Module.FRONTLEFT.getTurnEncoderOffset());

        frontRightModule = new SwerveModule(
            "FrontRight",
            Constants.Drive.Module.FRONTRIGHT.getDriveMotorCanID(),
            Constants.Drive.Module.FRONTRIGHT.getTurnMotorCanID(),
            Constants.Drive.Module.FRONTRIGHT.getAnalogEncoderPort(),
            Constants.Drive.Module.FRONTRIGHT.getLocation(),
            Constants.Drive.Module.FRONTRIGHT.getTurnEncoderOffset());
        backLeftModule = new SwerveModule(
            "BackLeft",
            Constants.Drive.Module.BACKLEFT.getDriveMotorCanID(),
            Constants.Drive.Module.BACKLEFT.getTurnMotorCanID(),
            Constants.Drive.Module.BACKLEFT.getAnalogEncoderPort(),
            Constants.Drive.Module.BACKLEFT.getLocation(),
            Constants.Drive.Module.BACKLEFT.getTurnEncoderOffset());
        backRightModule = new SwerveModule(
            "BackRight",
            Constants.Drive.Module.BACKRIGHT.getDriveMotorCanID(),
            Constants.Drive.Module.BACKRIGHT.getTurnMotorCanID(),
            Constants.Drive.Module.BACKRIGHT.getAnalogEncoderPort(),
            Constants.Drive.Module.BACKRIGHT.getLocation(),
            Constants.Drive.Module.BACKRIGHT.getTurnEncoderOffset());

        kinematics = new SwerveDriveKinematics(
                Constants.Drive.Module.FRONTLEFT.getLocation(),
                Constants.Drive.Module.FRONTRIGHT.getLocation(),
                Constants.Drive.Module.BACKLEFT.getLocation(),
                Constants.Drive.Module.BACKRIGHT.getLocation());  


        // SwerveDriveOdometry odometry = new SwerveDriveOdometry(m_kinematics, getAngle2d(),
        //         new SwerveModulePosition[]{m_frontLeftModule.getPosition(), m_frontRightModule.getPosition(),
        //                 m_backLeftModule.getPosition(), m_backRightModule.getPosition()});

        turnController = new PIDController(kP, kI, kD);
        turnController.setIntegratorRange(-6.283, 6.283);

        SmartDashboard.putData("Rot", turnController);
        SmartDashboard.putData("Field Test", field);
        resetGyroscope();
    }

    public void resetGyroscope() {
        navX.reset();
    }

    // public void resetOdometry(Pose2d startPos) {
    //     odometry.resetPosition(getAngle2d(),
    //             new Pose2d(m_frontLeftModule.getPosition(), m_frontRightModule.getPosition(),
    //                     m_backLeftModule.getPosition(), m_backRightModule.getPosition()),
    //             startPos);
    // }

    public void periodic() {
        m_ticks++;
        if (m_ticks > 5) {
            updateDashboard();
            m_ticks = 0;
        }
        if (driveEnabled) {
            advanceSubsystem();
        }
    }

    public void realignWeels() {
        frontLeftModule.realignWheel();
        frontRightModule.realignWheel();
        backLeftModule.realignWheel();
        backRightModule.realignWheel();
    }

    public void advanceSubsystem() {
        frontLeftModule.readHardware();
        frontRightModule.readHardware();
        backLeftModule.readHardware();
        backRightModule.readHardware();

        frontLeftModule.moveTowardsTarget();
        frontRightModule.moveTowardsTarget();
        backLeftModule.moveTowardsTarget();
        backRightModule.moveTowardsTarget();
    }

    public void hardStop() {
        frontLeftModule.stopMotion();
        frontRightModule.stopMotion();
        backLeftModule.stopMotion();
        backRightModule.stopMotion();
    }
    

    public void updateDashboard() {
        // ... (similar to C++ implementation)
    }

    public double getAngle() {
        return (-navX.getAngle() + 180) % 360;
    }

    // Helper method to convert rotation angle to Rotation2d
    private Rotation2d getAngle2d() {
        return Rotation2d.fromDegrees(-navX.getAngle() + 180);
    }
}
