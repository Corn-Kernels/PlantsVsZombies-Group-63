package com.cornkernels.game.hud.seeds;

import com.badlogic.gdx.scenes.scene2d.ui.WidgetGroup;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;

import java.util.ArrayList;
import java.util.List;

public class SeedSelectionBar extends WidgetGroup {

    public static final int MAX_PER_ROW = 4;

    private final Drawable emptyPacketDrawable;
    private final float slotWidth;
    private final float slotHeight;
    private final float padding;

    private final List<SeedSlotContainer> containers = new ArrayList<>();

    public SeedSelectionBar(Drawable emptyPacketDrawable, float slotWidth, float padding) {
        this.emptyPacketDrawable = emptyPacketDrawable;
        this.slotWidth = slotWidth;
        this.slotHeight = slotWidth / SeedSlotContainer.ASPECT;
        this.padding = padding;
    }

    public List<SeedSlotContainer> createSeedSlots(int amount) {
        clearChildren();
        containers.clear();

        for (int i = 0; i < amount; i++) {
            SeedSlotContainer container = new SeedSlotContainer(emptyPacketDrawable);
            containers.add(container);
            addActor(container);
        }

        int columns = Math.clamp(amount, 1, MAX_PER_ROW);
        int rows = (int) Math.ceil(amount / (float) MAX_PER_ROW);
        float barWidth = columns * slotWidth + (columns - 1) * padding;
        float barHeight = Math.max(rows, 1) * slotHeight + Math.max(rows - 1, 0) * padding;
        setSize(barWidth, barHeight);
        invalidate();

        return List.copyOf(containers);
    }

    public List<SeedSlotContainer> getContainers() {
        return containers;
    }

    public void assignPacket(int index, SeedPacket packet) {
        if (index < 0 || index >= containers.size()) return;
        containers.get(index).setPacket(packet);
    }

    @Override
    public void layout() {
        int rows = (int) Math.ceil(containers.size() / (float) MAX_PER_ROW);
        for (int i = 0; i < containers.size(); i++) {
            int col = i % MAX_PER_ROW;
            int row = i / MAX_PER_ROW;
            float x = col * (slotWidth + padding);
            float y = (rows - 1 - row) * (slotHeight + padding);
            SeedSlotContainer container = containers.get(i);
            container.setBounds(x, y, slotWidth, slotHeight);
        }
        super.layout();
    }
}
