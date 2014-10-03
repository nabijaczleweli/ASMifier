package com.nabijaczleweli.minecrasmer.compat.waila

import java.{lang, util}

import com.nabijaczleweli.minecrasmer.entity.tile.TileEntityOverclocker
import com.nabijaczleweli.minecrasmer.reference.Reference
import com.nabijaczleweli.minecrasmer.resource.ResourcesReloadedEvent
import cpw.mods.fml.common.eventhandler.SubscribeEvent
import cpw.mods.fml.relauncher.{Side, SideOnly}
import mcp.mobius.waila.api.{IWailaConfigHandler, IWailaDataAccessor, IWailaDataProvider}
import net.minecraft.item.ItemStack
import net.minecraft.util.StatCollector

@SideOnly(Side.CLIENT)
object ProviderOverclocker extends IWailaDataProvider {
	var multiplierMessage: String = _

	override def getWailaStack(accessor: IWailaDataAccessor, config: IWailaConfigHandler) =
		null

	override def getWailaHead(itemStack: ItemStack, currenttip: util.List[String], accessor: IWailaDataAccessor, config: IWailaConfigHandler) =
		currenttip

	override def getWailaBody(itemStack: ItemStack, currenttip: util.List[String], accessor: IWailaDataAccessor, config: IWailaConfigHandler) = {
		val position = accessor.getPosition
		val world = accessor.getWorld
		val te = world.getTileEntity(position.blockX, position.blockY, position.blockZ).asInstanceOf[TileEntityOverclocker]

		currenttip add multiplierMessage.format(te.multiplier: lang.Float)
		currenttip
	}

	override def getWailaTail(itemStack: ItemStack, currenttip: util.List[String], accessor: IWailaDataAccessor, config: IWailaConfigHandler) =
		currenttip

	@SubscribeEvent
	def onResourcesReloaded(event: ResourcesReloadedEvent) {
		multiplierMessage = StatCollector translateToLocal s"hud.${Reference.MOD_ID}:compat.waila.accessory.overclocker.multiplier.name"
	}
}
