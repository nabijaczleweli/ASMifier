package com.nabijaczleweli.minecrasmer.compat

import com.nabijaczleweli.minecrasmer.entity.tile.TileEntityComputer
import cpw.mods.fml.common.event.FMLInterModComms

class AE2 extends ICompat {
	override def getModIDs =
		"appliedenergistics2" :: Nil

	override def load = {
		var notAllSent = false

		notAllSent |= !FMLInterModComms.sendMessage(getModIDs(0), "whitelist-spatial", classOf[TileEntityComputer].getCanonicalName)

		!notAllSent
	}

	override def preLoad =
		true
}
