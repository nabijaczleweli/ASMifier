package com.nabijaczleweli.minecrasmer.creativetab

import com.nabijaczleweli.minecrasmer.block.BlockComputerOff
import net.minecraft.creativetab.CreativeTabs
import net.minecraft.item.Item
import net.minecraft.util.StatCollector

object CreativeTabMineCrASMer extends CreativeTabs("ASMifier") {
	override def getTabIconItem =
		Item getItemFromBlock BlockComputerOff

	override def getTranslatedTabLabel =
		StatCollector translateToLocal (super.getTranslatedTabLabel + ".name")
}
