package com.example.mites.blocks;

import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.network.NetworkHooks;

import javax.annotation.Nullable;
import java.util.List;


public class PowerMakerBlock extends Block implements EntityBlock {

    // Translatable messages, attach in language maker
    public static final String MESSAGE_POWERMAKER = "message.generator";
    public static final String SCREEN_TUTORIAL_POWERMAKER = "screan.tutorial.power_maker";

    public static final VoxelShape RENDER_SHAPE = Shapes.box(0,0,0,1,1,0.3);

    public PowerMakerBlock(){
        super(Properties.of(Material.PISTON)
                .sound(SoundType.AMETHYST)
                .strength(2.0f)
                .lightLevel(state -> state.getValue(BlockStateProperties.POWERED) ? 14 : 0)
        );
    }

    // Make slightely smaller?
    @Override
    public VoxelShape getOcclusionShape(BlockState state, BlockGetter reader, BlockPos pos) {return RENDER_SHAPE;}

    // Tool tip when hover over
    @Override
    public void appendHoverText(ItemStack stack, @Nullable BlockGetter reader, List<Component> list, TooltipFlag flags){
        list.add(new TranslatableComponent(MESSAGE_POWERMAKER, Integer.toString(PowerMakerBE.POWERMAKER_GENERATE))
                .withStyle(ChatFormatting.BLUE));
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState){
        return new PowerMakerBE(blockPos, blockState);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type){
        if (level.isClientSide()){
            return null;
        }
        return (lvl, pos, blockState, t) -> {
            if (t instanceof PowerMakerBE tile){
                tile.tickServer();
            }
        };
    }

    //Override creating state to add support for POWERED block state
    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder){
        super.createBlockStateDefinition(builder);
        builder.add(BlockStateProperties.POWERED);
    }

    // make sure is inactive when placed
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext pContext) {
        return super.getStateForPlacement(pContext).setValue(BlockStateProperties.POWERED, false);
    }

    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult trace){
        // Open UI on server, for networking or something
        if (!level.isClientSide){
            BlockEntity tileEntity = level.getBlockEntity(pos);
            if (tileEntity instanceof PowerMakerBE){
                MenuProvider containerProvider = new MenuProvider() {
                    @Override
                    public Component getDisplayName() {
                        return new TranslatableComponent(SCREEN_TUTORIAL_POWERMAKER);
                    }

                    @Override
                    public AbstractContainerMenu createMenu(int pContainerId, Inventory pInventory, Player pPlayer) {
                        return new PowerMakerContainer(pContainerId, pos, pInventory, pPlayer);
                    }
                };
                // Lambda so can add more data??
                NetworkHooks.openGui((ServerPlayer) player, containerProvider, tileEntity.getBlockPos());
            } else {
                throw new IllegalStateException("Container provider is missing!");
            }

        }
        return InteractionResult.SUCCESS;
    }


}
