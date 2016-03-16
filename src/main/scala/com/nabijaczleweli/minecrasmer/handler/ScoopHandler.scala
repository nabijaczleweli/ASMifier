package com.nabijaczleweli.minecrasmer.handler

import com.nabijaczleweli.minecrasmer.item.ItemScoop
import net.minecraft.block.BlockLiquid
import net.minecraft.block.material.Material
import net.minecraft.block.properties.IProperty
import net.minecraft.item.ItemStack
import net.minecraft.util.MovingObjectPosition
import net.minecraft.world.World
import net.minecraftforge.fluids._

object ScoopHandler {
	def fillScoop(world: World, pos: MovingObjectPosition): ItemStack = {
		var blockPos = pos.getBlockPos
		var block = world.getBlockState(blockPos).getBlock
		var fluid = FluidRegistry lookupFluidForBlock block

		world getTileEntity blockPos match {
			case null =>
			case te: IFluidHandler =>
				te getTankInfo pos.sideHit match {
					case null =>
					case ti if ti.nonEmpty =>
						fluid = ti(0).fluid.getFluid
						if(te.canDrain(pos.sideHit, fluid))
							te.drain(pos.sideHit, ItemScoop.capacity, false) match {
								case stack if stack.amount == ItemScoop.capacity =>
									te.drain(pos.sideHit, ItemScoop.capacity, true)
									return ItemScoop scoopWith fluid
								case _ =>
							}
					case _ =>
				}
			case _ =>
		}

		if(fluid == null) {
			blockPos = blockPos offset pos.sideHit
			block = world.getBlockState(blockPos).getBlock
			fluid = FluidRegistry lookupFluidForBlock block
			if(fluid == null)
				return null
		}

		(world getBlockState blockPos getValue BlockLiquid.LEVEL).asInstanceOf[Number].intValue match {
			case 0 =>
				world setBlockToAir blockPos
			case cur =>
				world.setBlockState(blockPos, (world getBlockState blockPos).withProperty(BlockLiquid.LEVEL: IProperty[Integer], cur - 1: Integer), 1 | 2)
		}
		ItemScoop scoopWith fluid
	}

	def emptyScoop(world: World, pos: MovingObjectPosition, scoop: ItemStack): ItemStack = {
		var blockPos = pos.getBlockPos
		var block = world.getBlockState(blockPos).getBlock
		val scoopContains = ItemScoop contains scoop
		def state = world getBlockState blockPos

		world getTileEntity blockPos match {
			case null =>
			case te: IFluidHandler =>
				te getTankInfo pos.sideHit match {
					case null =>
					case ti if ti.nonEmpty =>
						val fluid = ti(0).fluid.getFluid
						if(fluid == (ItemScoop fluid scoop) || fluid == null)
							if(te.canFill(pos.sideHit, fluid)) {
								val fs = new FluidStack(fluid, ItemScoop.capacity)
								te.fill(pos.sideHit, fs, false) match {
									case ItemScoop.capacity =>
										te.fill(pos.sideHit, fs, true)
										return ItemScoop.scoopEmpty
									case _ =>
								}
							}
					case _ =>
				}
			case _ =>
		}

		if(block == scoopContains) {
			world.setBlockState(blockPos, state.withProperty(BlockLiquid.LEVEL: IProperty[Integer], (state getValue BlockLiquid.LEVEL).asInstanceOf[Number].intValue + 1: Integer))
			ItemScoop.scoopEmpty
		} else {
			blockPos = blockPos offset pos.sideHit
			block = world.getBlockState(blockPos).getBlock
			if(block == scoopContains) {
				world.setBlockState(blockPos, state.withProperty(BlockLiquid.LEVEL: IProperty[Integer], (state getValue BlockLiquid.LEVEL).asInstanceOf[Number].intValue + 1: Integer))
				ItemScoop.scoopEmpty
			} else if(block.getMaterial != Material.air)
				null
			else {
				world.setBlockState(blockPos, scoopContains.getActualState(state, world, blockPos), 1 | 2)
				ItemScoop.scoopEmpty
			}
		}
	}
}
