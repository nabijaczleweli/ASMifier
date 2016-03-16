package com.nabijaczleweli.minecrasmer.proxy

import com.nabijaczleweli.minecrasmer.block.{BlockAccessoryAdditionalCPU, BlockAccessoryOverclocker, BlockComputerOff, BlockComputerOn}
import com.nabijaczleweli.minecrasmer.entity.Villager._
import com.nabijaczleweli.minecrasmer.entity.{EntityItemCleaner, EntityItemShredder}
import com.nabijaczleweli.minecrasmer.item._
import com.nabijaczleweli.minecrasmer.reference.Container._
import com.nabijaczleweli.minecrasmer.resource.{MineCrASMerLocation, ReloaderListener}
import com.nabijaczleweli.minecrasmer.util.IMultiModelItem
import com.nabijaczleweli.minecrasmer.util.StringUtils._
import net.minecraft.client.Minecraft
import net.minecraft.client.renderer.entity.{RenderEntityItem, RenderManager}
import net.minecraft.client.resources.IReloadableResourceManager
import net.minecraft.client.resources.model.ModelResourceLocation
import net.minecraft.entity.item.EntityItem
import net.minecraft.item.Item
import net.minecraftforge.fml.client.registry.{IRenderFactory, RenderingRegistry}
import net.minecraftforge.fml.common.registry.VillagerRegistry
import net.minecraftforge.fml.relauncher.{Side, SideOnly}

@SideOnly(Side.CLIENT)
class ClientProxy extends CommonProxy {
	private implicit class ModelRegistrationUtils(item: Item) extends IMultiModelItem {
		@inline
		override def registerModels() =
			Minecraft.getMinecraft.getRenderItem.getItemModelMesher.register(item, 0, new ModelResourceLocation(item.getUnlocalizedName substring ".", "inventory"))
	}

	private def itemRendererFactory[T <: EntityItem] =
		new IRenderFactory[T] {
			override def createRenderFor(manager: RenderManager) =
				new RenderEntityItem(manager, Minecraft.getMinecraft.getRenderItem)
		}


	override def registerRenderers() {
		super.registerRenderers()
		/*while(scoopRenderQueue.size != 0)
			MinecraftForgeClient.registerItemRenderer(scoopRenderQueue.dequeue(), FilledScoopRenderer)*/

		RenderingRegistry.registerEntityRenderingHandler(classOf[EntityItemShredder], itemRendererFactory[EntityItemShredder])
		RenderingRegistry.registerEntityRenderingHandler(classOf[EntityItemCleaner], itemRendererFactory[EntityItemCleaner])

		VillagerRegistry.instance.registerVillagerSkin(electronicsVillagerID, MineCrASMerLocation("textures/entity/villager/electronic.png"))
	}

	override def registerEvents() {
		super.registerEvents()
		Minecraft.getMinecraft.getResourceManager.asInstanceOf[IReloadableResourceManager] registerReloadListener ReloaderListener
	}

	override def registerModels() {
		super.registerModels()

		ItemWrench.registerModels()
		ItemPlastic.registerModels()
		ItemPCB.registerModels()
		ItemCPU.registerModels()
		socketCPU.registerModels()
		stoneRod.registerModels()
		ItemQuartz.registerModels()
		ItemPartialIron.registerModels()
		ItemScoop.registerModels()

		(Item getItemFromBlock BlockComputerOff).registerModels()
		(Item getItemFromBlock BlockComputerOn).registerModels()
		(Item getItemFromBlock BlockAccessoryOverclocker).registerModels()
		(Item getItemFromBlock BlockAccessoryAdditionalCPU).registerModels()
	}
}
