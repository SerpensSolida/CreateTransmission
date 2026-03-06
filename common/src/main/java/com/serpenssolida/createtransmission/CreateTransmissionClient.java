package com.serpenssolida.createtransmission;

import com.serpenssolida.createtransmission.ponder.CTPonderIndex;
import net.createmod.ponder.foundation.PonderIndex;

public class CreateTransmissionClient
{
	private CreateTransmissionClient() {}

	public static void init()
	{
		CTModels.init();
		PonderIndex.addPlugin(new CTPonderIndex());
	}
}
