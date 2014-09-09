package com.nabijaczleweli.minecrasmer.compat.waila;

import com.nabijaczleweli.minecrasmer.block.ComputerGeneric;
import mcp.mobius.waila.api.IWailaRegistrar;

public class WailaCompatRegisterer {
	public static final String pathToRegisterMethod = WailaCompatRegisterer.class.getName() + ".registerWailaCompats";

	@SuppressWarnings("unused")
	public static void registerWailaCompats(IWailaRegistrar registrar) {
		registrar.registerBodyProvider(ProviderComputer$.MODULE$, ComputerGeneric.class);
		registrar.registerStackProvider(ProviderComputer$.MODULE$, ComputerGeneric.class);
	}
}
