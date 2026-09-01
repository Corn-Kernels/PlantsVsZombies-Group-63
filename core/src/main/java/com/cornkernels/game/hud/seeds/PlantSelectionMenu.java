package com.cornkernels.game.hud.seeds;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.WidgetGroup;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.cornkernels.game.systems.controller.plants.SeedSlot;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;

public class PlantSelectionMenu extends WidgetGroup {

    public static final int MAX_PER_ROW = 5;

    private final Image background;
    private final Function<SeedSlot, SeedPacket> packetFactory;
    private final float packetWidth;
    private final float packetHeight;
    private final float padding;
    private final float margin;

    private final List<SeedPacket> packets = new ArrayList<>();

    private Consumer<SeedPacket> onPacketClicked = packet -> {
    };

    public PlantSelectionMenu(Drawable backgroundDrawable,
                              Function<SeedSlot, SeedPacket> packetFactory,
                              float packetWidth,
                              float padding,
                              float margin) {
        this.background = new Image(backgroundDrawable);
        this.packetFactory = packetFactory;
        this.packetWidth = packetWidth;
        this.packetHeight = packetWidth / SeedPacket.ASPECT;
        this.padding = padding;
        this.margin = margin;
        addActor(background);
    }

    public void setOnPacketClicked(Consumer<SeedPacket> listener) {
        this.onPacketClicked = listener != null ? listener : packet -> {
        };
    }

    public void setSeedSlots(List<SeedSlot> seedSlots) {
        for (SeedPacket packet : packets) {
            packet.remove();
        }
        packets.clear();

        for (SeedSlot slot : seedSlots) {
            SeedPacket packet = packetFactory.apply(slot);
            packet.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    onPacketClicked.accept(packet);
                }
            });
            packets.add(packet);
            addActor(packet);
        }

        int columns = Math.clamp(seedSlots.size(), 1, MAX_PER_ROW);
        int rows = (int) Math.ceil(seedSlots.size() / (float) MAX_PER_ROW);
        float contentWidth = columns * packetWidth + (columns - 1) * padding;
        float contentHeight = Math.max(rows, 1) * packetHeight + Math.max(rows - 1, 0) * padding;
        setSize(contentWidth + 2f * margin, contentHeight + 2f * margin);
        invalidate();
    }

    public List<SeedPacket> getPackets() {
        return List.copyOf(packets);
    }

    public List<SeedSlot> getSelectedSlots() {
        List<SeedSlot> result = new ArrayList<>();
        for (SeedPacket packet : packets) {
            if (packet.isSelected()) result.add(packet.getSeedSlot());
        }
        return result;
    }

    @Override
    public float getPrefWidth() {
        return getWidth();
    }

    @Override
    public float getPrefHeight() {
        return getHeight();
    }

    @Override
    public void layout() {
        background.setBounds(0f, 0f, getWidth(), getHeight());

        int rows = (int) Math.ceil(packets.size() / (float) MAX_PER_ROW);
        for (int i = 0; i < packets.size(); i++) {
            int col = i % MAX_PER_ROW;
            int row = i / MAX_PER_ROW;
            float x = margin + col * (packetWidth + padding);
            float y = margin + (rows - 1 - row) * (packetHeight + padding);
            packets.get(i).setBounds(x, y, packetWidth, packetHeight);
        }
        super.layout();
    }
}
