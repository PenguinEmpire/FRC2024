package frc.robot.subsystems;

import com.revrobotics.CANSparkMax;
import com.revrobotics.RelativeEncoder;
import com.revrobotics.SparkPIDController;
import com.revrobotics.CANSparkBase.ControlType;

import edu.wpi.first.wpilibj.AnalogInput;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Commands;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import edu.wpi.first.wpilibj.AnalogInput;
//remove suppressor once done implementing
@SuppressWarnings("unused")
public class ShooterSubsystem extends SubsystemBase {
    // used for intake rollers
    private final CANSparkMax intakeMotor;
    // used for output rollers
    private final CANSparkMax outputMotor;
    // used for turning the shooter
    private final CANSparkMax shooterEntMotor;

    private final SparkPIDController intakePID;
    private final SparkPIDController outputSpeedPID;
    private final SparkPIDController shooterEntPID;

    private final RelativeEncoder shooterEntEnocder;

    private final AnalogInput infraredSensor;

    private String iName = "Intake";
    private double intakeP = 0.0;
    private double intakeI = 0.0;
    private double intakeD = 0.0;
    private double intakeIz = 0.0;
    private double intakeFF = 0.0;
    private double intakeMaxOutput = 0.0;
    private double intakeMinOutput = 0.0;

    private String oName = "Output";
    private double outputP = 0.0;
    private double outputI = 0.0;
    private double outputD = 0.0;
    private double outputIz = 0.0;
    private double outputFF = 0.0;
    private double outputMaxOutput = 0.0;
    private double outputMinOutput = 0.0;

    private String sName = "Shooter";
    private double shooterP = 0.0;
    private double shooterI = 0.0;
    private double shooterD = 0.0;
    private double shooterIz = 0.0;
    private double shooterFF = 0.0;
    private double shooterMaxOutput = 0.0;
    private double shooterMinOutput = 0.0;

    public ShooterSubsystem(int intakeSparkID, int ouputSparkID, int shooterEntID, int infraredSensorID) {
        intakeMotor = new CANSparkMax(intakeSparkID, CANSparkMax.MotorType.kBrushless);
        outputMotor = new CANSparkMax(ouputSparkID, CANSparkMax.MotorType.kBrushless);
        shooterEntMotor = new CANSparkMax(shooterEntID, CANSparkMax.MotorType.kBrushless);
        infraredSensor = new AnalogInput(infraredSensorID);
        // PIDs for all motors
        // used for making not jerky
        intakePID = intakeMotor.getPIDController();
        shooterEntPID = shooterEntMotor.getPIDController();

        // used for changing velocity
        outputSpeedPID = outputMotor.getPIDController();

        // used for setting the position of the intake/shooter
        shooterEntEnocder = shooterEntMotor.getEncoder();

        SmartDashboard.putNumber(iName + "P Gain", intakeP);
        SmartDashboard.putNumber(iName + "I Gain", intakeI);
        SmartDashboard.putNumber(iName + "D Gain", intakeD);
        SmartDashboard.putNumber(iName + "I Zone", intakeIz);
        SmartDashboard.putNumber(iName + "Feed Forward", intakeFF);
        SmartDashboard.putNumber(iName + "Max Output", intakeMaxOutput);
        SmartDashboard.putNumber(iName + "Min Output", intakeMinOutput);

        SmartDashboard.putNumber(oName + "P Gain", outputP);
        SmartDashboard.putNumber(oName + "I Gain", outputI);
        SmartDashboard.putNumber(oName + "D Gain", outputD);
        SmartDashboard.putNumber(oName + "I Zone", outputIz);
        SmartDashboard.putNumber(oName + "Feed Forward", outputFF);
        SmartDashboard.putNumber(oName + "Max Output", outputMaxOutput);
        SmartDashboard.putNumber(oName + "Min Output", outputMinOutput);

        SmartDashboard.putNumber(sName + "P Gain", shooterP);
        SmartDashboard.putNumber(sName + "I Gain", shooterI);
        SmartDashboard.putNumber(sName + "D Gain", shooterD);
        SmartDashboard.putNumber(sName + "I Zone", shooterIz);
        SmartDashboard.putNumber(sName + "Feed Forward", shooterFF);
        SmartDashboard.putNumber(sName + "Max Output", shooterMaxOutput);
        SmartDashboard.putNumber(sName + "Min Output", shooterMinOutput);

    }

