package com.nabijaczleweli.minecrasmer.compat.waila

import com.nabijaczleweli.minecrasmer.compat.ICompat
import cpw.mods.fml.common.event.FMLInterModComms

class Waila extends ICompat {
	override def getModIDs =
		"Waila" :: Nil

	override def preLoad =
		true

	override def load = {
		var notAllSent = false

		notAllSent |= !FMLInterModComms.sendMessage(getModIDs(0), "register", WailaCompatRegisterer.pathToRegisterMethod)

		!notAllSent
	}
}
