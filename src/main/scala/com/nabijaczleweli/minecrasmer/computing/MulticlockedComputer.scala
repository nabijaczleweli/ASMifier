package com.nabijaczleweli.minecrasmer.computing

trait MulticlockedComputer extends Computer {
	def clockSpeed: Int
	def nativeMultiplier: Float
	def nativeProcessors: Int
	def externalProcessors: Int
	def externalMultiplier: Float

	override def processorTick() =
		for(_ <- 0 to clockSpeed) {
			if(externalMultiplier == 0)
				super.processorTick()
			else
				for(_ <- 0 to (externalProcessors * externalMultiplier).floor.toInt)
					super.processorTick()
			for(_ <- 0 to nativeProcessors)
				super.processorTick()
		}
}
