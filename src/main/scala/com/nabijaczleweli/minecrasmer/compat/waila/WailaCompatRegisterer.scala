package com.nabijaczleweli.minecrasmer.compat.waila

import com.nabijaczleweli.minecrasmer.block.{BlockOverclocker, ComputerGeneric}
import com.nabijaczleweli.minecrasmer.util.StringUtils._
import mcp.mobius.waila.api.IWailaRegistrar

object WailaCompatRegisterer {
	final val pathToRegisterMethod = (getClass.getName before "$") + ".registerWailaCompats"

	@SuppressWarnings(Array("unused"))
	def registerWailaCompats(registrar: IWailaRegistrar) {
		registrar.registerBodyProvider(ProviderComputer, classOf[ComputerGeneric])
		registrar.registerStackProvider(ProviderComputer, classOf[ComputerGeneric])

		registrar.registerBodyProvider(ProviderOverclocker, BlockOverclocker.getClass)
	}
}
