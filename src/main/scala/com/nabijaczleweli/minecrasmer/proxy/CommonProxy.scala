package com.nabijaczleweli.minecrasmer.proxy

import com.nabijaczleweli.minecrasmer.block.BlockComputerOff
import com.nabijaczleweli.minecrasmer.item.ItemWrench
import cpw.mods.fml.common.registry.GameRegistry
import net.minecraft.block.Block
import net.minecraft.item.Item
import net.minecraftforge.oredict.OreDictionary

class CommonProxy extends IProxy {
	final override def registerItemsAndBlocks() {
		@inline
		def defaultRegisterItem(it: Item) =
			GameRegistry.registerItem(it, it.getUnlocalizedName.substring(it.getUnlocalizedName.indexOf(':') + 1))
		@inline
		def defaultRegisterBlock(bl: Block) =
			GameRegistry.registerBlock(bl, bl.getUnlocalizedName.substring(bl.getUnlocalizedName.indexOf(':') + 1))

		defaultRegisterItem(ItemWrench)

		defaultRegisterBlock(BlockComputerOff)
	}

	final override def registerOreDict() {
		OreDictionary.registerOre("toolWrench", ItemWrench)
	}
}
