package com.nabijaczleweli.minecrasmer.block

import com.nabijaczleweli.minecrasmer.creativetab.CreativeTabMineCrASMer
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.util.ChatComponentText
import net.minecraft.world.World

object BlockComputerOff extends ComputerGeneric("off") {
	final val onButtonSwitchPixels = (13 :: 13 :: 11 :: 10 :: 11 :: 12 :: 13 :: 14 :: 15 :: 15 :: Nil,
					                          0  ::  1 ::  1 ::  2 ::  3 ::  4 ::  4 ::  4 ::  3 ::  1 :: Nil)

	setCreativeTab(CreativeTabMineCrASMer)

	override def onBlockActivated(world: World, x: Int, y: Int, z: Int, player: EntityPlayer, side: Int, clickX: Float, clickY: Float, clickZ: Float) =
		side match {
			case 1 =>
				if(onButtonSwitchPixels._1.contains(Math.floor(clickX * 16)) && onButtonSwitchPixels._2.contains(Math.floor(clickZ * 16))) {
					if(!world.isRemote) {
						player addChatMessage new ChatComponentText("Booting MC-DOS...")
						world.setBlock(x, y, z, BlockComputerOn, world.getBlockMetadata(x, y, z), 2 | 4)
					}
					true
				} else
					false
			case _ =>
				false
		}
}
