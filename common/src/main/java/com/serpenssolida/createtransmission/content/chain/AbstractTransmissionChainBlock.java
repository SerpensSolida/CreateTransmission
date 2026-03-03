package com.serpenssolida.createtransmission.content.chain;

import com.serpenssolida.createtransmission.CTBlockEntities;
import com.serpenssolida.createtransmission.CTBlocks;
import com.serpenssolida.createtransmission.content.chain.TransmissionChainHelpers.ChainConnection;
import com.serpenssolida.createtransmission.content.chain.TransmissionChainHelpers.ChainDirection;
import com.serpenssolida.createtransmission.content.chain.TransmissionChainHelpers.ChainSide;
import com.serpenssolida.createtransmission.content.chain.TransmissionChainHelpers.ConnectionType;
import com.simibubi.create.content.kinetics.base.KineticBlock;
import com.simibubi.create.content.kinetics.base.KineticBlockEntity;
import com.simibubi.create.content.kinetics.belt.BeltBlock;
import com.simibubi.create.content.kinetics.belt.BeltBlockEntity;
import com.simibubi.create.foundation.block.IBE;
import com.simibubi.create.foundation.block.ProperWaterloggedBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;

import java.util.List;

import static com.serpenssolida.createtransmission.CTShapes.*;

public abstract class AbstractTransmissionChainBlock extends KineticBlock implements IBE<TransmissionChainBlockEntity>, ProperWaterloggedBlock
{
	public static final DirectionProperty FACING = DirectionProperty.create("facing");
	public static final EnumProperty<ConnectionType> CONNECTION_TYPE = EnumProperty.create("connection_type", ConnectionType.class);
	public static final EnumProperty<ChainSide> CONNECTION_SIDE = EnumProperty.create("connection_side", ChainSide.class);

	protected AbstractTransmissionChainBlock(Properties properties)
	{
		super(properties);

		this.registerDefaultState(this.getStateDefinition()
									  .any()
									  .setValue(FACING, Direction.NORTH)
									  .setValue(CONNECTION_TYPE, ConnectionType.NONE)
									  .setValue(CONNECTION_SIDE, ChainSide.RIGHT)
									  .setValue(WATERLOGGED, false));
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder)
	{
		super.createBlockStateDefinition(builder.add(FACING, CONNECTION_TYPE, CONNECTION_SIDE, WATERLOGGED));
	}

	@Override
	public @NotNull List<ItemStack> getDrops(@NotNull BlockState state, LootParams.@NotNull Builder builder)
	{
		return List.of(new ItemStack(CTBlocks.TRANSMISSION_CHAIN.get()));
	}

	@Override
	public InteractionResult use(BlockState state, Level world, BlockPos pos, Player player, InteractionHand hand, BlockHitResult ray)
	{
		return InteractionResult.PASS;
	}

	@Override
	public boolean canSurvive(BlockState state, LevelReader world, BlockPos pos)
	{
		Direction facing = state.getValue(FACING);
		BlockPos otherPos = pos.offset(facing.getNormal());
		BlockEntity otherEntity = world.getBlockEntity(otherPos);

		if (otherEntity instanceof BeltBlockEntity)
		{
			BeltBlock beltBlock = (BeltBlock) otherEntity.getBlockState().getBlock();
			return beltBlock.hasShaftTowards(world, otherPos, otherEntity.getBlockState(), facing.getOpposite());
		}

		return false;
	}

	@Override
	public InteractionResult onWrenched(BlockState state, UseOnContext context)
	{
		if (context.getLevel().isClientSide)
			return InteractionResult.SUCCESS;

		ChainSide side = isConnected(state) ? state.getValue(CONNECTION_SIDE) : ChainSide.TOP;
		ChainConnection nextAvailableConnection = findFirstConnection(context.getLevel(), context.getClickedPos(), state, ChainSide.valuesFrom(side, false));

		if (nextAvailableConnection.side() != null)
			state = state.setValue(CONNECTION_SIDE, nextAvailableConnection.side());

		state = state.setValue(CONNECTION_TYPE, nextAvailableConnection.type());

		KineticBlockEntity.switchToBlockState(context.getLevel(), context.getClickedPos(), state);

		return InteractionResult.SUCCESS;
	}

