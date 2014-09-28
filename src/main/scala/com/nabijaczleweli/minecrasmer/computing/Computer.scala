package com.nabijaczleweli.minecrasmer.computing

import scala.collection.mutable

trait Computer {
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
}
