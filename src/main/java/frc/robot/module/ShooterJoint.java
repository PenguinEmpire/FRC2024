package frc.robot.module;

import com.revrobotics.SparkPIDController;
import com.revrobotics.CANSparkBase.ControlType;
import com.revrobotics.SparkAbsoluteEncoder.Type;

import java.beans.Encoder;

import com.revrobotics.AbsoluteEncoder;
import com.revrobotics.CANSparkMax;
import com.revrobotics.SparkAbsoluteEncoder;

import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.wpilibj.motorcontrol.Spark;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import com.revrobotics.CANSparkBase.ControlType;
import frc.robot.module.ShooterJoint;
import com.revrobotics.CANSparkLowLevel.MotorType;
public class ShooterJoint {

    private int armID;
    private CANSparkMax armMotor;
    private final SparkPIDController armPIDController;
    private final AbsoluteEncoder armEncoder; 
    private boolean inverted;
    private double offset;
    // neeed to set PID tuning
    private String name;
    public ShooterJoint(String name, int armSparkID, double offset) {
        SmartDashboard.putBoolean(name + ": Manual Control",true);
        this.name = name ;
        armMotor = new CANSparkMax(armSparkID, CANSparkMax.MotorType.kBrushless);
        armPIDController = armMotor.getPIDController();
        armEncoder =  armMotor.getAbsoluteEncoder(Type.kDutyCycle);
        this.offset = offset; 
    }

    public void periodic() {
        m_currentPosition = armEncoder.getPosition();
    }

    private double m_targetPosition;
    private double m_currentPosition;

    public boolean hasReachedTarget(double tolerance){
        return Math.abs(m_targetPosition - m_currentPosition) <= tolerance;
     }
    public double getPosition() {
        return armEncoder.getPosition();
    }
    
    public void setPosition(double position) {
        SmartDashboard.putNumber(name, position);
        if(SmartDashboard.getBoolean(name + ": Manual Control",true)) {
            return;
        }
        armPIDController.setReference(position, ControlType.kPosition);
            
        
    }



    }



        

    