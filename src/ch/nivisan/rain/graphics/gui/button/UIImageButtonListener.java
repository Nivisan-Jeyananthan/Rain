package ch.nivisan.rain.graphics.gui.button;

import java.awt.*;

public class UIImageButtonListener extends UIButtonListener {
    private final Image hoverImage;
    private final Image defaultImage;

    public UIImageButtonListener(UIButton button, Image hoverImage) {
        super(button);
        this.hoverImage = hoverImage;
        this.defaultImage = button.getBackgroundImage();
    }

    @Override
    protected void onHover() {
        button.setBackgroundImage(hoverImage);
    }

    @Override
    protected void onExit(){ button.setBackgroundImage(defaultImage); }
}
