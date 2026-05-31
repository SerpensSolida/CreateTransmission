package com.serpenssolida.createtransmission.forge;

import com.serpenssolida.createtransmission.CTModels;
import com.serpenssolida.createtransmission.CreateTransmission;
import com.serpenssolida.createtransmission.CreateTransmissionClient;
import com.serpenssolida.createtransmission.ponder.CTPonderIndex;
import net.createmod.ponder.foundation.PonderIndex;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(CreateTransmission.MOD_ID)
public class CreateTransmissionModForge
{
    public static ModLoadingContext context;

    public CreateTransmissionModForge()
    {
        context = FMLJavaModLoadingContext.get();

        // registrate must be given the mod event bus on forge before registration
        IEventBus eventBus = FMLJavaModLoadingContext.get().getModEventBus();
        CreateTransmission.REGISTRATE.registerEventListeners(eventBus);
        CreateTransmission.init();
        DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> CreateTransmissionClient::init);
        CTCreativeTabsImpl.register(eventBus);
    }
}
