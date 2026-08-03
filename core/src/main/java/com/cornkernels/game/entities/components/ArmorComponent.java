package com.cornkernels.game.entities.components;

import com.cornkernels.game.entities.types.zombies.armors.ArmorType;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class ArmorComponent {

    public final ArmorType armorType;
    private final List<OnArmorChangedListener> listeners = new ArrayList<>();
    public int currentArmorHealth;

    public ArmorComponent(@NotNull ArmorType armorType) {
        this.armorType = armorType;
        this.currentArmorHealth = armorType.getArmorDamage();
    }

    public void addListener(OnArmorChangedListener listener) {
        if (!listeners.contains(listener)) {
            listeners.add(listener);
        }
    }

    public void removeListener(OnArmorChangedListener listener) {
        listeners.remove(listener);
    }

    public int absorbDamage(int amount) {
        if (isDestroyed() || amount <= 0) {
            return amount;
        }

        int oldHealth = currentArmorHealth;
        int overflow = Math.max(0, amount - oldHealth);
        currentArmorHealth = Math.clamp(currentArmorHealth - amount, 0, armorType.getArmorDamage());

        if (currentArmorHealth != oldHealth) {
            notifyArmorChangeListeners(currentArmorHealth - oldHealth);
        }
        if (isDestroyed()) {
            notifyArmorDestroyedListeners();
        }

        return overflow;
    }

    private void notifyArmorChangeListeners(int delta) {
        for (OnArmorChangedListener listener : listeners) {
            listener.onArmorChanged(currentArmorHealth, armorType.getArmorDamage(), delta);
        }
    }

    private void notifyArmorDestroyedListeners() {
        for (OnArmorChangedListener listener : listeners) {
            listener.onArmorDestroyed(armorType);
        }
    }

    public boolean isDestroyed() {
        return currentArmorHealth <= 0;
    }

    public boolean isMetallic() {
        return armorType.isMetallic();
    }

    public interface OnArmorChangedListener {
        /**
         * Notifies subscribers when armor health has changed.
         *
         * @param armorHealth
         * @param maxArmorHealth
         * @param delta          The difference between the old armor health and the new armor health;
         */
        void onArmorChanged(int armorHealth, int maxArmorHealth, int delta);

        /**
         * Notifies subscribers when the armor has been fully destroyed — e.g. to
         * trigger a helmet-popping-off effect/sound, or to remove this component
         * from the entity.
         */
        void onArmorDestroyed(ArmorType armorType);
    }
}
