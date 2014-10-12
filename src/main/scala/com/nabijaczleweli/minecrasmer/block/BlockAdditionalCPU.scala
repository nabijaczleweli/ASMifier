package com.nabijaczleweli.minecrasmer.block

import com.nabijaczleweli.minecrasmer.MineCrASMer
import com.nabijaczleweli.minecrasmer.entity.tile.TileEntityAdditionalCPU
import com.nabijaczleweli.minecrasmer.reference.Reference
import com.nabijaczleweli.minecrasmer.render.gui.GUIAdditionalCPU
import net.minecraft.block.ITileEntityProvider
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.world.World

object BlockAdditionalCPU extends AccessoryGeneric("processor") with ITileEntityProvider {
	setBlockTextureName(Reference.NAMESPACED_PREFIX + "processor")

	override def onBlockActivated(world: World, x: Int, y: Int, z: Int, player: EntityPlayer, side: Int, clickX: Float, clickY: Float, clickZ: Float) = {
		player.openGui(MineCrASMer, GUIAdditionalCPU.id, world, x, y, z)
		true
	}

	override def createNewTileEntity(world: World, meta: Int) =
		new TileEntityAdditionalCPU
}
