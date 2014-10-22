package com.nabijaczleweli.minecrasmer.entity

import com.nabijaczleweli.minecrasmer.item.ItemQuartz
import net.minecraft.entity.item.EntityItem
import net.minecraft.init.Blocks
import net.minecraft.item.{Item, ItemStack}
import net.minecraft.world.World

class EntityItemPurifier(world: World, x: Double, y: Double, z: Double, is: ItemStack) extends EntityItem(world, x, y, z, is) {
	def this(world: World, is: ItemStack) =
		this(world, 0, 0, 0, is)

	def this(world: World) =
		this(world, new ItemStack(null: Item))


	override def onEntityUpdate() {
		super.onEntityUpdate()
		if(ticksExisted >= 100 && (getEntityItem.getItem == ItemQuartz && getEntityItem.getItemDamage == ItemQuartz.shardsDamage) && getEntityItem.stackSize > 0 && !isDead && !worldObj.isRemote)
			worldObj.getBlock(posX.floor.toInt, posY.floor.toInt, posZ.floor.toInt) match {
				case Blocks.water | Blocks.flowing_water =>
					val purified = new EntityItem(worldObj)

					purified.copyDataFrom(this, true)
					purified setEntityItemStack new ItemStack(ItemQuartz, getEntityItem.stackSize, ItemQuartz.pureShardsDamage)
					purified.delayBeforeCanPickup = delayBeforeCanPickup

					worldObj spawnEntityInWorld purified
					getEntityItem.stackSize = 0
				case _ =>
			}
	}
}
