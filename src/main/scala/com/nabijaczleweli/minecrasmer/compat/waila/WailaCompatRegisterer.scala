package com.nabijaczleweli.minecrasmer.compat.waila

import com.nabijaczleweli.minecrasmer.block.{BlockAdditionalCPU, BlockOverclocker, ComputerGeneric}
import com.nabijaczleweli.minecrasmer.util.StringUtils._
import cpw.mods.fml.relauncher.{Side, SideOnly}
import mcp.mobius.waila.api.IWailaRegistrar

@SideOnly(Side.CLIENT)
object WailaCompatRegisterer {
	final val pathToRegisterMethod = (getClass.getName before "$") + ".registerWailaCompats"

	@SuppressWarnings(Array("unused"))
	def registerWailaCompats(registrar: IWailaRegistrar) {
		registrar.registerBodyProvider(ProviderComputer, classOf[ComputerGeneric])
		registrar.registerStackProvider(ProviderComputer, classOf[ComputerGeneric])

		registrar.registerBodyProvider(ProviderOverclocker, BlockOverclocker.getClass)
		registrar.registerBodyProvider(ProviderAdditionalCPU, BlockAdditionalCPU.getClass)
	}
}
