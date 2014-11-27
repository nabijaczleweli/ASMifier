package com.nabijaczleweli.minecrasmer.resource

import com.nabijaczleweli.minecrasmer.reference.Container
import net.minecraft.client.resources.{IResourceManager, IResourceManagerReloadListener}
import net.minecraftforge.fml.relauncher.{Side, SideOnly}

@SideOnly(Side.CLIENT)
object ReloaderListener extends IResourceManagerReloadListener {
	override def onResourceManagerReload(mgr: IResourceManager) =
		Container.eventBus post new ResourcesReloadedEvent(mgr)
}
