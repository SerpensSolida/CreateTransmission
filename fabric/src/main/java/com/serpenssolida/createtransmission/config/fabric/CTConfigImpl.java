package com.serpenssolida.createtransmission.config.fabric;

import com.serpenssolida.createtransmission.CreateTransmission;
import com.serpenssolida.createtransmission.config.CTConfig;
import fuzs.forgeconfigapiport.api.config.v2.ForgeConfigRegistry;
import fuzs.forgeconfigapiport.api.config.v2.ModConfigEvents;
import net.createmod.catnip.config.ConfigBase;
import net.minecraftforge.fml.config.ModConfig;

import java.util.Map;

import static com.serpenssolida.createtransmission.config.CTConfig.CONFIGS;

public class CTConfigImpl
{
	public static void registerPlatform() {
		for(Map.Entry<ModConfig.Type, ConfigBase> pair : CONFIGS.entrySet())
			ForgeConfigRegistry.INSTANCE.register(CreateTransmission.MOD_ID, pair.getKey(), pair.getValue().specification);

		ModConfigEvents.loading(CreateTransmission.MOD_ID).register(CTConfig::onLoad);
		ModConfigEvents.reloading(CreateTransmission.MOD_ID).register(CTConfig::onReload);
	}
}
