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
		val LCD = new ItemStack(ItemPCB, 1, ItemPCB.LCDDamage)
		val emptyPCB = new ItemStack(ItemPCB, 1, ItemPCB.emptyPCBDamage)
		val LCDPCB = new ItemStack(ItemPCB, 1, ItemPCB.PCBLCDDamage)
		val polymer = new ItemStack(ItemPlastic, 1, ItemPlastic.polymerDamage)
		val plastic = new ItemStack(ItemPlastic, 1, ItemPlastic.plasticDamage)
		val monomer = new ItemStack(ItemPlastic, 1, ItemPlastic.polymerDamage)
		val crystalScoop = new ItemStack(Container.scoopLiquidCrystal)
		val polymerOre = ItemPlastic oreDictName ItemPlastic.polymerDamage
		val plasticOre = ItemPlastic oreDictName ItemPlastic.plasticDamage
		val monomerOre = ItemPlastic oreDictName ItemPlastic.monomerDamage
		val goldOre = "nuggetGold"
		val paneOre = "paneGlass"

		GameRegistry.addSmelting(crystalScoop, monomer, 5)

		new ShapelessOreRecipe(polymer, monomerOre, monomerOre, monomerOre, monomerOre).register()
		new ShapelessOreRecipe(plastic, polymerOre, polymerOre, polymerOre, polymerOre).register()
		new ShapedOreRecipe(LCD, "PPP", "PLP", "PGP", 'P': Character, plasticOre, 'L': Character, crystalScoop, 'G': Character, goldOre).register()
		new ShapedOreRecipe(LCDPCB, " L ", "GPG", 'P': Character, emptyPCB, 'L': Character, LCD, 'G': Character, goldOre).register()
		new ShapedOreRecipe(emptyPCB, " G ", "PNP", " Gp", 'P': Character, plastic, 'G': Character, paneOre, 'N': Character, goldOre, 'p': Character, Blocks.piston).register()
		new ShapedOreRecipe(emptyPCB, " G ", "PNP", " Gp", 'P': Character, plastic, 'G': Character, paneOre, 'N': Character, goldOre, 'p': Character, Blocks.sticky_piston).register()
	}
}
