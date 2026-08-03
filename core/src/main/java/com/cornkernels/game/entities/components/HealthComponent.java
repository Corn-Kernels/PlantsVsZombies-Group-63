package com.cornkernels.game.entities.components;

import java.util.ArrayList;
import java.util.List;

public class HealthComponent {

    private final static int MAX_ENTITY_HEALTH = 10000;
    private final List<OnHealthChangedListener> listeners = new ArrayList<>();
    public int maxHealth = 100;
    public int currentHealth = maxHealth;

    public void addListener(OnHealthChangedListener listener) {
        if (!listeners.contains(listener)) {
            listeners.add(listener);
        }
    }

    public void removeListener(OnHealthChangedListener listener) {
        listeners.remove(listener);
    }

    public void adjustHealth(int amount) {
        int oldHealth = currentHealth;
        currentHealth = Math.clamp(currentHealth + amount, 0, maxHealth);

        if (currentHealth != oldHealth) {
            notifyHealthChangeListeners(currentHealth - oldHealth);
        }
    }

    private void notifyHealthChangeListeners(int delta) {
        for (OnHealthChangedListener listener : listeners) {
            listener.OnHealthChanged(currentHealth, maxHealth, delta);
        }
    }

    private void notifyMaxHealthChangeListeners(int delta) {
        for (OnHealthChangedListener listener : listeners) {
            listener.onMaxHealthChanged(maxHealth, delta);
        }
    }

    public void adjustMaxHealth(int amount) {
        int oldMaxHealth = maxHealth;
        maxHealth = Math.clamp(maxHealth + amount, 0, MAX_ENTITY_HEALTH);
        currentHealth = Math.min(currentHealth, maxHealth);
        if (maxHealth != oldMaxHealth) {
            notifyMaxHealthChangeListeners(oldMaxHealth - maxHealth);
        }
    }

    public boolean isDead() {
        return currentHealth <= 0;
    }

    public interface OnHealthChangedListener {
        /**
         * Notifies subscribers when health has changed.
         *
         * @param health
         * @param maxHealth
         * @param delta     The difference between the old health and the new health;
         */
        void OnHealthChanged(int health, int maxHealth, int delta);

        void onMaxHealthChanged(int maxHealth, int delta);
    }

}
