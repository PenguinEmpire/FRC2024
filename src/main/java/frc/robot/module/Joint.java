package frc.robot.module;

import com.revrobotics.SparkPIDController;
import com.revrobotics.CANSparkBase.ControlType;
import com.revrobotics.SparkAbsoluteEncoder.Type;
import com.revrobotics.AbsoluteEncoder;
import com.revrobotics.CANSparkMax;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.module.Joint;

public class Joint {

    private String name;

    private CANSparkMax armMotor;
    private final SparkPIDController armPIDController;
    private final AbsoluteEncoder armEncoder; 

    private double targetPos;
    private double currentPos;

    private double armP;
    private double armI;
    private double armD;
    private double armIz;
    private double armFF;
    private double armMaxOutput;
    private double armMinOutput;
    private boolean invertedEncoder;

    public Joint(String nameIn, int sparkID, double P, double I, double D, double FF, double minOutput, double maxOutput, boolean invertEncoder) {
        this.name = nameIn + ":";
        this.armP = P;
        this.armI = I;
        this.armD = D;
        this.armFF = FF;
        this.armMinOutput = minOutput;
        this.armMaxOutput = maxOutput;
        this.invertedEncoder = invertEncoder;
        armMotor = new CANSparkMax(sparkID, CANSparkMax.MotorType.kBrushless);

        armEncoder =  armMotor.getAbsoluteEncoder(Type.kDutyCycle);
        armEncoder.setPositionConversionFactor(2 * Math.PI);
        armEncoder.setInverted(invertEncoder);
        
        armPIDController = armMotor.getPIDController();
        armPIDController.setFeedbackDevice(armEncoder);
        armPIDController.setPositionPIDWrappingEnabled(true);
        armPIDController.setPositionPIDWrappingMinInput(0);
        armPIDController.setPositionPIDWrappingMaxInput(2*Math.PI);
        
        SmartDashboard.putNumber(name + "P", armP);
        SmartDashboard.putNumber(name + "I", armI);
        SmartDashboard.putNumber(name + "D", armD);
        SmartDashboard.putNumber(name + "FF", armFF);
        SmartDashboard.putNumber(name + "Reference", armEncoder.getPosition());
        SmartDashboard.putBoolean(name + "Inverted", invertedEncoder);

        armPIDController.setP(armP);
        armPIDController.setI(armI);
        armPIDController.setD(armD);
        armPIDController.setIZone(armIz);
        armPIDController.setFF(armFF);
        armPIDController.setOutputRange(armMinOutput, armMaxOutput);
    }

    public void periodic() {
        SmartDashboard.putNumber(name + "Encoder", armEncoder.getPosition());
        currentPos = armEncoder.getPosition();

        double amrPValue = SmartDashboard.getNumber(name +"P", 0);
        if (amrPValue != armP) {
            armPIDController.setP(amrPValue);
        }

        double armIValue = SmartDashboard.getNumber(name + "I", 0);
        if (armIValue != armI) {
            armPIDController.setI(armIValue);
        }

        double armDValue = SmartDashboard.getNumber(name +"D", 0);
        if (armDValue != armD) {
            armPIDController.setD(armDValue);
        }

        double armFFValue = SmartDashboard.getNumber(name + "FF", 0);
        if (armFFValue != armFF) {
            armPIDController.setFF(armFFValue);
        }

        boolean invertedVal = SmartDashboard.getBoolean(name + "Inverted", invertedEncoder);
        if(invertedVal != this.invertedEncoder) {
            armEncoder.setInverted(invertedVal);
        }
        
        double armSetRef = SmartDashboard.getNumber(name + "Reference", armEncoder.getPosition());
        armPIDController.setReference(armSetRef, ControlType.kPosition);

    }

    public boolean hasReachedTarget(double tolerance){
        return Math.abs(targetPos - currentPos) <= tolerance;
    }

    public double getPosition() {
        return armEncoder.getPosition();
    }
    
    public void setPosition(double position) {
       armPIDController.setReference(position, ControlType.kPosition);
       SmartDashboard.putNumber(name + "Reference", position);
    }
}