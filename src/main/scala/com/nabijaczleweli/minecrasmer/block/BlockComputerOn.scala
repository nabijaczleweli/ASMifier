package com.nabijaczleweli.minecrasmer.block

import com.nabijaczleweli.minecrasmer.MineCrASMer
import com.nabijaczleweli.minecrasmer.entity.tile.TileEntityComputer
import com.nabijaczleweli.minecrasmer.gui.GUIComputer
import com.nabijaczleweli.minecrasmer.reference.Reference
import cpw.mods.fml.relauncher.{SideOnly, Side}
import net.minecraft.block.ITileEntityProvider
import net.minecraft.client.renderer.texture.IIconRegister
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.world.World

object BlockComputerOn extends ComputerGeneric("on") with ITileEntityProvider {
	@SideOnly(Side.CLIENT)
	override def registerBlockIcons(ir: IIconRegister) {
		super.registerBlockIcons(ir)
		computerTop = ir registerIcon Reference.NAMESPACED_PREFIX + "computer_top_clear"
	}

	override def onBlockActivated(world: World, x: Int, y: Int, z: Int, player: EntityPlayer, side: Int, clickX: Float, clickY: Float, clickZ: Float) = {
		player.openGui(MineCrASMer, GUIComputer.id, world, x, y, z)
		true
	}

	override def createNewTileEntity(world: World, meta: Int) =
		new TileEntityComputer
}
