package com.nabijaczleweli.minecrasmer.handler

import com.nabijaczleweli.minecrasmer.entity.{EntityItemCleaner, EntityItemShredder}
import com.nabijaczleweli.minecrasmer.item.ItemQuartz
import net.minecraft.entity.item.EntityItem
import net.minecraftforge.event.entity.EntityJoinWorldEvent
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent

object EntityHandler {
	@SubscribeEvent
	def onItemJoinWorld(event: EntityJoinWorldEvent) =
		if(!event.entity.worldObj.isRemote && !event.entity.isDead) {
			var entity: EntityItem = null

			event.entity match {
				case _: EntityItemShredder =>
				case _: EntityItemCleaner =>
				case entityItem: EntityItem if entityItem.getEntityItem.getItem == ItemQuartz && entityItem.getEntityItem.getItemDamage == ItemQuartz.plateDamage =>
					entity = new EntityItemShredder(event.world, entityItem.getEntityItem.copy)
				case entityItem: EntityItem if entityItem.getEntityItem.getItem == ItemQuartz && entityItem.getEntityItem.getItemDamage == ItemQuartz.shardsDamage =>
					entity = new EntityItemCleaner(event.world, entityItem.getEntityItem.copy)
				case _ =>
			}

			event.entity match {
				case entityItem: EntityItem if entity != null =>
					entity func_180432_n event.entity

					event setCanceled true
					entityItem.getEntityItem.stackSize = 0; // Tinkers Construct fix, because tinkers invokes EntityItem#onCollideWithPlayer(EntityItem) without checking if the item entity is dead.
					event.world spawnEntityInWorld entity
				case _ =>
			}
		}
}
