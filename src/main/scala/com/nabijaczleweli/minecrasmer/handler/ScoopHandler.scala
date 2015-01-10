package com.nabijaczleweli.minecrasmer.handler

import com.nabijaczleweli.minecrasmer.item.ItemScoop
import net.minecraft.block.BlockLiquid
import net.minecraft.block.material.Material
import net.minecraft.item.ItemStack
import net.minecraft.util.MovingObjectPosition
import net.minecraft.world.World
import net.minecraftforge.fluids._

object ScoopHandler {
	def fillScoop(world: World, pos: MovingObjectPosition): ItemStack = {
		var blockPos = pos.func_178782_a // getBlockPos
		var block = world.getBlockState(blockPos).getBlock
		var fluid = FluidRegistry lookupFluidForBlock block

		world getTileEntity blockPos match {
			case null =>
			case te: IFluidHandler =>
				te getTankInfo pos.field_178784_b match { // sideHit
					case null =>
					case ti if ti.length > 0 =>
						fluid = ti(0).fluid.getFluid
						if(te.canDrain(pos.field_178784_b, fluid)) // sideHit
							te.drain(pos.field_178784_b, ItemScoop.capacity, false) match { // sideHit
								case stack if stack.amount == ItemScoop.capacity =>
									te.drain(pos.field_178784_b, ItemScoop.capacity, true) // sideHit
									return ItemScoop scoopWith fluid
								case _ =>
							}
					case _ =>
				}
			case _ =>
		}

		if(fluid == null) {
			blockPos = blockPos offset pos.field_178784_b // sideHit
			block = world.getBlockState(blockPos).getBlock
			fluid = FluidRegistry lookupFluidForBlock block
			if(fluid == null)
				return null
		}

		(world getBlockState blockPos getValue BlockLiquid.LEVEL).asInstanceOf[Number].intValue match {
			case 0 =>
				world setBlockToAir blockPos
			case cur =>
				world.setBlockState(blockPos, (world getBlockState blockPos).withProperty(BlockLiquid.LEVEL, cur - 1), 1 | 2)
		}
		ItemScoop scoopWith fluid
	}

	def emptyScoop(world: World, pos: MovingObjectPosition, scoop: ItemStack): ItemStack = {
		var blockPos = pos.func_178782_a // getBlockPos
		var block = world.getBlockState(blockPos).getBlock
		val scoopContains = ItemScoop contains scoop
		def state = world getBlockState blockPos

		world getTileEntity blockPos match {
			case null =>
			case te: IFluidHandler =>
				te getTankInfo pos.field_178784_b match { // sideHit
					case null =>
					case ti if ti.length > 0 =>
						val fluid = ti(0).fluid.getFluid
						if(fluid == (ItemScoop fluid scoop) || fluid == null)
							if(te.canFill(pos.field_178784_b, fluid)) { // sideHit
								val fs = new FluidStack(fluid, ItemScoop.capacity)
								te.fill(pos.field_178784_b, fs, false) match { // sideHit
									case ItemScoop.capacity =>
										te.fill(pos.field_178784_b, fs, true) // sideHit
										return ItemScoop.scoopEmpty
									case _ =>
								}
							}
					case _ =>
				}
			case _ =>
		}

		if(block == scoopContains) {
			world.setBlockState(blockPos, state.withProperty(BlockLiquid.LEVEL, (state getValue BlockLiquid.LEVEL).asInstanceOf[Number].intValue + 1))
			ItemScoop.scoopEmpty
		} else {
			blockPos = blockPos offset pos.field_178784_b // sideHit
			block = world.getBlockState(blockPos).getBlock
			if(block == scoopContains) {
				world.setBlockState(blockPos, state.withProperty(BlockLiquid.LEVEL, (state getValue BlockLiquid.LEVEL).asInstanceOf[Number].intValue + 1))
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
