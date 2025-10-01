package com.serpenssolida.createtransmission.content.chain;

import com.jozufozu.flywheel.core.PartialModel;
import com.jozufozu.flywheel.util.transform.TransformStack;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.serpenssolida.createtransmission.CTModels;
import com.serpenssolida.createtransmission.CTSpriteShifts;
import com.serpenssolida.createtransmission.CreateTransmission;
import com.serpenssolida.createtransmission.content.chain.TransmissionChainHelpers.ChainConnection;
import com.serpenssolida.createtransmission.content.chain.TransmissionChainHelpers.ChainDirection;
import com.serpenssolida.createtransmission.content.chain.TransmissionChainHelpers.ChainSide;
import com.serpenssolida.createtransmission.content.chain.TransmissionChainHelpers.ConnectionType;
import com.simibubi.create.foundation.block.render.SpriteShiftEntry;
import com.simibubi.create.foundation.blockEntity.renderer.SafeBlockEntityRenderer;
import com.simibubi.create.foundation.render.CachedBufferer;
import com.simibubi.create.foundation.render.SuperByteBuffer;
import com.simibubi.create.foundation.utility.AnimationTickHolder;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderDispatcher;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.core.Direction;
import org.joml.Quaternionf;
import org.joml.Vector3f;

public class TransmissionChainRenderer extends SafeBlockEntityRenderer<TransmissionChainBlockEntity>
{

	public TransmissionChainRenderer(BlockEntityRendererProvider.Context context) {}

	@Override
	protected void renderSafe(TransmissionChainBlockEntity be, float partialTicks, PoseStack ms, MultiBufferSource buffer, int light, int overlay)
	{
		float renderTick = AnimationTickHolder.getRenderTime(be.getLevel());
		PoseStack localTransforms = new PoseStack();
		TransformStack msr = TransformStack.cast(localTransforms);
		VertexConsumer vb = buffer.getBuffer(RenderType.solid());
		ChainConnection connection = AbstractTransmissionChainBlock.getConnection(be.getBlockState());
		Direction facing = be.getBlockState().getValue(TransmissionChainBlock.FACING);

		//Rotate the model.
		Quaternionf rotation = TransmissionChainHelpers.getRotation(be);
		msr.rotateCentered(rotation);

		//Prepare model.
		PartialModel beltPartial = getChainModel(connection.type());
		SuperByteBuffer chainBuffer = CachedBufferer.partial(beltPartial, be.getBlockState())
													.light(light);
		SpriteShiftEntry spriteShift = CTSpriteShifts.CHAIN;

		//Texture scroll.
		float speed = getChainRotationalSpeed(facing, be) * be.getRotationSpeedModifier(facing);
		if (speed != 0)
		{
			float scrollMult = 0.5f;
			float spriteSize = spriteShift.getTarget().getV1() - spriteShift.getTarget().getV0();
			double scroll = speed * renderTick / (31.5 * 16);
			scroll = scroll - Math.floor(scroll);
			scroll = scroll * spriteSize * scrollMult;

			chainBuffer.shiftUVScrolling(spriteShift, (float) scroll);
		}

		chainBuffer.transform(localTransforms)
				   .renderInto(ms, vb);
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
	 * Retrieves the correct rotation speed of the chain for a given direction.
	 *
	 * @param direction   the direction.
	 * @param blockEntity
	 *
	 * @return the speed of the shaft facing the direction.
	 */
	private float getChainRotationalSpeed(Direction direction, TransmissionChainBlockEntity blockEntity)
	{
		return direction.getAxisDirection().getStep() * -blockEntity.getSpeed() * 0.415f;
	}
}
