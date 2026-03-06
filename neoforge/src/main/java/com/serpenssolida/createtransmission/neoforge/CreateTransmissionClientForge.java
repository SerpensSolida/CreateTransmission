package com.serpenssolida.createtransmission.neoforge;

import com.serpenssolida.createtransmission.CTModels;
import com.serpenssolida.createtransmission.CreateTransmission;
import com.serpenssolida.createtransmission.CreateTransmissionClient;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.common.NeoForge;

@Mod(value = CreateTransmission.MOD_ID, dist = Dist.CLIENT)
public class CreateTransmissionClientForge
{
	public CreateTransmissionClientForge(IEventBus modEventBus)
	{
		CreateTransmissionClient.init();

	}

}
