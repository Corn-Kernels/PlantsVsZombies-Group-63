package com.cornkernels.game.entities.components.zombie_specific.specific_specific;

import com.cornkernels.game.entities.types.obstacles.PushableObstacle;
import java.util.ArrayList;
import java.util.List;

public class BullyComponent {
    public List<PushableObstacle> pushStack = new ArrayList<>();
    public List<Integer> initialCols = new ArrayList<>();
    public boolean isPushing = false;
    public int ticksPushing = 0;
    public int cooldownTimer = 0;
}
