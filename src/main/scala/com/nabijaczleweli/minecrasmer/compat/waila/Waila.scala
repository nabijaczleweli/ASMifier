package com.nabijaczleweli.minecrasmer.compat.waila

import com.nabijaczleweli.minecrasmer.compat._
import net.minecraftforge.fml.common.event.FMLInterModComms
import net.minecraftforge.fml.relauncher.Side
import net.minecraftforge.fml.relauncher.Side.CLIENT

class Waila extends ICompat {
	override def getModIDs =
		"Waila" :: Nil

	override def preLoad(side: Side) =
		Empty

	override def load(side: Side) =
		side match {
			case CLIENT =>
				var notAllSent = false

				notAllSent |= !FMLInterModComms.sendMessage(getModIDs.head, "register", WailaCompatRegisterer.pathToRegisterMethod)

				if(notAllSent)
					Failed
				else
					Successful
			case _ =>
				WrongSide
		}
}
