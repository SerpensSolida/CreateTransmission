package com.serpenssolida.createtransmission.ponder.neoforge;

import com.simibubi.create.content.decoration.encasing.CasingBlock;
import com.simibubi.create.content.kinetics.belt.BeltBlock;
import com.simibubi.create.content.kinetics.belt.BeltBlockEntity;
import com.simibubi.create.AllBlocks;
import com.tterrag.registrate.util.entry.BlockEntry;
import net.createmod.catnip.nbt.NBTHelper;
import net.createmod.ponder.api.scene.SceneBuilder;
import net.createmod.ponder.api.scene.SceneBuildingUtil;
import net.minecraft.core.BlockPos;

public class CTPonderScenesImpl
{
	public static void encaseBelt(SceneBuilder scene, SceneBuildingUtil util, BlockPos pos, BlockEntry<CasingBlock> casing)
	{
		BeltBlockEntity.CasingType casingType;

		if (casing.equals(AllBlocks.ANDESITE_CASING))
			casingType = BeltBlockEntity.CasingType.ANDESITE;
		else
			casingType = BeltBlockEntity.CasingType.BRASS;

		scene.world().modifyBlock(pos, (s) -> s.setValue(BeltBlock.CASING, true), true);
		scene.world().modifyBlockEntityNBT(util.select().position(pos), BeltBlockEntity.class, (nbt) ->
				NBTHelper.writeEnum(nbt, "Casing", casingType));
	}
}
