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

    public ShooterJoint(String name, int armSparkID, double offset) {
        this.name = name;
        armMotor = new CANSparkMax(armSparkID, CANSparkMax.MotorType.kBrushless);
        armPIDController = armMotor.getPIDController();
        armEncoder =  armMotor.getAbsoluteEncoder(Type.kDutyCycle);
        this.offset = offset; 
        SmartDashboard.putBoolean(name + ": Manual Control",true);
    }

    public void periodic() {
        currentPos = armEncoder.getPosition();
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
    
    public void setPosition(double position) {
        targetPos = position;
        SmartDashboard.putNumber(name, position);
        if(SmartDashboard.getBoolean(name + ": Manual Control",true)) {
            return;
        }
        armPIDController.setReference(position, ControlType.kPosition);   
        
    }
}