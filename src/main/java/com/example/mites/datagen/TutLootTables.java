package com.example.mites.datagen;

import com.example.mites.setup.Registration;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.DataProvider;

public class TutLootTables extends BaseLootTableProvider{
    public TutLootTables(DataGenerator generator) {
        super(generator);
    }

    @Override
    protected void addTables(){
        lootTables.put(Registration.SAND_HOLE.get(), createSilkTouchTable("sand_hole", Registration.SAND_HOLE.get(), Registration.MITE.get(), 1, 1));
    }
}
