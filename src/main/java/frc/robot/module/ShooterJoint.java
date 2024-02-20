package frc.robot.module;

import com.revrobotics.SparkPIDController;
import com.revrobotics.CANSparkBase.ControlType;
import com.revrobotics.SparkAbsoluteEncoder.Type;
import com.revrobotics.AbsoluteEncoder;
import com.revrobotics.CANSparkMax;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.module.ShooterJoint;

public class ShooterJoint {

    private String name;

    private CANSparkMax armMotor;
    private final SparkPIDController armPIDController;
    private final AbsoluteEncoder armEncoder; 
    
    private double offset;
    private double targetPos;
    private double currentPos;
    // need to tune PID, set pid constants in rev hardware client

    private double armP = 0.200;
    private double armI = 0.0;
    private double armD = 0.0;
    private double armIz = 0.0;
    private double armFF = 0.0;
    private double armMaxOutput = 0.1;
    private double armMinOutput = -0.1;

    public ShooterJoint(String name, int armSparkID, double offset) {
        this.name = name;
        armMotor = new CANSparkMax(armSparkID, CANSparkMax.MotorType.kBrushless);
        armPIDController = armMotor.getPIDController();
        armEncoder =  armMotor.getAbsoluteEncoder(Type.kDutyCycle);
        armEncoder.setPositionConversionFactor(2 * Math.PI);
        // armPIDController.setFeedbackDevice(armEncoder);
        
        this.offset = offset; 
        SmartDashboard.putNumber("amrP", 0);
        SmartDashboard.putNumber("armSetReference", 0);

        armPIDController.setP(armP);
        armPIDController.setI(armI);
        armPIDController.setD(armD);
        armPIDController.setIZone(armIz);
        armPIDController.setFF(armFF);
        armPIDController.setOutputRange(armMinOutput, armMaxOutput);
    }

    public void periodic() {
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