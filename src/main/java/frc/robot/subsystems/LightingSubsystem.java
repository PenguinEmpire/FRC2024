package frc.robot.subsystems;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.ControlInput;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.AddressableLED;
import edu.wpi.first.wpilibj.AddressableLEDBuffer;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.Timer
;

public class LightingSubsystem extends SubsystemBase {
    private int firstPixelHue = 0;
    private int counterRGB = 0;
    private int length = 251;
    private AddressableLED led;
    private Timer timer = new Timer();
    private AddressableLEDBuffer ledBuffer;

    public int tempR = 0;
    public int tempG = 0;
    public int tempB = 0;
    public boolean isTempColor;
    public SendableChooser<String> chooser = new SendableChooser<String>();

    final String kBlue = "Blue";
    final String kRed = "Red";
    final String kBlueWhite = "Blue & White";
    final String kRainbow = "Rainbow";
    final String kPenguin = "Penguin";

    public LightingSubsystem(ControlInput controlInput) {
        led = new AddressableLED(9);
        ledBuffer = new AddressableLEDBuffer(length);

        led.setLength(length);
        led.setData(ledBuffer);
        led.start();

        chooser.setDefaultOption(kBlue, kBlue);
        chooser.addOption(kRed, kRed);
        chooser.addOption(kBlueWhite, kBlueWhite);
        chooser.addOption(kRainbow, kRainbow);
        chooser.addOption(kPenguin, kPenguin);

        SmartDashboard.putNumber("Animation Speed", 0);
        SmartDashboard.putData("Lighting Modes", chooser);
       
    }

    @Override
    public void periodic() {
        var choice = chooser.getSelected();

        if (choice == kBlue) blue();
        if (choice == kRed) red();
        if (choice == kBlueWhite) blueAndWhite();
        if (choice == kRainbow) rainbow();
        if (choice == kPenguin) penguin(0,0,255);
        

        if (isTempColor) {
            for (var i = 0; i < ledBuffer.getLength(); i++) {
                ledBuffer.setRGB(i, tempR, tempG, tempB);
            }
        }
        led.setData(ledBuffer);
    }

    public void blue() {
        for (var i = 0; i < ledBuffer.getLength(); i++) {
            ledBuffer.setRGB(i, 0, 0, 255);
        }
    }

    public void red(){
        for (var i = 0; i < ledBuffer.getLength(); i++) {
            ledBuffer.setRGB(i, 255, 0, 0);
        }
    }

    public void blueAndWhite(){
        for (int i = 0; i < ledBuffer.getLength(); i++) {
            // Calculate the hue - hue is easier for rainbows because the color
            // shape is a circle so only one value needs to precess
            final var pixelHue = (firstPixelHue + (i * 180 / ledBuffer.getLength())) % 180;
            // Set the value
            ledBuffer.setHSV(i, 221, pixelHue, 128);
          }
          // Increase by to make the rainbow "move"
          firstPixelHue += 3;
          // Check bounds
          firstPixelHue %= 180;

    }

    public void rainbow() {
        for (var i = 0; i < ledBuffer.getLength(); i++) {
            // Calculate the hue - hue is easier for rainbows because the color
            // shape is a circle so only one value needs to precess
            final var hue = (firstPixelHue + (i * 180 / ledBuffer.getLength())) % 180;
            // Set the value
            ledBuffer.setHSV(i, hue, 255, 128);
          }
          // Increase by to make the rainbow "move"
          firstPixelHue += 3;
          // Check bounds
          firstPixelHue %= 180;
    }

    public void penguin(int red, int green, int blue){
        for (var i = 0; i < ledBuffer.getLength(); i++){
            if ((i + counterRGB) % 5 == 0) {
              //blue
              ledBuffer.setRGB(i, red, green, blue);
            } else {      //black
              ledBuffer.setRGB(i, 0, 0, 0);
            }
          }
          if(timer.hasElapsed(SmartDashboard.getNumber("Animation Speed", 0))) {
            counterRGB++;
            timer.reset();
          }
    }

    public void setTemporaryColor(int red, int green, int blue) {
        isTempColor = true;
        tempR = red;
        tempG = green;
        tempB = blue;
      }
    public void releaseTemporaryColor() {
        isTempColor = false;
    }

    
}
