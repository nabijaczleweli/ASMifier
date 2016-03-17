package com.nabijaczleweli.minecrasmer.handler

import com.nabijaczleweli.minecrasmer.item.ItemWrench
import net.minecraftforge.event.world.BlockEvent.HarvestDropsEvent
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent

object BlocksHandler {
	@SubscribeEvent
	def onWrenchableHarvested(event: HarvestDropsEvent) {
		if(ItemWrench.effectiveAgainst.contains(event.state.getBlock))
			if((event.harvester == null || event.harvester.getCurrentEquippedItem == null || event.harvester.getCurrentEquippedItem.getItem == null) ||
			   event.harvester.getCurrentEquippedItem.getItem.getHarvestLevel(event.harvester.getCurrentEquippedItem, "wrench") == -1)
				event.dropChance = 0
	}
}
