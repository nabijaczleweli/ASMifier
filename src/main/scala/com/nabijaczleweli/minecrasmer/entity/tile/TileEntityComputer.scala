package com.nabijaczleweli.minecrasmer.entity.tile

import com.nabijaczleweli.minecrasmer.computing.Opcode
import com.nabijaczleweli.minecrasmer.reference.Reference
import com.nabijaczleweli.minecrasmer.util.IConfigurable
import net.minecraft.tileentity.TileEntity
import net.minecraftforge.common.config.Configuration

import scala.collection.mutable

class TileEntityComputer extends TileEntity {
	val lines: Array[String] = Array("TEXT0", "TEXT1", "TEXT2", "", "", "", "")
	val instructions = mutable.Queue[Opcode]()

	var curopcode: Opcode = _
	var opcodeprocessingticks = 0

	var totalticks = 0
	override def updateEntity() {
		super.updateEntity()
		for(clock <- 1 to TileEntityComputer.clocksPerSec) {
			if(curopcode == null)
				if(instructions.length == 0)
					return
				else
					curopcode = instructions.dequeue()
			processOpcode()
		}
	}

	def processOpcode() {
		curopcode.process(opcodeprocessingticks, this)

		opcodeprocessingticks += 1
		if(opcodeprocessingticks == curopcode.getTicks) {
			opcodeprocessingticks = 0
			curopcode = null
			return
		}
	}
}

object TileEntityComputer extends IConfigurable {
	final var clocksPerSec = 1

	override def load(config: Configuration) {
		clocksPerSec = config.getInt("TEComputerClocksPerSecond", Reference.CONFIG_COMPUTE_CATEGORY, clocksPerSec, 0, Int.MaxValue, "Processor ticks per one update tick")
	}
}
