package com.nabijaczleweli.minecrasmer.handler

import java.util.{Map => jMap}

import com.nabijaczleweli.minecrasmer.item.{ItemPCB, ItemQuartz, ItemScoop}
import net.minecraft.entity.item.EntityItem
import net.minecraft.entity.player.EntityPlayerMP
import net.minecraft.item.crafting.FurnaceRecipes
import net.minecraft.item.{ItemPiston, ItemStack}
import net.minecraft.nbt.NBTTagCompound
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import net.minecraftforge.fml.common.gameevent.PlayerEvent.{ItemCraftedEvent, ItemSmeltedEvent}

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
			FurnaceRecipes.instance.getSmeltingList.asInstanceOf[jMap[ItemStack, ItemStack]].entrySet filter {_.getValue.getItem == event.smelting.getItem} find {ent =>
				ent.getKey.getItem match {
					case null =>
						false
					case ItemScoop =>
						!ItemScoop.empty(ent.getKey)
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
					case ItemScoop if !ItemScoop.empty(is) =>
						is setTagCompound ItemScoop.scoopEmpty.getTagCompound.copy.asInstanceOf[NBTTagCompound]
						is.stackSize += 1
					case _ =>
				}
		}
}
