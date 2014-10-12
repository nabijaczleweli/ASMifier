package com.nabijaczleweli.minecrasmer.container

import net.minecraft.inventory.{IInventory, Slot}
import net.minecraft.item.ItemStack

class SlotRespectful(private val inv: IInventory, private val id: Int, private val xdisp: Int, private val ydisp: Int) extends Slot(inv, id, xdisp, ydisp) {
	override def isItemValid(is: ItemStack) =
		inventory.isItemValidForSlot(slotNumber, is)
}
