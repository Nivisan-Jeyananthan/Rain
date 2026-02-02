package ch.nivisan.rain.graphics;


import ch.nivisan.rain.entity.mob.Player;
import ch.nivisan.rain.graphics.gui.UILabel;
import ch.nivisan.rain.graphics.gui.UILabeledProgressbar;
import ch.nivisan.rain.graphics.gui.UIManager;
import ch.nivisan.rain.graphics.gui.UIPanel;
import ch.nivisan.rain.graphics.gui.button.UIButton;
import ch.nivisan.rain.graphics.gui.button.UIButtonActionListener;
import ch.nivisan.rain.graphics.gui.button.UIButtonListener;
import ch.nivisan.rain.graphics.gui.button.UIImageButtonListener;
import ch.nivisan.rain.utils.Vector2;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.DataBuffer;
import java.awt.image.DataBufferByte;
import java.awt.image.DataBufferInt;
import java.io.File;
import java.io.IOException;

public class PlayerUI {
    private static final UIManager uiManager = UIManager.getInstance();
    private UILabeledProgressbar uiHealthBar;
    private final int panelStartX = (WindowManager.getScaledWindowWidth() - WindowManager.getScaledGUIWidth());
    private final UIPanel mainPanel = new UIPanel(new Vector2(panelStartX, 0), new Vector2(WindowManager.getScaledGUIWidth(), WindowManager.getScaledWindowHeight()), 0x4f4f4f);
    private final Player player;

    public PlayerUI(Player player) {
        this.player = player;

        initializeGUI();
    }

    private void initializeGUI(){
        final int divisor = 100;
        final int componentPositionX = (WindowManager.getScaledGUIWidth() / divisor);
        final int maxComponentWidth = (WindowManager.getScaledGUIWidth() / divisor) * (divisor -1);
        final int widthOffset =  (maxComponentWidth / divisor);
        final int height = (WindowManager.getScaledWindowHeight()  / 5)  * 2;
        final int offsetHeight = (height / 10);

        uiManager.addPanel(mainPanel);
        UILabel nameLabel = new UILabel(new Vector2(componentPositionX, height), "Nivisan");
        nameLabel.setColor(0xbbbbbbbb);
        nameLabel.setFont(new Font("Courier New", Font.BOLD, 25));
        nameLabel.setShadow(true);

        Vector2 startPosition = new Vector2(componentPositionX ,height + offsetHeight);
        Vector2 barSize = new Vector2(maxComponentWidth - widthOffset,20);

        uiHealthBar = new UILabeledProgressbar(startPosition,barSize, 0xee3030,0xffffffff,"HP");
        uiHealthBar.setColor(0x6a6a6a);
        uiHealthBar.setShadow(true);

        var pos =  new Vector2(componentPositionX * 25 ,height + (offsetHeight * 2));
        UIButton button = new UIButton(pos, new Vector2(barSize.getX() / 2, barSize.getY() + 20), "Button text", new UIButtonActionListener());
        button.setColor(new Color(0x64A108));
        button.setTextColor(Color.BLACK);

        mainPanel.addComponent(nameLabel);
        mainPanel.addComponent(uiHealthBar);
        mainPanel.addComponent(button);


        String imagePath = "/home/deck/source/Java/Rain/src/ch/nivisan/rain/assets/gui/Home.png";
        BufferedImage image = null, hoverImage = null;
        Vector2 size = new Vector2(maxComponentWidth,maxComponentWidth);
        try {
            image = ImageIO.read(new File(imagePath));
            size = new Vector2(image.getWidth(null),image.getHeight(null));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        // depending on image which was provided the casting can fail
        int[] imagePixels = new int[image.getWidth() * image.getHeight()];
        image.getRGB(0, 0, image.getWidth(),image.getHeight(), imagePixels, 0, image.getWidth());
        hoverImage = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_ARGB);
        int[] hoverdPixels =  ((DataBufferInt) hoverImage.getRaster().getDataBuffer()).getData();


        for (int pixelY = 0; pixelY < image.getHeight(); pixelY++) {
            for (int pixelX = 0; pixelX < image.getWidth(); pixelX++) {
                int color = imagePixels[pixelY * image.getWidth() + pixelX];
                int red = (color & 0xff0000) >> 16;
                int green = (color & 0xff00) >> 8;
                int blue = (color & 0xff);

                red = (red + 50) & 255;
                green= (green + 50) & 255;
                blue = (blue + 50) & 255;
                int brightenedColor = (color & 0xff000000) << 24 | red << 16 | green << 8 | blue;
                hoverdPixels[pixelX + pixelY * image.getWidth()] = brightenedColor;
            }
        }

        UIButton imageButton = new UIButton(new Vector2(10,360),size, image,new UIButtonActionListener(){
            @Override
            public void performAction() {
                System.exit(0);
            }
        });
        imageButton.setButtonListener(new UIImageButtonListener(imageButton,hoverImage));
        mainPanel.addComponent(imageButton);
    }

    public void update(){
        uiHealthBar.setProgress(player.getHealth() / player.getMaxHealth());
    }

    public void render(){

    }
}