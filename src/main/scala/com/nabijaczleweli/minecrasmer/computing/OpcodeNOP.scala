package com.nabijaczleweli.minecrasmer.computing

import com.nabijaczleweli.minecrasmer.entity.tile.TileEntityComputer

object OpcodeNOP extends Opcode {
	override def getTicks =
		1

	override def process(tick: Int, computer: TileEntityComputer) =
		()
}
