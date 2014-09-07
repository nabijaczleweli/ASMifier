package com.nabijaczleweli.minecrasmer.proxy

import com.nabijaczleweli.minecrasmer.MineCrASMer
import com.nabijaczleweli.minecrasmer.block.{BlockLiquidCrystalFluid, BlockComputerOff, BlockComputerOn}
import com.nabijaczleweli.minecrasmer.entity.tile.TileEntityComputer
import com.nabijaczleweli.minecrasmer.gui.GUIHandler
import com.nabijaczleweli.minecrasmer.handler.BlocksHandler
import com.nabijaczleweli.minecrasmer.item.{ItemScoop, ItemWrench}
import com.nabijaczleweli.minecrasmer.reference.Container
import cpw.mods.fml.common.network.NetworkRegistry
import cpw.mods.fml.common.registry.GameRegistry
import net.minecraft.block.Block
import net.minecraft.item.{Item, ItemStack}
import net.minecraftforge.common.MinecraftForge
import net.minecraftforge.fluids.{Fluid, FluidContainerRegistry, FluidRegistry}
import net.minecraftforge.oredict.OreDictionary

class CommonProxy extends IProxy {
	final override def registerItemsAndBlocks() {
		@inline
		def defaultRegisterItem(it: Item) =
			GameRegistry.registerItem(it, it.getUnlocalizedName.substring(it.getUnlocalizedName.indexOf(':') + 1))
		@inline
		def defaultRegisterBlock(bl: Block) =
			GameRegistry.registerBlock(bl, bl.getUnlocalizedName.substring(bl.getUnlocalizedName.indexOf(':') + 1))
		@inline
		def defaultRegisterScoop(fl: Fluid, it: ItemScoop) =
			FluidContainerRegistry.registerFluidContainer(FluidRegistry.getFluidStack(fl.getName, ItemScoop.capacity), new ItemStack(it),
			                                              new ItemStack(Container.scoopEmpty))

		defaultRegisterItem(ItemWrench)
		defaultRegisterItem(Container.scoopEmpty)
		defaultRegisterItem(Container.scoopLiquidCrystal)

		defaultRegisterBlock(BlockComputerOff)
		defaultRegisterBlock(BlockComputerOn)
		defaultRegisterBlock(BlockLiquidCrystalFluid)

		defaultRegisterScoop(Container.liquidCrystal, Container.scoopLiquidCrystal)
	}

	final override def registerOreDict() {
		OreDictionary.registerOre("toolWrench", ItemWrench)
	}

	override def registerGUIs() {
		NetworkRegistry.INSTANCE.registerGuiHandler(MineCrASMer, GUIHandler)
	}

	override def registerEvents() {
		MinecraftForge.EVENT_BUS register BlocksHandler
	}

	override def registerEntities() {
		GameRegistry.registerTileEntity(classOf[TileEntityComputer], "PComputer")
	}

	override def registerFluids() {
		FluidRegistry registerFluid Container.liquidCrystal
	}
}
