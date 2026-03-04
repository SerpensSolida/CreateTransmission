package com.serpenssolida.createtransmission;

import dev.engine_room.flywheel.lib.model.baked.PartialModel;

public class CTModels
{
	public static final PartialModel CHAIN_SHAFT = model("transmission_chain/chain_shaft");
	public static final PartialModel CHAIN = model("transmission_chain/chain");
	public static final PartialModel CHAIN_CONNECTED = model("transmission_chain/chain_connected");
	public static final PartialModel CHAIN_BELT = model("transmission_chain/chain_belt");

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
	protected static PartialModel model(String path)
	{
		return PartialModel.of(CreateTransmission.asResource("block/" + path));
	}

}
