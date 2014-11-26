package com.nabijaczleweli.minecrasmer.compat

import appeng.api.AEApi
import com.nabijaczleweli.minecrasmer.entity.tile.{TileEntityAdditionalCPU, TileEntityComputer, TileEntityOverclocker}
import com.nabijaczleweli.minecrasmer.item.ItemCPU
import com.nabijaczleweli.minecrasmer.util.IOreDictRegisterable
import net.minecraftforge.fml.common.event.FMLInterModComms
import net.minecraftforge.fml.relauncher.Side
import net.minecraftforge.oredict.OreDictionary

class AE2 extends ICompat with IOreDictRegisterable {
	override def getModIDs =
		"appliedenergistics2" :: Nil

	override def load(side: Side) = {
		var notAllSent = false

		notAllSent |= !FMLInterModComms.sendMessage(getModIDs(0), "whitelist-spatial", classOf[TileEntityComputer].getCanonicalName)
		notAllSent |= !FMLInterModComms.sendMessage(getModIDs(0), "whitelist-spatial", classOf[TileEntityOverclocker].getCanonicalName)
		notAllSent |= !FMLInterModComms.sendMessage(getModIDs(0), "whitelist-spatial", classOf[TileEntityAdditionalCPU].getCanonicalName)

		registerOreDict()

		if(notAllSent)
			Failed
		else
			Successful
	}

	override def preLoad(side: Side) =
		Empty

	override def registerOreDict() {
		OreDictionary.registerOre(ItemCPU oreDictName 0, AEApi.instance().materials.materialLogicProcessor stack 1)
		OreDictionary.registerOre(ItemCPU oreDictName 1, AEApi.instance().materials.materialCalcProcessor stack 1)
		OreDictionary.registerOre(ItemCPU oreDictName 2, AEApi.instance().materials.materialEngProcessor stack 1)
	}
}
