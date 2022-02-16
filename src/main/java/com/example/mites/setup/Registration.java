package com.example.mites.setup;

import com.example.mites.Mites;
import com.example.mites.blocks.PowerMakerBE;
import com.example.mites.blocks.PowerMakerBlock;
import com.example.mites.blocks.PowerMakerContainer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.Container;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.Material;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.extensions.IForgeMenuType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.IRegistryDelegate;
import net.minecraftforge.registries.RegistryObject;

import static com.example.mites.Mites.MODID;

public class Registration {

    private static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, MODID);
    private static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, MODID);
    private static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES = DeferredRegister.create(ForgeRegistries.BLOCK_ENTITIES, MODID);
    private static final DeferredRegister<MenuType<?>> CONTAINERS = DeferredRegister.create(ForgeRegistries.CONTAINERS, MODID);

    public static void init(){
        IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();
        BLOCKS.register(bus);
        ITEMS.register(bus);
    }

    // Blockthing::new  =====  () ->  new Blockthing()

    // Common properties
    public static final Item.Properties ITEM_PROPERTIES = new Item.Properties().tab(ModSetup.ITEM_GROUP);
    public static final BlockBehaviour.Properties BLOCK_PROPERTIES = BlockBehaviour.Properties.of(Material.DIRT).strength(2f);

    // Blocks and block items
    public static final RegistryObject<Block> SAND_HOLE = BLOCKS.register("sand_hole", () -> new Block(BLOCK_PROPERTIES));
    public static final RegistryObject<Item> SAND_HOLE_ITEM = fromBlock(SAND_HOLE);

    // Fancy blocks
    public static final RegistryObject<PowerMakerBlock> POWER_MAKER = BLOCKS.register("power_maker", PowerMakerBlock::new);
    public static final RegistryObject<Item> POWER_MAKER_ITEM = fromBlock(POWER_MAKER);
    public static final RegistryObject<BlockEntityType<PowerMakerBE>> POWER_MAKER_BE = BLOCK_ENTITIES.register("power_maker",
            () -> BlockEntityType.Builder.of(PowerMakerBE::new, POWER_MAKER.get()).build(null));
    public static final RegistryObject<MenuType<PowerMakerContainer>> POWER_MAKER_CONTAINER = CONTAINERS.register("power_maker",
            () -> IForgeMenuType.create((windowId, inv, data) -> new PowerMakerContainer(windowId,data.readBlockPos(),inv,inv.player)));

    // Items
    public static final RegistryObject<Item> MITE = ITEMS.register("mite", () -> new Item(ITEM_PROPERTIES));

    // New tags
    public static final Tags.IOptionalNamedTag<Block> BURROW = BlockTags.createOptional(new ResourceLocation(Mites.MODID, "burrows"));
    public static final Tags.IOptionalNamedTag<Item> BURROW_ITEM = ItemTags.createOptional(new ResourceLocation(Mites.MODID, "burrows"));

    public static <B extends Block> RegistryObject<Item> fromBlock(RegistryObject<B> block){
        return ITEMS.register(block.getId().getPath(), () -> new BlockItem(block.get(), ITEM_PROPERTIES));
    }

}
