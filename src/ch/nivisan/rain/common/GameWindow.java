package ch.nivisan.rain.common;

import ch.nivisan.rain.graphics.layers.Layer;
import ch.nivisan.rain.utils.events.types.MouseMovedEvent;
import ch.nivisan.rain.utils.events.types.MousePressedEvent;
import ch.nivisan.rain.utils.events.types.MouseReleasedEvent;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;
import java.util.List;

public class GameWindow extends JFrame {
    private final WindowScreen windowScreen;
    private final List<Layer> layers = new ArrayList<Layer>();

    public GameWindow(String name, int width, int height){
        windowScreen = new WindowScreen(width,height);

        setTitle(name);
        setResizable(false);
        add(windowScreen);
        pack();
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setVisible(true);

        setupListeners();

        windowScreen.init();
        run();
    }

    private void setupListeners() {
        windowScreen.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e){
                MouseReleasedEvent event = new MouseReleasedEvent(e.getX(), e.getY(),e.getButton());
                onEvent(event);
            }

            @Override
            public void mousePressed(MouseEvent e){
                MousePressedEvent event = new MousePressedEvent(e.getX(), e.getY(),e.getButton());
                onEvent(event);
            }
        });


        windowScreen.addMouseMotionListener(new MouseMotionListener() {
            @Override
            public void mouseDragged(MouseEvent e) {
                MouseMovedEvent event = new MouseMovedEvent(e.getX(), e.getY(),true);
                onEvent(event);
            }

            @Override
            public void mouseMoved(MouseEvent e) {
                MouseMovedEvent event = new MouseMovedEvent(e.getX(), e.getY(),false);
                onEvent(event);
            }
        });
    }


    private void run(){
        windowScreen.beginRendering();
        windowScreen.clear();
        onRender(windowScreen.getGraphics());
        windowScreen.endRendering();
        try {
            Thread.sleep(3);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        SwingUtilities.invokeLater(this::run);
    }

    /**
     * From top to bottom layer
     * @param event
     */
    public void onEvent(ch.nivisan.rain.utils.events.Event event){
        for (int i = layers.size() - 1; i >= 0; i--) {
            layers.get(i).onEvent(event);
        }
    }

    /**
     * from bottom to top layer
     * @param graphics
     */
    private void onRender(Graphics graphics) {
        layers.forEach(x -> {
            x.onRender(graphics);
        });
    }

    public void addLayer(Layer layer){
        layers.add(layer);
    }
}
