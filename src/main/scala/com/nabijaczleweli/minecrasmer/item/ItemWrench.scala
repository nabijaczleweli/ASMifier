package com.nabijaczleweli.minecrasmer.item

import com.nabijaczleweli.minecrasmer.block.BlockComputerOff
import com.nabijaczleweli.minecrasmer.creativetab.CreativeTabMineCrASMer
import com.nabijaczleweli.minecrasmer.reference.Reference
import net.minecraft.block.Block
import net.minecraft.item.ItemTool
import net.minecraftforge.common.util.EnumHelper

import scala.collection.JavaConversions._

object ItemWrench extends ItemTool(0, EnumHelper.addToolMaterial("Wrench", 1000, -1, 1000, 1, 0), _ItemWrench.effectiveAgainst) {
	val effectiveAgainst = _ItemWrench.effectiveAgainst

	setUnlocalizedName(Reference.NAMESPACED_PREFIX + "wrench")
	setCreativeTab(CreativeTabMineCrASMer)
	setTextureName(Reference.NAMESPACED_PREFIX + "wrench")
	setHarvestLevel("wrench", 3)
}

private object _ItemWrench {
	val effectiveAgainst = Set[Block](BlockComputerOff)
}
