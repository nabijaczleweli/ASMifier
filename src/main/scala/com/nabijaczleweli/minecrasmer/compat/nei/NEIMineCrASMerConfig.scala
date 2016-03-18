/*package com.nabijaczleweli.minecrasmer.compat.nei

import codechicken.nei.api.{API, IConfigureNEI}
import com.nabijaczleweli.minecrasmer.reference.{Container, Reference}
import com.nabijaczleweli.minecrasmer.util.CompatUtil._
import net.minecraftforge.fml.common.Optional

@Optional.Interface(iface = "com.nabijaczleweli.minecrasmer.compat.nei.NEIMineCrASMerConfig", modid = "NotEnoughItems", striprefs = true)
class NEIMineCrASMerConfig extends IConfigureNEI {
	@Optional.Method(modid = "NotEnoughItems")
	override def loadConfig() =
		if(NEI.shouldPreLoad && NEI.hasAllLoaded && NEI.active != null) {
			Container.log info s"Initializing $getName version $getVersion..."

			API registerUsageHandler new NEIMineCrASMerInWorldCraftingManager
			API registerRecipeHandler new NEIMineCrASMerInWorldCraftingManager
		} else
			Container.log info s"NOT initializing $getName, since it\'s not enabled"

	@Optional.Method(modid = "NotEnoughItems")
	override def getName =
		Reference.MOD_NAME + " built-in NEI plugin"

	@Optional.Method(modid = "NotEnoughItems")
	override def getVersion =
		Reference.VERSION
}
*/
