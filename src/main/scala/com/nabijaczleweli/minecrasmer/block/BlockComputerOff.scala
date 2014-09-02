package com.nabijaczleweli.minecrasmer.block

import com.nabijaczleweli.minecrasmer.creativetab.CreativeTabMineCrASMer
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.world.World

object BlockComputerOff extends ComputerGeneric("off") {
	final val onButtonSwitchPixels = (List[Int](13, 13, 11, 10, 11, 12, 13, 14, 15, 15),
					                          List[Int](0 , 1 , 1 , 2 , 3 , 4 , 4 , 4 , 3 , 1 ))

	setCreativeTab(CreativeTabMineCrASMer)

	override def onBlockActivated(world: World, x: Int, y: Int, z: Int, player: EntityPlayer, side: Int, clickX: Float, clickY: Float, clickZ: Float) =
		side match {
			case 1 =>
				if(onButtonSwitchPixels._1.contains(Math.floor(clickX * 16)) && onButtonSwitchPixels._2.contains(Math.floor(clickZ * 16))) {
					if(!world.isRemote)
						world.setBlock(x, y, z, BlockComputerOn, world.getBlockMetadata(x, y, z), 2 | 4)
					BlockComputerOn.onBlockActivated(world, x, y, z, player, side, clickX, clickY, clickZ)
					true
				} else
					false
			case _ =>
				false
		}
}
