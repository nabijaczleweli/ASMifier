package com.nabijaczleweli.minecrasmer.block

import com.nabijaczleweli.minecrasmer.reference.{Container, Reference}
import net.minecraft.block.material.Material
import net.minecraft.util.BlockPos
import net.minecraft.world.{IBlockAccess, World}
import net.minecraftforge.fluids.{BlockFluidBase, BlockFluidFinite}

object BlockLiquidCrystalFluid extends BlockFluidFinite(Container.liquidCrystal, Material.water) {
	/*private final      val stillIconIndex   = 0
	private final      val flowingIconIndex = 1
	@SideOnly(Side.CLIENT)
	private final lazy val icons            = new Array[IIcon](2)*/

	setUnlocalizedName(Reference.NAMESPACED_PREFIX + "liquidcrystal")

	/*override def getIcon(side: Int, meta: Int) =
		if(side == 0 || side == 1)
			icons(stillIconIndex)
		else
			icons(flowingIconIndex)*/

	/*override def registerBlockIcons(register: IIconRegister) {
		icons(stillIconIndex) = register registerIcon Reference.NAMESPACED_PREFIX + "liquidcrystal_still"
		icons(flowingIconIndex) = register registerIcon Reference.NAMESPACED_PREFIX + "liquidcrystal_flow"
	}*/

	override def canDisplace(world: IBlockAccess, pos: BlockPos) =
		if(world.getBlockState(pos).getBlock.getMaterial.isLiquid)
			false
		else
			super.canDisplace(world, pos)

	override def displaceIfPossible(world: World, pos: BlockPos) =
		if(world.getBlockState(pos).getBlock.getMaterial.isLiquid)
			false
		else
			super.displaceIfPossible(world, pos)

	override def getStateFromMeta(meta: Int) =
		getDefaultState.withProperty(BlockFluidBase.LEVEL, meta)
}
