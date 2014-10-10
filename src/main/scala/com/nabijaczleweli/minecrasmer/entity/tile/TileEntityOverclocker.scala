package com.nabijaczleweli.minecrasmer.entity.tile

import com.nabijaczleweli.minecrasmer.computing.ComputerAccessory
import com.nabijaczleweli.minecrasmer.reference.Reference
import com.nabijaczleweli.minecrasmer.util.IConfigurable
import net.minecraft.tileentity.TileEntity
import net.minecraftforge.common.config.Configuration

class TileEntityOverclocker extends TileEntity with ComputerAccessory {
	def multiplier =
		TileEntityOverclocker.multiplier
}

object TileEntityOverclocker extends IConfigurable {
	final var multiplier = 1.5f

	override def load(config: Configuration) {
		multiplier = config.getFloat("TEOverclockerMultiplier", Reference.CONFIG_COMPUTE_CATEGORY, multiplier, 0, 10, "Multiplier for each of TileEntityOverclocker instances")
	}
}
