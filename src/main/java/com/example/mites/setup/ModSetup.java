package com.example.mites.setup;

import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.CreativeModeTab;

public class ModSetup {

    public static final String TAB_NAME = "mites";

    public static final CreativeModeTab ITEM_GROUP = new CreativeModeTab(TAB_NAME) {
        @Override
        public ItemStack makeIcon() {return new ItemStack(Items.SUNFLOWER); }
    };


    public static void init(FMLCommonSetupEvent event){

    }
}
