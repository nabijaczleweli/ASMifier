package com.nabijaczleweli.minecrasmer.compat.nei

import codechicken.nei.api.{API, IConfigureNEI}
import com.nabijaczleweli.minecrasmer.reference.{Container, Reference}
import cpw.mods.fml.common.Optional

@Optional.Interface(iface = "com.nabijaczleweli.minecrasmer.compat.nei.NEIMineCrASMerConfig", modid = "NotEnoughItems", striprefs = true)
class NEIMineCrASMerConfig extends IConfigureNEI {
	@Optional.Method(modid = "NotEnoughItems")
	override def loadConfig() {
		Container.log info s"Initializing $getName version $getVersion..."

		API.registerUsageHandler(NEIInWorldCraftingRecipeManager)
		API.registerRecipeHandler(NEIInWorldCraftingRecipeManager)
	}

	@Optional.Method(modid = "NotEnoughItems")
	override def getName =
		Reference.MOD_NAME + " built-in NEI plugin"

	@Optional.Method(modid = "NotEnoughItems")
	override def getVersion =
		Reference.VERSION
}
