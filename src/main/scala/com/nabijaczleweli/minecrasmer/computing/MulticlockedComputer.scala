package com.nabijaczleweli.minecrasmer.computing

import scala.math._

trait MulticlockedComputer extends Computer {
	def clockSpeed: Int
	def multiplier: Float
	def processors: Int

	override def processorTick() =
		for(proc <- 0 until processors; clock <- 1 to floor(clockSpeed * multiplier).toInt)
			super.processorTick()
}
