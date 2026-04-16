package ch.nivisan.rain.game.graphics.gui;

import java.awt.Color;
import java.awt.Font;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

import ch.nivisan.rain.game.entity.mob.Player;
import ch.nivisan.rain.game.graphics.WindowManager;
import ch.nivisan.rain.game.graphics.gui.components.UILabel;
import ch.nivisan.rain.game.graphics.gui.components.UILabeledProgressbar;
import ch.nivisan.rain.game.graphics.gui.components.UIPanel;
import ch.nivisan.rain.game.graphics.gui.components.button.UIButton;
import ch.nivisan.rain.game.graphics.gui.components.button.UIButtonActionListener;
import ch.nivisan.rain.game.graphics.gui.components.button.UIImageButtonListener;
import ch.nivisan.rain.game.utils.Vector2;

public class PlayerUI {
	public final int panelStartX = (WindowManager.getScaledWindowWidth() - WindowManager.getScaledGUIWidth());
	private static final UIManager uiManager = UIManager.getInstance();
	private static final String imagePath = "../../assets/gui/Home.png";
	private final UIPanel mainPanel = new UIPanel(new Vector2(panelStartX, 0),
			new Vector2(WindowManager.getScaledGUIWidth(), WindowManager.getScaledWindowHeight()), 0x4f4f4f);
	private final Player player;
	private UILabeledProgressbar uiHealthBar;

	public PlayerUI(Player player) {
		this.player = player;

		initializeGUI();
	}

	private void initializeGUI() {
		final int divisor = 100;
		final int componentPositionX = (WindowManager.getScaledGUIWidth() / divisor);
		final int maxComponentWidth = (WindowManager.getScaledGUIWidth() / divisor) * (divisor - 1);
		final int widthOffset = (maxComponentWidth / divisor);
		final int height = (WindowManager.getScaledWindowHeight() / 5) * 2;
		final int offsetHeight = (height / 10);

		uiManager.addPanel(mainPanel);
		UILabel nameLabel = new UILabel(new Vector2(componentPositionX, height), "Nivisan");
		nameLabel.setColor(0xbbbbbbbb);
		nameLabel.setFont(new Font("Courier New", Font.BOLD, 25));
		nameLabel.setShadow(true);

		Vector2 startPosition = new Vector2(componentPositionX, height + offsetHeight);
		Vector2 barSize = new Vector2(maxComponentWidth - widthOffset, 20);

		uiHealthBar = new UILabeledProgressbar(startPosition, barSize, 0xee3030, 0xffffffff, "HP");
		uiHealthBar.setColor(0x6a6a6a);
		uiHealthBar.setShadow(true);

		var buttonPos = new Vector2(componentPositionX * 25, height + (offsetHeight * 2));
		UIButton button = new UIButton(buttonPos, new Vector2(barSize.getX() / 2, barSize.getY() + 20), "Button text",
				new UIButtonActionListener());
		button.setColor(new Color(0x64A108));
		button.setTextColor(Color.BLACK);

		mainPanel.addComponent(nameLabel);
		mainPanel.addComponent(uiHealthBar);
		mainPanel.addComponent(button);
		
		UIPanel inventoryPanel = new UIPanel(new Vector2(startPosition.getX(), height + offsetHeight * 3),
				new Vector2(componentPositionX,barSize.getY() + 20),0xfff);
		mainPanel.addComponent(inventoryPanel);

		BufferedImage image = null, hoverImage = null;
		Vector2 size = new Vector2(maxComponentWidth, maxComponentWidth);
		try {
			image = ImageIO.read(PlayerUI.class.getResource(imagePath));
			size = new Vector2(image.getWidth(null), image.getHeight(null));
		} catch (IOException e) {
			throw new RuntimeException(e);
		}

		UIButton imageButton = new UIButton(new Vector2(10, 360), size, image, new UIButtonActionListener() {
			@Override
			public void performAction() {
				System.exit(0);
			}
		});

		imageButton.setButtonListener(new UIImageButtonListener(imageButton));
		mainPanel.addComponent(imageButton);
	}
	

	public void update() {
		uiHealthBar.setProgress(player.getHealth() / player.getMaxHealth());
	}

	public void render() {
	}
}