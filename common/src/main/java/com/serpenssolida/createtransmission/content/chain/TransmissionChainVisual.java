package com.serpenssolida.createtransmission.content.chain;

import com.simibubi.create.content.kinetics.KineticDebugger;
import com.simibubi.create.content.kinetics.base.KineticBlockEntityVisual;
import com.simibubi.create.content.kinetics.base.RotatingInstance;
import com.simibubi.create.content.kinetics.belt.BeltVisual;
import com.simibubi.create.content.processing.burner.ScrollInstance;
import com.simibubi.create.foundation.render.AllInstanceTypes;
import dev.engine_room.flywheel.api.instance.Instance;
import dev.engine_room.flywheel.api.instance.Instancer;
import dev.engine_room.flywheel.api.model.Model;
import dev.engine_room.flywheel.api.visualization.VisualizationContext;
import dev.engine_room.flywheel.lib.model.Models;
import dev.engine_room.flywheel.lib.model.baked.PartialModel;
import com.serpenssolida.createtransmission.CTModels;
import com.serpenssolida.createtransmission.CTSpriteShifts;
import com.serpenssolida.createtransmission.content.chain.TransmissionChainHelpers.ChainConnection;
import com.serpenssolida.createtransmission.content.chain.TransmissionChainHelpers.ChainDirection;
import com.serpenssolida.createtransmission.content.chain.TransmissionChainHelpers.ChainSide;
import com.serpenssolida.createtransmission.content.chain.TransmissionChainHelpers.ConnectionType;
import dev.engine_room.flywheel.lib.transform.TransformStack;
import net.createmod.catnip.render.SpriteShiftEntry;
import net.minecraft.core.Direction;
import net.minecraft.world.level.LightLayer;
import org.joml.Quaternionf;
import org.joml.Vector3f;

import java.util.EnumMap;
import java.util.Map;
import java.util.function.Consumer;

public class TransmissionChainVisual extends KineticBlockEntityVisual<TransmissionChainBlockEntity>
{
	protected final EnumMap<Direction, RotatingInstance> shaftInstances = new EnumMap<>(Direction.class);
	protected ScrollInstance chainInstance;

	public TransmissionChainVisual(VisualizationContext context, TransmissionChainBlockEntity blockEntity, float partialTick)
	{
		super(context, blockEntity, partialTick);

		ChainDirection facing = ChainDirection.of(blockState.getValue(AbstractTransmissionChainBlock.FACING));
		ChainConnection connection = AbstractTransmissionChainBlock.getConnection(blockState);

		//Shaft towards facing.
		shaftInstances.put(facing.direction, setupShaftData(facing.direction));

		//Chain instance.
		Instancer<ScrollInstance> chainKey = instancerProvider().instancer(AllInstanceTypes.SCROLLING, Models.partial(getChainModel(connection.type())));
		chainInstance = setupChainData(chainKey.createInstance(), CTSpriteShifts.CHAIN);

		//Shaft towards belt.
		if (connection.type() == ConnectionType.BELT)
		{
			Direction direction = facing.getDirectionFromSide(connection.side());
			RotatingInstance key = setupShaftData(direction);
			shaftInstances.put(direction, key);
		}

		//Update with other data.
		update(0);
	}

	@Override
	public void update(float partialTick)
	{
		Direction facing = blockState.getValue(AbstractTransmissionChainBlock.FACING);

		//Update shafts.
		for (Map.Entry<Direction, RotatingInstance> key : shaftInstances.entrySet())
		{
			Direction direction = key.getKey();
			Direction.Axis axis = direction.getAxis();

			key.getValue().setRotationAxis(axis)
			   .setRotationOffset(rotationOffset(blockEntity.getBlockState(), direction.getAxis(), blockEntity.getBlockPos()))
			   .setRotationalSpeed(blockEntity.getSpeed() * blockEntity.getRotationSpeedModifier(direction) * RotatingInstance.SPEED_MULTIPLIER);

			if (KineticDebugger.isActive())
				key.getValue().setColor(blockEntity);

			key.getValue().setChanged();
		}

		//Update chain.
		chainInstance.setSpriteShift(CTSpriteShifts.CHAIN)
					 .speed(0, getChainRotationalSpeed(facing) * blockEntity.getRotationSpeedModifier(facing) * BeltVisual.MAGIC_SCROLL_MULTIPLIER)
					 .colorRgb(RotatingInstance.colorFromBE(blockEntity))
					 .setChanged();
	}

