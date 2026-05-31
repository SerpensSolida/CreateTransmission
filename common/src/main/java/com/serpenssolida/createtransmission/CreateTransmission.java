package com.serpenssolida.createtransmission;

import com.serpenssolida.createtransmission.config.CTConfig;
import com.serpenssolida.createtransmission.content.chain.AbstractTransmissionChainBlock;
import com.serpenssolida.createtransmission.ponder.CTPonderIndex;
import com.simibubi.create.api.contraption.BlockMovementChecks;
import com.simibubi.create.foundation.data.CreateRegistrate;
import net.minecraft.resources.ResourceLocation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CreateTransmission
{
    public static final String MOD_ID = "createtransmission";
    public static final String NAME = "Create: Transmission";
    public static final Logger LOGGER = LoggerFactory.getLogger(NAME);

    public static final CreateRegistrate REGISTRATE = CreateRegistrate.create(CreateTransmission.MOD_ID);

    CreateTransmission() {}

    /**
     * Initializes mod content.
     */
    public static void init()
    {
        CTCreativeTabs.init();

        CTSpriteShifts.init();

        CTBlocks.init();
        CTBlockEntities.init();
		CTPonderIndex.init();
		CTConfig.register();

        //Check used when glue is applied to transmission chain.
        BlockMovementChecks.registerAttachedCheck((state, world, pos, direction) ->
		{
			if (!(state.getBlock() instanceof AbstractTransmissionChainBlock chain))
				return BlockMovementChecks.CheckResult.PASS;

			return chain.hasShaftTowards(world, pos, state, direction) ? BlockMovementChecks.CheckResult.SUCCESS : BlockMovementChecks.CheckResult.FAIL;
		});
    }

    /**
     * Creates a new {@link ResourceLocation} with the given path.
     * @param path The path of the resource.
     *
     * @return the {@link ResourceLocation} with the given path.
     */
	public static ResourceLocation asResource(String path)
    {
        return new ResourceLocation(MOD_ID, path);
    }
}
