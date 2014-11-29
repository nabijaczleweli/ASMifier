package com.nabijaczleweli.minecrasmer.proxy

import com.nabijaczleweli.minecrasmer.entity.Villager._
import com.nabijaczleweli.minecrasmer.entity.{EntityItemCleaner, EntityItemShredder}
import com.nabijaczleweli.minecrasmer.item._
import com.nabijaczleweli.minecrasmer.reference.Container._
import com.nabijaczleweli.minecrasmer.resource.{MineCrASMerLocation, ReloaderListener}
import com.nabijaczleweli.minecrasmer.util.StringUtils._
import net.minecraft.client.Minecraft
import net.minecraft.client.renderer.entity.RenderEntityItem
import net.minecraft.client.resources.IReloadableResourceManager
import net.minecraft.client.resources.model.ModelResourceLocation
import net.minecraft.item.Item
import net.minecraftforge.fml.client.registry.RenderingRegistry
import net.minecraftforge.fml.common.registry.VillagerRegistry
import net.minecraftforge.fml.relauncher.{Side, SideOnly}

import scala.collection.mutable.{Queue => mQueue}

@SideOnly(Side.CLIENT)
class ClientProxy extends CommonProxy {
	private implicit class ModelRegistrationUtils(item: Item) {
		def registerModel() =
			Minecraft.getMinecraft.getRenderItem.getItemModelMesher.register(item, 0, new ModelResourceLocation(item.getUnlocalizedName substring ".", "inventory"))
	}

	val scoopRenderQueue = mQueue[ItemScoop]()

	override def registerRenderers() {
		super.registerRenderers()
		/*while(scoopRenderQueue.size != 0)
			MinecraftForgeClient.registerItemRenderer(scoopRenderQueue.dequeue(), FilledScoopRenderer)*/

		val itemRenderer = new RenderEntityItem(Minecraft.getMinecraft.getRenderManager, Minecraft.getMinecraft.getRenderItem)
		RenderingRegistry.registerEntityRenderingHandler(classOf[EntityItemShredder], itemRenderer)
		RenderingRegistry.registerEntityRenderingHandler(classOf[EntityItemCleaner], itemRenderer)

		VillagerRegistry.instance.registerVillagerSkin(electronicsVillagerID, new MineCrASMerLocation("textures/entity/villager_electronic.png"))
	}

	override def registerEvents() {
		super.registerEvents()
		Minecraft.getMinecraft.getResourceManager.asInstanceOf[IReloadableResourceManager] registerReloadListener ReloaderListener
	}

	override def registerModels() = {
		super.registerModels()

		ItemWrench.registerModel()
		//ItemPlastic.register()
		//ItemPCB.register()
		//ItemCPU.register()
		socketCPU.registerModel()
		stoneRod.registerModel()
		//ItemQuartz.register()
		//ItemPartialIron.register()

		//scoopEmpty.register()
		//scoopLiquidCrystal.register()
	}
}
