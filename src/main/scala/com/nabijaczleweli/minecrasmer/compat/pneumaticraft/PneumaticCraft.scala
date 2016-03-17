package com.nabijaczleweli.minecrasmer.compat.pneumaticraft

import com.nabijaczleweli.minecrasmer.compat.{Empty, ICompat}
import net.minecraftforge.fml.relauncher.Side

class PneumaticCraft extends ICompat {
	override def getModIDs =
		"PneumaticCraft" :: Nil

	override def preLoad(side: Side) =
		Empty

	override def load(side: Side) = {/*
		OreDictionary.registerOre(ItemPlastic oreDictName ItemPlastic.plasticDamage, new ItemStack(ItemSupplier getItem "plastic", 1, OreDictionary.WILDCARD_VALUE))


		val quartzPlate = new ItemStack(ItemQuartz, 1, ItemQuartz.plateDamage)

		PressureChamberRecipe.specialRecipes add new PressureChamberOreRecipe(Array(("gemQuartz", 4)), Array(quartzPlate))
		PressureChamberRecipe.specialRecipes add new PressureChamberOreRecipe(Array("blockQuartz"), Array(quartzPlate))
		PressureChamberRecipe.specialRecipes add new PressureChamberOreRecipe(Array((ItemQuartz oreDictName ItemQuartz.shardsDamage, 4)), Array(quartzPlate))


		Successful*/
		Empty
	}
}
