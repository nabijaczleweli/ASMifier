package com.nabijaczleweli.minecrasmer.container

import com.nabijaczleweli.minecrasmer.entity.tile.TileEntityAdditionalCPU
import net.minecraft.entity.player.{EntityPlayer, InventoryPlayer}
import net.minecraft.inventory.{Container, Slot}
import net.minecraft.item.ItemStack

class ContainerAdditionalCPU(inventoryPlayer: InventoryPlayer, te: TileEntityAdditionalCPU) extends Container {
	addSlotToContainer(new SlotRespectful(te, 0, 80, 6))
	for(y <- 0 until 3)
		for(x <- 0 until 9)
			addSlotToContainer(new Slot(inventoryPlayer, x + y * 9 + 9, 8 + x * 18, 27 + y * 18))
	for(x <- 0 until 9)
		addSlotToContainer(new Slot(inventoryPlayer, x, 8 + x * 18, 85))

	override def canInteractWith(player: EntityPlayer) =
		te isUseableByPlayer player

	override def putStackInSlot(id: Int, is: ItemStack) =
		if(te.isItemValidForSlot(id, is))
			super.putStackInSlot(id, is)

	override def transferStackInSlot(player: EntityPlayer, id: Int): ItemStack = {
		var itemstack: ItemStack = null
		val slot = inventorySlots.get(id).asInstanceOf[Slot]

		if(slot != null && slot.getHasStack) {
			val itemstack1 = slot.getStack
			itemstack = itemstack1.copy
			if(id < 1) {
				if(!mergeItemStack(itemstack1, 1, 37, true))
					return null
			} else if(!mergeItemStack(itemstack1, 0, 1, false))
				return null
			if(itemstack1.stackSize == 0)
				slot putStack null
			else
				slot.onSlotChanged()
			if(itemstack1.stackSize == itemstack.stackSize)
				return null
			slot.onPickupFromSlot(player, itemstack1)
		}

		itemstack
	}
}
