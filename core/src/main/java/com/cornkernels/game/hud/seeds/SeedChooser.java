package com.cornkernels.game.hud.seeds;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
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
    private final SeedBank seedBank;
    private final Function<SeedSlot, SeedPacket> packetFactory;
    private final Function<SeedSlot, CursorAttachment> thumbnailFactory;
    private final BooleanSupplier placementMode;

    public SeedChooser(@NonNull SeedSelectionBar tray,
                       @NonNull PlantSelectionMenu menu,
                       @NonNull SeedBank seedBank,
                       @NonNull Function<SeedSlot, SeedPacket> packetFactory,
                       @NonNull Function<SeedSlot, CursorAttachment> thumbnailFactory,
                       @NonNull BooleanSupplier placementMode) {
        this.tray = tray;
        this.menu = menu;
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

    public List<SeedSlot> getChosenSlots() {
        List<SeedSlot> chosen = new ArrayList<>();
        for (SeedSlotContainer container : tray.getContainers()) {
            if (!container.isEmpty()) chosen.add(container.getPacket().getSeedSlot());
        }
        return chosen;
    }

    private void onMenuClicked(@NonNull SeedPacket menuPacket) {
        if (menuPacket.isSelected()) return;
        SeedSlotContainer target = firstEmptyContainer();
        if (target == null) return;
        target.setPacket(packetFactory.apply(menuPacket.getSeedSlot()));
        menuPacket.setSelected(true);
    }

    private void onTrayClicked(@NonNull SeedSlotContainer container) {
        if (container.isEmpty()) return;
        SeedSlot slot = container.getPacket().getSeedSlot();
        if (placementMode.getAsBoolean()) {
            seedBank.select(slot, thumbnailFactory.apply(slot), null);
        } else {
            container.setPacket(null);
            markMenuChosen(slot, false);
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
