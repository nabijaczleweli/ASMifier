package com.nabijaczleweli.minecrasmer.block

import com.nabijaczleweli.minecrasmer.creativetab.CreativeTabMineCrASMer
import com.nabijaczleweli.minecrasmer.reference.Reference
import cpw.mods.fml.relauncher.{Side, SideOnly}
import net.minecraft.block.Block
import net.minecraft.block.material.Material
import net.minecraft.client.renderer.texture.IIconRegister
import net.minecraft.entity.EntityLivingBase
import net.minecraft.item.ItemStack
import net.minecraft.util.{IIcon, MathHelper}
import net.minecraft.world.World

object BlockComputerOff extends Block(Material.piston) {
	setBlockName(Reference.NAMESPACED_PREFIX + "computer")
	setCreativeTab(CreativeTabMineCrASMer)
	setHardness(.3f) // Glass-like
	setHarvestLevel("wrench", 0)

	@SideOnly(Side.CLIENT)
	var computerFront: IIcon = null
	@SideOnly(Side.CLIENT)
	var computerSide: IIcon = null
	@SideOnly(Side.CLIENT)
	var computerTopBottom: IIcon = null

	@SideOnly(Side.CLIENT)
	override def registerBlockIcons(ir: IIconRegister) {
		computerFront     = ir registerIcon Reference.NAMESPACED_PREFIX + "computer_front"
		computerSide      = ir registerIcon Reference.NAMESPACED_PREFIX + "computer_side"
		computerTopBottom = ir registerIcon Reference.NAMESPACED_PREFIX + "computer_top_bottom"
	}

	override def getIcon(side: Int, meta: Int) =
		side match {
			case 1 | 0 => // top / bottom
				computerTopBottom
			case `meta` =>
				computerFront
			case _ =>
				computerSide
		}

	override def onBlockPlacedBy(world: World, x: Int, y: Int, z: Int, entity: EntityLivingBase, stack: ItemStack) =
		MathHelper.floor_double((entity.rotationYaw * 4.0F / 360.0F).asInstanceOf[Double] + 0.5D) & 3 match {  // Stolen from BlockFurnace
			case 0 =>
				world.setBlockMetadataWithNotify(x, y, z, 2, 2)
			case 1 =>
				world.setBlockMetadataWithNotify(x, y, z, 5, 2)
			case 2 =>
				world.setBlockMetadataWithNotify(x, y, z, 3, 2)
			case 3 =>
				world.setBlockMetadataWithNotify(x, y, z, 4, 2)
	}
}
