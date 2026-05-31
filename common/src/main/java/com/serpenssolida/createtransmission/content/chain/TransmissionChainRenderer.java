package com.serpenssolida.createtransmission.content.chain;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.serpenssolida.createtransmission.CTBlocks;
import com.serpenssolida.createtransmission.CTModels;
import com.serpenssolida.createtransmission.CTSpriteShifts;
import com.serpenssolida.createtransmission.content.chain.TransmissionChainHelpers.ChainConnection;
import com.serpenssolida.createtransmission.content.chain.TransmissionChainHelpers.ConnectionType;
import com.serpenssolida.createtransmission.content.chain.TransmissionChainHelpers.ChainDirection;
import com.simibubi.create.foundation.blockEntity.renderer.SafeBlockEntityRenderer;
import dev.engine_room.flywheel.api.visualization.VisualizationManager;
import dev.engine_room.flywheel.lib.model.baked.PartialModel;
import dev.engine_room.flywheel.lib.transform.PoseTransformStack;
import dev.engine_room.flywheel.lib.transform.TransformStack;
import net.createmod.catnip.animation.AnimationTickHolder;
import net.createmod.catnip.render.SpriteShiftEntry;
import net.createmod.catnip.render.SuperByteBuffer;
import net.createmod.catnip.render.CachedBuffers;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.state.BlockState;
import org.joml.Quaternionf;

import static com.simibubi.create.content.kinetics.base.KineticBlockEntityRenderer.*;

public class TransmissionChainRenderer extends SafeBlockEntityRenderer<TransmissionChainBlockEntity>
{

	public TransmissionChainRenderer(BlockEntityRendererProvider.Context context) {}

	@Override
	protected void renderSafe(TransmissionChainBlockEntity be, float partialTicks, PoseStack ms, MultiBufferSource buffer, int light, int overlay)
	{
		BlockState blockState = be.getBlockState();

		if (VisualizationManager.supportsVisualization(be.getLevel()) || !checkBlockState(be))
			return;

		renderChain(be, ms, buffer, light, blockState);

		ChainConnection connection = AbstractTransmissionChainBlock.getConnection(blockState);
		Direction facing = blockState.getValue(AbstractTransmissionChainBlock.FACING);

		if (connection.type() == ConnectionType.BELT)
			renderShaft(be, ms, buffer, light, blockState, ChainDirection.of(facing).getDirectionFromSide(connection.side()));


		renderShaft(be, ms, buffer, light, blockState, facing);
	}

	private void renderShaft(TransmissionChainBlockEntity be, PoseStack ms, MultiBufferSource buffer, int light, BlockState blockState, Direction facing)
	{
		VertexConsumer vb = buffer.getBuffer(RenderType.solid());

		BlockPos pos = be.getBlockPos();
		Direction.Axis axis = facing.getAxis();
		SuperByteBuffer superByteBuffer = kineticRotationTransform(CachedBuffers.partial(CTModels.CHAIN_SHAFT, blockState), be, axis, getAngleForBe(be, pos, axis), light);

		PoseStack ps = new PoseStack();
		PoseTransformStack msrShaft = TransformStack.of(ps);
		msrShaft.rotateCentered(TransmissionChainHelpers.getBlockRotation(facing.getOpposite()));
		msrShaft.translate(0, 0, 0.03);
		superByteBuffer.light(light).transform(ps).renderInto(ms, vb);
	}

	private void renderChain(TransmissionChainBlockEntity be, PoseStack ms, MultiBufferSource buffer, int light, BlockState blockState)
	{
		float renderTick = AnimationTickHolder.getRenderTime(be.getLevel());
		VertexConsumer vb = buffer.getBuffer(RenderType.solid());

		ChainConnection connection = AbstractTransmissionChainBlock.getConnection(blockState);
		Direction facing = blockState.getValue(AbstractTransmissionChainBlock.FACING);

		//Prepare model.
		PartialModel beltPartial = getChainModel(connection.type());
		SuperByteBuffer chainBuffer = CachedBuffers.partial(beltPartial, blockState);
		chainBuffer.light(light);
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

		//Rotate the model.
		PoseStack localTransforms = new PoseStack();
		PoseTransformStack msr = TransformStack.of(localTransforms);
		Quaternionf rotation = TransmissionChainHelpers.getRotation(be);
		msr.rotateCentered(rotation);

		chainBuffer.transform(localTransforms)
				   .renderInto(ms, vb);
	}

	private static boolean checkBlockState(TransmissionChainBlockEntity be)
	{
		BlockState blockState = be.getBlockState();
		return CTBlocks.TRANSMISSION_CHAIN.has(blockState) || CTBlocks.ANDESITE_ENCASED_TRANSMISSION_CHAIN.has(blockState) || CTBlocks.BRASS_ENCASED_TRANSMISSION_CHAIN.has(blockState);
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
	 * @param direction the direction.
	 * @param blockEntity chain block entity
	 *
	 * @return the speed of the shaft facing the direction.
	 */
	private float getChainRotationalSpeed(Direction direction, TransmissionChainBlockEntity blockEntity)
	{
		return direction.getAxisDirection().getStep() * -blockEntity.getSpeed() * 0.415f;
	}
}
