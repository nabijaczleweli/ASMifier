package com.nabijaczleweli.minecrasmer.block

import com.nabijaczleweli.minecrasmer.MineCrASMer
import com.nabijaczleweli.minecrasmer.entity.tile.TileEntityAdditionalCPU
import com.nabijaczleweli.minecrasmer.render.gui.GUIAdditionalCPU
import net.minecraft.block.ITileEntityProvider
import net.minecraft.block.state.IBlockState
import net.minecraft.entity.EntityLivingBase
import net.minecraft.entity.item.EntityItem
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.ItemStack
import net.minecraft.nbt.NBTTagCompound
import net.minecraft.util.{BlockPos, EnumFacing}
import net.minecraft.world.World

import scala.util.Random

object BlockAdditionalCPU extends AccessoryGeneric("processor") with ITileEntityProvider {
	private val rand = new Random

	//setBlockTextureName(Reference.NAMESPACED_PREFIX + "processor")

	override def onBlockActivated(worldIn: World, pos: BlockPos, state: IBlockState, playerIn: EntityPlayer, side: EnumFacing, hitX: Float, hitY: Float, hitZ: Float) = {
		playerIn.openGui(MineCrASMer, GUIAdditionalCPU.id, worldIn, pos.getX, pos.getY, pos.getZ)
		true
	}

	override def breakBlock(worldIn: World, pos: BlockPos, state: IBlockState) {
		val te = worldIn.getTileEntity(pos).asInstanceOf[TileEntityAdditionalCPU]
		if(te != null)
			for(is <- 0 until te.getSizeInventory map {te.getStackInSlot} filter {_ != null}) {
				def entOffset =
					rand.nextFloat() * .8F + .1F
				def entMotion =
					rand.nextGaussian().toFloat * .05F

				val entityitem = new EntityItem(worldIn, (pos.getX.toFloat + entOffset).toDouble, (pos.getY.toFloat + entOffset).toDouble, (pos.getZ.toFloat + entOffset).toDouble, is)
				if(is.hasTagCompound)
					entityitem.getEntityItem.setTagCompound(is.getTagCompound.copy.asInstanceOf[NBTTagCompound])
				entityitem.motionX = entMotion.toDouble
				entityitem.motionY = (entMotion + .2F).toDouble
				entityitem.motionZ = entMotion.toDouble
				worldIn spawnEntityInWorld entityitem
			}
	}

	override def onBlockPlacedBy(worldIn: World, pos: BlockPos, state: IBlockState, placer: EntityLivingBase, stack: ItemStack) {
		super.onBlockPlacedBy(worldIn, pos, state, placer, stack)
		if(stack.hasDisplayName)
			worldIn.getTileEntity(pos).asInstanceOf[TileEntityAdditionalCPU] setCustomName stack.getDisplayName
	}


	override def createNewTileEntity(world: World, meta: Int) =
		new TileEntityAdditionalCPU
}
