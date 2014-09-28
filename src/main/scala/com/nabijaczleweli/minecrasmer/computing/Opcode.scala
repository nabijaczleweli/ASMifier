package com.nabijaczleweli.minecrasmer.computing

trait Opcode {
  def getTicks: Int

	def process(tick: Int, computer: Computer): Unit

	override def toString =
		getClass.getSimpleName
}
