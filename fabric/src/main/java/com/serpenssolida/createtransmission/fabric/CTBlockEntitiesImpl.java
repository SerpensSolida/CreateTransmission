package com.serpenssolida.createtransmission.fabric;

import com.serpenssolida.createtransmission.content.chain.TransmissionChainBlockEntity;
import com.serpenssolida.createtransmission.content.chain.TransmissionChainVisual;
import com.simibubi.create.foundation.data.CreateBlockEntityBuilder;
import com.tterrag.registrate.builders.BlockEntityBuilder;
import com.tterrag.registrate.util.nullness.NonNullUnaryOperator;

public class CTBlockEntitiesImpl
{
	public static <T> NonNullUnaryOperator<BlockEntityBuilder<TransmissionChainBlockEntity, T>> chainVisual()
	{
		return b -> ((CreateBlockEntityBuilder<TransmissionChainBlockEntity, T>) b).visual(() -> TransmissionChainVisual::new);
	}
}
