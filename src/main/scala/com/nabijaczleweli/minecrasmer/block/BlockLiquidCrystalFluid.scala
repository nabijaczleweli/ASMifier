package com.nabijaczleweli.minecrasmer.block

import com.nabijaczleweli.minecrasmer.reference.{Container, Reference}
import cpw.mods.fml.relauncher.{Side, SideOnly}
import net.minecraft.block.material.Material
import net.minecraft.client.renderer.texture.IIconRegister
import net.minecraft.util.IIcon
import net.minecraft.world.{IBlockAccess, World}
import net.minecraftforge.fluids.BlockFluidFinite

object BlockLiquidCrystalFluid extends BlockFluidFinite(Container.liquidCrystal, Material.water) {
	private final      val stillIconIndex   = 0
	private final      val flowingIconIndex = 1
	@SideOnly(Side.CLIENT)
	private final lazy val icons            = new Array[IIcon](2)

	setBlockName(Reference.NAMESPACED_PREFIX + "liquidcrystal")

	override def getIcon(side: Int, meta: Int) =
		if(side == 0 || side == 1)
			icons(stillIconIndex)
		else
			icons(flowingIconIndex)

	override def registerBlockIcons(register: IIconRegister) {
		icons(stillIconIndex) = register registerIcon Reference.NAMESPACED_PREFIX + "liquidcrystal_still"
		icons(flowingIconIndex) = register registerIcon Reference.NAMESPACED_PREFIX + "liquidcrystal_flow"
	}

	override def canDisplace(world: IBlockAccess, x: Int, y: Int, z: Int) =
		if(world.getBlock(x, y, z).getMaterial.isLiquid)
			false
		else
			super.canDisplace(world, x, y, z)

	override def displaceIfPossible(world: World, x: Int, y: Int, z: Int) =
		if(world.getBlock(x, y, z).getMaterial.isLiquid)
			false
		else
			super.displaceIfPossible(world, x, y, z)
}
