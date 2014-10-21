package com.nabijaczleweli.minecrasmer.compat.pneumaticraft

import com.nabijaczleweli.minecrasmer.compat.{Empty, ICompat, Successful}
import com.nabijaczleweli.minecrasmer.item.{ItemPCB, ItemPlastic, ItemQuartz}
import com.nabijaczleweli.minecrasmer.util.RegistrationUtils._
import cpw.mods.fml.relauncher.Side
import net.minecraft.item.ItemStack
import net.minecraftforge.oredict.{OreDictionary, ShapelessOreRecipe}
import pneumaticCraft.api.item.ItemSupplier
import pneumaticCraft.api.recipe.PressureChamberRecipe

class PneumaticCraft extends ICompat {
	override def getModIDs =
		"PneumaticCraft" :: Nil

	override def preLoad(side: Side) =
		Empty

	override def load(side: Side) = {
		OreDictionary.registerOre(ItemPlastic oreDictName ItemPlastic.plasticDamage, new ItemStack(ItemSupplier getItem "plastic", 1, OreDictionary.WILDCARD_VALUE))


		val PCBElements = new ItemStack(ItemPCB, 1, ItemPCB.fullPCBDamage)
		val emptyPCB = new ItemStack(ItemPCB, 1, ItemPCB.emptyPCBDamage)
		val PCBElementsPC = ItemSupplier getItem "printedCircuitBoard"
		val emptyPCBPC = ItemSupplier getItem "unassembledPCB"
		val quartzPlate = new ItemStack(ItemQuartz, 1, ItemQuartz.plateDamage)

		new ShapelessOreRecipe(PCBElements, PCBElementsPC).register()
		new ShapelessOreRecipe(PCBElementsPC, PCBElements).register()
		new ShapelessOreRecipe(emptyPCB, emptyPCBPC).register()
		new ShapelessOreRecipe(emptyPCBPC, emptyPCB).register()

		PressureChamberRecipe.specialRecipes add new PressureChamberOreRecipe(Array(("gemQuartz", 4)), Array(quartzPlate))
		PressureChamberRecipe.specialRecipes add new PressureChamberOreRecipe(Array("blockQuartz"), Array(quartzPlate))
		PressureChamberRecipe.specialRecipes add new PressureChamberOreRecipe(Array((ItemQuartz oreDictName ItemQuartz.shardsDamage, 4)), Array(quartzPlate))


		Successful
	}
}
