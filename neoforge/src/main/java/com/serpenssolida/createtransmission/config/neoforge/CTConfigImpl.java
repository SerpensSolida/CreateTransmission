package com.serpenssolida.createtransmission.config.neoforge;

import com.serpenssolida.createtransmission.neoforge.CreateTransmissionModForge;
import net.createmod.catnip.config.ConfigBase;
import net.neoforged.fml.ModLoadingContext;
import net.neoforged.fml.config.ModConfig;

import java.util.Map;

import static com.serpenssolida.createtransmission.config.CTConfig.CONFIGS;

public class CTConfigImpl
{
	public static void registerPlatform() {
		for(Map.Entry<ModConfig.Type, ConfigBase> pair : CONFIGS.entrySet())
			ModLoadingContext.get().getActiveContainer().registerConfig(pair.getKey(), pair.getValue().specification);
	}
}
