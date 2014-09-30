package com.nabijaczleweli.minecrasmer.block

import com.nabijaczleweli.minecrasmer.creativetab.CreativeTabMineCrASMer
import com.nabijaczleweli.minecrasmer.entity.tile.TileEntityOverclocker
import com.nabijaczleweli.minecrasmer.reference.{Container, Reference}
import net.minecraft.block.{Block, ITileEntityProvider}
import net.minecraft.world.World

object BlockOverclocker extends Block(Container.materialComputer) with ITileEntityProvider {
	setHardness(.3f) // Glass-like
	setHarvestLevel("wrench", 0)
	setBlockName(Reference.NAMESPACED_PREFIX + "overclocker")
	setCreativeTab(CreativeTabMineCrASMer)

	override def createNewTileEntity(world: World, meta: Int) =
		new TileEntityOverclocker
}
