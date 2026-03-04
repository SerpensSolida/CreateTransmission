package com.serpenssolida.createtransmission;

import dev.architectury.injectables.annotations.ExpectPlatform;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.CreativeModeTab;

import java.util.function.Supplier;

public class CTCreativeTabs
{
	public static final ResourceKey<CreativeModeTab> CREATETRANSMISSION_TAB_KEY = ResourceKey.create(Registries.CREATIVE_MODE_TAB, CreateTransmission.asResource("main"));

	public static final Supplier<CreativeModeTab> CREATETRANSMISSION_TAB = registerTab("main", () -> createBuilder()
			.icon(CTBlocks.TRANSMISSION_CHAIN::asStack)
			.title(Component.translatable("creativeTab." + CreateTransmission.MOD_ID + ".main"))
			.displayItems((param, output) -> output.accept(CTBlocks.TRANSMISSION_CHAIN.get()))
			.build());

	private CTCreativeTabs(){}

	/**
	 * Initializes the class static fields.
	 */
	public static void init()
	{
		CreateTransmission.REGISTRATE.addRawLang("creativeTab." + CreateTransmission.MOD_ID + ".main", "Create: Transmission");
		CreateTransmission.LOGGER.info("Loading creative tabs for " + CreateTransmission.NAME);
	}

	/**
	 * Creates a new tab with the given id and registers it.
	 * @param id the id of the tab.
	 * @param sup the tab to register.
	 *
	 * @return the newly created tab.
	 */
	@ExpectPlatform
	public static Supplier<CreativeModeTab> registerTab(String id, Supplier<CreativeModeTab> sup)
	{
		throw new AssertionError();
	}

	/**
	 * Instantiates a new builder for a creative tab.
	 *
	 * @return the builder.
	 */
	@ExpectPlatform
	public static CreativeModeTab.Builder createBuilder()
	{
		throw new AssertionError();
	}

	/**
	 * Set the current creative tab of the registrate to the tab with the given key.
	 *
	 * @param key the key of the new tab.
	 */
	@ExpectPlatform
	public static void setTab(ResourceKey<CreativeModeTab> key)
	{
		throw new AssertionError();
	}
}
