package com.serpenssolida.createtransmission.content.chain;

import com.jozufozu.flywheel.api.InstanceData;
import com.jozufozu.flywheel.api.Instancer;
import com.jozufozu.flywheel.api.Material;
import com.jozufozu.flywheel.api.MaterialManager;
import com.jozufozu.flywheel.core.PartialModel;
import com.serpenssolida.createtransmission.CTModels;
import com.serpenssolida.createtransmission.CTSpriteShifts;
import com.serpenssolida.createtransmission.content.chain.TransmissionChainHelpers.ChainConnection;
import com.serpenssolida.createtransmission.content.chain.TransmissionChainHelpers.ChainDirection;
import com.serpenssolida.createtransmission.content.chain.TransmissionChainHelpers.ChainSide;
import com.serpenssolida.createtransmission.content.chain.TransmissionChainHelpers.ConnectionType;
import com.simibubi.create.content.kinetics.base.KineticBlockEntityInstance;
import com.simibubi.create.content.kinetics.base.flwdata.BeltData;
import com.simibubi.create.content.kinetics.base.flwdata.RotatingData;
import com.simibubi.create.foundation.block.render.SpriteShiftEntry;
import com.simibubi.create.foundation.render.AllMaterialSpecs;
import net.minecraft.core.Direction;
import net.minecraft.world.level.LightLayer;
import org.joml.Quaternionf;
import org.joml.Vector3f;

import java.util.EnumMap;
import java.util.Map;

public class TransmissionChainInstance extends KineticBlockEntityInstance<TransmissionChainBlockEntity>
{
	protected final EnumMap<Direction, RotatingData> keys = new EnumMap<>(Direction.class);
	protected BeltData chainInstance;

	public TransmissionChainInstance(MaterialManager materialManager, TransmissionChainBlockEntity blockEntity)
	{
		super(materialManager, blockEntity);

		ChainDirection facing = ChainDirection.of(blockState.getValue(TransmissionChainBlock.FACING));
		ChainConnection connection = AbstractTransmissionChainBlock.getConnection(blockState);

		//Shaft towards facing.
		keys.put(facing.direction, setupShaftData(facing.direction));

		//Chain instance.
		Instancer<BeltData> chainKey = materialManager.defaultSolid()
													  .material(AllMaterialSpecs.BELTS)
													  .getModel(getChainModel(connection.type()));

		chainInstance = setupChainData(chainKey.createInstance(), CTSpriteShifts.CHAIN);

		if (connection.type() != ConnectionType.BELT)
			return;

		//Shaft towards belt.
		Direction direction = facing.getDirectionFromSide(connection.side());
		RotatingData key = setupShaftData(direction);
		keys.put(direction, key);

	}

	@Override
	public void update()
	{
		Direction facing = blockState.getValue(TransmissionChainBlock.FACING);

		for (Map.Entry<Direction, RotatingData> key : keys.entrySet())
		{
			Direction direction = key.getKey();
			Direction.Axis axis = direction.getAxis();

			updateRotation(key.getValue(), axis, blockEntity.getSpeed() * blockEntity.getRotationSpeedModifier(direction));
		}

		chainInstance.setScrollTexture(CTSpriteShifts.CHAIN)
		   .setColor(blockEntity)
		   .setRotationalSpeed(getChainRotationalSpeed(facing) * blockEntity.getRotationSpeedModifier(facing));
	}

	@Override
	public void updateLight()
	{
		keys.forEach((direction, rotatingData) -> relight(pos, rotatingData));

		if (chainInstance != null)
			relight(pos, chainInstance);
	}

	@Override
	protected void remove()
	{
		chainInstance.delete();
		keys.values().forEach(InstanceData::delete);
		keys.clear();
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
	 * @return The {@link RotatingData} of the instance.
	 */
	private RotatingData setupShaftData(Direction direction)
	{
		int blockLight = world.getBrightness(LightLayer.BLOCK, pos);
		int skyLight = world.getBrightness(LightLayer.SKY, pos);

		Material<RotatingData> rotatingMaterial = getRotatingMaterial();
		Instancer<RotatingData> shaft = rotatingMaterial.getModel(CTModels.CHAIN_SHAFT, blockState, direction);

		Vector3f location = new Vector3f(getInstancePosition().getX(), getInstancePosition().getY(), getInstancePosition().getZ());
		Vector3f offset = direction.step().mul(0.03f);

		RotatingData key = shaft.createInstance();
		key.setRotationAxis(direction.getAxis())
		   .setRotationalSpeed(blockEntity.getSpeed() * blockEntity.getRotationSpeedModifier(direction))
		   .setRotationOffset(getRotationOffset(direction.getAxis()))
		   .setColor(blockEntity)
		   .setPosition(location.add(offset))
		   .setBlockLight(blockLight)
		   .setSkyLight(skyLight);

		return key;
	}

	/**
	 * Sets the starting data for a chain model instance.
	 * @param spriteShift the sprite shift for animating the instance.
	 *
	 * @return The {@link BeltData} of the instance.
	 */
	private BeltData setupChainData(BeltData key, SpriteShiftEntry spriteShift)
	{
		Direction facing = blockState.getValue(TransmissionChainBlock.FACING);
		Quaternionf rotation = TransmissionChainHelpers.getRotation(blockEntity);

		key.setScrollTexture(spriteShift)
		   .setScrollMult(0.5f)
		   .setRotation(rotation)
		   .setRotationalSpeed(getChainRotationalSpeed(facing) * blockEntity.getRotationSpeedModifier(facing))
		   .setRotationOffset(0f)
		   .setColor(blockEntity)
		   .setPosition(getInstancePosition())
		   .setBlockLight(world.getBrightness(LightLayer.BLOCK, pos))
		   .setSkyLight(world.getBrightness(LightLayer.SKY, pos));

		return key;
	}

	/**
	 * Retrieves the correct rotation speed of the chain for a given direction.
	 * @param direction the direction.
	 *
	 * @return the speed of the shaft facing the direction.
	 */
	private float getChainRotationalSpeed(Direction direction)
	{
		return direction.getAxisDirection().getStep() * -blockEntity.getSpeed() * 0.415f;
	}
}
