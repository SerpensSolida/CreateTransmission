package com.serpenssolida.createtransmission.config;

import dev.architectury.injectables.annotations.ExpectPlatform;
import io.github.fabricators_of_create.porting_lib.config.ModConfigSpec;
import net.createmod.catnip.config.ConfigBase;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.config.ModConfig;
import org.apache.commons.lang3.tuple.Pair;

import java.util.EnumMap;
import java.util.Map;
import java.util.function.Supplier;

public class CTConfig
{
	public static final Map<ModConfig.Type, ConfigBase> CONFIGS = new EnumMap<>(ModConfig.Type.class);

	protected static CTServer server;

	private CTConfig() {}

	public static CTServer server() {
		return server;
	}

	public static <T> Supplier<T> safeGetter(Supplier<T> getter, T defaultValue) {
		return () -> {
			try {
				return getter.get();
			} catch (IllegalStateException | NullPointerException ex) {
				// the config is accessed too early (before registration or before config load)
				return defaultValue;
			}
		};
	}

	public static ConfigBase byType(ModConfig.Type type) {
		return CONFIGS.get(type);
	}

	private static <T extends ConfigBase> T register(Supplier<T> factory, ModConfig.Type side) {
		Pair<T, ForgeConfigSpec> specPair = new ForgeConfigSpec.Builder().configure(builder -> {
			T config = factory.get();
			config.registerAll(builder);
			return config;
		});

		T config = specPair.getLeft();
		config.specification = specPair.getRight();
		CONFIGS.put(side, config);
		return config;
	}

	@ExpectPlatform
	public static void registerPlatform() {
		throw new AssertionError();
	}

	public static void register() {
		server = register(CTServer::new, ModConfig.Type.SERVER);

		registerPlatform();
	}

	public static void onLoad(ModConfig modConfig) {
		for(ConfigBase config : CONFIGS.values())
			if(config.specification == modConfig.getSpec())
				config.onLoad();
	}

	public static void onReload(ModConfig modConfig) {
		for(ConfigBase config : CONFIGS.values())
			if(config.specification == modConfig.getSpec())
				config.onReload();
	}

}
