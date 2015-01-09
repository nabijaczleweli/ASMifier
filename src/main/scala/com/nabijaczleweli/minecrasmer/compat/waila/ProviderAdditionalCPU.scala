package com.nabijaczleweli.minecrasmer.compat.waila

/*
@SideOnly(Side.CLIENT)
object ProviderAdditionalCPU extends AccessoryWailaDataProvider[TileEntityAdditionalCPU] {
	Container.eventBus register this

	var multiplierMessage = new ReloadableString(s"hud.${Reference.NAMESPACED_PREFIX}compat.waila.accessory.processor.cpus.name")

	override protected def getWailaBodyImpl(world: World, currenttip: util.List[String], te: TileEntityAdditionalCPU) = {
		currenttip add multiplierMessage.format(te.processors)
		currenttip
	}

	@SubscribeEvent
	def onResourcesReloaded(event: ResourcesReloadedEvent) {
		multiplierMessage.reload()
	}
}
*/