package com.nabijaczleweli.minecrasmer.render.gui

import com.nabijaczleweli.minecrasmer.container.ContainerAdditionalCPU
import com.nabijaczleweli.minecrasmer.entity.tile.TileEntityAdditionalCPU
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.util.BlockPos
import net.minecraft.world.World
import net.minecraftforge.fml.common.network.IGuiHandler

object GUIHandler extends IGuiHandler {
	override def getClientGuiElement(ID: Int, player: EntityPlayer, world: World, x: Int, y: Int, z: Int) =
		ID match {
			case GUIComputer.id =>
				GUIComputer.init(world, x, y, z)
				GUIComputer
			case GUIAdditionalCPU.id =>
				new GUIAdditionalCPU(getContainer(ID, player, world, x, y, z))
			case _ =>
				null
		}

	override def getServerGuiElement(ID: Int, player: EntityPlayer, world: World, x: Int, y: Int, z: Int) =
		ID match {
			case GUIAdditionalCPU.id =>
				getContainer(ID, player, world, x, y, z)
			case _ =>
				null
		}

	def getContainer(ID: Int, player: EntityPlayer, world: World, x: Int, y: Int, z: Int) =
		ID match {
			case GUIAdditionalCPU.id =>
				new ContainerAdditionalCPU(player.inventory, world.getTileEntity(new BlockPos(x, y, z)).asInstanceOf[TileEntityAdditionalCPU])
			case _ =>
				null
		}
}
