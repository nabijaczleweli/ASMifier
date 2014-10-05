package com.nabijaczleweli.minecrasmer.resource

import cpw.mods.fml.relauncher.{Side, SideOnly}
import net.minecraft.client.resources.IResourceManager

trait ISimpleReloadable {
	def reload(): Unit
}

@SideOnly(Side.CLIENT)
trait IManagerReloadable {
	def reload(manager: IResourceManager)
}

@SideOnly(Side.CLIENT)
trait IDoubleReloadable extends ISimpleReloadable with IManagerReloadable {
	override def reload() =
		reload(null)
}
