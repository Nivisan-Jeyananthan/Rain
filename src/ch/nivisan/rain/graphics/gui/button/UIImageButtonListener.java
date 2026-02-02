package ch.nivisan.rain.graphics.gui.button;

import java.awt.*;

public class UIImageButtonListener extends UIButtonListener {
    private Image hoverImage;
    public UIImageButtonListener(UIButton button, Image hoverImage) {
        super(button);
        this.hoverImage = hoverImage;
    }

    @Override
    protected void onHover(){
        button.setBackgroundImage(hoverImage);
    }
}
