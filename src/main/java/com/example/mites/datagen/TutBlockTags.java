package com.example.mites.datagen;

import com.example.mites.Mites;
import com.example.mites.setup.Registration;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.tags.BlockTagsProvider;
import net.minecraft.tags.BlockTags;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.data.ExistingFileHelper;

public class TutBlockTags extends BlockTagsProvider {

    public TutBlockTags(DataGenerator generator, ExistingFileHelper helper){
        super(generator, Mites.MODID, helper);
    }

    @Override
    protected void addTags(){
        tag(BlockTags.MINEABLE_WITH_PICKAXE).add(Registration.SAND_HOLE.get());
        tag(BlockTags.NEEDS_STONE_TOOL).add(Registration.SAND_HOLE.get());
        tag(Tags.Blocks.SAND_COLORLESS).add(Registration.SAND_HOLE.get());

        // New tags
        tag(Registration.BURROW).add(Registration.SAND_HOLE.get());


    }

    @Override
    public String getName(){return "tutorial tags";}
}
