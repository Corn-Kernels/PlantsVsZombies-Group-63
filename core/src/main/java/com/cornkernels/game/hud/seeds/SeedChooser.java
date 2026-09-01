package com.cornkernels.game.hud.seeds;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.cornkernels.game.hud.cursor.CursorAttachment;
import com.cornkernels.game.systems.controller.plants.SeedBank;
import com.cornkernels.game.systems.controller.plants.SeedSlot;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BooleanSupplier;
import java.util.function.Function;

public class SeedChooser {

    private final SeedSelectionBar tray;
    private final PlantSelectionMenu menu;
    private final ScrollPane menuScrollPane;
    private final SeedBank seedBank;
    private final Function<SeedSlot, SeedPacket> packetFactory;
    private final Function<SeedSlot, CursorAttachment> thumbnailFactory;
    private final BooleanSupplier placementMode;
    private Runnable onInsufficientSun = () -> {
    };
    private @Nullable SeedPacket pendingMenuPacket;

    public SeedChooser(@NonNull SeedSelectionBar tray,
                       @NonNull PlantSelectionMenu menu,
                       @NonNull ScrollPane menuScrollPane,
                       @NonNull SeedBank seedBank,
                       @NonNull Function<SeedSlot, SeedPacket> packetFactory,
                       @NonNull Function<SeedSlot, CursorAttachment> thumbnailFactory,
                       @NonNull BooleanSupplier placementMode) {
        this.tray = tray;
        this.menu = menu;
        this.menuScrollPane = menuScrollPane;
        this.seedBank = seedBank;
        this.packetFactory = packetFactory;
        this.thumbnailFactory = thumbnailFactory;
        this.placementMode = placementMode;

        menu.setOnPacketClicked(this::onMenuClicked);
        for (SeedSlotContainer container : tray.getContainers()) {
            container.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    onTrayClicked(container);
                }
            });
        }
    }

    public SeedSelectionBar getTray() {
        return tray;
    }

    public PlantSelectionMenu getMenu() {
        return menu;
    }

    public ScrollPane getMenuContainer() {
        return menuScrollPane;
    }

    public void setOnInsufficientSun(@NonNull Runnable onInsufficientSun) {
        this.onInsufficientSun = onInsufficientSun;
    }

    public List<SeedSlot> getChosenSlots() {
        List<SeedSlot> chosen = new ArrayList<>();
        for (SeedSlotContainer container : tray.getContainers()) {
            if (!container.isEmpty()) chosen.add(container.getPacket().getSeedSlot());
        }
        return chosen;
    }

    /**
     * First click on a plant just marks it "pending" (highlighted, and eligible for the Boost
     * button) without moving it into a seed slot yet. Clicking that same pending plant again
     * confirms it, moving it into the first empty tray slot - whatever its boosted status is by
     * then carries over, since the tray packet is built from the same {@link SeedSlot}.
     */
    private void onMenuClicked(@NonNull SeedPacket menuPacket) {
        if (menuPacket.isSelected()) return;

        if (pendingMenuPacket == menuPacket) {
            SeedSlotContainer target = firstEmptyContainer();
            if (target == null) return;
            target.setPacket(packetFactory.apply(menuPacket.getSeedSlot()));
            menuPacket.setSelected(true);
            menuPacket.setPending(false);
            pendingMenuPacket = null;
            return;
        }

        if (pendingMenuPacket != null) {
            pendingMenuPacket.setPending(false);
        }
        pendingMenuPacket = menuPacket;
        menuPacket.setPending(true);
    }

    /** The plant currently awaiting a confirming click, if any - what the Boost button acts on. */
    public @Nullable SeedSlot getPendingSlot() {
        return pendingMenuPacket != null ? pendingMenuPacket.getSeedSlot() : null;
    }

    private void onTrayClicked(@NonNull SeedSlotContainer container) {
        if (container.isEmpty()) return;
        SeedPacket packet = container.getPacket();
        SeedSlot slot = packet.getSeedSlot();

        if (placementMode.getAsBoolean()) {
            if (packet.isSelected()) {
                seedBank.cancelSelection();
                packet.setSelected(false);
            } else if (seedBank.canSelect(slot)) {
                clearTraySelection();
                seedBank.select(slot, thumbnailFactory.apply(slot), null, () -> packet.setSelected(false));
                packet.setSelected(true);
            } else if (slot.isReady() && !seedBank.canAfford(slot)) {
                onInsufficientSun.run();
            }
        } else {
            container.setPacket(null);
            markMenuChosen(slot, false);
        }
    }

    private void clearTraySelection() {
        for (SeedSlotContainer container : tray.getContainers()) {
            if (!container.isEmpty()) container.getPacket().setSelected(false);
        }
    }

    private void markMenuChosen(SeedSlot slot, boolean chosen) {
        for (SeedPacket packet : menu.getPackets()) {
            if (packet.getSeedSlot() == slot) {
                packet.setSelected(chosen);
                return;
            }
        }
    }

    private @Nullable SeedSlotContainer firstEmptyContainer() {
        for (SeedSlotContainer container : tray.getContainers()) {
            if (container.isEmpty()) return container;
        }
        return null;
    }
}
