package com.nabijaczleweli.minecrasmer.compat

import com.nabijaczleweli.minecrasmer.item.ItemPlastic
import net.minecraft.item.{Item, ItemStack}
import net.minecraftforge.fml.relauncher.Side
import net.minecraftforge.oredict.OreDictionary

class MFR extends ICompat {
	override def getModIDs =
		"MineFactoryReloaded" :: Nil

	override def preLoad(side: Side) =
		Empty

	override def load(side: Side) = {
		var worked = true

		OreDictionary.registerOre("dustPlastic", new ItemStack(ItemPlastic, 1, ItemPlastic.plasticDamage))

		try
			OreDictionary.registerOre(ItemPlastic oreDictName ItemPlastic.plasticDamage, (Class forName "powercrystals.minefactoryreloaded.MineFactoryReloadedCore" getField "rawPlasticItem" get null).asInstanceOf[Item])
		catch {
			case _: Throwable =>
				worked = false
		}

		if(worked)
			Successful
		else
			Failed
	}
}
