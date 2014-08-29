package com.nabijaczleweli.minecrasmer.reference

import net.minecraft.block.material.{MapColor, Material}
import net.minecraftforge.common.util.EnumHelper

object Container {
	lazy val materialWrench = EnumHelper.addToolMaterial("Wrench", 1000, -1, 1000, 1, 0)
	lazy val materialComputer = new Material(MapColor.grayColor)
}
