package physical;

import edu.wpi.first.wpilibj.Joystick;
// import edu.wpi.first.wpilibj.XboxController;

public class ControlInput {

    private Joystick accessoryJoystick = new Joystick(0);
    private Joystick rightJoystick = new Joystick(1);
    private Joystick leftJoystick = new Joystick(2);
    // private XboxController xboxController = new XboxController(3);

    public ControlInput() {
    }

    public Joystick getLeftJoystick() {
        return leftJoystick;
    }

    public Joystick getRightJoystick() {
        return rightJoystick;
    }

    public Joystick getAccessoryJoystick() {
        return accessoryJoystick;
    }

    // public XboxController getGameController() {
    //     return xboxController;
    // }

    public boolean isConeMode() {
        return getAccessoryJoystick().getRawAxis(3) > 0;
    }
}

