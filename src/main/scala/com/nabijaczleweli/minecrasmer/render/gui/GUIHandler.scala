package com.nabijaczleweli.minecrasmer.render.gui

import cpw.mods.fml.common.network.IGuiHandler
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.world.World

object GUIHandler extends IGuiHandler {
	override def getClientGuiElement(ID: Int, player: EntityPlayer, world: World, x: Int, y: Int, z: Int) =
		ID match {
			case GUIComputer.id =>
				GUIComputer
			case _ =>
				null
		}

	override def getServerGuiElement(ID: Int, player: EntityPlayer, world: World, x: Int, y: Int, z: Int) =
		ID match {
			case _ =>
				null
		}
}
