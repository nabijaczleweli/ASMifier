package com.nabijaczleweli.minecrasmer.proxy

import com.nabijaczleweli.minecrasmer.MineCrASMer
import com.nabijaczleweli.minecrasmer.block.{BlockOverclocker, BlockComputerOff, BlockComputerOn, BlockLiquidCrystalFluid}
import com.nabijaczleweli.minecrasmer.entity.tile.{TileEntityOverclocker, TileEntityComputer}
import com.nabijaczleweli.minecrasmer.handler.{BlocksHandler, CraftingHandler}
import com.nabijaczleweli.minecrasmer.item._
import com.nabijaczleweli.minecrasmer.reference.Container
import com.nabijaczleweli.minecrasmer.render.gui.GUIHandler
import com.nabijaczleweli.minecrasmer.util.RegistrationUtils._
import com.nabijaczleweli.minecrasmer.worldgen.WorldGenLiquidCrystal
import cpw.mods.fml.common.FMLCommonHandler
import cpw.mods.fml.common.network.NetworkRegistry
import cpw.mods.fml.common.registry.GameRegistry
import net.minecraft.init.Blocks
import net.minecraft.item.ItemStack
import net.minecraftforge.common.MinecraftForge
import net.minecraftforge.fluids.FluidRegistry
import net.minecraftforge.oredict.{OreDictionary, ShapedOreRecipe, ShapelessOreRecipe}

class CommonProxy extends IProxy {
	final override def registerItemsAndBlocks() {
		ItemWrench.register()
		ItemPlastic.register()
		ItemPCB.register()

		BlockLiquidCrystalFluid.register()
		BlockComputerOff.register()
		BlockComputerOn.register()
		BlockOverclocker.register()

		Container.scoopEmpty.register()
		Container.scoopLiquidCrystal.register()
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
		@inline
		implicit def obj[T](any: T) =  //  This needs to be a thing, even tho anything isInstanceOf[Object], but ForgeGradle's scalac doesn't understand it
			any.asInstanceOf[Object]

		GameRegistry.addSmelting(Container.scoopLiquidCrystal, new ItemStack(ItemPlastic, 1, ItemPlastic.plasticDamage), 5)

		val LCD = new ItemStack(ItemPCB, 1, ItemPCB.LCDDamage)
		val emptyPCB = new ItemStack(ItemPCB, 1, ItemPCB.emptyPCBDamage)
		val plastic = new ItemStack(ItemPlastic, 1, ItemPlastic.plasticDamage)

		GameRegistry.addRecipe(new ShapelessOreRecipe(plastic, "materialPolymer", "materialPolymer", "materialPolymer", "materialPolymer"))
		GameRegistry.addRecipe(new ShapedOreRecipe(LCD, obj("PPP"), obj("PLP"), obj("PGP"), obj('P'), obj("materialPlastic"), obj('L'), obj(new ItemStack(Container.scoopLiquidCrystal)), obj('G'), obj("nuggetGold")))
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ItemPCB, 1, ItemPCB.PCBLCDDamage), obj(" L "), obj("GPG"), obj('P'), obj(new ItemStack(ItemPCB, 1, ItemPCB.emptyPCBDamage)), obj('L'), obj(LCD), obj('G'),
		                                           obj("nuggetGold")))
		GameRegistry.addRecipe(new ShapedOreRecipe(emptyPCB, obj(" G "), obj("PNP"), obj(" Gp"), obj('P'), obj(plastic), obj('G'), obj("paneGlass"), obj('N'), obj("nuggetGold"), obj('p'), obj(Blocks.piston)))
		GameRegistry.addRecipe(new ShapedOreRecipe(emptyPCB, obj(" G "), obj("PNP"), obj(" Gp"), obj('P'), obj(plastic), obj('G'), obj("paneGlass"), obj('N'), obj("nuggetGold"), obj('p'), obj(Blocks.sticky_piston)))
	}
}