    @Override
    public void periodic() {
        double iP = SmartDashboard.getNumber(iName + "P Gain", intakeP);
        double iI = SmartDashboard.getNumber(iName + "I Gain", intakeI);
        double iD = SmartDashboard.getNumber(iName + "D Gain", intakeD);
        double iIz = SmartDashboard.getNumber(iName + "I Zone", intakeIz);
        double iFF = SmartDashboard.getNumber(iName + "Feed Forward", intakeFF);
        double iIMaxOut = SmartDashboard.getNumber(iName + "Max Output", intakeMaxOutput);
        double iIMinOut = SmartDashboard.getNumber(iName + "Min Output", intakeMinOutput);

        double sP = SmartDashboard.getNumber(sName + "P Gain", shooterP);
        double sI = SmartDashboard.getNumber(sName + "I Gain", shooterI);
        double sD = SmartDashboard.getNumber(sName + "D Gain", shooterD);
        double sIz = SmartDashboard.getNumber(sName + "I Zone", shooterIz);
        double sFF = SmartDashboard.getNumber(sName + "Feed Forward", shooterFF);
        double sMaxOut = SmartDashboard.getNumber(sName + "Max Output", shooterMaxOutput);
        double sMinOut = SmartDashboard.getNumber(sName + "Min Output", shooterMinOutput);

        if (iP != intakeP) { intakePID.setP(iP); intakeP = iP;}
        if (iI != intakeI) { intakePID.setI(iI); intakeI = iI;}
        if (iD != intakeD) { intakePID.setD(iD); intakeD = iD;}
        if (iIz != intakeIz) { intakePID.setIZone(iIz); intakeIz = iIz;}
        if (iFF != intakeFF) { intakePID.setFF(iFF); intakeFF = iFF;}
        if ((iIMaxOut != intakeMaxOutput) || (iIMinOut != intakeMinOutput)) {
            intakePID.setOutputRange(iIMinOut, iIMaxOut);
            intakeMinOutput = iIMinOut;
            intakeMaxOutput = iIMaxOut;
        }

        if (sP != shooterP) { shooterEntPID.setP(iP); shooterP = sP;}
        if (sI != shooterI) { shooterEntPID.setI(iI); shooterI = sI;}
        if (sD != shooterD) { shooterEntPID.setD(iD); shooterD = sD;}
        if (sIz != shooterIz) { shooterEntPID.setIZone(iIz); shooterIz = sIz;}
        if (sFF != shooterFF) { shooterEntPID.setFF(iFF); shooterFF = sFF;}
        if ((sMaxOut != shooterMaxOutput) || (sMinOut != shooterMinOutput)) {
            shooterEntPID.setOutputRange(sMinOut, sMaxOut);
            shooterMaxOutput = sMinOut;
            shooterMinOutput = sMaxOut;
        }
    }
    public Command stopIntakeRollers(){
     double speed = SmartDashboard.getNumber("Intake Speed", 0);
     return Commands.runEnd(
            () -> {
            // need to tune and change the value     
                if (infraredSensor.getVoltage() > 0) {
                    intakeMotor.set(0);
                } else {
                    intakeMotor.set(speed);
                }
            },
            () -> intakeMotor.set(0)
    );}

    
    public Command runIntakeRollers() {
        double speed = SmartDashboard.getNumber("Intake Speed", 0);
        return Commands.runEnd(
                () -> {
                    intakeMotor.set(speed);
                },
                () -> {
                    intakeMotor.set(0);
                });
        
    }

    public Command runShooterRollers(boolean reverse) {
        double speed = SmartDashboard.getNumber("shooterMotor", 0);
        return Commands.runEnd(
                () -> {
                    intakeMotor.set(-speed);
                },
                () -> {
                    intakeMotor.set(speed);
                });

    }

    public void setShooterPosition(double position){
        SmartDashboard.putNumber(sName + "pos", position);
        if (SmartDashboard.getBoolean(sName + "Manual Control", true))
            return;
        shooterEntPID.setReference(position, ControlType.kPosition);
    }

    // manual control
    public Command moveArm(boolean toggle, boolean reverse) {
        double speed = SmartDashboard.getNumber("armMotor", 0);
        return Commands.runEnd(
                () -> {
                    shooterEntMotor.set(reverse ? speed : -speed);
                },
                () -> {
                    shooterEntMotor.set(speed);
                });

    }

    

}
