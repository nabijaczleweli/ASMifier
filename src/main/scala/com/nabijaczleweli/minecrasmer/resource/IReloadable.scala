package com.nabijaczleweli.minecrasmer.resource

import net.minecraft.client.resources.IResourceManager
import net.minecraftforge.fml.relauncher.{Side, SideOnly}

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
