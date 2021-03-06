package com.nabijaczleweli.minecrasmer.computing

import net.minecraft.nbt.NBTTagCompound

object OpcodeNOP extends Opcode {
	override def getTicks =
		1

	override def process(tick: Int, computer: Computer) =
		()

	override def opcodeLoaderType =
		NOPLoader.getClass.asInstanceOf[Class[NOPLoader.type]]
}

object NOPLoader extends OpcodeLoader[OpcodeNOP.type] {
	override def openFromNBT(tag: NBTTagCompound) =
		OpcodeNOP
}
