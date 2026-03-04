package com.serpenssolida.createtransmission;

import com.serpenssolida.createtransmission.content.chain.EncasedTransmissionChainBlock;
import com.serpenssolida.createtransmission.content.chain.TransmissionChainBlock;
import com.simibubi.create.AllTags.AllBlockTags;
import com.simibubi.create.content.decoration.encasing.EncasingRegistry;
import com.tterrag.registrate.util.entry.BlockEntry;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.material.PushReaction;

import static com.serpenssolida.createtransmission.CTDataGen.encasedTransmissionChain;
import static com.simibubi.create.foundation.data.TagGen.axeOrPickaxe;

public class CTBlocks
{
	static
	{
		CTCreativeTabs.setTab(CTCreativeTabs.CREATETRANSMISSION_TAB_KEY);
	}

	public static final BlockEntry<TransmissionChainBlock> TRANSMISSION_CHAIN = CreateTransmission.REGISTRATE
			.block("transmission_chain", TransmissionChainBlock::new)
			.lang("Transmission Chain")
			.properties(properties -> properties.pushReaction(PushReaction.DESTROY).noCollission().strength(0.8f).sound(SoundType.WOOL))
			.tag(AllBlockTags.BRITTLE.tag, AllBlockTags.FAN_TRANSPARENT.tag, AllBlockTags.MOVABLE_EMPTY_COLLIDER.tag, AllBlockTags.WRENCH_PICKUP.tag)
			.transform(axeOrPickaxe())
			.blockstate(CTDataGen::noModel)
			.item()
			.model(CTDataGen::handheldItem)
			.build()
			.register();

	public static final BlockEntry<EncasedTransmissionChainBlock> ANDESITE_ENCASED_TRANSMISSION_CHAIN = CreateTransmission.REGISTRATE
			.block("andesite_encased_transmission_chain", EncasedTransmissionChainBlock::getAndesite)
			.lang("Andesite Encased Transmission Chain")
			.initialProperties(TRANSMISSION_CHAIN)
			.tag(AllBlockTags.BRITTLE.tag, AllBlockTags.FAN_TRANSPARENT.tag, AllBlockTags.MOVABLE_EMPTY_COLLIDER.tag, AllBlockTags.WRENCH_PICKUP.tag)
			.loot((p, b) -> p.dropOther(b, TRANSMISSION_CHAIN.get()))
			.transform(axeOrPickaxe())
			.transform(EncasingRegistry.addVariantTo(TRANSMISSION_CHAIN))
			.blockstate((c, p) -> encasedTransmissionChain(c, p, "andesite"))
			.register();

	public static final BlockEntry<EncasedTransmissionChainBlock> BRASS_ENCASED_TRANSMISSION_CHAIN = CreateTransmission.REGISTRATE
			.block("brass_encased_transmission_chain", EncasedTransmissionChainBlock::getBrass)
			.lang("Brass Encased Transmission Chain")
			.initialProperties(TRANSMISSION_CHAIN)
			.tag(AllBlockTags.BRITTLE.tag, AllBlockTags.FAN_TRANSPARENT.tag, AllBlockTags.MOVABLE_EMPTY_COLLIDER.tag, AllBlockTags.WRENCH_PICKUP.tag)
			.loot((p, b) -> p.dropOther(b, TRANSMISSION_CHAIN.get()))
			.transform(axeOrPickaxe())
			.transform(EncasingRegistry.addVariantTo(TRANSMISSION_CHAIN))
			.blockstate((c, p) -> encasedTransmissionChain(c, p, "brass"))
			.register();


	private CTBlocks() {}

	/**
	 * Initializes the class static fields.
	 */
	public static void init()
	{
		CreateTransmission.LOGGER.info("Registering blocks for " + CreateTransmission.NAME);
	}

}
