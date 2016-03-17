package com.nabijaczleweli.minecrasmer.block

import com.nabijaczleweli.minecrasmer.entity.tile.TileEntityOverclocker
import net.minecraft.block.ITileEntityProvider
import net.minecraft.world.World

object BlockAccessoryOverclocker extends AccessoryGeneric("overclocker") with ITileEntityProvider {
	override def createNewTileEntity(world: World, meta: Int) =
		new TileEntityOverclocker
}
