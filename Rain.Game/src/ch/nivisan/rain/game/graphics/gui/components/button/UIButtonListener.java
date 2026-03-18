package ch.nivisan.rain.game.graphics.gui.components.button;

import java.awt.Color;
import java.awt.Point;
import java.awt.Rectangle;

import ch.nivisan.rain.game.graphics.gui.IUIActionListener;
import ch.nivisan.rain.game.graphics.gui.UIDefaults;
import ch.nivisan.rain.game.input.Mouse;
import ch.nivisan.rain.game.input.MouseButton;

public class UIButtonListener {
    protected final UIButton button;
    public Color buttonDefaultColor = new Color(UIDefaults.buttonDefaultColor);
    public Color buttonDefaultHoveredColor = new Color(UIDefaults.buttonDefaultHoveredColor);
    public Color buttonDefaultPressedColor = new Color(UIDefaults.buttonDefaultPressedColor);
    protected boolean pressed = false;
    private boolean insideBounds = false;
    private boolean pressedWhileOut = false;

    public UIButtonListener(UIButton button) {
        this.button = button;
    }

    public void listen(Rectangle buttonBounds, IUIActionListener actionListener, UIButton button) {
        if (buttonBounds.contains(new Point(Mouse.getXPosition(), Mouse.getYPosition()))) {
            if (insideBounds) {
                if (!pressedWhileOut && !pressed && Mouse.getButtonState() == MouseButton.Left) {
                    onClick();
                    pressed = true;
                } else if (Mouse.getButtonState() == MouseButton.None) {
                    if (pressed) {
                        onRelease();
                        actionListener.performAction();
                        pressed = false;
                    }
                    pressedWhileOut = false;
                }
                return;
            }
            ;

            if (Mouse.getButtonState() == MouseButton.Left) {
                pressedWhileOut = true;
            }

            onHover();
            insideBounds = true;
        } else {
            if (insideBounds == false) return;

            onExit();
            pressed = false;
            insideBounds = false;
        }
    }

    protected void onHover() {
        button.setColor(buttonDefaultHoveredColor);
    }

    protected void onExit() {
        button.setColor(buttonDefaultPressedColor);
    }

    protected void onClick() {
        button.setColor(buttonDefaultPressedColor);
    }

    protected void onRelease() {
        button.setColor(buttonDefaultColor);
    }
}
