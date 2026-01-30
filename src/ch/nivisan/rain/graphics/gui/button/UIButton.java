package ch.nivisan.rain.graphics.gui.button;

import ch.nivisan.rain.graphics.gui.UIComponent;
import ch.nivisan.rain.graphics.gui.UILabel;
import ch.nivisan.rain.utils.Vector2;

import java.awt.*;

public class UIButton extends UIComponent {
    private final UILabel label;

    private boolean insideBounds = false;
    private Rectangle buttonBounds;
    private boolean pressed = false;
    private boolean blocked = false;
    private Vector2 absolutePosition = getAbsolutePosition();
    private final UIButtonListener buttonListener = new UIButtonListener(this);
    private IUIActionListener actionListener;

    public UIButton(Vector2 position, Vector2 size, String text, IUIActionListener actionListener) {
        super(position, size);
        label = new UILabel(position, text);
        this.actionListener = actionListener;
    }

    public UIButton(Vector2 position, String text,  IUIActionListener actionListener) {
        super(position);
        label = new UILabel(position, text);
        this.actionListener = actionListener;
    }

    public void setTextColor(int color) {
        label.setColor(color);
    }

    public void setTextColor(Color color) {
        label.setColor(color);
    }

    @Override
    public void setOffset(Vector2 offset) {
        super.setOffset(offset);
        label.setOffset(new Vector2(offset.getX() + 40,offset.getY() + 30));
        absolutePosition = getAbsolutePosition();
    }

    @Override
    public void update() {
        if(!active){ return; }

        super.update();
        if(label != null) label.update();

        buttonBounds = new Rectangle(absolutePosition.getX(), absolutePosition.getY(), size.getX(), size.getY());
        buttonListener.listen(buttonBounds,actionListener,this);
    }


    @Override
    public void render(Graphics graphics)
    {
       if(!active){ return; }
       super.render(graphics);


       graphics.setColor(color);
       graphics.fillRect(position.getX() + offset.getX(), position.getY() + offset.getY(), size.getX(), size.getY());

        if(label != null) label.render(graphics);
    }
}
