package com.nabijaczleweli.minecrasmer.creativetab

import com.nabijaczleweli.minecrasmer.block.BlockComputerOff
import net.minecraft.creativetab.CreativeTabs
import net.minecraft.item.Item

object CreativeTabMineCrASMer extends CreativeTabs("ASMifier") {
	override def getTabIconItem =
		Item getItemFromBlock BlockComputerOff
}
