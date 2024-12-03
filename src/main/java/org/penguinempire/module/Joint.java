package org.penguinempire.module;

import com.revrobotics.SparkPIDController;
import com.revrobotics.CANSparkBase.ControlType;
import com.revrobotics.CANSparkBase.IdleMode;
import com.revrobotics.SparkAbsoluteEncoder.Type;

import org.penguinempire.module.Joint;

import com.revrobotics.AbsoluteEncoder;
import com.revrobotics.CANSparkMax;

import edu.wpi.first.math.controller.ArmFeedforward;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class Joint {

    private String name;

    private CANSparkMax armMotor;
    private final SparkPIDController armPIDController;
    private final AbsoluteEncoder armEncoder; 

    private double targetPos;
    private double currentPos;

    private double offsetFF;
    private double armP;
    private double armI;
    private double armD;
    private double armIz;
    private double armFF;
    private double armMaxOutput;
    private double armMinOutput;
    private boolean invertedEncoder;
    public ArmFeedforward jointFF;

    public double staticGain = 0;
    public double gravityGain = 0;
    public double velocityGain = 0;

    private boolean reverseFF;

    public Joint(String nameIn, int sparkID, double P, double I, double Iz, double D, double FF, double minOutput, double maxOutput, boolean invertEncoder, ArmFeedforward jFF, double jFFOffset, boolean reverseFF) {
        this.name = nameIn + ": ";
        this.armP = P;
        this.armI = I;
        this.armD = D;
        this.armIz = Iz;
        this.jointFF = jFF;
        this.offsetFF = jFFOffset;
        if(jointFF != null) {
            this.staticGain = jointFF.ks;
            this.gravityGain = jointFF.kg;
            this.velocityGain = jointFF.kv;
        }
        this.reverseFF = reverseFF;
        this.armMinOutput = minOutput;
        this.armMaxOutput = maxOutput;
        this.invertedEncoder = invertEncoder;
        armMotor = new CANSparkMax(sparkID, CANSparkMax.MotorType.kBrushless);

        // change back to brake for everything
        armMotor.setIdleMode(IdleMode.kBrake);
        armEncoder = armMotor.getAbsoluteEncoder(Type.kDutyCycle);
        armEncoder.setPositionConversionFactor(2 * Math.PI);
        armEncoder.setInverted(invertEncoder);

        jointFF = new ArmFeedforward(staticGain, gravityGain, velocityGain);
        
        armPIDController = armMotor.getPIDController();
        armPIDController.setFeedbackDevice(armEncoder);
        armPIDController.setPositionPIDWrappingEnabled(true);
        armPIDController.setPositionPIDWrappingMinInput(0);
        armPIDController.setPositionPIDWrappingMaxInput(2*Math.PI);
        SmartDashboard.putNumber(name + "P", armP);
        SmartDashboard.putNumber(name + "I", armI);
        SmartDashboard.putNumber(name + "IZ", armIz);
        SmartDashboard.putNumber(name + "D", armD);
        SmartDashboard.putNumber(name + "FF", armFF);
        SmartDashboard.putNumber(name + "Static Gain", staticGain);
        SmartDashboard.putNumber(name + "Gravity Gain", gravityGain);
        SmartDashboard.putNumber(name + "Velocity Gain", velocityGain);
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

        double armIZValue = SmartDashboard.getNumber(name + "IZ", 0);
        if (armIZValue != armIz) {
            armPIDController.setIZone(armIZValue);
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


        double sGain = SmartDashboard.getNumber(name + "Static Gain", staticGain);
        if (staticGain != sGain) {
            jointFF = new ArmFeedforward(sGain, gravityGain, velocityGain);
            this.staticGain = sGain;
        }

        double gGain = SmartDashboard.getNumber(name + "Gravity Gain", gravityGain);
        if (gravityGain != gGain) {
            jointFF = new ArmFeedforward(staticGain, gGain, velocityGain);
            this.gravityGain = gGain;
        }

        double vGain = SmartDashboard.getNumber(name + "Velocity Gain", velocityGain);
        if (velocityGain != vGain) {
            jointFF = new ArmFeedforward(staticGain, gravityGain, vGain);
            this.velocityGain = vGain;
        }

        double ffAddition = 0;
        if(jointFF != null) {
            ffAddition = (reverseFF ? -1 : 1) * jointFF.calculate(WrapAngle(currentPos + offsetFF), armEncoder.getVelocity());
        }
        
        double armSetRef = SmartDashboard.getNumber(name + "Reference", armEncoder.getPosition());
        armPIDController.setReference(armSetRef, ControlType.kPosition, 0, ffAddition);

    }

    public static double WrapAngle(double _angle) {
        double twoPi = 2*Math.PI;

        if (_angle == twoPi) { // Handle this case separately to avoid floating point errors with the floor after the division in the case below
            return 0.0;
        }
        else if (_angle > twoPi) {
            return _angle - twoPi*Math.floor(_angle / twoPi);
        }
        else if (_angle < 0.0) {
            return _angle + twoPi*(Math.floor((-_angle) / twoPi)+1);
        }
        else {
            return _angle;
        }
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
