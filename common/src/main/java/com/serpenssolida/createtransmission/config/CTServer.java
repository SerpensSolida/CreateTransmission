package com.serpenssolida.createtransmission.config;

import net.createmod.catnip.config.ConfigBase;

public class CTServer extends ConfigBase
{
	public final ConfigBool placingCheck = b(true, "placing_check", Comments.PLACING_CHECK);

	@Override
	public String getName()
	{
		return "server";
	}

	private class Comments
	{
		public static final String PLACING_CHECK = "Enable or disable transmission chain placement check. When disabled allows transmission chains to be placed everywhere.";
	}
}
