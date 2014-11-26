package com.nabijaczleweli.minecrasmer.entity

import com.nabijaczleweli.minecrasmer.item.ItemQuartz
import com.nabijaczleweli.minecrasmer.reference.Reference
import com.nabijaczleweli.minecrasmer.util.IConfigurable
import net.minecraft.entity.item.EntityItem
import net.minecraft.item.{Item, ItemStack}
import net.minecraft.util.DamageSource
import net.minecraft.world.World
import net.minecraftforge.common.config.Configuration

class EntityItemShredder(world: World, x: Double, y: Double, z: Double, is: ItemStack) extends EntityItem(world, x, y, z, is) {
	def this(world: World, is: ItemStack) =
		this(world, 0, 0, 0, is)

	def this(world: World) =
		this(world, new ItemStack(null: Item))

	override def attackEntityFrom(src: DamageSource, dmg: Float) = {
		super.attackEntityFrom(src, dmg)
		if(src.isExplosion && (getEntityItem.getItem == ItemQuartz && getEntityItem.getItemDamage == ItemQuartz.plateDamage) && getEntityItem.stackSize > 0 && isDead && !worldObj.isRemote) {
			val shred = new EntityItem(worldObj)

			shred func_180432_n this
			shred setEntityItemStack new ItemStack(ItemQuartz, (getEntityItem.stackSize * EntityItemShredder.plateToShardRate).toInt, ItemQuartz.shardsDamage)

			worldObj spawnEntityInWorld shred
			getEntityItem.stackSize = 0
		}
		false
	}
}

object EntityItemShredder extends IConfigurable {
	final var plateToShardRate = 2.5F

	override def load(config: Configuration) =
		plateToShardRate = config.getFloat("EntItShredderPlateToShardRate", Reference.CONFIG_ENTIRY_CATEGORY, plateToShardRate, 0, 10, "Multiplier of original stackSize to stackSize of the shredded one")
}
