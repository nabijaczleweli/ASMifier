package com.nabijaczleweli.minecrasmer.computing

import com.nabijaczleweli.minecrasmer.entity.tile.TileEntityComputer

trait Opcode {
  def getTicks: Int

	def process(tick: Int, computer: TileEntityComputer): Unit

	override def toString =
		getClass.getSimpleName
}
