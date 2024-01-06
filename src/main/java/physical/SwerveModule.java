package physical;

import frc.robot.Constants;

import com.revrobotics.CANSparkMax;
import com.revrobotics.SparkMaxPIDController;
import com.revrobotics.SparkMaxRelativeEncoder;
import com.revrobotics.CANSparkMax.ControlType;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;
import edu.wpi.first.wpilibj.AnalogInput;
import edu.wpi.first.math.MathUtil;
import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.math.kinematics.SwerveModuleState;
import edu.wpi.first.wpilibj.AnalogEncoder;

import java.util.Objects;

public class SwerveModule {
    private final String name;
    private final CANSparkMax driveMotor, turningMotor;
    private SparkMaxRelativeEncoder driveEncoder, turningEncoder;
    private final AnalogEncoder analogEncoder;
    private final SparkMaxPIDController drivePIDController, turningPIDController;
    private final Translation2d alignmentOffset;
    private double analogOffset, encoderAngle, currentVelocity, targetAngle, targetVelocity;

    public SwerveModule(String name, int driveMotorCanID, int turnMotorCanID, int analogEncoderPort,
                        Translation2d alignmentOffset, double angleOffset) {
        this.name = name;
        this.driveMotor = new CANSparkMax(driveMotorCanID, MotorType.kBrushless);
        this.turningMotor = new CANSparkMax(turnMotorCanID, MotorType.kBrushless);
        this.analogEncoder = new AnalogEncoder(analogEncoderPort);
        this.drivePIDController = driveMotor.getPIDController();
        this.turningPIDController = turningMotor.getPIDController();
        this.alignmentOffset = alignmentOffset;
        this.analogOffset = angleOffset;

        analogEncoder.setDistancePerRotation(Constants.Drive.DISTANCE_PER_Rotation);
        turningMotor.setSmartCurrentLimit(20);
        driveMotor.setSmartCurrentLimit(35);
        turningMotor.setSecondaryCurrentLimit(30);
        driveMotor.setSecondaryCurrentLimit(40);

        driveEncoder.setPosition(0);
        driveEncoder.setPositionConversionFactor(Constants.Drive.DRIVE_CONVERSION);
        driveEncoder.setVelocityConversionFactor(Constants.Drive.DRIVE_CONVERSION / 60);
        turningPIDController.setP(1.5);
        turningPIDController.setI(0);
        turningPIDController.setD(0.5);

        readHardware();
    }

    public void realignWheel() {
        turningEncoder.setPosition(getAnalogEncoderAngle());
    }

    public void updateAnalogOffset(double offset) {
        analogOffset = offset;
    }

    public double getAnalogOffset() {
        return analogOffset;
    }

    public double getAnalogEncoderAngle() {
        double angle = (1.0 - analogEncoder.getAbsolutePosition()) * (2.0 * Math.PI);
        angle += analogOffset;
        return angle;
    }

    public double getRawAngle() {
        double angle = (1.0 - analogEncoder.getAbsolutePosition()) * (2.0 * Math.PI);
        return (angle % (2.0 * Math.PI) + 3.1415926535) % (2.0 * Math.PI) - 3.1415926535;
    }

    public double getRawAnalogAngle() {
        double angle = (1.0 - analogEncoder.getAbsolutePosition()) * (2.0 * Math.PI);
        return angle;
    }

    public double getRawAngleDouble() {
        return getRawAngle();
    }

    public void setTargetState(SwerveModuleState state) {
      targetVelocity = state.speedMetersPerSecond;
      targetAngle = state.angle.getRadians();
    }

    public void readHardware() {
        encoderAngle = getAnalogEncoderAngle();
        currentVelocity = driveEncoder.getVelocity();
    }

    public void stopMotion() {
        driveMotor.set(0);
    }

    public void moveTowardsTarget() {
        double targetSpeed = targetVelocity;
        double targetAngle = this.targetAngle;

        double currentAngle = turningEncoder.getPosition();
        double currentAngleMod = (currentAngle % (2.0 * Math.PI) + 2.0 * Math.PI) % (2.0 * Math.PI);

        double newTarget = targetAngle + currentAngle - currentAngleMod;
        if (targetAngle - currentAngleMod > Math.PI) {
            newTarget -= 2.0 * Math.PI;
        } else if (targetAngle - currentAngleMod < -Math.PI) {
            newTarget += 2.0 * Math.PI;
        }

        driveMotor.set(targetSpeed);
        turningPIDController.setReference(newTarget, ControlType.kPosition);
    }
}
