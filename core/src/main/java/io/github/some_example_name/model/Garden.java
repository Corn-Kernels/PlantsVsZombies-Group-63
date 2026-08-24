package io.github.some_example_name.model;

import java.util.ArrayList;
import java.util.List;

public class Garden {
    private List<GardenPot> pots;
    private static final int ROWS = 3;      // 3 ردیف
    private static final int COLS = 4;      // 4 ستون
    private static final int MAX_POTS = 12; // 12 گلدان

    public Garden() {
        this.pots = new ArrayList<>();
        initializePots();
    }

    private void initializePots() {
        for (int row = 1; row <= ROWS; row++) {
            for (int col = 1; col <= COLS; col++) {
                // ردیف اول: ۲ تا آنلاک، بقیه لاک
                boolean isLocked = !(row == 1 && col <= 2);
                pots.add(new GardenPot(row, col, isLocked));
            }
        }
    }

    public List<GardenPot> getPots() { return pots; }

    public GardenPot getPot(int row, int col) {
        for (GardenPot pot : pots) {
            if (pot.getRow() == row && pot.getCol() == col) {
                return pot;
            }
        }
        return null;
    }

    // ===== تعداد گلدان‌های آنلاک‌شده =====
    public int getUnlockedPotCount() {
        int count = 0;
        for (GardenPot pot : pots) {
            if (!pot.isLocked()) count++;
        }
        return count;
    }

    // ===== تعداد کل گلدان‌ها =====
    public int getTotalPotCount() {
        return MAX_POTS;
    }

    public boolean isFull() {
        return getUnlockedPotCount() >= MAX_POTS;
    }
}
