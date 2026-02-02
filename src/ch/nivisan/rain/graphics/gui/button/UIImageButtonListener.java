package ch.nivisan.rain.graphics.gui.button;

import ch.nivisan.rain.utils.ImageUtils;

import java.awt.*;
import java.awt.image.BufferedImage;

public class UIImageButtonListener extends UIButtonListener {
    private final Image hoverImage;
    private final Image defaultImage;
    private final Image pressedImage;

    public UIImageButtonListener(UIButton button) {
        super(button);
        this.defaultImage = button.getBackgroundImage();
        this.hoverImage = ImageUtils.changeBrightness(50, defaultImage);
        this.pressedImage = ImageUtils.changeBrightness(-50, defaultImage);
    }

    public UIImageButtonListener(UIButton button, BufferedImage hoverImage, BufferedImage pressedImage) {
        super(button);
        this.hoverImage = hoverImage;
        this.defaultImage = button.getBackgroundImage();
        this.pressedImage = pressedImage;
    }

    @Override
    protected void onHover() {
        button.setBackgroundImage(hoverImage);
    }

    @Override
    protected void onExit(){ button.setBackgroundImage(defaultImage); }

    @Override
    protected void onClick() { button.setBackgroundImage(pressedImage); }
}
