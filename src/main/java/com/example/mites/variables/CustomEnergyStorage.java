package com.example.mites.variables;

import net.minecraftforge.energy.EnergyStorage;

public class CustomEnergyStorage extends EnergyStorage {
    public CustomEnergyStorage(int capacity, int maxXfer) { super(capacity, maxXfer, 0);}

    // new method for when energy changes
    protected void onEnergyChanged(){
    }

    @Override
    public int receiveEnergy(int maxRecieve, boolean simulate){
        int rc = super.receiveEnergy(maxRecieve, simulate);
        if (rc > 0 && !simulate){
            onEnergyChanged();
        }
        return rc;
    }

    public void setEnergy(int energy){
        this.energy = energy;
        onEnergyChanged();
    }

    public void addEnergy(int energy){
        this.energy += energy;
        if (this.energy > getMaxEnergyStored()){
            this.energy = getEnergyStored();
        }
        onEnergyChanged();
    }

    public void consumeEnergy(int energy){
        this.energy -= energy;
        if (this.energy < 0){
            this.energy = 0;
        }
        onEnergyChanged();
    }
}
