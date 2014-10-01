package com.nabijaczleweli.minecrasmer.block

import com.nabijaczleweli.minecrasmer.creativetab.CreativeTabMineCrASMer
import com.nabijaczleweli.minecrasmer.reference.{Reference, Container}
import cpw.mods.fml.relauncher.{Side, SideOnly}
import net.minecraft.block.Block
import net.minecraft.client.renderer.texture.IIconRegister
import net.minecraft.util.IIcon
import net.minecraft.world.{IBlockAccess, World}

class AccessoryGeneric(suffix: String) extends Block(Container.materialComputer) {
	setHardness(.3f) // Glass-like
	setHarvestLevel("wrench", 0)
	setBlockName(s"${Reference.NAMESPACED_PREFIX}accessory_$suffix")
	setCreativeTab(CreativeTabMineCrASMer)

	@SideOnly(Side.CLIENT)
	protected final lazy val icons = new Array[IIcon](1)

	override def getCollisionBoundingBoxFromPool(world: World, x: Int, y: Int, z: Int) = {
		setBlockBoundsBasedOnState(world, x, y, z)
		super.getCollisionBoundingBoxFromPool(world, x, y, z)
	}

	@SideOnly(Side.CLIENT)
	override def getSelectedBoundingBoxFromPool(world: World, x: Int, y: Int, z: Int) = {
		setBlockBoundsBasedOnState(world, x, y, z)
		super.getSelectedBoundingBoxFromPool(world, x, y, z)
	}

	override def setBlockBoundsBasedOnState(world: IBlockAccess, x: Int, y: Int, z: Int) {
		super.setBlockBoundsBasedOnState(world, x, y, z)
		world.getBlockMetadata(x, y, z) match {
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

	override def onBlockPlaced(world: World, x: Int, y: Int, z: Int, side: Int, hitX: Float, hitY: Float, hitZ: Float, meta: Int) = {
		super.onBlockPlaced(world, x, y, z, side, hitX, hitY, hitZ, meta)
		// Bottom side -> Upward pointing  Bottom side = 0  Upward pointing = 1
		// Top side -> Downward pointing   Top side = 1     Downward pointing = 0
		// Works for all sides
		side ^ 1
	}

	override def isOpaqueCube =
		false

	override def renderAsNormalBlock =
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
			icons(0)
}
