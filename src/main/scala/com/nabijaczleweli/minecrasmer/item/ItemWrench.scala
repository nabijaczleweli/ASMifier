package com.nabijaczleweli.minecrasmer.item

import com.nabijaczleweli.minecrasmer.block.BlockComputerOff
import com.nabijaczleweli.minecrasmer.creativetab.CreativeTabMineCrASMer
import com.nabijaczleweli.minecrasmer.reference.{Container, Reference}
import net.minecraft.block.Block
import net.minecraft.item.ItemTool

import scala.collection.JavaConversions._

object ItemWrench extends ItemTool(0, Container.materialWrench, _ItemWrench.effectiveAgainst) {
	val effectiveAgainst = _ItemWrench.effectiveAgainst

	setUnlocalizedName(Reference.NAMESPACED_PREFIX + "wrench")
	setCreativeTab(CreativeTabMineCrASMer)
	setTextureName(Reference.NAMESPACED_PREFIX + "wrench")
	setHarvestLevel("wrench", 3)
}

private object _ItemWrench {
	val effectiveAgainst = Set[Block](BlockComputerOff)
}
