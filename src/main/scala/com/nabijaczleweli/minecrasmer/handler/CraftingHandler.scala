package com.nabijaczleweli.minecrasmer.handler

import java.util.{Map => jMap}

import com.nabijaczleweli.minecrasmer.item.{ItemPCB, ItemQuartz, ItemScoop}
import cpw.mods.fml.common.eventhandler.SubscribeEvent
import cpw.mods.fml.common.gameevent.PlayerEvent.{ItemCraftedEvent, ItemSmeltedEvent}
import net.minecraft.entity.item.EntityItem
import net.minecraft.entity.player.EntityPlayerMP
import net.minecraft.item.crafting.FurnaceRecipes
import net.minecraft.item.{ItemPiston, ItemStack}

import scala.collection.JavaConversions._

object CraftingHandler {
	@SubscribeEvent
	def onCraftedWithScoop(event: ItemCraftedEvent) =
		for(idx <- 0 until event.craftMatrix.getSizeInventory)
			processStack(event.craftMatrix getStackInSlot idx)

	@SubscribeEvent
	def onCraftedNoScoop(event: ItemCraftedEvent) =
		if((event.crafting isItemEqual new ItemStack(ItemPCB, 1, ItemPCB.emptyPCBDamage)) || (event.crafting isItemEqual new ItemStack(ItemQuartz, 1, ItemQuartz.plateDamage)))
			for(idx <- (0 until event.craftMatrix.getSizeInventory).reverse)
				event.craftMatrix getStackInSlot idx match {
					case null =>
					case is =>
						is.getItem match {
							case null =>
							case it: ItemPiston =>
								is.stackSize += 1
							case _ =>
						}
				}

	@SubscribeEvent
	def onScoopSmelted(event: ItemSmeltedEvent) =
		if(!event.player.getEntityWorld.isRemote)
			FurnaceRecipes.smelting.getSmeltingList.asInstanceOf[jMap[ItemStack, ItemStack]].entrySet filter {_.getValue.getItem == event.smelting.getItem} find {
				_.getKey.getItem match {
					case null =>
						false
					case item: ItemScoop =>
						!item.empty
					case _ =>
						false
				}
			} match {
				case None =>
				case Some(entry) =>
					val player = event.player
					val newStack = entry.getKey.getItem getContainerItem event.smelting
					if(player.inventory addItemStackToInventory newStack) // Stolen from BlockCauldron
						player match {
							case p: EntityPlayerMP =>
								p sendContainerToPlayer p.inventoryContainer
							case _ =>
						}
					else
						player.getEntityWorld.spawnEntityInWorld(new EntityItem(player.getEntityWorld, player.posX + .5D, player.posY + 1.5D, player.posZ + 0.5D, newStack))
			}

	private def processStack(stack: ItemStack) =
		stack match {
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
