package com.nabijaczleweli.minecrasmer.computing

import com.nabijaczleweli.minecrasmer.reference.Container
import com.nabijaczleweli.minecrasmer.util.NBTUtil.NBTReloadable
import net.minecraft.nbt.{NBTTagCompound, NBTTagList, NBTTagString}
import net.minecraftforge.common.util.Constants.NBT

import scala.collection.mutable
import scala.reflect.runtime.ReflectionUtils

trait Computer extends NBTReloadable {
	final val instructions          = mutable.Queue[Opcode]()
	final var curopcode: Opcode     = _
	final var opcodeprocessingticks = 0

	def processorTick() {
		if(curopcode == null)
			if(instructions.isEmpty)
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
			try {
				val path = instructionsNbt.getTagList("loaderList", NBT.TAG_STRING) getStringTagAt i
				val loaderModule = ReflectionUtils.staticSingletonInstance(getClass.getClassLoader, path)
				val openOpcodeMethod = loaderModule.getClass.getMethod("openFromNBT", classOf[NBTTagCompound])
				val savedInstruction = instructionsNbt.getTagList("instructionList", NBT.TAG_COMPOUND) getCompoundTagAt i
				instructions enqueue openOpcodeMethod.invoke(loaderModule, savedInstruction).asInstanceOf[Opcode]
			} catch {
				case t: Throwable =>
					Container.log warn s"Loading Opcode #$i/${size - 1} failed! Cause: $t. Cause\'s cause: ${t.getCause}. Skipping..."
			}
		}
	}

	override def writeToNBT(tag: NBTTagCompound) {
		val instructionsNbt = new NBTTagCompound
		val instructionLoaderListNbt = new NBTTagList
		val instructionListNbt = new NBTTagList
		val instructionNbt = new NBTTagCompound
		instructionsNbt.setInteger("size", instructions.size)
		for(op <- instructions.toIterator) {
			instructionLoaderListNbt appendTag new NBTTagString(op.opcodeLoaderType.getName)
			op writeToNBT instructionNbt
			instructionListNbt appendTag instructionNbt.copy()
		}
		instructionsNbt.setTag("loaderList", instructionLoaderListNbt)
		instructionsNbt.setTag("instructionList", instructionListNbt)
		tag.setTag("instructions", instructionsNbt)
	}
}
