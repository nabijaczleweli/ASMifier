package com.nabijaczleweli.minecrasmer.compat

//import appeng.api.AEApi
import com.nabijaczleweli.minecrasmer.entity.tile.{TileEntityAdditionalCPU, TileEntityComputer, TileEntityOverclocker}
import com.nabijaczleweli.minecrasmer.util.IOreDictRegisterable
import net.minecraftforge.fml.common.event.FMLInterModComms
import net.minecraftforge.fml.relauncher.Side

import scala.language.postfixOps

class AE2 extends ICompat with IOreDictRegisterable {
	override def getModIDs =
		"appliedenergistics2" :: Nil

	override def load(side: Side) = {
		var notAllSent = false

		notAllSent |= !FMLInterModComms.sendMessage(getModIDs.head, "whitelist-spatial", classOf[TileEntityComputer].getCanonicalName)
		notAllSent |= !FMLInterModComms.sendMessage(getModIDs.head, "whitelist-spatial", classOf[TileEntityOverclocker].getCanonicalName)
		notAllSent |= !FMLInterModComms.sendMessage(getModIDs.head, "whitelist-spatial", classOf[TileEntityAdditionalCPU].getCanonicalName)

		registerOreDict()

		if(notAllSent)
			Failed
		else
			Successful
	}

	override def preLoad(side: Side) =
		Empty

	override def registerOreDict() {
		/*OreDictionary.registerOre(ItemCPU oreDictName 0, AEApi.instance.definitions.materials.logicProcessor maybeStack 1 get)
		OreDictionary.registerOre(ItemCPU oreDictName 1, AEApi.instance.definitions.materials.calcProcessor maybeStack 1 get)
		OreDictionary.registerOre(ItemCPU oreDictName 2, AEApi.instance.definitions.materials.engProcessor maybeStack 1 get)*/
	}
}
