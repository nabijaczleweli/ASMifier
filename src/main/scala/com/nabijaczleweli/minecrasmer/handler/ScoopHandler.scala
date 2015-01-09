package com.nabijaczleweli.minecrasmer.handler

import com.nabijaczleweli.minecrasmer.block.BlockLiquidCrystalFluid
import com.nabijaczleweli.minecrasmer.item.ItemScoop
import com.nabijaczleweli.minecrasmer.reference.Container
import net.minecraft.block.material.Material
import net.minecraft.block.{Block, BlockLiquid}
import net.minecraft.init.Blocks
import net.minecraft.item.{Item, ItemStack}
import net.minecraft.util.MovingObjectPosition
import net.minecraft.world.World
import net.minecraftforge.fluids._

import scala.collection.immutable.HashMap

object ScoopHandler {
	var scoops = HashMap[Block, Item] (
		Blocks.air -> Container.scoopEmpty,
		BlockLiquidCrystalFluid -> Container.scoopLiquidCrystal
	)

	def fillScoop(world: World, pos: MovingObjectPosition): ItemStack = {
		var blockPos = pos.func_178782_a // getBlockPos
		var block = world.getBlockState(blockPos).getBlock
		var scoop = scoops get block

		world getTileEntity blockPos match {
			case null =>
			case te: IFluidHandler =>
				te getTankInfo pos.field_178784_b match { // sideHit
					case null =>
					case ti if ti.length > 0 =>
						val fluid = ti(0).fluid.getFluid
						scoop = scoops get fluid.getBlock
						if(scoop.isDefined)
							if(te.canDrain(pos.field_178784_b, fluid)) // sideHit
								te.drain(pos.field_178784_b, ItemScoop.capacity, false) match { // sideHit
									case stack if stack.amount == ItemScoop.capacity =>
										te.drain(pos.field_178784_b, ItemScoop.capacity, true) // sideHit
										return new ItemStack(scoop.get)
									case _ =>
								}
					case _ =>
				}
			case _ =>
		}

		if(scoop.isEmpty) {
			blockPos = blockPos offset pos.field_178784_b // sideHit
			block = world.getBlockState(blockPos).getBlock
			scoop = scoops get block
			if(scoop.isEmpty)
				return null
		}

		(world getBlockState blockPos getValue BlockLiquid.LEVEL).asInstanceOf[Number].intValue match {
			case 0 =>
				world setBlockToAir blockPos
			case cur =>
				world.setBlockState(blockPos, (world getBlockState blockPos).withProperty(BlockLiquid.LEVEL, cur - 1), 1 | 2)
		}
		new ItemStack(scoop.get)
	}

	def emptyScoop(world: World, pos: MovingObjectPosition, scoop: ItemStack): ItemStack = {
		var blockPos = pos.func_178782_a // getBlockPos
		var block = world.getBlockState(blockPos).getBlock
		val scoopItem = scoop.getItem.asInstanceOf[ItemScoop]
		val scoopContains = scoopItem.contains
		def state = world getBlockState blockPos

		world getTileEntity blockPos match {
			case null =>
			case te: IFluidHandler =>
				te getTankInfo pos.field_178784_b match { // sideHit
					case null =>
					case ti if ti.length > 0 =>
						val fluid = ti(0).fluid.getFluid
						if(fluid == scoopItem.fluid || fluid == null)
							if(te.canFill(pos.field_178784_b, fluid)) { // sideHit
								val fs = new FluidStack(fluid, ItemScoop.capacity)
								te.fill(pos.field_178784_b, fs, false) match { // sideHit
									case ItemScoop.capacity =>
										te.fill(pos.field_178784_b, fs, true) // sideHit
										return new ItemStack(scoopItem.getContainerItem, 1, 0)
									case _ =>
								}
							}
					case _ =>
				}
			case _ =>
		}

		if(block == scoopContains) {
			world.setBlockState(blockPos, state.withProperty(BlockLiquid.LEVEL, (state getValue BlockLiquid.LEVEL).asInstanceOf[Number].intValue + 1))
			new ItemStack(scoopItem.getContainerItem, 1, 0)
		} else {
			blockPos = blockPos offset pos.field_178784_b // sideHit
			block = world.getBlockState(blockPos).getBlock
			if(block == scoopContains) {
				world.setBlockState(blockPos, state.withProperty(BlockLiquid.LEVEL, (state getValue BlockLiquid.LEVEL).asInstanceOf[Number].intValue + 1))
				new ItemStack(scoopItem.getContainerItem, 1, 0)
			} else if(block.getMaterial != Material.air)
				null
			else {
				world.setBlockState(blockPos, scoopContains.getActualState(state, world, blockPos), 1 | 2)
				new ItemStack(scoopItem.getContainerItem, 1, 0)
			}
		}
	}
}
