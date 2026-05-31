package com.serpenssolida.createtransmission;

import com.serpenssolida.createtransmission.content.chain.TransmissionChainBlockEntity;
import com.serpenssolida.createtransmission.content.chain.TransmissionChainRenderer;
import com.tterrag.registrate.builders.BlockEntityBuilder;
import com.tterrag.registrate.util.entry.BlockEntityEntry;
import com.tterrag.registrate.util.nullness.NonNullUnaryOperator;
import dev.architectury.injectables.annotations.ExpectPlatform;

public class CTBlockEntities
{
	public static final BlockEntityEntry<TransmissionChainBlockEntity> TRANSMISSION_CHAIN = CreateTransmission.REGISTRATE
			.blockEntity("transmission_chain", TransmissionChainBlockEntity::new)
			.transform(chainVisual())
			.validBlocks(CTBlocks.TRANSMISSION_CHAIN, CTBlocks.ANDESITE_ENCASED_TRANSMISSION_CHAIN, CTBlocks.BRASS_ENCASED_TRANSMISSION_CHAIN)
			.renderer(() -> TransmissionChainRenderer::new)
			.register();

	private CTBlockEntities() {}

	/**
	 * Initializes the class static fields.
	 */
	public static void init()
	{
		CreateTransmission.LOGGER.info("Registering blocks entities for " + CreateTransmission.NAME);
	}

	@ExpectPlatform
	public static <T> NonNullUnaryOperator<BlockEntityBuilder<TransmissionChainBlockEntity, T>> chainVisual() { throw new AssertionError(); }
}
