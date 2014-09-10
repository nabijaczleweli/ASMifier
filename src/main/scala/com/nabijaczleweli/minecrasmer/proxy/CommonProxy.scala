package com.nabijaczleweli.minecrasmer.proxy

import com.nabijaczleweli.minecrasmer.MineCrASMer
import com.nabijaczleweli.minecrasmer.block.{BlockComputerOff, BlockComputerOn, BlockLiquidCrystalFluid}
import com.nabijaczleweli.minecrasmer.entity.tile.TileEntityComputer
import com.nabijaczleweli.minecrasmer.render.gui.GUIHandler
import com.nabijaczleweli.minecrasmer.handler.BlocksHandler
import com.nabijaczleweli.minecrasmer.item.{ItemPCB, ItemPlastic, ItemScoop, ItemWrench}
import com.nabijaczleweli.minecrasmer.reference.Container
import cpw.mods.fml.common.network.NetworkRegistry
import cpw.mods.fml.common.registry.GameRegistry
import net.minecraft.block.Block
import net.minecraft.item.{Item, ItemStack}
import net.minecraftforge.common.MinecraftForge
import net.minecraftforge.fluids.{FluidContainerRegistry, FluidRegistry, IFluidBlock}
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
		def defaultRegisterScoop(it: ItemScoop) =
			FluidContainerRegistry.registerFluidContainer(FluidRegistry.getFluidStack(it.contains.asInstanceOf[IFluidBlock].getFluid.getName, ItemScoop.capacity),
			                                              new ItemStack(it), new ItemStack(Container.scoopEmpty))

		defaultRegisterItem(ItemWrench)
		defaultRegisterItem(Container.scoopEmpty)
		defaultRegisterItem(Container.scoopLiquidCrystal)
		defaultRegisterItem(ItemPlastic)
		defaultRegisterItem(ItemPCB)

		defaultRegisterBlock(BlockComputerOff)
		defaultRegisterBlock(BlockComputerOn)
		defaultRegisterBlock(BlockLiquidCrystalFluid)

		defaultRegisterScoop(Container.scoopLiquidCrystal)
	}

	final override def registerOreDict() {
		OreDictionary.registerOre("toolWrench", ItemWrench)

		val is = new ItemStack(ItemPlastic)
		for(i <- ItemPlastic.monomerDamage to ItemPlastic.plasticDamage) {
			is setItemDamage i
			OreDictionary.registerOre(ItemPlastic oreDictName i, is)
		}
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

	override def registerRenderers() {}
}
