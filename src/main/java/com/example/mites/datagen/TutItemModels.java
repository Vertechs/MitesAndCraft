package com.example.mites.datagen;

import com.example.mites.Mites;
import com.example.mites.setup.Registration;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.DataProvider;
import net.minecraftforge.client.model.generators.BlockStateProvider;
import net.minecraftforge.client.model.generators.ItemModelBuilder;
import net.minecraftforge.client.model.generators.ItemModelProvider;
import net.minecraftforge.common.data.ExistingFileHelper;

public class TutItemModels extends ItemModelProvider {
    public TutItemModels(DataGenerator generator, ExistingFileHelper existingFileHelper) {
        super(generator, Mites.MODID, existingFileHelper);
    }

    @Override
    protected void registerModels(){

        // ResoruceLocation RL = new ResourceLocation(Mod.MODID, "name" *OR* "block/name") **in registration

        // Block models
        withExistingParent(Registration.SAND_HOLE.get().getRegistryName().getPath(), modLoc("block/sand_hole"));

        // Item models
        singleTexture(Registration.MITE.get().getRegistryName().getPath(), mcLoc("item/generated"),
                "Layer0", modLoc("item/mite"));

    }

}
