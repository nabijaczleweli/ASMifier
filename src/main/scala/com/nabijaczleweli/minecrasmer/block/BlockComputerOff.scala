package com.nabijaczleweli.minecrasmer.block

import com.nabijaczleweli.minecrasmer.creativetab.CreativeTabMineCrASMer
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.world.World

object BlockComputerOff extends ComputerGeneric("off") {
	setCreativeTab(CreativeTabMineCrASMer)

	override def onBlockActivated(world: World, x: Int, y: Int, z: Int, player: EntityPlayer, side: Int, clickX: Float, clickY: Float, clickZ: Float) = {
		world.setBlock(x, y, z, BlockComputerOn, world.getBlockMetadata(x, y, z), 2 | 4)
		false
	}
}
