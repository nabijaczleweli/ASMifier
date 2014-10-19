package com.nabijaczleweli.minecrasmer.compat

import com.nabijaczleweli.minecrasmer.item.{ItemPCB, ItemPlastic}
import com.nabijaczleweli.minecrasmer.reference.Container
import com.nabijaczleweli.minecrasmer.util.RegistrationUtils._
import cpw.mods.fml.relauncher.Side
import net.minecraft.item.ItemStack
import net.minecraftforge.oredict.{OreDictionary, ShapelessOreRecipe}
import pneumaticCraft.api.item.ItemSupplier
import pneumaticCraft.api.recipe.PressureChamberRecipe

import scala.collection.JavaConversions._

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
		val quartzPlate = new ItemStack(Container.quartzPlate)

		new ShapelessOreRecipe(PCBElements, PCBElementsPC).register()
		new ShapelessOreRecipe(PCBElementsPC, PCBElements).register()
		new ShapelessOreRecipe(emptyPCB, emptyPCBPC).register()
		new ShapelessOreRecipe(emptyPCBPC, emptyPCB).register()

		for(quartzGem <- OreDictionary getOres "gemQuartz") {
			val four = quartzGem.copy
			four.stackSize = 4
			PressureChamberRecipe.chamberRecipes add new PressureChamberRecipe(Array(four), 2.5F, Array(quartzPlate), false)
		}
		for(quartzBlock <- OreDictionary getOres "blockQuartz")
			PressureChamberRecipe.chamberRecipes add new PressureChamberRecipe(Array(quartzBlock), 2.5F, Array(quartzPlate), false)


		Successful
	}
}
