package com.nabijaczleweli.minecrasmer.container

import net.minecraft.inventory.{IInventory, Slot}
import net.minecraft.item.ItemStack

class SlotRespectful(inv: IInventory, id: Int, xdisp: Int, ydisp: Int) extends Slot(inv, id, xdisp, ydisp) {
	override def isItemValid(is: ItemStack) =
		inventory.isItemValidForSlot(slotNumber, is)
}
