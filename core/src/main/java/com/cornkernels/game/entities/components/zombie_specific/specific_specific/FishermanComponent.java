package com.cornkernels.game.entities.components.zombie_specific.specific_specific;

import com.cornkernels.game.entities.types.plants.PlantInstance;

public class FishermanComponent {
    public int tickCounter = 0;
    public PlantInstance pullingPlant = null;
    public double targetX = -1;
    public boolean fastPull = false;
    public int initialGridCol = -1;
}
