package com.nabijaczleweli.minecrasmer.block

import java.util.Random

import com.nabijaczleweli.minecrasmer.reference.{Container, Reference}
import cpw.mods.fml.relauncher.{Side, SideOnly}
import net.minecraft.block.Block
import net.minecraft.client.renderer.texture.IIconRegister
import net.minecraft.entity.EntityLivingBase
import net.minecraft.item.{Item, ItemStack}
import net.minecraft.util.{IIcon, MathHelper}
import net.minecraft.world.World

class ComputerGeneric(private final val suffix: String) extends Block(Container.materialComputer) {
	setHardness(.3f) // Glass-like
	setHarvestLevel("wrench", 0)
	setBlockName(Reference.NAMESPACED_PREFIX + "computer" + suffix)

	@SideOnly(Side.CLIENT)
	var computerFront: IIcon = null
	@SideOnly(Side.CLIENT)
	var computerSide : IIcon = null
	@SideOnly(Side.CLIENT)
	var computerTop: IIcon = null
	@SideOnly(Side.CLIENT)
	var computerBottom: IIcon = null

	@SideOnly(Side.CLIENT)
	override def registerBlockIcons(ir: IIconRegister) {
		computerFront = ir registerIcon Reference.NAMESPACED_PREFIX + "computer_front"
		computerSide = ir registerIcon Reference.NAMESPACED_PREFIX + "computer_side"
		computerTop = ir registerIcon Reference.NAMESPACED_PREFIX + "computer_top"
		computerBottom = ir registerIcon Reference.NAMESPACED_PREFIX + "computer_bottom"
	}

	override def getIcon(side: Int, meta: Int) =
		side match {
			case 0 =>
				computerBottom
			case 1 =>
				computerTop
			case `meta` =>
				computerFront
			case _ =>
				computerSide
		}

	override def onBlockPlacedBy(world: World, x: Int, y: Int, z: Int, entity: EntityLivingBase, stack: ItemStack) =
		MathHelper.floor_double((entity.rotationYaw * 4.0F / 360.0F).asInstanceOf[Double] + 0.5D) & 3 match {
			case 0 =>
				world.setBlockMetadataWithNotify(x, y, z, 2, 2)
			case 1 =>
				world.setBlockMetadataWithNotify(x, y, z, 5, 2)
			case 2 =>
				world.setBlockMetadataWithNotify(x, y, z, 3, 2)
			case 3 =>
				world.setBlockMetadataWithNotify(x, y, z, 4, 2)
		}

	override def getItemDropped(meta: Int, rand: Random, fortune: Int) =
		Item getItemFromBlock BlockComputerOff

	override def canSilkHarvest =
		false
}
