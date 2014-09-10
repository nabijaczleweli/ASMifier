package com.nabijaczleweli.minecrasmer.handler

import com.nabijaczleweli.minecrasmer.block.BlockLiquidCrystalFluid
import com.nabijaczleweli.minecrasmer.item.ItemScoop
import com.nabijaczleweli.minecrasmer.reference.Container
import net.minecraft.block.Block
import net.minecraft.block.material.Material
import net.minecraft.init.Blocks
import net.minecraft.item.{Item, ItemStack}
import net.minecraft.util.MovingObjectPosition
import net.minecraft.world.World
import net.minecraftforge.common.util.ForgeDirection
import net.minecraftforge.fluids._

object ScoopHandler {
	var scoops = Map[Block, Item](
		Blocks.air -> Container.scoopEmpty,
		BlockLiquidCrystalFluid -> Container.scoopLiquidCrystal
	)

	def fillScoop(world: World, pos: MovingObjectPosition): ItemStack = {
		var posModifX = 0
		var posModifY = 0
		var posModifZ = 0
		var block = world.getBlock(pos.blockX, pos.blockY, pos.blockZ)
		var scoop = scoops get block

		world.getTileEntity(pos.blockX, pos.blockY, pos.blockZ) match {
			case null =>
			case te: IFluidHandler =>
				val direction = ForgeDirection getOrientation pos.sideHit
				te getTankInfo direction match {
					case null =>
					case ti if ti.length > 0 =>
						val fluid = ti(0).fluid.getFluid
						scoop = scoops get fluid.getBlock
						if(scoop.isDefined)
							if(te.canDrain(direction, fluid))
								te.drain(direction, ItemScoop.capacity, false) match {
									case stack if stack.amount == ItemScoop.capacity =>
										te.drain(direction, ItemScoop.capacity, true)
										return new ItemStack(scoop.get)
									case _ =>
								}
					case _ =>
				}
			case _ =>
		}

		if(scoop.isEmpty) {
			pos.sideHit match {
				case 0 =>
					posModifY = -1
				case 1 =>
					posModifY = 1
				case 2 =>
					posModifZ = -1
				case 3 =>
					posModifZ = 1
				case 4 =>
					posModifX = -1
				case 5 =>
					posModifX = 1
				case _ =>
			}
			block = world.getBlock(pos.blockX + posModifX, pos.blockY + posModifY, pos.blockZ + posModifZ)
			scoop = scoops get block
			if(scoop.isEmpty)
				return null
		}

		val metadata = world.getBlockMetadata(pos.blockX + posModifX, pos.blockY + posModifY, pos.blockZ + posModifZ)
		if(metadata == 0)
			world.setBlockToAir(pos.blockX + posModifX, pos.blockY + posModifY, pos.blockZ + posModifZ)
		else
			world.setBlockMetadataWithNotify(pos.blockX + posModifX, pos.blockY + posModifY, pos.blockZ + posModifZ, metadata - 1, 1 | 2)
		new ItemStack(scoop.get)
	}

	def emptyScoop(world: World, pos: MovingObjectPosition, scoop: ItemStack): ItemStack = {
		var block = world.getBlock(pos.blockX, pos.blockY, pos.blockZ)
		val scoopItem = scoop.getItem.asInstanceOf[ItemScoop]
		val scoopContains = scoopItem.contains
		val addMeta = (modX: Int, modY: Int, modZ: Int) => {
			val metadata = world.getBlockMetadata(pos.blockX + modX, pos.blockY + modY, pos.blockZ + modZ)
			if(metadata == 7)
				null
			else {
				world.setBlockMetadataWithNotify(pos.blockX + modX, pos.blockY + modY, pos.blockZ + modZ, metadata + 1, 1 | 2)
				new ItemStack(scoopItem.getContainerItem, 1, 0)
			}
		}

		world.getTileEntity(pos.blockX, pos.blockY, pos.blockZ) match {
			case null =>
			case te: IFluidHandler =>
				val direction = ForgeDirection getOrientation pos.sideHit
				te getTankInfo direction match {
					case null =>
					case ti if ti.length > 0 =>
						val fluid = ti(0).fluid.getFluid
						if(fluid == scoopItem.fluid || fluid == null)
							if(te.canFill(direction, fluid)) {
								val fs = new FluidStack(fluid, ItemScoop.capacity)
								te.fill(direction, fs, false) match {
									case ItemScoop.capacity =>
										te.fill(direction, fs, true)
										return new ItemStack(scoopItem.getContainerItem, 1, 0)
									case _ =>
								}
							}
					case _ =>
				}
			case _ =>
		}

		if(block == scoopContains)
			addMeta(0, 0, 0)
		else {
			var posModifX = 0
			var posModifY = 0
			var posModifZ = 0
			pos.sideHit match {
				case 0 =>
					posModifY = -1
				case 1 =>
					posModifY = 1
				case 2 =>
					posModifZ = -1
				case 3 =>
					posModifZ = 1
				case 4 =>
					posModifX = -1
				case 5 =>
					posModifX = 1
				case _ =>
			}
			block = world.getBlock(pos.blockX + posModifX, pos.blockY + posModifY, pos.blockZ + posModifZ)
			if(block == scoopContains)
				addMeta(posModifX, posModifY, posModifZ)
			else if(block.getMaterial != Material.air)
				null
			else {
				world.setBlock(pos.blockX + posModifX, pos.blockY + posModifY, pos.blockZ + posModifZ, scoopContains, 0, 1 | 2)
				new ItemStack(scoopItem.getContainerItem, 1, 0)
			}
		}
	}
}
