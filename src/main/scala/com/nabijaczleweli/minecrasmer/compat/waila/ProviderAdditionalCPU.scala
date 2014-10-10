package com.nabijaczleweli.minecrasmer.compat.waila

import java.util

import com.nabijaczleweli.minecrasmer.entity.tile.TileEntityAdditionalCPU
import com.nabijaczleweli.minecrasmer.reference.{Container, Reference}
import com.nabijaczleweli.minecrasmer.resource.{ReloadableString, ResourcesReloadedEvent}
import cpw.mods.fml.common.Optional
import cpw.mods.fml.common.eventhandler.SubscribeEvent
import cpw.mods.fml.relauncher.{Side, SideOnly}
import mcp.mobius.waila.api.{IWailaConfigHandler, IWailaDataAccessor, IWailaDataProvider}
import net.minecraft.item.ItemStack

@SideOnly(Side.CLIENT)
@Optional.Interface(iface = "mcp.mobius.waila.api.IWailaDataProvider", modid = "Waila", striprefs = true)
object ProviderAdditionalCPU extends IWailaDataProvider {
	Container.eventBus register this

	var multiplierMessage = new ReloadableString(s"hud.${Reference.NAMESPACED_PREFIX}compat.waila.accessory.processor.cpus.name")

	@Optional.Method(modid = "Waila")
	override def getWailaStack(accessor: IWailaDataAccessor, config: IWailaConfigHandler) =
		null

	@Optional.Method(modid = "Waila")
	override def getWailaHead(itemStack: ItemStack, currenttip: util.List[String], accessor: IWailaDataAccessor, config: IWailaConfigHandler) =
		currenttip

	@Optional.Method(modid = "Waila")
	override def getWailaBody(itemStack: ItemStack, currenttip: util.List[String], accessor: IWailaDataAccessor, config: IWailaConfigHandler) = {
		val position = accessor.getPosition
		val world = accessor.getWorld
		val te = world.getTileEntity(position.blockX, position.blockY, position.blockZ).asInstanceOf[TileEntityAdditionalCPU]

		currenttip add multiplierMessage.format(te.processors)
		currenttip
	}

	@Optional.Method(modid = "Waila")
	override def getWailaTail(itemStack: ItemStack, currenttip: util.List[String], accessor: IWailaDataAccessor, config: IWailaConfigHandler) =
		currenttip

	@SubscribeEvent
	@SideOnly(Side.CLIENT)
	def onResourcesReloaded(event: ResourcesReloadedEvent) {
		multiplierMessage.reload()
	}
}
