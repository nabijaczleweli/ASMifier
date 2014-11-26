package com.nabijaczleweli.minecrasmer.block

import java.util.Random

import com.nabijaczleweli.minecrasmer.reference.{Container, Reference}
import net.minecraft.block.Block
import net.minecraft.block.properties.PropertyDirection
import net.minecraft.block.state.IBlockState
import net.minecraft.entity.EntityLivingBase
import net.minecraft.item.{Item, ItemStack}
import net.minecraft.util._
import net.minecraft.world.World

//@Optional.Interface(iface = "pneumaticCraft.api.block.IPneumaticWrenchable", modid = "PneumaticCraft", striprefs = true)
class ComputerGeneric(private final val suffix: String) extends Block(Container.materialComputer) /* with IPneumaticWrenchable*/ {
	import com.nabijaczleweli.minecrasmer.block.ComputerGeneric.FACING

	setHardness(.3f) // Glass-like
	setHarvestLevel("wrench", 0)
	setUnlocalizedName(Reference.NAMESPACED_PREFIX + "computer" + suffix)
	setDefaultState(blockState.getBaseState.withProperty(FACING, EnumFacing.NORTH))

	/*protected final      val computerFrontIndex  = 0
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
		}*/

	override def onBlockPlacedBy(worldIn: World, pos: BlockPos, state: IBlockState, placer: EntityLivingBase, stack: ItemStack) =
		worldIn.setBlockState(pos, state.withProperty(FACING, placer.func_174811_aO.getOpposite), 2)

	override def getItemDropped(state: IBlockState, rand: Random, fortune: Int) =
		Item getItemFromBlock BlockComputerOff

	override def getPickBlock(target: MovingObjectPosition, world: World, pos: BlockPos) =
		new ItemStack(Item getItemFromBlock BlockComputerOff)

	override def canSilkHarvest =
		false

	override def getValidRotations(world: World, pos: BlockPos) =
		EnumFacing.Plane.HORIZONTAL.facings

	/*@Optional.Method(modid = "PneumaticCraft")
	override def rotateBlock(world: World, player: EntityPlayer, x: Int, y: Int, z: Int, side: ForgeDirection) =
		rotateBlock(world, x, y, z, side, invert = player.isSneaking)*/
}

object ComputerGeneric {
	val FACING = PropertyDirection.create("facing", EnumFacing.Plane.HORIZONTAL)
}
