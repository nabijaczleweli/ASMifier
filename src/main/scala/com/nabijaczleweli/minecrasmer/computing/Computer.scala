package com.nabijaczleweli.minecrasmer.computing

import com.nabijaczleweli.minecrasmer.util.NBTUtil.NBTReloadable
import net.minecraft.nbt.{NBTBase, NBTTagCompound, NBTTagList, NBTTagString}

import scala.collection.mutable

trait Computer extends NBTReloadable {
	final val instructions = mutable.Queue[Opcode]()
	final var curopcode: Opcode = _
	final var opcodeprocessingticks = 0

	def processorTick() {
		if(curopcode == null)
			if(instructions.length == 0)
				return
			else
				curopcode = instructions.dequeue()
		processOpcode()
	}

	def processOpcode() {
		curopcode.process(opcodeprocessingticks, this)

		opcodeprocessingticks += 1
		if(opcodeprocessingticks == curopcode.getTicks) {
			opcodeprocessingticks = 0
			curopcode = null
		}
	}

	override def readFromNBT(tag: NBTTagCompound) {
		val instructionsNbt = tag getCompoundTag "instructions"
		val size = instructionsNbt getInteger "size"
		for(i <- 0 until size) {
			val path = instructionsNbt.getTagList("loaderList", NBTBase.NBTTypes.toSeq indexOf "STRING") getStringTagAt i
			val loaderClass = Class forName path
			val method = loaderClass.getMethod("openFromNBT", classOf[NBTTagCompound])
			instructions enqueue method.invoke(null, instructionsNbt getCompoundTag "instructionList").asInstanceOf[Opcode]
		}
	}

	override def writeToNBT(tag: NBTTagCompound) {
		val instructionsNbt = new NBTTagCompound
		val instructionLoaderListNbt = new NBTTagList
		val instructionListNbt = new NBTTagList
		val instructionNbt = new NBTTagCompound
		instructionsNbt.setInteger("size", instructions.size)
		for(op <- instructions.toIterator) {
			instructionLoaderListNbt appendTag new NBTTagString(op.getOpcodeLoaderPath)
			op writeToNBT instructionNbt
			instructionListNbt appendTag instructionNbt.copy()
		}
		instructionsNbt.setTag("loaderList", instructionLoaderListNbt)
		instructionsNbt.setTag("instructionList", instructionListNbt)
		tag.setTag("instructions", instructionsNbt)
	}
}
