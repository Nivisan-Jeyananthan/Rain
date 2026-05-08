package ch.nivisan.rain.game.input;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

/**
 * Keyboard class implements KeyListener to handle keyboard input for the game. 
 * It maintains an array of boolean values to track which keys are currently pressed,
 * and provides public boolean variables for common actions such as movement and interaction.
 */
public class Keyboard implements KeyListener {
    private final boolean[] keys = new boolean[256];
    public boolean up, down, left, right, interact;


    public void update() {
        up = keys[KeyEvent.VK_UP] || keys[KeyEvent.VK_W];
        down = keys[KeyEvent.VK_DOWN] || keys[KeyEvent.VK_S];
        left = keys[KeyEvent.VK_LEFT] || keys[KeyEvent.VK_A];
        right = keys[KeyEvent.VK_RIGHT] || keys[KeyEvent.VK_D];
        interact = keys[KeyEvent.VK_E];
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyPressed(KeyEvent e) {
        keys[e.getKeyCode()] = true;
    }

    @Override
    public void keyReleased(KeyEvent e) {
        keys[e.getKeyCode()] = false;
    }
}
