package com.nabijaczleweli.minecrasmer.compat.waila

import com.nabijaczleweli.minecrasmer.block.{BlockAccessoryAdditionalCPU, BlockAccessoryOverclocker, ComputerGeneric}
import com.nabijaczleweli.minecrasmer.util.StringUtils._
import mcp.mobius.waila.api.IWailaRegistrar
import net.minecraftforge.fml.relauncher.{Side, SideOnly}

@SideOnly(Side.CLIENT)
object WailaCompatRegisterer {
	final val pathToRegisterMethod = (getClass.getName before "$") + ".registerWailaCompats"

	@SuppressWarnings(Array("unused"))
	def registerWailaCompats(registrar: IWailaRegistrar) {
		registrar.registerBodyProvider(ProviderComputer, classOf[ComputerGeneric])
		registrar.registerStackProvider(ProviderComputer, classOf[ComputerGeneric])

		registrar.registerBodyProvider(ProviderOverclocker, BlockAccessoryOverclocker.getClass)
		registrar.registerBodyProvider(ProviderAdditionalCPU, BlockAccessoryAdditionalCPU.getClass)
	}
}
