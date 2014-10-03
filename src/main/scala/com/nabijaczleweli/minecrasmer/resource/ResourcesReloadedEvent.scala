package com.nabijaczleweli.minecrasmer.resource

import cpw.mods.fml.common.eventhandler.Event
import cpw.mods.fml.relauncher.{Side, SideOnly}
import net.minecraft.client.resources.IResourceManager

@SideOnly(Side.CLIENT)
class ResourcesReloadedEvent(val manager: IResourceManager) extends Event
