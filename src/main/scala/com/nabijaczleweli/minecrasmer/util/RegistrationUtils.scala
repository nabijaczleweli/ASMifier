package com.nabijaczleweli.minecrasmer.util

import com.nabijaczleweli.minecrasmer.item.ItemScoop
import com.nabijaczleweli.minecrasmer.reference.Container
import com.nabijaczleweli.minecrasmer.util.StringUtils._
import net.minecraft.block.Block
import net.minecraft.item.crafting.IRecipe
import net.minecraft.item.{Item, ItemStack}
import net.minecraftforge.fluids.{FluidContainerRegistry, FluidRegistry, IFluidBlock}
import net.minecraftforge.fml.common.registry.GameRegistry

object RegistrationUtils {
	implicit class IRecipeUtils(val recipe: IRecipe) extends AnyVal {
		@inline
		def register() =
			GameRegistry addRecipe recipe
	}

	implicit class BlockUtils(val block: Block) extends AnyVal {
		@inline
		def register() =
			GameRegistry.registerBlock(block, block.getUnlocalizedName substring ":")
	}

	implicit class ItemUtils(val item: Item) extends AnyVal {
		@inline
		def register() =
			GameRegistry.registerItem(item, item.getUnlocalizedName substring ":")
	}

	implicit class ScoopUtils(val item: ItemScoop) extends AnyVal {
		def register() {
			(item: Item).register()
			item.contains match {
				case contains: IFluidBlock if !item.empty =>
					FluidContainerRegistry.registerFluidContainer(FluidRegistry.getFluidStack(contains.getFluid.getName, ItemScoop.capacity), new ItemStack(item), new ItemStack(Container.scoopEmpty))
				case _ =>
			}
		}
	}
}
