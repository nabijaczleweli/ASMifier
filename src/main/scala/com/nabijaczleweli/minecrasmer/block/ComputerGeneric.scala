package com.nabijaczleweli.minecrasmer.block

import java.util.Random

import com.nabijaczleweli.minecrasmer.reference.{Container, Reference}
import cpw.mods.fml.common.Optional
import cpw.mods.fml.relauncher.{Side, SideOnly}
import net.minecraft.block.Block
import net.minecraft.client.renderer.texture.IIconRegister
import net.minecraft.entity.EntityLivingBase
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.{Item, ItemStack}
import net.minecraft.util.{IIcon, MathHelper, MovingObjectPosition}
import net.minecraft.world.World
import net.minecraftforge.common.util.ForgeDirection
import pneumaticCraft.api.block.IPneumaticWrenchable

@Optional.Interface(iface = "pneumaticCraft.api.block.IPneumaticWrenchable", modid = "PneumaticCraft", striprefs = true)
class ComputerGeneric(private final val suffix: String) extends Block(Container.materialComputer) with IPneumaticWrenchable {
	setHardness(.3f) // Glass-like
	setHarvestLevel("wrench", 0)
	setBlockName(Reference.NAMESPACED_PREFIX + "computer" + suffix)

	protected final      val computerFrontIndex  = 0
	protected final      val computerSideIndex   = 1
	protected final      val computerTopIndex    = 2
	protected final      val computerBottomIndex = 3
	@SideOnly(Side.CLIENT)
	protected final lazy val icons               = new Array[IIcon](4)

	@SideOnly(Side.CLIENT)
	override def registerBlockIcons(ir: IIconRegister) {
		icons(computerFrontIndex) = ir registerIcon Reference.NAMESPACED_PREFIX + "computer_front"
		icons(computerSideIndex) = ir registerIcon Reference.NAMESPACED_PREFIX + "computer_side"
		icons(computerTopIndex) = ir registerIcon Reference.NAMESPACED_PREFIX + "computer_top"
		icons(computerBottomIndex) = ir registerIcon Reference.NAMESPACED_PREFIX + "computer_bottom"
	}

	override def getIcon(side: Int, meta: Int) =
		side match {
			case 0 =>
				icons(computerBottomIndex)
			case 1 =>
				icons(computerTopIndex)
			case `meta` =>
				icons(computerFrontIndex)
			case _ =>
				icons(computerSideIndex)
		}

	override def onBlockPlacedBy(world: World, x: Int, y: Int, z: Int, entity: EntityLivingBase, stack: ItemStack) =
		world.setBlockMetadataWithNotify(x, y, z, MathHelper.floor_double((entity.rotationYaw * 4F / 360F).toDouble + .5D) & 3 match {
			case 0 =>
				2
			case 1 =>
				5
			case 2 =>
				3
			case 3 =>
				4
		}, 2)

	override def getItemDropped(meta: Int, rand: Random, fortune: Int) =
		Item getItemFromBlock BlockComputerOff

	override def getPickBlock(target: MovingObjectPosition, world: World, x: Int, y: Int, z: Int) =
		new ItemStack(Item getItemFromBlock BlockComputerOff)

	override def canSilkHarvest =
		false

	override def getValidRotations(worldObj: World, x: Int, y: Int, z: Int) =
		ComputerGeneric.sideRotations

	@Optional.Method(modid = "PneumaticCraft")
	override def rotateBlock(world: World, player: EntityPlayer, x: Int, y: Int, z: Int, side: ForgeDirection) =
		rotateBlock(world, x, y, z, side, invert = player.isSneaking)

	override def rotateBlock(worldObj: World, x: Int, y: Int, z: Int, axis: ForgeDirection) =
		rotateBlock(worldObj, x, y, z, axis, invert = false)

	def rotateBlock(world: World, x: Int, y: Int, z: Int, side: ForgeDirection, invert: Boolean) =
		if(!world.isRemote)
			ComputerGeneric.sideToMeta get side match {
				case None =>
					false
				case Some(meta) =>
					world.setBlockMetadataWithNotify(x, y, z, if(invert) meta ^ 1 else meta, 1 | 2)
					true
			}
		else
			false
}

object ComputerGeneric {
	import net.minecraftforge.common.util.ForgeDirection._

	val sideRotations = Array(EAST, NORTH, SOUTH, WEST)
	val sideToMeta    = Map(
		WEST -> 4,
		NORTH -> 2,
		EAST -> 5,
		SOUTH -> 3
	)
}
