package com.serpenssolida.createtransmission.content.chain;


import com.serpenssolida.createtransmission.CTBlocks;
import com.simibubi.create.AllBlocks;
import com.simibubi.create.api.schematic.requirement.SpecialBlockItemRequirement;
import com.simibubi.create.content.decoration.encasing.CasingBlock;
import com.simibubi.create.content.decoration.encasing.EncasedBlock;
import com.simibubi.create.content.kinetics.base.KineticBlockEntity;
import com.simibubi.create.content.schematics.requirement.ItemRequirement;
import com.tterrag.registrate.util.entry.BlockEntry;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;

import java.util.Objects;
import java.util.function.Supplier;

public class EncasedTransmissionChainBlock extends AbstractTransmissionChainBlock implements EncasedBlock, SpecialBlockItemRequirement
{
	private final Supplier<Block> casing;

	public EncasedTransmissionChainBlock(Properties properties, Supplier<Block> casing)
	{
		super(properties);
		this.casing = casing;
	}

	public static EncasedTransmissionChainBlock getAndesite(Properties properties)
	{
		BlockEntry<CasingBlock> casing = AllBlocks.ANDESITE_CASING;
		Objects.requireNonNull(casing);
		return new EncasedTransmissionChainBlock(properties, casing::get);
	}

	public static EncasedTransmissionChainBlock getBrass(Properties properties)
	{
		BlockEntry<CasingBlock> casing = AllBlocks.BRASS_CASING;
		Objects.requireNonNull(casing);
		return new EncasedTransmissionChainBlock(properties, casing::get);
	}

	@Override
	public InteractionResult use(BlockState state, Level world, BlockPos pos, Player player, InteractionHand hand, BlockHitResult ray)
	{
		return super.use(state, world, pos, player, hand, ray);
	}

	@Override
	public InteractionResult onSneakWrenched(BlockState state, UseOnContext context)
	{
		if (context.getLevel().isClientSide)
			return InteractionResult.SUCCESS;

		BlockState newState = CTBlocks.TRANSMISSION_CHAIN.getDefaultState()
														 .setValue(FACING, state.getValue(FACING))
														 .setValue(CONNECTION_TYPE, state.getValue(CONNECTION_TYPE))
														 .setValue(CONNECTION_SIDE, state.getValue(CONNECTION_SIDE))
														 .setValue(WATERLOGGED, state.getValue(WATERLOGGED));

		context.getLevel().levelEvent(2001, context.getClickedPos(), Block.getId(state));
		KineticBlockEntity.switchToBlockState(context.getLevel(), context.getClickedPos(), newState);
		return InteractionResult.SUCCESS;
	}

	@Override
	public Block getCasing()
	{
		return casing.get();
	}

	@Override
	public void handleEncasing(BlockState state, Level level, BlockPos pos, ItemStack heldItem, Player player, InteractionHand hand, BlockHitResult ray)
	{
		BlockState newState = defaultBlockState()
				.setValue(FACING, state.getValue(FACING))
				.setValue(CONNECTION_TYPE, state.getValue(CONNECTION_TYPE))
				.setValue(CONNECTION_SIDE, state.getValue(CONNECTION_SIDE))
				.setValue(WATERLOGGED, state.getValue(WATERLOGGED));

		KineticBlockEntity.switchToBlockState(level, pos, newState);
	}

	@Override
	public ItemRequirement getRequiredItems(BlockState state, BlockEntity blockEntity)
	{
		return ItemRequirement.of(CTBlocks.TRANSMISSION_CHAIN.getDefaultState(), blockEntity);
	}

	@Override
	public ItemStack getCloneItemStack(BlockGetter blockGetter, BlockPos blockPos, BlockState blockState)
	{
		return CTBlocks.TRANSMISSION_CHAIN.asStack();
	}
}
