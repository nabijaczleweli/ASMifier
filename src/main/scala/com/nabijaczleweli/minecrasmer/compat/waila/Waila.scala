package com.nabijaczleweli.minecrasmer.compat.waila

import com.nabijaczleweli.minecrasmer.compat._
import cpw.mods.fml.common.event.FMLInterModComms
import cpw.mods.fml.relauncher.Side
import cpw.mods.fml.relauncher.Side.CLIENT

class Waila extends ICompat {
	override def getModIDs =
		"Waila" :: Nil

	override def preLoad(side: Side) =
		Empty

	override def load(side: Side) =
		side match {
			case CLIENT =>
				var notAllSent = false

				notAllSent |= !FMLInterModComms.sendMessage(getModIDs(0), "register", WailaCompatRegisterer.pathToRegisterMethod)

				if(notAllSent)
					Failed
				else
					Successful
			case _ =>
				WrongSide
		}
}
