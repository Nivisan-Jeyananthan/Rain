package ch.nivisan.rain.graphics.gui.button;

import ch.nivisan.rain.graphics.gui.UIDefaults;
import ch.nivisan.rain.input.Mouse;
import ch.nivisan.rain.input.MouseButton;

import java.awt.*;

public class UIButtonListener {
    protected final UIButton button;
    private boolean insideBounds = false;
    protected boolean pressed = false;
    private boolean pressedWhileOut = false;
    public Color buttonDefaultColor = new Color(UIDefaults.buttonDefaultColor);
    public Color buttonDefaultHoveredColor = new Color(UIDefaults.buttonDefaultHoveredColor);
    public Color buttonDefaultPressedColor = new Color(UIDefaults.buttonDefaultPressedColor);

    public UIButtonListener(UIButton button) {
        this.button = button;
    }

    public void listen(Rectangle buttonBounds, IUIActionListener actionListener, UIButton button){
        if(buttonBounds.contains(new Point(Mouse.getXPosition(), Mouse.getYPosition()))){
            if(insideBounds) {
                if(!pressedWhileOut && !pressed && Mouse.getButtonState() == MouseButton.Left){
                    onClick();
                    pressed = true;
                }
                else if(Mouse.getButtonState() == MouseButton.None){
                    if(pressed) {
                        onRelease();
                        actionListener.performAction();
                        pressed = false;
                    }
                    pressedWhileOut = false;
                }
                return;
            };

            if(Mouse.getButtonState() == MouseButton.Left){
                pressedWhileOut = true;
            }

            onHover();
            insideBounds = true;
        }else{
            if(insideBounds == false) return;

            onExit();
            pressed = false;
            insideBounds = false;
        }
    }

    protected void onHover() { button.setColor(buttonDefaultHoveredColor); }

    protected void onExit(){
        button.setColor(buttonDefaultPressedColor);
    }

    protected void onClick(){
        button.setColor(buttonDefaultPressedColor);
    }

    protected void onRelease(){ button.setColor(buttonDefaultColor); }
}
