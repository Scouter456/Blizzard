package com.scouter.blizzard.events;

import net.minecraft.nbt.CompoundTag;

public class BlizzardData {
    private int blizzardStrength = 0;
    private boolean hasBlizzard;

    public BlizzardData(int blizzardStrength, boolean hasBlizzard){
        this.hasBlizzard = hasBlizzard;
        this.blizzardStrength = blizzardStrength;
    }
    public int getBlizzardStrength() {
        return blizzardStrength;
    }

    public void setBlizzardStrength(int blizzardStrength) {
        this.blizzardStrength = blizzardStrength;
    }

    public void setHasBlizzard(boolean hasBlizzard) {
        this.hasBlizzard = hasBlizzard;
    }

    public boolean isHasBlizzard() {
        return hasBlizzard;
    }


    public CompoundTag serialize(){
        CompoundTag tag = new CompoundTag();
        tag.putInt("blizzardStrength", blizzardStrength);
        tag.putBoolean("hasBlizzard", hasBlizzard);

        return tag;
    }

    public static BlizzardData deserialize(CompoundTag tag){
        BlizzardData blizzardData = new BlizzardData(tag.getInt("blizzardStrength"), tag.getBoolean("hasBlizzard"));
        return  blizzardData;
    }

    public static BlizzardData defaultData(){
        BlizzardData data = new BlizzardData(0 ,false);
        return data;
    }
}
