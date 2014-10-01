package com.nabijaczleweli.minecrasmer.compat.waila

import java.util

import com.nabijaczleweli.minecrasmer.entity.tile.TileEntityOverclocker
import com.nabijaczleweli.minecrasmer.reference.Reference
import mcp.mobius.waila.api.{IWailaConfigHandler, IWailaDataAccessor, IWailaDataProvider}
import net.minecraft.item.ItemStack
import net.minecraft.util.StatCollector

object ProviderOverclocker extends IWailaDataProvider {
	override def getWailaStack(accessor: IWailaDataAccessor, config: IWailaConfigHandler) =
		null

	override def getWailaHead(itemStack: ItemStack, currenttip: util.List[String], accessor: IWailaDataAccessor, config: IWailaConfigHandler) =
		currenttip

	override def getWailaBody(itemStack: ItemStack, currenttip: util.List[String], accessor: IWailaDataAccessor, config: IWailaConfigHandler) = {
		val position = accessor.getPosition
		val world = accessor.getWorld

		currenttip add StatCollector.translateToLocalFormatted(s"hud.${Reference.MOD_ID}:compat.waila.accessory.overclocker.multiplier.name",
		                                                       world.getTileEntity(position.blockX, position.blockY, position.blockZ).asInstanceOf[TileEntityOverclocker].multiplier.asInstanceOf[Object])
		currenttip
	}

	override def getWailaTail(itemStack: ItemStack, currenttip: util.List[String], accessor: IWailaDataAccessor, config: IWailaConfigHandler) =
		currenttip
}
