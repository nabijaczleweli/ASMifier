package com.nabijaczleweli.minecrasmer.entity.tile

import com.nabijaczleweli.minecrasmer.computing.ComputerAccessory
import com.nabijaczleweli.minecrasmer.reference.Reference
import com.nabijaczleweli.minecrasmer.util.{IConfigurable, SimpleDataProcessingTileEntity}
import net.minecraftforge.common.config.Configuration

class TileEntityAdditionalCPU extends SimpleDataProcessingTileEntity with ComputerAccessory {
	def processors =
		1
}

object TileEntityAdditionalCPU extends IConfigurable {
	final var processors = 1

	override def load(config: Configuration) {
		processors = config.getInt("TEAdditionalCPUProcessors", Reference.CONFIG_COMPUTE_CATEGORY, processors, 0, Int.MaxValue, "Amount of processors each TileEntityAdditionalCPU provides")
	}
}
