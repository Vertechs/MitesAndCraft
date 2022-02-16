package com.example.mites.datagen;

import com.example.mites.Mites;
import com.example.mites.setup.Registration;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.DataProvider;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraft.tags.Tag;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.data.ExistingFileHelper;

public class TutItemTags extends ItemTagsProvider {
    public TutItemTags(DataGenerator generator, TutBlockTags blockTags, ExistingFileHelper existingFileHelper) {
        super(generator, blockTags, Mites.MODID, existingFileHelper);
    }

    @Override
    protected void addTags(){
        tag(Tags.Items.SAND_COLORLESS)
                .add(Registration.SAND_HOLE_ITEM.get());

        tag(Tags.Items.SEEDS)
                .add(Registration.MITE.get());

        // New tags
        tag(Registration.BURROW_ITEM).add(Registration.SAND_HOLE_ITEM.get());
    }

    @Override
    public String getName(){return "Mites tags";}

}
