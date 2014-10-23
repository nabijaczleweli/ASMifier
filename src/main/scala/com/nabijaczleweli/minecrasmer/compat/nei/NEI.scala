package com.nabijaczleweli.minecrasmer.compat.nei

import com.nabijaczleweli.minecrasmer.compat.{Empty, ICompat}
import com.nabijaczleweli.minecrasmer.reference.Container
import cpw.mods.fml.relauncher.Side

class NEI extends ICompat {
	override def getModIDs =
		"NotEnoughItems" :: Nil

	override def preLoad(side: Side) =
		Empty

	override def load(side: Side) = {
		Container.log info s"NEI plugin will be automatically loaded by NEI itself on the world load."
		Empty
	}
}
