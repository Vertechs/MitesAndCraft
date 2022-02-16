package com.example.mites.blocks;

import com.example.mites.setup.Registration;
import com.example.mites.variables.CustomEnergyStorage;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.security.DrbgParameters;
import java.util.concurrent.atomic.AtomicInteger;

public class PowerMakerBE extends BlockEntity {

    public static final int POWERMAKER_CAPACITY = 50000;
    public static final int POWERMAKER_GENERATE = 60;
    public static final int POWERMAKER_SEND = 200;

    // Capabilities, item and energy, must be stored in lazy optional for forge capabilities, internal vs external
    private final ItemStackHandler itemHandler = createHandler();
    private final LazyOptional<IItemHandler> handler = LazyOptional.of(() -> itemHandler);

    private final CustomEnergyStorage energyStorage = createEnergy();
    private final LazyOptional<IEnergyStorage> energy = LazyOptional.of(() -> energyStorage);

    private int counter;

    public PowerMakerBE(BlockPos position , BlockState state){
        super(Registration.POWER_MAKER_BE.get(), position, state);

    }

    @Override
    public void setRemoved(){
        super.setRemoved();
        handler.invalidate();
        energy.invalidate();
    }

    // Called from ticker in block
    public void tickServer(){
        if (counter > 0){
            energyStorage.addEnergy(POWERMAKER_GENERATE);
            counter--;
            setChanged();
        }

        if (counter <= 0){
            ItemStack stack = itemHandler.getStackInSlot(0);
            int burnTime = ForgeHooks.getBurnTime(stack, RecipeType.SMELTING);
            if (burnTime > 0){
                itemHandler.extractItem(0, 1, false);
                counter = burnTime;
                setChanged();
            }
        }

        BlockState blockState = level.getBlockState(worldPosition);
        if (blockState.getValue(BlockStateProperties.POWERED) != counter > 0){
            level.setBlock(worldPosition, blockState.setValue(BlockStateProperties.POWERED, counter > 0),
                    Block.UPDATE_ALL);
        }

        sendPower();
    }

    private void sendPower() {
        // Atomic so power can decrease from lambda later in function
        AtomicInteger capacity = new AtomicInteger(energyStorage.getEnergyStored());
        if (capacity.get() > 0) {
            // Iterate over all directions
            for (Direction direction : Direction.values()) {
                BlockEntity be = level.getBlockEntity(worldPosition.relative(direction));
                // if theres and entity near us, get energy, put energy from opposite side
                if (be != null) {
                    boolean doContinue = be.getCapability(CapabilityEnergy.ENERGY, direction.getOpposite()).map(handler -> {
                                if (handler.canReceive()) {
                                    // check if handler, send min of capacity to recieve or send, subtract from internal
                                    int received = handler.receiveEnergy(Math.min(capacity.get(), POWERMAKER_SEND), false);
                                    capacity.addAndGet(-received);
                                    energyStorage.consumeEnergy(received);
                                    setChanged();
                                    return capacity.get() > 0;
                                } else {
                                    return true;
                                }
                            }
                    ).orElse(true);
                    // dont check if no more power in block
                    if (!doContinue) {
                        return;
                    }
                }
            }
        }
    }

    // Load method for block
    @Override
    public void load(CompoundTag tag) {
        if (tag.contains("Inventory")) {
            itemHandler.deserializeNBT(tag.getCompound("Inventory"));
        }
        if (tag.contains("Energy")) {
            energyStorage.deserializeNBT(tag.get("Energy"));
        }
        if (tag.contains("Info")) {
            counter = tag.getCompound("Info").getInt("Counter");
        }
        super.load(tag);
    }

    // Save method for block, use saveAdditional so not override save
    @Override
    public void saveAdditional(CompoundTag tag) {
        tag.put("Inventory", itemHandler.serializeNBT());
        tag.put("Energy", energyStorage.serializeNBT());

        // New tag to track counter
        CompoundTag infoTag = new CompoundTag();
        infoTag.putInt("Counter", counter);
        tag.put("Info", infoTag);
    }

    private ItemStackHandler createHandler(){
        return new ItemStackHandler(1){

            @Override
            protected void onContentsChanged(int slot){
                // Mark as dirty (need saving) when item handler changes
                setChanged();
            }

            @Override
            public boolean isItemValid(int slot, @Nonnull ItemStack stack){
                return ForgeHooks.getBurnTime(stack, RecipeType.SMELTING) > 0;
            }

            // Check if can burn, return insert method if so, return items if not
            // Simualte boolean returns like normal but not active, for conduit checks etc
            @Override
            public ItemStack insertItem(int slot, @Nonnull ItemStack stack, boolean simulate){
                if (ForgeHooks.getBurnTime(stack, RecipeType.SMELTING) <=0 ){
                    return stack;
                }
                return super.insertItem(slot, stack, simulate);
            }

        };
    }

    private CustomEnergyStorage createEnergy() {
        return new CustomEnergyStorage(POWERMAKER_CAPACITY, 0) {
            @Override
            protected void onEnergyChanged() {
                setChanged();
            }
        };
    }

    // No lazy option in somewhere?, this should be very fast
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side){
        if ( cap == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY){
            return handler.cast();
        }
        if ( cap == CapabilityEnergy.ENERGY){
            return energy.cast();
        }
        return super.getCapability(cap, side);
    }

}
