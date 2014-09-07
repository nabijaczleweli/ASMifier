package com.nabijaczleweli.minecrasmer.item

import com.nabijaczleweli.minecrasmer.creativetab.CreativeTabMineCrASMer
import com.nabijaczleweli.minecrasmer.handler.ScoopHandler
import com.nabijaczleweli.minecrasmer.reference.{Container, Reference}
import net.minecraft.block.Block
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.init.Blocks
import net.minecraft.item.{Item, ItemBucket, ItemStack}
import net.minecraft.util.MovingObjectPosition
import net.minecraft.world.World
import net.minecraftforge.fluids.FluidContainerRegistry
import com.nabijaczleweli.minecrasmer.reference.Container.log

class ItemScoop(val contains: Block) extends ItemBucket(contains) {
	val empty = contains == Blocks.air
	protected lazy val containsDisplayName = Item getItemFromBlock contains match {
		case null =>
			log error s"Someone registered a scoop with an unregistered block inside! Block name: ${contains.getUnlocalizedName}"
			"<null>"
		case it =>
			it getItemStackDisplayName new ItemStack(it)
	}

	println(s"scoop${if(empty) "Empty" else {
		val bldr = new StringBuilder(contains.getUnlocalizedName substring 5)
		bldr.setCharAt(0, bldr.charAt(0).toUpper)
		bldr
	}}")
	setUnlocalizedName(s"scoop${if(empty) "Empty" else {contains.getUnlocalizedName.charAt(5).toUpper + contains.getUnlocalizedName substring 6}}")
	setCreativeTab(CreativeTabMineCrASMer)
	setMaxStackSize(1)
	if(!empty)
		setContainerItem(Container.scoopEmpty)  // This requires empty scoop to be created before any others!
	setTextureName(Reference.NAMESPACED_PREFIX + s"${if(empty) "empty" else "full"}scoop")

	override def getItemStackDisplayName(is: ItemStack) =
		if(empty)
			"Empty scoop"
		else
			s"Scoop with $containsDisplayName"

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
}

object ItemScoop {
	val capacity = FluidContainerRegistry.BUCKET_VOLUME / 7
}
