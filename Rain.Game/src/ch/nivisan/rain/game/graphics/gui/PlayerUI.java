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
		final int padding = 14;
		final int componentPositionX = padding;
		final int contentWidth = WindowManager.getScaledGUIWidth() - padding * 2;
		final int healthBarY = 60;

		uiManager.addPanel(mainPanel);
		UILabel nameLabel = new UILabel(new Vector2(componentPositionX, padding), player.getName());
		nameLabel.setColor(0xffffffff);
		nameLabel.setFont(new Font("Courier New", Font.BOLD, 28));
		nameLabel.setShadow(true);

		Vector2 healthPosition = new Vector2(componentPositionX, healthBarY);
		Vector2 barSize = new Vector2(contentWidth, 24);

		uiHealthBar = new UILabeledProgressbar(healthPosition, barSize, 0xee3030, 0xffffffff, "HP");
		uiHealthBar.setColor(0x4f4f4f);
		uiHealthBar.setShadow(true);

		mainPanel.addComponent(nameLabel);
		mainPanel.addComponent(uiHealthBar);

		InventoryGUI inventoryGUI = new InventoryGUI(new Vector2(componentPositionX, healthBarY + barSize.getY() + 26),
				new Vector2(contentWidth, WindowManager.getScaledWindowHeight() - (healthBarY + barSize.getY() + 26) - padding),
				player.getInventory());
		mainPanel.addComponent(inventoryGUI);

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