package com.serpenssolida.createtransmission;

import com.tterrag.registrate.providers.DataGenContext;
import com.tterrag.registrate.providers.RegistrateBlockstateProvider;
import com.tterrag.registrate.providers.RegistrateItemModelProvider;
import dev.architectury.injectables.annotations.ExpectPlatform;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

public class CTDataGen
{
	private CTDataGen(){}

	/**
	 * Generates a blockstate that has no model.
	 * @param ctx block context.
	 * @param prov block provider.
	 * @param <T> block class.
	 */
	@ExpectPlatform
	static <T extends Block> void noModel(DataGenContext<Block, T> ctx, RegistrateBlockstateProvider prov)
	{
		throw new AssertionError();
	}

	/**
	 * Generates blockstate for the encased transmission chain block.
	 * @param ctx block context.
	 * @param prov block provider.
	 * @param casing name of the casing variant.
	 * @param <T> block class.
	 */
	@ExpectPlatform
	static <T extends Block> void encasedTransmissionChain(DataGenContext<Block, T> ctx, RegistrateBlockstateProvider prov, String casing)
	{
		throw new AssertionError();
	}

	/**
	 * Generates handheld model for the item.
	 * @param ctx item context.
	 * @param prov item provider.
	 */
	@ExpectPlatform
	public static void handheldItem(DataGenContext<Item, BlockItem> ctx, RegistrateItemModelProvider prov)
	{
		throw new AssertionError();
	}
}
