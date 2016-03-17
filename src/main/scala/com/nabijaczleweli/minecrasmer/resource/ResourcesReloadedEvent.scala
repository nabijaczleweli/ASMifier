package com.nabijaczleweli.minecrasmer.resource

import net.minecraft.client.resources.IResourceManager
import net.minecraftforge.fml.common.eventhandler.Event
import net.minecraftforge.fml.relauncher.{Side, SideOnly}

@SideOnly(Side.CLIENT)
class ResourcesReloadedEvent(val manager: IResourceManager) extends Event
