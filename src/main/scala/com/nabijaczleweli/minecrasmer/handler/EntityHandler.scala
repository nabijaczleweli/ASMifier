package com.nabijaczleweli.minecrasmer.handler

import com.nabijaczleweli.minecrasmer.entity.{EntityItemPurifier, EntityItemShredder}
import com.nabijaczleweli.minecrasmer.item.ItemQuartz
import cpw.mods.fml.common.eventhandler.SubscribeEvent
import net.minecraft.entity.item.EntityItem
import net.minecraftforge.event.entity.EntityJoinWorldEvent

object EntityHandler {
	@SubscribeEvent
	def onItemJoinWorld(event: EntityJoinWorldEvent) =
		if(!event.entity.worldObj.isRemote && !event.entity.isDead) {
			var entity: EntityItem = null

			event.entity match {
				case _: EntityItemShredder =>
				case _: EntityItemPurifier =>
				case entityItem: EntityItem if entityItem.getEntityItem.getItem == ItemQuartz && entityItem.getEntityItem.getItemDamage == ItemQuartz.plateDamage =>
					entity = new EntityItemShredder(event.world, entityItem.getEntityItem.copy)
				case entityItem: EntityItem if entityItem.getEntityItem.getItem == ItemQuartz && entityItem.getEntityItem.getItemDamage == ItemQuartz.shardsDamage =>
					entity = new EntityItemPurifier(event.world, entityItem.getEntityItem.copy)
				case _ =>
			}

			event.entity match {
				case entityItem: EntityItem if entity != null =>
					entity.copyDataFrom(event.entity, true)
					entity.delayBeforeCanPickup = entityItem.delayBeforeCanPickup

					event setCanceled true
					entityItem.getEntityItem.stackSize = 0; // Tinkers Construct fix, because tinkers invokes EntityItem#onCollideWithPlayer(EntityItem) without checking if the item entity is dead.
					event.world spawnEntityInWorld entity
				case _ =>
			}
		}
}
