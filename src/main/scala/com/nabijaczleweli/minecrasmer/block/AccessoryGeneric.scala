package com.nabijaczleweli.minecrasmer.block

import com.nabijaczleweli.minecrasmer.creativetab.CreativeTabMineCrASMer
import com.nabijaczleweli.minecrasmer.reference.{Container, Reference}
import net.minecraft.block.Block
import net.minecraft.block.properties.PropertyDirection
import net.minecraft.block.state.{BlockState, IBlockState}
import net.minecraft.entity.EntityLivingBase
import net.minecraft.util.{BlockPos, EnumFacing}
import net.minecraft.world.{IBlockAccess, World}
import net.minecraftforge.fml.common.Optional
import net.minecraftforge.fml.relauncher.{Side, SideOnly}

@Optional.Interface(iface = "pneumaticCraft.api.block.IPneumaticWrenchable", modid = "PneumaticCraft", striprefs = true)
class AccessoryGeneric(suffix: String) extends Block(Container.materialComputer) /*with IPneumaticWrenchable*/ {
	import com.nabijaczleweli.minecrasmer.block.AccessoryGeneric.FACING

	setHardness(.3f) // Glass-like
	setUnlocalizedName(s"${Reference.NAMESPACED_PREFIX}accessory_$suffix")
	setCreativeTab(CreativeTabMineCrASMer)
	setDefaultState(blockState.getBaseState.withProperty(FACING, EnumFacing.NORTH))
	setHarvestLevel("wrench", 0)

	/*@SideOnly(Side.CLIENT)
	protected final lazy val icons = new Array[IIcon](1)*/

	override def getMetaFromState(state: IBlockState) =
		(state getValue FACING).getIndex

	override def getStateFromMeta(meta: Int) = // Stolen from BlockFurnace
		getDefaultState.withProperty(FACING, (EnumFacing getFront meta).getAxis match {
			case EnumFacing.Axis.Y =>
				EnumFacing.NORTH
			case _ =>
				EnumFacing getFront meta
		})

	override def createBlockState() =
		new BlockState(this, FACING)

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
		actualStateOrDefault(world, pos) getValue FACING match {
			case EnumFacing.DOWN =>
				setBlockBounds(0, 0, 0, 1, .2f, 1)
			case EnumFacing.UP =>
				setBlockBounds(0, .8f, 0, 1, 1, 1)
			case EnumFacing.NORTH =>
				setBlockBounds(0, 0, 0, 1, 1, .2f)
			case EnumFacing.SOUTH =>
				setBlockBounds(0, 0, .8f, 1, 1, 1)
			case EnumFacing.WEST =>
				setBlockBounds(0, 0, 0, .2f, 1, 1)
			case EnumFacing.EAST =>
				setBlockBounds(.8f, 0, 0, 1, 1, 1)
			case _ =>
		}
	}

	override def setBlockBoundsForItemRender() =
		setBlockBounds(0, 0, .8f, 1, 1, 1)

	override def onBlockPlaced(worldIn: World, pos: BlockPos, facing: EnumFacing, hitX: Float, hitY: Float, hitZ: Float, meta: Int, placer: EntityLivingBase) =
		getDefaultState.withProperty(FACING, facing.getOpposite)

	override def isOpaqueCube =
		false

	override def isSideSolid(world: IBlockAccess, pos: BlockPos, side: EnumFacing) =
		(world getBlockState pos getValue FACING) == side

	protected def actualStateOrDefault(world: IBlockAccess, pos: BlockPos) = {
		val t = world getBlockState pos
		if(t.getBlock == this)
			t
		else
			getDefaultState
	}

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
