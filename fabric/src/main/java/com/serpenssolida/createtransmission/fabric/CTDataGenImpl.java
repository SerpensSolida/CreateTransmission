package com.serpenssolida.createtransmission.fabric;

import com.tterrag.registrate.providers.DataGenContext;
import com.tterrag.registrate.providers.RegistrateBlockstateProvider;
import com.tterrag.registrate.providers.RegistrateItemModelProvider;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

public class CTDataGenImpl
{
	private CTDataGenImpl(){}

	public static <T extends Block> void encasedTransmissionChain(DataGenContext<Block, T> ctx, RegistrateBlockstateProvider prov, String casing)
	{
		//Data generation only work on forge side.
	}

	public static <T extends Block> void noModel(DataGenContext<Block, T> ctx, RegistrateBlockstateProvider prov)
	{
		//Data generation only work on forge side.
	}

	public static void handheldItem(DataGenContext<Item, BlockItem> ctx, RegistrateItemModelProvider prov)
	{
		//Data generation only work on forge side.
	}
}
