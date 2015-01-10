package com.nabijaczleweli.minecrasmer.compat

import com.nabijaczleweli.minecrasmer.reference.Reference
import net.minecraft.nbt.NBTTagCompound
import net.minecraftforge.fluids.FluidRegistry
import net.minecraftforge.fml.common.event.FMLInterModComms
import net.minecraftforge.fml.relauncher.Side

class Vanilla extends ICompat {
	override def getModIDs =
		Nil

	override def preLoad(side: Side) =
		Empty

	override def load(side: Side) = {
		val nbt = new NBTTagCompound

		nbt.setString("fluid_name", FluidRegistry.LAVA.getName)
		nbt.setInteger("color", 0xec0808)
		FMLInterModComms.sendMessage(Reference.MOD_ID, "register-scoop", nbt.copy.asInstanceOf[NBTTagCompound])

		nbt.setString("fluid_name", FluidRegistry.WATER.getName)
		nbt.setInteger("color", 0x344df4)
		FMLInterModComms.sendMessage(Reference.MOD_ID, "register-scoop", nbt.copy.asInstanceOf[NBTTagCompound])

		Successful
	}
}
