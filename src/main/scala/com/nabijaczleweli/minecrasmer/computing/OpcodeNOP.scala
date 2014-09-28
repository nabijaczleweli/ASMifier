package com.nabijaczleweli.minecrasmer.computing

object OpcodeNOP extends Opcode {
	override def getTicks =
		1

	override def process(tick: Int, computer: Computer) =
		()
}
