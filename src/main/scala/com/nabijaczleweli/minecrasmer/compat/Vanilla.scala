package com.nabijaczleweli.minecrasmer.compat

import com.nabijaczleweli.minecrasmer.item.ItemScoop
import com.nabijaczleweli.minecrasmer.reference.Reference
import com.nabijaczleweli.minecrasmer.util.RegistrationUtils._
import net.minecraft.init.Blocks
import net.minecraft.item.{Item, ItemStack}
import net.minecraft.nbt.NBTTagCompound
import net.minecraftforge.fluids.FluidRegistry
import net.minecraftforge.fml.common.event.FMLInterModComms
import net.minecraftforge.fml.relauncher.Side

class Vanilla extends ICompat {
	private lazy val lavaScoop  = new ItemScoop(Blocks.lava, FluidRegistry.LAVA, 0xec0808)
	private lazy val waterScoop = new ItemScoop(Blocks.water, FluidRegistry.WATER, 0x344df4)

	override def getModIDs =
		Nil

	override def preLoad(side: Side) = {
		(lavaScoop: Item).register()
		(waterScoop: Item).register()

		Successful
	}

	override def load(side: Side) = {
		val nbt = new NBTTagCompound
		val itemNBT = new NBTTagCompound

		nbt.setTag("itemstack", new ItemStack(lavaScoop) writeToNBT itemNBT)
		FMLInterModComms.sendMessage(Reference.MOD_ID, "register-scoop", nbt.copy().asInstanceOf[NBTTagCompound])

		nbt.setTag("itemstack", new ItemStack(waterScoop) writeToNBT itemNBT)
		FMLInterModComms.sendMessage(Reference.MOD_ID, "register-scoop", nbt.copy().asInstanceOf[NBTTagCompound])

		Successful
	}
}
