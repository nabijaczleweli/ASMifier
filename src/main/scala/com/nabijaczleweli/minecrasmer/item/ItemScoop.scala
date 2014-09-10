package com.nabijaczleweli.minecrasmer.item

import com.nabijaczleweli.minecrasmer.creativetab.CreativeTabMineCrASMer
import com.nabijaczleweli.minecrasmer.handler.ScoopHandler
import com.nabijaczleweli.minecrasmer.reference.Container.log
import com.nabijaczleweli.minecrasmer.reference.{Container, Reference}
import com.nabijaczleweli.minecrasmer.util.StringUtils._
import net.minecraft.block.Block
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.init.Blocks
import net.minecraft.item.{Item, ItemBucket, ItemStack}
import net.minecraft.util.{MovingObjectPosition, StatCollector}
import net.minecraft.world.World
import net.minecraftforge.fluids.FluidContainerRegistry

class ItemScoop(val contains: Block, val color: Int) extends ItemBucket(contains) {
	val empty = contains == Blocks.air
	protected lazy val containsDisplayName = Item getItemFromBlock contains match {
		case null =>
			log error s"Someone created a scoop with an unregistered block inside! Block name: ${contains.getUnlocalizedName}"
			"<null>"
		case it =>
			it getItemStackDisplayName new ItemStack(it)
	}

	setUnlocalizedName(s"${Reference.NAMESPACED_PREFIX}scoop${if(empty) "Empty" else {contains.getUnlocalizedName substring 5 substring ":" toUpper 0}}")
	setCreativeTab(CreativeTabMineCrASMer)
	setMaxStackSize(1)
	if(!empty)
		setContainerItem(Container.scoopEmpty)
	setTextureName(Reference.NAMESPACED_PREFIX + "scoop_empty")

	override def getItemStackDisplayName(is: ItemStack) =
		if(empty)
			super.getItemStackDisplayName(is)
		else
			StatCollector.translateToLocalFormatted(s"item.${Reference.NAMESPACED_PREFIX}scoopFilled.name", containsDisplayName)

	override def onItemRightClick(is: ItemStack, world: World, player: EntityPlayer) = {
		val mop = getMovingObjectPositionFromPlayer(world, player, empty)
		if(mop == null)
			is
		else {
			val processedIs =
				mop.typeOfHit match {
					case MovingObjectPosition.MovingObjectType.MISS | MovingObjectPosition.MovingObjectType.ENTITY =>
						is
					case MovingObjectPosition.MovingObjectType.BLOCK =>
						if(empty) {
							val resultItemStack = ScoopHandler.fillScoop(world, mop)
							if(resultItemStack == null)
								is
							else
								resultItemStack
						} else {
							val resultItemStack = ScoopHandler.emptyScoop(world, mop, is)
							if(resultItemStack == null)
								is
							else
								resultItemStack
						}
				}
			if(player.capabilities.isCreativeMode)
				is
			else
				processedIs
		}
	}

	override def getMaxDamage =
		0

	override def getColorFromItemStack(is: ItemStack, pass: Int) =
		if(empty)
			super.getColorFromItemStack(is, pass)
		else
			color
}

object ItemScoop {
	val capacity = FluidContainerRegistry.BUCKET_VOLUME / 7
}
