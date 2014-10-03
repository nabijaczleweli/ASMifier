package com.nabijaczleweli.minecrasmer.resource

import com.nabijaczleweli.minecrasmer.reference.Container
import cpw.mods.fml.relauncher.{Side, SideOnly}
import net.minecraft.client.resources.{IResourceManagerReloadListener, IResourceManager}

@SideOnly(Side.CLIENT)
object ReloaderListener extends IResourceManagerReloadListener {
	 override def onResourceManagerReload(mgr: IResourceManager) =
		 Container.eventBus post new ResourcesReloadedEvent(mgr)
}
