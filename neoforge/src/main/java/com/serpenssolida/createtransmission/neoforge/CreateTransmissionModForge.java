package com.serpenssolida.createtransmission.neoforge;

import com.serpenssolida.createtransmission.CTModels;
import com.serpenssolida.createtransmission.CreateTransmission;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.ModLoadingContext;
import net.neoforged.fml.common.Mod;

@Mod(CreateTransmission.MOD_ID)
public class CreateTransmissionModForge
{
    //public static final ModLoadingContext context = ModLoadingContext.get();

    public CreateTransmissionModForge(IEventBus modBus, ModContainer container)
    {
        // registrate must be given the mod event bus on forge before registration
        CreateTransmission.REGISTRATE.registerEventListeners(modBus);
        CreateTransmission.init();
        CTCreativeTabsImpl.register(modBus);
    }


}
