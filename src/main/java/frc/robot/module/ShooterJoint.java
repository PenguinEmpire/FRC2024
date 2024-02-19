package frc.robot.module;

import com.revrobotics.SparkPIDController;
import com.revrobotics.CANSparkBase.ControlType;
import com.revrobotics.SparkAbsoluteEncoder.Type;
import com.revrobotics.AbsoluteEncoder;
import com.revrobotics.CANSparkMax;
import com.revrobotics.RelativeEncoder;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.module.ShooterJoint;

public class ShooterJoint {

    private String name;

    private CANSparkMax armMotor;
    private final SparkPIDController armPIDController;
    private final AbsoluteEncoder armEncoder; 
    private final RelativeEncoder relativeEncoder;
    
    private double offset;
    private double targetPos;
    private double currentPos;
    // need to tune PID, set pid constants in rev hardware client

    private double armP = 0.200;
    private double armI = 0.0;
    private double armD = 0.0;
    private double armIz = 0.0;
    private double armFF = 0.0;
    private double armMaxOutput = 0.6;
    private double armMinOutput = -0.6;

    public ShooterJoint(String name, int armSparkID, double offset) {
        this.name = name;
        armMotor = new CANSparkMax(armSparkID, CANSparkMax.MotorType.kBrushless);
        armPIDController = armMotor.getPIDController();
        armEncoder =  armMotor.getAbsoluteEncoder(Type.kDutyCycle);
        relativeEncoder = armMotor.getEncoder();
        armEncoder.setPositionConversionFactor(2 * Math.PI);

        relativeEncoder.setPositionConversionFactor((24/16) * (84/16) * (84/16) * (84/16) * 2 * Math.PI);

        
        // relativeEncoder.setPosition(armEncoder.getPosition());
        // 
        // armPIDController.setFeedbackDevice(armEncoder);
        
        this.offset = offset; 
        SmartDashboard.putNumber("armP", armP);
        SmartDashboard.putNumber("armSetReference", 0);

        armPIDController.setP(armP);
        armPIDController.setI(armI);
        armPIDController.setD(armD);
        armPIDController.setIZone(armIz);
        armPIDController.setFF(armFF);
        armPIDController.setOutputRange(armMinOutput, armMaxOutput);
    }

    public void periodic() {
        SmartDashboard.putNumber("armabsencoder", armEncoder.getPosition());
        currentPos = armEncoder.getPosition();

        double amrPValue = SmartDashboard.getNumber("armP", 0);
        if (amrPValue != armP) {
            armPIDController.setP(amrPValue);
        }

        double armSetRef = SmartDashboard.getNumber("armSetReference", 0);
        armPIDController.setReference(armSetRef, ControlType.kPosition);

    }

    public double getOffset() {
        return this.offset;
    }

    public boolean hasReachedTarget(double tolerance){
        return Math.abs(targetPos - currentPos) <= tolerance;
    }

    public double getPosition() {
        return armEncoder.getPosition();
    }
    
    public void setArmPosition(double position) {
       
    }
}