package com.serpenssolida.createtransmission.fabric;

import com.serpenssolida.createtransmission.CreateTransmission;
import com.serpenssolida.createtransmission.CreateTransmissionClient;
import net.fabricmc.api.ClientModInitializer;

public class CreateTransmissionFabricClient implements ClientModInitializer
{
	@Override
	public void onInitializeClient()
	{
		CreateTransmission.LOGGER.info("Create Transmission Client");
		CreateTransmissionClient.init();
	}
}
