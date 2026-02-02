package ch.nivisan.rain.graphics.gui.button;

import ch.nivisan.rain.graphics.gui.UIComponent;
import ch.nivisan.rain.graphics.gui.UILabel;
import ch.nivisan.rain.utils.Vector2;

import java.awt.*;
import java.awt.image.BufferedImage;

public class UIButton extends UIComponent {
    private final UIComponent childComponent;
    private final IUIActionListener actionListener;
    private Image backgroundImage;
    private Rectangle buttonBounds;
    private Vector2 absolutePosition = getAbsolutePosition();
    private UIButtonListener buttonListener = new UIButtonListener(this);

    public UIButton(Vector2 position, Vector2 size, String text, IUIActionListener actionListener) {
        super(position, size);
        childComponent = new UILabel(position, text);
        this.actionListener = actionListener;
    }

    public UIButton(Vector2 position, String text, IUIActionListener actionListener) {
        super(position);
        childComponent = new UILabel(position, text);
        this.actionListener = actionListener;
    }

    public UIButton(Vector2 position, Vector2 size, Image image, IUIActionListener actionListener) {
        super(position, size);

        childComponent = new UILabel(position, "");
        this.actionListener = actionListener;
        this.backgroundImage = image;
    }

    public Image getBackgroundImage(){
        return backgroundImage;
    }

    public void setButtonListener(UIButtonListener buttonListener) {
        this.buttonListener = buttonListener;
    }

    public void setTextColor(int color) {
        childComponent.setColor(color);
    }

    public void setTextColor(Color color) {
        childComponent.setColor(color);
    }

    public void setBackgroundImage(Image image) {
        this.backgroundImage = image;
    }

    @Override
    public void setOffset(Vector2 offset) {
        super.setOffset(offset);
        childComponent.setOffset(new Vector2(offset.getX() + 40, offset.getY() + 30));

        absolutePosition = getAbsolutePosition();
        buttonBounds = new Rectangle(absolutePosition.getX(), absolutePosition.getY(), size.getX(), size.getY());
    }

    @Override
    public void update() {
        if (!active) {
            return;
        }

        super.update();
        if (childComponent != null) childComponent.update();

        buttonListener.listen(buttonBounds, actionListener, this);
    }


    @Override
    public void render(Graphics graphics) {
        if (!active) {
            return;
        }
        super.render(graphics);

        if (backgroundImage != null) {
            graphics.drawImage(backgroundImage, position.getX() + offset.getX(), position.getY() + offset.getY(), size.getX(), size.getY(), null);
        } else {
            graphics.setColor(color);
            graphics.fillRect(position.getX() + offset.getX(), position.getY() + offset.getY(), size.getX(), size.getY());
        }

        if (childComponent != null) childComponent.render(graphics);
    }
}