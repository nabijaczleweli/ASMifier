package com.nabijaczleweli.minecrasmer.block

import com.nabijaczleweli.minecrasmer.creativetab.CreativeTabMineCrASMer
import net.minecraft.block.state.IBlockState
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.util.{BlockPos, ChatComponentText, EnumFacing}
import net.minecraft.world.World

object BlockComputerOff extends ComputerGeneric("off") {
	final val onButtonSwitchPixels = (13 :: 13 :: 11 :: 10 :: 11 :: 12 :: 13 :: 14 :: 15 :: 15 :: Nil,
					                          0  ::  1 ::  1 ::  2 ::  3 ::  4 ::  4 ::  4 ::  3 ::  1 :: Nil)

	setCreativeTab(CreativeTabMineCrASMer)

	override def onBlockActivated(worldIn: World, pos: BlockPos, state: IBlockState, playerIn: EntityPlayer, side: EnumFacing, hitX: Float, hitY: Float, hitZ: Float) =
		side match {
			case 1 =>
				if(onButtonSwitchPixels._1.contains(Math.floor(hitX * 16)) && onButtonSwitchPixels._2.contains(Math.floor(hitZ * 16))) {
					if(!worldIn.isRemote) {
						playerIn addChatMessage new ChatComponentText("Booting MC-DOS...")
						worldIn.setBlockState(pos, BlockComputerOn.getActualState(worldIn getBlockState pos, worldIn, pos), 2 | 4)
					}
					true
				} else
					false
			case _ =>
				false
		}
}
