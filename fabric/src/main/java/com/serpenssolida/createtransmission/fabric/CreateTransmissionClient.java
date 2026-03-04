package com.serpenssolida.createtransmission.fabric;

import com.serpenssolida.createtransmission.CTModels;
import com.serpenssolida.createtransmission.CreateTransmission;
import net.fabricmc.api.ClientModInitializer;

public class CreateTransmissionClient implements ClientModInitializer
{
	@Override
	public void onInitializeClient()
	{
		CreateTransmission.LOGGER.info("Create Transmission Client");
		CTModels.init();
	}
}
