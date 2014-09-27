package com.nabijaczleweli.minecrasmer.entity.tile

import com.nabijaczleweli.minecrasmer.computing.Opcode
import com.nabijaczleweli.minecrasmer.reference.Reference
import com.nabijaczleweli.minecrasmer.util.NBTUtil._
import com.nabijaczleweli.minecrasmer.util.{IConfigurable, SimpleDataProcessingTileEntity}
import net.minecraft.nbt.NBTTagCompound
import net.minecraftforge.common.config.Configuration

import scala.collection.mutable
import scala.util.Random

class TileEntityComputer extends SimpleDataProcessingTileEntity {
	var lines: Array[String] = Array("TEXT0", "TEXT1", "TEXT2", "", "", "", new Random().nextInt().toString)
	val instructions = mutable.Queue[Opcode]()

	var curopcode: Opcode = _
	var opcodeprocessingticks = 0

	override def updateEntity() {
		super.updateEntity()
		for(clock <- 1 to TileEntityComputer.clocksPerSec) {
			if(curopcode == null)
				if(instructions.length == 0)
					return
				else
					curopcode = instructions.dequeue()
			processOpcode()
			markDirty()
		}
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
		super.readFromNBT(tag)

		lines = tag readStringArray "lines"
	}

	override def writeToNBT(tag: NBTTagCompound) {
		super.writeToNBT(tag)

		lines.writeToNBT(tag, "lines")
	}
}

object TileEntityComputer extends IConfigurable {
	final var clocksPerSec = 1

	override def load(config: Configuration) {
		clocksPerSec = config.getInt("TEComputerClocksPerSecond", Reference.CONFIG_COMPUTE_CATEGORY, clocksPerSec, 0, Int.MaxValue, "Processor ticks per one update tick")
	}
}
