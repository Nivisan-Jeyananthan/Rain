package ch.nivisan.rain.graphics.gui;

import ch.nivisan.rain.utils.Vector2;

import java.awt.*;

public class UIButton extends UIComponent{
    private IUIButtonListener buttonListener;
    private UILabel label;

    public UIButton(Vector2 position, Vector2 size, String text) {
        super(position);
        this.size = size;
        label = new UILabel(position, text);
    }

    public UIButton(Vector2 position, String text) {
        super(position);
        label = new UILabel(position, text);
    }

    public void setTextColor(int color) {
        label.setColor(color);
    }

    public void setTextColor(Color color) {
        label.setColor(color);
    }

    @Override
    public void update() {
        super.update();

        label.setOffset(new Vector2(offset.getX() + 40,offset.getY() + 30));
        label.update();
    }

    @Override
    public void render(Graphics graphics)
    {
        super.render(graphics);

        graphics.setColor(color);
        graphics.fillRect(position.getX() + offset.getX(), position.getY() + offset.getY(), size.getX(), size.getY());

        if(label.active)
            label.render(graphics);
    }
}
