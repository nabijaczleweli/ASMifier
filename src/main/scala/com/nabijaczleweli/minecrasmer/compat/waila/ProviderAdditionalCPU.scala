package com.nabijaczleweli.minecrasmer.compat.waila

import com.nabijaczleweli.minecrasmer.entity.tile.TileEntityAdditionalCPU
import com.nabijaczleweli.minecrasmer.reference.{Reference, Container}
import com.nabijaczleweli.minecrasmer.resource.{ResourcesReloadedEvent, ReloadableString}
import net.minecraft.world.World
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import net.minecraftforge.fml.relauncher.{Side, SideOnly}

import java.util.{List => jList}


@SideOnly(Side.CLIENT)
object ProviderAdditionalCPU extends AccessoryWailaDataProvider[TileEntityAdditionalCPU] {
	Container.eventBus register this

	var multiplierMessage = new ReloadableString(s"hud.${Reference.NAMESPACED_PREFIX}compat.waila.accessory.processor.cpus.name")

	override protected def getWailaBodyImpl(world: World, currenttip: jList[String], te: TileEntityAdditionalCPU) = {
		currenttip add multiplierMessage.format(te.processors)
		currenttip
	}

	@SubscribeEvent
	def onResourcesReloaded(event: ResourcesReloadedEvent) {
		multiplierMessage.reload()
	}
}
