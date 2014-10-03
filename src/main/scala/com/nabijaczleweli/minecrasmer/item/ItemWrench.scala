package com.nabijaczleweli.minecrasmer.item

import com.nabijaczleweli.minecrasmer.block.BlockComputerOff
import com.nabijaczleweli.minecrasmer.creativetab.CreativeTabMineCrASMer
import com.nabijaczleweli.minecrasmer.reference.{Container, Reference}
import com.nabijaczleweli.minecrasmer.util.IOreDictRegisterable
import net.minecraft.block.Block
import net.minecraft.item.ItemTool
import net.minecraftforge.oredict.OreDictionary

import scala.collection.JavaConversions._

object ItemWrench extends ItemTool(0, Container.materialWrench, _ItemWrench.effectiveAgainst) with IOreDictRegisterable {
	val effectiveAgainst = _ItemWrench.effectiveAgainst

	setUnlocalizedName(Reference.NAMESPACED_PREFIX + "wrench")
	setCreativeTab(CreativeTabMineCrASMer)
	setTextureName(Reference.NAMESPACED_PREFIX + "wrench")
	setHarvestLevel("wrench", 3)

	override def registerOreDict() =
		OreDictionary.registerOre("toolWrench", ItemWrench)
}

private object _ItemWrench {
	val effectiveAgainst = Set[Block](BlockComputerOff)
}
