package com.nabijaczleweli.minecrasmer.block

import com.nabijaczleweli.minecrasmer.entity.tile.TileEntityAdditionalCPU
import com.nabijaczleweli.minecrasmer.reference.Reference
import net.minecraft.block.ITileEntityProvider
import net.minecraft.world.World

object BlockAdditionalCPU extends AccessoryGeneric("processor") with ITileEntityProvider {
	setBlockTextureName(Reference.NAMESPACED_PREFIX + "processor")

	override def createNewTileEntity(world: World, meta: Int) =
		new TileEntityAdditionalCPU
}
