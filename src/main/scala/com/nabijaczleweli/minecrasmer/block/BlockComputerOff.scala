package com.nabijaczleweli.minecrasmer.block

import com.nabijaczleweli.minecrasmer.creativetab.CreativeTabMineCrASMer
import com.nabijaczleweli.minecrasmer.reference.Reference
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.world.World

object BlockComputerOff extends ComputerGeneric {
	setCreativeTab(CreativeTabMineCrASMer)
	setBlockName(Reference.NAMESPACED_PREFIX + "computeroff")

	override def onBlockActivated(world: World, x: Int, y: Int, z: Int, player: EntityPlayer, side: Int, clickX: Float, clickY: Float, clickZ: Float) = {
		world.setBlock(x, y, z, BlockComputerOn, world.getBlockMetadata(x, y, z), 2 | 4)
		false
	}
}
