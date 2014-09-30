package com.nabijaczleweli.minecrasmer.computing

import scala.math._

trait MulticlockedComputer extends Computer {
	val clockSpeed: Int

	override def processorTick() =
		processorTick(1F)

	def processorTick(multiplier: Float) =
		for(clock <- 1 to floor(clockSpeed * multiplier).toInt)
			super.processorTick()
}
