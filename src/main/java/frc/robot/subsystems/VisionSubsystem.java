package frc.robot.subsystems;
import com.revrobotics.AbsoluteEncoder;
import com.revrobotics.CANSparkMax;
import com.revrobotics.SparkAbsoluteEncoder;
import com.revrobotics.SparkAbsoluteEncoder.Type;

import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Commands;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.ControlInput;

public class VisionSubsystem extends SubsystemBase{
    private ControlInput m_ci;
    private double m_ticks;
    private NetworkTable table;   
    private boolean m_customPipeline;


    private CANSparkMax motor;
    private AbsoluteEncoder encoder;

    public VisionSubsystem(ControlInput ci) {
        motor = new CANSparkMax(7, CANSparkMax.MotorType.kBrushless);
        encoder = motor.getAbsoluteEncoder(Type.kDutyCycle);
        m_ci = ci;
        table = NetworkTableInstance.getDefault().getTable("limelight"); 
        m_ticks = 0;

    }

    @Override
    public void periodic() {
       System.out.println(encoder.getPosition());
    }

    public double getYaw() {
        return table.getEntry("tx").getDouble(0.0);
    }

    public double getPitch() {
        return table.getEntry("ty").getDouble(0.0);
    }

    public void setLED(boolean toggle) {
        table.getEntry("ledMode").setNumber(toggle ? 3 : 1);
    }

    public void setCustomPipeline(boolean toggle) {
        m_customPipeline = toggle;
    }

    public boolean hasTargets() {
        return table.getEntry("tv").getDouble(0.0) == 1.0;
    }

    public long getID() {
        return table.getEntry("tid").getInteger(-1);
    }

    public Command UpdateLimelight(boolean toggle, int index) {
        return Commands.startEnd(
            () -> {
                table.getEntry("ledMode").getDouble(toggle ? 3 : 1);
            },
            () -> {
                table.getEntry("pipeline").getDouble(index);
            }
        );
    }

    public void setPipeline(int index) {
        table.getEntry("pipeline").setNumber(index);
    }

    public void Periodic() {
    }
}