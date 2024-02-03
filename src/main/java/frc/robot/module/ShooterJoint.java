package frc.robot.module;

import com.revrobotics.SparkPIDController;

import com.revrobotics.CANSparkMax;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import com.revrobotics.CANSparkBase.ControlType;
import frc.robot.module.ShooterJoint;
import com.revrobotics.CANSparkLowLevel.MotorType;
public class ShooterJoint {

    private int armID;
    private CANSparkMax armMotor;
    private final SparkPIDController armPIDController;
    private boolean inverted;
    private boolean driveInverted;
    private double offset;

    public ShooterJoint(int armSparkID, boolean inverted, boolean driveInverted, double offset) {
        armMotor = new CANSparkMax(armSparkID, CANSparkMax.MotorType.kBrushless);
        armPIDController = armMotor.getPIDController();
        this.inverted = inverted;
        this.driveInverted = driveInverted;
        this.offset = offset; 

        
    }
}


        

    