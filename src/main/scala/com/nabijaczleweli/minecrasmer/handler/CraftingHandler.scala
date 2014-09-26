package com.nabijaczleweli.minecrasmer.handler

import com.nabijaczleweli.minecrasmer.item.ItemScoop
import cpw.mods.fml.common.eventhandler.SubscribeEvent
import cpw.mods.fml.common.gameevent.PlayerEvent.ItemCraftedEvent

object CraftingHandler {
	@SubscribeEvent
	def onCraftedWithScoop(event: ItemCraftedEvent) =
		for(idx <- 0 until event.craftMatrix.getSizeInventory)
			event.craftMatrix getStackInSlot idx match {
				case null =>
				case is =>
					is.getItem match {
						case null =>
						case it: ItemScoop if !it.empty =>
							is func_150996_a it.getContainerItem
							is.stackSize += 1
						case _ =>
					}
			}
}
