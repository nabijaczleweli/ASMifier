package com.nabijaczleweli.minecrasmer.computing

trait MulticlockedComputer extends Computer {
	def clockSpeed: Int

	def CPUs: Seq[(Int, Float)]

	override def processorTick() =
		for(_ <- 0 to clockSpeed; (procs, mul) <- CPUs; _ <- 0 until (procs * mul).floor.toInt)
			super.processorTick()
}
