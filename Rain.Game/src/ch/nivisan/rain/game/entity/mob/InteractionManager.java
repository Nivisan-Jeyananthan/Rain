package ch.nivisan.rain.game.entity.mob;

import java.util.ArrayList;
import java.util.List;

import ch.nivisan.rain.game.input.Keyboard;

public class InteractionManager {
    private List<MerchantInteraction> interactions = new ArrayList<>();
    private MerchantInteraction currentInteraction = null;
    private Keyboard keyboard;
    private boolean interactKeyLastPressed = false;

    public InteractionManager(Keyboard keyboard) {
        this.keyboard = keyboard;
    }

    public void registerMerchant(Merchant merchant, Player player) {
        interactions.add(new MerchantInteraction(merchant, player));
    }

    public void update() {
        boolean interactKeyPressed = keyboard.interact;
        
        if (interactKeyPressed && !interactKeyLastPressed) {
            handleInteractionKeyPress();
        }
        interactKeyLastPressed = interactKeyPressed;

        // Check for nearby merchants
        for (MerchantInteraction interaction : interactions) {
            if (interaction.canInteract() && currentInteraction == null) {
                currentInteraction = interaction;
            }
            if (currentInteraction != interaction && currentInteraction != null && currentInteraction.isShopOpen()) {
                currentInteraction.closeShop();
                currentInteraction = null;
            }
        }

        // Close shop if we're out of range
        if (currentInteraction != null && !currentInteraction.canInteract()) {
            currentInteraction.closeShop();
            currentInteraction = null;
        }
    }

    private void handleInteractionKeyPress() {
        if (currentInteraction != null) {
            if (currentInteraction.isShopOpen()) {
                currentInteraction.closeShop();
            } else {
                currentInteraction.openShop();
            }
        }
    }

    public MerchantInteraction getCurrentMerchant() {
        return currentInteraction;
    }
}