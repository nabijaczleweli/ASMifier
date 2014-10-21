package com.nabijaczleweli.minecrasmer.handler

import com.nabijaczleweli.minecrasmer.entity.EntityItemShredder
import com.nabijaczleweli.minecrasmer.item.ItemQuartz
import cpw.mods.fml.common.eventhandler.SubscribeEvent
import net.minecraft.entity.item.EntityItem
import net.minecraftforge.event.entity.EntityJoinWorldEvent

object EntityHandler {
	@SubscribeEvent
	def onJoinWorld(event: EntityJoinWorldEvent) =
		if(!event.entity.worldObj.isRemote && !event.entity.isDead)
			event.entity match {
				case _: EntityItemShredder =>
				case entityItem: EntityItem if entityItem.getEntityItem.getItem == ItemQuartz && entityItem.getEntityItem.getItemDamage == ItemQuartz.plateDamage =>
					val newEntity = new EntityItemShredder(event.world, entityItem.getEntityItem.copy)

					newEntity.copyDataFrom(event.entity, true)
					newEntity.delayBeforeCanPickup = entityItem.delayBeforeCanPickup

					event setCanceled true
					entityItem.getEntityItem.stackSize = 0; // Tinkers Construct fix, because tinkers invokes EntityItem#onCollideWithPlayer(EntityItem) without checking if the item entity is dead.
					event.world spawnEntityInWorld newEntity
				case _ =>
			}
}
