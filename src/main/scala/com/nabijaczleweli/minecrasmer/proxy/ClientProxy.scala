package com.nabijaczleweli.minecrasmer.proxy

import java.lang.{Float => jFloat}

import com.nabijaczleweli.minecrasmer.entity.Villager._
import com.nabijaczleweli.minecrasmer.entity.{EntityItemCleaner, EntityItemShredder}
import com.nabijaczleweli.minecrasmer.item.ItemScoop
import com.nabijaczleweli.minecrasmer.render.FilledScoopRenderer
import com.nabijaczleweli.minecrasmer.resource.{MineCrASMerLocation, ReloaderListener}
import net.minecraft.client.Minecraft
import net.minecraft.client.renderer.entity.RenderEntityItem
import net.minecraft.client.resources.IReloadableResourceManager
import net.minecraftforge.client.MinecraftForgeClient
import net.minecraftforge.fml.client.registry.RenderingRegistry
import net.minecraftforge.fml.common.registry.VillagerRegistry
import net.minecraftforge.fml.relauncher.{Side, SideOnly}

import scala.collection.mutable

@SideOnly(Side.CLIENT)
class ClientProxy extends CommonProxy {
	val scoopRenderQueue = mutable.Queue[ItemScoop]()

	override def registerRenderers() {
		super.registerRenderers()
		while(scoopRenderQueue.size != 0)
			MinecraftForgeClient.registerItemRenderer(scoopRenderQueue.dequeue(), FilledScoopRenderer)

		val itemRenderer = new RenderEntityItem(Minecraft.getMinecraft.getRenderManager, Minecraft.getMinecraft.getRenderItem)
		RenderingRegistry.registerEntityRenderingHandler(classOf[EntityItemShredder], itemRenderer)
		RenderingRegistry.registerEntityRenderingHandler(classOf[EntityItemCleaner], itemRenderer)

		VillagerRegistry.instance.registerVillagerSkin(electronicsVillagerID, new MineCrASMerLocation("textures/entity/villager_electronic.png"))
	}

	override def registerEvents() {
		super.registerEvents()
		Minecraft.getMinecraft.getResourceManager.asInstanceOf[IReloadableResourceManager] registerReloadListener ReloaderListener
	}
}
