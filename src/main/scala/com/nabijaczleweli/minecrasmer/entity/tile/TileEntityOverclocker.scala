package com.nabijaczleweli.minecrasmer.entity.tile

import com.nabijaczleweli.minecrasmer.computing.ComputerAccessory
import com.nabijaczleweli.minecrasmer.reference.Reference
import com.nabijaczleweli.minecrasmer.util.IConfigurable
import net.minecraft.server.gui.IUpdatePlayerListBox
import net.minecraft.tileentity.TileEntity
import net.minecraftforge.common.config.Configuration

class TileEntityOverclocker extends TileEntity with ComputerAccessory with IUpdatePlayerListBox {
	def multiplier =
		TileEntityOverclocker.multiplier

	override def update() =
		()
}

object TileEntityOverclocker extends IConfigurable {
	final var multiplier = 1.5f

	override def load(config: Configuration) {
		multiplier = config.getFloat("TEOverclockerMultiplier", Reference.CONFIG_COMPUTE_CATEGORY, multiplier, 0, 10, "Multiplier for each of TileEntityOverclocker instances")
	}
}
