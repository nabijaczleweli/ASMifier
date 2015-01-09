package com.nabijaczleweli.minecrasmer.compat.waila

/*
@SideOnly(Side.CLIENT)
object ProviderOverclocker extends AccessoryWailaDataProvider[TileEntityOverclocker] {
	Container.eventBus register this

	var multiplierMessage = new ReloadableString(s"hud.${Reference.NAMESPACED_PREFIX}compat.waila.accessory.overclocker.multiplier.name")

	override protected def getWailaBodyImpl(world: World, currenttip: util.List[String], te: TileEntityOverclocker) = {
		currenttip add multiplierMessage.format(te.multiplier)
		currenttip
	}

	@SubscribeEvent
	def onResourcesReloaded(event: ResourcesReloadedEvent) {
		multiplierMessage.reload()
	}
}
*/