	@Override
	public void updateLight(float partialTick)
	{
		shaftInstances.forEach((direction, rotatingData) -> relight(pos, rotatingData));

		if (chainInstance != null)
			relight(pos, chainInstance);
	}

	@Override
	public void collectCrumblingInstances(Consumer<Instance> consumer)
	{
		consumer.accept(chainInstance);
		shaftInstances.forEach((direction, rotatingInstance) -> consumer.accept(rotatingInstance));
	}

	@Override
	protected void _delete()
	{
		chainInstance.delete();
		shaftInstances.values().forEach(RotatingInstance::delete);
		shaftInstances.clear();
	}

	/**
	 * Gets the correct chain model based on the connection type.
	 * @param connectionType the connection type.
	 *
	 * @return the {@link PartialModel} corresponding to the given connection type.
	 */
	private PartialModel getChainModel(ConnectionType connectionType)
	{
		if (connectionType == ConnectionType.NONE)
			return CTModels.CHAIN;

		return connectionType == ConnectionType.CHAIN ? CTModels.CHAIN_CONNECTED : CTModels.CHAIN_BELT;
	}

	/**
	 * Sets the starting data for a shaft model instance facing the given direction.
	 * @param direction the direction the shaft is facing.
	 *
	 * @return The {@link RotatingInstance} of the instance.
	 */
	private RotatingInstance setupShaftData(Direction direction)
	{
		int blockLight = level.getBrightness(LightLayer.BLOCK, pos);
		int skyLight = level.getBrightness(LightLayer.SKY, pos);

		Instancer<RotatingInstance> shaft = instancerProvider().instancer(AllInstanceTypes.ROTATING, getShaftModel(direction));

		Vector3f location = new Vector3f(getVisualPosition().getX(), getVisualPosition().getY(), getVisualPosition().getZ());

		RotatingInstance instance = shaft.createInstance();
		instance.setRotationAxis(direction.getAxis())
		   .setPosition(location)
		   .light(blockLight, skyLight);

		return instance;
	}

	private Model getShaftModel(Direction direction)
	{
		return Models.partial(CTModels.CHAIN_SHAFT, direction, (axis, modelTransform) ->
		{
			var msr = TransformStack.of(modelTransform);
			msr.center();
			msr.rotateToFace(direction.getOpposite());
			msr.uncenter();
		});

	}

	/**
	 * Sets the starting data for a chain model instance.
	 * @param spriteShift the sprite shift for animating the instance.
	 *
	 * @return The {@link ScrollInstance} of the chain.
	 */
	private ScrollInstance setupChainData(ScrollInstance instance, SpriteShiftEntry spriteShift)
	{
		Direction facing = blockState.getValue(AbstractTransmissionChainBlock.FACING);
		Quaternionf rotation = TransmissionChainHelpers.getBlockRotation(facing);

		if (blockEntity.isConnected())
		{
			ChainSide side = AbstractTransmissionChainBlock.getConnection(blockState).side();
			Quaternionf connectionRotation = new Quaternionf().fromAxisAngleRad(facing.getOpposite().step(), (float) side.rotationAngle);
			rotation = connectionRotation.mul(rotation);
		}

		instance.setSpriteShift(spriteShift, 1f, 0.5f)
		   .position(getVisualPosition())
		   .rotation(rotation)
		   .speed(0, getChainRotationalSpeed(facing) * blockEntity.getRotationSpeedModifier(facing))
		   .offset(0f, 0f)
		   .colorRgb(RotatingInstance.colorFromBE(blockEntity))
		   .setChanged();

		return instance;
	}

	/**
	 * Retrieves the correct roation speed of the chain for a given direction.
	 * @param direction the direction.
	 *
	 * @return the speed of the shaft facing the direction.
	 */
	private float getChainRotationalSpeed(Direction direction)
	{
		return direction.getAxisDirection().getStep() * -blockEntity.getSpeed() * 0.415f;
	}
}
