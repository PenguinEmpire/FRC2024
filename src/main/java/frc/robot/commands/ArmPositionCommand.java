// package frc.robot.commands;

// import edu.wpi.first.math.proto.Geometry2D;
// import edu.wpi.first.wpilibj.RobotController;
// import edu.wpi.first.wpilibj2.command.Command;
// import edu.wpi.first.math.geometry.Translation2d;
// import edu.wpi.first.hal.ConstantsJNI;


// public class ArmPositionCommand extends Command{
//     // private ArmSubsystem m_subsystem;
//     // private Position m_pos;

//     // public ArmPositionCommand(ArmSubsystem subsystem, Position pos) {
//     //     this.m_subsystem = subsystem;
//     //     this.m_pos = pos;
//     // }
// }

// public class ArmPositionCommand {
//     private int m_ticks;
    
//     public void Initialize() {
//         m_ticks = 0;
//     }
// }
// public void execute() {
//     m_ticks++;
    
//     if (m_pos == Position.GROUND) {
//         m_subsystem.setPosition(m_subsystem.isConeMode() ? 0 : 0, 0);
//         m_subsystem.setWrist(m_subsystem.isConeMode() ? 0 : 0);
//     } else if (m_pos == Position.MID) {
//         double armX = 0;
//         double armY = 0;
//         m_subsystem.setPosition(armX, armY);
//         m_subsystem.setWrist(0);
//     } else if (m_pos == Position.HIGH) {
//         // old value for old intake
//         // m_subsystem.setPosition(0.425, 0.787);
//         double armX = 0;
//         double armY = 0;
//         m_subsystem.setPosition(armX, armY);
//         m_subsystem.setWrist(0);
//     } else if (m_pos == Position.PICKUP) {
//         double armX = 0;
//         double armY = m_subsystem.isConeMode() ? 0 : 0;
//         m_subsystem.setPosition(armX, armY);
//         m_subsystem.setWrist(0);
//     } else if (m_pos == Position.CHECKUP) {
//         if (m_ticks < 0) {
//             m_subsystem.setPosition(0, 0);
//             m_subsystem.setWrist(0);
//         } else if (m_ticks < 0) {
//             m_subsystem.setPosition(m_subsystem.getArmModule().getPosition(), 0);
//         } else {
//             m_subsystem.setPosition(0.31, m_subsystem.getArmModuleB().getPosition());
//         }
//     } else if (m_pos == Position.HOME) {
//         if (m_ticks < 0) {
//             m_subsystem.setPosition(0, 0);
//             m_subsystem.setWrist(0);
//         } else {
//             m_subsystem.setPosition(0, 0);
//         }
//     }
// }
// public void ArmPositionCommand end(boolean interrupted) {
// }

// public boolean isFinished() {
//     return (m_pos == Position::HOME || m_pos == Position::CHECKUP) ? m_ticks > 0 : true;
// }