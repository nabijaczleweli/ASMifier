package com.nabijaczleweli.minecrasmer.compat.waila

import java.util

import com.nabijaczleweli.minecrasmer.block.BlockComputerOn
import com.nabijaczleweli.minecrasmer.entity.tile.TileEntityComputer
import com.nabijaczleweli.minecrasmer.reference.{Container, Reference}
import com.nabijaczleweli.minecrasmer.resource.{ReloadableString, ResourcesReloadedEvent}
import cpw.mods.fml.common.Optional
import cpw.mods.fml.common.eventhandler.SubscribeEvent
import cpw.mods.fml.relauncher.{Side, SideOnly}
import mcp.mobius.waila.api.{IWailaConfigHandler, IWailaDataAccessor, IWailaDataProvider}
import net.minecraft.item.{ItemBlock, ItemStack}

import scala.math._

@SideOnly(Side.CLIENT)
@Optional.Interface(iface = "mcp.mobius.waila.api.IWailaDataProvider", modid = "Waila", striprefs = true)
object ProviderComputer extends IWailaDataProvider {
	Container.eventBus register this

	var PCName = new ReloadableString(s"tile.${Reference.NAMESPACED_PREFIX}computer.neutral.name")
	var PCOffState = new ReloadableString(s"hud.${Reference.NAMESPACED_PREFIX}compat.waila.computer.state.off.name")
	var PCOnState = new ReloadableString(s"hud.${Reference.NAMESPACED_PREFIX}compat.waila.computer.state.on.name")
	var PCBaseClockSpeed = new ReloadableString(s"hud.${Reference.NAMESPACED_PREFIX}compat.waila.computer.clock.name")
	var PCMultiplier = new ReloadableString(s"hud.${Reference.NAMESPACED_PREFIX}compat.waila.computer.multiplier.name")
	var PCCPUs = new ReloadableString(s"hud.${Reference.NAMESPACED_PREFIX}compat.waila.computer.processors.name")
	var PCCPT = new ReloadableString(s"hud.${Reference.NAMESPACED_PREFIX}compat.waila.computer.cpt.name")

	@Optional.Method(modid = "Waila")
	override def getWailaStack(accessor: IWailaDataAccessor, config: IWailaConfigHandler) = {
		val is = accessor.getStack.copy()
		is setItemDamage 3  // Facing to user to right-hand side
		is setStackDisplayName PCName
		is
	}

	@Optional.Method(modid = "Waila")
	override def getWailaHead(itemStack: ItemStack, currenttip: util.List[String], accessor: IWailaDataAccessor, config: IWailaConfigHandler) =
		currenttip

	@Optional.Method(modid = "Waila")
	override def getWailaBody(itemStack: ItemStack, currenttip: util.List[String], accessor: IWailaDataAccessor, config: IWailaConfigHandler) = {
		val block = itemStack.getItem.asInstanceOf[ItemBlock].field_150939_a
		val position = accessor.getPosition
		val world = accessor.getWorld
		val on = block.isInstanceOf[BlockComputerOn.type]
		lazy val te = world.getTileEntity(position.blockX, position.blockY, position.blockZ).asInstanceOf[TileEntityComputer]

		if(on) {
			currenttip add PCOnState
			currenttip add PCBaseClockSpeed.format(te.clockSpeed)
			currenttip add PCMultiplier.format(te.multiplier)
			currenttip add PCCPUs.format(te.processors)
			currenttip add PCCPT.format(te.processors * floor(te.clockSpeed * te.multiplier).toInt)
		} else
			currenttip add PCOffState
		currenttip
	}

	@Optional.Method(modid = "Waila")
	override def getWailaTail(itemStack: ItemStack, currenttip: util.List[String], accessor: IWailaDataAccessor, config: IWailaConfigHandler) =
		currenttip

	@SubscribeEvent
	@SideOnly(Side.CLIENT)
	def onResourcesReloaded(event: ResourcesReloadedEvent) = {
		PCName.reload()
		PCOffState.reload()
		PCOnState.reload()
		PCBaseClockSpeed.reload()
		PCMultiplier.reload()
		PCCPUs.reload()
		PCCPT.reload()
	}
}
