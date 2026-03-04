package com.serpenssolida.createtransmission.neoforge;

import com.serpenssolida.createtransmission.content.chain.AbstractTransmissionChainBlock;
import com.serpenssolida.createtransmission.content.chain.TransmissionChainHelpers.ChainConnection;
import com.simibubi.create.Create;
import com.tterrag.registrate.providers.DataGenContext;
import com.tterrag.registrate.providers.RegistrateBlockstateProvider;
import com.tterrag.registrate.providers.RegistrateItemModelProvider;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.client.model.generators.ConfiguredModel;
import net.neoforged.neoforge.client.model.generators.ModelFile;
import net.neoforged.neoforge.client.model.generators.VariantBlockStateBuilder;

import static com.serpenssolida.createtransmission.content.chain.TransmissionChainHelpers.*;

public class CTDataGenImpl
{
	public static <T extends Block> void encasedTransmissionChain(DataGenContext<Block, T> ctx, RegistrateBlockstateProvider prov, String casing)
	{
		String path = "block/transmission_chain/encased/";
		VariantBlockStateBuilder variantBuilder = prov.getVariantBuilder(ctx.getEntry());

		variantBuilder.forAllStatesExcept(state ->
		{
			Direction facing = state.getValue(AbstractTransmissionChainBlock.FACING);
			boolean isHorizontal = facing.getAxis().isHorizontal();
			int zRot = isHorizontal ? 0 : (facing.equals(Direction.UP) ? 270 : 90);
			String modelTypePath = "";
			String sidePath = "";

			if (AbstractTransmissionChainBlock.isConnected(state))
			{
				ChainConnection connection = AbstractTransmissionChainBlock.getConnection(state);
				
				modelTypePath = connection.type() == ConnectionType.CHAIN ? "_connected" : "_belt";
				sidePath = "_" + connection.side().toString().toLowerCase();
			}

			ModelFile.ExistingModelFile modelFile = prov.models().getExistingFile(prov.modLoc(path + casing + "_encased_chain" + modelTypePath + sidePath));

			return ConfiguredModel.builder()
								  .rotationY((int) (facing.toYRot() - 180) - (!isHorizontal ? 90 : 0))
								  .rotationX(zRot)
								  .modelFile(modelFile)
								  .uvLock(false)
								  .build();

		}, AbstractTransmissionChainBlock.WATERLOGGED);
	}

	public static <T extends Block> void noModel(DataGenContext<Block, T> ctx, RegistrateBlockstateProvider prov)
	{
		VariantBlockStateBuilder variantBuilder = prov.getVariantBuilder(ctx.getEntry());
		ModelFile.ExistingModelFile modelFile = prov.models().getExistingFile(ResourceLocation.fromNamespaceAndPath(Create.ID, "block/belt/particle"));

		variantBuilder.forAllStatesExcept(state -> ConfiguredModel.builder().modelFile(modelFile).build(), AbstractTransmissionChainBlock.WATERLOGGED);
	}

	public static void handheldItem(DataGenContext<Item, BlockItem> ctx, RegistrateItemModelProvider prov)
	{
		prov.handheld(ctx);
	}
}
