package com.cornkernels.game.hud.seeds;

import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.WidgetGroup;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.utils.Scaling;

public class SeedSlotContainer extends WidgetGroup {

    public static final float ASPECT = SeedPacket.ASPECT;

    private final Image emptyBackground;
    private SeedPacket packet;

    public SeedSlotContainer(Drawable emptyPacketDrawable) {
        this.emptyBackground = new Image(emptyPacketDrawable);
        this.emptyBackground.setScaling(Scaling.stretch);
        addActor(emptyBackground);
    }

    public boolean isEmpty() {
        return packet == null;
    }

    public SeedPacket getPacket() {
        return packet;
    }

    public void setPacket(SeedPacket packet) {
        if (this.packet != null) {
            this.packet.remove();
        }
        this.packet = packet;
        if (packet != null) {
            addActor(packet);
        }
        emptyBackground.setVisible(packet == null);
        invalidate();
    }

    @Override
    public void layout() {
        emptyBackground.setBounds(0f, 0f, getWidth(), getHeight());
        if (packet != null) {
            packet.setBounds(0f, 0f, getWidth(), getHeight());
        }
        super.layout();
    }

    @Override
    public float getPrefWidth() {
        return getWidth();
    }

    @Override
    public float getPrefHeight() {
        return getHeight();
    }
}
