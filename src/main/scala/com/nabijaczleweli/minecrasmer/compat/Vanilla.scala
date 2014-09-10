package com.nabijaczleweli.minecrasmer.compat

import com.nabijaczleweli.minecrasmer.item.ItemScoop
import com.nabijaczleweli.minecrasmer.reference.Reference
import cpw.mods.fml.common.event.FMLInterModComms
import cpw.mods.fml.common.registry.GameRegistry
import net.minecraft.init.Blocks
import net.minecraft.item.{ItemStack, Item}
import net.minecraft.nbt.NBTTagCompound
import net.minecraftforge.fluids.FluidRegistry

class Vanilla extends ICompat {
	private lazy val lavaScoop = new ItemScoop(Blocks.lava, FluidRegistry.LAVA, 0xec0808)
	private lazy val waterScoop = new ItemScoop(Blocks.water, FluidRegistry.WATER, 0x344df4)

	override def getModIDs =
		Nil

	override def preLoad = {
		@inline
		def defaultRegisterItem(it: Item) =
			GameRegistry.registerItem(it, it.getUnlocalizedName.substring(it.getUnlocalizedName.indexOf(':') + 1))

		defaultRegisterItem(lavaScoop)
		defaultRegisterItem(waterScoop)
		true
	}

	override def load = {
		val nbt = new NBTTagCompound
		val itemNBT = new NBTTagCompound

		nbt.setTag("itemstack", new ItemStack(lavaScoop) writeToNBT itemNBT)
		FMLInterModComms.sendMessage(Reference.MOD_ID, "register-scoop", nbt.copy().asInstanceOf[NBTTagCompound])

		nbt.setTag("itemstack", new ItemStack(waterScoop) writeToNBT itemNBT)
		FMLInterModComms.sendMessage(Reference.MOD_ID, "register-scoop", nbt.copy().asInstanceOf[NBTTagCompound])
		true
	}
}
