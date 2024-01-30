package frc.robot.module;
import com.revrobotics.CANSparkMax;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class ShooterJoint {
    private CANSparkMax armMotor;

    public ShooterJoint() {
        // need to change ID
        this.armMotor = new CANSparkMax( 0, CANSparkMax.MotorType.kBrushless);
        SmartDashboard.putNumber("Arm-A Speed", 0.6);
    }

    public void moveA(boolean toggle, boolean reverse) {
        double speed = SmartDashboard.getNumber("Arm-A Speed", 0);
      
        armMotor.set(speed);
    
        
    }
}
