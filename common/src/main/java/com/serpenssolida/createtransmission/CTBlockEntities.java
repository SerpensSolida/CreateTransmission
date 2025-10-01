package com.serpenssolida.createtransmission;

import com.serpenssolida.createtransmission.content.chain.TransmissionChainBlockEntity;
import com.serpenssolida.createtransmission.content.chain.TransmissionChainInstance;
import com.serpenssolida.createtransmission.content.chain.TransmissionChainRenderer;
import com.tterrag.registrate.util.entry.BlockEntityEntry;

public class CTBlockEntities
{
	public static final BlockEntityEntry<TransmissionChainBlockEntity> TRANSMISSION_CHAIN = CreateTransmission.REGISTRATE
			.blockEntity("transmission_chain", TransmissionChainBlockEntity::new)
			.instance(() -> TransmissionChainInstance::new)
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
}
