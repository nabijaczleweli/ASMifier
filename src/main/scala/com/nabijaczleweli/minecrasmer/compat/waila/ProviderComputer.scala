package com.nabijaczleweli.minecrasmer.compat.waila

import java.util

import com.nabijaczleweli.minecrasmer.block.BlockComputerOn
import com.nabijaczleweli.minecrasmer.entity.tile.TileEntityComputer
import com.nabijaczleweli.minecrasmer.reference.Reference
import com.nabijaczleweli.minecrasmer.resource.ResourcesReloadedEvent
import cpw.mods.fml.common.eventhandler.SubscribeEvent
import cpw.mods.fml.relauncher.{Side, SideOnly}
import mcp.mobius.waila.api.{IWailaConfigHandler, IWailaDataAccessor, IWailaDataProvider}
import net.minecraft.item.{ItemBlock, ItemStack}
import net.minecraft.util.StatCollector

@SideOnly(Side.CLIENT)
object ProviderComputer extends IWailaDataProvider {
	var PCName: String = _
	var PCOffState: String = _
	var PCOnState: String = _
	var PCBaseClockSpeed: String = _

	override def getWailaStack(accessor: IWailaDataAccessor, config: IWailaConfigHandler) = {
		val is = accessor.getStack.copy()
		is setItemDamage 3  // Facing to user to right-hand side
		is setStackDisplayName PCName
		is
	}

	override def getWailaHead(itemStack: ItemStack, currenttip: util.List[String], accessor: IWailaDataAccessor, config: IWailaConfigHandler) =
		currenttip

	override def getWailaBody(itemStack: ItemStack, currenttip: util.List[String], accessor: IWailaDataAccessor, config: IWailaConfigHandler) = {
		val block = itemStack.getItem.asInstanceOf[ItemBlock].field_150939_a
		val position = accessor.getPosition
		val world = accessor.getWorld
		val on = block.isInstanceOf[BlockComputerOn.type]
		lazy val te = world.getTileEntity(position.blockX, position.blockY, position.blockZ).asInstanceOf[TileEntityComputer]

		if(on) {
			currenttip add PCOnState
			currenttip add PCBaseClockSpeed.format(te.clockSpeed: Integer)
		} else
			currenttip add PCOffState
		currenttip
	}

	override def getWailaTail(itemStack: ItemStack, currenttip: util.List[String], accessor: IWailaDataAccessor, config: IWailaConfigHandler) =
		currenttip

	@SubscribeEvent
	def onResourcesReloaded(event: ResourcesReloadedEvent) {
		PCName = StatCollector translateToLocal s"tile.${Reference.NAMESPACED_PREFIX}computer.neutral.name"
		PCOffState = StatCollector translateToLocal s"hud.${Reference.MOD_ID}:compat.waila.computer.state.off.name"
		PCOnState = StatCollector translateToLocal s"hud.${Reference.MOD_ID}:compat.waila.computer.state.on.name"
		PCBaseClockSpeed = StatCollector translateToLocal s"hud.${Reference.MOD_ID}:compat.waila.computer.clock.name"
	}
}
