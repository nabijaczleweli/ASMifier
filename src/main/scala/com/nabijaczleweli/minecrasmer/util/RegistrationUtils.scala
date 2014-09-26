package com.nabijaczleweli.minecrasmer.util

import com.nabijaczleweli.minecrasmer.item.ItemScoop
import com.nabijaczleweli.minecrasmer.reference.Container
import com.nabijaczleweli.minecrasmer.util.StringUtils._
import cpw.mods.fml.common.registry.GameRegistry
import net.minecraft.block.Block
import net.minecraft.item.crafting.IRecipe
import net.minecraft.item.{Item, ItemStack}
import net.minecraftforge.fluids.{FluidContainerRegistry, FluidRegistry, IFluidBlock}

object RegistrationUtils {
	implicit class IRecipeUtils(recipe: IRecipe) {
		@inline
		def register() =
			GameRegistry addRecipe recipe
	}

	implicit class BlockUtils(block: Block) {
		@inline
		def register() =
			GameRegistry.registerBlock(block, block.getUnlocalizedName substring ":")
	}

	implicit class ItemUtils(item: Item) {
		@inline
		def register() =
			GameRegistry.registerItem(item, item.getUnlocalizedName substring ":")
	}

	implicit class ScoopUtils(item: ItemScoop) {
		@inline
		def register() {
			item.asInstanceOf[Item].register()
			if(!item.empty)
				FluidContainerRegistry.registerFluidContainer(FluidRegistry.getFluidStack(item.contains.asInstanceOf[IFluidBlock].getFluid.getName, ItemScoop.capacity), new ItemStack(item),
				                                              new ItemStack(Container.scoopEmpty))
		}
	}
}
