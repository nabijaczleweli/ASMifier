package com.nabijaczleweli.minecrasmer.compat.pneumaticraft

import com.nabijaczleweli.minecrasmer.compat.{Successful, Empty, ICompat}
import com.nabijaczleweli.minecrasmer.item.{ItemPlastic, ItemQuartz}
import net.minecraft.item.ItemStack
import net.minecraftforge.fml.relauncher.Side
import net.minecraftforge.oredict.OreDictionary
import pneumaticCraft.api.PneumaticRegistry
import pneumaticCraft.api.item.ItemSupplier

class PneumaticCraft extends ICompat {
	override def getModIDs =
		"PneumaticCraft" :: Nil

	override def preLoad(side: Side) =
		Empty

	override def load(side: Side) = {
		OreDictionary.registerOre(ItemPlastic oreDictName ItemPlastic.plasticDamage, new ItemStack(ItemSupplier getItem "plastic", 1, OreDictionary.WILDCARD_VALUE))


		val quartzPlate = new ItemStack(ItemQuartz, 1, ItemQuartz.plateDamage)

		PneumaticRegistry.getInstance.getRecipeRegistry registerPressureChamberRecipe new PressureChamberOreRecipe(Array(("gemQuartz", 4)), Array(quartzPlate))
		PneumaticRegistry.getInstance.getRecipeRegistry registerPressureChamberRecipe new PressureChamberOreRecipe(Array("blockQuartz"), Array(quartzPlate))
		PneumaticRegistry.getInstance.getRecipeRegistry registerPressureChamberRecipe new PressureChamberOreRecipe(Array((ItemQuartz oreDictName ItemQuartz.shardsDamage, 4)), Array(quartzPlate))


		Successful
	}
}
