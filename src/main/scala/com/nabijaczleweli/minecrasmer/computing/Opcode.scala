package com.nabijaczleweli.minecrasmer.computing

import com.nabijaczleweli.minecrasmer.util.NBTUtil.NBTReloadable
import net.minecraft.nbt.NBTTagCompound

trait Opcode extends NBTReloadable {
  def getTicks: Int

	def process(tick: Int, computer: Computer): Unit

	override def toString =
		getClass.getSimpleName

	override def readFromNBT(tag: NBTTagCompound) =
		()

	override def writeToNBT(tag: NBTTagCompound) =
		()

	def getOpcodeLoaderPath: String
}

trait OpcodeLoader[+T <: Opcode] {
	def openFromNBT(tag: NBTTagCompound): T
}
