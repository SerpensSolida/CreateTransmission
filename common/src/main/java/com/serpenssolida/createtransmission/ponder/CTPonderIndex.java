package com.serpenssolida.createtransmission.ponder;

import com.serpenssolida.createtransmission.CTBlocks;
import com.serpenssolida.createtransmission.CreateTransmission;
import com.simibubi.create.infrastructure.ponder.AllCreatePonderTags;
import com.tterrag.registrate.util.entry.ItemProviderEntry;
import com.tterrag.registrate.util.entry.RegistryEntry;
import net.createmod.ponder.api.registration.PonderPlugin;
import net.createmod.ponder.api.registration.PonderSceneRegistrationHelper;
import net.minecraft.resources.ResourceLocation;

public class CTPonderIndex implements PonderPlugin
{
	public static void init()
	{
		CreateTransmission.REGISTRATE.addRawLang("createtransmission.ponder.usage.header", "Relaying rotational force using Transmission Chains");
		CreateTransmission.REGISTRATE.addRawLang("createtransmission.ponder.usage.text_1", "Encased chain drive can be used to transfer rotational power between belts.");
		CreateTransmission.REGISTRATE.addRawLang("createtransmission.ponder.usage.text_2", "But they are bulky...");
		CreateTransmission.REGISTRATE.addRawLang("createtransmission.ponder.usage.text_3", "As an alternative you can use Transmission Chains!\nThey are slim and only work on belts.");
		CreateTransmission.REGISTRATE.addRawLang("createtransmission.ponder.usage.text_4", "They can power belt in different arrangements.");
		CreateTransmission.REGISTRATE.addRawLang("createtransmission.ponder.usage.text_5", "Horizontally...");
		CreateTransmission.REGISTRATE.addRawLang("createtransmission.ponder.usage.text_6", "...on a right angle...");
		CreateTransmission.REGISTRATE.addRawLang("createtransmission.ponder.usage.text_7", "...and vertically.");
		CreateTransmission.REGISTRATE.addRawLang("createtransmission.ponder.encasing.header", "Encasing Transmission Chains");
		CreateTransmission.REGISTRATE.addRawLang("createtransmission.ponder.encasing.text_1", "Transmission Chains can also be encased!");
		CreateTransmission.REGISTRATE.addRawLang("createtransmission.ponder.encasing.text_2", "A wrench can be used to remove the casing.");
	}

	@Override
	public void registerScenes(PonderSceneRegistrationHelper<ResourceLocation> sceneRegistrationHelper)
	{
		PonderSceneRegistrationHelper<ItemProviderEntry<?>> helper = sceneRegistrationHelper.withKeyFunction(RegistryEntry::getId);

		helper.forComponents(CTBlocks.TRANSMISSION_CHAIN, CTBlocks.ANDESITE_ENCASED_TRANSMISSION_CHAIN, CTBlocks.BRASS_ENCASED_TRANSMISSION_CHAIN)
			  .addStoryBoard("chain/usage", CTPonderScenes::chainUsage, AllCreatePonderTags.KINETIC_RELAYS)
			  .addStoryBoard("chain/encasing", CTPonderScenes::chainEncasing);
	}

	@Override
	public String getModId()
	{
		return CreateTransmission.MOD_ID;
	}
}
