package io.github.some_example_name.model;

import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;

import java.util.ArrayList;
import java.util.List;
public class Garden{
    private List<GardenPot>pots;
    private int unlockedPotCount;
    private static final int ROWS=3;
    private static final int COLS=4;
    private static final int MAX_POTS=12;
    private static final int UNLOCK_COST=200;

    public Garden(){
        this.pots=new ArrayList<>();
        this.unlockedPotCount=0;
        initializePots();
    }
    private void initializePots(){
        for(int row=1;row<=ROWS;row++){
            for(int col=1;col<=COLS;col++){
                boolean isLocked=(row>=2);
                pots.add(new GardenPot(row,col,isLocked));
            }
        }
    }
    public List<GardenPot>getPots(){return pots;}
    public GardenPot getPot(int row,int col){
        for(GardenPot pot:pots){
            if(pot.getRow()==row&&pot.getCol()==col)
                return pot;
        }
        return null;
    }
    public int getUnlockedPotCount(){return unlockedPotCount;}
    public void unlockPot(int row,int col){
        GardenPot pot=getPot(row,col);
        if(pot!=null&&pot.isLocked()){
            pot.setLocked(false);
            unlockedPotCount++;

        }
    }
    public boolean isFull(){
        return unlockedPotCount>=MAX_POTS;
    }
}
