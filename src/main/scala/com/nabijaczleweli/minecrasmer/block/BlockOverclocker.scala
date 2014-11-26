package com.nabijaczleweli.minecrasmer.block

import com.nabijaczleweli.minecrasmer.entity.tile.TileEntityOverclocker
import net.minecraft.block.ITileEntityProvider
import net.minecraft.world.World

object BlockOverclocker extends AccessoryGeneric("overclocker") with ITileEntityProvider {
	//setBlockTextureName(Reference.NAMESPACED_PREFIX + "overclocker")

	override def createNewTileEntity(world: World, meta: Int) =
		new TileEntityOverclocker
}
