package io.github.some_example_name.model;

public class GardenPot {
    private int row;
    private int col;
    private boolean isLocked;
    private String plantType;
    private long plantedAt;
    private long readyAt;
    private boolean isReady;

    private static final int MARIGOLD_GROWTH_HOURS = 2;
    private static final int OTHER_PLANT_GROWTH_HOURS = 8;

    public GardenPot(int row, int col, boolean isLocked) {
        this.row = row;
        this.col = col;
        this.isLocked = isLocked;
        this.plantType = null;
        this.plantedAt = 0;
        this.readyAt = 0;
        this.isReady = false;
    }

    public int getRow() { return row; }
    public int getCol() { return col; }
    public boolean isLocked() { return isLocked; }
    public String getPlantType() { return plantType; }
    public long getPlantedAt() { return plantedAt; }
    public long getReadyAt() { return readyAt; }
    public boolean isReady() { return isReady; }
    public boolean isEmpty() { return plantType == null; }

    public void setLocked(boolean locked) { isLocked = locked; }

    public void plant(String plantType) {
        this.plantType = plantType;
        this.plantedAt = System.currentTimeMillis();
        int growthHours = plantType.equals("MARIGOLD") ? MARIGOLD_GROWTH_HOURS : OTHER_PLANT_GROWTH_HOURS;
        this.readyAt = plantedAt + (growthHours * 60 * 60 * 1000L);
        this.isReady = false;
    }

    public void checkReady() {
        if (plantType != null && readyAt > 0) {
            if (System.currentTimeMillis() >= readyAt) {
                this.isReady = true;
            }
        }
    }

    public void clear() {
        this.plantType = null;
        this.plantedAt = 0;
        this.readyAt = 0;
        this.isReady = false;
    }

    public long getHoursRemaining() {
        if (readyAt == 0 || isReady) return 0;
        long diff = readyAt - System.currentTimeMillis();
        return Math.max(0, diff / (60 * 60 * 1000));
    }

    public String getStatusDisplay() {
        if (isLocked) return "LOCKED";
        if (plantType == null) return "EMPTY";
        if (isReady) return " READY - " + plantType;
        long hours = getHoursRemaining();
        if (hours > 0) return plantType + " (" + hours + "h left)";
        return plantType + " (soon)";
    }
}
