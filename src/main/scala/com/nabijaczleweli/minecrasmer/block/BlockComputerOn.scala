package com.nabijaczleweli.minecrasmer.block

import com.nabijaczleweli.minecrasmer.MineCrASMer
import com.nabijaczleweli.minecrasmer.entity.tile.TileEntityComputer
import com.nabijaczleweli.minecrasmer.render.gui.GUIComputer
import net.minecraft.block.ITileEntityProvider
import net.minecraft.block.state.IBlockState
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.util.{BlockPos, EnumFacing}
import net.minecraft.world.World

object BlockComputerOn extends ComputerGeneric("on") with ITileEntityProvider {
	/*@SideOnly(Side.CLIENT)
	override def registerBlockIcons(ir: IIconRegister) {
		super.registerBlockIcons(ir)
		icons(computerTopIndex) = ir registerIcon Reference.NAMESPACED_PREFIX + "computer_top_clear"
	}*/

	override def onBlockActivated(worldIn: World, pos: BlockPos, state: IBlockState, playerIn: EntityPlayer, side: EnumFacing, hitX: Float, hitY: Float, hitZ: Float) = {
		playerIn.openGui(MineCrASMer, GUIComputer.id, worldIn, pos.getX, pos.getY, pos.getZ)
		true
	}

	override def createNewTileEntity(world: World, meta: Int) =
		new TileEntityComputer
}
