package com.nabijaczleweli.minecrasmer.proxy

import com.nabijaczleweli.minecrasmer.item.ItemScoop
import com.nabijaczleweli.minecrasmer.render.FilledScoopRenderer
import com.nabijaczleweli.minecrasmer.resource.ReloaderListener
import cpw.mods.fml.relauncher.{Side, SideOnly}
import net.minecraft.client.Minecraft
import net.minecraft.client.resources.SimpleReloadableResourceManager
import net.minecraftforge.client.MinecraftForgeClient

import scala.collection.mutable

@SideOnly(Side.CLIENT)
class ClientProxy extends CommonProxy {
	val scoopRenderQueue = mutable.Queue[ItemScoop]()

	override def registerRenderers() {
		super.registerRenderers()
		while(scoopRenderQueue.size != 0)
			MinecraftForgeClient.registerItemRenderer(scoopRenderQueue.dequeue(), FilledScoopRenderer)
	}

	override def registerEvents() {
		super.registerEvents()
		Minecraft.getMinecraft.getResourceManager.asInstanceOf[SimpleReloadableResourceManager] registerReloadListener ReloaderListener
	}
}
