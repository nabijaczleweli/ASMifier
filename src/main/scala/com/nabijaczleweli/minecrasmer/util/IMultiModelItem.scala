package com.nabijaczleweli.minecrasmer.util

import net.minecraftforge.fml.relauncher.{Side, SideOnly}

trait IMultiModelItem {
	@SideOnly(Side.CLIENT)
	def registerModels(): Unit
}
