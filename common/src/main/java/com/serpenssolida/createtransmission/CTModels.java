package com.serpenssolida.createtransmission;

import com.jozufozu.flywheel.core.PartialModel;

public class CTModels
{
	public static final PartialModel CHAIN_SHAFT = block("transmission_chain/chain_shaft");
	public static final PartialModel CHAIN = block("transmission_chain/chain");
	public static final PartialModel CHAIN_CONNECTED = block("transmission_chain/chain_connected");
	public static final PartialModel CHAIN_BELT = block("transmission_chain/chain_belt");

	CTModels(){}

	/**
	 * Initializes the class static fields.
	 */
	public static void init()
	{
		CreateTransmission.LOGGER.info("Loading partial models for " + CreateTransmission.NAME);
	}

	/**
	 * Creates a {@link PartialModel} with the given path.
	 */

	protected static PartialModel block(String path)
	{
		return new PartialModel(CreateTransmission.asResource("block/" + path));
	}

}
