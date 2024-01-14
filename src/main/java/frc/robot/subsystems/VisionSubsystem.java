package frc.robot.subsystems;

import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Commands;
import frc.ControlInput;

public class VisionSubsystem {
    private ControlInput m_ci;
    private double m_ticks;
    private NetworkTable table;   
    private boolean m_customPipeline;

    public VisionSubsystem(ControlInput ci) {
        m_ci = ci;
        table = NetworkTableInstance.getDefault().getTable("limelight"); 
        m_ticks = 0;

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