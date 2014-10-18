package com.nabijaczleweli.minecrasmer.proxy

import com.nabijaczleweli.minecrasmer.MineCrASMer
import com.nabijaczleweli.minecrasmer.block._
import com.nabijaczleweli.minecrasmer.entity.tile.{TileEntityAdditionalCPU, TileEntityComputer, TileEntityOverclocker}
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
		ItemCPU.register()
		Container.socketCPU.register()
		Container.stoneRod.register()

		(BlockLiquidCrystalFluid: Block).register()
		BlockComputerOff.register()
		BlockComputerOn.register()
		BlockOverclocker.register()
		BlockAdditionalCPU.register()

		Container.scoopEmpty.register()
		Container.scoopLiquidCrystal.register()
	}

	final override def registerOreDict() =
		CommonProxy.oreRegistrables foreach {_.registerOreDict()}

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
		GameRegistry.registerTileEntity(classOf[TileEntityAdditionalCPU], "AdditionalCPU")
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
		val PCBLCD = new ItemStack(ItemPCB, 1, ItemPCB.PCBLCDDamage)
		val PCBElements = new ItemStack(ItemPCB, 1, ItemPCB.fullPCBDamage)
		val polymer = new ItemStack(ItemPlastic, 1, ItemPlastic.polymerDamage)
		val plastic = new ItemStack(ItemPlastic, 1, ItemPlastic.plasticDamage)
		val monomer = new ItemStack(ItemPlastic, 1, ItemPlastic.polymerDamage)
		val crystalScoop = new ItemStack(Container.scoopLiquidCrystal)

		val polymerOre = ItemPlastic oreDictName ItemPlastic.polymerDamage
		val plasticOre = ItemPlastic oreDictName ItemPlastic.plasticDamage
		val monomerOre = ItemPlastic oreDictName ItemPlastic.monomerDamage
		val goldOre = "nuggetGold"
		val paneOre = "paneGlass"
		val ironOre = "ingotIron"
		val redDyeOre = "dyeRed"
		val stoneRodOre = "rodStone"

		GameRegistry.addSmelting(crystalScoop, monomer, 5)

		new ShapelessOreRecipe(polymer, monomerOre, monomerOre, monomerOre, monomerOre).register()
		new ShapelessOreRecipe(plastic, polymerOre, polymerOre, polymerOre, polymerOre).register()
		new ShapelessOreRecipe(PCBElements, emptyPCB, Container.socketCPU, goldOre).register()
		new ShapedOreRecipe(LCD, "PPP", "PLP", "PGP", 'P': Character, plasticOre, 'L': Character, crystalScoop, 'G': Character, goldOre).register()
		new ShapedOreRecipe(PCBLCD, " L ", "GPG", 'P': Character, emptyPCB, 'L': Character, LCD, 'G': Character, goldOre).register()
		new ShapedOreRecipe(emptyPCB, " G ", "PNP", " Gp", 'P': Character, plastic, 'G': Character, paneOre, 'N': Character, goldOre, 'p': Character, Blocks.piston).register()
		new ShapedOreRecipe(emptyPCB, " G ", "PNP", " Gp", 'P': Character, plastic, 'G': Character, paneOre, 'N': Character, goldOre, 'p': Character, Blocks.sticky_piston).register()
		new ShapedOreRecipe(Container.socketCPU, "P P", " P ", 'P': Character, plastic).register()
		new ShapedOreRecipe(ItemWrench, "  I", " T ", "TRR", 'I': Character, ironOre, 'T': Character, stoneRodOre, 'R': Character, redDyeOre).register()
		new ShapedOreRecipe(new ItemStack(Container.stoneRod, 2), "C", "C", 'C': Character, Blocks.cobblestone).register()
		new ShapedOreRecipe(new ItemStack(Container.stoneRod, 4), "C", "C", 'C': Character, Blocks.stone).register()
	}
}

private object CommonProxy {
	private final lazy val oreRegistrables = ItemWrench :: ItemPlastic :: ItemCPU :: Container :: Nil
}
