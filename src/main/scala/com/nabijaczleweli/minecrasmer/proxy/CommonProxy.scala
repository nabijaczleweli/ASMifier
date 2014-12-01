package com.nabijaczleweli.minecrasmer.proxy

import com.nabijaczleweli.minecrasmer.MineCrASMer
import com.nabijaczleweli.minecrasmer.block._
import com.nabijaczleweli.minecrasmer.entity.Villager._
import com.nabijaczleweli.minecrasmer.entity.tile.{TileEntityAdditionalCPU, TileEntityComputer, TileEntityOverclocker}
import com.nabijaczleweli.minecrasmer.entity.{Villager, EntityItemCleaner, EntityItemShredder}
import com.nabijaczleweli.minecrasmer.handler.{BlocksHandler, CraftingHandler, EntityHandler}
import com.nabijaczleweli.minecrasmer.item._
import com.nabijaczleweli.minecrasmer.reference.Container
import com.nabijaczleweli.minecrasmer.reference.Container._
import com.nabijaczleweli.minecrasmer.render.gui.GUIHandler
import com.nabijaczleweli.minecrasmer.util.RegistrationUtils._
import com.nabijaczleweli.minecrasmer.worldgen.{VillageComponentElectronicShop, WorldGenLiquidCrystal}
import net.minecraft.block.Block
import net.minecraft.init.{Blocks, Items}
import net.minecraft.item.ItemStack
import net.minecraft.util.WeightedRandomChestContent
import net.minecraft.world.gen.structure.MapGenStructureIO
import net.minecraftforge.common.{ChestGenHooks, MinecraftForge}
import net.minecraftforge.fluids.FluidRegistry
import net.minecraftforge.fml.common.FMLCommonHandler
import net.minecraftforge.fml.common.network.NetworkRegistry
import net.minecraftforge.fml.common.registry.{EntityRegistry, GameRegistry, VillagerRegistry}
import net.minecraftforge.oredict.{ShapedOreRecipe, ShapelessOreRecipe}

class CommonProxy extends IProxy {
	final override def registerItemsAndBlocks() {
		ItemWrench.register()
		ItemPlastic.register()
		ItemPCB.register()
		ItemCPU.register()
		socketCPU.register()
		stoneRod.register()
		ItemQuartz.register()
		ItemPartialIron.register()

		(BlockLiquidCrystalFluid: Block).register()
		BlockComputerOff.register()
		BlockComputerOn.register()
		BlockOverclocker.register()
		BlockAdditionalCPU.register()

		scoopEmpty.register()
		scoopLiquidCrystal.register()
	}

	final override def registerOreDict() =
		CommonProxy.oreRegistrables foreach {_.registerOreDict()}

	override def registerGUIs() {
		NetworkRegistry.INSTANCE.registerGuiHandler(MineCrASMer, GUIHandler)
	}

	override def registerEvents() {
		MinecraftForge.EVENT_BUS register BlocksHandler
		MinecraftForge.EVENT_BUS register EntityHandler
		FMLCommonHandler.instance.bus register CraftingHandler
	}

	final override def registerEntities() {
		GameRegistry.registerTileEntity(classOf[TileEntityComputer], "PComputer")
		GameRegistry.registerTileEntity(classOf[TileEntityOverclocker], "Overclocker")
		GameRegistry.registerTileEntity(classOf[TileEntityAdditionalCPU], "AdditionalCPU")

		EntityRegistry.registerModEntity(classOf[EntityItemShredder], "QuartzShredder", 0, MineCrASMer, 80, 1, true)
		EntityRegistry.registerModEntity(classOf[EntityItemCleaner], "QuartzCleaner", 1, MineCrASMer, 80, 1, true)

		VillagerRegistry.instance registerVillagerId electronicsVillagerID
	}

