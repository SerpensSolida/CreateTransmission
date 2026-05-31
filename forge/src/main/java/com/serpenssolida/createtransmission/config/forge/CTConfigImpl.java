package com.serpenssolida.createtransmission.config.forge;

import com.serpenssolida.createtransmission.forge.CreateTransmissionModForge;
import net.createmod.catnip.config.ConfigBase;
import net.minecraftforge.fml.config.ModConfig;

import java.util.Map;

import static com.serpenssolida.createtransmission.config.CTConfig.CONFIGS;

public class CTConfigImpl
{
	public static void registerPlatform()
	{
		for(Map.Entry<ModConfig.Type, ConfigBase> pair : CONFIGS.entrySet())
			CreateTransmissionModForge.context.registerConfig(pair.getKey(), pair.getValue().specification);
	}
}
