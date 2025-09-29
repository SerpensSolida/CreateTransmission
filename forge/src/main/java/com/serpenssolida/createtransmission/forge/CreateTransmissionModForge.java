package com.serpenssolida.createtransmission.forge;

import com.serpenssolida.createtransmission.CTModels;
import com.serpenssolida.createtransmission.CreateTransmission;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(CreateTransmission.MOD_ID)
public class CreateTransmissionModForge
{
    public CreateTransmissionModForge()
    {
        // registrate must be given the mod event bus on forge before registration
        IEventBus eventBus = FMLJavaModLoadingContext.get().getModEventBus();
        CreateTransmission.REGISTRATE.registerEventListeners(eventBus);
        CreateTransmission.init();
        DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> CTModels::init);
        CTCreativeTabsImpl.register(eventBus);
    }
}