	@Override
	public VoxelShape getShape(BlockState state, BlockGetter blockGetter, BlockPos blockPos, CollisionContext collisionContext)
	{
		ChainConnection connection = getConnection(state);
		Direction facing = state.getValue(FACING);

		if (connection.side() == null)
			return CHAIN.get(facing);

		if (connection.type() == ConnectionType.CHAIN)
			return switch (connection.side())
			{
				case TOP -> CHAIN_CONNECTED_TOP.get(facing);
				case RIGHT -> CHAIN_CONNECTED_RIGHT.get(facing);
				case BOTTOM -> CHAIN_CONNECTED_BOTTOM.get(facing);
				case LEFT -> CHAIN_CONNECTED_LEFT.get(facing);
			};
		else if (connection.type() == ConnectionType.BELT)
			return switch (connection.side())
			{
				case TOP -> CHAIN_BELT_TOP.get(facing);
				case RIGHT -> CHAIN_BELT_RIGHT.get(facing);
				case BOTTOM -> CHAIN_BELT_BOTTOM.get(facing);
				case LEFT -> CHAIN_BELT_LEFT.get(facing);
			};

		return CHAIN.get(facing);
	}

	@Override
	public BlockState getStateForPlacement(BlockPlaceContext context)
	{
		if (context.getPlayer() == null)
			return defaultBlockState();

		//Face the clicked block.
		BlockPos clickedPos = context.getClickedPos();
		Direction facing = context.getClickedFace().getOpposite();
		BlockState state = defaultBlockState().setValue(FACING, facing);

		//Handle waterlogging.
		state = withWater(state, context);

		//Update the state.
		return updateState(state, clickedPos, context.getLevel());
	}

	@Override
	public BlockState updateShape(BlockState state, Direction face, BlockState neighbour, LevelAccessor world, BlockPos currentPos, BlockPos facingPos)
	{
		//Auto break chain if it is in an invalid state.
		if (!canSurvive(state, world, currentPos))
			return Blocks.AIR.defaultBlockState();

		//Handle state update and waterlogging.
		BlockState newState = updateState(state, currentPos, world);
		updateWater(world, newState, currentPos);

		return newState;
	}

	/**
	 * Updates the state according to surrounding blocks.
	 * @param state state that will get updated.
	 * @param pos location of the block.
	 * @param world the world the block is in.
	 *
	 * @return the new updated state.
	 */
	private BlockState updateState(BlockState state, BlockPos pos, LevelAccessor world)
	{
		boolean wasConnected = isConnected(state);

		if (wasConnected)
		{
			//Get the connection and update it if was removed.
			ChainConnection oldConnection = getConnection(state);
			ConnectionType connection = queryWorldForConnection(world, pos, state, oldConnection.side());

			if (connection == ConnectionType.NONE)
			{
				state = state.setValue(CONNECTION_TYPE, connection);
				state = state.setValue(CONNECTION_SIDE, oldConnection.side());
			}

			return state;
		}

		//Try to find a new connection and update it if found.
		ChainConnection firstConnection = findFirstConnection(world, pos, state);

		if (firstConnection.side() != null && firstConnection.type() != ConnectionType.NONE)
		{
			state = state.setValue(CONNECTION_TYPE, firstConnection.type());
			state = state.setValue(CONNECTION_SIDE, firstConnection.side());
		}

		return state;
	}

	/**
	 * Finds the first connection available to the chain by querying the world for connections.
	 * @param world the world the bloc is in.
	 * @param pos location of the block.
	 * @param state current state of the block.
	 *
	 * @return the connection if one is found.
	 */
	private static ChainConnection findFirstConnection(LevelAccessor world, BlockPos pos, BlockState state)
	{
		return findFirstConnection(world, pos, state, ChainSide.values());
	}

