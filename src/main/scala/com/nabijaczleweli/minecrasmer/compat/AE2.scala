package com.nabijaczleweli.minecrasmer.compat

import com.nabijaczleweli.minecrasmer.entity.tile.TileEntityComputer
import cpw.mods.fml.common.event.FMLInterModComms
import cpw.mods.fml.relauncher.Side

class AE2 extends ICompat {
	override def getModIDs =
		"appliedenergistics2" :: Nil

	override def load(side: Side) = {
		var notAllSent = false

		notAllSent |= !FMLInterModComms.sendMessage(getModIDs(0), "whitelist-spatial", classOf[TileEntityComputer].getCanonicalName)

		if(notAllSent)
			Failed
		else
			Successful
	}

	override def preLoad(side: Side) =
		Successful
}
