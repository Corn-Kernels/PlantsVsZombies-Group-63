package io.github.some_example_name.model;

import java.time.LocalDateTime;
import java.time.Duration;

public class GardenPot{
    private int row;
    private int col;
    private boolean isLocked;
    private String plantType;
    private LocalDateTime plantedAt;
    private LocalDateTime readyAt;
    private boolean isReady;

    private static final int MARIGOLD_GROWTH_HOURS = 2;
    private static final int OTHER_PLANT_GROWTH_HOURS = 8;
    public GardenPot(int row,int col,boolean isLocked){
        this.row=row;
        this.col=col;
        this.isLocked=isLocked;
        this.plantType=null;
        this.plantedAt=null;
        this.readyAt=null;
        this.isReady=false;
    }
    public int getRow(){return row;}
    public int getCol(){return col;}
    public boolean isLocked(){return isLocked;}
    public String getPlantType(){return plantType;}
    public LocalDateTime getPlantedAt(){return plantedAt;}
    public LocalDateTime getReadyAt(){return readyAt;}
    public boolean isReady(){return isReady;}
    public boolean isEmpty(){return plantType==null;}

    public void setLocked(boolean locked){isLocked=locked;}

    public void plant(String plantType){
        this.plantType=plantType;
        this.plantedAt=LocalDateTime.now();
        int growthHours = plantType.equals("MARIGOLD") ? MARIGOLD_GROWTH_HOURS : OTHER_PLANT_GROWTH_HOURS;
        this.readyAt = plantedAt.plusHours(growthHours);
        this.isReady = false;
    }
    public void checkReady(){
        if(plantType!=null&&readyAt!=null){
            if(LocalDateTime.now().isAfter(readyAt)||LocalDateTime.now().isEqual(readyAt))
                this.isReady=true;
        }
    }
    public void clear(){
        this.plantType=null;
        this.plantedAt=null;
        this.readyAt=null;
        this.isReady=false;
    }
    public long getHoursRemaining(){
        if(readyAt==null||isReady)return 0;
        long hours = Duration.between(LocalDateTime.now(), readyAt).toHours();
        return Math.max(0,hours);
    }
    public String getStatusDisplay(){
        if(isLocked)return "LOCKED";
        if(plantType==null)return "EMPTY";
        if(isReady)return " READY - " + plantType;
        long hours=getHoursRemaining();
        if(hours>0)
            return  plantType + " (" + hours + "h left)";
        return  plantType + " (soon)";
    }
}