	/**
	 * Finds the first connection available to the chain by querying the world for block from the given sides.
	 *
	 * @param world the world the block is in.
	 * @param pos location of the block.
	 * @param state current state of the block.
	 * @param sides all sides that needs to be queried.
	 *
	 * @return the connection if one is found.
	 */
	private static ChainConnection findFirstConnection(LevelAccessor world, BlockPos pos, BlockState state, ChainSide[] sides)
	{
		//Check all side for a connection.
		for (ChainSide side : sides)
		{
			//Get connection type.
			ConnectionType connection = queryWorldForConnection(world, pos, state, side);

			//The block was connected and a type has been lost.
			if (connection != ConnectionType.NONE)
				return new ChainConnection(side, connection);
		}

		return new ChainConnection(null, ConnectionType.NONE);
	}

	/**
	 * Finds the connection type by querying the world for block on the given side.
	 * @param world the world the block is in.
	 * @param pos location of the block.
	 * @param state current state of the block.
	 * @param side the side of the connection.
	 *
	 * @return the connection type on the given side.
	 */
	private static ConnectionType queryWorldForConnection(LevelAccessor world, BlockPos pos, BlockState state, ChainSide side)
	{
		ChainDirection facing = ChainDirection.of(state.getValue(FACING));
		BlockPos neighbourPos = pos.offset(facing.getDirectionFromSide(side).getNormal());
		BlockEntity neighbourEntity = world.getBlockEntity(neighbourPos);

		if (neighbourEntity == null)
			return ConnectionType.NONE;

		BlockState neighbourState = neighbourEntity.getBlockState();

		if (neighbourEntity instanceof TransmissionChainBlockEntity chainEntity) //Transmission Chain.
		{
			ChainConnection otherConnection = getConnection(neighbourState);
			boolean isConnectionValid = (chainEntity.isConnected() && otherConnection.type() == ConnectionType.CHAIN && ChainSide.opposite(otherConnection.side()) == side);
			boolean isConnectionAvailable = (!chainEntity.isConnected() && otherConnection.type() == ConnectionType.NONE);

			if (facing.direction == neighbourState.getValue(FACING) && (isConnectionValid || isConnectionAvailable))
				return ConnectionType.CHAIN;
		}
		else if (neighbourEntity instanceof BeltBlockEntity) //Belt.
		{
			BeltBlock belt = (BeltBlock) neighbourState.getBlock();
			Direction directionBeltToChain = facing.getDirectionFromSide(side).getOpposite();

			boolean hasShaftTowards = belt.hasShaftTowards(world, neighbourPos, neighbourState, directionBeltToChain);

			if (hasShaftTowards)
				return ConnectionType.BELT;
		}

		return ConnectionType.NONE;
	}

	/**
	 * Gets the connection data from the block state. It does not query the world for neighbour blocks.
	 * @param state the state of the block.
	 *
	 * @return a {@link ConnectionType} representing the connection type.
	 */
	public static ChainConnection getConnection(BlockState state)
	{
		if (!(state.getBlock() instanceof AbstractTransmissionChainBlock))
			return new ChainConnection(null, ConnectionType.NONE);

		ConnectionType connectionType = state.getValue(CONNECTION_TYPE);
		ChainSide side = state.getValue(CONNECTION_SIDE);

		return new ChainConnection(connectionType != ConnectionType.NONE ? side : null, connectionType);
	}

	/**
	 * Checks if the given state represents a chain that is connected. It does not query the world for neighbour blocks.
	 * @param state state of the block.
	 *
	 * @return true if in the given state the chain is connected, false otherwise.
	 */
	public static boolean isConnected(BlockState state)
	{
		if (!(state.getBlock() instanceof AbstractTransmissionChainBlock))
			return false;

		return state.getValue(CONNECTION_TYPE) != ConnectionType.NONE;
	}

	/**
	 * Checks if the chain is connected from the given side. It does not query the world for neighbour blocks.
	 * @param state state of the block.
	 *
	 * @return true if in the chain is connected from the given side, false otherwise.
	 */
	public static boolean isSideConnected(BlockState state, ChainSide side)
	{
		if (!(state.getBlock() instanceof AbstractTransmissionChainBlock))
			return false;

		return state.getValue(CONNECTION_SIDE) == side;
	}

