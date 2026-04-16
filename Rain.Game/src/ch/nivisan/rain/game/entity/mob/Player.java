package ch.nivisan.rain.game.entity.mob;

import ch.nivisan.rain.game.entity.projectile.ShurikenProjectile;
import ch.nivisan.rain.game.entity.projectile.WizardProjectile;
import ch.nivisan.rain.game.events.Event;
import ch.nivisan.rain.game.events.EventDispatcher;
import ch.nivisan.rain.game.events.EventType;
import ch.nivisan.rain.game.events.IEventListener;
import ch.nivisan.rain.game.events.mouse.MousePressedEvent;
import ch.nivisan.rain.game.events.mouse.MouseReleasedEvent;
import ch.nivisan.rain.game.graphics.AnimatedSprite;
import ch.nivisan.rain.game.graphics.Screen;
import ch.nivisan.rain.game.graphics.SpriteSheet;
import ch.nivisan.rain.game.graphics.WindowManager;
import ch.nivisan.rain.game.graphics.gui.PlayerUI;
import ch.nivisan.rain.game.input.Keyboard;
import ch.nivisan.rain.game.input.Mouse;
import ch.nivisan.rain.game.input.MouseButton;
import ch.nivisan.rain.game.inventory.PlayerInventory;
import ch.nivisan.rain.game.level.Level;
import ch.nivisan.rain.game.utils.Debug;

public class Player extends Mob implements IEventListener {
	private static final AnimatedSprite front = new AnimatedSprite(SpriteSheet.playerFront, 32, 32, 3);
	private static final AnimatedSprite back = new AnimatedSprite(SpriteSheet.playerBack, 32, 32, 3);
	private static final AnimatedSprite right = new AnimatedSprite(SpriteSheet.playerRight, 32, 32, 3);
	private static final AnimatedSprite left = new AnimatedSprite(SpriteSheet.playerLeft, 32, 32, 3);
	private final Keyboard input;
	private final String name;
	private final PlayerUI playerUI;
	private final PlayerInventory inventory;
	private final InteractionManager interactionManager;
	int time = 0;
	private float fireRate = 0;
	private boolean shooting = false;
	private AnimatedSprite animatedSprite = front;

	public Player(String name, int x, int y, Keyboard input, Level level) {
		super(level);
		this.name = name;
		this.x = x;
		this.y = y;
		this.input = input;
		this.fireRate = ShurikenProjectile.fireRate;
		walkSpeed = 1.4f;
		health = 0;
		maxHealth = 100;

		inventory = new PlayerInventory(20);
		playerUI = new PlayerUI(this);
		interactionManager = new InteractionManager(input);
	}

	public String getName() {
		return name;
	}

	@Override
	public void onEvent(Event event) {
		EventDispatcher dispatcher = new EventDispatcher(event);
		dispatcher.dispatch(EventType.MOUSE_PRESSED, (e) -> onMousePressed((MousePressedEvent) e));
		dispatcher.dispatch(EventType.MOUSE_RELEASED, (e) -> onMouseReleased((MouseReleasedEvent) e));
	}

	@Override
	public void update() {
		interactionManager.update();
		playerUI.update();

		time++;
		if (time % 20 == 0) {
			if (health < maxHealth)
				health++;
			time = 0;
		}

		if (walking)
			animatedSprite.update();
		else {
			animatedSprite.setFrame(0);
		}
		if (fireRate > 0)
			fireRate--;

		float xAbsolute = 0, yAbsolute = 0;

		if (input.up) {
			yAbsolute -= walkSpeed;
			animatedSprite = back;
		} else if (input.down) {
			yAbsolute += walkSpeed;
			animatedSprite = front;
		}
		if (input.right) {
			xAbsolute += walkSpeed;
			animatedSprite = right;
		} else if (input.left) {
			xAbsolute -= walkSpeed;
			animatedSprite = left;
		}

		if (xAbsolute != 0 || yAbsolute != 0) {
			move(xAbsolute, yAbsolute);
			walking = true;
		} else {
			walking = false;
		}

		if (shooting | fireRate < 0) {
			updateShooting();
		}

	}

	public boolean onMousePressed(MousePressedEvent e) {
		if (e.getX() > (WindowManager.getScaledWindowWidth() - WindowManager.getScaledGUIWidth())
				|| e.getY() > WindowManager.getScaledWindowHeight() || e.getY() < 0 || e.getX() < 0)
			return false;

		if (e.getButton() == MouseButton.Left.getNumValue()) {
			shooting = true;
			return true;
		}
		return false;
	}

	public boolean onMouseReleased(MouseReleasedEvent e) {
		var val1 = e.getButton() == MouseButton.Left.getNumValue();
		var val = val1;
		if (val) {
			shooting = false;
			return true;
		}
		return false;
	}

	/**
	 * projectile calculations
	 */
	private void updateShooting() {
		int midpointWidth = WindowManager.getScaledGameWidth() / 2;
		int midpointHeight = WindowManager.getScaledWindowHeight() / 2;

		float dx = (Mouse.getXPosition() - midpointWidth);
		float dy = (Mouse.getYPosition() - midpointHeight);
		float dir = (float) Math.atan2(dy, dx);

		shoot(x, y, dir);
		fireRate = WizardProjectile.fireRate;
	}

	public void render(Screen screen) {
		float xCenter = x - 16;
		float yCenter = y - 30;

		Debug.drawRectangle(screen, 20 << 4, 60 << 4, 100, 40, 0xff000, true);
		screen.renderMob((int) xCenter, (int) yCenter, animatedSprite.getSprite(), FlipState.None);
	}

	public PlayerInventory getInventory() {
		return inventory;
	}

	public void registerMerchant(Merchant merchant) {
		interactionManager.registerMerchant(merchant, this);
	}
}
