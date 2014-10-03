package com.nabijaczleweli.minecrasmer.proxy

import com.nabijaczleweli.minecrasmer.MineCrASMer
import com.nabijaczleweli.minecrasmer.block.{BlockComputerOff, BlockComputerOn, BlockLiquidCrystalFluid, BlockOverclocker}
import com.nabijaczleweli.minecrasmer.entity.tile.{TileEntityComputer, TileEntityOverclocker}
import com.nabijaczleweli.minecrasmer.handler.{BlocksHandler, CraftingHandler}
import com.nabijaczleweli.minecrasmer.item._
import com.nabijaczleweli.minecrasmer.reference.Container
import com.nabijaczleweli.minecrasmer.render.gui.GUIHandler
import com.nabijaczleweli.minecrasmer.util.RegistrationUtils._
import com.nabijaczleweli.minecrasmer.worldgen.WorldGenLiquidCrystal
import cpw.mods.fml.common.FMLCommonHandler
import cpw.mods.fml.common.network.NetworkRegistry
import cpw.mods.fml.common.registry.GameRegistry
import net.minecraft.block.Block
import net.minecraft.init.Blocks
import net.minecraft.item.ItemStack
import net.minecraftforge.common.MinecraftForge
import net.minecraftforge.fluids.FluidRegistry
import net.minecraftforge.oredict.{ShapedOreRecipe, ShapelessOreRecipe}

class CommonProxy extends IProxy {
	final override def registerItemsAndBlocks() {
		ItemWrench.register()
		ItemPlastic.register()
		ItemPCB.register()

		(BlockLiquidCrystalFluid: Block).register()
		BlockComputerOff.register()
		BlockComputerOn.register()
		BlockOverclocker.register()

		Container.scoopEmpty.register()
		Container.scoopLiquidCrystal.register()
	}

	final override def registerOreDict() {
		ItemWrench.registerOreDict()
		ItemPlastic.registerOreDict()
	}

	override def registerGUIs() {
		NetworkRegistry.INSTANCE.registerGuiHandler(MineCrASMer, GUIHandler)
	}

	override def registerEvents() {
		MinecraftForge.EVENT_BUS register BlocksHandler
		FMLCommonHandler.instance.bus register CraftingHandler
	}

	override def registerEntities() {
		GameRegistry.registerTileEntity(classOf[TileEntityComputer], "PComputer")
		GameRegistry.registerTileEntity(classOf[TileEntityOverclocker], "Overclocker")
	}

	override def registerFluids() {
		FluidRegistry registerFluid Container.liquidCrystal
	}

	override def registerRenderers() {}

	final override def registerOreGen() {
		GameRegistry.registerWorldGenerator(WorldGenLiquidCrystal, 1)
	}

	final override def registerRecipes() {
		GameRegistry.addSmelting(Container.scoopLiquidCrystal, new ItemStack(ItemPlastic, 1, ItemPlastic.plasticDamage), 5)

		val LCD = new ItemStack(ItemPCB, 1, ItemPCB.LCDDamage)
		val emptyPCB = new ItemStack(ItemPCB, 1, ItemPCB.emptyPCBDamage)
		val plastic = new ItemStack(ItemPlastic, 1, ItemPlastic.plasticDamage)

		GameRegistry.addRecipe(new ShapelessOreRecipe(plastic, "materialPolymer", "materialPolymer", "materialPolymer", "materialPolymer"))
		GameRegistry.addRecipe(new ShapedOreRecipe(LCD, "PPP", "PLP", "PGP", 'P': Character, "materialPlastic", 'L': Character, new ItemStack(Container.scoopLiquidCrystal), 'G': Character, "nuggetGold"))
		GameRegistry.addRecipe(new ShapedOreRecipe(LCD, "PPP", "PLP", "PGP", 'P': Character, "materialPlastic", 'L': Character, new ItemStack(Container.scoopLiquidCrystal), 'G': Character, "nuggetGold"))
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ItemPCB, 1, ItemPCB.PCBLCDDamage), " L ", "GPG", 'P': Character, new ItemStack(ItemPCB, 1, ItemPCB.emptyPCBDamage), 'L': Character, LCD, 'G': Character,
		                                           "nuggetGold"))
		GameRegistry.addRecipe(new ShapedOreRecipe(emptyPCB, " G ", "PNP", " Gp", 'P': Character, plastic, 'G': Character, "paneGlass", 'N': Character, "nuggetGold", 'p': Character, Blocks.piston))
		GameRegistry.addRecipe(new ShapedOreRecipe(emptyPCB, " G ", "PNP", " Gp", 'P': Character, plastic, 'G': Character, "paneGlass", 'N': Character, "nuggetGold", 'p': Character, Blocks.sticky_piston))
	}
}
