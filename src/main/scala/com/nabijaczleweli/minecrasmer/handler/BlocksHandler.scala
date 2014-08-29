package com.nabijaczleweli.minecrasmer.handler

import com.nabijaczleweli.minecrasmer.item.ItemWrench
import cpw.mods.fml.common.eventhandler.SubscribeEvent
import net.minecraftforge.event.world.BlockEvent.HarvestDropsEvent

object BlocksHandler {
	@SubscribeEvent
	def onWrenchableHarvested(event: HarvestDropsEvent) {
		if(ItemWrench.effectiveAgainst.contains(event.block))
			if((event.harvester == null || event.harvester.getCurrentEquippedItem == null || event.harvester.getCurrentEquippedItem.getItem == null) ||
			   event.harvester.getCurrentEquippedItem.getItem.getHarvestLevel(event.harvester.getCurrentEquippedItem, "wrench") == -1)
				event.dropChance = 0
	}
}