	final override def registerFluids() {
		FluidRegistry registerFluid liquidCrystal
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
		val monomer = new ItemStack(ItemPlastic, 1, ItemPlastic.monomerDamage)
		val crystalScoop = new ItemStack(scoopLiquidCrystal)
		val emptyScoop = new ItemStack(scoopEmpty)
		val quartzPlate = new ItemStack(ItemQuartz, 1, ItemQuartz.plateDamage)
		val halfIronx4 = new ItemStack(ItemPartialIron, 4, ItemPartialIron.halfDamage)
		val halfIronx1 = new ItemStack(ItemPartialIron, 1, ItemPartialIron.halfDamage)
		val quarterIronx8 = new ItemStack(ItemPartialIron, 8, ItemPartialIron.quarterDamage)
		val quarterIronx1 = new ItemStack(ItemPartialIron, 1, ItemPartialIron.quarterDamage)
		val elementaryCPU = new ItemStack(ItemCPU, 1, ItemCPU.elementaryDamage)
		val simpleCPU = new ItemStack(ItemCPU, 1, ItemCPU.simpleDamage)
		val goodCPU = new ItemStack(ItemCPU, 1, ItemCPU.goodDamage)

		val polymerOre = ItemPlastic oreDictName ItemPlastic.polymerDamage
		val plasticOre = ItemPlastic oreDictName ItemPlastic.plasticDamage
		val monomerOre = ItemPlastic oreDictName ItemPlastic.monomerDamage
		val quartzShardsOre = ItemQuartz oreDictName ItemQuartz.shardsDamage
		val goldOre = "nuggetGold"
		val paneOre = "paneGlass"
		val ironOre = "ingotIron"
		val redDyeOre = "dyeRed"
		val stoneRodOre = "rodStone"
		val quartzGemOre = "gemQuartz"
		val quartzBlockOre = "blockQuartz"

		GameRegistry.addSmelting(crystalScoop, monomer, 5)

		new ShapelessOreRecipe(polymer, monomerOre, monomerOre, monomerOre, monomerOre).register()
		new ShapelessOreRecipe(plastic, polymerOre, polymerOre, polymerOre, polymerOre).register()
		new ShapelessOreRecipe(PCBElements, emptyPCB, socketCPU, goldOre).register()
		new ShapelessOreRecipe(quartzPlate, quartzGemOre, quartzGemOre, quartzGemOre, quartzGemOre, Blocks.piston, Blocks.piston).register()
		new ShapelessOreRecipe(quartzPlate, quartzGemOre, quartzGemOre, quartzGemOre, quartzGemOre, Blocks.piston, Blocks.sticky_piston).register()
		new ShapelessOreRecipe(quartzPlate, quartzGemOre, quartzGemOre, quartzGemOre, quartzGemOre, Blocks.sticky_piston, Blocks.sticky_piston).register()
		new ShapelessOreRecipe(quartzPlate, quartzBlockOre, Blocks.piston, Blocks.piston).register()
		new ShapelessOreRecipe(quartzPlate, quartzBlockOre, Blocks.piston, Blocks.sticky_piston).register()
		new ShapelessOreRecipe(quartzPlate, quartzBlockOre, Blocks.sticky_piston, Blocks.sticky_piston).register()
		new ShapelessOreRecipe(quartzPlate, quartzShardsOre, quartzShardsOre, quartzShardsOre, quartzShardsOre, Blocks.piston, Blocks.piston).register()
		new ShapelessOreRecipe(quartzPlate, quartzShardsOre, quartzShardsOre, quartzShardsOre, quartzShardsOre, Blocks.piston, Blocks.sticky_piston).register()
		new ShapelessOreRecipe(quartzPlate, quartzShardsOre, quartzShardsOre, quartzShardsOre, quartzShardsOre, Blocks.sticky_piston, Blocks.sticky_piston).register()
		new ShapedOreRecipe(LCD, "PPP", "PLP", "PGP", 'P': Character, plasticOre, 'L': Character, crystalScoop, 'G': Character, goldOre).register()
		new ShapedOreRecipe(PCBLCD, " L ", "GPG", 'P': Character, emptyPCB, 'L': Character, LCD, 'G': Character, goldOre).register()
		new ShapedOreRecipe(emptyPCB, " G ", "PNP", " Gp", 'P': Character, plasticOre, 'G': Character, paneOre, 'N': Character, goldOre, 'p': Character, Blocks.piston).register()
		new ShapedOreRecipe(emptyPCB, " P ", "GNG", " Pp", 'P': Character, plasticOre, 'G': Character, paneOre, 'N': Character, goldOre, 'p': Character, Blocks.piston).register()
		new ShapedOreRecipe(emptyPCB, " G ", "PNP", " Gp", 'P': Character, plasticOre, 'G': Character, paneOre, 'N': Character, goldOre, 'p': Character, Blocks.sticky_piston).register()
		new ShapedOreRecipe(emptyPCB, " P ", "GNG", " Pp", 'P': Character, plasticOre, 'G': Character, paneOre, 'N': Character, goldOre, 'p': Character, Blocks.sticky_piston).register()
		new ShapedOreRecipe(socketCPU, "P P", " P ", 'P': Character, plasticOre).register()
		new ShapedOreRecipe(ItemWrench, "  I", " T ", "TRR", 'I': Character, ironOre, 'T': Character, stoneRodOre, 'R': Character, redDyeOre).register()
		new ShapedOreRecipe(new ItemStack(stoneRod, 2), "C", "C", 'C': Character, Blocks.cobblestone).register()
		new ShapedOreRecipe(new ItemStack(stoneRod, 4), "S", "S", 'S': Character, Blocks.stone).register()
		new ShapedOreRecipe(quarterIronx8, "H H", 'H': Character, halfIronx4).register()
		new ShapedOreRecipe(halfIronx4, "I I", 'I': Character, ironOre).register()
		new ShapedOreRecipe(Items.iron_ingot, "HH", "HH", 'H': Character, halfIronx1).register()
		new ShapedOreRecipe(halfIronx1, "QQ", "QQ", 'Q': Character, quarterIronx1).register()
		new ShapedOreRecipe(emptyScoop, "  H", "QH ", "QQ ", 'H': Character, halfIronx1, 'Q': Character, quarterIronx1).register()
		new ShapedOreRecipe(emptyScoop, "  H", "QH ", "QQ ", 'H': Character, halfIronx1, 'Q': Character, quarterIronx1).register()
		new ShapedOreRecipe(goodCPU, "CgC", " S ", 'C': Character, simpleCPU, 'g': Character, goldOre, 'S': Character, socketCPU).register()
		new ShapedOreRecipe(simpleCPU, "CgC", " S ", 'C': Character, elementaryCPU, 'g': Character, goldOre, 'S': Character, socketCPU).register()
	}

