package ch.nivisan.rain.graphics.gui.button;

import ch.nivisan.rain.graphics.gui.UIComponent;
import ch.nivisan.rain.graphics.gui.UILabel;
import ch.nivisan.rain.utils.Vector2;

import java.awt.*;

public class UIButton extends UIComponent {
    private final UIComponent childComponent;
    private Image backgroundImage;

    private boolean insideBounds = false;
    private Rectangle buttonBounds;
    private boolean pressed = false;
    private boolean blocked = false;
    private Vector2 absolutePosition = getAbsolutePosition();
    private final UIButtonListener buttonListener = new UIButtonListener(this);
    private final IUIActionListener actionListener;

    public UIButton(Vector2 position, Vector2 size, String text, IUIActionListener actionListener) {
        super(position, size);
        childComponent = new UILabel(position, text);
        this.actionListener = actionListener;
    }

    public UIButton(Vector2 position, String text,  IUIActionListener actionListener) {
        super(position);
        childComponent = new UILabel(position, text);
        this.actionListener = actionListener;
    }

    public UIButton(Vector2 position,Vector2 size,Image image, IUIActionListener actionListener) {
        super(position,size);

        childComponent = new UILabel(position,"");
        this.actionListener = actionListener;
        this.backgroundImage = image;
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
        childComponent.setOffset(new Vector2(offset.getX() + 40,offset.getY() + 30));

        absolutePosition = getAbsolutePosition();
        buttonBounds = new Rectangle(absolutePosition.getX(), absolutePosition.getY(), size.getX(), size.getY());
    }

    @Override
    public void update() {
        if(!active){ return; }

        super.update();
        if(childComponent != null) childComponent.update();

        buttonListener.listen(buttonBounds,actionListener,this);
    }


    @Override
    public void render(Graphics graphics)
    {
       if(!active){ return; }
       super.render(graphics);

        if(backgroundImage != null){
            graphics.drawImage(backgroundImage,position.getX() + offset.getX(), position.getY() + offset.getY(),size.getX(), size.getY(),null);
        }else {
            graphics.setColor(color);
            graphics.fillRect(position.getX() + offset.getX(), position.getY() + offset.getY(), size.getX(), size.getY());
        }

        if(childComponent != null) childComponent.render(graphics);
    }
}
