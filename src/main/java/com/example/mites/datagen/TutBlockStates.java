package com.example.mites.datagen;

import com.example.mites.Mites;
import com.example.mites.setup.Registration;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.DataProvider;
import net.minecraftforge.client.model.generators.BlockStateProvider;
import net.minecraftforge.common.data.ExistingFileHelper;

public class TutBlockStates extends BlockStateProvider {

    public TutBlockStates(DataGenerator generator, ExistingFileHelper helper) {
        super(generator, Mites.MODID, helper);

    }

    @Override
    protected void registerStatesAndModels(){
        simpleBlock(Registration.SAND_HOLE.get());

    }

}
