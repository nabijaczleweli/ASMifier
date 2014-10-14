package com.nabijaczleweli.minecrasmer.block

import com.nabijaczleweli.minecrasmer.MineCrASMer
import com.nabijaczleweli.minecrasmer.entity.tile.TileEntityAdditionalCPU
import com.nabijaczleweli.minecrasmer.reference.Reference
import com.nabijaczleweli.minecrasmer.render.gui.GUIAdditionalCPU
import net.minecraft.block.{Block, ITileEntityProvider}
import net.minecraft.entity.EntityLivingBase
import net.minecraft.entity.item.EntityItem
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.ItemStack
import net.minecraft.nbt.NBTTagCompound
import net.minecraft.world.World

import scala.util.Random

object BlockAdditionalCPU extends AccessoryGeneric("processor") with ITileEntityProvider {
	private val rand = new Random

	setBlockTextureName(Reference.NAMESPACED_PREFIX + "processor")

	override def onBlockActivated(world: World, x: Int, y: Int, z: Int, player: EntityPlayer, side: Int, clickX: Float, clickY: Float, clickZ: Float) = {
		player.openGui(MineCrASMer, GUIAdditionalCPU.id, world, x, y, z)
		true
	}

	override def breakBlock(world: World, x: Int, y: Int, z: Int, self: Block, meta: Int) {
		val te = world.getTileEntity(x, y, z).asInstanceOf[TileEntityAdditionalCPU]
		if(te != null)
			for(is <- 0 until te.getSizeInventory map {te.getStackInSlot} filter {_ != null}) {
				def entOffset =
					rand.nextFloat() * .8F + .1F
				def entMotion =
					rand.nextGaussian().toFloat * .05F

				val entityitem = new EntityItem(world, (x.toFloat + entOffset).toDouble, (y.toFloat + entOffset).toDouble, (z.toFloat + entOffset).toDouble, is)
				if(is.hasTagCompound)
					entityitem.getEntityItem.setTagCompound(is.getTagCompound.copy.asInstanceOf[NBTTagCompound])
				entityitem.motionX = entMotion.toDouble
				entityitem.motionY = (entMotion + .2F).toDouble
				entityitem.motionZ = entMotion.toDouble
				world spawnEntityInWorld entityitem
			}
	}

	override def onBlockPlacedBy(world: World, x: Int, y: Int, z: Int, entity: EntityLivingBase, is: ItemStack) {
		super.onBlockPlacedBy(world, x, y, z, entity, is)
		if(is.hasDisplayName)
			world.getTileEntity(x, y, z).asInstanceOf[TileEntityAdditionalCPU] setCustomName is.getDisplayName
	}


	override def createNewTileEntity(world: World, meta: Int) =
		new TileEntityAdditionalCPU
}
