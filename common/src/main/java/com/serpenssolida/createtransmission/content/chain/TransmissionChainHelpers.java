package com.serpenssolida.createtransmission.content.chain;

import com.google.common.collect.ImmutableBiMap;
import com.simibubi.create.foundation.utility.Lang;
import net.minecraft.core.Direction;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.level.block.Rotation;
import org.joml.Quaternionf;
import org.joml.Vector3f;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TransmissionChainHelpers
{
	public static Quaternionf getRotation(TransmissionChainBlockEntity be)
	{
		Direction facing = be.getBlockState().getValue(TransmissionChainBlock.FACING);
		ChainConnection connection = AbstractTransmissionChainBlock.getConnection(be.getBlockState());

		Quaternionf rotation = new Quaternionf();
		Direction up = Direction.UP;

		Vector3f vecFacing = facing.step().normalize();
		Vector3f vecUp = up.step().normalize();

		if (facing == Direction.UP)
			rotation = new Quaternionf().fromAxisAngleDeg(1, 0, 0, 90);
		else if (facing == Direction.DOWN)
			rotation = new Quaternionf().fromAxisAngleDeg(1, 0, 0, -90);
		else
			rotation = rotation.lookAlong(vecFacing, vecUp);

		//Fix for wrong axis alignment (maybe check why).
		if (facing == Direction.WEST || facing == Direction.EAST)
			rotation = rotation.invert();

		if (be.isConnected())
		{
			ChainSide side = AbstractTransmissionChainBlock.getConnection(be.getBlockState()).side();
			Quaternionf connectionRotation = new Quaternionf().fromAxisAngleRad(facing.getOpposite().step(), (float) side.rotationAngle);
			rotation = connectionRotation.mul(rotation);
		}

		return rotation;
	}

	/**
	 * Enum representing the connection types of a transmission chain.
	 */
	public enum ConnectionType implements StringRepresentable
	{
		NONE, CHAIN, BELT;

		@Override
		public String getSerializedName()
		{
			return Lang.asId(name());
		}
	}

	/**
	 * Record representing a connection of a transmission chain block.
	 * @param side the side of the connection.
	 * @param type the type of the connection.
	 */
	public record ChainConnection(ChainSide side, ConnectionType type) {}

	/**
	 *  Enum representing the sides of all possible neighbours of the transmission chain.
	 */
	public enum ChainSide implements StringRepresentable
	{
		TOP(Math.PI * 0.5), RIGHT(0), BOTTOM(Math.PI * 1.5), LEFT(Math.PI);

		final double rotationAngle;

		ChainSide(double rotationAngle)
		{
			this.rotationAngle = rotationAngle;
		}

		@Override
		public String getSerializedName()
		{
			return Lang.asId(name());

		}

		public static ChainSide opposite(ChainSide side)
		{
			return switch (side)
			{
				case TOP -> BOTTOM;
				case RIGHT -> LEFT;
				case BOTTOM -> TOP;
				case LEFT -> RIGHT;
			};
		}

		public static ChainSide rotate(ChainSide side, Rotation rotation)
		{
			if (rotation == Rotation.NONE)
				return side;
			else if (rotation == Rotation.COUNTERCLOCKWISE_90)
				return rotate(ChainSide.opposite(side), Rotation.CLOCKWISE_90);
			else if (rotation == Rotation.CLOCKWISE_180)
				return opposite(side);

			return switch (side)
			{
				case TOP -> RIGHT;
				case RIGHT -> BOTTOM;
				case BOTTOM -> LEFT;
				case LEFT -> TOP;
			};
		}

		public static ChainSide[] valuesFrom(ChainSide startingSide, boolean excludeStarting)
		{
			if (startingSide == LEFT && !excludeStarting)
				return ChainSide.values();

			//Split values at startingSide and merge them in reverse order.
			List<ChainSide> sides = new ArrayList<>(Arrays.asList(ChainSide.values())
												  .subList(startingSide.ordinal() + 1, ChainSide.values().length));
			List<ChainSide> beforeStarting = Arrays.asList(ChainSide.values())
												   .subList(0, startingSide.ordinal() + (excludeStarting ? 0 : 1));
			sides.addAll(beforeStarting);

			return sides.toArray(new ChainSide[0]);
		}
	}

	/**
	 *  Enum that helps to convert side values and direction value based on the direction the transmission chain is facing.
	 */
	public enum ChainDirection implements StringRepresentable
	{
		DOWN(Direction.DOWN, ImmutableBiMap.of(ChainSide.TOP, Direction.NORTH, ChainSide.RIGHT, Direction.EAST, ChainSide.BOTTOM, Direction.SOUTH, ChainSide.LEFT, Direction.WEST)),
		UP(Direction.UP, ImmutableBiMap.of(ChainSide.TOP, Direction.SOUTH, ChainSide.RIGHT, Direction.EAST, ChainSide.BOTTOM, Direction.NORTH, ChainSide.LEFT, Direction.WEST)),
		NORTH(Direction.NORTH, ImmutableBiMap.of(ChainSide.TOP, Direction.UP, ChainSide.RIGHT, Direction.EAST, ChainSide.BOTTOM, Direction.DOWN, ChainSide.LEFT, Direction.WEST)),
		SOUTH(Direction.SOUTH, ImmutableBiMap.of(ChainSide.TOP, Direction.UP, ChainSide.RIGHT, Direction.WEST, ChainSide.BOTTOM, Direction.DOWN, ChainSide.LEFT, Direction.EAST)),
		WEST(Direction.WEST, ImmutableBiMap.of(ChainSide.TOP, Direction.UP, ChainSide.RIGHT, Direction.NORTH, ChainSide.BOTTOM, Direction.DOWN, ChainSide.LEFT, Direction.SOUTH)),
		EAST(Direction.EAST, ImmutableBiMap.of(ChainSide.TOP, Direction.UP, ChainSide.RIGHT, Direction.SOUTH, ChainSide.BOTTOM, Direction.DOWN, ChainSide.LEFT, Direction.NORTH));

		public final Direction direction;
		private final ImmutableBiMap<ChainSide, Direction> directionMap;

		ChainDirection(Direction direction, ImmutableBiMap<ChainSide, Direction> directionMap)
		{
			this.direction = direction;
			this.directionMap = directionMap;
		}

		public static ChainDirection of(Direction direction)
		{
			return switch (direction)
			{
				case DOWN -> DOWN;
				case UP -> UP;
				case NORTH -> NORTH;
				case SOUTH -> SOUTH;
				case WEST -> WEST;
				case EAST -> EAST;
			};
		}

		public Direction.Axis getAxis()
		{
			return direction.getAxis();
		}

		public Direction getDirectionFromSide(ChainSide side)
		{
			return directionMap.get(side);
		}

		public ChainSide getSideFromDirection(Direction direction)
		{
			return directionMap.inverse().get(direction);
		}

		@Override
		public String getSerializedName()
		{
			return Lang.asId(name());
		}
	}
}
