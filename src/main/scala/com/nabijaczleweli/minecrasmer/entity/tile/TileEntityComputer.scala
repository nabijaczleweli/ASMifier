package com.nabijaczleweli.minecrasmer.entity.tile

import com.nabijaczleweli.minecrasmer.computing.MulticlockedComputer
import com.nabijaczleweli.minecrasmer.reference.Reference
import com.nabijaczleweli.minecrasmer.util.NBTUtil._
import com.nabijaczleweli.minecrasmer.util.{IConfigurable, SimpleDataProcessingTileEntity}
import net.minecraft.nbt.NBTTagCompound
import net.minecraftforge.common.config.Configuration

import scala.util.Random

class TileEntityComputer extends SimpleDataProcessingTileEntity with MulticlockedComputer {
	var lines: Array[String] = Array("TEXT0", "TEXT1", "TEXT2", "", "", "", new Random().nextInt().toString)

	override val clockSpeed = TileEntityComputer.clocksPerSec

	override def updateEntity() {
		super.updateEntity()
		processorTick()
		markDirty()
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
