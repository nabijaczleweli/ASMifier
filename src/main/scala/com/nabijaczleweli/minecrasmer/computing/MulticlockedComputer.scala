package com.nabijaczleweli.minecrasmer.computing

trait MulticlockedComputer extends Computer {
	val clockSpeed: Int

	override def processorTick() =
		for(clock <- 1 to clockSpeed)
			super.processorTick()
}
