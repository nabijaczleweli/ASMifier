package com.nabijaczleweli.minecrasmer.proxy

import com.nabijaczleweli.minecrasmer.MineCrASMer
import com.nabijaczleweli.minecrasmer.block.{BlockComputerOn, BlockComputerOff}
import com.nabijaczleweli.minecrasmer.entity.tile.TileEntityComputer
import com.nabijaczleweli.minecrasmer.gui.GUIHandler
import com.nabijaczleweli.minecrasmer.handler.BlocksHandler
import com.nabijaczleweli.minecrasmer.item.ItemWrench
import cpw.mods.fml.common.network.NetworkRegistry
import cpw.mods.fml.common.registry.GameRegistry
import net.minecraft.block.Block
import net.minecraft.item.Item
import net.minecraftforge.common.MinecraftForge
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
		defaultRegisterBlock(BlockComputerOn)
	}

	final override def registerOreDict() {
		OreDictionary.registerOre("toolWrench", ItemWrench)
	}

	override def registerGUIs() {
		NetworkRegistry.INSTANCE.registerGuiHandler(MineCrASMer, GUIHandler)
	}

	override def registerEvents() {
		MinecraftForge.EVENT_BUS.register(BlocksHandler)
	}

	override def registerEntities() {
		GameRegistry.registerTileEntity(classOf[TileEntityComputer], "PComputer")
	}
}
