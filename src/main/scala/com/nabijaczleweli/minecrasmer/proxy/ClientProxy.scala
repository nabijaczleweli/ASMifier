package com.nabijaczleweli.minecrasmer.proxy

import com.nabijaczleweli.minecrasmer.entity.{EntityItemPurifier, EntityItemShredder}
import com.nabijaczleweli.minecrasmer.item.ItemScoop
import com.nabijaczleweli.minecrasmer.render.FilledScoopRenderer
import com.nabijaczleweli.minecrasmer.resource.ReloaderListener
import cpw.mods.fml.client.registry.RenderingRegistry
import cpw.mods.fml.relauncher.{Side, SideOnly}
import net.minecraft.client.Minecraft
import net.minecraft.client.renderer.entity.RenderItem
import net.minecraft.client.resources.IReloadableResourceManager
import net.minecraftforge.client.MinecraftForgeClient

import scala.collection.mutable

@SideOnly(Side.CLIENT)
class ClientProxy extends CommonProxy {
	val scoopRenderQueue = mutable.Queue[ItemScoop]()

	override def registerRenderers() {
		super.registerRenderers()
		while(scoopRenderQueue.size != 0)
			MinecraftForgeClient.registerItemRenderer(scoopRenderQueue.dequeue(), FilledScoopRenderer)

		val itemRenderer = new RenderItem
		RenderingRegistry.registerEntityRenderingHandler(classOf[EntityItemShredder], itemRenderer)
		RenderingRegistry.registerEntityRenderingHandler(classOf[EntityItemPurifier], itemRenderer)
	}

	override def registerEvents() {
		super.registerEvents()
		Minecraft.getMinecraft.getResourceManager.asInstanceOf[IReloadableResourceManager] registerReloadListener ReloaderListener
	}
}