	@Override
	protected boolean areStatesKineticallyEquivalent(BlockState oldState, BlockState newState)
	{
		ChainConnection oldConnection = getConnection(oldState);
		ChainConnection newConnection = getConnection(newState);

		return super.areStatesKineticallyEquivalent(oldState, newState) && oldConnection.equals(newConnection);
	}

	@Override
	public BlockState mirror(BlockState state, Mirror mirror)
	{
		Direction facing = state.getValue(FACING);
		state = state.setValue(FACING, mirror.mirror(facing));

		if (isConnected(state))
			state = mirrorSide(state, mirror);

		return state;
	}

	@Override
	public BlockState rotate(BlockState state, Rotation rotation)
	{
		Direction facing = state.getValue(FACING);

		if (rotation == Rotation.NONE)
			return state;

		//Rotate the side only if the chain is facing a vertical direction.
		if (facing.getAxis().isVertical())
			return rotateSide(state, rotation == Rotation.CLOCKWISE_180 ? rotation : rotation.getRotated(Rotation.CLOCKWISE_180));

		state = state.setValue(FACING, rotation.rotate(facing));

		return state;
	}

	/**
	 * Mirrors the side inside the state.
	 * @param state the state of the block.
	 * @param mirror the mirror operation.
	 *
	 * @return the state with mirrored side.
	 */
	private BlockState mirrorSide(BlockState state, Mirror mirror)
	{
		Direction facing = state.getValue(FACING);
		ChainSide side = state.getValue(CONNECTION_SIDE);

		if (mirror == Mirror.NONE)
			return state;

		//If the chain is facing a horizontal direction left and right always switch side.
		if (facing.getAxis().isHorizontal())
		{
			if (side == ChainSide.LEFT || side == ChainSide.RIGHT)
				return state.setValue(CONNECTION_SIDE, ChainSide.opposite(side));
			else
				return state;
		}

		//Switch side accordingly to the mirror direction.
		if (mirror == Mirror.FRONT_BACK && (side == ChainSide.LEFT || side == ChainSide.RIGHT))
			state = state.setValue(CONNECTION_SIDE, ChainSide.opposite(side));
		else if (mirror == Mirror.LEFT_RIGHT && (side == ChainSide.TOP || side == ChainSide.BOTTOM))
			state = state.setValue(CONNECTION_SIDE, ChainSide.opposite(side));

		return state;
	}

	/**
	 * Rotates the side inside the state.
	 * @param state the state of the block.
	 * @param rotation the rotation operation.
	 *
	 * @return the state with rotated side.
	 */
	private static BlockState rotateSide(BlockState state, Rotation rotation)
	{
		Direction facing = state.getValue(FACING);
		ChainSide side = state.getValue(CONNECTION_SIDE);

		//If the chain is facing down it needs rotating in the opposite direction.
		if (facing == Direction.DOWN && rotation != Rotation.CLOCKWISE_180)
			rotation = rotation == Rotation.CLOCKWISE_90 ? Rotation.COUNTERCLOCKWISE_90 : Rotation.CLOCKWISE_90;

		state = state.setValue(CONNECTION_SIDE, ChainSide.rotate(side, rotation));

		return state;
	}

	//ProperWaterloggedBlock
	@Override
	public FluidState getFluidState(BlockState blockState)
	{
		return fluidState(blockState);
	}

	//IRotate
	@Override
	public boolean hasShaftTowards(LevelReader world, BlockPos pos, BlockState state, Direction direction)
	{
		ChainDirection facing = ChainDirection.of(state.getValue(FACING));

		if (facing.direction == direction)
			return true;

		ChainSide side = facing.getSideFromDirection(direction);
		ChainConnection connection = AbstractTransmissionChainBlock.getConnection(state);

		return side == connection.side() && connection.type() != ConnectionType.NONE;
	}

	@Override
	public Direction.Axis getRotationAxis(BlockState state)
	{
		return state.getValue(FACING).getAxis();
	}

	@Override
	public Class<TransmissionChainBlockEntity> getBlockEntityClass()
	{
		return TransmissionChainBlockEntity.class;
	}

	@Override
	public BlockEntityType<? extends TransmissionChainBlockEntity> getBlockEntityType()
	{
		return CTBlockEntities.TRANSMISSION_CHAIN.get();
	}
}