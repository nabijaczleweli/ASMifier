package com.nabijaczleweli.minecrasmer.compat

import com.nabijaczleweli.minecrasmer.item.ItemPlastic
import cpw.mods.fml.relauncher.Side
import net.minecraft.item.ItemStack
import net.minecraftforge.oredict.OreDictionary
import pneumaticCraft.api.item.ItemSupplier

class PneumaticCraft extends ICompat {
	override def getModIDs =
		"PneumaticCraft" :: Nil

	override def preLoad(side: Side) =
		Empty

	override def load(side: Side) = {
		OreDictionary.registerOre(ItemPlastic oreDictName ItemPlastic.plasticDamage, new ItemStack(ItemSupplier getItem "plastic", 1, OreDictionary.WILDCARD_VALUE))

		Successful
	}
}