	final override def registerLoot() {
		val dungeonChestGen = ChestGenHooks getInfo ChestGenHooks.DUNGEON_CHEST
		val mineshaftChestGen = ChestGenHooks getInfo ChestGenHooks.MINESHAFT_CORRIDOR
		val blacksmithChestGen = ChestGenHooks getInfo ChestGenHooks.MINESHAFT_CORRIDOR

		dungeonChestGen addItem new WeightedRandomChestContent(ItemCPU, ItemCPU.elementaryDamage, 2, 6, 3)
		dungeonChestGen addItem new WeightedRandomChestContent(ItemCPU, ItemCPU.simpleDamage, 1, 3, 2)
		dungeonChestGen addItem new WeightedRandomChestContent(ItemCPU, ItemCPU.goodDamage, 1, 1, 1)

		dungeonChestGen addItem new WeightedRandomChestContent(ItemPCB, ItemPCB.emptyPCBDamage, 1, 6, 1)
		dungeonChestGen addItem new WeightedRandomChestContent(ItemPlastic, ItemPlastic.monomerDamage, 1, 20, 3)
		dungeonChestGen addItem new WeightedRandomChestContent(ItemQuartz, ItemQuartz.plateDamage, 1, 4, 1)

		mineshaftChestGen addItem new WeightedRandomChestContent(ItemPCB, ItemPCB.fullPCBDamage, 1, 6, 1)
		mineshaftChestGen addItem new WeightedRandomChestContent(ItemPlastic, ItemPlastic.polymerDamage, 1, 10, 2)
		mineshaftChestGen addItem new WeightedRandomChestContent(ItemQuartz, ItemQuartz.shardsDamage, 3, 8, 2)

		blacksmithChestGen addItem new WeightedRandomChestContent(ItemPCB, ItemPCB.PCBLCDDamage, 1, 1, 1)
		blacksmithChestGen addItem new WeightedRandomChestContent(scoopEmpty, 0, 1, 4, 3)


		/*VillagerRegistry.instance.registerVillageTradeHandler(1, CommonProxy)
		VillagerRegistry.instance.registerVillageTradeHandler(2, CommonProxy)
		VillagerRegistry.instance.registerVillageTradeHandler(3, CommonProxy)*/
		Villager.registerProfessions()

		MapGenStructureIO.registerStructureComponent(VillageComponentElectronicShop.getComponentClass, "ElectronicShop")
		VillagerRegistry.instance registerVillageCreationHandler VillageComponentElectronicShop
	}

	override def registerModels() {}
}

private object CommonProxy {
	private final lazy val oreRegistrables = ItemWrench :: ItemPlastic :: ItemCPU :: Container :: ItemQuartz :: ItemPartialIron :: Nil


	/*override def manipulateTradesForVillager(villager: EntityVillager, recipeList: MerchantRecipeList, random: Random) =
		villager.getProfession match {
			case 1 => // Librarian
				recipeList addToListWithCheck new MerchantRecipe(new ItemStack(Items.emerald, random nextInt 10 max 3), new ItemStack(ItemPCB, 1, ItemPCB.emptyPCBDamage))
			case 2 => // Priest
				recipeList addToListWithCheck new MerchantRecipe(new ItemStack(ItemCPU, 1, ItemCPU.elementaryDamage), new ItemStack(Items.emerald, random nextInt 8 max 3), new ItemStack(ItemCPU, 1, ItemCPU.simpleDamage))
				recipeList addToListWithCheck new MerchantRecipe(new ItemStack(ItemCPU, 1, ItemCPU.simpleDamage), new ItemStack(Items.emerald, random nextInt 30 max 12), new ItemStack(ItemCPU, 1, ItemCPU.goodDamage))
			case 3 => // Blacksmith
				recipeList addToListWithCheck new MerchantRecipe(new ItemStack(ItemPCB, 1, ItemPCB.emptyPCBDamage), new ItemStack(ItemPCB, 1, ItemPCB.LCDDamage), new ItemStack(ItemPCB, 1, ItemPCB.PCBLCDDamage))
			case _ =>
		}*/
}
