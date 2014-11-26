package com.nabijaczleweli.minecrasmer.block

import com.nabijaczleweli.minecrasmer.creativetab.CreativeTabMineCrASMer
import com.nabijaczleweli.minecrasmer.reference.{Container, Reference}
import net.minecraft.block.Block
import net.minecraft.block.properties.PropertyDirection
import net.minecraft.block.state.IBlockState
import net.minecraft.entity.EntityLivingBase
import net.minecraft.util.{BlockPos, EnumFacing}
import net.minecraft.world.{IBlockAccess, World}
import net.minecraftforge.fml.relauncher.{Side, SideOnly}

//@Optional.Interface(iface = "pneumaticCraft.api.block.IPneumaticWrenchable", modid = "PneumaticCraft", striprefs = true)
class AccessoryGeneric(suffix: String) extends Block(Container.materialComputer) /*with IPneumaticWrenchable*/ {
	import com.nabijaczleweli.minecrasmer.block.AccessoryGeneric.FACING

	setHardness(.3f) // Glass-like
	setHarvestLevel("wrench", 0)
	setUnlocalizedName(s"${Reference.NAMESPACED_PREFIX}accessory_$suffix")
	setCreativeTab(CreativeTabMineCrASMer)
	setDefaultState(blockState.getBaseState.withProperty(FACING, EnumFacing.NORTH))

	/*@SideOnly(Side.CLIENT)
	protected final lazy val icons = new Array[IIcon](1)*/

	override def getCollisionBoundingBox(worldIn: World, pos: BlockPos, state: IBlockState) = {
		setBlockBoundsBasedOnState(worldIn, pos)
		super.getCollisionBoundingBox(worldIn, pos, state)
	}

	@SideOnly(Side.CLIENT)
	override def getSelectedBoundingBox(world: World, pos: BlockPos) = {
		setBlockBoundsBasedOnState(world, pos)
		super.getSelectedBoundingBox(world, pos)
	}

	override def setBlockBoundsBasedOnState(world: IBlockAccess, pos: BlockPos) {
		super.setBlockBoundsBasedOnState(world, pos)
		world getBlockState pos match {
			case 0 => // Down
				setBlockBounds(0, 0, 0, 1, .2f, 1)
			case 1 => // Up
				setBlockBounds(0, .8f, 0, 1, 1, 1)
			case 2 => // North
				setBlockBounds(0, 0, 0, 1, 1, .2f)
			case 3 => // South
				setBlockBounds(0, 0, .8f, 1, 1, 1)
			case 4 => // West
				setBlockBounds(0, 0, 0, .2f, 1, 1)
			case 5 => // East
				setBlockBounds(.8f, 0, 0, 1, 1, 1)
			case _ =>
		}
	}

	override def setBlockBoundsForItemRender() =
		setBlockBounds(0, 0, .8f, 1, 1, 1)

	override def onBlockPlaced(worldIn: World, pos: BlockPos, facing: EnumFacing, hitX: Float, hitY: Float, hitZ: Float, meta: Int, placer: EntityLivingBase) =
		getDefaultState.withProperty(FACING, placer.func_174811_aO.getOpposite)

	override def isOpaqueCube =
		false

	/*override def renderAsNormalBlock =
		false

	@SideOnly(Side.CLIENT)
	override def registerBlockIcons(register: IIconRegister) {
		super.registerBlockIcons(register)
		icons(0) = register registerIcon s"${Reference.NAMESPACED_PREFIX}accessory_side"
	}

	@SideOnly(Side.CLIENT)
	override def getIcon(side: Int, meta: Int) =
		if((side >> 1) == (meta >> 1))
			blockIcon
		else
			icons(0)*/

	/*@Optional.Method(modid = "PneumaticCraft")
	override def rotateBlock(world: World, player: EntityPlayer, x: Int, y: Int, z: Int, side: ForgeDirection) =
		rotateBlock(world, x, y, z, side, invert = player.isSneaking)*/
}

object AccessoryGeneric {
	val FACING = PropertyDirection create "facing"
}
