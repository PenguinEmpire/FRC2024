package frc.robot.subsystems;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.ControlInput;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.AddressableLED;
import edu.wpi.first.wpilibj.AddressableLEDBuffer;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.DriverStation.MatchType;

public class LightingSubsystem extends SubsystemBase {
    private int firstPixelHue = 0;
    private int counterRGB = 0;
    private int trailCounter = 0;
    private int length = 86;
    private AddressableLED led;
    private Timer timer = new Timer();
    private AddressableLEDBuffer ledBuffer;

    public int tempR = 0;
    public int tempG = 0;

    public final int trailLength = 6;
    public int tempB = 0;
    public boolean isTempColor;
    public SendableChooser<String> chooser = new SendableChooser<String>();

    final String kOff = "Off";
    final String kBlue = "Blue";
    final String kRed = "Red";
    final String kBlueWhite = "Blue & White";
    final String kRainbow = "Rainbow";
    final String kPenguin = "Penguin";
    final String kNewTest = "New";
    private int matchDiv = 1;

    public LightingSubsystem(ControlInput controlInput) {
        led = new AddressableLED(9);
        ledBuffer = new AddressableLEDBuffer(length);

        led.setLength(length);
        led.setData(ledBuffer);
        led.start();

        chooser.setDefaultOption(kOff, kOff);
        chooser.addOption(kBlue, kBlue);
        chooser.addOption(kNewTest, kNewTest);
        chooser.addOption(kRed, kRed);
        chooser.addOption(kBlueWhite, kBlueWhite);
        chooser.addOption(kRainbow, kRainbow);
        chooser.addOption(kPenguin, kPenguin);

        SmartDashboard.putData("Lighting Modes", chooser);

    }

    @Override
    public void periodic() {
        matchDiv = DriverStation.getMatchType() == MatchType.None ? 4 : 2;
        var choice = chooser.getSelected();
        if (choice == kBlue)
             newTest(0,0,255);
        if (choice == kRed)
            newTest(255,0,0);
        if (choice == kBlueWhite)
            blueAndWhite();
        if (choice == kRainbow)
            rainbow();
        if (choice == kNewTest)
            newTest(255,255,255);
        if (choice == kPenguin)
            penguin(0, 0, 255);
        if (choice == kOff)
            off();

        if (isTempColor) {
            hasPiece();
        }

        led.setData(ledBuffer);
    }

    public void blue() {
        for (var i = 0; i < ledBuffer.getLength(); i++) {
            ledBuffer.setRGB(i, 0, 0, (int) 255 / matchDiv);
        }
    }

    public void red() {
        for (var i = 0; i < ledBuffer.getLength(); i++) {
            ledBuffer.setRGB(i, (int) 255 / matchDiv, 0, 0);
        }
    }

    public void newTest(int r, int g, int b) {
         for (var i = 0; i < ledBuffer.getLength(); i++) {
            ledBuffer.setRGB(i, (int) 0, 0, 0);
        }
        int size = 25;
        var length = Math.min(trailCounter + size, ledBuffer.getLength() + size);
        for (var i = trailCounter - size; i < length - size; i++) {
            if(i >= 0)
                ledBuffer.setRGB(i, (int) r / matchDiv, (int) g / matchDiv, (int) b / matchDiv);
        }
        trailCounter++;
        if(trailCounter > ledBuffer.getLength() + size) {
            trailCounter = 0;
        }
    }

    public void blueAndWhite() {
        for (int i = 0; i < ledBuffer.getLength(); i++) {
            // Calculate the hue - hue is easier for rainbows because the color
            // shape is a circle so only one value needs to precess
            final var pixelHue = (firstPixelHue + (i * 180 / ledBuffer.getLength())) % 180;
            // Set the value
            ledBuffer.setHSV(i, 221, pixelHue, 128 / matchDiv);
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
            ledBuffer.setHSV(i, hue, 255, 128 / matchDiv);
        }
        // Increase by to make the rainbow "move"
        firstPixelHue += 3;
        // Check bounds
        firstPixelHue %= 180;
    }

    public void penguin(int red, int green, int blue) {
        for (var i = 0; i < ledBuffer.getLength(); i++) {
            if ((i + counterRGB) % 5 == 0) {
                // blue
                ledBuffer.setRGB(i, red, green, blue);
            } else { // black
                ledBuffer.setRGB(i, 0, 0, 0);
            }
        }
        if (timer.hasElapsed(SmartDashboard.getNumber("Animation Speed", 0))) {
            counterRGB++;
            timer.reset();
        }
    }

    public void off() {
        for (var i = 0; i < ledBuffer.getLength(); i++) {
            ledBuffer.setRGB(i, 0, 0, 0);
        }
    }

    public void setPulsing(boolean pulsing) {
        isTempColor = pulsing;
    }

    public void releaseTemporaryColor() {
        isTempColor = false;
    }

    public void hasPiece() {
        for (var i = 0; i < ledBuffer.getLength(); i++) {
            if (counterRGB % 16 >= 8) {
                ledBuffer.setRGB(i, 0, (int) 255 / matchDiv, 0);

            } else {
                ledBuffer.setRGB(i, 0, 0, 0);

            }
        }
        counterRGB++;
    }

}
