package com.example.mites.datagen;

import com.example.mites.Mites;
import com.example.mites.setup.Registration;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.DataProvider;
import net.minecraftforge.common.data.LanguageProvider;

import static com.example.mites.setup.ModSetup.TAB_NAME;

public class TutLanguageProvider extends LanguageProvider {
    public TutLanguageProvider(DataGenerator generator, String locale) {
        super(generator, Mites.MODID, locale);
    }

    @Override
    protected void addTranslations(){
        add("itemGroup." + TAB_NAME,"Mites");

        add(Registration.SAND_HOLE.get(), "Hole in the sand");

        add(Registration.MITE.get(), "Mite");
    }
}
