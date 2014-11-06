package com.nabijaczleweli.minecrasmer.compat.nei

import net.minecraft.item.ItemStack

private[nei] case class ItemStackWrapper(is: ItemStack) extends Equals {
	override def equals(obj: Any) =
		canEqual(obj) && ItemStack.areItemStacksEqual(is, obj.asInstanceOf[ItemStackWrapper].is)

	override def canEqual(that: Any) =
		that != null && that.isInstanceOf[this.type]
}
