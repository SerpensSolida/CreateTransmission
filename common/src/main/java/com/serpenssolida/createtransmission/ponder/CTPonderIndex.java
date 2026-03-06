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
